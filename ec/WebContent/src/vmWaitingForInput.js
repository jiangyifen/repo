/* Copyright 1998 VMware, Inc.  All rights reserved. -- VMware Confidential */

// vmWaitingForInput.js

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
  var h = getArg("h");
  var vm = main.sx.vms[h];
  var lu = parent.getTmpFrame(getArg("lu"));
  var wfi = vm.wfi().id;
  var dom = new Object();

  dom.dn = obj("dn");
  dom.question = obj("question");
  dom.btnRow = obj("btnRow");
  dom.btn = dom.btnRow.removeChild(obj("btn"));
  dom.btns = new Array();

  clik = function (e) {
    e = eObj(e);
    var t = getTrgt(e);

    if (t != null) {
      if (t.id != null) {
        var s = "/sx-answer" + (main.sx._os == "win32" ? "/index.pl" : "") +
          "?c=" + esc(vm.cfg()) +
          "&vmid=" + h +
          "&question-id=" + vm.wfi().id +
          "&answer-id=" + t.id;
        vm.frame().doc().location.replace(s);
        main.setTimeout("getUpdates(true);", 1500);
        main.vmWfiObsrvr.ignr("wfi_" + getArg("h") + "_" + lu);
        parent.remTmpFrame(getArg("lu"));
      }
    }
  };


  handleWfi = function (u, vm, v) {
    try {
      if (v == null || v.id != wfi) {
        parent.remTmpFrame(getArg("lu"));
      }
    } catch (ex) {
      main.vmWfiObsrvr.ignr("wfi_" + getArg("h") + "_" + lu);
    }
  };
  main.vmWfiObsrvr.lstn("wfi_" + getArg("h") + "_" + lu, handleWfi, vm);


  for (var i = 0; i < vm.wfi().a.length; i++) {
    dom.btns[i] = dom.btnRow.appendChild(dom.btn.cloneNode(true));
    dom.btns[i].ctrl = "btn";
    dom.btns[i].id = i;
    dom.btns[i].getElementsByTagName("td")[0].innerHTML = vm.wfi().a[i];
    lstn(dom.btns[i], "click", clik);
  }


  // ------------------------------------------------------------------------
  adjustSize = function () {
    lu.pos(32, 32, 999);
    lu.dim(480, dh());
  };


  // ------------------------------------------------------------------------
  deferRender = function () {
    dom.dn.innerHTML = escHtml(vm.dn());
    dom.question.innerHTML = vm.wfi().q;
    setTimeout("adjustSize();", 50);
  };

  lu.dim(480, 480);
  setTimeout("deferRender();", 250);
}