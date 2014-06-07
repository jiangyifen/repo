/* Copyright 1998 VMware, Inc.  All rights reserved. -- VMware Confidential */

// vmIndex.js --
//
//      Implements common code for the virtual machine details window.
//
// Todo:
//
//  XXX If this window is closed, the main context will break with a runtime
//      error. Either the error must be caught or dependencies on this window
//      in main context must be properly handled when the window is closed.
//

// iFrames
var op, tabs, page, stat, summary, hardware, options, cpu, memory, disk, network, eventlog, helpurl, vm;
helpurl = "/vmware/en/esx/vmSummaryHelp.html";

// An accounting object for the current top-level element (e.g., a context
// menu or control palette). This object will be discovered from init.js using
// initTle().
var tle = new Object();

var vmCtrlBar, vmCtxMenu;


function opCb(w) {
  if (page != null && page.win().opCb) page.win().opCb(w);
}

// --------------------------------------------------------------------------
//
// reposVmCtrlBar --
//
//      Position the VM control bar near the click origin, within the bounds
//      of the main window.
//
// --------------------------------------------------------------------------

function reposVmCtrlBar(e) {
  var t = getTrgt(e);
  vmCtrlBar.ibp(absPos(t).x + parseInt(objDim(t).w / 2),
      absPos(t).y + parseInt(objDim(t).h / 2) + tabs.dim().h, 999);
}


// --------------------------------------------------------------------------
//
// adjustSize --
//
//      Size the tab and page iframes; stack the former on the latter.
//
// To Do:
//
//    - This function will be useful wherever a tab control is used.
//      Perhaps it can be generalized...
//
// --------------------------------------------------------------------------

var MW = 0;   // Minimum and maximum width for this window
function adjustSize() {
  if (! tabs || ! page || ! stat) return;

  if (page.win().ready == null || ! page.win().ready()) {
    setTimeout("adjustSize();", 50);
    return;
  }

  var ch = 64; // Pessimistic guess for height chrome will add to a window.

  var th = tabs.win().dh();
  var sh = stat.win().dh();
  var ph = page.win().dh();

  // Size the window to fit the tabs, status and page content, if possible.
  if (ph + th + sh < screen.availHeight - ch) {
    self.dim(self.dim().w, ph + th + sh);
  } else {
    self.dim(self.dim().w, screen.availHeight - ch);
  }

  tabs.dim(self.dim().w, tabs.win().dh());
  stat.dim(self.dim().w, stat.win().dh());

  // Size each frame to fit.
  resize();

  // Stack the status and page frames under the tab frame.
  stat.pos(0, tabs.dim().h);
  page.pos(0, tabs.dim().h + stat.dim().h);

  // Make sure that the tabs content occupies the entire width of the frame.
  objCss(tabs.doc().getElementById('NoTabs'), "width", "100%");

  // If the window is resized (i.e., because the noresize window property is
  // ignored by some Linux window managers), make sure the page frame is sized
  // to fit the window.
  lstn(self, "resize", resize);
}


function initResize() {
  var SZ = false;

  resize = function (e) {
    if (SZ) {
      SZ = false;
      return;
    }

    if (! tabs || ! page || ! stat) return;

    // Determine minimum width if it has not yet been set.
    if (MW == 0) {
      MW = objW(tabs.doc().getElementById('Tabs'));
    if (MW < 730) MW = 730 < screen.availWidth ? 730 : screen.availWidth - 32;
    }

    // Set the window width to accomodate the tabs and page content comfortably.
    if (MW != self.dim().w) {
      SZ = true;
      self.dim(MW, self.dim().h);
      tabs.dim(MW, tabs.win().dh());
      stat.dim(MW, stat.win().dh());
    }

    page.dim(self.dim().w, self.dim().h - tabs.dim().h - stat.dim().h);
  }
}
initResize();


