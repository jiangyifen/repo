/* Copyright 1998 VMware, Inc.  All rights reserved. -- VMware Confidential */

var vmRenderInterval = 40;
var obsrvrStr = "sxMonitor";

// --------------------------------------------------------------------------
//
// clik --
//
//      Handle click events in the gsxMonitor context.
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


function mbStr(v) {
  // Note that Number().toFixed() is required in all cases. Both Mozilla and
  // MSIE have trouble subtracting two failry simple numbers. For exammple:
  //
  //      496.6 - 81.9 = 414.70000000000005
  //
  // Why? Probably because the numbers are represented in binary form, as whole
  // and sum of 1/2, 1/4, 1/8, 1/16, etc.

  if (v > 1024) {
    return new Number(v / 1024).toFixed(1) + " G";
  }
  return new Number(v).toFixed(1) + " M";
}


function initSx(i) {
  if (main == null || ! main.sx) {
    window.setTimeout("initSx();", 50);
    return;
  }

  var dom = new Object();

  dom.sxStatHdr = obj("sxStatHdr");
  dom.sxStat = obj("sxStat");
  dom.sxStatPeriod = obj("sxStatPeriod");

  dom.vmCpu = obj("vmCpu");
  dom.vmCpuBar = obj("vmCpuBar").childNodes;
  dom.osCpu = obj("osCpu");
  dom.osCpuBar = obj("osCpuBar").childNodes;
  dom.sxCpu = obj("sxCpu");
  dom.sxCpuBar = obj("sxCpuBar").childNodes;

  dom.vmRam = obj("vmRam");
  dom.vmRamBar = obj("vmRamBar").childNodes;
  dom.osRam = obj("osRam");
  dom.osRamBar = obj("osRamBar").childNodes;
  dom.sxRam = obj("sxRam");
  dom.sxRamBar = obj("sxRamBar").childNodes;

  sxRenderPeriod = function (t, o, v) {
    dom.sxStatPeriod.innerHTML = v + " Minute Average";
  };
  main.sxPeriodObsrvr.lstn(obsrvrStr, sxRenderPeriod);

  // ------------------------------------------------------------------------
  sxRenderCpu = function (ut, sx, v) {
    var os = v - sx.vmCpu();

    dom.sxCpu.innerHTML = v + " %";
    renderUsgBar(dom.sxCpuBar, v);

    dom.osCpu.innerHTML = os + " %";
    renderUsgBar(dom.osCpuBar, os);
  };
  main.sxCpuObsrvr.lstn(obsrvrStr, sxRenderCpu);


  // ------------------------------------------------------------------------
  sxRenderVmCpu = function (ut, sx, v) {
    var os = sx.cpu() - v;

    dom.vmCpu.innerHTML = v + " %";
    renderUsgBar(dom.vmCpuBar, v);

    dom.osCpu.innerHTML = os + " %";
    renderUsgBar(dom.osCpuBar, os);
  };
  main.sxVmCpuObsrvr.lstn(obsrvrStr, sxRenderVmCpu);


  // ------------------------------------------------------------------------
  sxRenderRam = function (ut, sx, v) {
    var os = v - sx.vmRam();

    dom.sxRam.innerHTML = mbStr(v);
    renderUsgBar(dom.sxRamBar, parseInt(v / main.sx._memInfo * 100));

    dom.osRam.innerHTML = mbStr(os);
    renderUsgBar(dom.osRamBar, parseInt(os / main.sx._memInfo * 100));
  };
  main.sxRamObsrvr.lstn(obsrvrStr, sxRenderRam);


  // ------------------------------------------------------------------------
  sxRenderVmRam = function (ut, sx, v) {
    var os = sx.ram() - v;

    dom.vmRam.innerHTML = mbStr(v);
    renderUsgBar(dom.vmRamBar, parseInt(v / main.sx._memInfo * 100));

    dom.osRam.innerHTML = mbStr(os);
    renderUsgBar(dom.osRamBar, parseInt(os / main.sx._memInfo * 100));
  };
  main.sxVmRamObsrvr.lstn(obsrvrStr, sxRenderVmRam);


  // ------------------------------------------------------------------------
  sxInitRender = function () {
    obj("processors").innerHTML = "Processors (" + main.sx._cpuInfo + ")";
    obj("memory").innerHTML = "Memory (" + (main.sx._memInfo < 1024
      ? parseInt(main.sx._memInfo) + " M"
      : mbStr(main.sx._memInfo)) + ")";

    sxRenderPeriod(null, main.sx, main.period);
    sxRenderCpu(null, main.sx, main.sx.cpu());
    sxRenderVmCpu(null, main.sx, main.sx.vmCpu());
    sxRenderRam(null, main.sx, main.sx.ram());
    sxRenderVmRam(null, main.sx, main.sx.vmRam());
  };

  if (ie5_5) {
    window.setTimeout("sxInitRender();", vmRenderInterval * i);
  } else {
    sxInitRender();
  }
}


