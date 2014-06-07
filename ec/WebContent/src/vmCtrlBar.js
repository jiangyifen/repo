/* Copyright 1998 VMware, Inc.  All rights reserved. -- VMware Confidential */

// vmCtrlBar.js --
//
//       Implements initialization and control logic for the virtual machine
//       control bar.


var vmCtrlBar;
var obsrvrStr = "vmCtrlBar";

function getVmCtrlBar() {
  var w = self;
  var v = w.vmCtrlBar == null ? w.obj("vmCtrlBar") : w.vmCtrlBar;

  while (v == null) {
    if (w == w.parent) {
      return null;
    } else {
      w = w.parent;
    }
    v = w.vmCtrlBar == null ? w.obj("vmCtrlBar") : w.vmCtrlBar;
  }

  vmCtrlBar = v;
  return v;
}


function initVmCtrlBar() {
  var trgt, tvm;
  var dom = new Object();
  var ready = false;


  // ------------------------------------------------------------------------
  function slct(t) {
    if (trgt != null) {
      trgt = null;
      tvm = null;
      if (t.useKit != null) {
        t.useKit("hover");
      } else {
        Style.set(t, Style.HOVR);
      }
      return false;
    } else {
      trgt = t;
      tvm = t.vm;
      if (t.useKit != null) {
        t.useKit("selected");
      } else {
        Style.set(t, Style.SLCT);
      }
      return true;
    }
  };

  function initStyle(o, s) {
    if (o.kit == null) initXuaStyle(o);

    if (s == "ctrl") {
      o.xuaKit("normal", "padding", "1px");
      o.xuaKit("normal", "background", "none");
      o.xuaKit("normal", "borderWidth", "0px");
      o.xuaKit("normal", "cursor", ! ie ? "pointer" : "hand");
      if (ie) o.xuaKit("normal", "width", "100%");

      o.xuaKit("hover", "padding", "0px");
      o.xuaKit("hover", "background", "url(../imx/tile-0003.png)");
      o.xuaKit("hover", "border", "solid 1px #999999");
      o.xuaKit("normal", "cursor", ! ie ? "pointer" : "hand");

      o.xuaKit("disabled", "padding", "1px");
      o.xuaKit("disabled", "background", "none");
      o.xuaKit("disabled", "borderWidth", "0px");
      o.xuaKit("disabled", "cursor", "default");
    }
  }

  // ------------------------------------------------------------------------
  function init() {
    // Grab the conrol bar object if it hasn't been grabbed before.
    if (getVmCtrlBar() == null) return;

    if (! ready) {
      obsrvrStr += "_" + new Date().getTime();

      dom.stop = vmCtrlBar.win().obj("stp");
      dom.stop.img = vmCtrlBar.doc().images["stp"];

      dom.pause = vmCtrlBar.win().obj("pse");
      dom.pause.img = vmCtrlBar.doc().images["pse"];

      dom.start = vmCtrlBar.win().obj("stt");
      dom.start.img = vmCtrlBar.doc().images["stt"];

      dom.restart = vmCtrlBar.win().obj("rst");
      dom.restart.img = vmCtrlBar.doc().images["rst"];

      for (n in dom) {
        dom[n].enabled = true;
        initStyle(dom[n], "ctrl");
        dom[n].useKit("normal");
      }

      vmCtrlBar.dim(vmCtrlBar.win().obj("vmCtrlBar").dim());

      ready = true;
    }

    tle.o = vmCtrlBar;
    tle.d = vmCtrlBarDstryr;

    main.vmExecutionStateObsrvr.lstn(obsrvrStr, renderVmCtrlBar);
    main.vmGuestStateObsrvr.lstn(obsrvrStr, renderVmCtrlBar);
    main.vmModeObsrvr.lstn(obsrvrStr, renderVmCtrlBar);
  };


  // ------------------------------------------------------------------------
  function destroy() {
    if (tle.o != null) tle.o.pos(-99999, -99999);

    if (trgt != null) {
      if (trgt.useKit != null) {
        trgt.useKit("normal");
      } else {
        Style.set(trgt, Style.NRML);
      }
      trgt = null;
      tvm = null;
    }

    tle.o = null;
    tle.d = null;

    for (n in dom) {
      vmCtrlBar.win().ignr(dom[n], "click", vmCtrlBarOp);
    }

    main.vmExecutionStateObsrvr.ignr(obsrvrStr);
    main.vmGuestStateObsrvr.ignr(obsrvrStr);
    main.vmModeObsrvr.ignr(obsrvrStr);
  };


  // ------------------------------------------------------------------------
  vmCtrlBarDstryr = function () {
    destroy();
  };


  // ------------------------------------------------------------------------
  renderVmCtrlBar = function (ut, vm, v) {
    if (tvm == null) { return; }

    for (n in dom) {
      vmCtrlBar.win().lstn(dom[n], "click", vmCtrlBarOp);
    }

    dom.stop.img.src = main.esImx.stp.DSBL.src;
    dom.stop.useKit("disabled");

    dom.pause.img.src = main.esImx.pse.DSBL.src;
    dom.pause.useKit("disabled");

    dom.start.img.src = main.esImx.stt.DSBL.src;
    dom.start.useKit("disabled");

    dom.restart.img.src = main.esImx.rst.DSBL.src;
    dom.restart.useKit("disabled");

    dom.stop.enabled =
      dom.pause.enabled =
      dom.start.enabled =
      dom.restart.enabled = false;

    dom.stop.ctrl = dom.pause.ctrl = dom.start.ctrl = dom.restart.ctrl = "noop";

    // If the current user does not have execute permissions, bail.
    if ((vm.mode() & 1) == 0) {
      return;
    }

    // Enable appropriate controls.
    if (tvm.es() == esOff || tvm.es() == esSusp) {
      dom.start.enabled = true;
      dom.start.ctrl = "vmOpStart";
      dom.start.img.src = main.esImx.stt.NRML.src;
      dom.start.useKit("normal");
    }

    if (tvm.es() == esOn) {
      dom.stop.enabled = true;
      dom.stop.ctrl = "vmOpStop";
      dom.stop.img.src = main.esImx.stp.NRML.src;
      dom.stop.useKit("normal");

      dom.pause.enabled = true;
      dom.pause.ctrl = "vmOpPause";
      dom.pause.img.src = main.esImx.pse.NRML.src;
      dom.pause.useKit("normal");

      dom.restart.enabled = true;
      dom.restart.ctrl = "vmOpRestart";
      dom.restart.img.src = main.esImx.rst.NRML.src;
      dom.restart.useKit("normal");
    }
  };


  // ------------------------------------------------------------------------
  vmCtrlBarOp = function (e) {
    if (tvm == null) { return; }

    e = eObj(e);
    e.stp();
    var t = getTrgt(e);
    var h;

    h = tvm.hash();

    if (t != null) {
      switch (t.ctrl)
      {
        case "vmOpStop":
          main.vmOp(h, "stop", null, self);
          destroy();
          break;
        case "vmOpPause":
          main.vmOp(h, "pause", null, self);
          destroy();
          break;
        case "vmOpStart":
          main.vmOp(h, "start", null, self);
          destroy();
          break;
        case "vmOpRestart":
          main.vmOp(h, "restart", null, self);
          destroy();
          break;
        default:
          break;
      }
    }
  };


  // ------------------------------------------------------------------------
  function reposVmCtrlBar() {
    // Position the top-left corner of the control bar in the lower right
    // corner of the target element. Make sure to adjust the target's
    // reported y position according to the scroll position of the target's
    // document.
    var x = absPos(trgt).x + parseInt(objDim(trgt).w / 5) * 4;
    var y = absPos(trgt).y + parseInt(objDim(trgt).h / 5) * 4 +
      parent[self.name].pos().y - self.viewTop();

    vmCtrlBar.ibp(x, y, 999);
    vmCtrlBar.css("visibility", "visible");
  };


  // ------------------------------------------------------------------------
  vmCtrlBarHndlr = function (e) {
    // Make sure we handle the current top-level object correctly.
    if (tle.o != null && tle.o != vmCtrlBar) {
      if (tle.d != null) tle.d(true);
    }

    if (tle.o == null) init();

    // Deselect previously selected virtual machine, then select the new one.
    if (trgt != null) {
      var tmp = trgt;
      if (trgt != getTrgt(e)) slct(trgt);

      if (tmp.useKit != null) {
        tmp.useKit("normal");
      } else {
        Style.set(tmp, Style.NRML);
      }
    }

    if (slct(getTrgt(e))) {
      renderVmCtrlBar(null, tvm, tvm.es());
      reposVmCtrlBar();
    } else {
      destroy();
    }
  };
}

initVmCtrlBar();
