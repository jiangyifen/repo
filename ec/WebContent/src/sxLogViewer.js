/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// vmLogViewer.js --

// iFrames
var op, tabs, stat, page, vmkwarning, vmkernel, messages, vmksummary, helpurl;

// An accounting object for the current top-level element (e.g., a context
// menu or control palette). This object will be discovered from init.js using
// initTle().
var tle = new Object();

function opCb(w) {
  // if (page != null && page.win().opCb) page.win().opCb(w);
  if (page != null && page.win().init) page.win().init(w);
  adjustSize();
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
  };
}
initResize();


function initTabs() {
  var tbs = {
    vmkwarning: tabs.win().obj("vmkwarning"),
    vmkernel: tabs.win().obj("vmkernel"),
    messages: tabs.win().obj("messages"),
    vmksummary: tabs.win().obj("vmksummary")
  };
  var slct = null;

  getSlctdTabId = function () {
    return slct != null ? slct.att("id") : null;
  };

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
        case "vmkwarning":
          if (main.sx._prodId == "gsx") {
            helpurl = "vserver/VMkernelWarningsHelp.html";
          } else {
            helpurl = "esx/VMkernelWarningsHelp.html";
          }
          break;
        case "vmkernel":
          if (main.sx._prodId == "gsx") {
            helpurl = "vserver/VMkernelLogHelp.html";
          } else {
            helpurl = "esx/VMkernelWarningsHelp.html";
          }
          break;
        case "messages":
          if (main.sx._prodId == "gsx") {
            helpurl = "vserver/ServiceConsoleLogHelp.html";
          } else {
            helpurl = "esx/ServiceConsoleLogHelp.html";
          }
          break;
        case "vmksummary":
          if (main.sx._prodId == "gsx") {
            helpurl = "vserver/AvailabilityReportHelp.html";
          } else {
            helpurl = "esx/AvailabilityReportHelp.html";
          }
          break;
        default:
          helpurl = null;
          break;
      }

      // Where window[i] is equivalent to window.summary, window.eventlog, etc.
      if (window[i] == null) {
        window[i] = obj(i);

        var u;

        switch (i) {
          case "vmkwarning":
            u = "/sx-log?log=vmkwarning";
            break;
          case "vmkernel":
            u = "/sx-log?log=vmkernel";
            break;
          case "messages":
            u = "/sx-log?log=messages";
            break;
          case "vmksummary":
            u = "/sx-log?log=vmksummary.html";
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
  var q = new Query(location.search);

  document.title = document.domain + ": System Logs";

  op = obj("op");
  tabs = obj("tabs");
  stat = obj("stat");

  initTabs();

  if (q.arg("tab")[0] == "vmkernel") {
    slctTab(getTab("vmkernel"));
  } else if (q.arg("tab")[0] == "messages") {
    slctTab(getTab("messages"));
  } else if (q.arg("tab")[0] == "vmksummary") {
    slctTab(getTab("vmksummary"));
  } else {
    slctTab(getTab("vmkwarning"));
  }
}
