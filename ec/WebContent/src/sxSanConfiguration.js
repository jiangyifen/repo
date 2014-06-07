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

  if (ok) {
    ok = false;
    fillForm();
    prev();
    return;
  }

  next();
}


// --------------------------------------------------------------------------

function initPage() {
  if (parent.ready == null || ! parent.ready()) { window.setTimeout(initPage, 50); return; }

  parent.slctBtns("cls");

  var w = parent.op.win();
  var data;

  var dom = {};
  dom.bdy = obj("tbl").getElementsByTagName("tbody")[0];
  dom.devRow = dom.bdy.removeChild(obj("devRow"));
  dom.keyRow = dom.bdy.removeChild(obj("keyRow"));
  dom.noCtlrs = obj("noCtlrs");

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var scnMsg = new Step("scnMsg", obj("scnMsg"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  fillForm = function () {
    data = clone(w.data);

    for (var n in data.adapters) {
      var d = data.adapters[n];
      var kri = n + ":trgts";
      dom[kri].val.innerHTML = d.trgts;
    }
  }

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (idx) {
    data = clone(w.data);

    var cnt = 0;
    for (var n in data.adapters) {
      var d = data.adapters[n];
      var kri;

      dom[n + ":row"] = dom.bdy.appendChild(dom.devRow.cloneNode(true));
      objAtt(dom[n + ":row"].devRow, "id", "");

      dom[n + ":hdr"] = document.getElementById("dev");
      objAtt(dom[n + ":hdr"], "id", "");
      dom[n + ":hdr"].innerHTML = n;

      dom[n + ":scanLink"] = document.getElementById("scanLink");
      dom[n + ":scanLink"].ctrl = true;
      dom[n + ":scanLink"].dev = n;
      lstn(dom[n + ":scanLink"], "click", rescan);
      objAtt(dom[n + ":scanLink"], "id", "");

      dom[n + ":saveLink"] = document.getElementById("saveLink");
      dom[n + ":saveLink"].ctrl = true;
      dom[n + ":saveLink"].bsf =  d.bus + ":" + d.dev + ":" + d.fun;
      lstn(dom[n + ":saveLink"], "click", save);
      objAtt(dom[n + ":saveLink"], "id", "");

      kri = initKeyRow(n, "trgts", "Device Targets",
        dom, dom.bdy, dom.keyRow);
      dom[kri].val.innerHTML = d.trgts;

      kri = initKeyRow(n, "dev", "Device Name",
        dom, dom.bdy, dom.keyRow);
      dom[kri].val.innerHTML = d.dnm;

      kri = initKeyRow(n, "driver", "Driver",
        dom, dom.bdy, dom.keyRow);
      dom[kri].val.innerHTML = d.drvrs[d.slctDrvr];

      kri = initKeyRow(n, "pciid", "PCI Bus:Device.Function",
        dom, dom.bdy, dom.keyRow);
      dom[kri].val.innerHTML = d.bus + ":" + d.dev + "." + d.fun;

      kri = initKeyRow(n, "wwpn", "World Wide Port Name",
        dom, dom.bdy, dom.keyRow);
      dom[kri].val.innerHTML = d.wwpn;

      cnt++;
    }

    if (cnt == 0) {
      dom.noCtlrs.css("display", "");
    } else {
      dom.noCtlrs.css("display", "none");
    }
  };

  function getQ() {
    var q = new Query();
    q.action("/sx-san");
    q.target(parent.op.win());
    return q;
  }

  rescan = function (e) {
    var v = getTrgt(eObj(e)).dev;
    var q = getQ();
    q.arg("rescan", v);
    ok = true;
    q.submit();
    next(0);
  };

  save = function (e) {
    var v = getTrgt(eObj(e)).bsf;
    var q = getQ();
    q.arg("save", v);
    ok = true;
    q.submit();
    next(1);
  };

  editor.nxt[0] = scnMsg;
  editor.nxt[1] = svgMsg;

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  w.location.replace("/sx-san");
}
