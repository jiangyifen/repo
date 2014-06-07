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
//       case "vmCtxMenu":      // A virtual machine context menu was clicked.
//         vmCtxMenuHndlr(e);
//         break;
      case "vmConsole":
        vmConsoleHndlr(e);
        break;
      default:
        break;
    }
  } else {
    if (tle.d != null) tle.d();
    if (e.ctrlKey) e.stp();
  }
}

function initVmCtrlBar() {
  var list = new Array();
  var bar;
  var dom = new Object();

  // ------------------------------------------------------------------------
  function slct(t) {
    var h = t.vm.hash();
    if (list[h] == null) {
      list[h] = new Object();
      list[h].trgt = t;
      list[h].slct = false;
    }

    if (list[h].slct) {
      list[h].slct = false;
      Style.set(t, Style.HOVR);
    } else {
      list[h].slct = true;
      Style.set(t, Style.SLCT);
    }

    return list[h].slct;
  }


  // ------------------------------------------------------------------------
  function init() {
    if (bar == null) {
      dom.row = main.obj("vmCtrlBarRow");
      dom.tbl = dom.row.parentNode;

      var chldrn = dom.row.getElementsByTagName('td');
      dom.stop = chldrn[0];
      dom.stop.ctrl = "vmOpStop";
      dom.stop.img = main.document.images["stp"];
      main.lstn(dom.stop, "click", vmCtrlBarOp);
      dom.stopCtx = chldrn[1];
      dom.pause = chldrn[2];
      dom.pause.ctrl = "vmOpPause";
      dom.pause.img = main.document.images["pse"];
      main.lstn(dom.pause, "click", vmCtrlBarOp);
      dom.pauseCtx = chldrn[3];
      dom.start = chldrn[4];
      dom.start.ctrl = "vmOpStart";
      dom.start.img = main.document.images["ply"];
      main.lstn(dom.start, "click", vmCtrlBarOp);
      dom.startCtx = chldrn[5];
      dom.reset = chldrn[6];
      dom.reset.img = main.document.images["rst"];
      main.lstn(dom.reset, "click", vmCtrlBarOp);
      dom.reset.ctrl = "vmOpReset";
      dom.resetCtx = chldrn[7];
    }

    tle.o = main.vmCtrlBar;
    tle.d = vmCtrlBarDstryr;
    tle.o.css("visibility", "visible");

    main.vmExecutionStateObsrvr.lstn("vmCtrlBar", renderVmCtrlBar);
  }


  // ------------------------------------------------------------------------
  function destroy() {
    if (tle.o != null) tle.o.pos(-99999, -99999);
    
    for (var vm in list) {
      if (list[vm].slct) {
        list[vm].slct = false;
        Style.set(list[vm].trgt, Style.NRML);
      }
    }
    
    tle.o = null;
    tle.d = null;

    main.vmExecutionStateObsrvr.ignr("vmCtrlBar");
  }


  // ------------------------------------------------------------------------
  vmCtrlBarDstryr = function () {
    destroy();
  };


  // ------------------------------------------------------------------------
  renderVmCtrlBar = function (ut, vm, v) {
    var off = false;
    var on = false;
    var paused = false;

    // Only make rendering updates that apply to the list of selected VMs.
    if (list[vm.hash()] != null && list[vm.hash()].slct) {
      // Reset the field.
      // XXX This would be better done with image objects.
      dom.stop.img.src = main.esImx.stp.DSBL.src;
      dom.stopCtx.innerHTML = '<img src="../imx/ctxArrow-dsbl.gif" />';
      Style.set(dom.stop, Style.DSBL);
      Style.set(dom.stopCtx, Style.DSBL);

      dom.pause.img.src = main.esImx.pse.DSBL.src;
      dom.pauseCtx.innerHTML = '<img src="../imx/ctxArrow-dsbl.gif" />';
      Style.set(dom.pause, Style.DSBL);
      Style.set(dom.pauseCtx, Style.DSBL);

      dom.start.img.src = main.esImx.ply.DSBL.src;
      dom.startCtx.innerHTML = '<img src="../imx/ctxArrow-dsbl.gif" />';
      Style.set(dom.start, Style.DSBL);
      Style.set(dom.startCtx, Style.DSBL);

      dom.reset.img.src = main.esImx.rst.DSBL.src;
      dom.resetCtx.innerHTML = '<img src="../imx/ctxArrow-dsbl.gif" />';
      Style.set(dom.reset, Style.DSBL);
      Style.set(dom.resetCtx, Style.DSBL);

      for (var k in list) {
        if (! list[k].slct) continue;

        if (list[k].trgt.vm.es() == esOff)
          off = true;

        if (list[k].trgt.vm.es() == esOn)
          on = true;

        if (list[k].trgt.vm.es() == esSusp)
          paused = true;

        if (off && on && paused) 
          break;
      }

      // Add appropriate controls.
      // XXX This would be better done with image objects.
      if (off) {
        dom.start.img.src = main.esImx.ply.NRML.src;
        dom.startCtx.innerHTML = '<img src="../imx/ctxArrow.gif" />';
        Style.set(dom.start, Style.NRML);
        Style.set(dom.startCtx, Style.NRML);
      }

      if (on) {
        dom.stop.img.src = main.esImx.stp.NRML.src;
        dom.stopCtx.innerHTML = '<img src="../imx/ctxArrow.gif" />';
        Style.set(dom.stop, Style.NRML);
        Style.set(dom.stopCtx, Style.NRML);

        dom.pause.img.src = main.esImx.pse.NRML.src;
        dom.pauseCtx.innerHTML = '<img src="../imx/ctxArrow.gif" />';
        Style.set(dom.pause, Style.NRML);
        Style.set(dom.pauseCtx, Style.NRML);

        dom.reset.img.src = main.esImx.rst.NRML.src;
        dom.resetCtx.innerHTML = '<img src="../imx/ctxArrow.gif" />';
        Style.set(dom.reset, Style.NRML);
        Style.set(dom.resetCtx, Style.NRML);
      }

      if (paused) {
        dom.start.img.src = main.esImx.ply.NRML.src;
        dom.startCtx.innerHTML = '<img src="../imx/ctxArrow.gif" />';
        Style.set(dom.start, Style.NRML);
        Style.set(dom.startCtx, Style.NRML);
      }
    }
  };


  // ------------------------------------------------------------------------
  vmCtrlBarOp = function (e) {
    var off = false;
    var on = false;
    var paused = false;

    e = eObj(e);
    e.stp();
    var t = getTrgt(e);
  
    for (var k in list) {
      if (! list[k].slct) continue;

      if (list[k].trgt.vm.es() == esOff)
        off = true;

      if (list[k].trgt.vm.es() == esOn)
        on = true;

      if (list[k].trgt.vm.es() == esSusp)
        paused = true;

      if (off && on && paused) 
        break;
    }

    if (t != null) {
      switch (t.ctrl)
      {
        case "vmOpStop":
          if (! on) break;
          for (var h in list) {
            if (list[h].slct) {
              main.op(h, "stop");
            }
          }
          destroy();
          break;
        case "vmOpPause":
          if (! on) break;
          for (var h in list) {
            if (list[h].slct) {
              main.op(h, "pause");
            }
          }
          destroy();
          break;
        case "vmOpStart":
          if (! off && ! paused) break;
          for (var h in list) {
            if (list[h].slct) {
              main.op(h, "start");
            }
          }
          destroy();
          break;
        case "vmOpReset":
          if (! on) break;
          for (var h in list) {
            if (list[h].slct) {
              main.op(h, "reset");
            }
          }
          destroy();
          break;
        default:
          break;
      }
    }
  };


  // ------------------------------------------------------------------------
  vmCtrlBarHndlr = function (e) {
    // Make sure we handle the current top-level object correctly.
    if (tle.o != null && tle.o != main.vmCtrlBar) {
      if (tle.d != null) tle.d(true);
    }

    // Grab the conrol bar object if it hasn't been grabbed before.
    if (main.vmCtrlBar == null) main.vmCtrlBar = main.obj("vmCtrlBar");

    if (tle.o == null) init();

    // Unless the Control or Shift key is pressed, the current selection
    // should replace the previous one.
    if (! e.ctrlKey && ! e.shiftKey) {
      var c = 0; // Count the number of selected virtual machines.
      for (var vm in list) {
        // Deselect previously selected virtual machines
        if (list[vm].trgt != getTrgt(e)) {
          if (list[vm].slct) {
            list[vm].slct = false;
            Style.set(list[vm].trgt, Style.NRML);
            c++;
          }
        }
      }

      // If there were multiple virtual machines selected, then we want to
      // make sure that the target virtual machine will be selected with a
      // statement like slct(getTrgt(e)). slct operates by toggling the
      // selection state of the target so we need to make sure that the
      // current selection is false.
      if (c > 0 && list[getTrgt(e).vm.hash()] != null)
        list[getTrgt(e).vm.hash()].slct = false;
    }

    if (slct(getTrgt(e))) {
      main.reposVmCtrlBar(e, self);
      renderVmCtrlBar(null, getTrgt(e).vm, getTrgt(e).vm.es());
    } else {
      Style.set(getTrgt(e), Style.HOVR);
    }

    var slctd = false;

    for (var vm in list) {
      if (list[vm].slct) {
        slctd = true;
      }
    }

    if (! slctd) destroy();
  };
}

