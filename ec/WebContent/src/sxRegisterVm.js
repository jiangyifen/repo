/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// Original query, edited query
var oq, eq;
var ok = false;
var obsrvrStr;

// --------------------------------------------------------------------------

function exit() {
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
    prev();
    return;
  }

  if (ok) {
    main.getUpdates(true);
  }

  next();
}


// --------------------------------------------------------------------------

function initPage() {
  parent.layoutCb();

  if (parent.ready == null || ! parent.ready()) { window.setTimeout(initPage, 50); return; }
  parent.slctBtns("cls");

  var w = parent.op.win();
  var vmcfg = new Query(parent.location.search).arg("vmcfg")[0];
  var data;

  var dom = {};
  dom.status = obj("status");
  dom.unrgd = obj("unrgd");
  dom.rgd = obj("rgd");

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  regVm = function () { next(0); };
  unregVm = function () { next(1); };
  openVm = function () { main.MuiWindow("details", data.vmid); exit(); };

  function rndrPage() {
    editor.prv = null;

    data = clone(w.data);
    obj("hdr").innerHTML = data.dn;

    if (data.registered) {
      dom.status.innerHTML = "Registered";
      dom.unrgd.css("display", "none");
      dom.rgd.css("display", "");
    } else {
      dom.status.innerHTML = "Not Registered";
      dom.unrgd.css("display", "");
      dom.rgd.css("display", "none");
    }

    setTimeout("parent.layoutCb()", 40);
  }

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = rndrPage;

  editor.nxt[0] = svgMsg;
  editor.nxt[1] = svgMsg;
  editor.exec = function (idx) {
    var q = new Query();
    q.action("/sx-registervm");
    q.target(parent.op.win());
    q.arg("op", idx == 0 ? "register" : "unregister");
    q.arg("vmcfg", vmcfg);
    ok = true;
    q.submit();
  };

  svgMsg.nxt[0] = editor;
  svgMsg.exec = rndrPage;

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  w.location.replace("/sx-registervm?vmcfg=" + esc(vmcfg));
}
