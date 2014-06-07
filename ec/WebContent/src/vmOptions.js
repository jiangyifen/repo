/* Copyright 2002 VMware, Inc.  All rights reserved. -- VMware Confidential */

var ok = false;

// --------------------------------------------------------------------------
//
// initPage --
//
// --------------------------------------------------------------------------

function initPage() {
  var vm = main.sx.vms[new Query(parent.location.search).arg("h")[0]];
  var dom = new Object();

  dom.mod = obj("mod");
  dom.name = obj("name");
  dom.guestOS = obj("guestOS");
  dom.suspendDirectory = obj("suspendDirectory");
  dom.enableLogging = obj("enableLogging");
  dom.runWithDebugInfo = obj("runWithDebugInfo");

  dom.autostart = obj("autostart");
  dom.autostop = obj("autostop");
  //  dom.autostartOrder = obj("autostartOrder");  // XXX VMs sequence
  //  dom.autostopOrder = obj("autostopOrder"); // XXX VMs sequence
  dom.autostartDelay = obj("autostartDelay");
  dom.autostopDelay = obj("autostopDelay");
  dom.autostartContinueWhenToolsRun = obj("autostartContinueWhenToolsRun");

  dom.aShutdown = obj("aShutdown");
  dom.aStartup = obj("aStartup");
  dom.divStartupDisable = obj("divStartupDisable");
  dom.divShutdownDisabled = obj("divShutdownDisabled");

  var obsrvrStr = "vmOptions_" + vm.hash();


  // ------------------------------------------------------------------------
  rndrMode = function (ut, vm, mode) {
    if ((mode & 2) != 2) {
      objAtt(dom.mod, "href", "javascript:;");
      objAtt(dom.mod, "class", "dsbld");
    } else {
      objAtt(dom.mod, "href", "javascript:setOpts();");
      objAtt(dom.mod, "class", "");
    }
  };

  //-------------------------------------------------------------------------
  lstnrAutoStart = function() {
    /*    window.alert(" The system configuration for virtual machines autostart" +
		 " has changed !\n" +
		 " You can view and modify these settings in the Options tab" +
		 " of the main screen");
    */
    updateSysDefaults();
    toggleAutostart();
  }
    
  updateSysDefaults = function () { 
    dom["autostartDelay"].innerHTML = strAutoStartDelay(vm);
    dom["autostopDelay"].innerHTML = strAutoStopDelay(vm);
  }

  toggleAutostart = function () { 
    var boolEnabled = (main.sx.vmAutoDflts("autostart") == 1);
    var boolRunAsUser = (vm.usr() != "");
    var strColor = (boolEnabled && boolRunAsUser) ? "" : "silver";

    dom.autostart.css("color",strColor);
    dom.autostop.css("color",strColor);
    //    dom.autostartOrder.css("color",strColor);  // XXX VMs sequence
    //  dom.autostopOrder.css("color",strColor);  // XXX VMs sequence
    dom.autostartDelay.css("color",strColor);
    dom.autostopDelay.css("color",strColor);
    dom.autostartContinueWhenToolsRun.css("color",strColor);
    if (boolEnabled && boolRunAsUser) {
      objAtt(dom.aShutdown, "class", "");
      objAtt(dom.aShutdown, "href", "javascript:setOpts('startup');");
      objAtt(dom.aStartup, "class", "");
      objAtt(dom.aStartup, "href", "javascript:setOpts('startup');");
      dom.divStartupDisable.css("display", "none");
      dom.divShutdownDisabled.css("display", "none");
    } else {
      objAtt(dom.aShutdown, "class", "dsbld");
      objAtt(dom.aShutdown, "href", "javascript:;");
      objAtt(dom.aStartup, "class", "dsbld");
      objAtt(dom.aStartup, "href", "javascript:;");
      dom.divStartupDisable.css("display", "");
      dom.divShutdownDisabled.css("display", "");

      var msg = "";
      if (!boolRunAsUser) {
        msg = "To enable, use the Console to specify a user or system " +
              "account for this virtual machine";
      } else if (!boolEnabled) {
        msg = "Disabled per System. Enable in the Options tab of the main screen";
      }
      objAtt(dom.divStartupDisable, "title", msg);
      objAtt(dom.divShutdownDisabled, "title", msg);

      var img;
      img = dom.divStartupDisable.getElementsByTagName('img')[0];
      objAtt(img, "title", msg);
      img = dom.divShutdownDisabled.getElementsByTagName('img')[0];
      objAtt(img, "title", msg);
    }

    //  dom. // XXX add power off before cont when implemented
  };

  // ------------------------------------------------------------------------
  fillPage = function (ut, vm, k) {

    switch (k) {
      case "name":
        dom[k].innerHTML = escHtml(vm.opt(k));
        break;
      case "guestOS":
        dom[k].innerHTML = hrGos(vm.opt(k));
        break;
      case "suspendDirectoryGSX":
        if (main.sx._prodId == "gsx") {
           dom["suspendDirectory"].innerHTML = vm.opt(k);
	}
        break;
      case "suspendDirectory":
        if (main.sx._prodId != "gsx") {
           dom[k].innerHTML = vm.opt(k);
	}
        break;
      case "enableLogging":
        dom[k].innerHTML = vm.opt(k) ? "Yes" : "No";
        break;
      case "runWithDebugInfo":
        dom[k].innerHTML = vm.opt(k) ? "Yes" : "No";
        break;
      case "autostart":
	if (vm.opt(k)=="none") {
	  dom[k].innerHTML = "Do not start virtual machine";
	} else {
	  dom[k].innerHTML = "Start virtual machine";
	}
	dom["autostartDelay"].innerHTML = strAutoStartDelay(vm);
	//	dom["autostartOrder"].innerHTML = strAutoStartOrder(vm);  // XXX VMs sequence
	break;
      case "autostop":
	dom[k].innerHTML = strAutoStop(vm);
	break;
	//      case "autostartOrder": // XXX VMs sequence
	//	dom[k].innerHTML = strAutoStartOrder(vm);	
	//	break;
      case "autostartDelay":
	dom[k].innerHTML = strAutoStartDelay(vm);
	break;
      case "autostopDelay":
	dom[k].innerHTML = strAutoStopDelay(vm);
	break;
      case "autostartContinueWhenToolsRun":
	dom["autostartDelay"].innerHTML = strAutoStartDelay(vm);
	break;
      default:
	break;
    }

    toggleAutostart();

  };

  // ------------------------------------------------------------------------
  // SetOpts parameter can be one of :
  //   "standard" - standard options
  //   "startup"  - startup and shutdown configuration options
  //   "raw"      - for verbose options configuration
  //
  // If any other parameter is passed, or no paramter is passed, the standard 
  // options page will popup by default.

  setOpts = function (raw) {
    var l ="";
    switch (raw) {
      case "startup" :
	l = "vmOptionsStartup";
	break;
      case "raw" :
	l = "vmOptionsRaw";
	break;
      case "standard" :
      default : 
	l = "vmOptions";
	break;
    }

    var u = "/vmware/en/vmCfgContainer.html?unit=" + l + "&vmid=" + vm.hash();

    var n = main.user + "@" + main.sx._name + ":" + vm.cfg() + ":" + l;
    n = escJs(n);

    var w = main.getRgWndw(n);

    if (w != null && ! w.closed) {
      w.focus();
    } else {
      var i = 16;
      var p = ie ? ",top="+i+",left="+i : ",screenX="+i+",screenY="+i;

      w = window.open("", "", "width=560,height=200,resizable" + (raw ? p : ""));
      w.document.location.replace(u);
      w.name = n;
      main.addRgWndw(w);
    }
  };


  // ------------------------------------------------------------------------
  tryUpdates = function () {
    if (! main || main.getUpdates == null || ! main.getUpdates()) {
      setTimeout("tryUpdates();", 250);
    }
  };


  // ------------------------------------------------------------------------
  doUp = function () {
    // Toggle extra info on.
    var i;
    //    for (i in main.sx.vms) {
    //    }
    if ((vm.extra() & 2) == 0) {
      vm.extra(2);
    }

    for (var k in vm._opt) {
      fillPage(main.vmOptObsrvr.t, vm, k);
    }
    rndrMode(main.vmModeObsrvr.t, vm, vm.mode());

    main.vmOptObsrvr.lstn(obsrvrStr, fillPage, vm);
    main.vmModeObsrvr.lstn(obsrvrStr, rndrMode, vm);
    main.vmUsrObsrvr.lstn(obsrvrStr, lstnrAutoStart, vm);
    main.sxVmAutoDfltsObsrvr.lstn(obsrvrStr,lstnrAutoStart);
    setTimeout("parent.adjustSize()", 40);
  };


  // ------------------------------------------------------------------------
  doDn = function () {
    // Toggle extra info off.
    if ((vm.extra() & 2) == 2) {
      vm.extra(2);
    }

    main.vmOptObsrvr.ignr(obsrvrStr, fillPage, vm);
    main.vmModeObsrvr.ignr(obsrvrStr, rndrMode, vm);
    main.vmUsrObsrvr.lstn(obsrvrStr,lstnrAutoStart, vm);
    main.sxVmAutoDfltsObsrvr.ignr(obsrvrStr,toggleAutostart);
  };

  setTimeout("tryUpdates();", 250);
}

