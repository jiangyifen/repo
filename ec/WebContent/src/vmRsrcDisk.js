/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// Original query, edited query
var oq, eq;
var ok = false;
var obsrvrStr;
var dom = {};


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
    main.vmRsrcDskObsrvr.lstn(obsrvrStr, lstnr, vm);
    prev();
    return;
  }

  if (ok) {
    // Update immediately instead of waiting for a refresh
    var vm = main.sx.vms[new Query(parent.location.search).arg("vmid")[0]];
    for (var id in dom.tbl.tgt) {
      // XXX Kludge to work around brokenness of VM_GetSetRsrcDsk
      var oldVal = vm.rsrcDsk(id).val;
      var newVal = {};
      for (var key in oldVal) {
        newVal[key] = (key == "shares") ? eq.arg(id + "-shares")[0] : oldVal[key];
      }
      vm.rsrcDsk(id, newVal);
    }
  }

  next();
}


// --------------------------------------------------------------------------

function initPage() {
  var q = new Query(parent.location.search);
  var vm = main.sx.vms[q.arg("vmid")[0]];
  var dev = q.arg("dev")[0];

  obsrvrStr = "vmRsrcDisk_" + vm.hash();

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  dom.tbl = obj("tbl").getElementsByTagName("tbody")[0];
  dom.tbl.row = dom.tbl.removeChild(obj("row"));
  dom.tbl.tgt = {};

  function fillForm() {
    // Delete any existing table rows
    for (var id in dom.tbl.tgt) {
      dom.tbl.removeChild(dom.tbl.tgt[id]);
      delete dom.tbl.tgt[id];
    }

    // Create a row for each target
    for (var id in vm._rsrcDsk) {
      dom.tbl.tgt[id] = dom.tbl.appendChild(dom.tbl.row.cloneNode(true));
      var lbl = document.getElementById("lbl");
      objAtt(lbl, "id", "");
      lbl.innerHTML = id;
      var shr = document.getElementById("shr");
      objAtt(shr, "id", "");
      objAtt(shr, "name", id + "-shares");
    }

    // Make a new Query object to represent the original form.
    oq = new Query(document.forms[0]);
    oq.arg("vmid", vm.hash());
    oq.arg("dev", dev);

    // Shares
    for (var id in dom.tbl.tgt) {
      oq.arg(id + "-shares", vm.rsrcDsk(id).val.shares);
    }

    // Make a new Query object to represent the modified form.
    eq = new Query(document.forms[0]);
    eq.sync(oq);
  }

  function validate() {
    for (var id in dom.tbl.tgt) {
      if (! eq.arg(id + "-shares")[0].match(/^(high|normal|low|\d+)$/i)) {
        alert("Resource settings must be positive integers or " +
          "one of the special named values: high, normal or low.");
        return false;
      } else { // make sure base10 number
	eq.arg(id + "-shares", 
	       parseInt(eq.arg(id + "-shares")[0],10)); 
      }
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
        main.vmRsrcDskObsrvr.ignr(obsrvrStr);
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
  main.vmRsrcDskObsrvr.lstn(obsrvrStr, lstnr, vm);

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  next();
}
