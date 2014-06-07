/* Copyright 1998 VMware, Inc.  All rights reserved. -- VMware Confidential */

// vmSummary.js

var q;

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
      case "vmConsole":
        vmConsoleHndlr(e);   // A virtual machine console icon was clicked.
        break;
      case "vmAddEdit":
        vmAddEditHndlr(e);   // The Add/Edit VM button was clicked
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


function vmAddEditHndlr(e) {
  var t = getTrgt(e);
  var h = esc(t.vm.cfg());
  main.vmAddEditWindow(h);

}

function vmOpEditDev() {
  main.MuiWindow("details", q.arg("h")[0], "hardware");
}

function vmOpEditOpt() {
  main.MuiWindow("details", q.arg("h")[0], "options");
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

// --------------------------------------------------------------------------
//
// initPage --
//
// --------------------------------------------------------------------------

function initPage() {
  q = new Query(parent.location.search);
  var vm = main.sx.vms[q.arg("h")[0]];
  var dom = new Object();
  var NA = "Not available";

  var obsrvrStr = "vmSummary_" + vm.hash();

  dom.es = obj("es");
  dom.up = obj("up");
  dom.gs = obj("gs");
  dom.os = obj("os");
  obj("cfg").innerHTML = vm.cfg();

  dom.vmStats = obj("vmStats");

  dom.avgCpu = obj("avgCpu");
  dom.avgCpuBar = obj("avgCpuBar").childNodes;
  dom.maxCpu = obj("maxCpu");
  dom.maxCpuBar = obj("maxCpuBar").childNodes;
  dom.minCpu = obj("minCpu");
  dom.minCpuBar = obj("minCpuBar").childNodes;

  dom.avgRam = obj("avgRam");
  dom.avgRamBar = obj("avgRamBar").childNodes;
  dom.maxRam = obj("maxRam");
  dom.maxRamBar = obj("maxRamBar").childNodes;
  dom.minRam = obj("minRam");
  dom.minRamBar = obj("minRamBar").childNodes;

  dom.processors = obj("processors");
  dom.hb = obj("hb");
  dom.ip = obj("ip");
  // dom.edit.ctrl = "vmConsole";

  dom.proc = obj("proc");
  dom.memSize = obj("memSize");

  dom.sxStatPeriodA = obj("sxStatPeriodA");
  dom.sxStatPeriodB = obj("sxStatPeriodB");
  dom.sxStatPeriodC = obj("sxStatPeriodC");

  // ------------------------------------------------------------------------
  sxRenderPeriod = function (t, o, v) {
    var s = "Last " + v + (v > 1 ? " Minutes" : " Minute");
    dom.sxStatPeriodA.innerHTML = s;
    dom.sxStatPeriodB.innerHTML = s;
    dom.sxStatPeriodC.innerHTML = "(" + v + " minute average)";
  };
  main.sxPeriodObsrvr.lstn(obsrvrStr, sxRenderPeriod);


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
  vmHideSummary = function (t, vm, v) {
    if (v != esOn) {
      if (dom.vmStats.css("display") != "none") {
        dom.vmStats.css("display", "none");
        window.setTimeout("parent.adjustSize()", 40);
      }
    } else {
      if (dom.vmStats.css("display") == "none") {
        dom.vmStats.css("display", "");
        window.setTimeout("parent.adjustSize()", 40);
      }
    }
  };


  // ------------------------------------------------------------------------
  vmRenderHb = function (ut, vm, v) {
    dom.hb.innerHTML = (vm.es() == esOn && vm.gs() == "running") ? v+"%" : NA;
  };


  // ------------------------------------------------------------------------
  vmRenderCpu = function (ut, vm, v) {
    var s = "";
    switch (ut)
    {
      case "vmAvgCpu":
        s = "avgCpu";
        break;
      case "vmMaxCpu":
        s = "maxCpu";
        break;
      case "vmMinCpu":
        s = "minCpu";
        break;
      default:
        s = "avgCpu";
        break;
    }
    dom[s].innerHTML = v + " %";
    renderUsgBar(dom[s + "Bar"], v);
  };


  // ------------------------------------------------------------------------
  vmRenderRam = function (ut, vm, v) {
    var s = "";
    switch (ut)
    {
      case "vmAvgRam":
        s = "avgRam";
        break;
      case "vmMaxRam":
        s = "maxRam";
        break;
      case "vmMinRam":
        s = "minRam";
        break;
      default:
        s = "avgRam";
        break;
    }

    dom[s].innerHTML = mbStr(v);
    renderUsgBar(dom[s + "Bar"], parseInt(v / main.sx._memInfo * 100));
  };


  // ------------------------------------------------------------------------
  vmRenderMemSize = function (ut, vm, v) {
    v = (ut == null && vm.memSize() == "Unknown") ? v : vm.memSize();
    dom.memSize.innerHTML = v + " M";
  };


  // ------------------------------------------------------------------------
  vmRenderExecutionState = function (ut, vm, v) {
    if (ut == "vmWfi") {
      v = vm.es();
    }

    if (v == esOn) {
      v = "Powered on";
    } else if (vm.wfi().id != null) {
      v = NA;
    } else if (v == esOff) {
      v = "Powered off";
    } else if (v == esSusp) {
      v = "Suspended";
    }

    if (vm.wfi().id != null) v += " | Waiting for input";

    dom.es.innerHTML = v;
  };


  // ------------------------------------------------------------------------
  vmRenderGuestState = function (ut, vm, v) {
    if (vm.es() == esOn) {
      switch (v)
      {
        case "unknown":
          v = NA;
          break;
        case "halting":
        case "rebooting":
        case "halting/rebooting":
          v = "Shutting down or rebooting";
          break;
        case "notRunning":
          v = "Not running";
          break;
        case "running":
          v = "Running";
          break;
        default:
          break;
      }
    } else {
      v = NA;
    }

    dom.gs.innerHTML = v;
  };


  // ------------------------------------------------------------------------
  vmRenderUptime = function (ut, vm, v) {
    dom.up.innerHTML = vm.es() == esOn ? upStr(v, true) : NA;
  };


  // ------------------------------------------------------------------------
  vmRenderIp = function (ut, vm, v) {
    dom.ip.innerHTML = (vm.es() == esOn && vm.gs() == "running") ? v : NA;
  };


  // ------------------------------------------------------------------------
  vmRenderGuestOs = function (ut, vm ,v) {
    dom.os.innerHTML = hrGos(v);
  };


  // ------------------------------------------------------------------------
  vmRenderVcpu = function (ut, vm, v) {
    if (v >= 1) {
      dom.processors.innerHTML="Processors ("+v+")";
      dom.proc.innerHTML=v;
    } else {
      dom.processors.innerHTML="Processors (1)";
      dom.proc.innerHTML="1";
    }
  }


  // ------------------------------------------------------------------------
  tryUpdates = function () {
    if (! main || main.getUpdates == null || ! main.getUpdates()) {
      setTimeout("tryUpdates();", 250);
    }
  };


  // ------------------------------------------------------------------------
  doUp = function () {
    // Toggle extra info on.
      if ((vm.extra() & 1) == 0) {
      vm.extra(1);
    }

    vmRenderExecutionState(null, vm, vm.es());
    vmRenderUptime(null, vm, vm.up());
    vmRenderGuestState(null, vm, vm.gs());
    vmRenderGuestOs(null, vm, vm.os());

    sxRenderPeriod(null, main.sx, main.period);

    vmRenderVcpu(main.vmExecutionStateObsrvr.t, vm, vm.vcpu())

    vmRenderCpu(main.vmCpuObsrvr.t, vm, vm.cpu());
    vmRenderCpu(main.vmMaxCpuObsrvr.t, vm, vm.maxCpu());
    vmRenderCpu(main.vmMinCpuObsrvr.t, vm, vm.minCpu());

    vmRenderRam(main.vmRamObsrvr.t, vm, vm.ram());
    vmRenderRam(main.vmMaxRamObsrvr.t, vm, vm.maxRam());
    vmRenderRam(main.vmMinRamObsrvr.t, vm, vm.minRam());

    vmRenderHb(main.vmHbObsrvr.t, vm, vm.hb());
    vmRenderIp(main.vmIpObsrvr.t, vm, vm.ip());

    vmRenderMemSize(null, vm, vm.memSize());
    vmHideSummary(null, vm, vm.es());

    setTimeout("parent.adjustSize()", 40);

    main.vmExecutionStateObsrvr.lstn(obsrvrStr + "_HideSummary", vmHideSummary, vm);
    main.vmHbObsrvr.lstn(obsrvrStr, vmRenderHb, vm);
    main.vmVcpuObsrvr.lstn(obsrvrStr, vmRenderVcpu, vm);
    main.vmCpuObsrvr.lstn(obsrvrStr, vmRenderCpu, vm);
    main.vmMaxCpuObsrvr.lstn(obsrvrStr, vmRenderCpu, vm);
    main.vmMinCpuObsrvr.lstn(obsrvrStr, vmRenderCpu, vm);
    main.vmRamObsrvr.lstn(obsrvrStr, vmRenderRam, vm);
    main.vmMaxRamObsrvr.lstn(obsrvrStr, vmRenderRam, vm);
    main.vmMinRamObsrvr.lstn(obsrvrStr, vmRenderRam, vm);
    main.vmMemSizeObsrvr.lstn(obsrvrStr, vmRenderMemSize, vm);
    main.vmExecutionStateObsrvr.lstn(obsrvrStr, vmRenderExecutionState, vm);
    main.vmGuestStateObsrvr.lstn(obsrvrStr, vmRenderGuestState, vm);
    main.vmUptimeObsrvr.lstn(obsrvrStr, vmRenderUptime, vm);
    main.vmIpObsrvr.lstn(obsrvrStr, vmRenderIp, vm);
    main.vmGuestOsObsrvr.lstn(obsrvrStr, vmRenderGuestOs, vm);
    main.vmWfiObsrvr.lstn(obsrvrStr, vmRenderExecutionState, vm);
  };


  // ------------------------------------------------------------------------
  doDn = function () {
    // Toggle extra info off.
    if ((vm.extra() & 1) == 1) {
      vm.extra(1);
    }

    main.vmExecutionStateObsrvr.ignr(obsrvrStr + "_HideSummary");
    main.vmHbObsrvr.ignr(obsrvrStr);
    main.vmCpuObsrvr.ignr(obsrvrStr);
    main.vmMaxCpuObsrvr.ignr(obsrvrStr);
    main.vmMinCpuObsrvr.ignr(obsrvrStr);
    main.vmRamObsrvr.ignr(obsrvrStr);
    main.vmMaxRamObsrvr.ignr(obsrvrStr);
    main.vmMinRamObsrvr.ignr(obsrvrStr);
    main.vmMemSizeObsrvr.ignr(obsrvrStr);
    main.vmExecutionStateObsrvr.ignr(obsrvrStr);
    main.vmGuestStateObsrvr.ignr(obsrvrStr);
    main.vmUptimeObsrvr.ignr(obsrvrStr);
    main.vmIpObsrvr.ignr(obsrvrStr);
    main.vmGuestOsObsrvr.ignr(obsrvrStr);
    main.vmWfiObsrvr.ignr(obsrvrStr);
  };

  doUp();

  setTimeout("tryUpdates();", 250);

}
