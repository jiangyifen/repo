/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// Original query, edited query
var oq, eq;
var ok = false;
var obsrvrStr;
var fields = ["min", "shares"];
var numa;

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
    main.vmRsrcMemObsrvr.lstn(obsrvrStr, lstnr, vm);
    prev();
    return;
  }

  if (ok) {
    // Update immediately instead of waiting for a refresh
    var vm = main.sx.vms[new Query(parent.location.search).arg("vmid")[0]];
    for (var i in fields) {
      vm.rsrcMem(fields[i], eq.arg(fields[i])[0]);
    }
    if (numa) {
      vm.rsrcMem("affinity", affinityStr());
    }
  }

  next();
}


// --------------------------------------------------------------------------

function initPage() {
  var q = new Query(parent.location.search);
  var vm = main.sx.vms[q.arg("vmid")[0]];
  var dev = q.arg("dev")[0];

  obsrvrStr = "vmRsrcMemory_" + vm.hash();

  numa = (main.sx._numaInfo > 1);

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  var dom = {};
  dom.tbl = obj("tbl").getElementsByTagName("tbody")[0];

  if (numa) {
    // Create one checkbox for each NUMA node
    dom.affin = {};
    dom.affin.cell = obj("affinCell");
    dom.affin.node = dom.affin.cell.removeChild(obj("affinNode"));
    for (var i = 0; i < main.sx._numaInfo; i++) {
      var node = dom.affin.node.cloneNode(true);
      node.innerHTML = i + '<input type="checkbox" name="chk' + i + '" value="1" />&nbsp;';
      dom.affin.cell.appendChild(node);
    }
  } else {
    dom.tbl.removeChild(obj("affinHdr"));
    dom.tbl.removeChild(obj("affinRow"));
  }

  function fillForm() {
    // Make a new Query object to represent the original form.
    oq = new Query(document.forms[0]);
    oq.arg("vmid", vm.hash());
    oq.arg("dev", dev);

    for (var i in fields) {
      oq.arg(fields[i], vm.rsrcMem(fields[i]).val);
    }
    if (numa) {
      var affinity = vm.rsrcMem("affinity").val.split(",");
      for (var i = 0; i < main.sx._numaInfo; i++) {
        var chk = (grep(affinity, "all") || grep(affinity, i)) ? 1 : 0;
        oq.arg("chk" + i, chk);
      }
    }

    // Make a new Query object to represent the modified form.
    eq = new Query(document.forms[0]);
    eq.sync(oq);
  }

//------------------------------------------------------------------------------
  function validate() {

    // validate minimum memory input
    var minValue = eq.arg("min")[0];
    if ( minValue.match(/^\d+$/) == null || minValue < 1 ){
      window.alert("Minimum memory settings must be whole positive integers and at least 1MB !");
      return false;
    } else if (minValue > vm.rsrcMem("max").val){ 
      window.alert("Minimum memory cannot be greater than the maximum (" + 
		   vm.rsrcMem("max").val + " MB)");
      return false;
    }

    // validate shares input
    var sharesValue = eq.arg("shares")[0];
    if ( !sharesValue.match(/^(high|normal|low)$/) ){
      if( sharesValue.match(/^\d+$/) == null || 
	  sharesValue<0 || 
	  sharesValue>100000){ 
	    window.alert("Shares settings must be whole positive integers between 0 and 100000" +
			  "\nor"+
			  "\nOne of the special named values: high, normal or low !");
	    return false;
      }
    }

    return true; // inputs are valid minimum and shares memory values
  }



  // Convert the checkboxes into an affinity specification string
  affinityStr = function () {
    var a = [];
    for (var i = 0; i < main.sx._numaInfo; i++) {
      if (eq.arg("chk" + i)[0] == 1) {
        a.push(i);
      }
    }
    return (a.length == main.sx._numaInfo || a.length == 0) ? "all" : a.join(",");
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
      if (numa) {
        // Generate affinity string from checkboxes
        eq.arg("affinity", affinityStr());
      }
      if (validate()) {
	
	// make sure integer inputs are base 10
	eq.arg("min",parseInt(eq.arg("min")[0],10));    
	eq.arg("shares",parseInt(eq.arg("shares")[0],10));

        ok = true;
        main.vmRsrcMemObsrvr.ignr(obsrvrStr);
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
  main.vmRsrcMemObsrvr.lstn(obsrvrStr, lstnr, vm);

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  next();
}