function initPage() {
  var vms;
  var dom = new Array();
  var domVmRow = obj("vmPROTOrow");
  var domList = domVmRow.parentNode;
  domVmRow = domList.removeChild(domVmRow);
  var domNoVms = obj("noVms");
  var domVmsHdr = initXuaObj(obj("vmStat").getElementsByTagName("tr")[0]);
  var domUpdate = obj("lmod");
  var domVmCount = obj("vmCount");
  var count = 0;

  // Hide the number of VCPUs column for GSX
  if (main.sx._prodId == "gsx") {
    objCss(domVmsHdr.getElementsByTagName("td")[5], "display", "none");
    objCss(domVmRow.getElementsByTagName("td")[6], "display", "none");
  }

  // ------------------------------------------------------------------------
  vmRenderGuestOs = function (ut, vm) {
    if (vm.hash() != "console") {
      var img = main.getGosImg(vm);
      if (img != null) dom[vm.hash()].rc.img.src = img.src;
    }
  };
  main.vmGuestOsObsrvr.lstn(obsrvrStr, vmRenderGuestOs);

  // ------------------------------------------------------------------------
  vmRenderWfi = function (ut, vm, v) {
    if (v.id == null) {
      // The user is not allowed to control this virtual machine. Bail.
      if ((vm.mode() & 1) == 0) {
        return;
      }

      dom[vm.hash()].es.ctrl = "vmState";
      // MSIE doesn't pop up the tooltip unless the title is in the img element.
      objAtt(dom[vm.hash()].es.img, "title",
        "Access the power controls for this virtual machine.");
      objAtt(dom[vm.hash()].es, "title",
        "Access the power controls for this virtual machine.");
    } else {
      dom[vm.hash()].es.ctrl = "vmWfi";
      objAtt(dom[vm.hash()].es.img, "title", "Answer questions about this virtual machine.");
      objAtt(dom[vm.hash()].es, "title",  "Answer questions about this virtual machine.");
    }
  };
  main.vmWfiObsrvr.lstn(obsrvrStr, vmRenderWfi);

  // ------------------------------------------------------------------------
  vmRenderMode = function (ut, vm, v) {
    if ((v & 1) == 0) {
      dom[vm.hash()].es.ctrl = null;
      objAtt(dom[vm.hash()].es.img, "title", "");
      objAtt(dom[vm.hash()].es, "title", "");
    } else {
      // Waiting for input. Don't enable the power control.
      if (vm.wfi().id != null) {
        return;
      }

      dom[vm.hash()].es.ctrl = "vmState";
      // MSIE doesn't pop up the tooltip unless the title is in the img element.
      objAtt(dom[vm.hash()].es.img, "title",
        "Access the power controls for this virtual machine.");
      objAtt(dom[vm.hash()].es, "title",
        "Access the power controls for this virtual machine.");
    }
  };
  main.vmModeObsrvr.lstn(obsrvrStr, vmRenderMode);


  // ------------------------------------------------------------------------
  vmRenderExecutionState = function (ut, vm, es) {
    if (ut == "vmWfi") es = vm.es();

    var img = main.getEsImg(vm);
    if (img != null) dom[vm.hash()].es.img.src = img.src;

     if (es == esOff || es == esSusp) {
        dom[vm.hash()].cpu.innerHTML = '&nbsp;';
        dom[vm.hash()].ram.innerHTML = '&nbsp;';
     }
  };
  main.vmExecutionStateObsrvr.lstn(obsrvrStr, vmRenderExecutionState);
  main.vmWfiObsrvr.lstn(obsrvrStr + "_es", vmRenderExecutionState);


  // ------------------------------------------------------------------------
  vmRenderDisplayName = function (ut, vm, dn) {
    if (ut == "vmGuestState" || ut == "vmPid" || ut == "vmWid" || ut == "vmWfi") {
      dn = vm.dn();
    }

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

    gs = (vmInfo && gs ? " | " : "") + gs;

    gs = '<div class="vmGuestState">' + vmInfo + gs + '</div>';

    // XXX Waiting for input.
//     dom[vm.hash()].dn.innerHTML = '<div>' +
//       '<a href="javascript:;">' + dn + '</a></div>' + gs;
    dom[vm.hash()].dn.innerHTML = '<div>' +
      '<a href="javascript:MuiWindow(\'details\',\'' + vm.hash() +
      '\');" title="' + vm.cfg() + '">' + escHtml(dn) + '</a></div>' + gs;
  };
  main.vmDisplayNameObsrvr.lstn(obsrvrStr, vmRenderDisplayName);
  main.vmGuestStateObsrvr.lstn(obsrvrStr, vmRenderDisplayName);
  main.vmPidObsrvr.lstn(obsrvrStr, vmRenderDisplayName);
  main.vmWidObsrvr.lstn(obsrvrStr, vmRenderDisplayName);
  main.vmWfiObsrvr.lstn(obsrvrStr + "_dn", vmRenderDisplayName);


  // ------------------------------------------------------------------------
  vmRenderHb = function (ut, vm, v) {
    if (vm.es() == esOn && vm.gs() == "running") {
      var s = "Heartbeat is " + v + "%.";
      objAtt(dom[vm.hash()].hb, "title", s);
      objAtt(dom[vm.hash()].hbBar[0], "title", s);
      objAtt(dom[vm.hash()].hbBar[1], "title", s);
      objAtt(dom[vm.hash()].hbBar[2], "title", s);

      if (v > 66) {
        objAtt(dom[vm.hash()].hbBar[0], "class", "hbGood");
        objAtt(dom[vm.hash()].hbBar[1], "class", "hbGood");
        objAtt(dom[vm.hash()].hbBar[2], "class", "hbGood");
      } else if (v > 33) {
        objAtt(dom[vm.hash()].hbBar[0], "class", "hbSlice");
        objAtt(dom[vm.hash()].hbBar[1], "class", "hbBad");
        objAtt(dom[vm.hash()].hbBar[2], "class", "hbBad");
      } else if (v >= 0) {
        objAtt(dom[vm.hash()].hbBar[0], "class", "hbSlice");
        objAtt(dom[vm.hash()].hbBar[1], "class", "hbSlice");
        objAtt(dom[vm.hash()].hbBar[2], "class", "hbUgly");
      }
    } else {
      var s = "Heartbeat is not available.";
      objAtt(dom[vm.hash()].hb, "title", s);
      objAtt(dom[vm.hash()].hbBar[0], "title", s);
      objAtt(dom[vm.hash()].hbBar[1], "title", s);
      objAtt(dom[vm.hash()].hbBar[2], "title", s);

       objAtt(dom[vm.hash()].hbBar[0], "class", "hbSlice");
       objAtt(dom[vm.hash()].hbBar[1], "class", "hbSlice");
       objAtt(dom[vm.hash()].hbBar[2], "class", "hbSlice");
    }
  };
  main.vmHbObsrvr.lstn(obsrvrStr, vmRenderHb);


  // ------------------------------------------------------------------------
  vmRenderVcpus = function (ut, vm, v) {
    if (vm.hash() != "console") {
      dom[vm.hash()].vcpu.innerHTML = v + "P";
    }
  };
  main.vmVcpuObsrvr.lstn(obsrvrStr, vmRenderVcpus);


  // ------------------------------------------------------------------------
  vmRenderCpu = function (ut, vm, v) {
    if (vm._es == esOn) {
      dom[vm.hash()].cpu.innerHTML = v;
    } else {
      dom[vm.hash()].cpu.innerHTML = '&nbsp;';
    }
    renderUsgBar(dom[vm.hash()].cpuBar, v);
  };
  main.vmCpuObsrvr.lstn(obsrvrStr, vmRenderCpu);


  // ------------------------------------------------------------------------
  vmRenderRam = function (ut, vm, v) {
    if (vm._es == esOn) {
      dom[vm.hash()].ram.innerHTML = mbStr(v);
    } else {
      dom[vm.hash()].ram.innerHTML = '&nbsp;';
    }
  };
  main.vmRamObsrvr.lstn(obsrvrStr, vmRenderRam);


  // ------------------------------------------------------------------------
  vmRenderUptime = function (ut, vm, v) {
    if (vm.es() == esOn) {
      dom[vm.hash()].up.innerHTML = upStr(v);
      objAtt(dom[vm.hash()].up, "title", upStr(v, true));
    } else {
      dom[vm.hash()].up.innerHTML = '&nbsp;';
      objAtt(dom[vm.hash()].up, "title", "");
    }
  };
  main.vmUptimeObsrvr.lstn(obsrvrStr, vmRenderUptime);


  // ------------------------------------------------------------------------
  renderUsgBar = function (bar, avg) {
    var i = 0;
    while (avg > 0) {
      if (i + 1 <= Math.round(bar.length * .5)) {
        objAtt(bar[i], "class", "usgFill050");
      } else if (i + 1 <= Math.round(bar.length * .8)) {
        objAtt(bar[i], "class", "usgFill080");
      } else {
        objAtt(bar[i], "class", "usgFill100");
      }
      avg -= parseInt(100 / bar.length);
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
  vmInitRender = function (h, i) {
    var vm = main.sx.vms[h];
    vmRenderGuestOs(null, vm, vm.os());
    vmRenderExecutionState(null, vm, vm.es());
    vmRenderDisplayName(null, vm, vm.dn());
    vmRenderHb(null, vm, vm.hb());
    vmRenderVcpus(null, vm, vm.vcpu());
    vmRenderCpu(null, vm, vm.cpu());
    vmRenderRam(null, vm, vm.ram());
    vmRenderUptime(null, vm, vm.up());
    vmRenderMode(null, vm, vm.mode());
    vmRenderWfi(null, vm, vm.wfi());
  };


  // ------------------------------------------------------------------------
  addVm = function (ut, sx, vm) {
    // Don't display service console in the VM list
    if (vm.hash() == "console") return;

    var t1 = new Date().getTime();
    if (count == 0) {
      domNoVms.css("display", "none");
      domVmsHdr.css("display", "");
    }

    if (vms[vm.hash()] == null)
      vms[vm.hash()] = vm;
    if (dom[vm.hash()] != null) return;

    dom[vm.hash()] = new Object();

    dom[vm.hash()].row = domList.appendChild(domVmRow.cloneNode(true));

    objAtt(dom[vm.hash()].row, "id", vm.hash());

    var chldrn = dom[vm.hash()].row.getElementsByTagName('td');

    if (vm.hash() != "console") {
      dom[vm.hash()].rc = chldrn[0];
      dom[vm.hash()].rc.ctrl = "vmConsole";
      dom[vm.hash()].rc.vm = vm;
      dom[vm.hash()].rc.img = dom[vm.hash()].rc.getElementsByTagName('img')[0];
      // MSIE doesn't pop up the tooltip unless the title is in the img element.
      objAtt(dom[vm.hash()].rc.img, "title",
             "Attach a console to this virtual machine.");
      objAtt(dom[vm.hash()].rc, "title",
             "Attach a console to this virtual machine.");

      dom[vm.hash()].ctx = chldrn[1];
      dom[vm.hash()].ctx.ctrl = "vmCtxMenu";
      dom[vm.hash()].ctx.vm = vm;
      dom[vm.hash()].ctx.img = dom[vm.hash()].ctx.getElementsByTagName('img')[0];
      dom[vm.hash()].ctx.img.src = "../imx/ctxArrow.gif";
      // MSIE doesn't pop up the tooltip unless the title is in the img element.
      objAtt(dom[vm.hash()].ctx.img, "title",
             "Access a menu of commands for this virtual machine.");
      objAtt(dom[vm.hash()].ctx, "title",
             "Access a menu of commands for this virtual machine.");
    }

    dom[vm.hash()].es = chldrn[2];
    dom[vm.hash()].es.ctrl = null;
    dom[vm.hash()].es.vm = vm;
    dom[vm.hash()].es.img = dom[vm.hash()].es.getElementsByTagName('img')[0];

    dom[vm.hash()].hb = chldrn[3];
    dom[vm.hash()].hbBar = chldrn[3].getElementsByTagName('img');
    dom[vm.hash()].dn = chldrn[4];
    dom[vm.hash()].up = chldrn[5];
    dom[vm.hash()].vcpu = chldrn[6];
    dom[vm.hash()].cpu = chldrn[7];
    dom[vm.hash()].cpuBar = chldrn[8].childNodes;
    dom[vm.hash()].ram = chldrn[9];

    count++;
    domVmCount.innerHTML = count;

    if (ie5_5) {
      t1 = new Date().getTime() - t1;
      window.setTimeout("vmInitRender('" + vm.hash() + "', " + count + ");",
        (vmRenderInterval > t1 ? vmRenderInterval : t1) * count);
    } else {
      vmInitRender(vm.hash(), count);
    }
  };


  // ------------------------------------------------------------------------
  remVm = function (ut, vm, h) {
    if (dom[vm.hash()] == null) return;

    domList.removeChild(dom[vm.hash()].row);
    delete dom[vm.hash()];
    delete vms[vm.hash()];

    count--;
    domVmCount.innerHTML = count;

    if (count == 0) {
      domNoVms.css("display", "");
      domVmsHdr.css("display", "none");
    }
  };
  main.sxRemVmObsrvr.lstn(obsrvrStr, remVm);


  // ------------------------------------------------------------------------
  for (var vm in vms = main.getVmList()) addVm(null, null, vms[vm]);

  main.sxAddVmObsrvr.lstn(obsrvrStr, addVm);

  renderUpdate = function (t) {
    if (t == "preUpdate") {
      domUpdate.innerHTML = "Loading...";
    } else {
      domUpdate.innerHTML = " Last updated " + new Date().toString();
    }
  };
  main.updateObsrvr.lstn(obsrvrStr, renderUpdate);
  main.preUpdateObsrvr.lstn(obsrvrStr, renderUpdate);

  renderUpdate();

  // XXX MSIE will hang or appear to hang if this page is reloaded by itself.
  if (ie) {
    lstn(self, "unload", parent.document.location.reload);
  } else {
    lstn(self, "unload", main.getUpdates);
  }

  if (ie5_5) {
    window.setTimeout("initSx(" + count + ");", vmRenderInterval);
  } else {
    initSx(count);
  }

  if (count == 0) {
    domNoVms.css("display", "");
    domVmsHdr.css("display", "none");
    domVmCount.innerHTML = count;
  }
}

function updateNow() {
  if (main && main.getUpdates) return main.getUpdates();
}

function addWndw() {
  var u = "/vmware/en/vmCfgContainer.html?unit=sxAddVirtualMachine";
  var n = escJs(main.user + "@" + main.sx._name + ":sxAddVirtualMachine" +
    new Date().getTime());
  var w = main.getRgWndw(n);

  if (w != null && ! w.closed) {
    w.focus();
  } else {
    w = window.open("", "", "width=560,height=536,resizable");
    w.document.location.replace(u);
    w.name = n;
    main.addRgWndw(w);
  }
}
