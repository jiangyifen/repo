/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// Original query, edited query
var oq, eq;
var ok = false;
var obsrvrStr;


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
    if (parent.ctx.match(/^editor$/i)) {
      main.vmDevObsrvr.lstn(obsrvrStr, lstnr, vm);
      main.vmHwvObsrvr.lstn(obsrvrStr, lstnr, vm);
    }
    prev();
    return;
  }

  if (ok == "mod") {
    var vm = main.sx.vms[new Query(parent.location.search).arg("vmid")[0]];
    vm.dev(w.devs[0].id, w.devs[0]);
  }

  if (ok == "upgrade") {
    var vm = main.sx.vms[new Query(parent.location.search).arg("vmid")[0]];
    vm.hwv(main.defaultHwv);
    unlockHwvRow();
  }

  next();
}


// --------------------------------------------------------------------------

function initPage() {
  var w = parent.op.win();
  var q = new Query(parent.location.search);
  var vm = main.sx.vms[q.arg("vmid")[0]];
  var vdevs = ["vmxbuslogic", "vmxlsilogic"]; // supported virtual devices
  var dev;
  if (parent.ctx == "vmAddDeviceWizard") {
    // Create a temporary device with appropriate defaults.
    // XXX: Defaults should come from the server.
    dev = {vdev:"vmxbuslogic",id:"scsiCtlr",shbus:"none"};
  } else {
    dev = vm.dev(q.arg("dev")[0]);
  }

  var supported = false;
  for (var i = 0; i < vdevs.length; i++) {
    if (vdevs[i] == dev.vdev) {
      supported = true;
      break;
    } 
  }

  obsrvrStr = "vmScsiController_" + parent.ctx + "_" + vm.hash() + ":" + dev.id;

  var dom = {};
  dom.hwvLock = obj("hwvLock");
  dom.vdevLock = obj("vdevLock");
  dom.vdevStr = obj("vdevStr");
  dom.vdevRow = obj("vdevRow");

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var hwvMsg = new Step("hwvMsg", obj("hwvMsg"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  unlockVdevRow = function () {
    dom.vdevRow.css("display", "");
    dom.vdevLock.css("display", "none");
    setTimeout("parent.layoutCb()", 40);
  };

  lockVdevRow = function () {
    dom.vdevRow.css("display", "none");
    dom.vdevLock.css("display", "");
    dom.vdevStr.innerHTML = dev.vdev;
    setTimeout("parent.layoutCb()", 40);
  };

  unlockHwvRow = function () {
    dom.vdevRow.css("display", "");
    dom.hwvLock.css("display", "none");
    setTimeout("parent.layoutCb()", 40);
  };

  lockHwvRow = function () {
    dom.vdevRow.css("display", "none");
    dom.hwvLock.css("display", "");
    setTimeout("parent.layoutCb()", 40);
  };

  function fillForm () {
    // Make a new Query object to represent the original form.
    oq = new Query(document.forms[0]);
    oq.arg("vmid", vm.hash());
    oq.arg("dev", dev.id);

    // Use the operation appropriate for the current context.
    if (parent.ctx == "vmAddDeviceWizard") {
      oq.arg("op", "add");
    }

    // Virtual Device
    // XXX Hide this row for legacy OSes that can't use vmxlsilogic?
    var m = dev.id.match(/^scsiCtlr(\d+)$/i);
    if (m != null && m.length > 1) {
      var ix = m[1];

      if (vm.hwv() < main.defaultHwv) {
        lockHwvRow();
      } else if (ix == 0) {
        lockVdevRow();
      } else {
        unlockVdevRow();
      }
    }
    
    // Fix bug 31274
    // - If the current OS is NetWare and if the config file contained an 
    // unsupported/unknown virtual device then update the virtual device
    // list to display nothing but vlance and the unsupported/unknown device 
    // ( marked "not supported" ). In other words, give the user a chance
    // to select the supported virtual device. Ref. bug 31274
    // - If the selected virtual device is supported then do not list
    // any other device that is not supported to prevent the user
    // from selecting an unsupported virtual device thro' MUI.
    dom.vdevSlct = obj("vdevSlct");
    if (vm.os().match(/^netware\d$/i)) {
      var matched = false;
      dom.vdevSlct = obj("vdevSlct");
      var options = dom.vdevSlct.options;
      for (var i = 0; i < options.length; i++) {
        if (options[i].value != "vmxlsilogic") {
          // for netware vm we do not support anything other than vmxlsilogic
          if (!matched && options[i].value == dev.vdev) {
            alert("Warning: " + dev.vdev + " is not supported by " + vm.dn());
            dom.vdevSlct.options[i].text += " (not supported)";
            matched = true;
          }
          else {
            dom.vdevSlct.options[i] = null;
          }
        }
      }
      setTimeout("parent.layoutCb()", 40);
    }

    if (!supported) {
      alert("Warning: " + dev.vdev + " is not supported.");
      dom.vdevSlct.options[dom.vdevSlct.options.length] = 
        new Option(dev.vdev + " (not supported)", dev.vdev);
      setTimeout("parent.layoutCb()", 40);
    }
   
    if (dom.vdevSlct.options.length == 1) {
      // harish: XXX - Add code to hide vdevLock message because with
      // just one virtual device there is no need for the default msg.
      dom.vdevSngl = obj("vdevSngl");
      dom.vdevSngl.innerHTML = dev.vdev;
      dom.vdevSngl.css("display", "");
      dom.vdevSlct.css("display", "none");
      setTimeout("parent.layoutCb()", 40);
    }

    oq.arg("vdev", dev.vdev);
    // Bus Sharing
    oq.arg("shbus", dev.shbus);

    // Make a new Query object to represent the modified form.
    eq = new Query(document.forms[0]);
    eq.sync(oq);
  }

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;

    obj("hdr").innerHTML = hrDev(dev);
    fillForm();

    setTimeout("parent.layoutCb()", 40);
  };

  editor.nxt[0] = svgMsg;
  editor.nxt[1] = hwvMsg;
  editor.exec = function (idx) {
    if (idx == 1) {
      if (confirm("If this virtual machine's hardware is upgraded, it can " +
        "only be powered on with ESX Server version 2.0 or later. If you " +
        "need to run this virtual machine on versions of ESX Server prior " +
        "to 2.0, then click Cancel. Upgrade virtual hardware now?")) {
        var q = new Query();
        q.action("/vm-config/index.pl");
        q.target(parent.op.win());
        q.arg("op", "upgrade");
        q.arg("vmid", vm.hash());
        ok = "upgrade";
        q.submit();

        return true;
      } else {
        return false;
      }
    }

    var a = eq.arg();
    for (var x = 0; x < a.length; x++) {
      eq.cache(a[x]);
    }

    // Submit the form if there are diffs or we're trying to add a new device
    // with default settings.
    if (parent.ctx == "vmAddDeviceWizard" || eq.diff(oq)) {
      ok = "mod";

      // Params should be qualified with their dev name before being submitted.
      for (var j = 0; j < eq.f.length; j++) {
        if (eq.f[j].name.match(/^(op|vmid|dev)$/i)) { continue; }
        eq.f[j].name = dev.id + "::" + eq.f[j].name;
      }

      if (parent.ctx.match(/^editor$/i)) {
        main.vmDevObsrvr.ignr(obsrvrStr);
        main.vmHwvObsrvr.ignr(obsrvrStr, lstnr, vm);
      }
      eq.submit();
    } else {
      exit();
    }
  };

  hwvMsg.nxt[0] = editor;
  hwvMsg.exec = function () {
    editor.prv = null;
    fillForm();
    setTimeout("parent.layoutCb()", 40);
  };

  lstnr = function (t, o, v) {
    if (v.id == dev.id && confirm("The SCSI controller configuration " +
      "has changed. Reload configuration?")) {
      fillForm();
    }
  };
  if (parent.ctx.match(/^editor$/i)) {
    main.vmDevObsrvr.lstn(obsrvrStr, lstnr, vm);
    main.vmHwvObsrvr.lstn(obsrvrStr, lstnr, vm);
  }

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  next();
}
