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

  var obsrvrStr = "vmMemory_" + vm.hash();
  var numa = (main.sx._numaInfo > 1);

  if (!main.admin) {
    var tip = "Only administrators may change resource settings";
    var dsbl = '<span class="note" title="' + tip + '">Edit...</span>';
    obj("editSet").innerHTML = dsbl;
    obj("editAff").innerHTML = dsbl;
  }

  dom.rsrc = obj("rsrc").getElementsByTagName("tbody")[0];

  if (!numa) {
    dom.rsrc.removeChild(obj("affinHdr"));
    dom.rsrc.removeChild(obj("affinRow"));
  }

  // ------------------------------------------------------------------------
  renderRsrc = function (t, v, r) {
    if (r == null) {
      return;
    } else if (r.id == "shares" || r.id == "min" || r.id == "max") {
      obj(r.id).innerHTML = r.val;
    } else if (r.id == "affinity") {
      if (numa) obj(r.id).innerHTML = r.val;
    } else {
      obj(r.id).innerHTML = hrVal(r.val, 1, "k");
    }
  };

  // ------------------------------------------------------------------------
  mod = function () {
    var u = "/vmware/en/vmCfgContainer.html?unit=rsrc&dev=mem&vmid=" + vm.hash();
    var n = main.user + "@" + main.sx._name + ":" + vm.cfg() + ":rsrcMem";

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

    for (var i in vm._rsrcMem) {
      renderRsrc(main.vmRsrcMemObsrvr.t, vm, vm.rsrcMem(i));
    }

    main.vmRsrcMemObsrvr.lstn(obsrvrStr, renderRsrc, vm);

    setTimeout("parent.adjustSize()", 40);
  };


  // ------------------------------------------------------------------------
  doDn = function () {
    // Toggle extra info off.
    if ((vm.extra() & 8) == 8) {
      vm.extra(8);
    }

    main.vmRsrcMemObsrvr.ignr(obsrvrStr, renderRsrc, vm);
  };

  setTimeout("tryUpdates();", 250);
}

