/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// Original query, edited query
var oq, eq;
var ok = false;
var obsrvrStr;
var wzdMode; // One of new, existing or physical
var fromAddVmWzd = false;


// --------------------------------------------------------------------------
function exit(i) {
  if (parent.ctx == "vmAddDeviceWizard") {
    if (i == -1) {
      self.location.replace("/vmware/en/vmAddDeviceWizard.html");
    } else {
      parent.close();
    }
  } else {
    parent.close();
  }
}


// --------------------------------------------------------------------------
function opCb(w) {
  if (w.wrn.length > 0) {
    var s = "One or more warnings were generated:";
    for (var i in w.wrn) {
      s += "\n\n" + w.wrn[i];
    }
    alert(s);
  }

  if (w.err.length > 0) {
    var s = "One or more errors occurred:";
    for (var i in w.err) {
      s += "\n\n" + w.err[i];
    }
    alert(s);
  }

  if (ok && w.err.length > 0) {
    ok = false;

    for (var j = 0; j < eq.f.length; j++) {
      if (eq.f[j].name.match(/^(op|vmid|dev)$/i)) { continue; }

      var m = eq.f[j].name.match(/::(.*)$/);
      if (m.length > 1) { eq.f[j].name = m[1]; }
    }

    if (parent.ctx.match(/^editor$/i)) {
      main.vmDevObsrvr.lstn(obsrvrStr, lstnr, vm);
    }

    prev();
    return;
  }

  if (w.fromCheckDisk) {
    var dev = parent.op.win().devs[0];
    var size = new Number(dev.size / (1024 * 1024 * 1024)).toFixed(1);

    if (wzdMode == "create") {
      obj("capCheck").css("display", dev.size ? "" : "none");
      obj("capCreate").css("display", dev.size ? "none" : "");
    } else if (!dev.size) {
      alert('Disk file "' + dev.loc + '" does not exist.');
    }

    obj("diskSize").innerHTML = size ? size : "---";

    return;
  }

  if (ok) {
    var q = new Query(fromAddVmWzd ? self.location.search : parent.location.search);
    var vm = main.sx.vms[q.arg("vmid")[0]];

    if (fromAddVmWzd) {
      main.MuiWindow("details", vm.hash(), "hardware");
    } else {
      if (parent.ctx == "editor" &&
        vm.dev(q.arg("dev")[0]) != null &&
        w.devs[0].id != vm.dev(q.arg("dev")[0]).id) {
        // Dev node changed. Remove dev at old node.
        main.vmRemDevObsrvr.exec(vm, vm._dev[q.arg("dev")[0]]);
        delete vm._dev[q.arg("dev")[0]];
      }

      // Mod/add dev.
      vm.dev(w.devs[0].id, w.devs[0]);
    }
  }

  next();
}


// --------------------------------------------------------------------------
devMode = function(mode) {
  if (mode == "independent") {
    var enable = eq.select("independent", 1);

    // toggle checkbox
    eq.select("independent", 0, !enable);

    // enable disable the widget
    eq.f.persistent.disabled = !enable;
    eq.f.nonpersistent.disabled = !enable;

    mode = enable ? "independent-persistent" : "persistent";
  }

  eq.select("persistent", 1, mode == "independent-persistent");
  eq.select("nonpersistent", 1, mode == "independent-nonpersistent");

  // set Disk Mode in the hidden input
  eq.arg("mode", mode);
}


// --------------------------------------------------------------------------
checkDisk = function() {
  var w = parent.op.win();
  var q = new Query(fromAddVmWzd ? self.location.search : parent.location.search);
  var vm = main.sx.vms[q.arg("vmid")[0]];
  var nq = new Query(document.forms[0]);
  
  w.location.replace("/vm-config/index.pl?op=checkDisk&vmid=" + vm.hash() + 
                     "&loc=" +  nq.arg("loc"));
}

