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

  var obsrvrStr = "vmDisk_" + vm.hash();

  if (!main.admin) {
    var tip = "Only administrators may change resource settings";
    var dsbl = '<span class="note" title="' + tip + '">Edit...</span>';
    obj("editShr").innerHTML = dsbl;
  }

  dom.perf = obj("perf").getElementsByTagName("tbody")[0];
  dom.rdRow = dom.perf.removeChild(obj("rdRow"));
  dom.rd = {};
  dom.wrRow = dom.perf.removeChild(obj("wrRow"));
  dom.wr = {};

  dom.rsrc = obj("rsrc").getElementsByTagName("tbody")[0];
  dom.shRow = dom.rsrc.removeChild(obj("shRow"));
  dom.sh = {};

  // ------------------------------------------------------------------------
  addTarget = function(id) {
    // Read bandwidth
    dom.rd[id] = dom.perf.insertBefore(dom.rdRow.cloneNode(true), obj("wrHdr"));
    dom.rd[id].lbl = document.getElementById("rdLbl");
    objAtt(dom.rd[id].lbl, "id", "");
    dom.rd[id].lbl.innerHTML = id;
    dom.rd[id].val = document.getElementById("rdVal");
    objAtt(dom.rd[id].val, "id", "");
    // Write bandwidth
    dom.wr[id] = dom.perf.appendChild(dom.wrRow.cloneNode(true));
    dom.wr[id].lbl = document.getElementById("wrLbl");
    objAtt(dom.wr[id].lbl, "id", "");
    dom.wr[id].lbl.innerHTML = id;
    dom.wr[id].val = document.getElementById("wrVal");
    objAtt(dom.wr[id].val, "id", "");
    // Shares
    dom.sh[id] = dom.rsrc.appendChild(dom.shRow.cloneNode(true));
    dom.sh[id].lbl = document.getElementById("shLbl");
    objAtt(dom.sh[id].lbl, "id", "");
    dom.sh[id].lbl.innerHTML = id;
    dom.sh[id].val = document.getElementById("shVal");
    objAtt(dom.sh[id].val, "id", "");
    // Resize the window
    setTimeout("parent.adjustSize()", 40);
  };

  // ------------------------------------------------------------------------
  remTarget = function(id) {
    dom.perf.removeChild(dom.rd[id]);
    delete dom.rd[id];
    dom.perf.removeChild(dom.wr[id]);
    delete dom.wr[id];
    dom.rsrc.removeChild(dom.sh[id]);
    delete dom.sh[id];

    var cnt = 0;
    for (var n in vm._rsrcDsk) {
      cnt++;
    }

    // The resource we are currently removing will still be counted.
    if (cnt <= 1) {
      obj("dsks").css("display", "none");
      obj("noDsks").css("display", "");
    }

    setTimeout("parent.adjustSize()", 40);
  };

  // ------------------------------------------------------------------------
  var bwFmts = ["Bps", "KBps", "MBps", "GBps", "TBps"];
  renderRsrc = function (t, v, r) {
    if (r == null) return;
    if (dom.rd[r.id] == null) {
      addTarget(r.id);
      obj("dsks").css("display", "");
      obj("noDsks").css("display", "none");
    }
    if (vm.hash() == "console") {
      dom.rd[r.id].val.innerHTML = "Not available";
      dom.wr[r.id].val.innerHTML = "Not available";
    } else {
      dom.rd[r.id].val.innerHTML = hrVal(r.val.read, 1, "k", null, 1024, bwFmts);
      dom.wr[r.id].val.innerHTML = hrVal(r.val.write, 1, "k", null, 1024, bwFmts);
    }
    dom.sh[r.id].val.innerHTML = r.val.shares;
  };

  // ------------------------------------------------------------------------
  remRsrc = function (t, v, r) {
    remTarget(r.id);
  };

  // ------------------------------------------------------------------------
  mod = function () {
    var u = "/vmware/en/vmCfgContainer.html?unit=rsrc&dev=dsk&vmid=" + vm.hash();
    var n = main.user + "@" + main.sx._name + ":" + vm.cfg() + ":rsrcDsk";

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

    for (var n in dom.rd) {
      if (vm.rsrcDsk(n) == null) {
        remTarget(n);
      }
    }

    var cnt = 0;
    for (var i in vm._rsrcDsk) {
      renderRsrc(main.vmRsrcDskObsrvr.t, vm, vm.rsrcDsk(i));
      cnt++;
    }

    if (cnt == 0) {
      obj("dsks").css("display", "none");
      obj("noDsks").css("display", "");
    } // renderRsrc already unhides the stats and config columns as necessary.

    main.vmRsrcDskObsrvr.lstn(obsrvrStr, renderRsrc, vm);
    main.vmRemRsrcDskObsrvr.lstn(obsrvrStr, remRsrc, vm);

    setTimeout("parent.adjustSize()", 40);
  };


  // ------------------------------------------------------------------------
  doDn = function () {
    // Toggle extra info off.
    if ((vm.extra() & 8) == 8) {
      vm.extra(8);
    }

    main.vmRsrcDskObsrvr.ignr(obsrvrStr, renderRsrc, vm);
    main.vmRemRsrcDskObsrvr.ignr(obsrvrStr, remRsrc, vm);
  };

  setTimeout("tryUpdates();", 250);
}

