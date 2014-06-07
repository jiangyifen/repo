/* Copyright 1998 VMware, Inc.  All rights reserved. -- VMware Confidential */

// errorMsg.js

function getArg(a) {
  var r;

  var s = location.search.substr(1);
  s = s.split('&');

  for (var i = 0; i < s.length; i++) {
    var kv = s[i].split('=');

    if (kv[0] == a) {
      return kv[1];
    }
  }

  return r;
}


// --------------------------------------------------------------------------
//
// initPage --
//
// --------------------------------------------------------------------------

function initPage() {
  var lu = parent.getTmpFrame(getArg("lu"));
  var dom = new Object();

  dom.question = obj("question");
  dom.btnRow = obj("btnRow");
  dom.btn = dom.btnRow.removeChild(obj("btn"));
  dom.btns = new Array();

  clik = function (e) {
    e = eObj(e);
    var t = getTrgt(e);

    if (t != null) {
      parent.remTmpFrame(getArg("lu"));
    }
  };


  dom.btns = dom.btnRow.appendChild(dom.btn.cloneNode(true));
  dom.btns.ctrl = "btn";
  dom.btns.id = 0;
  dom.btns.getElementsByTagName("td")[0].innerHTML = "OK";
  lstn(dom.btns, "click", clik);


  // ------------------------------------------------------------------------
  adjustSize = function () {
    lu.pos(32, 32, 999);
    lu.dim(480, dh());
  };


  // ------------------------------------------------------------------------
  deferRender = function () {
    var errMsg = "";
    var msgStr = "";

    while (errMsg = parent.errMsgs.shift()) {
      msgStr += "<p>" + errMsg + "</p>";
    }

    dom.question.innerHTML = msgStr;
    setTimeout("adjustSize();", 50);
  };

  lu.dim(480, 480);
  setTimeout("deferRender();", 250);
}









