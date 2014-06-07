/* Copyright 1998 VMware, Inc.  All rights reserved. -- VMware Confidential */


// --------------------------------------------------------------------------
//
// clik --
//
//      Handle click events in the vmList context.
//
// --------------------------------------------------------------------------

function clik(e) {
  e = eObj(e);
  var t = getTrgt(e);

  if (t != null) {
    // First, make sure the target's class represents its normal style.
    Style.set(t, Style.NRML);

    switch (t.ctrl)
    {
      case "vmState":        // A virtual machine power control was clicked.
        vmCtrlBarHndlr(e);
        break;
      case "vmCtxMenu":      // A virtual machine context menu was clicked.
        vmCtxMenuHndlr(e);
        break;
      case "vmConsole":
        vmConsoleHndlr(e);   // A virtual machine console icon was clicked.
        break;
      case "vmWfi":
        vmWfiHandler(e);     // A virtual machine WFI icon was clicked.
        break;
      default:
        break;
    }
  } else {
    if (tle.d != null) tle.d();
    if (e.ctrlKey) e.stp();
  }
}


function vmConsoleHndlr(e) {
  var t = getTrgt(e);
  var u = "/vmware-console" + (main.sx._os == "win32" ? "/index.xvm" : "") +
    "?c=" + esc(t.vm.cfg()) + '.xvm';

  t.vm.frame().doc().location.replace(u);
}


function vmWfiHandler(e) {
  var t = getTrgt(e);
  parent.waitingForInput(t.vm.hash());
}


// --------------------------------------------------------------------------
//
// initPage --
//
// --------------------------------------------------------------------------

