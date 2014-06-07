/* Copyright 1998 VMware, Inc.  All rights reserved. -- VMware Confidential */

// vmCtxMenu.js --
//
//       Implements initialization and control logic for the virtual machine
//       context menu.


var vmCtxMenu;
var obsrvrStr = "vmCtxMenu";

function getVmCtxMenu() {
  var w = self;
  var v = w.vmCtxMenu == null ? w.obj("vmCtxMenu") : w.vmCtxMenu;

  while (v == null) {
    if (w == w.parent) {
      return null;
    } else {
      w = w.parent;
    }
    v = w.vmCtxMenu == null ? w.obj("vmCtxMenu") : w.vmCtxMenu;
  }

  vmCtxMenu = v;
  return v;
}


function initVmCtxMenu() {
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
      o.xuaKit("normal", "padding", "5px 10px 7px 16px");
      o.xuaKit("normal", "borderWidth", "0px");
      o.xuaKit("normal", "fontWeight", "bold");
      o.xuaKit("normal", "color", "#666666");
      o.xuaKit("normal", "background", "none");
      o.xuaKit("normal", "cursor", ! ie ? "pointer" : "hand");
      if (ie) o.xuaKit("normal", "width", "100%");

      o.xuaKit("hover", "padding", "4px 9px 6px 15px");
      o.xuaKit("hover", "border", "solid 1px #999999");
      o.xuaKit("hover", "background", "url(../imx/tile-0003.png)");
      o.xuaKit("normal", "cursor", ! ie ? "pointer" : "hand");

      o.xuaKit("disabled", "padding", "5px 10px 7px 16px");
      o.xuaKit("disabled", "borderWidth", "0px");
      o.xuaKit("disabled", "fontWeight", "bold");
      o.xuaKit("disabled", "color", "#666666");
      o.xuaKit("disabled", "background", "none");
      o.xuaKit("disabled", "cursor", "default");
      o.xuaKit("disabled", "color", "#999999");
    }
  }


  // ------------------------------------------------------------------------
  function init() {
    // Grab the conrol bar object if it hasn't been grabbed before.
    if (getVmCtxMenu() == null) return;

    if (! ready) {
      obsrvrStr += "_" + new Date().getTime();

      dom.console = vmCtxMenu.win().obj("rc");
      dom.properties  = vmCtxMenu.win().obj("prps");

      dom.softStop = vmCtxMenu.win().obj("sstp");
      dom.softPause = vmCtxMenu.win().obj("spse");
      dom.softStart = vmCtxMenu.win().obj("sstt");
      dom.softRestart = vmCtxMenu.win().obj("srst");
      dom.hardStop = vmCtxMenu.win().obj("hstp");
      dom.hardPause = vmCtxMenu.win().obj("hpse");
      dom.hardStart = vmCtxMenu.win().obj("hstt");
      dom.hardRestart = vmCtxMenu.win().obj("hrst");

      dom.editOpt = vmCtxMenu.win().obj("editOpt");

      for (var n in dom) {
        dom[n].enabled = false;
        initStyle(dom[n], "ctrl");
        dom[n].useKit("disabled");
      }

      dom.console.enabled = true;
      dom.console.ctrl = "vmOpAttach";
      dom.console.useKit("normal");

      dom.editOpt.enabled = true;
      dom.editOpt.ctrl = "vmOpEditOpt";
      dom.editOpt.useKit("normal");

      if (vmCtxMenu.win().parent.isMain) {
        dom.properties.enabled = true;
        dom.properties.ctrl = "vmOpView";
        dom.properties.useKit("normal");
      } else {
        dom.properties.css("display", "none");
      }

      vmCtxMenu.dim(vmCtxMenu.win().obj("vmCtxMenu").dim());

      ready = true;
    }

    tle.o = vmCtxMenu;
    tle.d = vmCtxMenuDstryr;

    main.vmExecutionStateObsrvr.lstn(obsrvrStr, renderVmCtxMenu);
    main.vmGuestStateObsrvr.lstn(obsrvrStr, renderVmCtxMenu);
    main.vmModeObsrvr.lstn(obsrvrStr, renderVmCtxMenu);
    main.vmWfiObsrvr.lstn(obsrvrStr, renderVmCtxMenu);
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

    for (var n in dom) {
      vmCtxMenu.win().ignr(dom[n], "click", vmCtxMenuOp);
    }

    main.vmExecutionStateObsrvr.ignr(obsrvrStr);
    main.vmGuestStateObsrvr.ignr(obsrvrStr);
    main.vmModeObsrvr.ignr(obsrvrStr);
    main.vmWfiObsrvr.ignr(obsrvrStr);
  };


  // ------------------------------------------------------------------------
  vmCtxMenuDstryr = function () {
    destroy();
  };


  // ------------------------------------------------------------------------
  renderVmCtxMenu = function (ut, vm, v) {
    if (tvm == null) { return; }

    for (var n in dom) {
      vmCtxMenu.win().lstn(dom[n], "click", vmCtxMenuOp);
    }

    dom.softStop.enabled =
      dom.softPause.enabled =
      dom.softStart.enabled =
      dom.softRestart.enabled =
      dom.hardStop.enabled =
      dom.hardPause.enabled =
      dom.hardStart.enabled =
      dom.hardRestart.enabled = false;
      
    dom.softStop.useKit("disabled");
    dom.softPause.useKit("disabled");
    dom.softStart.useKit("disabled");
    dom.softRestart.useKit("disabled");
    dom.hardStop.useKit("disabled");
    dom.hardPause.useKit("disabled");
    dom.hardStart.useKit("disabled");
    dom.hardRestart.useKit("disabled");
    
    dom.softStop.ctrl =
      dom.softPause.ctrl =
      dom.softStart.ctrl =
      dom.softRestart.ctrl =
      dom.hardStop.ctrl =
      dom.hardPause.ctrl =
      dom.hardStart.ctrl =
      dom.hardRestart.ctrl = "noop";
      
    // Make label changes.
    if (tvm.es() == esOff) {
      dom.softStart.innerHTML = "Power On and Run Script";
      dom.hardStart.innerHTML = "Power On";
    }

    if (tvm.es() == esSusp || tvm.es() == esOn) {
      dom.softStart.innerHTML = "Resume and Run Script";
      dom.hardStart.innerHTML = "Resume";
    }

    if ((tvm.mode() & 2) != 2) {
      dom.editOpt.innerHTML = "View Options...";
    } else {
      dom.editOpt.innerHTML = "Configure Options...";
    }

    // If the current user does not have execute permissions or the virtual
    // machine is waiting for input, bail.
    if ((tvm.mode() & 1) == 0 || tvm.wfi().id != null) {
      return;
    }

    // Enable appropriate controls.
    if (tvm.es() == esOff || tvm.es() == esSusp) {
      dom.softStart.enabled = true;
      dom.softStart.ctrl = "vmSoftOpStart";
      dom.softStart.useKit("normal");

      dom.hardStart.enabled = true;
      dom.hardStart.ctrl = "vmHardOpStart";
      dom.hardStart.useKit("normal");
    }

    if (tvm.es() == esOn) {
      dom.hardStop.enabled = true;
      dom.hardStop.ctrl = "vmHardOpStop";
      dom.hardStop.useKit("normal");

      dom.hardPause.enabled = true;
      dom.hardPause.ctrl = "vmHardOpPause";
      dom.hardPause.useKit("normal");

      dom.hardRestart.enabled = true;
      dom.hardRestart.ctrl = "vmHardOpRestart";
      dom.hardRestart.useKit("normal");

      if (tvm.tr()) {
        dom.softStop.enabled = true;
        dom.softStop.ctrl = "vmSoftOpStop";
        dom.softStop.useKit("normal");

        dom.softPause.enabled = true;
        dom.softPause.ctrl = "vmSoftOpPause";
        dom.softPause.useKit("normal");

        dom.softRestart.enabled = true;
        dom.softRestart.ctrl = "vmSoftOpRestart";
        dom.softRestart.useKit("normal");
      }
    }
  };


  // ------------------------------------------------------------------------
  vmCtxMenuOp = function (e) {
    if (tvm == null) { return; }

    e = eObj(e);
    e.stp();
    var t = getTrgt(e);
    var h;

    h = tvm.hash();

    if (t != null) {
      switch (t.ctrl)
      {
        case "vmSoftOpStop":
        case "vmHardOpStop":
          main.vmOp(h, "stop", t.ctrl == "vmHardOpStop", self);
          destroy();
          break;
        case "vmSoftOpPause":
        case "vmHardOpPause":
          main.vmOp(h, "pause", t.ctrl == "vmHardOpPause", self);
          destroy();
          break;
        case "vmSoftOpStart":
        case "vmHardOpStart":
          main.vmOp(h, "start", t.ctrl == "vmHardOpStart", self);
          destroy();
          break;
        case "vmSoftOpRestart":
        case "vmHardOpRestart":
          main.vmOp(h, "restart", t.ctrl == "vmHardOpRestart", self);
          destroy();
          break;
        case "vmOpAttach":
          try {
            var u = "/vmware-console" + (main.sx._os == "win32" ? "/index.xvm" : "") +
              "?c=" + esc(tvm.cfg()) + '.xvm';
            tvm.frame().doc().location.replace(u);
            destroy();
          } catch (err) {
            ;
          }
          break;
        case "vmOpView":
          main.MuiWindow("details", h);
          destroy();
          break;
        case "vmOpEditOpt":
          main.MuiWindow("details", h, "options");
          destroy();
          break;
        default:
          break;
      }
    }
  };


  // ------------------------------------------------------------------------
  function reposVmCtxMenu() {
    // Position the top-left corner of the context menu in the lower right
    // corner of the target element. Make sure to adjust the target's
    // reported y position according to the scroll position of the target's
    // document.
    var x = absPos(trgt).x + parseInt(objDim(trgt).w / 5) * 4;
    var y = absPos(trgt).y + parseInt(objDim(trgt).h / 5) * 4 +
      parent[self.name].pos().y - self.viewTop();

    vmCtxMenu.ibp(x, y, 999);
    vmCtxMenu.css("visibility", "visible");
  };


  // ------------------------------------------------------------------------
  vmCtxMenuHndlr = function (e) {
    // Make sure we handle the current top-level object correctly.
    if (tle.o != null && tle.o != vmCtxMenu) {
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
      renderVmCtxMenu(null, tvm, tvm.es());
      reposVmCtxMenu();
    } else {
      destroy();
    }
  };
}

initVmCtxMenu();
