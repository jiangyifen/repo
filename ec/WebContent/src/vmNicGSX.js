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
    dev = {vdev:"vmxnet",id:"nic",hi:"bridged",rdsc:true,rdcs:false};
  } else {
    dev = vm.dev(q.arg("dev")[0]);
  }

  obsrvrStr = "vmNic_" + parent.ctx + "_" + vm.hash() + ":" + dev.id;

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));


  rndrMode = function () {
    oq.f.vdev.disabled = !((vm.mode() & 2) == 2 && vm.es() == esOff);

    // There are 2 checkboxes. Trying to set oq.f.rdcs.disabled will not work.
    var l = oq.list("rdcs");
    for (var j = 0; j < l.length; j++) {
      oq.f[l[j]].disabled = vm.es() != esOn;
    }
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

    // Connected
    oq.select("rdcs", 1, dev.rdcs);
    oq.select("rdcs", 0, ! dev.rdcs);

    // Connect at Power On
    oq.select("rdsc", 1, dev.rdsc);
    oq.select("rdsc", 0, ! dev.rdsc);

    // Virtual Device
    oq.arg("vdev", dev.vdev);

    // init the custom slection depending on the host
    if (main.sx._os == "win32") {
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("Vmnet0", "vmnet0");
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("Vmnet1", "vmnet1");
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("Vmnet2", "vmnet2");
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("Vmnet3", "vmnet3");
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("Vmnet4", "vmnet4");
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("Vmnet5", "vmnet5");
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("Vmnet6", "vmnet6");
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("Vmnet7", "vmnet7");
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("Vmnet8", "vmnet8");
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("Vmnet9", "vmnet9");

      oq.arg("vmnet", "Vmnet0");
    } else {
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("/dev/vmnet0", "/dev/vmnet0");
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("/dev/vmnet1", "/dev/vmnet1");
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("/dev/vmnet2", "/dev/vmnet2");
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("/dev/vmnet3", "/dev/vmnet3");
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("/dev/vmnet4", "/dev/vmnet4");
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("/dev/vmnet5", "/dev/vmnet5");
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("/dev/vmnet6", "/dev/vmnet6");
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("/dev/vmnet7", "/dev/vmnet7");
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("/dev/vmnet8", "/dev/vmnet8");
      oq.f.vmnet.options[oq.f.vmnet.options.length] = 
	new Option("/dev/vmnet9", "/dev/vmnet9");

      oq.arg("vmnet", "/dev/vmnet0");
    }

    // init the network name selection
    var found = false;
    for (var n in w.nicNames) {
      found = found || (w.nicNames[n] == dev.netnm);
      oq.f.netnm.options[oq.f.netnm.options.length] =
         new Option(w.nicNames[n], w.nicNames[n]);
    }

    // don't allow named to be selected if no names exist
    if (oq.f.netnm.options.length == 0) {
      oq.f.hi[3].disabled = true;
      obj("named").att("class", "dsbld");
      obj("named").att("href", "javascript:;");
    } else {
      oq.f.hi[3].disabled = false;
      obj("named").att("class", "radioLabel");
      obj("named").att("href", "javascript:slct('named');");
    }

    // Preserve existing setting even if network name is invalid
    if (!found && dev.netnm != "") {
      oq.f.netnm.options[oq.f.netnm.options.length] =
         new Option(dev.netnm, dev.netnm);
    }

    // set network name (if specified)
    oq.arg("netnm", dev.netnm);

    // set vmnet (custom) type
    oq.arg("vmnet", dev.vmnet);

    // set the connection type
    oq.arg("hi", dev.hi);

    // Make a new Query object to represent the modified form.
    eq = new Query(document.forms[0]);
    eq.sync(oq);
    oq.sync(eq);
  }

  hilite = function(hi) {
    obj("bridged").att("class", "radioLabel");
    obj("nat").att("class", "radioLabel");
    obj("hostOnly").att("class", "radioLabel");
    if (obj("named").att("class") != "dsbld") {
      obj("named").att("class", "radioLabel");
    }
    obj("custom").att("class", "radioLabel");

    obj(hi).att("class", "slctd");

    // enable disable the custom select widgets
    eq.f.vmnet.disabled = hi != "custom";
    eq.f.netnm.disabled = hi != "named";
  };

  slct = function (hi) {
    eq.arg("hi", hi);
    hilite(hi);
  };

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;

    obj("hdr").innerHTML = hrDev(dev);

    fillForm();
    rndrMode();
    hilite(dev.hi);

    setTimeout("parent.layoutCb()", 40);
  };


  editor.nxt[0] = svgMsg;
  editor.exec = function () {
    var a = eq.arg();
    for (var x = 0; x < a.length; x++) {
      eq.cache(a[x]);
    }

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
        main.vmExecutionStateObsrvr.ignr(obsrvrStr);
        main.vmModeObsrvr.ignr(obsrvrStr);
      }

      eq.submit();
    } else {
      exit();
    }
  };


  lstnr = function (t, o, v) {
    if (v.id == dev.id && confirm("The network adapter configuration " +
      "has changed. Reload configuration?")) {
      fillForm();
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
    w.location.replace("/vm-config/index.pl?op=getVmNetDevData");
  } else {
    w.location.replace("/vm-config/index.pl?vmid=" + vm.hash() + "&dev=" + esc(dev.id));
  }
}
