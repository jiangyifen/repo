/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// XXX Punt on per-VCPU affinity for the time being

// Original query, edited query
var oq, eq;
var ok = false;
var obsrvrStr;
var fields = ["min", "max", "shares", "ht"];
var canSetAffinity = false;
var smpVm;

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
    main.vmRsrcCpuObsrvr.lstn(obsrvrStr, lstnr, vm);
    prev();
    return;
  }

  if (ok) {
    // Update immediately instead of waiting for a refresh
    var vm = main.sx.vms[new Query(parent.location.search).arg("vmid")[0]];
    for (var i in fields) {
      vm.rsrcCpu(fields[i], eq.arg(fields[i])[0]);
    }

    if ( !eq.arg("ht")[0] ) {
      vm.rsrcCpu("ht","any");
    } else if (eq.arg("ht")[0]==1 && vm._vcpu > 1) {
      vm.rsrcCpu("ht","internal");
    } else if (eq.arg("ht")[0]==1 && vm._vcpu == 1) {
      vm.rsrcCpu("ht","none");
    }

    if (canSetAffinity) {
      // Affinity is a list of masks -- one for each VCPU or one for all VCPUs.
      // This page currently only supports the latter case.
      vm.rsrcCpu("affinity", [affinMsk()]);
    }
  }

  next();

}


// --------------------------------------------------------------------------

function initPage() {
  
  var q = new Query(parent.location.search);
  var vm = main.sx.vms[q.arg("vmid")[0]];
  var dev = q.arg("dev")[0];

  obsrvrStr = "vmRsrcCpu_" + vm.hash();

  canSetAffinity = (main.sx._cpuInfo > 1) && (vm.hash() != "console");
  smpVm = (vm._vcpu > 1);
  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  var dom = {};
  dom.tbl = obj("tbl").getElementsByTagName("tbody")[0];

  if ( main.sx._ht == 0 || !main.sx._htEnbl ) {
    dom.tbl.removeChild(obj("htRow"));
  } else {
    obj("ht").checked = (vm.rsrcCpu(fields[3]).val=="any") ? false : true;
  }

  if (canSetAffinity) {
    // Create one pair of radiobuttons for each CPU
    dom.affin = {};
    dom.affin.cell = obj("affinCell");
    dom.affin.cpu = dom.affin.cell.removeChild(obj("affinCpu"));
    dom.noaffin = {};
    dom.noaffin.cell = obj("noAffinCell");
    dom.noaffin.cpu = dom.noaffin.cell.removeChild(obj("noAffinCpu"));
    for (var i = 0; i < main.sx._cpuInfo; i++) {
      var cpu = dom.affin.cpu.cloneNode(true);
      cpu.innerHTML = i + '<input type="radio" name="rad' + i + '" value="1" />&nbsp;';
      dom.affin.cell.appendChild(cpu);

      var cpu = dom.noaffin.cpu.cloneNode(true);
      cpu.innerHTML = i + '<input type="radio" name="rad' + i + '" value="0" />&nbsp;';
      dom.noaffin.cell.appendChild(cpu);

    }
  } else {
    dom.tbl.removeChild(obj("affinHdr"));
    dom.tbl.removeChild(obj("affinRow"));
    dom.tbl.removeChild(obj("noAffinRow"));
  }

  function fillForm() {
    // Make a new Query object to represent the original form.
    oq = new Query(document.forms[0]);
    oq.arg("vmid", vm.hash());
    oq.arg("dev", dev);

    for (var i in fields) {
      oq.arg(fields[i], vm.rsrcCpu(fields[i]).val);
    }
    if (canSetAffinity) {
      var affinity = vm.rsrcCpu("affinity").val[0];
      for (var i = 0; i < main.sx._cpuInfo; i++, affinity >>>= 1) {
        oq.arg("rad" + i, affinity & 1);
      }
    }

    // Make a new Query object to represent the modified form.
    eq = new Query(document.forms[0]);
    eq.sync(oq);
  }

  function validate() {
    var textFields = ["min","max","shares"];
    for (var i in textFields) {
      if (! eq.arg(textFields[i])[0].match(/^(high|normal|low|\d+)$/i)) {
        alert("Resource settings must be positive integers or " +
          "one of the special named values: high, normal or low.");
        return false;
      }
    }

    // make sure to treat nubmers as base 10 

    var shares = parseInt(eq.arg("shares")[0],10);
    var min = parseInt(eq.arg("min")[0],10);
    var max = parseInt(eq.arg("max")[0],10);

    if (min > max) {
      alert("Minimum cannot be greater than maximum");
      return false;
    } else if (min > 100 * vm._vcpu) {
      alert("Minimum cannot be greater than " + vm._vcpu + "00");
      return false;
    } else if (max > 100 * vm._vcpu) {
      alert("Maximum cannot be greater than " + vm._vcpu + "00");
      return false;
    }

    if (canSetAffinity && affinMsk() == 0) {
      alert("Must specify affinity for at least one processor");
      return false;
    }

    eq.arg("min",min);
    eq.arg("max",max);
    eq.arg("shares",shares);

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
      if (canSetAffinity) {
        // Generate affinity mask from radiobuttons
        eq.arg("affinity", affinMsk());
      }
      if (validate()) {
        ok = true;
        main.vmRsrcCpuObsrvr.ignr(obsrvrStr);
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
  main.vmRsrcCpuObsrvr.lstn(obsrvrStr, lstnr, vm);

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  next();
}

// Convert the radiobuttons into an affinity string
affinStr = function () {
  var a = [];
  for (var i = 0; i < main.sx._cpuInfo; i++) {
    if (eq.arg("rad" + i)[0] == 1) {
      a.push(i);
    }
  }
  return a.join(",");
}

// Convert the radiobuttons into an affinity mask
// (the nth bit is set if the nth CPU is available)
affinMsk = function () {
  var m = 0;
  for (var i = 0; i < main.sx._cpuInfo; i++) {
    if (eq.arg("rad" + i)[0] == 1) {
      m |= 1 << i;
    }
  }
  return m;
}
