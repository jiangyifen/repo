/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// Original query, edited query
var oq, eq;
var ok = false;
var obsrvrStr;
var fields = ["avg", "peak", "burst"];
var scale = {b:1,k:1e3,m:1e6,g:1e9,t:1e12};

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
    main.vmRsrcNetObsrvr.lstn(obsrvrStr, lstnr, vm);
    prev();
    return;
  }

  if (ok) {
    // Update immediately instead of waiting for a refresh    
    var vm = main.sx.vms[new Query(parent.location.search).arg("vmid")[0]];
    var enbl = (eq.arg("nfshaper")[0] == 1);
    var val = {enable:enbl};
    if (enbl) {
      val.avg = eq.arg("avg")[0] * scale[eq.arg("avgUnits")[0] || "b"];
      val.peak = eq.arg("peak")[0] * scale[eq.arg("peakUnits")[0] || "b"];
      val.burst = eq.arg("burst")[0] * scale[eq.arg("burstUnits")[0] || "b"];
    }
    vm.rsrcNet("nfshaper", val);
  }

  next();
}


// --------------------------------------------------------------------------

function initPage() {
  var q = new Query(parent.location.search);
  var vm = main.sx.vms[q.arg("vmid")[0]];
  var dev = q.arg("dev")[0];

  obsrvrStr = "vmRsrcNetwork_" + vm.hash();

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  function fillForm() {
    // Make a new Query object to represent the original form.
    oq = new Query(document.forms[0]);
    oq.arg("vmid", vm.hash());
    oq.arg("dev", dev);

    // Nfshaper
    var nfshaper = vm.rsrcNet("nfshaper").val;
    oq.arg("nfshaper", nfshaper.enable ? 1 : 0);
    oq.arg("nfshaper-old", nfshaper.enable ? 1 : 0);
    
    if (nfshaper.enable) {
      var units = ["", "k", "m", "g", "t"];
      var re = /^(\d+) ?([kmgt]?)$/;
      var m;
      
      m = hrVal(nfshaper.avg, 0, "b", null, 1000, units).match(re);
      oq.arg("avg", m[1]);
      oq.arg("avgUnits", m[2]);
      
      m = hrVal(nfshaper.peak, 0, "b", null, 1000, units).match(re);
      oq.arg("peak", m[1]);
      oq.arg("peakUnits", m[2]);
      
      m = hrVal(nfshaper.burst, 0, "b", null, 1000, units).match(re);
      oq.arg("burst", m[1]);
      oq.arg("burstUnits", m[2]);
    }

    // Make a new Query object to represent the modified form.
    eq = new Query(document.forms[0]);
    eq.sync(oq);

    // Initialize form enable/disable state
    toggle();
  }

  toggle = function () {
    var dsbl = (eq.cache("nfshaper") != 1);
    for (var i in fields) {
      eq.f[fields[i]].disabled = dsbl;
      eq.f[fields[i] + "Units"].disabled = dsbl;
    }
  }

  function validate() {
    for (var i in fields) {
      if (!eq.arg(fields[i])[0].match(/^\d+$/)) {
        alert("Traffic shaping parameters must be positive integers");
        return false;
      }
    }

    var avg = eq.arg("avg")[0] * scale[eq.arg("avgUnits")[0] || "b"];
    var peak = eq.arg("peak")[0] * scale[eq.arg("peakUnits")[0] || "b"];
    if (avg > peak) {
      alert("Average bandwidth cannot be greater than peak bandwidth");
      return false;
    }

    return true;
  }

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;
    fillForm();
    setTimeout("parent.layoutCb()", 40);
  };

  editor.nxt[0] = svgMsg;
  editor.exec = function () {
    eq.cache();

    if (eq.diff(oq)) {
      if (validate()) {
        ok = true;
        main.vmRsrcNetObsrvr.ignr(obsrvrStr);
        eq.submit();
      } else {
        return false;
      }
    } else {
      exit();
    }
  };

  // Listen for updates; only prompt once on multiple simultaneous changes
  var pending = false;
  lstnr = function (t, o, v) {
    if (!pending) {
      pending = true;
      setTimeout("check()", 200);
    }
  };
  check = function() {
    if (confirm("The resource settings have changed. Reload configuration?")) {
      fillForm();
    }
    pending = false;
  };
  main.vmRsrcNetObsrvr.lstn(obsrvrStr, lstnr, vm);

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  next();
}