function strAutoStop(objVm) {
  var strStop = "";
  switch (objVm.opt("autostop")) {
    case "poweroff" : 
    case "none" :
      strStop = "Power off virtual machine";
      break;
    case "softpoweroff" :
      strStop = "Shut down guest operating system";
      break;
    case "suspend" :
    case "softsuspend" :
      strStop = "Suspend virtual machine";
      break;
  }
  return strStop;
}

function strAutoStopDelay(objVm) {
  var strDelay = "";
  var intMins = "";
  var autostopDelay;
  var sysDflts;

  if (objVm.opt("autostopDelay") == - 1) {
    autostopDelay = main.sx.vmAutoDflts("autostopDelay");
    sysDflts = " (system default)";
  } else {
    autostopDelay = objVm.opt("autostopDelay");
    sysDflts = "";
  }  

  if (autostopDelay == 0) {
    strDelay = "Don't Wait" + sysDflts;
  } else {
    intMins = autostopDelay/60;
    strDelay += intMins + " minute" + ((intMins==1) ? "" : "s") 
      + sysDflts + " at most";
  }
  return strDelay;

}

function strAutoStartDelay(objVm) {

  var strDelay = "";
  var intMins = "";
  var autostartDelay;
  var sysDflts;

  if (objVm.opt("autostartDelay")==-1) {
    autostartDelay = main.sx.vmAutoDflts("autostartDelay");
    sysDflts = " (system default)";
  } else {
    autostartDelay = objVm.opt("autostartDelay");
    sysDflts = "";
  }

  if (objVm.opt("autostart") != "none") {
    if (autostartDelay==0) {
      strDelay = "Don't Wait" + sysDflts;
    } else {
      intMins = autostartDelay/60;
      strDelay += intMins + " minute" + ((intMins==1) ? "" : "s") + sysDflts;
      if (objVm.opt("autostartContinueWhenToolsRun")) {
	strDelay += ", or when VMware Tools starts";
      }
    }
  }

  return strDelay;
}

/*  // XXX VMs sequence
function strAutoStartOrder(objVm) {
  var strOrder = "";

  if (objVm.opt("autostart") != "none") {
    if (objVm.opt("autostartOrder")==0) {
      strOrder = "No specific order";
    } else {
      // XXX distinguish here between last and before some virtual machine
      strOrder = "Not implemented yet";
    }
  }

  return strOrder;
}
*/