function vmConsoleHndlr(e) {
  var t = getTrgt(e);
  var f = t.vm.frame();
  
  // XXX Won't work with Win32 servers.
  f.contentWindow.document.location.replace('/vmware-console?vm=' + 
    escape(t.vm.cfg()) + '.xvm');
}

function initPage() {
  var vms;
  var dom = new Array();
  var domLastRow = document.getElementById("coPROTOrow");
  var domList = domLastRow.parentNode;
  var domVmRow = domList.removeChild(document.getElementById("vmPROTOrow"));
  var domVmCount = obj("sxPROTOvmCount");
  var count = 0;
  
  
  // ------------------------------------------------------------------------
  vmRenderExecutionState = function (ut, vm, es) {
    var img = main.getEsImg(vm);
    if (img != null) dom[vm.hash()].es.img.src = img.src;

    if (es == esOff || es == esSusp) {
       dom[vm.hash()].mhz.innerHTML = main.htmlPad('--');
       dom[vm.hash()].mem.innerHTML = main.htmlPad('&nbsp;&nbsp;&nbsp;--&nbsp;');
    }                                                         
  };
  main.vmExecutionStateObsrvr.lstn("vmList", vmRenderExecutionState);


  // ------------------------------------------------------------------------
  vmRenderDisplayName = function (ut, vm, dn) {
    dom[vm.hash()].dn.innerHTML = '<div>' +
      '<a href="javascript:MuiWindow(\'details\',\'' + vm.hash() +
      '\');">' + dn + '</a></div>';
  };
  main.vmDisplayNameObsrvr.lstn("vmList", vmRenderDisplayName);


  // ------------------------------------------------------------------------
  vmRenderHb = function (ut, vm, v) {
    if (v > 66) {
       objAtt(dom[vm.hash()].hbBar[0], "class", "hbGood");
       objAtt(dom[vm.hash()].hbBar[1], "class", "hbGood");
       objAtt(dom[vm.hash()].hbBar[2], "class", "hbGood");
    } else if (v > 33) {
       objAtt(dom[vm.hash()].hbBar[0], "class", "hbSlice");
       objAtt(dom[vm.hash()].hbBar[1], "class", "hbBad");
       objAtt(dom[vm.hash()].hbBar[2], "class", "hbBad");
    } else if (v > 0) {
       objAtt(dom[vm.hash()].hbBar[0], "class", "hbSlice");
       objAtt(dom[vm.hash()].hbBar[1], "class", "hbSlice");
       objAtt(dom[vm.hash()].hbBar[2], "class", "hbUgly");
    } else {
       objAtt(dom[vm.hash()].hbBar[0], "class", "hbSlice");
       objAtt(dom[vm.hash()].hbBar[1], "class", "hbSlice");
       objAtt(dom[vm.hash()].hbBar[2], "class", "hbSlice");
    }
  };
  main.vmHbObsrvr.lstn("vmList", vmRenderHb);


  // ------------------------------------------------------------------------
  vmRenderCpu = function (ut, vm, v) {
    if (vm._es == esOn) {
      dom[vm.hash()].mhz.innerHTML = main.htmlPad(v);
    } else {
      dom[vm.hash()].mhz.innerHTML = '&nbsp;--';
    }
    vmRenderUsgBar(dom[vm.hash()].cpuBar, v);
  };
  main.vmMhzObsrvr.lstn("vmList", vmRenderCpu);


  // ------------------------------------------------------------------------
  vmRenderMem = function (ut, vm, v) {
    if (vm._es == esOn) {
      dom[vm.hash()].mem.innerHTML = main.htmlPad(v + '&nbsp;MB&nbsp;');
    } else {
      dom[vm.hash()].mem.innerHTML = '&nbsp;&nbsp;&nbsp;--&nbsp;';
    }
  };
  main.vmMemObsrvr.lstn("vmList", vmRenderMem);


  // ------------------------------------------------------------------------
  vmRenderUsgBar = function (bar, avg) {
    var i = 0;
    while (avg > 0) {
      if (i + 1 <= Math.round(bar.length * .5)) {
        objAtt(bar[i], "class", "usgFill050");
      } else if (i + 1 <= Math.round(bar.length * .8)) {
        objAtt(bar[i], "class", "usgFill080");
      } else {
        objAtt(bar[i], "class", "usgFill100");
      }
      avg -= 10;
      i++;
    }

    while (i < bar.length) {
      if (i + 1 <= Math.round(bar.length * .5)) {
        objAtt(bar[i], "class", "usgSlice050");
      } else if (i + 1 <= Math.round(bar.length * .8)) {
        objAtt(bar[i], "class", "usgSlice080");
      } else {
        objAtt(bar[i], "class", "usgSlice100");
      }
      i++;
    }
  };

  
  // ------------------------------------------------------------------------
  addVm = function (ut, sx, vm) {
    if (vms[vm.hash()] == null)
      vms[vm.hash()] = vm;
    if (dom[vm.hash()] != null) return;

    dom[vm.hash()] = new Object();

    dom[vm.hash()].row = domList.insertBefore(domVmRow.cloneNode(true),
      domLastRow);

    objAtt(dom[vm.hash()].row, "id", vm.hash());

    var chldrn = dom[vm.hash()].row.getElementsByTagName('td');

    dom[vm.hash()].rc = chldrn[0];
    dom[vm.hash()].rc.ctrl = "vmConsole";
    dom[vm.hash()].rc.vm = vm;

    dom[vm.hash()].ctx = chldrn[1];
    dom[vm.hash()].ctx.ctrl = "vmCtxMenu";
    dom[vm.hash()].ctx.vm = vm;
  
    dom[vm.hash()].es = chldrn[2];
    dom[vm.hash()].es.ctrl = "vmState";
    dom[vm.hash()].es.vm = vm;
    dom[vm.hash()].es.img = dom[vm.hash()].es.getElementsByTagName('img')[0];
    
    dom[vm.hash()].hbBar = chldrn[3].getElementsByTagName('img');
    dom[vm.hash()].dn = chldrn[4];
    dom[vm.hash()].mhz = chldrn[7];
    dom[vm.hash()].cpuBar = chldrn[8].childNodes;
    dom[vm.hash()].mem = chldrn[14];

    vmRenderExecutionState(null, vm, vm._es);
    vmRenderDisplayName(null, vm, vm._dn);
    vmRenderHb(null, vm, vm._hb);
    vmRenderCpu(null, vm, vm._mhz);
    vmRenderMem(null, vm, vm._mem);
    
    count++;
    domVmCount.innerHTML = count;
  };

  
  // ------------------------------------------------------------------------
  remVm = function (ut, sx, vm) {
    if (dom[vm.hash()] == null) return;
    
    domList.removeChild(dom[vm.hash()].row);
    delete dom[vm.hash()];
    delete vms[vm.hash()];
    
    count--;
    count == 0 ? domVmCount.innerHTML = "--" : domVmCount.innerHTML = count;
  };
  main.sxRemVmObsrvr.lstn("vmList", remVm);

  
  // ------------------------------------------------------------------------
  for (var vm in vms = main.getVmList()) addVm(null, null, vms[vm]);

  main.sxAddVmObsrvr.lstn("vmList", addVm);

  // XXX MSIE will be unusable if this page is reloaded by itself.
  if (ie) {
    lstn(self, "unload", parent.location.reload);
  }

  initVmCtrlBar();
}
