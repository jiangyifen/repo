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
    if (i == -1 && ! fromAddVmWzd) {
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

function initPage() {
  if (parent.ready == null || ! parent.ready()) { window.setTimeout(initPage, 50); return; }

  var w = parent.op.win();
  var vols, dsks, bsm, bsl, ams;

  // Returns a Prt (partition) object given a volume lable or id.
  function vl2Prt(l) {
    // Get the id.
    l = l2id(l);

    // Now look for the Prt object in the available disks.
    for (var i = 0; dsks != null && i < dsks.length; i++) {
      var pt = dsks[i].pt;
      for (var j = 0; pt != null && j < pt.length; j++) {
        if (l == pt[j].id) { return pt[j]; }
      }
    }
    return null;
  }

  // Returns pretty volume labels.
  function vl(v,m) {
    if (v == null) {
      return "";
    }
    m = m ? ": " + hrVal(v.fmb) + " free" : "";
    if (v.vl) {
      return v.vl + " (" + v.id + ")" + m;
    }
    return v.id + m;
  }

  // Returns an array of pretty volume labels.
  function vls(m) {
    var a = new Array();
    for (var i in vols) {
      if (vols[i].fl.length > 0 || wzdMode == "new") {
        a.push(vl(vols[i], m));
      }
    }
    return a;
  }

  // Returns an array of volume IDs.
  function vvs() {
    var a = new Array();
    for (var i in vols) {
      if (vols[i].fl.length > 0 || wzdMode == "new") {
        // XXX: Change this to use one of (in this order): UUID, volume lable
        // or volume ID.
        a.push(vols[i].id);
      }
    }
    return a;
  }

  // Returns the index of a volume given an ID or label.
  function l2idx(l) {
    for (var i in vols) {
      if (vols[i].vl == l || vols[i].id == l) {
        return i;
      }
    }
    return -1;
  }

  // Returns the corresponding volume ID of a given volume label.
  function l2id(l) {
    var i = l2idx(l);
    if (i > -1) {
      return vols[i].id;
    }
    return "";
  }

  // Returns an array of file names.
  function fns(v) {
    var a = new Array();
    if (v != null) {
      for (var i in v.fl) {
        a.push(v.fl[i].fn);
      }
    }
    return a;
  }

  // Returns the index of file name fn in the list of files on volume v.
  function fn2idx(fn, v) {
    for (var i = 0; v != null && v.fl != null && i < v.fl.length; i++) {
      if (v.fl[i].fn == fn) {
        return i;
      }
    }
    return -1;
  }

  // Returns the size in megabytes of the file name fn on the volume v.
  function fn2mb(fn, v) {
    var i = fn2idx(fn, v);
    if (i > -1) {
      return v.fl[i].mb;
    }
    return 0;
  }

  // Returns a size in megabytes equal to the recommended disk capcity or the
  // available space on volume v.
  function defMb(v) {
    return v.fmb >= 4000 ? 4000 : v.fmb;
  }

  // Return the ID of the disk at index i.
  function dskVal(i) {
    return dsks[i].id;
  }

  // Return an array of disk IDs.
  function dskVals() {
    var a = [];
    for (var i = 0; i < dsks.length; i++) {
      a.push(dskVal(i));
    }
    return a;
  }

  // Return a label for the disk at index i.
  function dskLbl(i) {
    var d = dsks[i];
    return d.id + ": " + hrVal(d.mb) + " (Partitions: " + d.pt.length + ")";
  }

  // Return an array of disk lables.
  function dskLbls() {
    var a = [];
    for (var i = 0; i < dsks.length; i++) {
      a.push(dskLbl(i));
    }
    return a;
  }

  // Given a disk ID, return its index.
  function dskLbl2Idx(id) {
    for (var i = 0; i < dsks.length; i++) {
      if (dsks[i].id == id) {
        return i;
      }
    }
    return -1;
  }

  // (Re)write the file name select control with the selected volume label and
  // file name.
  function fnSlct(svl, sfn) {
    dom.fnVal.innerHTML = htmlSlct("fn", fns(vols[l2idx(svl)], true), sfn,
      fns(vols[l2idx(svl)]), "fnChgd(this);");
    if (eq != null) { eq.cache("fn"); }
  }

  // Handle VMFS volume option selections.
  vlChgd = function (e) {
    eq.cache("vl");
    var id = eq.arg("vl")[0];

    if (parent.ctx == "editor" || wzdMode == "existing") {
      fnSlct(id, fnval);
    }

    var mb = fn2mb(eq.arg("fn")[0], vols[l2idx(id)]);
    eq.arg("mb", mb > 0 ? mb : defMb(vols[l2idx(id)]));
  };

  // Handle file name option selections.
  fnChgd = function (e) {
    var id = eq.arg("vl")[0];
    var fn;

    // Grab the value of the selected file name option.
    fn = eq.cache("fn")[0];

    var mb = fn2mb(fn, vols[l2idx(id)]);
    eq.arg("mb", mb > 0 ? mb : defMb(vols[l2idx(id)]));
  };

  // Handle raw LUN selections.
  dskChgd = function (e) {
    eq.cache("lun");
    var id = eq.arg("lun")[0];
    eq.arg("mb",
      hrVal(dsks[dskLbl2Idx(id)].mb, 0, "m", "m", 1024, ["","","","",""]));
  };

  var q = new Query(parent.location.search);
  var vm = main.sx.vms[q.arg("vmid")[0]];
  var dev;

  if (parent.ctx == "vmAddDeviceWizard") {
    if (vm == null || ! vm) {
      q = new Query(self.location.search);
      vm = main.sx.vms[q.arg("vmid")[0]];
      parent.document.title = parent.document.domain + ": " + vm.dn() + " | Configuration";
      parent.helpurl = "esx/vmVirtualDiskHelp.html";
      fromAddVmWzd = true;
    }

    // Create a temporary device with appropriate defaults.
    // XXX: Defaults should come from the server.
    dev = {hi:"disk:file",mode:"persistent",id:"disk",loc:""};
  } else {
    dev = vm.dev(q.arg("dev")[0]);
  }

  obsrvrStr = "vmVirtualDisk_" + parent.ctx + "_" + vm.hash() + ":" + dev.id;

  var vlval, fnval, vlidx;

  var dom = {};
  dom.persistent = obj("persistent");
  dom.nonpersistent = obj("nonpersistent");
  dom.undoable = obj("undoable");
  dom.append = obj("append");
  dom.noDsks = obj("noDsks");
  dom.blank = obj("blank");
  dom.existing = obj("existing");
  dom.physical = obj("physical");
  dom.vlVal = obj("vlVal");
  dom.vlKey = obj("vlKey");
  dom.fnVal = obj("fnVal");
  dom.fnKey = obj("fnKey");
  dom.scsiRow = obj("scsiRow");
  dom.bootLock = obj("bootLock");
  dom.bootStr = obj("bootStr");
  dom.scsiLock = obj("scsiLock");
  dom.scsiStr = obj("scsiStr");
  dom.ideRow = obj("ideRow");
  dom.fnRow = obj("fnRow");
  dom.modeRow = [];
  for (var i = 0; true; i++) {
    var o = obj("modeRow" + i);
    if (o != null) {
      dom.modeRow.push(o);
    } else {
      break;
    }
  }

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
      if (wzdMode == "new") {
        oq.arg("createDisk", 1);
      }
    }

    // VMFS Volume
    if (oq.f.vl != null) {
      oq.arg("vl", vlval);
    } else {
      oq.arg("lun", vlval);
    }

    // Hidden input for loc
    if (fnval != null || oq.f.fn != null) {
      // VMware Disk Image
      oq.arg("fn", fnval);

      // Disk Mode
      oq.arg("mode", dev.mode);

      // Capacity
      oq.arg("mb", fn2mb(fnval, vols[vlidx]));
      oq.f.mb.disabled = true;

      oq.arg("loc", oq.arg("vl")[0] + ":" + fnval);
    } else {
      oq.arg("mb",
        hrVal(dsks[vlidx].mb, 0, "m", "m", 1024, ["","","","",""]));
      oq.f.mb.disabled = true;

      oq.arg("loc", oq.arg("lun")[0] + ":0");
    }

    var m;
    if (parent.ctx == "vmAddDeviceWizard") {
      devSelect("scsi", null);
    } else if ((m = dev.id.match(/^scsidev(\d:\d)$/i)) != null) {
      devSelect("scsi", m[1]);
    } else if ((m = dev.id.match(/^idedev(\d:\d)$/i)) != null) {
      devSelect("ide", m[1]);
    }

    // Make a new Query object to represent the modified form.
    eq = new Query(document.forms[0]);
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
    objCss(dom.ideRow, "display", t == "scsi" ? "none" : "");
    objCss(dom.scsiRow, "display", t == "scsi" ? "" : "none");

    var ctlrMax = (t == "ide" ? 2 : 4);
    var devMax = (t == "ide" ? 2 : 7);

    // Create a list of device IDs that are in use
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
    
    // Select the correct option
    if (n != null) {
      oq.arg(t + "ID", n);
    }
  };

  hilite = function (m) {
    dom.persistent.att("class", "");
    dom.nonpersistent.att("class", "");
    dom.undoable.att("class", "");
    dom.append.att("class", "");

    dom[m].att("class", "slctd");
  };

  slct = function (m) {
    eq.arg("mode", m);
    hilite(m);
  };

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;

    if (w.vols != null) { vols = clone(w.vols, true); }
    if (w.dsks != null) { dsks = clone(w.dsks, true); }
    if (w.bsm != null) { bsm = clone(w.bsm, true); }
    if (w.bsl != null) { bsl = clone(w.bsm, true); }
    if (w.ams != null) { ams = clone(w.ams, true); }

    if (dev.loc.match(/vmhba\d+:\d+:\d+:0:?$/)) {
      vlval = dev.loc.replace(/(vmhba\d+:\d+:\d+):0:?/, "$1");
      fnval = null;
    } else if (dev.loc.match(/vmhba\d+:\d+:\d+:\d+:.*/)) {
      vlval = dev.loc.replace(/(vmhba\d+:\d+:\d+:\d+):(.*)/, "$1");
      fnval = dev.loc.replace(/(vmhba\d+:\d+:\d+:\d+):(.*)/, "$2");
    } else {
      vlval = dev.loc.replace(/(.*?):(.*)/, "$1");
      fnval = dev.loc.replace(/(.*?):(.*)/, "$2");
    }

    if (fnval != null) {
      vlidx = vlval ? l2idx(vlval) : 0;

      // VMFS Volume
      var ids, labels, errmsg = "";
      if (vlidx < 0 || vlidx >= vols.length) {
         errmsg = "VMFS volume (" + vlval + ") not found. \n";
         ids = vvs(); // volume IDs
         labels = vls(true); // volume labels
         ids.unshift(vlval);
         labels.unshift(vlval + " (not found)");
         dom.vlVal.innerHTML = htmlSlct("vl", ids, l2id(vlval), labels, "vlChgd(this);");
      } else {
         if (vols.length > 1) {
           dom.vlVal.innerHTML = htmlSlct("vl", vvs(), l2id(vlval),
             vls(true), "vlChgd(this);");
         } else {
           dom.vlVal.innerHTML = vl(vols[0], true) +
             '<input type="hidden" name="vl" value="' + vols[0].id + '">';
           dom.vlVal.att("class", "val");
         }
      }

      // File Name
      var vol = vols[vlidx];
      ids = fns(vol);
      labels = fns(vol);
      if (vol) {
         var found = false;
         var length = vol.fl.length;
         for (var i = 0; i < length; i++) {
           if (fnval == vol.fl[i].fn) {
             found = true;
             break;
           }
         }

         if (!found) {
           errmsg += "Disk image (" + fnval + ") not found.";
           ids.unshift(fnval);
           labels.unshift(fnval + " (not found)");
         }
      }

      dom.fnVal.innerHTML =
         htmlSlct("fn", ids, fnval, labels, "fnChgd(this);");
      if (eq != null) { eq.cache("fn"); }

      if (errmsg.length > 0) {
        alert(errmsg);
      }

      for (var j = 0; j < dom.modeRow.length; j++) {
        dom.modeRow[j].css("display", "");
      }
      dom.fnRow.css("display", "");
      dom.vlKey.innerHTML = "<div>VMFS Volume</div>";
    } else {
      vlidx = vlval ? dskLbl2Idx(vlval) : 0;

      // Raw LUN
      if (dsks.length > 1) {
        dom.vlVal.innerHTML = htmlSlct("lun", dskVals(), vlval,
          dskLbls(true), "dskChgd(this);");
      } else {
        dom.vlVal.innerHTML = dskLbl(0, true) +
          '<input type="hidden" name="lun" value="' + dsks[0].id + '">';
        dom.vlVal.att("class", "val");
      }

      for (var j = 0; j < dom.modeRow.length; j++) {
        dom.modeRow[j].css("display", "none");
      }
      dom.fnRow.css("display", "none");
      dom.vlKey.innerHTML = "<div>System LUN/Disk</div>";
    }

    obj("hdr").innerHTML = hrDev(dev);

    fillForm();


    if (fnval != null) {
      hilite(dev.mode);
    }

    setTimeout("parent.layoutCb()", 40);
  };

  function validate() {
    if (parent.ctx != "vmAddDeviceWizard" || fnval == null) { return true; }
    if (wzdMode == "existing" || wzdMode == "physical") { return true; }

    if (isNaN(Number(eq.arg("mb")[0])) || Number(eq.arg("mb")[0]) <= 0 ||
      eq.arg("mb")[0].match(/\./)) {
      alert("Virtual disk capacity must be specified as an integer greater " +
        "than zero.");
      eq.arg("mb", defMb(vols[l2idx(eq.arg("vl")[0])]));
      eq.f.mb.select();
      return false;
    }

    var fmb = vols[l2idx(eq.arg("vl")[0])].fmb;

    var pt = vl2Prt(eq.arg("vl")[0]);
    var fs = pt.tn.toUpperCase() == "VMFS-1" ? "vmfs1" : "vmfs2";
    var ms = pt.bs * bsm[fs];
    if (ams[fs] && ms > ams[fs]) {
      ms = ams[fs];
    }
    // ms is in gigabytes, we need it in megabytes.
    ms = ms * 1024;

    var max = fmb <= ms ? fmb : ms;

    if (eq.arg("mb")[0] > max) {
      alert("The maximum virtual disk capacity for the selected VMFS volume " +
        "is " + max + " megabytes.");
      eq.arg("mb", max);
      eq.f.mb.select();
      return false;
    }

    return true;
  }

  editor.nxt[0] = svgMsg;
  editor.exec = function () {
    eq.cache();

    // Hidden input for loc
    if (oq.f.fn != null) {
      // Use a VMFS volume label if one is available.
      var idx = l2idx(eq.arg("vl")[0]);
      if (idx >= 0 && idx < vols.length) {
        var vlid = vols[idx].vl ? vols[idx].vl : vols[idx].id;
        eq.arg("loc", vlid + ":" + eq.arg("fn")[0]);
      }
    } else {
      eq.arg("loc", eq.arg("lun")[0] + ":0");
    }

    // Submit the form if there are diffs or we're trying to add a new device
    // with default settings.
    if (parent.ctx == "vmAddDeviceWizard" || eq.diff(oq)) {
      if (! validate()) { return false; }

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
    if (w.vols != null) { vols = clone(w.vols, true); }
    if (w.dsks != null) { dsks = clone(w.dsks, true); }
    if (w.bsm != null) { bsm = clone(w.bsm, true); }
    if (w.bsl != null) { bsl = clone(w.bsm, true); }
    if (w.ams != null) { ams = clone(w.ams, true); }

    obj("subHdr").innerHTML = "Add a hard disk to your virtual machine.";

    if (dsks == null || dsks.length < 1) {
      dom.noDsks.css("display", "");
      this.o.css("display", "none");

      parent.tglBtns(true);
      parent.slctBtns("cls");
      setTimeout("parent.layoutCb()", 40);
      return false;
    } else {
      wizard.prv = null;

      if (vols == null || vols.length < 1) {
        dom.blank.att("class", "dsbld");
        dom.blank.att("href", "javascript:;");
      } else {
        dom.blank.att("class", "");
        dom.blank.att("href", "javascript:next(0);");
      }

      var fnCnt = 0;
      for (var i = 0; vols != null && i < vols.length; i++) {
        if (vols[i].fl != null && vols[i].fl.length > 0) {
          fnCnt += vols.fl;
        }
      }
      if (fnCnt == 0) {
        dom.existing.att("class", "dsbld");
        dom.existing.att("href", "javascript:;");
      } else {
        dom.existing.att("class", "");
        dom.existing.att("href", "javascript:next(1);");
      }

      parent.tglBtns(false);
      setTimeout("parent.layoutCb()", 40);
    }
  };

  wizard.nxt[0] = wizard.nxt[1] = wizard.nxt[2] = editor;
  wizard.exec = function (i) {
    if (w.vols != null) { vols = clone(w.vols, true); }
    if (w.dsks != null) { dsks = clone(w.dsks, true); }
    if (w.bsm != null) { bsm = clone(w.bsm, true); }
    if (w.bsl != null) { bsl = clone(w.bsm, true); }
    if (w.ams != null) { ams = clone(w.ams, true); }

    if (i == 0) {
      wzdMode = "new";
    } else if (i == 1) {
      wzdMode = "existing";
    } else {
      wzdMode = "physical";
    }

    if (i != 2) {
      vlval = vvs()[0];
      vlidx = vlval ? l2idx(vlval) : 0;
      if (wzdMode == "existing") {
        fnval = vols[vlidx].fl[0].fn;
      } else {
        fnval = "Untitled.dsk";
      }

      // VMFS Volume
      if (vols.length > 1) {
        dom.vlVal.innerHTML = htmlSlct("vl", vvs(), l2id(vlval),
          vls(true), "vlChgd(this);");
      } else {
        dom.vlVal.innerHTML = vl(vols[0], true) +
          '<input type="hidden" name="vl" value="' + vols[0].id + '">';
        dom.vlVal.att("class", "val");
      }

      // File Name
      if (i == 1) {
        fnSlct(vlval, fnval);
      } else {
        dom.fnVal.innerHTML = htmlTxtInpt("fn", "Untitled.dsk");
      }

      for (var j = 0; j < dom.modeRow.length; j++) {
        dom.modeRow[j].css("display", "");
      }
      dom.fnRow.css("display", "");
      dom.vlKey.innerHTML = "<div>VMFS Volume</div>";
    } else {
      vlval = dskVals()[0];
      vlidx = vlval ? dskLbl2Idx(vlval) : 0;
      fnval = null;

      // Invalidate certain form elements to account for the event that someone
      // chooses one path (blank or existing) before choosing this path (raw
      // disk).
      if (oq != null) {
        if (oq.f.vl != null) {
          if (ie) {
            dom.vlVal.innerHTML = "";
          } else {
            delete oq.f.vl;
          }
          delete oq.args.vl;
          delete eq.args.vl;
        }

        if (oq.f.fn != null) {
          if (ie) {
            dom.fnVal.innerHTML = "";
          } else {
            delete oq.f.fn;
          }
          delete oq.args.fn;
          delete eq.args.fn;
        }
      }

      dom.fnVal.innerHTML = "";

      // Raw LUN
      if (dsks.length > 1) {
        dom.vlVal.innerHTML = htmlSlct("lun", dskVals(), vlval,
          dskLbls(true), "dskChgd(this);");
      } else {
        dom.vlVal.innerHTML = dskLbl(0, true) +
          '<input type="hidden" name="lun" value="' + dsks[0].id + '">';
        dom.vlVal.att("class", "val");
      }

      for (var j = 0; j < dom.modeRow.length; j++) {
        dom.modeRow[j].css("display", "none");
      }
      dom.fnRow.css("display", "none");
      dom.vlKey.innerHTML = "<div>Storage Controller LUN</div>";
    }

    fillForm();

    if (i == 0) {
      eq.arg("mb", defMb(vols[vlidx]));
      oq.f.mb.disabled = false;

      if (! ie) { objCss(eq.f.fn, "width", "200px;"); }
    }

    if (i != 2) {
      hilite(dev.mode);
    }

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
    w.location.replace("/vm-config?op=getVmDiskDevData");
  } else {
    w.location.replace("/vm-config?vmid=" + vm.hash() + "&dev=" + esc(dev.id));
  }
}
