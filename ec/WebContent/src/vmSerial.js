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

function slctDevice() {
  eq.cache("hi");

  var disable = true;
  if ( eq.arg("hi")[0] ==  "pipe") {
     disable = false;
  }

  eq.f.endp.disabled = disable;
  eq.f.tnrl.disabled = disable;
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
    dev = {hi:"device",rdsc:true,rdcs:false,id:"serial",loc:"/dev/ttyS0"};
    if (main.sx._os == "win32") {
      dev.loc = "COM1";
    }
  } else {
    dev = vm.dev(q.arg("dev")[0]);
  }
  obsrvrStr = "vmSerial_" + parent.ctx + "_" + vm.hash() + ":" + dev.id;

  var dom = {};

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

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

    // There are 2 checkboxes. Trying to set oq.f.rdcs.disabled will not work.
    var l = oq.list("rdcs")
    for (var j = 0; j < l.length; j++) {
      oq.f[l[j]].disabled = vm.es() != esOn;
    }

    // Connect at Power On
    oq.select("rdsc", 1, dev.rdsc);
    oq.select("rdsc", 0, ! dev.rdsc);

    // Device
    oq.arg("hi", dev.hi);

    // Location
    oq.arg("loc", dev.loc);

    // Location
    oq.select("poll", 1, dev.poll);
    oq.select("poll", 0, ! dev.poll);

    // enable/disable the pipe setting
    var disable = true;
    if (dev.hi ==  "pipe") {
       disable = false;
    }
    oq.f.endp.disabled = disable;
    oq.f.tnrl.disabled = disable;
    oq.arg("endp", dev.endp);
    oq.arg("tnrl", dev.tnrl);

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
  editor.exec = function () {
    var a = eq.arg();
    for (var x = 0; x < a.length; x++) {
      eq.cache(a[x]);
    }

    // Negating versions of the rdcs and rdsc checkboxes are hidden. Toggle
    // them like radio buttons.
    eq.select("rdcs", 0, ! eq.select("rdcs", "1"));
    eq.select("rdsc", 0, ! eq.select("rdsc", "1"));
    eq.select("poll", 0, ! eq.select("poll", "1"));

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
    if (v.id == dev.id && confirm("The serial port configuration " +
      "has changed. Reload configuration?")) {
      fillForm();
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

  next();
}
