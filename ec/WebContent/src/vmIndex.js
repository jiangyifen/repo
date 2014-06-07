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
var tabs, page, stat, summary, hardware, eventlog;

// An accounting object for the current top-level element (e.g., a context
// menu or control palette). This object will be discovered from init.js using
// initTle().
var tle = new Object();

var vmCtrlBar;


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
  
  if (page.contentWindow.document.getElementById("bodyObj") == null) {
    self.setTimeout("adjustSize();", 50);
    return;
  }

  var ch = 64; // Pessimistic guess for height chrome will add to a window.
  
  var th = tabs.contentWindow.dh();
  var sh = stat.contentWindow.dh();
  var ph = page.contentWindow.dh();
  
  // Size the window to fit the tabs, status and page content, if possible.
  if (ph + th + sh < screen.availHeight - ch) {
    self.dim(self.dim().w, ph + th + sh);
  } else {
    self.dim(self.dim().w, screen.availHeight - ch);
  }
  
  tabs.dim(self.dim().w, tabs.contentWindow.dh());
  stat.dim(self.dim().w, stat.contentWindow.dh());
  
  // Size each frame to fit.
  resize();

  // Stack the status and page frames under the tab frame.
  stat.pos(0, tabs.dim().h);
  page.pos(0, tabs.dim().h + stat.dim().h);

  // Make sure that the tabs content occupies the entire width of the frame.
  objCss(tabs.contentWindow.document.getElementById('NoTabs'), "width", "100%");

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
      MW = objW(tabs.contentWindow.document.getElementById('Tabs'));
    if (MW < 680) MW = 680;
    }
    
    // Set the window width to accomodate the tabs and page content comfortably.
    if (MW != self.dim().w) { 
      SZ = true;
      self.dim(MW, self.dim().h);
      tabs.dim(MW, tabs.contentWindow.dh());
      stat.dim(MW, stat.contentWindow.dh());
    }
    
    page.dim(self.dim().w, self.dim().h - tabs.dim().h - stat.dim().h);
  }
}
initResize();


function initTabs() {
  var tbs = {
    summary: tabs.contentWindow.obj("summary"),
    hardware: tabs.contentWindow.obj("hardware"),
//     options: tabs.contentWindow.obj("options"),
//     cpu: tabs.contentWindow.obj("cpu"),
//     memory: tabs.contentWindow.obj("memory"),
//     network: tabs.contentWindow.obj("network"),
//     disk: tabs.contentWindow.obj("disk"),
    eventlog: tabs.contentWindow.obj("eventlog")
  };
  var slct = tbs["summary"];

  tab = function (e) {
    e = eObj(e);
    var t = getTrgt(e);
    var r = false;
    
    if (slct == t || t == null) {
       // Clicking an active tab will size the window to fit. Useful for Linux
       // WMs that do not obey the noresize request from the browser.
      if (! ie) r = true;
    } else {
      r = true;
      e.stp();

      if (tle.d != null) tle.d();

      var i = t.att("id");

      // Where window[i] is equivalent to window.summary, window.eventlog, etc.
      if (window[i] == null) {
        window[i] = obj(i);

        var u;

        switch (i) {
          case "summary":
            u = "/vmware/en/vmSummary.html"
              break;
          case "hardware":
            u = "/vmware/en/vmHardware.html"
              break;
          case "eventlog":
            u = "/vmware/en/vmEventLog.html"
              break;
          default:
            break;
        }
        window[i].contentWindow.document.location.replace(u);
      }

      objZ(window[i], 1);
      objZ(page, -1);
      page = window[i];

      Style.set(slct, Style.NRML);
      Style.set(t, Style.SLCT);

      slct = t;
    }
    
    if (r) {
      if (ie) {
        adjustTabSize();
      } else {
        setTimeout("adjustTabSize();", 100);
      }
    }
  }

  for (var t in tbs) {
    tbs[t].ctrl = "tab";
    tabs.contentWindow.lstn(tbs[t], "click", tab);
  }
}

function adjustTabSize() {
  if (! ie && page.contentWindow.document.getElementById("bodyObj") == null) {
    lstn(page.contentWindow, "onload", adjustSize);
  }
  adjustSize();
}


// --------------------------------------------------------------------------
//
// initPage --
//
//      Initialize iframes.
//
// --------------------------------------------------------------------------

function initPage() {
  tabs = obj("tabs");
  stat = obj("stat");
  page = summary = obj("summary");

  vmCtrlBar = initXuaObj(dup(main.vmCtrlBar));
  
  adjustSize();
  initTabs();
}
