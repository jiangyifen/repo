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
      main.vmExecutionStateObsrvr.lstn(obsrvrStr, rndrMode, vm);
      main.vmModeObsrvr.lstn(obsrvrStr, rndrMode, vm);
    }
    prev();
    return;
  }

  if (ok) {
    var vm = main.sx.vms[new Query(parent.location.search).arg("vmid")[0]];
    vm.dev(w.devs[0].id, w.devs[0]);
  }

  next();
}


// --------------------------------------------------------------------------

function initPage() {
  var w = parent.op.win();
  var q = new Query(parent.location.search);
  var vm = main.sx.vms[q.arg("vmid")[0]];
  var vdevs = ["vlance", "vmxnet"]; // supported virtual devices
  var dev;
  if (parent.ctx == "vmAddDeviceWizard") {
    // Create a temporary device with appropriate defaults.
    // XXX: Defaults should come from the server.
    dev = {vdev:"vlance",rdsc:true,rdcs:true,id:"nic",devnm:"",netnm:""};
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

  obsrvrStr = "vmNic_" + parent.ctx + "_" + vm.hash() + ":" + dev.id;

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));


  rndrMode = function () {
    oq.f.vdev.disabled = !((vm.mode() & 2) == 2 && vm.es() == esOff);

    // There are 2 checkboxes. Trying to set oq.f.rdcs.disabled will not work.
    var l = oq.list("rdcs")
    for (var j = 0; j < l.length; j++) {
      oq.f[l[j]].disabled = (vm.es() != esOn);
    }
  };

  function getDevByNet(net) {
    if (! net) { return null; }

    for (var n in w.nicNames) {
      if (w.nicNames[n] == net) {
        return n;
      }
    }

    return null;
  }

  function initNcToString() {
    var nq = new Query();
    var na = " (unavailable)";

    ncVal = function (devnm, netnm) {
      // The network is not configured.
      if (! netnm) {
        // But the device is mapped to an existing network label. Use it.
        if (defined(w.nicNames[devnm])) {
          netnm = w.nicNames[devnm];
        } else {
          netnm = "";
        }
      } else {
        var fdevnm = getDevByNet(netnm);
        if (defined(fdevnm)) {
          devnm = fdevnm;
        } // Otherwise, the network is misconfigured.
      }

      nq.arg("devnm", devnm);
      nq.arg("netnm", netnm);
      return nq.toString();
    };

    ncLbl = function (devnm, netnm) {
      // The network is not configured.
      if (! netnm) {
        // But the device is mapped to an existing network label.
        if (defined(w.nicNames[devnm])) {
          return w.nicNames[devnm];
        }

        // And the device does not exist.
        return devnm + na;
      }

      // The network is configured.
      if (defined(getDevByNet(netnm))) {
        return netnm;
      }

      // The network is misconfigured.
      return netnm + na;
    };
  }
  initNcToString();

  function setConnectionState(q, d) {
    // Connected
    q.select("rdcs", 1, d.rdcs);
    q.select("rdcs", 0, ! d.rdcs);
  }

  function fillForm () {
    // Make a new Query object to represent the original form.
    oq = new Query(document.forms[0]);
    oq.arg("vmid", vm.hash());
    oq.arg("dev", dev.id);

    // Use the operation appropriate for the current context.
    if (parent.ctx == "vmAddDeviceWizard") {
      oq.arg("op", "add");
    }

    // Connected
    setConnectionState(oq, dev);

    // Connect at Power On
    oq.select("rdsc", 1, dev.rdsc);
    oq.select("rdsc", 0, ! dev.rdsc);

    // Network Connection
    // oq.arg("netnm", dev.netnm ? dev.netnm : "");

    objCss(oq.f.devnm, "width", "200");

    if (parent.ctx != "vmAddDeviceWizard") {
      oq.arg("devnm", ncVal(dev.devnm, dev.netnm));
      if (oq.arg("devnm")[0] != ncVal(dev.devnm, dev.netnm)) {
        oq.f.devnm.options[oq.f.devnm.options.length] =
          new Option(ncLbl(dev.devnm, dev.netnm),
            ncVal(dev.devnm, dev.netnm));
        oq.arg("devnm", ncVal(dev.devnm, dev.netnm));
      }
    }

//     if (oq.arg("devnm")[0] != dev.devnm && dev.devnm != "") {
//       var s = "";
//       if (dev.devnm.match(/^(vmnic|bond)/i)) {
//         var net;
//         for (var n in w.nicNames) {
//           if (w.nicNames[n] == dev.devnm) {
//             net = n;
//             break;
//           }
//         }

//         if (! defined(net)) {
//           s = " (Unavailable)";
//         }
//         oq.f.devnm.options[oq.f.devnm.options.length] = new Option(dev.devnm + s,
//           dev.devnm);
//         oq.arg("devnm", dev.devnm);
//       }
//     }

    // Fix bug 31274
    // - If the current OS is NetWare and if the config file contained an
    // unsupported/unknown virtual device then update the virtual device
    // list to display nothing but vlance and the unsupported device
    // ( marked "not supported" ). In other words, give the user a chance
    // to select the supported virtual device.
    // - If the selected virtual device is supported then do not list
    // any other device that is not supported to prevent the user
    // from selecting an unsupported virtual device thro' MUI.
    var dom = {};
    dom.vdevSlct = obj("vdevSlct");
    if (vm.os().match(/^netware\d$/i)) {
      var matched = false;
      dom.vdevSlct = obj("vdevSlct");
      var options = dom.vdevSlct.options;
      for (var i = 0; i < options.length; i++) {
        if (options[i].value != "vlance") {
          // for netware vm we do not support anything other than vlance
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

    // Virtual Device
    oq.arg("vdev", dev.vdev);

    // Make a new Query object to represent the modified form.
    eq = new Query(document.forms[0]);
    eq.sync(oq);
  }

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;

    var vals = [];
    var lbls = [];

    for (var n in w.nicNames) {
      vals.push(ncVal(n, w.nicNames[n]));
      lbls.push(ncLbl(n, w.nicNames[n]));
    }

    obj("devnmSlct").innerHTML = htmlSlct("devnm", vals, dev.devnm, lbls);

    obj("hdr").innerHTML = hrDev(dev);

    fillForm();
    rndrMode();

    setTimeout("parent.layoutCb()", 40);
  };

  // If a network label is defined for a device, but only the device is
  // mentioned in the config file, then this will return true whether or not
  // the user hase actively modified the form.
  function diffdevnm(s) {
    var dq = new Query(s);
    return dq.arg("devnm")[0] != dev.devnm || dq.arg("netnm")[0] != dev.netnm;
  }

  editor.nxt[0] = svgMsg;
  editor.exec = function () {
    eq.cache();

    // Negating versions of the rdcs and rdsc checkboxes are hidden. Toggle
    // them like radio buttons.
    eq.select("rdcs", 0, ! eq.select("rdcs", "1"));
    eq.select("rdsc", 0, ! eq.select("rdsc", "1"));

    // Submit the form if there are diffs or we're trying to add a new device
    // with default settings. If a network label is defined for a device, but
    // only the device is mentioned in the config file, then the form will be
    // submitted despite the fact that the user did not actively change any
    // visible form values.
    if (parent.ctx == "vmAddDeviceWizard" || eq.diff(oq) ||
      diffdevnm(eq.arg("devnm")[0])) {
      ok = true;

      // Params should be qualified with their dev name before being submitted.
      for (var j = 0; j < eq.f.length; j++) {
        if (eq.f[j].name.match(/^(op|vmid|dev)$/i)) { continue; }
        eq.f[j].name = dev.id + "::" + eq.f[j].name;
      }

      if (parent.ctx.match(/^editor$/i)) {
        main.vmDevObsrvr.ignr(obsrvrStr, lstnr, vm);
        main.vmExecutionStateObsrvr.ignr(obsrvrStr, rndrMode, vm);
        main.vmModeObsrvr.ignr(obsrvrStr, rndrMode, vm);
      }
      eq.submit();
    } else {
      exit();
    }
  };

  lstnr = function (t, o, v) {
    if (v.id != dev.id) {
      return;
    }

    rdcsChangeOnly = true;

    // Has "Connect at Power On" changed?
    if (v.rdsc && oq.arg("rdsc")[0] == 0 ||
      ! v.rdsc && oq.arg("rdsc")[0] == 1 ) {
      rdcsChangeOnly = false;
    }

    // Has "Network Connection" changed?
    else if (ncVal(v.devnm, v.netnm) != oq.arg("devnm")[0]) {
      rdcsChangeOnly = false;
    }

    // Has "Virtual Device" changed?
    else if (v.vdev != oq.arg("vdev")[0]) {
      rdcsChangeOnly = false;
    }

    if (! rdcsChangeOnly && confirm("The network adapter configuration has " +
      "changed. Reload configuration?")) {
      fillForm();
    } else {
      setConnectionState(oq, dev);
      setConnectionState(eq, dev);
    }
  };

  if (parent.ctx.match(/^editor$/i)) {
    main.vmDevObsrvr.lstn(obsrvrStr, lstnr, vm);
    main.vmExecutionStateObsrvr.lstn(obsrvrStr, rndrMode, vm);
    main.vmModeObsrvr.lstn(obsrvrStr, rndrMode, vm);
  }

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  if (parent.ctx == "vmAddDeviceWizard") {
    w.location.replace("/vm-config?op=getVmNetDevData");
  } else {
    w.location.replace("/vm-config?vmid=" + vm.hash() + "&dev=" + esc(dev.id));
  }
}
