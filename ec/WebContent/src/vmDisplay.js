/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// Original query, edited query
var oq, eq;
var ok = false;


// --------------------------------------------------------------------------

function exit(i) {
  parent.close();
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
    main.vmDevObsrvr.lstn(obsrvrStr, lstnr, vm);
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
  var dev = vm.dev(q.arg("dev")[0]);
  var obsrvrStr = "vmDisplay_" + vm.hash();

  var dom = {};

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  function fillForm () {
    // Make a new Query object to represent the original form.
    oq = new Query(document.forms[0]);
    oq.arg("vmid", vm.hash());
    oq.arg("dev", dev.id);

    // Device
    oq.arg("depth", dev.depth);

    // Make a new Query object to represent the modified form.
    eq = new Query(document.forms[0]);
    eq.sync(oq);
  }

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;

    fillForm();

    setTimeout("parent.layoutCb()", 40);
  };

  editor.nxt[0] = svgMsg;
  editor.exec = function () {
    var a = eq.arg();
    for (var x = 0; x < a.length; x++) {
      eq.cache(a[x]);
    }

    if (eq.diff(oq)) {
      ok = true;

      // Params should be qualified with their dev name before being submitted.
      for (var j = 0; j < eq.f.length; j++) {
        if (eq.f[j].name.match(/^(op|vmid|dev)$/i)) { continue; }
        eq.f[j].name = dev.id + "::" + eq.f[j].name;
      }

      main.vmDevObsrvr.ignr(obsrvrStr);
      eq.submit();
    } else {
      exit();
    }
  };

  lstnr = function (t, o, v) {
    if (v.id == dev.id && confirm("The display configuration " +
      "has changed. Reload configuration?")) {
      fillForm();
    }
  };
  main.vmDevObsrvr.lstn(obsrvrStr, lstnr, vm);

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  next();
}