function initTabs() {
  var esxTbs = {
    summary: tabs.win().obj("summary"),
    cpu: tabs.win().obj("cpu"),
    memory: tabs.win().obj("memory"),
    disk: tabs.win().obj("disk"),
    network: tabs.win().obj("network"),
    hardware: tabs.win().obj("hardware"),
    options: tabs.win().obj("options"),
    eventlog: tabs.win().obj("eventlog")
  };
  var gsxTbs = {
    summary: tabs.win().obj("summary"),
    options: tabs.win().obj("options"),
    eventlog: tabs.win().obj("eventlog")
  };
  var tbs = main.sx._prodId == "gsx" ? gsxTbs : esxTbs;
  var slct = null;

  if (main.sx._prodId == "gsx") {
    helpurl = "/vmware/en/vserver/vmSummaryHelp.html";
  } else {
    helpurl = "/vmware/en/esx/vmSummaryHelp.html";
  }

  getTab = function (t) {
    return tbs[t];
  };

  tab = function (e) {
    e = eObj(e);
    slctTab(getTrgt(e));
    e.stp();
  };

  slctTab = function (t) {
    var r = false;

    if (slct == t || t == null) {
       // Clicking an active tab will size the window to fit. Useful for Linux
       // WMs that do not obey the noresize request from the browser.
      if (! ie) r = true;
    } else {
      r = true;

      if (tle.d != null) tle.d();

      var i = t.att("id");

      switch (i) {
        case "summary":
          if (main.sx._prodId == "gsx") {
            helpurl = "/vmware/en/vserver/vmSummaryHelp.html";
          } else {
            helpurl = "/vmware/en/esx/vmSummaryHelp.html";
          }
          break;
        case "hardware":
          if (main.sx._prodId == "gsx") {
            helpurl = "/vmware/en/vserver/vmDevicesHelp.html";
          } else {
            helpurl = "/vmware/en/esx/vmDevicesHelp.html";
          }
          break;
        case "options":
          if (main.sx._prodId == "gsx") {
            helpurl = "/vmware/en/vserver/vmOptionsHelp.html";
          } else {
            helpurl = "/vmware/en/esx/vmOptionsHelp.html";
          }
          break;
        case "cpu":
          if (main.sx._prodId == "gsx") {
            helpurl = (vm.hash() == "console") ? "/vmware/en/vserver/scRsrcHelp.html" : 
              "/vmware/en/vserver/vmCpuHelp.html";
          } else {
            helpurl = (vm.hash() == "console") ? "/vmware/en/esx/scRsrcHelp.html" : 
              "/vmware/en/esx/vmCpuHelp.html";
          }
          break;
        case "memory":
          if (main.sx._prodId == "gsx") {
            helpurl = "/vmware/en/vserver/vmMemoryHelp.html";
          } else {
            helpurl = "/vmware/en/esx/vmMemoryHelp.html";
          }
          break;
        case "disk":
          if (main.sx._prodId == "gsx") {
            helpurl = (vm.hash() == "console") ? "/vmware/en/vserver/scRsrcHelp.html" :
              "/vmware/en/vserver/vmDiskHelp.html";
          } else {
            helpurl = (vm.hash() == "console") ? "/vmware/en/esx/scRsrcHelp.html" :
              "/vmware/en/esx/vmDiskHelp.html";
          }
          break;
        case "network":
          if (main.sx._prodId == "gsx") {
            helpurl = "/vmware/en/vserver/vmNetworkHelp.html";
          } else {
            helpurl = "/vmware/en/esx/vmNetworkHelp.html";
          }
          break;
        case "eventlog":
          if (main.sx._prodId == "gsx") {
            helpurl = "/vmware/en/vserver/vmUsersAndEventsHelp.html";
          } else {
            helpurl = "/vmware/en/esx/vmUsersAndEventsHelp.html";
          }
          break;
        default:
          break;
      }

      // Where window[i] is equivalent to window.summary, window.eventlog, etc.
      if (window[i] == null) {
        window[i] = obj(i);

        var u;

        switch (i) {
          case "summary":
            u = "/vmware/en/vmSummary.html";
            break;
          case "hardware":
            u = "/vmware/en/vmDevices.html";
            break;
          case "options":
            u = "/vmware/en/vmOptions.html";
            break;
          case "cpu":
            u = "/vmware/en/vmCpu.html";
            break;
          case "memory":
            u = "/vmware/en/vmMemory.html";
            break;
          case "disk":
            u = "/vmware/en/vmDisk.html";
            break;
          case "network":
            u = "/vmware/en/vmNetwork.html";
            break;
          case "eventlog":
            u = "/vmware/en/vmUsersAndEvents.html";
            break;
          default:
            break;
        }
        window[i].doc().location.replace(u);
      }

      if (page != null) {
        if (page.win().doDn != null) page.win().doDn();

        page.pos(-99999, page.pos().y, -1);
      }
      window[i].pos(0, window[i].pos().y, 1);
      page = window[i];

      Style.set(slct, Style.NRML);
      Style.set(t, Style.SLCT);

      slct = t;
    }

    if (r) {
      initTab();
    }
  };

  for (var t in tbs) {
    tbs[t].ctrl = "tab";
    tabs.win().lstn(tbs[t], "click", tab);
  }
}

function initTab() {
  if (page.win().ready == null || ! page.win().ready()) {
    setTimeout("initTab();", 50);
    return;
  }

  if (page.win().doUp != null) page.win().doUp();
}



// --------------------------------------------------------------------------
//
// initPage --
//
//      Initialize iframes.
//
// --------------------------------------------------------------------------

function initPage() {
  var q = new Query(location.search);
  vm = main.sx.vms[q.arg("h")[0]];
  op = obj("op");
  tabs = obj("tabs");
  stat = obj("stat");

  initTabs();

  var sTab = q.arg("tab")[0];
  if (sTab == "hardware") {
    slctTab(getTab("hardware"));
  } else if (sTab == "options") {
    slctTab(getTab("options"));
  } else if (sTab == "cpu") {
    slctTab(getTab("cpu"));
  } else if (sTab == "memory") {
    slctTab(getTab("memory"));
  } else {
    slctTab(getTab("summary"));
  }

  var s = location.search.substr(1);
  s = s.split('&');
  for (var i = 0; i < s.length; i++) {
    var h = s[i].split('=');
    if (h[0] == 'h') {
      var hash = h[1];
      vm = main.sx.vms[hash];
      break;
    }
  }

  if (vm == null) {
    self.close();
  } else {
    if (vm.hash() == "console") {
      var hideTabs = ["summary", "memory", "network", "hardware", "options", "eventlog"];
      for (var i in hideTabs) {
        var t = hideTabs[i];
        getTab(t).css("display", "none");
      }
    }
    main.sxRemVmObsrvr.lstn("vmProperties", function(){self.close();}, vm);
    document.title = main.sx._name + ": " + vm.dn();
    main.vmDisplayNameObsrvr.lstn("vmProperties", function () {document.title = main.sx._name + ": " + vm.dn()}, vm);
    lstn(self, "unload", function(){try{vm.extra(0);}catch(e){;}if(ie){self.location.reload(true)}});
  }
}
