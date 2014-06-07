/* Copyright 1998 VMware, Inc.  All rights reserved. -- VMware Confidential */

// sxPathCtxMenu.js --
//
//     Implements initialization and control logic for the LUN control menu.


var pathCtxMenu;
var obsrvrStr = "pathCtxMenu";

function getPathCtxMenu() {
  var w = self;
  var menu = w.pathCtxMenu == null ? w.obj("pathCtxMenu") : w.pathCtxMenu;

  while (menu == null) {
    if (w == w.parent) {
      return null;
    } else {
      w = w.parent;
    }
    menu= w.pathCtxMenu == null ? w.obj("pathCtxMenu") : w.pathCtxMenu;
  }

  pathCtxMenu = menu;
  return menu;
}


function initPathCtxMenu(aMenuCb) {
  var trgt;
  var dom = new Object();
  var ready = false;

  // ----------------- BEGIN PRIVATE METHIODS ---------------
  
  function slct(t) {
    if (!t) return false;
    if (trgt != null) {
      trgt = null;
      if (t.useKit != null) {
        t.useKit("hover");
      } else {
        Style.set(t, Style.HOVR);
      }
      return false;
    } else {
      trgt = t;
      if (t.useKit != null) {
        t.useKit("selected");
      } else {
        Style.set(t, Style.SLCT);
      }
      return true;
    }
  };
  
  
  // --------------------------------------------------------
  function initStyle(o, s) {
    if (o.kit == null) initXuaStyle(o);

    if (s == "ctrl") {
      o.xuaKit("normal", "padding", "1px");
      o.xuaKit("normal", "background", "none");
      o.xuaKit("normal", "borderWidth", "0px");
      o.xuaKit("normal", "cursor", ! ie ? "pointer" : "hand");
      o.xuaKit("normal", "color", "#666666");
      if (ie) o.xuaKit("normal", "width", "100%");

      o.xuaKit("hover", "padding", "0px");
      o.xuaKit("hover", "background", "url(../imx/tile-0003.png)");
      o.xuaKit("hover", "border", "solid 1px #999999");
      o.xuaKit("normal", "cursor", ! ie ? "pointer" : "hand");

      o.xuaKit("disabled", "padding", "1px");
      o.xuaKit("disabled", "background", "none");
      o.xuaKit("disabled", "borderWidth", "0px");
      o.xuaKit("disabled", "cursor", "default");
      o.xuaKit("disabled", "color", "#CCCCCC");
      
      
      o.xuaKit("noop", "padding", "0px");
      o.xuaKit("noop", "background", "url(../imx/tile-0005.png)");
      o.xuaKit("noop", "color", "#666666");
      o.xuaKit("noop", "border", "inset 1px #999999");
    }
  }
  
  
  // ------------------------------------------------------------------------
  function init() {
    // Grab the conrol bar object if it hasn't been grabbed before.
    if (getPathCtxMenu() == null) return;
    if (! ready) {
      obsrvrStr += "_" + new Date().getTime();
      
      dom.prfrd = pathCtxMenu.win().obj("prfrd");
      dom.prfrd.img = pathCtxMenu.doc().images["prfrd"];

      dom.actv =  pathCtxMenu.win().obj("actv");
      dom.actv.img = pathCtxMenu.doc().images["actv"];

      dom.idle = pathCtxMenu.win().obj("idle");
      dom.idle.img = pathCtxMenu.doc().images["idle"];

      dom.dsbld = pathCtxMenu.win().obj("dsbld");
      dom.dsbld.img = pathCtxMenu.doc().images["dsbld"];

      for (n in dom) {
        dom[n].enabled = true;
        initStyle(dom[n], "ctrl");
        dom[n].useKit("normal");
      }

      pathCtxMenu.dim(pathCtxMenu.win().obj("pathCtxMenu").dim());

      ready = true;
    }

    tle.o = pathCtxMenu;
    tle.d = pathCtxMenuDstryr;
    
    // Register callback
    main.sxPathCtxMenuObsrvr.lstn(obsrvrStr, aMenuCb);
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
    }

    tle.o = null;
    tle.d = null;
    
    main.sxPathCtxMenuObsrvr.ignr(obsrvrStr);
  };
  
  // ----------------- END PRIVATE METHIODS ---------------
  
  
  // ------------------------------------------------------------------------
  pathCtxMenuDstryr = function () {
    destroy();
  };
  
  
  // ------------------------------------------------------------------------
  renderpathCtxMenu = function () {
    for (var n in dom) {
      pathCtxMenu.win().lstn(dom[n], "click", lunCtxMenuOp);
    }
    dom.prfrd.img.src = "../imx/info-10x10.png";
    dom.prfrd.useKit("disabled");

    dom.actv.img.src = "../imx/info-10x10.png";
    dom.actv.useKit("disabled");

    dom.idle.img.src = "../imx/info.holder-10x10.png";
    dom.idle.useKit("disabled");

    dom.dsbld.img.src = "../imx/warning-10x10.png";
    dom.dsbld.useKit("disabled");

    dom.prfrd.enabled = 
      dom.actv.enabled = 
      dom.idle.enabled = 
      dom.dsbld.enabled = false;
    
    // --------------------------------------------------------------------
    //
    //  To understand the menu logic please review the following states:
    //  Preferred-active:
    //   choices: active (nop), disabled
    //	 notes: idle should not be allowed because it will just become
    //          active immediately.
    //
    //  Preferred-disabled:
    //	 choices: active, disabled (nop)
    //	 notes: idle should not be allowed because it will just become
    //		      active immediately
    //
    //  Unpreferred-active:d
    //	 choices: preferred, disabled, active (nop)
    //	 notes: The preferred path must either be disabled or broken
    //		      to get to this state.  setting "idle" doesn't make
    //		      sense.  If we want another path to be active, set that
    //		      path to active directly.  Otherwise, it's random which
    //		      path becomes active -- we could do that, but it sounds
    //		      more confusing than it's worth.
    //
    //  Unpreferred-idle:
    //	choices: preferred, disabled, idle (nop)
    //	notes: should we allow active?  This might make sense if preferred
    //         path is broken or disabled.
    //
    //  Unpreferred-disabled:
    //	choices: idle, preferred
    //	notes: it's a little confusing what will happen if we allow setting
    //		     to preferred.  logically what will happen is that the path
    //		     is set to preferred, but it is still disabled.  Then you
    //		     have to set it to active as a separate step.
    //
    //  Preferred-broken:
    //	display: red circle
    //	choices: none
    //	notes:  if you want to set another path to be preferred, go to that
    //		      path and set it there.  we do not allow setting a path to be
    //		      "unpreferred".
    // -----------------------------------------------------------------------
    
    var plcy = getCurrentPolicy(); // XXX - Not a good design pattern :-(
    dom.prfrd.ctrl = dom.actv.ctrl = dom.idle.ctrl = dom.dsbld.ctrl = "noop";
    // Note: No choices should be displayed for a brkn path.
    if (trgt.path && trgt.path.id && !trgt.path.brkn) {
      if (plcy == "fixed" && trgt.path.prfrd == true) {
        if (trgt.path.actv == true) {
          dom.actv.useKit("noop");
          dom.dsbld.enabled = true;
          dom.dsbld.ctrl = "lunOpDsbld";
          dom.dsbld.useKit("normal");
        } else if (trgt.path.enbld == false) {
          dom.dsbld.useKit("noop");
          dom.actv.enabled = true;
          dom.actv.ctrl = "lunOpActv";
          dom.actv.useKit("normal");
        }
      } else { // Unpreferred
        if (trgt.path.actv == true) {
          dom.actv.useKit("noop");
          if (plcy == "fixed") {
            dom.prfrd.enabled = true;
            dom.prfrd.ctrl = "lunOpPrfrd";
            dom.prfrd.useKit("normal");
          }
          dom.dsbld.enabled = true;
          dom.dsbld.ctrl = "lunOpDsbld";
          dom.dsbld.useKit("normal");
        } else if (trgt.path.enbld == true) {
          dom.idle.useKit("noop");
          // Preferred menu item should remain disabled
          // for policies other than "fixed"
          if (plcy == "fixed") { 
            dom.prfrd.enabled = true;
            dom.prfrd.ctrl = "lunOpPrfrd";
            dom.prfrd.useKit("normal");
          }
          dom.actv.enabled = true;
          dom.actv.ctrl = "lunOpActv";
          dom.actv.useKit("normal");
          dom.dsbld.enabled = true;
          dom.dsbld.ctrl = "lunOpDsbld";
          dom.dsbld.useKit("normal");
        } else if (trgt.path.enbld == false) {
          dom.dsbld.useKit("noop");
          // Preferred menu item should remain disabled
          // for policies other than "fixed"
          if (plcy == "fixed") {
            dom.prfrd.enabled = true;
            dom.prfrd.ctrl = "lunOpPrfrd";
            dom.prfrd.useKit("normal");
          }
          dom.actv.enabled = true;
          dom.actv.ctrl = "lunOpActv";
          dom.actv.useKit("normal");
          dom.idle.enabled = true;
          dom.idle.ctrl = "lunOpIdle";
          dom.idle.useKit("normal");
        }
      }
    }
  };
  
  
  // ------------------------------------------------------------------------
  function repospathCtxMenu() {
    // Position the top-left corner of the control bar in the lower right
    // corner of the target element. Make sure to adjust the target's
    // reported y position according to the scroll position of the target's
    // document.
    var x = absPos(trgt).x + parseInt(objDim(trgt).w / 5) * 4;
    var y = absPos(trgt).y + parseInt(objDim(trgt).h / 5) * 4 +
      parent[self.name].pos().y - self.viewTop();

    pathCtxMenu.ibp(x, y, 999);
    pathCtxMenu.css("visibility", "visible")
  };
  
  
  // ------------------------------------------------------------------------
  pathCtxMenuHndlr = function (e) {
    // Make sure we handle the current top-level object correctly.
    if (tle.o != null && tle.o != pathCtxMenu) {
      if (tle.d != null) tle.d(true);
    }

    if (tle.o == null) {
      init();
    }

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
      renderpathCtxMenu();
      repospathCtxMenu();
    } else {
      destroy();
    }
  };
  
  
  // ------------------------------------------------------------------------
  lunCtxMenuOp = function (e) {
    e = eObj(e);
    var t = getTrgt(e);
    if (t.ctrl ==  "lunOpPrfrd" || t.ctrl == "lunOpActv" ||
        t.ctrl ==  "lunOpIdle"  || t.ctrl == "lunOpDsbld") {
      main.sxPathCtxMenuObsrvr.exec(null, t.ctrl); // notify menu observers.
      destroy();
    } else {
      // unknown operation!
    }
  };
}
