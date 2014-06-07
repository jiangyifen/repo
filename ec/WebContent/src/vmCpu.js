/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// --------------------------------------------------------------------------
//
// initPage --
//
//      Initialize iframes.
//
// --------------------------------------------------------------------------

function initPage() {
  var vm;
  var dom = new Object();

  var s = parent.location.search.substr(1);
  s = s.split('&');
  for (var i = 0; i < s.length; i++) {
    var h = s[i].split('=');
    if (h[0] == 'h') {
      var hash = h[1];
      vm = main.sx.vms[hash];
      break;
    }
  }

  var obsrvrStr = "vmCpu_" + vm.hash();
  var canSetAffinity = (main.sx._cpuInfo > 1) && (vm.hash() != "console");
  var smpVm = (vm._vcpu > 1);

  if (!main.admin) {
    var tip = "Only administrators may change resource settings";
    var dsbl = '<span class="note" title="' + tip + '">Edit...</span>';
    obj("editSet").innerHTML = dsbl;
    obj("editAff").innerHTML = dsbl;
  }

  dom.val = new Object();
  dom.val["min"] = obj("minVal");
  dom.val["max"] = obj("maxVal");
  dom.val["shares"] = obj("shrVal");
  dom.val["ht"] = obj("htVal");   

  dom.rsrc = obj("rsrc").getElementsByTagName("tbody")[0];

  if ( !main.sx._htEnbl || main.sx._ht==0 ) {
    dom.rsrc.removeChild(obj("htRow"));
  }


  if (canSetAffinity) {
    dom.affinity = new Object();
    dom.affinity.row = obj("affinRow");
    dom.affinity.row.key = dom.affinity.row.getElementsByTagName("td")[0];
    dom.affinity.row.val = dom.affinity.row.getElementsByTagName("td")[1];

    if (smpVm) {
      dom.affinity.vcpu = new Array();
      for (var i = 0; i < vm._vcpu; i++) {
        var row = dom.affinity.row.cloneNode(true);
        objAtt(row, "id", "");
        row.key = row.getElementsByTagName("td")[0];
        row.val = row.getElementsByTagName("td")[1];
        row.key.innerHTML = "Run VCPU " + i + " on Processor(s)";
        dom.affinity.vcpu[i] = initXuaObj(dom.rsrc.appendChild(row));
      }
    }
  } else {
    dom.rsrc.removeChild(obj("affinHdr"));
    dom.rsrc.removeChild(obj("affinRow"));
  }

  dom.perf = obj("perf").getElementsByTagName("tbody")[0];
  dom.usageRow = dom.perf.removeChild(obj("usageRow"));
  for (var i = 0; i < vm._vcpu; i++) {
    // Create a row for this VCPU
    var idx = "p" + i + "usage";
    dom.perf.appendChild(dom.usageRow.cloneNode(true));
    // Set up the label: "CPU" for UP VMs, "VCPU <#>" for SMP VMs
    if (smpVm) {
      document.getElementById("usageLbl").innerHTML = "VCPU " + i;
    }
    objAtt(document.getElementById("usageLbl"), "id", "");
    // Set up the value
    dom.val[idx] = document.getElementById("usageVal");
    objAtt(dom.val[idx], "id", "");
  }

  // ------------------------------------------------------------------------
  renderRsrc = function (t, v, r) {
    if (r == null) {
      return;
    } else if (r.id == "usage") {
      for (var i = 0; i < r.val.length; i++) {
        var idx = "p" + i + r.id;
        if (vm.hash() == "console") {
          dom.val[idx].innerHTML = "Not available";
        } else {
          dom.val[idx].innerHTML = r.val[i] + "%";
        }
      }
    } else if (r.id == "affinity") {
      if (!canSetAffinity) return;
      if (!smpVm) {
        dom.affinity.row.val.innerHTML = affinStr(r.val[0]);
      } else if (r.val.length == 1) {
        dom.affinity.row.val.innerHTML = affinStr(r.val[0]);
        dom.affinity.row.css("display", "");
        for (var i = 0; i < vm._vcpu; i++) {
          dom.affinity.vcpu[i].css("display", "none");
        }
      } else {
        dom.affinity.row.css("display", "none");
        for (var i = 0; i < vm._vcpu; i++) {
          dom.affinity.vcpu[i].val.innerHTML = affinStr(r.val[i]);
          dom.affinity.vcpu[i].css("display", "");
        }
      }
    } else if (r.id == "ht") {
      if ( main.sx._htEnbl ) {
	if ( r.val=="any" ) {
	  dom.val[r.id].innerHTML = "No";
	} else {
	  dom.val[r.id].innerHTML = "Yes";
	}
      }
    } else {
      dom.val[r.id].innerHTML = r.val;
    } 
  };

  // ------------------------------------------------------------------------
  // affinStr: Convert a CPU affinity mask (in which the nth bit is set
  //           iff the VM/VCPU can run on CPU n) into a comma-separated
  //           list of CPUs.  For example, a mask of 0x15 would decode
  //           to the list "0,2,4".
  // ------------------------------------------------------------------------
  affinStr = function (m) {
    var a = new Array();
    for (var p = 0; m != 0; p++, m >>>= 1) {
      if (m & 1) a.push(p);
    }
    return a.join(',');
  };

  // ------------------------------------------------------------------------
  mod = function () {
    var u = "/vmware/en/vmCfgContainer.html?unit=rsrc&dev=cpu&vmid=" + vm.hash();
    var n = main.user + "@" + main.sx._name + ":" + vm.cfg() + ":rsrcCpu";

    n = escJs(n);
    var w = main.getRgWndw(n);

    if (w != null && ! w.closed) {
      w.focus();
    } else {
      w = window.open("", "", "width=560,height=200,resizable");
      w.document.location.replace(u);
      w.name = n;
      main.addRgWndw(w);
    }
  };

  // ------------------------------------------------------------------------
  tryUpdates = function () {
    if (! main || main.getUpdates == null || ! main.getUpdates()) {
      setTimeout("tryUpdates();", 250);
    }
  };

  // ------------------------------------------------------------------------
  doUp = function () {
    // Toggle extra info on.
    if ((vm.extra() & 8) == 0) {
      vm.extra(8);
    }

    for (var i in vm._rsrcCpu) {
      renderRsrc(main.vmRsrcCpuObsrvr.t, vm, vm.rsrcCpu(i));
    }

    main.vmRsrcCpuObsrvr.lstn(obsrvrStr, renderRsrc, vm);

    setTimeout("parent.adjustSize()", 40);
  };


  // ------------------------------------------------------------------------
  doDn = function () {
    // Toggle extra info off.
    if ((vm.extra() & 8) == 8) {
      vm.extra(8);
    }

    main.vmRsrcCpuObsrvr.ignr(obsrvrStr, renderRsrc, vm);
  };

  setTimeout("tryUpdates();", 250);
}