function initPage() {
  var vm;
  var dom = new Object();

  function initStyle(o, s) {
    if (o.kit == null) initXuaStyle(o);

    if (s == "ctrl") {
      o.xuaKit("normal", "padding", "4px");
      o.xuaKit("normal", "background", "none");
      o.xuaKit("normal", "border", "none");
      o.xuaKit("normal", "cursor", ! ie ? "pointer" : "hand");

      o.xuaKit("hover", "padding", "3px");
      o.xuaKit("hover", "background", "url(../imx/tile-0003.png)");
      o.xuaKit("hover", "border", "solid 1px #666666");
      o.xuaKit("normal", "cursor", ! ie ? "pointer" : "hand");
    }
  }

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

  var obsrvrStr = "vmStatus_" + vm.hash();

  dom.rc = obj("rc");
  dom.rc.img = dom.rc.getElementsByTagName('img')[0];
  if (vm.hash() != "console") {
    dom.rc.ctrl = "vmConsole";
    dom.rc.vm = vm;
    dom.rc.enabled = true;
    // MSIE doesn't pop up the tooltip unless the title is in the img element.
    objAtt(dom.rc.img, "title",
           "Attach a console to this virtual machine.");
    objAtt(dom.rc, "title",
           "Attach a console to this virtual machine.");
  }
  initStyle(dom.rc, "ctrl");
  dom.rc.useKit("normal");

  dom.ctx = obj("ctx");
  if (vm.hash() != "console") {
    dom.ctx.ctrl = "vmCtxMenu";
    dom.ctx.vm = vm;
    dom.ctx.img = dom.ctx.getElementsByTagName('img')[0];
    dom.ctx.img.src = "../imx/ctxArrow.gif";
    dom.ctx.enabled = true;
    // MSIE doesn't pop up the tooltip unless the title is in the img element.
    objAtt(dom.ctx.img, "title",
           "Access a menu of commands for this virtual machine.");
    objAtt(dom.ctx, "title",
           "Access a menu of commands for this virtual machine.");
  }
  initStyle(dom.ctx, "ctrl");
  o.xuaKit("hover", "paddingLeft", "0px");
  o.xuaKit("hover", "paddingRight", "0px");
  o.xuaKit("normal", "paddingLeft", "1px");
  o.xuaKit("normal", "paddingRight", "1px");
  dom.ctx.useKit("normal");

  dom.es = obj("es");
  dom.es.ctrl = vm.wfi().id == null ? "vmState" : null;
  dom.es.vm = vm;
  dom.es.img = dom.es.getElementsByTagName('img')[0];
  dom.es.enabled = vm.wfi().id == null;
  initStyle(dom.es, "ctrl");
  dom.es.useKit("normal");

  dom.dn = obj("dn");
  dom.gs = obj("gs");
  dom.hb = obj("hb");
  dom.hbBar = obj("hb").getElementsByTagName('img');
  dom.lmod = obj("lmod");

  if (vm.hash() == "console") {
    dom.rc.img.src = "../imx/vmware_boxes-16x16.png";
    dom.ctx.css("display", "none");
    dom.es.css("display", "none");
    dom.gs.css("display", "none");
    dom.hb.css("display", "none");
  }

  // ------------------------------------------------------------------------
  vmRenderExecutionState = function (ut, vm, es) {
    var img = main.getEsImg(vm);
    if (img != null) dom.es.img.src = img.src;
  };
  main.vmExecutionStateObsrvr.lstn(obsrvrStr, vmRenderExecutionState, vm);
  main.vmWfiObsrvr.lstn(obsrvrStr + "_es", vmRenderExecutionState, vm);


  // ------------------------------------------------------------------------
  vmRenderWfi = function (ut, vm, v) {
    if (v.id == null) {
      // The user is not allowed to control this virtual machine. Bail.
      if ((vm.mode() & 1) == 0) {
        return;
      }

      dom.es.ctrl = "vmState";
      dom.es.enabled = true;
      // MSIE doesn't pop up the tooltip unless the title is in the img element.
      objAtt(dom.es.img, "title",
        "Access the power controls for this virtual machine.");
      objAtt(dom.es, "title",
        "Access the power controls for this virtual machine.");
    } else {
      dom.es.ctrl = "vmWfi";
      dom.es.enabled = true;
      objAtt(dom.es.img, "title", "Answer questions about this virtual machine.");
      objAtt(dom.es, "title",  "Answer questions about this virtual machine.");
    }
  };
  main.vmWfiObsrvr.lstn(obsrvrStr, vmRenderWfi, vm);


  // ------------------------------------------------------------------------
  vmRenderMode = function (ut, vm, v) {
    if ((v & 1) == 0) {
      dom.es.ctrl = null;
      dom.es.enabled = false;
      objAtt(dom.es.img, "title", "");
      objAtt(dom.es, "title", "");
    } else {
      // Waiting for input. Don't enable the power control.
      if (vm.wfi().id != null) {
        return;
      }

      dom.es.ctrl = "vmState";
      dom.es.enabled = true;
      // MSIE doesn't pop up the tooltip unless the title is in the img element.
      objAtt(dom.es.img, "title",
        "Access the power controls for this virtual machine.");
      objAtt(dom.es, "title",
        "Access the power controls for this virtual machine.");
    }
  };
  main.vmModeObsrvr.lstn(obsrvrStr, vmRenderMode, vm);


  // ------------------------------------------------------------------------
  vmRenderHb = function (ut, vm, v) {
    if (vm.es() == esOn && vm.gs() == "running") {
      var s = "Heartbeat is " + v + "%.";
      objAtt(dom.hb, "title", s);
      objAtt(dom.hbBar[0], "title", s);
      objAtt(dom.hbBar[1], "title", s);
      objAtt(dom.hbBar[2], "title", s);

      if (v > 66) {
        objAtt(dom.hbBar[0], "class", "hbGood");
        objAtt(dom.hbBar[1], "class", "hbGood");
        objAtt(dom.hbBar[2], "class", "hbGood");
      } else if (v > 33) {
        objAtt(dom.hbBar[0], "class", "hbSlice");
        objAtt(dom.hbBar[1], "class", "hbBad");
        objAtt(dom.hbBar[2], "class", "hbBad");
      } else if (v >= 0) {
        objAtt(dom.hbBar[0], "class", "hbSlice");
        objAtt(dom.hbBar[1], "class", "hbSlice");
        objAtt(dom.hbBar[2], "class", "hbUgly");
      }
    } else {
      var s = "Heartbeat is not available.";
      objAtt(dom.hb, "title", s);
      objAtt(dom.hbBar[0], "title", s);
      objAtt(dom.hbBar[1], "title", s);
      objAtt(dom.hbBar[2], "title", s);

      objAtt(dom.hbBar[0], "class", "hbSlice");
      objAtt(dom.hbBar[1], "class", "hbSlice");
      objAtt(dom.hbBar[2], "class", "hbSlice");
    }
  };
  main.vmHbObsrvr.lstn(obsrvrStr, vmRenderHb, vm);


  // ------------------------------------------------------------------------
  vmRenderDisplayName = function (ut, vm, dn) {
    dom.dn.innerHTML = escHtml(dn);
  };
  main.vmDisplayNameObsrvr.lstn(obsrvrStr, vmRenderDisplayName, vm);


  // ------------------------------------------------------------------------
  vmRenderGuestOs = function (ut, vm) {
    if (vm.hash() != "console") {
      var img = main.getGosImg(vm);
      if (img != null) dom.rc.img.src = img.src;
    }
  };
  main.vmGuestOsObsrvr.lstn(obsrvrStr, vmRenderGuestOs, vm);


  // ------------------------------------------------------------------------
  vmRenderGuestState = function (ut, vm) {
    var gs = "";
    var pid = vm.pid();
    var wid = vm.wid();
    var vmInfo  = vm.es() == esOn ? "Powered on" : "";
    vmInfo += vm.pid() && vm.es() == esOn ? " | PID " + vm.pid() : "";
    vmInfo += vm.wid() && vm.es() == esOn ? " | VMID " + vm.wid() : "";

    if (vm.es() == esOn) {
      switch (vm.gs().toLowerCase())
      {
        case "unknown":
        case "notrunning":
          gs = "VMware Tools not available";
          break;
        case "halting":
        case "rebooting":
        case "halting/rebooting":
        case "scriptexecuting":
          gs = "Guest OS shutting down or rebooting";
          break;
        default:
          break;
      }
    } else if (vm.wfi().id != null) {
      gs = "Not available";
    } else if (vm.es() == esOff) {
      gs = "Powered off";
    } else if (vm.es() == esSusp) {
      gs = "Suspended";
    }

    if (vm.wfi().id != null) {
      gs += (gs ? " | " : "") +
        '<a href="javascript:parent.waitingForInput(\'' +
        vm.hash() + '\');">Waiting for input...</a>';
    }

    gs = vmInfo + (vmInfo && gs ? " | " : "") + gs;

    dom.gs.innerHTML = gs;
  };
  main.vmGuestStateObsrvr.lstn(obsrvrStr, vmRenderGuestState, vm);
  main.vmPidObsrvr.lstn(obsrvrStr, vmRenderGuestState, vm);
  main.vmWidObsrvr.lstn(obsrvrStr, vmRenderGuestState, vm);
  main.vmExecutionStateObsrvr.lstn(obsrvrStr + "_GuestState", vmRenderGuestState, vm);
  main.vmWfiObsrvr.lstn(obsrvrStr + "_GuestState", vmRenderGuestState, vm);


  // ------------------------------------------------------------------------
  renderUpdate = function (t) {
    if (t == "preUpdate") {
      dom.lmod.innerHTML = "Loading...";
    } else {
      dom.lmod.innerHTML = " Last updated " + new Date().toString();
    }
  };
  main.updateObsrvr.lstn(obsrvrStr, renderUpdate);
  main.preUpdateObsrvr.lstn(obsrvrStr, renderUpdate);

  renderUpdate();

  vmRenderExecutionState(null, vm, vm.es());
  vmRenderHb(null, vm, vm.hb());
  vmRenderGuestOs(null, vm);
  vmRenderGuestState(null, vm);
  vmRenderDisplayName(null, vm, vm.dn());
  vmRenderMode(null, vm, vm.mode());
  vmRenderWfi(null, vm, vm.wfi());
}

