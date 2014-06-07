/* Copyright 1998 VMware, Inc.  All rights reserved. -- VMware Confidential */

// Initialization code for individual frames.

// Pointer to the main control context.
var main = null;

// Global Variables
var errMsgs = new Array();

function noop() { ; }

// Some global constants, used to isolate VMDB schema details
var esOff = "poweredOff";
var esOn = "poweredOn";
var esSusp = "suspended";


function help() {
  if (helpurl == null) return;

  var w = window.open("", "",
    "location,menubar,resizable,scrollbars,status,toolbar,width=730,height=487");
  w.document.location.href = helpurl;
  w.name = "vmware_mui_help";
  w.focus();
  main.addRgWndw(w);
}


function update() {
  if (!main)
    main = getMain();
  if (main.getUpdates) 
    return main.getUpdates(true);
  return null;
}

// --------------------------------------------------------------------------
//
// getMain --
//
//      Return the main control context.
//
//      XXX This method is not prepared to be called from within an embedded
//      frameset where examining parent.isMain would violate browsers' "same
//      origin" security policy.
//
// --------------------------------------------------------------------------

function getMain() {
  var w = self;

  while (main == null) {
    if (w.isMain) {
      return w;
    } else if (w.opener) {
      w = w.opener;
    } else if (w == w.parent) {
      return w;
    } else {
      w = w.parent;
    }
  }
}


// --------------------------------------------------------------------------
//
// initTle --
//
//      Discover the top-level element accounting object.
//
//      XXX This method is not prepared to be called from within an embedded
//      frameset where examining parent.tle would violate browsers' "same
//      origin" security policy.
//
// --------------------------------------------------------------------------

function initTle() {
  var w = self;

  while (self.tle == null && self != self.parent) {
    if (w.tle != null) {
      return self.tle = w.tle;   // Found top-level element accounting object.
    } else {
      w = w.parent;              // Try to find tle in the parent window.
    }
  }
  return null;                   // At top, but tle is not defined.
}


// --------------------------------------------------------------------------
//
// initHover --
//
//      Gecko-based browsers allow a hover pseudoclass to be defined for any
//      element. MSIE does not so we have to fake a hover pseudoclass by
//      adding an onmouseover attribute to the element that should respond to
//      hover events such as
//
//           <td class="console"
//               onmouseover="hover(event);"><img src="../imx/rd-0.gif"
//                                                alt="" /></td>
//
//      Then we define our fake pseudoclass as "<baseClass>Hvr" like so
//
//           .consoleHvr img {
//             border: solid 1px #0066FF;
//           }
//
//      and the hover function defined below takes care of the rest.
//
// --------------------------------------------------------------------------

function initHover() {
  var t = null;

  hover = function (e) {
    e = eObj(e);
    var o = getTrgt(e);

    if (t != null) {
      if (t.useKit != null) {
        if (t.useKit() == "hover") t.useKit("normal");
      } else {
        if (Style.get(t) == Style.HOVR) Style.set(t, Style.NRML);
      }

      t = null;
    }

    if (o != null && e.type == "mouseover") {
      if (o.useKit != null) {
        if (! o.enabled) return;
        if (o.useKit() == "normal") o.useKit("hover");
      } else {
        if (Style.get(o) == Style.DSBL) return;
        if (Style.get(o) == Style.NRML) Style.set(o, Style.HOVR);
      }

      lstn(o, "mouseout", hover);
      t = o;
    }
  };
}

// --------------------------------------------------------------------------
//
// esc --
//
//      Escape everything that needs to be escaped but that JavaScript won't.
//
// Prototype:
//
//      string esc(string s)
//
// Arguments:
//
//      s ... The string to escape
//
// Results:
//
//      A URL-encoded version of s. Additionally, '+' will be encoded.
//
// --------------------------------------------------------------------------

function esc(s) {
  var s = escape(s);
  s = s.split("");
  for (var i = 0; i < s.length; i++) {
    if (s[i] == "+") s[i] = "%2B";
  }
  return s.join("");
}


// --------------------------------------------------------------------------
function initTmpFrame() {
  var lu = new Object();
  var temp = obj("temp");
  if (temp == null) return;

  getTmpFrame = function (i) {
    if (i == null) i = "lu_" + new Date().getTime();

    if (lu[i] == null) {
      lu[i] = initXuaObj(document.body.appendChild(temp.cloneNode(true)));
      lu[i].att("id", i);
      lu[i].att("name", i);
      lu[i].css("border", "solid 1px #000000");
      lu[i].lu = i; // Add look up string property.
      lu[i].fr = self; // Add frame reference property.
    }
    return lu[i];
  }

  remTmpFrame = function (i) {
    if (i == null || lu[i] == null) return;
    var tmp = document.body.removeChild(lu[i]);
    delete lu[i];
    return tmp;
  }
}


