/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// Original query, edited query
var oq, eq;
var ok = false;
var obsrvrStr;

// --------------------------------------------------------------------------

function exit(i) {
  parent.close();
}


// --------------------------------------------------------------------------

function initPage() {
  if (parent.ready == null || ! parent.ready()) { window.setTimeout(initPage, 50); return; }

  var w = parent.op.win();
  var q = new Query(parent.location.search);
  var vm = main.sx.vms[q.arg("vmid")[0]];
  var opts, shiftOpts;

  obsrvrStr = "vmOptionsRaw" + parent.ctx + "_" + vm.hash();

  var dom = {};
  dom.bdy = obj("tbl").getElementsByTagName("tbody")[0];
  dom.keyRow = dom.bdy.removeChild(obj("keyRow"));
  dom.sepRow = null;
  dom.addLink = obj("addLink");

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  rndrMode = function () {
    var dsbld = ! ((vm.mode() & 2) == 2 && vm.es() == esOff);

    for (var i = 0; i < eq.f.length; i++) {
      eq.f[i].disabled = dsbld;
    }

    if (dsbld) {
      dom.addLink.att("class", "dsbld");
      dom.addLink.att("href", "javascript:;");
    } else {
      dom.addLink.att("class", "");
      dom.addLink.att("href", "javascript:addOpt();");
    }
  };


  addOptRow = function (opt, sep) {
    var kri = initKeyRow("", opt.k, opt.k, dom, dom.bdy, dom.keyRow, sep);
    dom[kri].val.att("name", opt.k);
    dom[kri].val.att("value", opt.v);

    if (dom.sepRow == null) { dom.sepRow = dom[kri].keyRow; }

    return dom[kri];
  };

  addOpt = function () {
    var key = prompt("Enter an option name:", "");
    if (key == null || ! key) {
      return;
    }

    var val = prompt("Enter a value for option " + key + ":", "");
    if (val == null) {
      return;
    }

    dom.sepRow = addOptRow({k:key,v:val}, dom.sepRow).keyRow;

    eq.arg(key, val);

    rndrMode();
    parent.layoutCb();
  };

  function rndrOpts() {
    if (shiftOpts.length > 0 && shiftOpts[0] != null) {
      addOptRow(shiftOpts.shift());
      if (ie) { parent.layoutCb(); }
      setTimeout(rndrOpts, 1);
    } else {
      oq = new Query(document.forms[0]);
      oq.arg("vmid", vm.hash());
      eq = new Query(document.forms[0]);

      rndrMode();
      main.vmExecutionStateObsrvr.lstn(obsrvrStr, rndrMode, vm);
      main.vmModeObsrvr.lstn(obsrvrStr, rndrMode, vm);
    }

    return true;
  }

  // --------------------------------------------------------------------------
  opCb = function (w) {
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
      
      main.vmExecutionStateObsrvr.ignr(obsrvrStr, rndrMode, vm);
      main.vmModeObsrvr.ignr(obsrvrStr, rndrMode, vm);
      
      prev();
      return;
    }
    
    if (ok) {
      if (main && main.getUpdates) { main.getUpdates(); }
    }
    
    next();
  }

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;

    opts = clone(w.opts);
    shiftOpts = clone(w.opts);
    rndrOpts();
    setTimeout("parent.layoutCb()", 40);
  };

  editor.nxt[0] = svgMsg;
  editor.exec = function () {
    eq.cache();

    if (eq.diff(oq) && (vm.mode() & 2) == 2 && vm.es() == esOff) {
      ok = true;

      main.vmExecutionStateObsrvr.ignr(obsrvrStr, rndrMode, vm);
      main.vmModeObsrvr.ignr(obsrvrStr, rndrMode, vm);

      eq.submit();
    } else {
      exit();
    }
  };

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  w.location.replace("/vm-options/index.pl?vmid=" + vm.hash() + "&raw=1");
}
