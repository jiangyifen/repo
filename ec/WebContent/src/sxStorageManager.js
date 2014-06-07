/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// sxStorageManager.js --

var ctx = "editor";

// Tab global cache for disk, LUN and SAN data.
var sanData, dskData, pathCtxMenu;

// iFrames
var op, sanOp, tabs, stat, page, disks, paths, bindings, btns, helpurl;

// An accounting object for the current top-level element (e.g., a context
// menu or control palette). This object will be discovered from init.js using
// initTle().
var tle = new Object();


function opCb(w) {
  if (pageRefreshing != "") {
    // Check if each page is the page refreshing. If it is, then call the opCb
    // for that page. If it is not the page refreshing, we know that something
    // has changed, so reload it.

    if (disks != null) {
      if (disks.att("id") != pageRefreshing) {
        disks.win().location.reload();
      } else if (w.name == "op" && disks.win().opCb) {
        disks.win().opCb(w);
      }
    }

    if (paths != null) {
      if (paths.att("id") != pageRefreshing) {
        paths.win().location.reload();
      } else if (w.name == "sanOp" && paths.win().opCb) {
        paths.win().opCb(w);
      }
    }

    if (bindings != null) {
      if (bindings.att("id") != pageRefreshing) {
        bindings.win().location.reload();
      } else if (w.name == "sanOp" && bindings.win().opCb) {
        bindings.win().opCb(w);
      }
    }

    pageRefreshing = "";
  } else if (w.name == "op") {
    if (disks != null && disks.win().opCb) {
      disks.win().opCb(w);
    }
  } else if (w.name == "sanOp" && page != null) {
    var lq = new Query(sanOp.win().location.search);
    // Make sure that we handle a rescan by updating all of the pages
    // appropriately.
    if (lq.arg("op")[0] == "rescan") {
      sanData = clone(w.data);

      // Don't update the storage configuration tab if an editor is open.
      // XXX: L10N
      if (disks != null) {
        if (disks.win().location.pathname == "/vmware/en/storageConfiguration.html") {
          disks.win().location.reload();
        }
      }

      if (paths != null) { paths.win().location.reload(); }

      if (bindings != null) { bindings.win().location.reload(); }
    } else if ((page.att("id") == "paths" || page.att("id") == "bindings") &&
      page.win().opCb) {
      // We should be looking at a SAN management page.
      page.win().opCb(w);
    }
  }
}

var pageRefreshing = "";

function refresh() {
  if (page == null) { return; }

  pageRefreshing = page.att("id");
  sanData = null; // Clear cache.
  page.win().location.reload();
}


function help() {
  if (helpurl == null | ! helpurl) { return; }

  var w = window.open("", "",
    "location,menubar,resizable,scrollbars,status,toolbar,width=730,height=487");
  w.document.location.href = helpurl;
  w.name = "vmware_mui_help";
  w.focus();
  main.addRgWndw(w);
}


function loadData(u) {
  op.win().location.replace(u);
}


// --------------------------------------------------------------------------
//
// layoutCb --
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
function layoutCb() {
  if (! tabs || ! page || ! stat) return;

  if (page.win().ready == null || ! page.win().ready()) {
    setTimeout("layoutCb();", 50);
    return;
  }

  var ch = 64; // Pessimistic guess for height chrome will add to a window.

  var th = tabs.win().dh();
  var sh = stat.win().dh();
  var ph = page.win().dh();
  // XXX: If btns.css("display") == "none", then btns.win().dh will not be
  // defined. Carry on.
  var bh = btns.win().dh ? btns.win().dh() : 0;

  // Size the window to fit the tabs, status page content and buttons, if
  // possible. XXX rbosch -- this call doesn't seem to change the window size
  // at all, at least on WinXP with Mozilla 1.1

  // The problem is that the methods and properities of the btns iframe
  // document are not accessible in Mozilla 1.1 (as if the document doesn't
  // exist) if the iframe is hidden. -- mikol August 21, 2003 6:15 PM
  if (ph + th + sh + bh < screen.availHeight - ch) {
    // XXX: Gecko-based browsers do not resize properly in -this- context. Some
    // arithmetic starts to break down; If the window should be less than 150px
    // tall, every time the window is sized, it shrinks. If we don't set the
    // default height to 150px, then larger windows size smaller than their
    // optimal height by a fraction. Don't know what is causing this. I thought
    // the problem was in xuaLib.js::dim() setting innerHeight, but I doubt
    // that now since other windows do not have this problem and changing
    // xuaLib.js had unintended side effects. --mikol September 3, 2003 4:16 PM
    if (self.dim(self.dim().w, 150).h < ph + th + sh + bh) {
      self.dim(self.dim().w, ph + th + sh + bh);
    }
  } else {
    self.dim(self.dim().w, screen.availHeight - ch);
  }

  tabs.dim(self.dim().w, th);
  stat.dim(self.dim().w, sh);
  btns.dim(self.dim().w, bh);

  // Size each frame to fit.
  resize();

  if (ph > page.dim().h) { ph = page.dim().h; }

  // Stack the status and page frames under the tab frame.
  stat.pos(0, th);
  page.pos(0, th + sh);
  btns.pos(0, th + sh + ph);

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

    if (! tabs || ! page || ! stat || ! btns) return;

    // Determine minimum width if it has not yet been set.
    if (MW == 0) {
      MW = objW(tabs.doc().getElementById('Tabs'));
      if (MW < 560) MW = 560 < screen.availWidth ? 560 : screen.availWidth - 32;
    }

    // Set the window width to accomodate the tabs and page content comfortably.
    if (MW != self.dim().w) {
      SZ = true;
      self.dim(MW, self.dim().h);
      tabs.dim(MW, tabs.win().dh());
      stat.dim(MW, stat.win().dh());
      btns.dim(MW, btns.win().dh());
    }

    page.dim(self.dim().w, self.dim().h - tabs.dim().h - stat.dim().h - btns.dim().h);
    btns.pos(0, tabs.dim().h +  stat.dim().h + page.dim().h);
  };
}
initResize();


