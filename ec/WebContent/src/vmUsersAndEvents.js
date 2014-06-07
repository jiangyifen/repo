/* Copyright 1998 VMware, Inc.  All rights reserved. -- VMware Confidential */

// vmUsersAndEvents.js


// --------------------------------------------------------------------------
//
// initPage --
//
// --------------------------------------------------------------------------

function initPage() {
  var vm;
  var dom = new Object();
  var count = 0;

  var s = parent.location.search.substr(1);
  s = s.split('&');
  for (var i = 0; i < s.length; i++) {
    var h = s[i].split('=');
    if (h[0] == 'h') {
      hash = h[1];
      vm = main.sx.vms[hash];
      break;
    }
  }

  var obsrvrStr = "vmUsersAndEvents_" + vm.hash();

  dom.conTbl = obj("conTable").getElementsByTagName("tbody")[0];
  dom.whoRow = dom.conTbl.removeChild(obj("whoRow"));
  dom.whenRow = dom.conTbl.removeChild(obj("whenRow"));
  dom.whereRow = dom.conTbl.removeChild(obj("whereRow"));

  dom.user = obj("user");
  dom.r = obj("r");
  dom.w = obj("w");
  dom.x = obj("x");

  dom.noCon = obj("noCon");
  dom.noCon.css("display", "none");

  dom.evts = obj("evts");

  dom.cons = new Array();


  // ------------------------------------------------------------------------
  function pad(t) {
    return t < 10 ? "0" + t : t;
  };


  // ------------------------------------------------------------------------
  deferRenderUsr = function () {
    dom.user.innerHTML = "Current User (" + main.user + ")";
    dom.x.innerHTML = (vm.mode() & 1) == 0 ? "Deny" : "Allow";
    dom.w.innerHTML = (vm.mode() & 2) == 0 ? "Deny" : "Allow";
    dom.r.innerHTML = (vm.mode() & 4) == 0 ? "Deny" : "Allow";

    setTimeout("parent.adjustSize()", 40);
  };

  deferRenderCon = function (c) {
    c = vm.con(c);

    if (dom.cons[c.id] != null) {
      dom.cons[c.id].who.innerHTML = c.a;
      dom.cons[c.id].when.innerHTML = new Date(parseInt(c.b/1000)).toString();
      dom.cons[c.id].where.innerHTML = c.c;
    }
  };

  // ------------------------------------------------------------------------
  remCon = function (t, v, c) {
    dom.conTbl.removeChild(dom.cons[c.id].whoRow);
    dom.conTbl.removeChild(dom.cons[c.id].whenRow);
    dom.conTbl.removeChild(dom.cons[c.id].whereRow);
    delete dom.cons[c.id];

    count--;

    if (count > 0) {
      dom.noCon.css("display","none");
    } else {
      dom.noCon.css("display","");
    }

    setTimeout("parent.adjustSize()", 40);
  };

  // ------------------------------------------------------------------------
  renderCon = function (t, v, c) {
    if (c == null) return;

    if (dom.cons[c.id] == null) {
      dom.cons[c.id] = new Object();

      dom.cons[c.id].whoRow =
        dom.conTbl.appendChild(dom.whoRow.cloneNode(true));
      objAtt(dom.cons[c.id].whoRow, "id", "con_" + c.id + "_who");

      dom.cons[c.id].who = document.getElementById("who");
      objAtt(dom.cons[c.id].who, "id", "");

      dom.cons[c.id].whenRow =
        dom.conTbl.appendChild(dom.whenRow.cloneNode(true));
      objAtt(dom.cons[c.id].whenRow, "id", "con_" + c.id + "_when");

      dom.cons[c.id].when = document.getElementById("when");
      objAtt(dom.cons[c.id].when, "id", "");

      dom.cons[c.id].whereRow =
        dom.conTbl.appendChild(dom.whereRow.cloneNode(true));
      objAtt(dom.cons[c.id].whereRow, "id", "con_" + c.id + "_where");

      dom.cons[c.id].where = document.getElementById("where");
      objAtt(dom.cons[c.id].where, "id", "");

      count++;
    }

    if (count > 0) {
      dom.noCon.css("display","none");
    } else {
      dom.noCon.css("display","");
    }

    setTimeout("deferRenderCon('" + c.id + "');", 250);
  };


  // ------------------------------------------------------------------------
  tryUpdates = function () {
    if (! main || main.getUpdates == null || ! main.getUpdates()) {
      window.setTimeout("tryUpdates();", 250);
    }
  };


  // ------------------------------------------------------------------------
  doUp = function () {
    // Toggle extra info on.
    if ((vm.extra() & 4) == 0) {
      vm.extra(4);
    }

    for (var n in dom.cons) {
      dom.conTbl.removeChild(dom.cons[n].whoRow);
      dom.conTbl.removeChild(dom.cons[n].whenRow);
      dom.conTbl.removeChild(dom.cons[n].whereRow);
      delete dom.cons[n];
    }
    count = 0;
    dom.noCon.css("display","");

    for (var n in vm._con) {
      renderCon(main.vmConObsrvr.t, vm, vm.con(n));
    }

    setTimeout("deferRenderUsr();", 250);
    if (dom.evts.win().doUp != null) dom.evts.win().doUp();

    main.vmConObsrvr.lstn(obsrvrStr, renderCon, vm);
    main.vmRemConObsrvr.lstn(obsrvrStr, remCon, vm);
    main.vmModeObsrvr.lstn(obsrvrStr, deferRenderUsr, vm);
  };


  // ------------------------------------------------------------------------
  doDn = function () {
    // Toggle extra info off.
    if ((vm.extra() & 4) == 4) {
      vm.extra(4);
    }

    if (dom.evts.win().doDn != null) dom.evts.win().doDn();

    main.vmConObsrvr.ignr(obsrvrStr, renderCon, vm);
    main.vmRemConObsrvr.ignr(obsrvrStr, remCon, vm);
    main.vmModeObsrvr.ignr(obsrvrStr, deferRenderUsr, vm);
  };

  setTimeout("tryUpdates();", 250);
}
