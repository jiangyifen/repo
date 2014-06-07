/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// Original query, edited query
var oq, eq;
var ok = false;
var obsrvrStr;
var wzdMode; // One of new, existing or physical

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

  if (ok) {
    var q = new Query(parent.location.search);
    var vm = main.sx.vms[q.arg("vmid")[0]];

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

  next();
}


// --------------------------------------------------------------------------

function initPage() {
  var w = parent.op.win();
  var q = new Query(parent.location.search);
  var vm = main.sx.vms[q.arg("vmid")[0]];
  var dev;
  if (parent.ctx == "vmAddDeviceWizard") {
    // Create a temporary device with appropriate defaults.
    // XXX: Defaults should come from the server.
    dev = {hi:"device",rdsc:true,rdcs:false,id:"passthru",loc:""};
  } else {
    dev = vm.dev(q.arg("dev")[0]);
  }
  var node;

  obsrvrStr = "vmGenericScsi_" + parent.ctx + "_" + vm.hash() + ":" + dev.id;

  var vlval, fnval, vlidx;

  var dom = {};
  dom.scsiRow = obj("scsiRow");
  dom.bootLock = obj("bootLock");
  dom.bootStr = obj("bootStr");
  dom.scsiLock = obj("scsiLock");
  dom.scsiStr = obj("scsiStr");
  dom.loc = obj("loc");
  dom.oneLoc = obj("oneLoc");
  
  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  function initDevToString() {
    var na = " (unknown device)";

    devLbl = function (vmhba) {
      if (defined(w.genScsi[vmhba])) {
	switch (w.genScsi[vmhba].type) {
	  case "tape":
	    return "Tape Device (" + vmhba + ")";
	  case "cdrom":
	    return "CD-ROM Device (" + vmhba + ")";
	  default:
	    break;
	}
      }

      return vmhba + na;
    };
  }
  initDevToString();
  
  // Try to place the device where it will most likely work (i.e., on the same
  // virtual SCSI target as the device's physical target).
  slctScsiNode = function (vmhba) {
    var m = vmhba.match(new RegExp("^vmhba\\d+:(\\d+):\\d+:\\d+", "i"));

    if (defined(m)) {
      // Try not to put the device on the same controller as the boot device.
      if (eq.arg("scsiID", "1:" + m[1])[0] == "1:" + m[1]) {
	return true;
      }

      // Try the boot controller.
      if (eq.arg("scsiID", "0:" + m[1])[0] == "0:" + m[1]) {
	return true;
      }
    }
    
    return false;
  }
  
  function genScsiSlct() {
    var opts = dom.loc.options;
    
    ignr(dom.loc, "change", slctScsiNode);
    lstn(dom.loc, "change", slctScsiNode);
    
    opts.length = 0;
    
    for (var vmhba in w.genScsi) {
      var slctd = (dev.loc == vmhba);
      opts[opts.length] = new Option(devLbl(vmhba), vmhba, slctd, slctd);
    }

    if (dev.loc && ! defined(w.genScsi[dev.loc])) {
	opts[opts.length] = new Option(devLbl(dev.loc), dev.loc, slctd, slctd);
    }
    
    if (opts.length == 0) {
      dom.oneLoc.innerHTML = "No generic SCSI devices found.";
    } else if (opts.length == 1) {
      dom.oneLoc.innerHTML = opts[0].text;
    }
    
    if (opts.length <= 1) {
      dom.loc.css("display", "none");
      dom.oneLoc.css("display", "");
    } else {
      dom.loc.css("display", "");
      dom.oneLoc.css("display", "none");
    }
    
  }
  
  function fillForm () {
    // Make a new Query object to represent the original form.
    oq = new Query(document.forms[0]);

    // Make a new Query object to represent the modified form.
    eq = new Query(document.forms[0]);
    
    oq.arg("vmid", vm.hash());
    oq.arg("dev", dev.id);

    // Use the operation appropriate for the current context.
    if (parent.ctx == "vmAddDeviceWizard") {
      oq.arg("op", "add");
    }

    // Connected
    oq.select("rdcs", 1, dev.rdcs);
    oq.select("rdcs", 0, ! dev.rdcs);

    // There are 2 checkboxes. Trying to set oq.f.rdcs.disabled will not work.
    var l = oq.list("rdcs")
    for (var j = 0; j < l.length; j++) {
      oq.f[l[j]].disabled = vm.es() != esOn;
    }

    // Connect at Power On
    oq.select("rdsc", 1, dev.rdsc);
    oq.select("rdsc", 0, ! dev.rdsc);

    // Location
    oq.arg("loc", dev.loc);

    var m;
    if ((m = dev.id.match(/^scsidev(\d:\d)$/i)) != null) {
      node = m[1];
    }

    if (parent.ctx == "vmAddDeviceWizard") {
      devSelect("scsi", null);
    } else {
      devSelect("scsi", node);
    }

    eq.sync(oq);
  }

  unlockScsiCtlr = function () {
    dom.scsiRow.css("display", "");
    dom.bootLock.css("display", "none");
    dom.scsiLock.css("display", "none");
    setTimeout("parent.layoutCb()", 40);
  };

  lockScsiCtlr = function (n, b) {
    dom.scsiRow.css("display", "none");
    dom.bootLock.css("display", b ? "" : "none");
    dom.scsiLock.css("display", b ? "none" : "");
    b ? dom.bootStr.innerHTML = n : dom.scsiStr.innerHTML = n;
    setTimeout("parent.layoutCb()", 40);
  };

  // Show the appropriate selector, sans options for existing devices
  devSelect = function(t, n) {
    // Set up the form to display the correct disk type
    oq.arg("ctlr", t);

    var ctlrMax = 4;
    var devMax = 7;

    // Create a list of device IDs that are in use
    var a = [];
    var pciCount = 0;
    var scsiCount = 0;
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
        ++scsiCount;
        ++pciCount;
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

      // Lock the Virtual SCSI Node if we're editing the boot device
      if (n != null && n == r[0]) {
        lockScsiCtlr(n, true);
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
          if (ctlrIx == 0 || pciCount < 5 || found) {
            if (grep(a, new RegExp(ctlrIx + ":" + ix)).length == 0) {
              n = ctlrIx + ":" + ix;
              break;
            }
          }
          if (++ix == 7) {
            ctlrIx++;
            ix = 0;
          }
        }
      }
    }

    // Add options for available devices
    oq.f.scsiID.options.length = 0;
    // We support a maximum of 4 SCSI controllers
    for (var ctlrIx = 0; ctlrIx < ctlrMax; ctlrIx++) {
      // We support a maximum of 5 PCI devices
      var found = grep(a, new RegExp("^" + ctlrIx + ":\\d+$")).length > 0;
      if (ctlrIx == 0 || pciCount < 5 || found) {
        for (var ix = 0; ix < devMax; ix++) {
          var id = ctlrIx + ":" + ix;
          if (grep(a, id).length == 0) {
            var opts = oq.f.scsiID.options;
            opts[opts.length] = new Option(id, id);
          }
        }
        if (!found) pciCount++;
      }
    }
    
    var usingGenScsiNode = slctScsiNode(oq.arg("loc")[0]);
    
    // Select the correct option
    if (usingGenScsiNode) {
      oq.arg("scsiID", eq.arg("scsiID")[0]);
    } else if (n != null) {
      oq.arg("scsiID", n);
    }
  };

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;

    obj("hdr").innerHTML = hrDev(dev);

    genScsiSlct();
    fillForm();

    setTimeout("parent.layoutCb()", 40);
  };

  editor.nxt[0] = svgMsg;
  editor.exec = function () {
    eq.cache();

    // Negating versions of the rdcs and rdsc checkboxes are hidden. Toggle
    // them like radio buttons.
    eq.select("rdcs", 0, ! eq.select("rdcs", "1"));
    eq.select("rdsc", 0, ! eq.select("rdsc", "1"));

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

  lstnr = function (t, o, v) {
    if (v.id == dev.id && confirm("The generic SCSI device configuration " +
      "has changed. Reload configuration?")) {
      ldgMsg.exec();
    }
  };
  if (parent.ctx.match(/^editor$/i)) {
    main.vmDevObsrvr.lstn(obsrvrStr, lstnr, vm);
  }

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  if (parent.ctx.match(/^vmAddDeviceWizard$/i)) {
    w.location.replace("/vm-config?op=getVmGenericScsiDevData");
  } else {
    w.location.replace("/vm-config?vmid=" + vm.hash() + "&dev=" + esc(dev.id));
  }
}