function initTabs() {
  var tbs = {
    disks: tabs.win().obj("disks"),
    paths: tabs.win().obj("paths"),
    bindings: tabs.win().obj("bindings")
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
        case "disks":
          if (main.sx._prodId == "gsx") {
            helpurl = "vserver/storageConfigurationHelp.html";
          } else {
            helpurl = "esx/storageConfigurationHelp.html";
          }

          stat.win().obj("hdr").innerHTML = "Storage Management: " +
            "Disk and LUN Configuration";

          stat.win().obj("subHdr").innerHTML = "Create and modify VMFS " +
            "volumes suitable for storing virtual disk files.";
          break;
        case "paths":
          if (main.sx._prodId == "gsx") {
            helpurl = "vserver/failoverPathsHelp.html";
          } else {
            helpurl = "esx/failoverPathsHelp.html";
          }
          stat.win().obj("hdr").innerHTML = "Storage Management: " +
            "Failover Paths";

          stat.win().obj("subHdr").innerHTML = "Review the current state " +
            "of paths from your system to SAN LUNs.";
          break;
        case "bindings":
          if (main.sx._prodId == "gsx") {
            helpurl = "vserver/adapterBindersHelp.html";
          } else {
            helpurl = "esx/adapterBindersHelp.html";
          }

          stat.win().obj("hdr").innerHTML = "Storage Management: " +
            "Adapter Bindings";

          stat.win().obj("subHdr").innerHTML = "Review the current state " +
            "of your system's SAN adapters and persistent bindings.";
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
          case "disks":
            u = "storageConfiguration.html";
            break;
          case "paths":
            u = "sxFailoverPaths.html";
            break;
          case "bindings":
            u = "sxAdapterBindings.html";
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


// If boolean s is true, show the buttons. If boolean s is false, hide them.
function tglBtns(s) {
  if ((s == null && btns.css("display") == "none") || (s != null && s)) {
    btns.css("display", "");
    btns.dim(self.dim().w, btns.win().dh ? btns.win().dh() : btns.dim().h);
  } else if ((s == null && btns.css("display") != "none") || (s != null && !s)) {
    btns.dim(self.dim().w, btns.win().dh ? btns.win().dh() : 0);
    btns.css("display", "none");
  }
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

  document.title = document.domain + ": Storage Management";

  op = obj("op");
  sanOp = obj("sanOp");
  tabs = obj("tabs");
  stat = obj("stat");
  btns = obj("btns");

  initTabs();

  if (q.arg("tab")[0] == "bindings") {
    slctTab(getTab("bindings"));
  } else if (q.arg("tab")[0] == "paths") {
    slctTab(getTab("paths"));
  } else {
    slctTab(getTab("disks"));
  }

  slctBtns = function (s) {
    if (btns.win().slctBtns == null) {
      window.setTimeout("slctBtns('" + s + "');", 10);
      return;
    }

    return btns.win().slctBtns(s);
  }

  next = function () {
    if (page.win().next) page.win().next(); else self.close();
  };

  prev = function () {
    if (page.win().prev) page.win().prev(); else self.close();
  };

  exit = function (i) {
    if (page.win().exit) page.win().exit(-1); else self.close();
  };
}
