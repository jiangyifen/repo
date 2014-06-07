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

  var obsrvrStr = "vmNetwork_" + vm.hash();

  if (!main.admin) {
    var tip = "Only administrators may change resource settings";
    var dsbl = '<span class="note" title="' + tip + '">Edit...</span>';
    obj("editShp").innerHTML = dsbl;
  }

  dom.perf = obj("perf").getElementsByTagName("tbody")[0];
  dom.rxRow = dom.perf.removeChild(obj("rxRow"));
  dom.rx = {};
  dom.txRow = dom.perf.removeChild(obj("txRow"));
  dom.tx = {};

  // ------------------------------------------------------------------------
  addNic = function(id) {
    // Receive bandwidth
    dom.rx[id] = dom.perf.insertBefore(dom.rxRow.cloneNode(true), obj("txHdr"));
    dom.rx[id].lbl = document.getElementById("rxLbl");
    objAtt(dom.rx[id].lbl, "id", "");
    dom.rx[id].lbl.innerHTML = id;
    dom.rx[id].val = document.getElementById("rxVal");
    objAtt(dom.rx[id].val, "id", "");
    // Transmit bandwidth
    dom.tx[id] = dom.perf.appendChild(dom.txRow.cloneNode(true));
    dom.tx[id].lbl = document.getElementById("txLbl");
    objAtt(dom.tx[id].lbl, "id", "");
    dom.tx[id].lbl.innerHTML = id;
    dom.tx[id].val = document.getElementById("txVal");
    objAtt(dom.tx[id].val, "id", "");
    // Resize the window
    setTimeout("parent.adjustSize()", 40);
  };

  // ------------------------------------------------------------------------
  var bwFmts = ["bps", "Kbps", "Mbps", "Gbps", "Tbps"];
  renderRsrc = function (t, v, r) {
    if (r == null) {
      return;
    } else if (r.id == "nfshaper") {
      obj("shpVal").innerHTML = r.val.enable ? "Yes" : "No";
      obj("avgRow").css("display", r.val.enable ? "" : "none");
      obj("peakRow").css("display", r.val.enable ? "" : "none");
      obj("burstRow").css("display", r.val.enable ? "" : "none");
      if (r.val.enable) {
        obj("avgVal").innerHTML = hrVal(r.val.avg, 0, "b", null, 1000, bwFmts);
        obj("peakVal").innerHTML = hrVal(r.val.peak, 0, "b", null, 1000, bwFmts);
        obj("burstVal").innerHTML = hrVal(r.val.burst, 0, "b", null, 1000);
      }
      // Resize the window since rows may have appeared/disappeared
      setTimeout("parent.adjustSize()", 40);
    } else {
      if (dom.rx[r.id] == null) {
        addNic(r.id);
      }
      dom.rx[r.id].val.innerHTML = hrVal(r.val.rx, 1, "k", null, 1024, bwFmts);
      dom.tx[r.id].val.innerHTML = hrVal(r.val.tx, 1, "k", null, 1024, bwFmts);
    }
  };

  // ------------------------------------------------------------------------
  mod = function () {
    var u = "/vmware/en/vmCfgContainer.html?unit=rsrc&dev=net&vmid=" + vm.hash();
    var n = main.user + "@" + main.sx._name + ":" + vm.cfg() + ":rsrcNet";

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

    for (var i in vm._rsrcNet) {
      renderRsrc(main.vmRsrcNetObsrvr.t, vm, vm.rsrcNet(i));
    }

    main.vmRsrcNetObsrvr.lstn(obsrvrStr, renderRsrc, vm);

    setTimeout("parent.adjustSize()", 40);
  };


  // ------------------------------------------------------------------------
  doDn = function () {
    // Toggle extra info off.
    if ((vm.extra() & 8) == 8) {
      vm.extra(8);
    }

    main.vmRsrcNetObsrvr.ignr(obsrvrStr, renderRsrc, vm);
  };

  setTimeout("tryUpdates();", 250);
}