// --------------------------------------------------------------------------
function upStr(u, v) {
  var s = u % 60;
  u = (u - s) / 60;

  var m = u % 60;
  u = (u - m) / 60;

  var h = u % 24;
  u = (u - h) / 24;

  var d = u;

  if (v) {
    var r = d > 0 ? d + (d > 1 ? " days" : " day") : "";
    r += (r ? ", " : "") + (h > 0 ? h + (h > 1 ? " hours" : " hour") : "");
    r += (r ? ", " : "") + (m > 0 ? m + (m > 1 ? " minutes" : " minute") : "");
    r += (r ? ", " : "") + (s > 0 ? s + (s > 1 ? " seconds" : " second") : "");
    return r;
  }

  // Return seconds up to 119.
  if (m < 2 && h < 1 && d < 1) {
    s = (s + (m * 60));
    return s + (s > 1 ? " seconds" : " second");
  }

  // Return minutes up to 119.
  if (h < 2 && d < 1) {
    return (m + (h * 60)) + " minutes";
  }

  // Return hours up to 71.
  if (d < 3) {
    return (h + (d * 24)) + " hours";
  }

  return d + " days";
}

// --------------------------------------------------------------------------
function waitingForInput(h) {
  var f = getTmpFrame();
  var u = "/vmware/en/vmWaitingForInput.html?h=" + h + "&lu=" + f.lu;

  f.doc().location.replace(u);
}


// --------------------------------------------------------------------------
function errorMsg(errMsgStr) {
  if(errMsgStr != null) {
    errMsgs.push(unescape(errMsgStr));
  }

  var f = getTmpFrame();
  var u = "/vmware/en/errorMsg.html?lu=" + f.lu;

  f.doc().location.replace(u);
}


function init() {
  var loaded = false;

  try {
    if (self.isMain == null || ! self.isMain) {
      main = getMain();

      // Make sure that the main context is in a good state before proceeding.
      if (main == null || ! main || main.ready == null || ! main.ready()) {
        self.setTimeout("init();", 100);
        return;
      }

      self.getTrgt = main.getTrgt;
      self.MuiWindow = main.MuiWindow;
      self.Style = main.Style;
    } else {
      main = self;
    }
  } catch (err) {
    return; // Security restrictions are preventing us from continuing.
  }

  // Set up the top-level element accounting object.
  initTle();

  // Set up default handlers and functions
  if (self.msdn == null) msdn = noop;
  if (self.msup == null) msup = noop;
  if (self.clik == null) clik = noop;
  if (self.dblk == null) dblk = noop;
  if (self.initPage == null) initPage = noop;

  lstn(document, "mousedown", msdn);
  lstn(document, "mouseup", msup);
  lstn(document, "click", clik);
  lstn(document, "dblclik", dblk);

  // Make sure that double click and drag selections do not affect controls.
  function stpslct(e) {
    e = eObj(e);
    var t = getTrgt(e);

    if (t != null) {
      e.stp();
      return false;
    }
    return true;
  }
  lstn(document, "mousedown", stpslct);
  if (ie) lstn(document, "selectstart", stpslct);

  // Make sure that clicks bring down the top-level element.
  function stpclik(e) {
    e = eObj(e);
    var t = getTrgt(e);

    if (t == null) {
      if (tle.d != null) tle.d();
    }
  }
  lstn(document, "click", stpclik);

  function keepAlive(e) {
    e = eObj(e);
    try {
      if (main.rato.win == self && main.rato.pos.x == e.pos.x && main.rato.pos.y == e.pos.y) return;
    } catch (err) {
      ; // MSIE throws the equivalent of a null pointer exception if main.rato.win is closed.
    }

    main.idle = 0;
    main.rato = {win: self, pos: e.pos};
    main.defaultStatus = (main.idle ? "Idle since " + new Date(main.idle).toString() : "Active");
  }
  lstn(document, "mousemove", keepAlive);

  initTmpFrame();
  initHover();
  initPage();

  // Use F5 or Ctrl R to call main.getUpdates().
  function FF5(e) {
    e = eObj(e);
    if (e.keyCode == 116 || (e.keyCode == 82 && e.ctrlKey)) {
      if (e.shiftKey) {
        parent.location.reload(true);
      } else {
        if (ie) { e.keyCode = 0; }
        e.stp();
        main.getUpdates();
      }
    }
  }
  lstn(document, "keydown", FF5);

  ready = function (b) {
    if (b != null) {
      loaded = b;
    }
    return loaded;
  };
  loaded = true;
}

onload = init;