// --------------------------------------------------------------------------
function initPage() {
  if (parent.ready == null || ! parent.ready()) { window.setTimeout(initPage, 50); return; }
  var w = parent.op.win();

  var q = new Query(parent.location.search);
  var vm = main.sx.vms[q.arg("vmid")[0]];
  var dev;

  var wzdType; // One of new, existing or physical

  if (parent.ctx == "vmAddDeviceWizard") {
    if (vm == null || ! vm) {
      q = new Query(self.location.search);
      vm = main.sx.vms[q.arg("vmid")[0]];
      parent.document.title = parent.document.domain + ": " + vm.dn() + " | Configuration";
      parent.helpurl = "vserver/vmVirtualDiskHelp.html";
      fromAddVmWzd = true;
    }

    // Create a temporary device with appropriate defaults.
    // XXX: Defaults should come from the server.
    dev = {hi:"disk:file",mode:"persistent",id:"disk",loc:"",size:"8"};
  } else {
    dev = vm.dev(q.arg("dev")[0]);
  }

  obsrvrStr = "vmVirtualDisk_" + parent.ctx + "_" + vm.hash() + ":" + dev.id;

  var dom = {};
  dom.ideRow = obj("ideRow");
  dom.scsiRow = obj("scsiRow");
  dom.bootLock = obj("bootLock");
  dom.bootStr1 = obj("bootStr1");
  dom.bootStr2 = obj("bootStr2");
  dom.bootStr3 = obj("bootStr3");

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  function fillForm () {
    // Make a new Query object to represent the original form.
    oq = new Query(document.forms[0]);
    oq.arg("vmid", vm.hash());
    oq.arg("dev", dev.id);

    // Don't try to create a new disk unless directed to do so.
    oq.arg("createDisk", 0);

    // Use the operation appropriate for the current context.
    if (parent.ctx == "vmAddDeviceWizard") {
      oq.arg("op", "add");
      if (wzdMode == "create") {
        oq.arg("createDisk", 1);
        oq.arg("flat", 0);
        oq.arg("split", main.sx._os == "win32" ? 0 : 1);
      }
    }

    var size = new Number(dev.size / (1024 * 1024 * 1024)).toFixed(1);

    // Location
    oq.arg("loc", dev.loc);

    // disk size
    if (wzdMode == "create") {
       oq.arg("diskSize", dev.size);
    } else {
       obj("diskSize").innerHTML = size > 0 ? size : "---";
    }

    if (wzdMode == "create") {
      obj("capCheck").css("display", "none");
      obj("capCreate").css("display", "");
      obj("createOpts").css("display", "");
    } else {
      obj("capCheck").css("display", "");
      obj("capCreate").css("display", "none");
      obj("createOpts").css("display", "none");
    }

    // disk node
    var m;
    if (parent.ctx == "vmAddDeviceWizard") {
      devSelect(wzdType, null);
    } else if ((m = dev.id.match(/^scsidev(\d:\d)$/i)) != null) {
      devSelect("scsi", m[1]);
    } else if ((m = dev.id.match(/^idedev(\d:\d)$/i)) != null) {
      devSelect("ide", m[1]);
    }

    // device mode
    oq.select("independent", 1, dev.mode.match(/^independent/i));
    oq.select("persistent", 1, dev.mode == "independent-persistent");
    oq.select("nonpersistent", 1, dev.mode == "independent-nonpersistent");
    oq.f.persistent.disabled = !oq.select("independent", 1);
    oq.f.nonpersistent.disabled = !oq.select("independent", 1);
    oq.arg("mode", dev.mode);

    setTimeout("parent.layoutCb()", 40);

    // Make a new Query object to represent the modified form.
    eq = new Query(document.forms[0]);
    eq.sync(oq);
    oq.sync(eq);
  }


  unlockNodeCtlr = function () {
    dom.bootLock.css("display", "none");
    if (dev.id.match(/^scsi/i) != null || wzdType == "scsi") {
      objCss(dom.ideRow, "display", "none");
      objCss(dom.scsiRow, "display", "");
    } else  {
      objCss(dom.ideRow, "display", "");
      objCss(dom.scsiRow, "display", "none");
    }
    setTimeout("parent.layoutCb()", 40);
  };


  lockNodeCtlr = function (n) {
    dom.scsiRow.css("display", "none");
    dom.ideRow.css("display", "none");
    dom.bootLock.css("display", "");
    if (dev.id.match(/^scsi/i) != null || wzdType == "scsi") {
      dom.bootStr1.innerHTML = "the SCSI Controller 0 (usually 0:0) ";
      dom.bootStr2.innerHTML = "SCSI ";
      dom.bootStr3.innerHTML = "SCSI " + n;
    } else {
      dom.bootStr1.innerHTML = "the IDE node ";
      dom.bootStr2.innerHTML = "IDE ";
      dom.bootStr3.innerHTML = "IDE " + n;
    }
    setTimeout("parent.layoutCb()", 40);
  };

 
  // Show the appropriate selector, sans options for existing devices
  devSelect = function(t, n) {
    // Set up the form to display the correct disk type
    oq.arg("ctlr", t);
    objCss(dom.ideRow, "display", t == "ide" ? "" : "none");
    objCss(dom.scsiRow, "display", t == "scsi" ? "" : "none");

    var ctlrMax = (t == "ide" ? 2 : 4);
    var devMax = (t == "ide" ? 2 : 7);

    // Create a list of SCSI device IDs that are in use
    var pciCount = 0;
    var scsiCount = 0;
    var a = [];
    var re = new RegExp(t + "dev(\\d:\\d)", "i");
    for (var i in vm._dev) {
      var d = vm.dev(i);
      var m;
      if (((m = d.id.match(re)) != null) && (m[1] != n)) {
        a.push(m[1]);
      }
      if (d.id.match(/^(nic)/i)) {
        ++pciCount;
      } else if (d.id.match(/^(scsiCtlr)/i)) {
        ++pciCount;
        ++scsiCount;
      }
    }

    // Get a list of occupied slots on the boot controller.
    var r = grep(a, new RegExp("^0:\\d+$"));

    // Add the current device to the list of occupied slots.
    if (n != null && n.indexOf("0") == 0) {
      r.push(n);
    }
    
    // At least one non-zero slot on the boot controller is already occupied.
    if (r.length > 0) {
      r = r.sort();
      // Lock the Virtual SCSI/IDE Node if we're editing the boot device
      if (t == "scsi") {
	if (n != null && n == r[0]) {
          lockNodeCtlr(n);
        }
      } else {
	if (n == "0:0") {
          lockNodeCtlr(n);
	}
      }
    }

    if (n == null) {
      // We're in the add device wizard. Choose an empty slot that won't
      // interfere with any existing boot device.

      // If we don't enter this block, don't worry. The boot controller is empty or full.
      if (r.length > 0 && r.length < devMax) {
        // Start with an ix that has a chance of not being used.
        var ix = Number(r[0].split(":")[1]) + 1;
        for (var ctlrIx = 0; ctlrIx < ctlrMax;) {
          // Don't add a SCSI controller if we already have 5 PCI devices
          var found = grep(a, new RegExp("^" + ctlrIx + ":\\d+$")).length > 0;
          if (ctlrIx == 0 || t == "ide" || pciCount < 5 || found) {
            if (grep(a, new RegExp(ctlrIx + ":" + ix)).length == 0) {
              n = ctlrIx + ":" + ix;
              break;
            }
          }
          if (++ix == devMax) {
            ctlrIx++;
            ix = 0;
          }
        }
      }
    }

    // Add options for available devices
    oq.f.ideID.options.length = 0;
    oq.f.scsiID.options.length = 0;
    // We support a maximum of 4 SCSI controllers
    for (var ctlrIx = 0; ctlrIx < ctlrMax; ctlrIx++) {
      // We support a maximum of 5 PCI devices
      var found = grep(a, new RegExp("^" + ctlrIx + ":\\d+$")).length > 0;
      if (ctlrIx == 0 || t == "ide" || pciCount < 5 || found) {
        for (var ix = 0; ix < devMax; ix++) {
          var id = ctlrIx + ":" + ix;
          if (grep(a, id).length == 0) {
            var opts = t == "ide" ? oq.f.ideID.options : oq.f.scsiID.options;
            opts[opts.length] = new Option(id, id);
          }
        }
        if (t == "scsi" && !found) pciCount++;
      }
    }
    

    // Remove options for in-use devices, working backward
    // so that the indices don't shift out from under us
    if (a.length > 0) {
      var o = oq.f[t + "ID"].options;
      var remcount = 0;
      for (var i = o.length - 1; i >= 0; i--) {
        if (grep(a, o[i].value).length > 0) {
          o[i] = null;
	  remcount++;
        }
      }
      if ((t == "ide") && (remcount == 4)) {
        oq.f.ideID.options[oq.f.ideID.options.length] =
          new Option("all used", "all used");
      }
    }
    
    // Select the correct option
    if (n != null) {
      oq.arg(t + "ID", n);
    }
  };

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;

    obj("hdr").innerHTML = hrDev(dev);
    fillForm();

    setTimeout("parent.layoutCb()", 40);
  };


  editor.nxt[0] = svgMsg;
  editor.exec = function () {
    eq.cache();

    // Validate disk size
    if ((parent.ctx == "vmAddDeviceWizard") && (wzdMode == "create")) {
      var size = parseFloat(eq.arg("diskSize"));
      var t = eq.arg("ctlr");

      if (isNaN(size)) {
        alert("The specified disk size (" + eq.arg("diskSize") + " GB) is invalid.  " +
              "Please specify a valid number.");
        return false;
      }

      // XXX Limits aren't available in VMDB
      var min = 0.1;
      var max = t == "ide" ? 128 : 256;
      if ((size < min) || (size > max)) {
        alert("The specified disk size (" + size + " GB) is " +
              ((size < min) ? "less than the minimum " : "greater than the maximum ") +
              "size for a virtual " + (t == "ide" ? "IDE" : "SCSI") + " disk.  " +
              "Please specify a size between " + min + " GB and " + max + " GB.");
        return false;
      }
    }

    // Submit the form if there are diffs or we're trying to add a new device
    // with default settings.
    if (parent.ctx == "vmAddDeviceWizard" || eq.diff(oq)) {
      ok = true;

      // Params should be qualified with their dev name before being submitted.
      for (var j = 0; j < eq.f.length; j++) {
        if (eq.f[j].name.match(/^(op|vmid|dev)$/i)) { continue; }
        eq.f[j].name = dev.id + "::" + eq.f[j].name;
      }

      if (parent.ctx.match(/^editor$/i)) {
        main.vmDevObsrvr.ignr(obsrvrStr);
      }

      eq.submit();
    } else {
      exit();
    }
  };


  var wzdMsg = new Step("wzdMsg", obj("ldgMsg"));
  var wizard = new Step("wizard", obj("wizard"));

  wzdMsg.nxt[0] = wizard;
  wzdMsg.exec = function () {
    obj("subHdr").innerHTML = "Add a hard disk to your virtual machine.";

    var os = vm.os();
    for (var i = 0; i < main.sx._guests.length; i++) {
      var guest = main.sx._guests[i];
      if (guest.key == os) {
        obj("ideType").innerHTML = "IDE type" + (guest.scsi ? "" : " (recommended)") + ":";
        obj("scsiType").innerHTML = "SCSI type" + (guest.scsi ? " (recommended)" : "") + ":";
        break;
      }
    }

    parent.tglBtns(false);
    setTimeout("parent.layoutCb()", 40);
  };

  wizard.nxt[0] = wizard.nxt[1] = wizard.nxt[2] = editor;
  wizard.nxt[3] = wizard.nxt[4] = wizard.nxt[5] = editor;
  wizard.exec = function (i) {
    if (i == 0) {
      wzdMode = "create";
      wzdType = "ide";
    } else if (i == 1) {
      wzdMode = "existing";
      wzdType = "ide";
    } else if (i == 2) {
      wzdMode = "physical";
      wzdType = "ide";
    } else if (i == 3) {
      wzdMode = "create";
      wzdType = "scsi";
    } else if (i == 4) {
      wzdMode = "existing";
      wzdType = "scsi";
    } else {
      wzdMode = "physical";
      wzdType = "scsi";
    }

    if (wzdMode == "physical") {
      alert("Physical disk are not implemented.");
      return false;
    }

    fillForm();

    parent.tglBtns(true);
    parent.slctBtns(fromAddVmWzd ? "wiz" : "std");
    setTimeout("parent.layoutCb()", 40);
    return true;
  };


  lstnr = function (t, o, v) {
    if (v.id == dev.id && confirm("The virtual disk configuration " +
      "has changed. Reload configuration?")) {
      ldgMsg.exec();
    }
  };

  if (parent.ctx.match(/^editor$/i)) {
    main.vmDevObsrvr.lstn(obsrvrStr, lstnr, vm);
  }

  var curStep = parent.ctx == "vmAddDeviceWizard" ? wzdMsg : ldgMsg;

  prev = function () {
    if (curStep == editor && parent.ctx == "vmAddDeviceWizard") {
      wzdMsg.slct(0);
    }
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  if (parent.ctx.match(/^vmAddDeviceWizard$/i)) {
    w.location.replace("/vm-config/index.pl?op=getVmDiskDevData");
  } else {
    w.location.replace("/vm-config/index.pl?vmid=" + vm.hash() + "&dev=" + esc(dev.id));
  }
}


