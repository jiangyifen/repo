/* Copyright 1998 VMware, Inc.  All rights reserved. -- VMware Confidential */

// main.js --
//
//      Implements the core logic for the management interface.


// iFrames
var op, auth, tabs, page, data, sxOptions, monitor, hostOs, helpurl, memory, report, currTab;
helpurl = "/vmware/en/esx/sxMonitorHelp.html";
// Default Hardware Version XXX: Hardcoded
defaultHwv = 3;

// Facts about the current user - name, is an admin, can create VMs, current
// activity, mouse position, the number of times we have tried to restart the
// data pull loop and the ax number of times we should try.
var user, admin, canAddVm, idle, rato, rtry, mxrtry;
idle = new Date().getTime();
rato = {win: self, pos: new Pos(0, 0)};
rtry = 0;
mxrtry = 4;

// Refresh rate, statistic sampling period and a primitive update lock
var interval, period, updatePending;
updatePending = false;

// An accounting object for the current top-level element (e.g., a context
// menu or control palette). This object will be discovered from init.js using
// initTle().
var tle = new Object();

var vmCtrlBar, vmCtxMenu;

// The object returned by setTimeout(), the object returned by setInterval(),
// the time in milliseconds since the epoch that the data was last refreshed.
var wto, bto, lmod;
lmod = new Date().getTime();

// Used in an interval loop, this function should be more reliable than timed
// out getUpdates. If we haven't seen an update for twice the length of the
// interval, then we will force another getUpdates call.
function aux() {
  if (hasSslChanged()) return;

  if ((new Date().getTime() - lmod) > (interval * 2)) {
    rtry++;

    // If this attempt to restart the data pull loop is going to push us beyond
    // our maximum number of retries, bail.
    if (rtry > mxrtry) {
      clearInterval(bto);
      alert("Could not restart data refresh cycle after "+mxrtry+" attempts.");
      main.document.location.replace("about:blank");
      return;
    }

    if (rtry > 0) {
      clearInterval(bto);
      bto = setInterval("aux();", 30 * 1000 * rtry);
    }

    retryObsrvr.exec();

    clearTimeout(wto);
    wto = null;
    idle = idle ? idle : new Date().getTime();
    updatePending = false;
    getUpdates();
  } else {
    rtry = 0;
  }
}

// Global list of virtual machines
var sx;
var servers = new Array();

// Global execution state image cache
var esImx = {
  stp: {
    DSBL: new Image(22, 22),
    NRML: new Image(22, 22),
    SLCT: new Image(22, 22)
  },

  pse: {
    DSBL: new Image(22, 22),
    NRML: new Image(22, 22),
    SLCT: new Image(22, 22)
  },

  stt: {
    DSBL: new Image(22, 22),
    NRML: new Image(22, 22),
    SLCT: new Image(22, 22)
  },

  rst: {
    DSBL: new Image(22, 22),
    NRML: new Image(22, 22),
    SLCT: new Image(22, 22)
  },

  wrn: {
    DSBL: new Image(22, 22),
    NRML: new Image(22, 22),
    SLCT: new Image(22, 22)
  }
};

esImx.stt.DSBL.src = "../imx/power_on-dsbl.gif";
esImx.stt.NRML.src = "../imx/power_on.gif";
esImx.stt.SLCT.src = "../imx/power_on-slct.gif";

esImx.stp.DSBL.src = "../imx/power_off-dsbl.gif";
esImx.stp.NRML.src = "../imx/power_off.gif";
esImx.stp.SLCT.src = "../imx/power_off-slct.gif";

esImx.pse.DSBL.src = "../imx/suspend-dsbl.gif";
esImx.pse.NRML.src = "../imx/suspend.gif";
esImx.pse.SLCT.src = "../imx/suspend-slct.gif";

esImx.rst.DSBL.src = "../imx/reset-dsbl.gif";
esImx.rst.NRML.src = "../imx/reset.gif";
esImx.rst.SLCT.src = "../imx/reset-slct.gif";

esImx.wrn.DSBL.src = "../imx/warning-22x22.png";
esImx.wrn.NRML.src = "../imx/warning-22x22.png";
esImx.wrn.SLCT.src = "../imx/warning-22x22.png";

// Global execution state image cache
var gosImx = {
  win32: {
    DSBL: new Image(22, 22),
    NRML: new Image(22, 22),
    SLCT: new Image(22, 22)
  },

  linux: {
    DSBL: new Image(22, 22),
    NRML: new Image(22, 22),
    SLCT: new Image(22, 22)
  },

  novell: {
    DSBL: new Image(22, 22),
    NRML: new Image(22, 22),
    SLCT: new Image(22, 22)
  },

  bsd: {
    DSBL: new Image(22, 22),
    NRML: new Image(22, 22),
    SLCT: new Image(22, 22)
  },

  solaris: {
    DSBL: new Image(22, 22),
    NRML: new Image(22, 22),
    SLCT: new Image(22, 22)
  },

  def: {
    DSBL: new Image(22, 22),
    NRML: new Image(22, 22),
    SLCT: new Image(22, 22)
  },

  wrn: {
    DSBL: new Image(22, 22),
    NRML: new Image(22, 22),
    SLCT: new Image(22, 22)
  },

  err: {
    DSBL: new Image(22, 22),
    NRML: new Image(22, 22),
    SLCT: new Image(22, 22)
  }
};

gosImx.win32.DSBL.src = "../imx/win32.png";
gosImx.win32.NRML.src = "../imx/win32.png";
gosImx.win32.SLCT.src = "../imx/win32.png";

gosImx.linux.DSBL.src = "../imx/linux.png";
gosImx.linux.NRML.src = "../imx/linux.png";
gosImx.linux.SLCT.src = "../imx/linux.png";

gosImx.novell.DSBL.src = "../imx/novell.png";
gosImx.novell.NRML.src = "../imx/novell.png";
gosImx.novell.SLCT.src = "../imx/novell.png";

gosImx.bsd.DSBL.src = "../imx/bsd.png";
gosImx.bsd.NRML.src = "../imx/bsd.png";
gosImx.bsd.SLCT.src = "../imx/bsd.png";

gosImx.solaris.DSBL.src = "../imx/solaris.png";
gosImx.solaris.NRML.src = "../imx/solaris.png";
gosImx.solaris.SLCT.src = "../imx/solaris.png";

gosImx.def.DSBL.src = "../imx/rd-0.gif";
gosImx.def.NRML.src = "../imx/rd-0.gif";
gosImx.def.SLCT.src = "../imx/rd-0.gif";

gosImx.wrn.DSBL.src = "../imx/rc-warning.png";
gosImx.wrn.NRML.src = "../imx/rc-warning.png";
gosImx.wrn.SLCT.src = "../imx/rc-warning.png";

gosImx.err.DSBL.src = "../imx/rc-error.png";
gosImx.err.NRML.src = "../imx/rc-error.png";
gosImx.err.SLCT.src = "../imx/rc-error.png";


// --------------------------------------------------------------------------
//
// getTrgt --
//
//      Get a viable event target from a literal-but-not-always-what-we-want
//      event source by recursively looking for the ctrl property in the event
//      source object and its parents.
//
// --------------------------------------------------------------------------

function getTrgt(e) {
  var trgt = e.src;
  // XXX: getTrgt fires when a disabled form element is clicked in MSIE.
  // trgt.nodeName will not be defined in this case. Weird. --mikol May 30,
  // 2003 3:02 PM
  if (trgt != null && trgt.nodeName != null) {
    while (trgt.nodeName.toUpperCase() != "HTML" && trgt.ctrl == null) {
      trgt = trgt.parentNode;
    }
  }

  if (trgt != null && trgt.ctrl != null) return trgt;

  return null;
}


// --------------------------------------------------------------------------
//
// getEsImg --
//
//      Given a virtual machine vm, return a ref to the correct Image object
//      representing its state.
//
// --------------------------------------------------------------------------

function getEsImg(vm) {
  if (vm.wfi().id != null) return esImx.wrn.NRML;

  switch (vm.es()) {
    case esOff:
      switch (vm.op()) {
        case "start":
          return esImx.stt.SLCT;
        default:
          return esImx.stp.NRML;
      }

    case esSusp:
      switch (vm.op()) {
        case "start":
          return esImx.stt.SLCT;
        default:
          return esImx.pse.NRML;
      }

    case esOn:
      switch (vm.op()) {
        case "stop":
          return esImx.stp.SLCT;
        case "pause":
          return esImx.pse.SLCT;
        case "restart":
          return esImx.rst.SLCT;
        default:
          return esImx.stt.NRML;
      }

    default:
      return null;
  }
}


// --------------------------------------------------------------------------
//
// getGosImg --
//
//      Given a virtual machine vm, return a ref to the correct Image object
//      representing its guest operating system.
//
// --------------------------------------------------------------------------

function getGosImg(vm) {
  // Generic "linux" is listed as unsupported, so we don't see it in sx._guests
  if (vm.os() == "linux") {
    return gosImx.linux.NRML;
  }
  for (var i = 0; i < sx._guests.length; i++) {
    if (sx._guests[i].key == vm.os()) {
      switch (sx._guests[i].family) {
      case "windows":
        return gosImx.win32.NRML;
      case "linux":
        return gosImx.linux.NRML;
      case "netware":
        return gosImx.novell.NRML;
      case "solaris":
        return gosImx.solaris.NRML;
      case "other":
      default:
        if (vm.os() == "dos") {
          return gosImx.def.NRML;
        } else if (vm.os().toLowerCase() == "freebsd") {
          return gosImx.bsd.NRML;
        } else {
          return gosImx.def.NRML;
        }
      }
    }
  }
  return gosImx.def.NRML;
}


// --------------------------------------------------------------------------
//
// Style --
//
//      This singleton object provides abstractions for applying styles to
//      HTML elements.
//
// --------------------------------------------------------------------------

var Style = new Object();
Style.NRML = 0;
Style.HOVR = 1;
Style.SLCT = 2;
Style.DSBL = 3;
Style.keys = ["", "Hvr", "Slct", "Dsbl"];


// --------------------------------------------------------------------------
//
// Style.set --
//
//      This function applies style s (where s is one of the Style constants)
//      to HTML element o.
//
// Prototype:
//
//      string Style.set(Object o, int s)
//
// Arguments:
//
//      o ... The HTML element that will be styled
//      s ... An integer corresponding to one of the Style constants
//
// Results:
//
//      The CSS class name set for the HTML element.
//
// --------------------------------------------------------------------------

Style.set = function (o, s) {
  var b = objAtt(o, "class");
  if (b == null || ! b) return -1;

  // Ensure that b represents the normal style for this class.
  for (var i = 1; i < this.keys.length; i++) {
    b = b.split(this.keys[i])[0];
  }

  // Apply the selected style s.
  return objAtt(o, "class", b + this.keys[s]);
};


// --------------------------------------------------------------------------
//
// Style.get --
//
//      This function gets the current style of HTML element o.
//
// Prototype:
//
//      string Style.set(Object o, int s)
//
// Arguments:
//
//      o ... The HTML element to query
//
// Results:
//
//      An integer corresponding to a Style constant, or -1.
//
// --------------------------------------------------------------------------

Style.get = function (o) {
  var c = objAtt(o, "class");
  if (c == null || ! c) return -1;
  var b = c;

  // Ensure that b represents the normal style for this class.
  for (var i = 1; i < this.keys.length; i++) {
    b = b.split(this.keys[i])[0];
  }

  for (var i = 0; i < this.keys.length; i++) {
    if (c == b + this.keys[i]) return i;
  }
  return -1;
};


// --------------------------------------------------------------------------
//
// VM --
//
//      VM constructor.
//
// --------------------------------------------------------------------------

function VM(/*sx,*/ hash,cfg,dn,es,hb,vcpu,cpu,ram,os,up,gs,tr,pid,wid,mode,wfi,lc,hwv,usr) {
  this._cfg = cfg;
  this._hash = hash;
  this._dn = dn;
  this._es = es;
  this._hb = hb;
  this._vcpu = vcpu;
  this._cpu = cpu;
  this._ram = ram;
  this._os = os;
  this._up = up;
  this._gs = gs;
  this._tr = tr;
  this._pid = pid;
  this._wid = wid;
  this._mode = mode;
  this._wfi = wfi;
  this._lc = lc;
  this._hwv = hwv;
  this._usr = usr;
  this._err = {id:0,msg:""};
  this._frame;
  this._op;
  this._extra = 0; // extra info bit: 1 -> VM Overview, 2 -> Hardware, 4 -> Users & Events, 8 -> Resources
  this._maxCpu = 0;
  this._minCpu = 0;
  this._maxRam = 0;
  this._minRam = 0;
  this._memSize = "Unknown";
  this._ip = "Unknown";
  this._dev = {};
  this._opt = {};
  this._evt = [];
  this._con = {};
  this._rsrcCpu = {};
  this._rsrcMem = {};
  this._rsrcDsk = {};
  this._rsrcNet = {};

  // XXX This will break when we support multiple servers.
  sx.vms[this._hash] = this;
  sxAddVmObsrvr.exec(sx, this);
}

function VM_GetSetHash(v) {
  if (v != null && v != this._hash) {
    this._hash = v;
//     vmCfgObsrvr.exec(this, this._hash);
  }
  return this._hash;
}
VM.prototype.hash = VM_GetSetHash;

function VM_GetSetCfg(v) {
  if (v != null && v != this._cfg) {
    this._cfg = v;
    vmCfgObsrvr.exec(this, this._cfg);
  }
  return this._cfg;
}
VM.prototype.cfg = VM_GetSetCfg;

function VM_GetSetDisplayName(v) {
  if (v != null && v != this._dn) {
    this._dn = v;
    vmDisplayNameObsrvr.exec(this, this._dn);
  }
  return this._dn;
}
VM.prototype.dn = VM_GetSetDisplayName;

function VM_GetSetGuestOs(v) {
  if (v != null && (this._os != null && this._os != "" && v != "--") &&
    v != this._os) {
    this._os = v;
    vmGuestOsObsrvr.exec(this, this._os);
  }
  return this._os;
}
VM.prototype.os = VM_GetSetGuestOs;

function VM_GetSetHb(v) {
  if (v != null) {
    if (v != this._hb) {
      this._hb = v;
      vmHbObsrvr.exec(this, this._hb);
    }
  }
  return this._hb;
}
VM.prototype.hb = VM_GetSetHb;

function VM_GetSetCpu(v) {
  if (v != null) {
    if (v != this._cpu) {
      this._cpu = v;
      vmCpuObsrvr.exec(this, this._cpu);
    }
  }
  return this._cpu;
}
VM.prototype.cpu = VM_GetSetCpu;

function VM_GetSetVcpu(v) {
  if (v != null) {
    if (v != this._vcpu) {
      this._vcpu = v;
      vmVcpuObsrvr.exec(this, this._vcpu);
    }
  }
  return this._vcpu;
}
VM.prototype.vcpu = VM_GetSetVcpu;

function VM_GetSetRam(v) {
  if (v != null) {
    if (v != this._ram) {
      this._ram = v;
      vmRamObsrvr.exec(this, this._ram);
    }
  }
  return this._ram;
}
VM.prototype.ram = VM_GetSetRam;

function VM_GetSetExecutionState(v) {
  if (v != null) {
    // If the guest is restarting, we want to wait to acknowledge a "change" in
    // state until it has been powered on again.
    if ((v != this._es && this._op != "restart") ||
      (this._op == "restart" && v == esOn)) {
      this._es = v;
      if (this._op != null) this._op = null;
      if (this._es == esOff || this._es == esSusp) {
        this.ram(0);
        this.wid(0);
        this.cpu(0);
        this.up(0);
        this.hb(0);
      }
      vmExecutionStateObsrvr.exec(this, this._es);
      vmDisplayNameObsrvr.exec(this, this._dn);
      vmHbObsrvr.exec(this, this._hb);
      vmWidObsrvr.exec(this, this._wid);
      vmCpuObsrvr.exec(this, this._cpu);
      vmRamObsrvr.exec(this, this._ram);
      vmUptimeObsrvr.exec(this, this._up);
    }
  }
  return this._es;
}
VM.prototype.es = VM_GetSetExecutionState;

function VM_GetSetUptime(v) {
  if (v != null) {
    if (v != this._up) {
      this._up = v;
      vmUptimeObsrvr.exec(this, this._up);
    }
  }
  return this._up;
}
VM.prototype.up = VM_GetSetUptime;

function VM_GetSetGuestState(v) {
  if (v != null) {
    if (v != this._gs) {
      this._gs = v;
      vmGuestStateObsrvr.exec(this, this._gs);
    }
  }
  return this._gs;
}
VM.prototype.gs = VM_GetSetGuestState;

function VM_GetSetToolsReady(v) {
  if (v != null) {
    if (v != this._tr) {
      this._tr = v;
      vmHbObsrvr.exec(this, this._hb);
      vmToolsReadyObsrvr.exec(this, this._tr);
    }
  }
  return this._tr;
}
VM.prototype.tr = VM_GetSetToolsReady;

function VM_GetSetPid(v) {
  if (v != null) {
    if (v != this._pid) {
      this._pid = v;
      vmPidObsrvr.exec(this, this._pid);
    }
  }
  return this._pid;
}
VM.prototype.pid = VM_GetSetPid;

function VM_GetSetWid(v) {
  if (v != null) {
    if (v != this._wid) {
      this._wid = v;
      vmWidObsrvr.exec(this, this._wid);
    }
  }
  return this._wid;
}
VM.prototype.wid = VM_GetSetWid;

function VM_GetSetMode(v) {
  if (v != null) {
    if (v != this._mode) {
      this._mode = v;
      vmModeObsrvr.exec(this, this._mode);
    }
  }
  return this._mode;
}
VM.prototype.mode = VM_GetSetMode;

function VM_GetFrame() {
  if (this._frame == null) {
    this._frame = document.createElement('iframe');
    objAtt(this._frame, "id", "op" + this._hash);
    objAtt(this._frame, "name", "op" + this._hash);
    objAtt(this._frame, "src", "/vmware/blank.html");
    objCss(this._frame, "position", "absolute");
    objCss(this._frame, "top", "-99999px");
    objCss(this._frame, "left", "-99999px");
    this._frame = document.body.appendChild(this._frame);
    this._frame = initXuaObj(this._frame);
  }
  return this._frame;
}
VM.prototype.frame = VM_GetFrame;

function VM_GetSetOp(v) {
  if (v != null && v != this._op) {
    this._op = v ? v : null;
    vmExecutionStateObsrvr.exec(this, this._es);
  }
  return this._op;
}
VM.prototype.op = VM_GetSetOp;

function VM_GetSetExtra(v) {
  if (v != null) {
    if (v == 0) {
      this._extra = v;
    } else if ((this._extra & v ) == v) {
      this._extra -= v;
    } else {
      this._extra += v;
    }
  }
  return this._extra;
}
VM.prototype.extra = VM_GetSetExtra;

function VM_GetSetMaxCpu(v) {
  if (v != null) {
    if (v != this._maxCpu) {
      this._maxCpu = v;
      vmMaxCpuObsrvr.exec(this, this._maxCpu);
    }
  }
  return this._maxCpu;
}
VM.prototype.maxCpu = VM_GetSetMaxCpu;

function VM_GetSetMinCpu(v) {
  if (v != null) {
    if (v != this._minCpu) {
      this._minCpu = v;
      vmMinCpuObsrvr.exec(this, this._minCpu);
    }
  }
  return this._minCpu;
}
VM.prototype.minCpu = VM_GetSetMinCpu;

function VM_GetSetMaxRam(v) {
  if (v != null) {
    if (v != this._maxRam) {
      this._maxRam = v;
      vmMaxRamObsrvr.exec(this, this._maxRam);
    }
  }
  return this._maxRam;
}
VM.prototype.maxRam = VM_GetSetMaxRam;

function VM_GetSetMinRam(v) {
  if (v != null) {
    if (v != this._minRam) {
      this._minRam = v;
      vmMinRamObsrvr.exec(this, this._minRam);
    }
  }
  return this._minRam;
}
VM.prototype.minRam = VM_GetSetMinRam;

function VM_GetSetMemSize(v) {
  if (v != null) {
    if (v != this._memSize) {
      this._memSize = v;
      vmMemSizeObsrvr.exec(this, this._memSize);
    }
  }
  return this._memSize;
}
VM.prototype.memSize = VM_GetSetMemSize;

function VM_GetSetIp(v) {
  if (v != null) {
    if (v != this._ip) {
      this._ip = v;
      vmIpObsrvr.exec(this, this._ip);
    }
  }
  return this._ip;
}
VM.prototype.ip = VM_GetSetIp;

function VM_GetSetDev(d, v) {
  if (d == "scsi0" || d == "scsi1" || d == "usb") {
    return;
  }

  if (v != null) {
    var diff = false;

    // Create the device object if it doesn't already exist.
    if (this._dev[d] == null) {
      this._dev[d] = new Object();
      this._dev[d].id = d;
      diff = true;
    }

    // Look for diffs and new keys.
    for (var k in v) {
      if (this._dev[d][k] == null || this._dev[d][k] != v[k]) {
        this._dev[d][k] = v[k];
        diff = true;
      }
    }

    // Remove old keys.
    for (var k in this._dev[d]) {
      if (v[k] == null) {
        delete this._dev[d][k];
        diff = true;
      }
    }

    if (diff)
      vmDevObsrvr.exec(this, this._dev[d]);
  }
  return this._dev[d];
}
VM.prototype.dev = VM_GetSetDev;

function VM_GetSetOpt(k, v) {
  if (v != null) {
    // Create the option if it doesn't already exist.
    if (this._opt[k] == null || this._opt[k] != v) {
      this._opt[k] = v;
      vmOptObsrvr.exec(this, k);
      // execute specific option observers -
      // some listeners will be interested in specific option updates
      switch (k) {
        case "autostart" :
	  vmOptAutoStartObsrvr.exec(this, k);
	  break;
      }
    }
  }
  return this._opt[k];
}
VM.prototype.opt = VM_GetSetOpt;

function VM_GetSetEvt(i, v) {
  if (v != null) {
    var diff = false;

    // Create the event object if it doesn't already exist.
    if (this._evt[i] == null) {
      this._evt[i] = v;
      this._evt[i].id = i;
      diff = true;
    }

    // Look for diffs.
    for (var k in v) {
      if (this._evt[i][k] != v[k]) {
        this._evt[i][k] = v[k];
        diff = true;
      }
    }

    if (diff)
      vmEvtObsrvr.exec(this, this._evt[i]);
  }
  return this._evt[i];
}
VM.prototype.evt = VM_GetSetEvt;

function VM_GetSetCon(i, v) {
  if (v != null) {
    var diff = false;

    // Create the event object if it doesn't already exist.
    if (this._con[i] == null) {
      this._con[i] = v;
      this._con[i].id = i;
      diff = true;
    }

    // Look for diffs.
    for (var k in v) {
      if (this._con[i][k] != v[k]) {
        this._con[i][k] = v[k];
        diff = true;
      }
    }

    if (diff)
      vmConObsrvr.exec(this, this._con[i]);
  }
  return this._con[i];
}
VM.prototype.con = VM_GetSetCon;

function VM_GetSetRsrc(i, v, r, o) {
  function diff(a, b) {
    if (typeof(a) == "object" && typeof(b) == "object") {
      for (var i in a) {
        if (diff(a[i], b[i])) return true;
      }
      for (var i in b) {
        if (diff(a[i], b[i])) return true;
      }
      return false;
    } else {
      return (a != b);
    }
  }

  function dup(a) {
    var d;
    if (typeof(a) == "object") {
      if (a == null) {
        // Anonymous constructions such as [] or {} will report typeof ==
        // "object" but will also be null.
        return a;
      }

      // XXX: The try clause works perfectly fine when the data is coming from
      // the server. When rsrcNet is manually invoked from vmRsrcNetwork.js, we
      // get an exception; I don't know why. The code with a try/catch
      // statement A) sidesteps PR 28922 and B) is certainly no worse than
      // without one. --mikol July 8, 2003 10:34 AM
      try {
        d = new a.constructor();
      } catch (ex) {
        var cnstrctr = a.constructor.toString();
        if (cnstrctr.indexOf("function Object()") == 1) {
          d = {};
        } else if (cnstrctr.indexOf("function Array()") == 1) {
          d = [];
        } else {
          throw("VM_GetSetRsrc::dup: Could not use constructor:\n" +
            cnstrctr);
        }
      }
      for (var i in a) {
        try { d[i] = dup(a[i]); } catch (ex) { alert(ex); }
      }
    } else {
      // Disallow negative values (-1 is used for unset -- report as 0)
      d = (a < 0) ? 0 : a;
    }
    return d;
  }

  if (v != null) {
     if (r[i] == null) {
        r[i] = {};
     }
     if (diff(r[i].val, v)) {
       try { r[i].val = dup(v); } catch (ex) { alert(ex); }
       r[i].id = i;
       o.exec(this, r[i]);
     }
  }
  return r[i];

}
VM.prototype.rsrc = VM_GetSetRsrc;

function VM_GetSetRsrcCpu(i, v) {
  return this.rsrc(i, v, this._rsrcCpu, vmRsrcCpuObsrvr);
}
VM.prototype.rsrcCpu = VM_GetSetRsrcCpu;

function VM_GetSetRsrcMem(i, v) {
  return this.rsrc(i, v, this._rsrcMem, vmRsrcMemObsrvr);
}
VM.prototype.rsrcMem = VM_GetSetRsrcMem;

function VM_GetSetRsrcDsk(i, v) {
  return this.rsrc(i, v, this._rsrcDsk, vmRsrcDskObsrvr);
}
VM.prototype.rsrcDsk = VM_GetSetRsrcDsk;

function VM_GetSetRsrcNet(i, v) {
  return this.rsrc(i, v, this._rsrcNet, vmRsrcNetObsrvr);
}
VM.prototype.rsrcNet = VM_GetSetRsrcNet;

function VM_GetSetWfi(v) {
  if (v != null) {
    if (v.id != this._wfi.id) {
      this._wfi.id = v.id;
      this._wfi.q = v.q;
      this._wfi.a = v.a;
      vmWfiObsrvr.exec(this, this._wfi);
    }
  }
  return this._wfi;
}
VM.prototype.wfi = VM_GetSetWfi;

function VM_GetSetLc(v) {
  if (v != null) {
    if (v != this._lc) {
      this._lc = v;
      vmLcObsrvr.exec(this, this._lc);
    }
  }
  return this._lc;
}
VM.prototype.lc = VM_GetSetLc;

function VM_GetSetHwv(v) {
  if (v != null) {
    if (v != this._hwv) {
      this._hwv = v;
      vmHwvObsrvr.exec(this, this._hwv);
    }
  }
  return this._hwv;
}
VM.prototype.hwv = VM_GetSetHwv;

function VM_GetSetUsr(v) {
  if (v != null) {
    if (v != this._usr) {
      this._usr = usr;
      vmUsrObsrvr.exec(this, this._usr);
    }
  }
  return this._usr;
}
VM.prototype.usr = VM_GetSetUsr;

// --------------------------------------------------------------------------
//
// vmOp --
//
//      This function performs operation o on the virtual machine specified
//      by hash h.
//
// Prototype:
//
//      void vmOp(string h, string o, boolean f)
//
// Arguments:
//
//      h ... A hash corresponding to a virtual machine
//      o ... The operation
//      f ... Whether or not to force a hard power operation
//
// Results:
//
//      None. The success or failure of operation o will be returned from the
//      server in the data frame.
//
// --------------------------------------------------------------------------

function vmOp(h, o, f, w) {
  // XXX This will break when we support multiple servers.
  var vm = sx.vms[h];
  if (vm == null) return;

  // If f is not specified and the tools are not ready, use a hard op.
  if (f == null) {
    f = vm.es() == esOn && ! vm.tr();
  }

  function doop(op) {
    // If the current user does not have execute permissions, bail.
    if ((vm.mode() & 1) == 0) {
      return false;
    }

    if ((op == "powerOff" || op == "reset") && f &&
      ! w.confirm("Powering off or resetting a virtual machine " +
        "before shutting down its guest operating system " +
        "may have unintended consequences.\n\n" +
        "Are you sure you want to continue?")) {
      return false;
    }

    var u = "/power" + (sx._os == "win32" ? "/index.pl" : "") +
            "?vmid=" + h + "&op=" + op + "&optype=" + (f ? 1 : 0);

    vm.frame().doc().location.replace(u);

    return true;
  }

  switch (o)
  {
    case "stop":
      if (vm.es() == esOn) {
        if (doop("powerOff"))
          vm.op("stop");
      }
      break;
    case "start":
      if (vm.es() == esOff) {
        if (doop("powerOn"))
          vm.op("start");
      } else if (vm.es() == esSusp) {
        if (doop("resume"))
          vm.op("start");
      }
      break;
    case "pause":
      if (vm.es() == esOn) {
        if (doop("suspend"))
          vm.op("pause");
      }
      break;
    case "restart":
      if (vm.es() == esOn) {
        if (doop("reset"))
          vm.op("restart");
      }
      break;
    default:
      break;
  }
}


// --------------------------------------------------------------------------
//
// SX --
//
//      Server constructor.
//
// --------------------------------------------------------------------------

function SX(name, prodId, prodName, cpuInfo, memInfo, cpu, vmCpu, ram, vmRam, os, up, numaInfo, license, ht, htEnbl, vmAutoDflts, guests, agtId) {
  this.vms = new Array();
  this._name = name;
  this._prodId = prodId;
  this._prodName = prodName;
  this._cpuInfo = cpuInfo;
  this._memInfo = memInfo;
  this._cpu = cpu;
  this._vmCpu = vmCpu;
  this._ram = ram;
  this._vmRam = vmRam;
  this._os = os;
  this._up = up;
  this._numaInfo = numaInfo;
  this._license = license;
  this._ht = ht;
  this._htEnbl = htEnbl;
  this._vmAutoDflts = vmAutoDflts;
  this._guests = guests;
  this._agtId = agtId;

  document.title = this._name + ": VMware Management Interface";
}

function SX_GetSetCpu(v) {
  if (v != null) {
    if (v != this._cpu) {
      this._cpu = v;
      sxCpuObsrvr.exec(this, this._cpu);
    }
  }
  return this._cpu;
}
SX.prototype.cpu = SX_GetSetCpu;

function SX_GetSetVmCpu(v) {
  if (v != null) {
    if (v != this._vmCpu) {
      this._vmCpu = v;
      sxVmCpuObsrvr.exec(this, this._vmCpu);
    }
  }
  return this._vmCpu;
}
SX.prototype.vmCpu = SX_GetSetVmCpu;

function SX_GetSetRam(v) {
  if (v != null) {
    if (v != this._ram) {
      this._ram = v;
      sxRamObsrvr.exec(this, this._ram);
    }
  }
  return this._ram;
}
SX.prototype.ram = SX_GetSetRam;

function SX_GetSetVmRam(v) {
  if (v != null) {
    if (v != this._vmRam) {
      this._vmRam = v;
      sxVmRamObsrvr.exec(this, this._vmRam);
    }
  }
  return this._vmRam;
}
SX.prototype.vmRam = SX_GetSetVmRam;

function SX_GetSetUptime(v) {
  if (v != null) {
    if (v != this._up) {
      this._up = v;
      sxUptimeObsrvr.exec(this, this._up);
    }
  }
  return this._up;
}
SX.prototype.up = SX_GetSetUptime;

function SX_GetSetVmAutostartDefs(k, v) {
  if (v != null) {
    // Create the option if it doesn't already exist.
    if (this._vmAutoDflts[k] == null || this._vmAutoDflts[k] != v) {
      this._vmAutoDflts[k] = v;
      sxVmAutoDfltsObsrvr.exec(this, k);
    }
  }
  return this._vmAutoDflts[k];
}
SX.prototype.vmAutoDflts = SX_GetSetVmAutostartDefs;





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

function adjustSize() {
  if (data == null) return;
  // data.dim(self.dim());

  if (tabs.win().ready == null || ! tabs.win().ready() ||
    page.win().ready == null || ! page.win().ready()) {
    setTimeout("adjustSize();", 50);
    return;
  }
  var th = tabs.win().dh();

  tabs.dim(self.dim().w, th);
  tabs.pos(0, 0);
  page.dim(self.dim().w, self.dim().h - tabs.dim().h);
  page.pos(0, tabs.dim().h);
}


function initTabs() {
  var q = new Query(self.location.search);

  var esxTbs = {
    monitor: tabs.win().obj("monitor"),
    sxOptions: tabs.win().obj("sxOptions")
  };
  var gsxTbs = {
    monitor: tabs.win().obj("monitor"),
    sxOptions: tabs.win().obj("sxOptions")
  };
  var tbs = sx._prodId == "gsx"	? gsxTbs : esxTbs;
  var slct = tbs["monitor"];
  currTab = "monitor";
  helpurl = "/vmware/en/vserver/sxMonitorHelp.html";

  getTab = function (t) {
    return tbs[t];
  };

  tab = function (e) {
    e = eObj(e);
    slctTab(getTrgt(e));
    e.stp();
  };

  slctTab = function (t) {
    if (slct != t && t != null) {
      if (tle.d != null) tle.d();

      currTab = t.att("id");

      if (currTab == "sxOptions" && ! admin) {
        alert(user + "@" + sx._name +
          " is not authorized to modify host configuration options.");
        return;
      }

      switch (currTab) {
        case "monitor":
          helpurl = "/vmware/en/vserver/sxMonitorHelp.html";
          break;
        case "sxOptions":
          helpurl = "/vmware/en/vserver/sxOptionsHelp.html";
          break;
        case "memory":
          helpurl = "/vmware/en/vserver/sxMemoryHelp.html";
          break;
        case "report":
          helpurl = "/vmware/en/vserver/sxHostStatusHelp.html";
          break;
        default:
          break;
      }

      // Where window[i] is equivalent to window.summary, window.eventlog, etc.
      if (window[currTab] == null) {
        window[currTab] = obj(currTab);

        var u;

        switch (currTab) {
          case "monitor":
            u = "/vmware/en/sxMonitor.html";
            break;
          case "sxOptions":
            if (sx._prodId == "gsx") {
              u = "/vmware/en/sxOptionsGSX.html";
            } else {
              u = "/vmware/en/sxOptions.html";
            }
            break;
          case "memory":
            u = "/stats/memory?mui=new";
            break;
          case "report":
            u = "/hstatus?mui=new";
            break;
          case "memory":
            u = "/stats/memory?mui=new";
            break;
          case "report":
            u = "/hstatus?mui=new";
            break;
          default:
            break;
        }
        window[currTab].doc().location.replace(u);
      }

      if (page.win().doDn != null) page.win().doDn();

      window[currTab].pos(0, window[currTab].pos().y, 1);
      page.pos(-99999, page.pos().y, -1);
      page = window[currTab];

      Style.set(slct, Style.NRML);
      Style.set(t, Style.SLCT);

      slct = t;
    }

    initTab();
  };

  var showTabs = q.arg("tabs");

  for (var t in tbs) {
    if (showTabs != null && showTabs.length > 0) {
      var re = new RegExp("^" + t + "$", "i");
      if (grep(showTabs, re).length == 0) {
        tbs[t].css("display", "none");
        continue;
      }
    }

    tbs[t].ctrl = "tab";
    tabs.win().lstn(tbs[t], "click", tab);
  }

  var slctd = q.arg("slctTab")[0] || (showTabs != null ? showTabs[0] : null);
  if (slctd && (showTabs.length == 0 ||
    grep(showTabs, new RegExp("^" + slctd + "$", "i")).length > 0)) {
    slctTab(tbs[slctd]);
  }
}

function initTab() {
  if (page.win().ready == null || ! page.win().ready()) {
    setTimeout("initTab();", 50);
    return;
  }

  if (page.win().doUp != null) page.win().doUp();
  adjustSize();
}


var sslrd = false;

function sslRedirect() {
  if (sslrd) return;
  sslrd = true;

  // <protocol>//<hostname>[:<port>]/<pathname>
  var loc = main.document.location;
  var u, m;
  m = "Secure Sockets Layer (SSL) options have changed. Redirecting to ";
  n = "\n\nNote: You will need to log in again.";

  if (loc.protocol.indexOf("https") == 0) {
    u = "http://" + loc.hostname + (sx._prodId == "gsx" ? ":8222" : "");
    m += "unencrypted";
  } else {
    u = "https://" + loc.hostname + (sx._prodId == "gsx" ? ":8333" : "");
    m += "encrypted";
  }
  u += "/vmware/en/";
  m += " Management Interface:\n\n\t" + u + n;
  alert(m);
  main.document.location.replace(u);
  return true;
}

function hasSslChanged() {
  try {
    // IIS serves up an error page rather than automatically redirecting to "a
    // secure channel."
    var s = data.doc().title;
    if (s.indexOf("a secure channel") > -1) {
      return sslRedirect();
    }

    // But we still might trigger an access violation.
    s = page.doc().title;
  } catch (ex) {
    if (ie) {
      if (ie5_5) {
        if (ex.number == -2146823281) {
          window.setTimeout("sslRedirect();", 9000);
          return true;
        }
      } else {
        if (ex.number == -2146828218) {
          window.setTimeout("sslRedirect();", 9000);
          return true;
        }
      }
    } else {
      if (ex.toLowerCase().indexOf("permission denied") == 0)
        return sslRedirect();
    }
  }
  return false;
}


// --------------------------------------------------------------------------
//
// loginCb --
//
//      When the login data loads, it calls parent.loginCb.
//
// --------------------------------------------------------------------------

var ehcwWindow;
function esxHostCfgWizardWindow(u) {
  var n = escJs(document.domain +
    ":VMwareEsxServerHostConfigurationWizard");

  if (ehcwWindow == null || ehcwWindow.closed) {
    ehcwWindow = window.open("", "", "width=760,height=536,resizable");
    ehcwWindow.name = n;
    if (ehcwWindow.location.href == "about:blank" ||
      ehcwWindow.location.href == "") {
      ehcwWindow.location.replace(u);
    }
  }
  ehcwWindow.focus();
}


function loginCb(w) {
  clearTimeout(wto);
  clearInterval(bto);

  try { closeAll(); } catch(err) { ; }

  // Reset the server (and by extension, the vm) list. If this step is
  // omitted, vms removed from vm-list will not be removed in the MUI.
  try {
    servers = new Array();
    wipe();
  } catch (err) {
    ;
  }

  cb = initCB;

  try {
    if (w.next) {
      esxHostCfgWizardWindow(w.next);
    } else {
      auth.pos(0, 0);
      auth.win().setup(w.user, w.str);
    }
  } catch(ex) {
    ;
  }
}


// --------------------------------------------------------------------------
//
// unframe --
//
//      When the login page loads, it calls parent.unframe. This is a cheap
//      way to handle the login functionality for now.
//
// --------------------------------------------------------------------------

function unframe() {
  clearTimeout(wto);
  clearInterval(bto);

  try { closeAll(); } catch(err) { ; }

  // Reset the server (and by extension, the vm) list. If this step is
  // omitted, vms removed from vm-list will not be removed in the MUI.
  try {
    servers = new Array();
    page.win().ready(false);
    page.doc().location.replace("/vmware/blank.html");
  } catch (err) {
    ;
  }

  cb = initCB;

  try {
    data.pos(0, 0);
    data.doc().u.v.focus();
  } catch(err) {
    ;
  }
}


// --------------------------------------------------------------------------
//
// logout --
//
// --------------------------------------------------------------------------

function logout(s, f) {
  s = s == null ? "Logging out will end your session, closing any open " +
    "management windows.\n\nAre you sure you want to log out?" : s;
  // XXX i18n

  var logout = true;
  if (s != "") {
    f ? alert(s) : (logout = confirm(s));
  }

  if (logout) {
    clearTimeout(wto);
    clearInterval(bto);

    data.doc().location.replace("/sx-logout/index.pl");

    return true;
  }
  
  return false;
}

function logoutCb() {
  wipe();
  self.location.reload();
}

// --------------------------------------------------------------------------
//
// getUpdates --
//
//      Grab data changes from the server.
//
// --------------------------------------------------------------------------

function getUpdates(f) {
  if ((f == null || ! f) && updatePending) return false;

  var u;
  // extra info bit: 1 -> VM Overview, 2 -> Hardware, 4 -> Users & Events
  var extra = "";

  // Accumulate extra bits for all VMs.
  var allVms = 0;

  for (var n in sx.vms) {
    if (sx.vms[n].extra() > 0) {
      extra += "&" + n + "=" + sx.vms[n].extra();
      allVms = allVms | sx.vms[n].extra();
    }
  }

  var u = "/monitor/index.pl" +
    "?auto-refresh=" + (idle ? "1" : "0") +
    "&allVms=" + allVms + extra;

  data.doc().location.replace(u);


  updatePending = true;
  clearTimeout(wto);
  wto = null;
  idle = idle ? idle : new Date().getTime();

  if (rtry == 0)
    preUpdateObsrvr.exec();

  return true;
}

// --------------------------------------------------------------------------
//
// initCB --
//
//      Initialize callback handler.
//
// --------------------------------------------------------------------------

function initCB(w) {
  if (ehcwWindow != null && ! ehcwWindow.closed) {
    ehcwWindow.focus();
    return;
  }
  var vms;

  user = w.user;
  admin = w.admin;
  canAddVm = w.canAddVm;
  interval = w.interval;
  period = w.period;

  // XXX this will break when we support multiple servers.
  if (servers.length == 0) {
    // a ... Hostname
    // b ... Product ID
    // c ... Product name
    // d ... CPU info
    // e ... Memory info
    // f ... Total system CPU usage
    // g ... VM CPU usage
    // h ... Total system RAM usage
    // i ... VM RAM usage
    // j ... Host operating system
    // k ... Host uptime
    // l ... Number of NUMA nodes
    // m ... License info
    // n ... hyperThreading supported
    // o ... hyperThreading enabled/disabled
    // p ... collection of vms autostart and autostop defaults
    //       - autostart
    //       - autostartDelay
    //       - autostartContOnTools
    //       - autostopDelay
    //       - autostopPwrOffBeforeCont
    // q ... List of supported guest OSes
    // r ... Agent ID (vmserverd or ccagent)

    servers.push(new SX(w.sx.a, w.sx.b, w.sx.c, w.sx.d, w.sx.e, w.sx.f, w.sx.g,
      w.sx.h, w.sx.i, w.sx.j, w.sx.k, w.sx.l, w.sx.m, w.sx.n, w.sx.o, w.sx.p,
      w.sx.q, w.sx.r));
    sx = servers[0];
  }

  function initVms() {
    if (page.win().addVm == null) {
      setTimeout(initVms, 50);
      return;
    }

    if (vms != null) {
      var h;

      for (h in vms) {
        break;
      }

      if (h != null) {
        var vm = vms[h];

        // a ... Config path
        // b ... Display name
        // c ... Execution state (poweredOn, poweredOff or suspended)
        // d ... Heartbeat
        // v ... # of CPUs allocated to VM
        // e ... CPU usage
        // f ... RAM usage
        // g ... Guest operating system
        // h ... Up time
        // i ... Guest state (unknown, running, halting, rebooting or
        //       halting/rebooting)
        // j ... Tools ready (i.e. available, soft power capable and not
        //       currently executing a soft power op)
        // k ... VMX process ID
        // w ... VMX world ID
        // l ... Effective permissions (mode)
        // m ... Waiting for input
        // n ... Attached to local console
        // hwv ... Virtual hardware version
        // usr ... User to run VM as (empty string for interactive user)
        new VM(h, vm.a, vm.b, vm.c, vm.d, vm.v, vm.e, vm.f, vm.g, vm.h,
               vm.i, vm.j, vm.k, vm.w, vm.l, vm.m, vm.n, vm.hwv, vm.usr);

        delete vms[h];

        setTimeout(initVms, ie ? 5 : 40);
      } else {
        updateObsrvr.exec();
        updatePending = false;
        wto = setTimeout("getUpdates();", interval);
        if (bto == null) {
          bto = setInterval("aux();", 3000);
        }
      }
    }
    return true;
  }

  // delta processes updates that appear in window w.
  function delta(w) {
    sx.cpu(w.sx.f);
    sx.vmCpu(w.sx.g);
    sx.ram(w.sx.h);
    sx.vmRam(w.sx.i);
    sx.up(w.sx.k);

    interval = w.interval;
    if (period != w.period &&
      (w.period == 1 || w.period == 5 || w.period == 15)) {
      period = w.period;
      sxPeriodObsrvr.exec(this, period);
    }

    // Remove unregistered VMs.
    for (var h in sx.vms) {
      if (w.vms[h] == null) {
        sxRemVmObsrvr.exec(sx.vms[h], h);
        delete sx.vms[h];
      }
    }

    for (var h in w.vms) {
      var vm = w.vms[h];
      if (sx.vms[h]) {
        // Reset wait-for-input first.
        sx.vms[h].wfi(vm.m);

        // Then reset local console.
        sx.vms[h].lc(vm.n);

        sx.vms[h].dn(vm.b);
        sx.vms[h].es(vm.c);
        sx.vms[h].hb(vm.d);
        sx.vms[h].vcpu(vm.v);
        sx.vms[h].cpu(vm.e);
        sx.vms[h].ram(vm.f);
        sx.vms[h].os(vm.g);
        sx.vms[h].up(vm.h);
        sx.vms[h].gs(vm.i);
        sx.vms[h].tr(vm.j);
        sx.vms[h].pid(vm.k);
        sx.vms[h].wid(vm.w);
        sx.vms[h].mode(vm.l);
        sx.vms[h].hwv(vm.hwv);
        sx.vms[h].usr(vm.usr);

        if (vm.x != null) {
          sx.vms[h].maxCpu(vm.x.a);
          sx.vms[h].minCpu(vm.x.b);
          sx.vms[h].maxRam(vm.x.c);
          sx.vms[h].minRam(vm.x.d);
          sx.vms[h].memSize(vm.x.e);
          sx.vms[h].ip(vm.x.f);
        }

        // Devices
        if (vm.y != null) {
          for (var d in sx.vms[h]._dev) {
            if (vm.y.dev[d] == null) {
              vmRemDevObsrvr.exec(sx.vms[h], sx.vms[h]._dev[d]);
              delete sx.vms[h]._dev[d];
            }
          }

          for (var d in vm.y.dev) {
            sx.vms[h].dev(d, vm.y.dev[d]);
          }
        }

        // Options
        if (vm.y != null) {
          // for (var k in sx.vms[h]._opt) {
          //   if (vm.y.opt[k] == null) {
          //     vmRemOptObsrvr.exec(sx.vms[h], k);
          //     delete sx.vms[h]._opt[k];
          //   }
          // }

          for (var k in vm.y.opt) {
            sx.vms[h].opt(k, vm.y.opt[k]);
          }
        }

        // Users and events
        if (vm.z != null) {
          for (var i = 0; i < vm.z.a.length; i++) {
            sx.vms[h].evt(i, vm.z.a[i]);
          }

          // Find current connections.
          var crntCons = {};
          for (var i = 0; vm.z.b != null && i < vm.z.b.length; i++) {
            // XXX: .b is the time the connection was established in
            // microseconds, which should be granular enough to avoid most
            // races but is not bulletproof.
            crntCons[vm.z.b[i].b] = true;
          }

          // Remove stale connections.
          for (var n in sx.vms[h]._con) {
            if (crntCons[n] == null) {
              vmRemConObsrvr.exec(sx.vms[h], sx.vms[h]._con[n]);
              delete sx.vms[h]._con[n];
            }
          }

          // Refresh (or add to) current connections.
          for (var i = 0; vm.z.b != null && i < vm.z.b.length; i++) {
            sx.vms[h].con(vm.z.b[i].b, vm.z.b[i]);
          }
        }

        // Resources
        if (vm.r != null) {
          for (var i in vm.r.a) {
            sx.vms[h].rsrcCpu(i, vm.r.a[i]);
          }
          for (var i in vm.r.b) {
            sx.vms[h].rsrcMem(i, vm.r.b[i]);
          }

          // Remove stale disk resource data.
          for (var d in sx.vms[h]._rsrcDsk) {
            if (vm.r.c[d] == null) {
              vmRemRsrcDskObsrvr.exec(sx.vms[h], sx.vms[h]._rsrcDsk[d]);
              delete sx.vms[h]._rsrcDsk[d];
            }
          }

          for (var i in vm.r.c) {
            sx.vms[h].rsrcDsk(i, vm.r.c[i]);
          }
          for (var i in vm.r.d) {
            sx.vms[h].rsrcNet(i, vm.r.d[i]);
          }
        }

      } else {
        new VM(h, vm.a, vm.b, vm.c, vm.d, vm.v, vm.e, vm.f, vm.g, vm.h,
               vm.i, vm.j, vm.k, vm.w, vm.l, vm.m, vm.n, vm.hwv, vm.usr);
      }
    }
  }

  if (tabs.win().ready == null || ! tabs.win().ready()) {
     if (sx._prodId == "gsx") {
        tabs.doc().location.replace("/vmware/en/gsx-sxTabs.html");
     } else {
        tabs.doc().location.replace("/vmware/en/sxTabs.html");
     }
  }
  if (page.win().ready == null || ! page.win().ready())
    page.doc().location.replace("/vmware/en/sxMonitor.html");

  adjustSize();

  cb = function(w) {
    delta(w);
    clearTimeout(wto);
    updateObsrvr.exec();
    updatePending = false;
    wto = setTimeout("getUpdates();", interval);
    return true;
  };

  opCb = function(w) {
    var vm = sx.vms[w.data.vmId];

    if (w.wrn.length > 0) {
      var s = vm.dn() + ": One or more warnings were generated:";
      for (var i in w.wrn) {
	s += "\n\n" + w.wrn[i];
      }
      alert(s);

      // XXX: Do not cancel the pending operation. If the message is truly a
      // warning, then the operation will still succeed thereby changing the
      // virtual machine's state and notifying listeners.
    }

    if (w.err.length > 0) {
      var s = vm.dn() + ": One or more errors occurred:";
      for (var i in w.err) {
	s += "\n\n" + w.err[i];
      }
      alert(s);

      // Cancel the pending operation thereby notifying listeners of the
      // virtual machine's state prior to the operation.
      vm.op(false);
    }

    getUpdates();
  }

  getVmList = function (/*sx*/) {
    var a = new Array();
    if (sx.vms == null) return a;
    for (var vm in sx.vms) {
      a.push(sx.vms[vm]);
    }
    return a;
  };


  auth.pos(-99999, -99999);
  if (w.vms != null) {
    vms = w.vms;
    initVms();
  } else {
    updateObsrvr.exec();
    updatePending = false;
    wto = setTimeout("getUpdates();", 500);
    bto = setInterval("aux();", 3000);
  }
}


// --------------------------------------------------------------------------
//
// Obsrvr --
//
//      Obsrvr object constructor.
//
// --------------------------------------------------------------------------

function Obsrvr(t) {
  this.t = t; // where t is the type of update.
  this.m = new Array(); // where m is a list of registered methods.

  // Register an update listener given a key k, a method m and an optional
  // target object o. If o is specified, the listener will only be notified
  // when o is updated.
  this.lstn = function (k, m, o) {
    this.m[k] = {m: m, o: o};
  };

  // Unregister an update listener given a key k and a method m.
  this.ignr = function (k) {
    delete this.m[k];
  };

  // Iterate through the listeners, executing the registered methods with
  // arguments for the update type, calling context (object) and updated value.
  this.exec = function (o, v) {
    for (var k in this.m) {
      if (this.m[k] != null && (this.m[k].o == null || this.m[k].o == o)) {
        try {
          this.m[k].m(this.t, o, v);
        } catch (err) {
          this.ignr(k);
        }
      }
    }
  };
}


function initMuiWindow() {
  var vmWndws = [];
  var rgWndws = [];
  var busy = false;

  MuiWindow = function () {
    var u, p; // URL, properties

    // Only open one window at a time, to prevent a race if user double-clicks
    if (busy) {
      var argv = [];
      for (var i = 0; i < arguments.length; i++) {
        argv.push('"' + arguments[i].replace('"', '\\"') + '"');
      }
      window.setTimeout("MuiWindow(" + argv.join() + ")", 500);
      return;
    }

    busy = true;
    switch (arguments[0])
    {
      case "details": // Open a virtual machine details window
        var h = arguments[1];
        var t = arguments[2];

        // Set i according to the number of existing MUI windows.
        // Use i as the x and y screen coordinates for new windows.
        var i = 1;
        for (var n in vmWndws) {
          if (vmWndws[n] && ! vmWndws[n].closed) i++;
        }
        i = i * 8;

        if (! vmWndws[h] || vmWndws[h].closed) {
          if (sx._prodId == "gsx") {
             u = "/vmware/en/gsx-vmProperties.html?h=" + h + "&n=" + escape(sx.vms[h].dn());
          } else {
             u = "/vmware/en/vmProperties.html?h=" + h + "&n=" + escape(sx.vms[h].dn());
          }
          if (t) u += "&tab=" + t;

          p = "width=730,height=536";

          if (ie) {
            p += ",top=" + i + ",left=" + i;
          } else {
            p += ",screenX=" + i + ",screenY=" + i;
          }

          vmWndws[h] = self.open("", "", p);
          vmWndws[h].name = "vm_" + h;
          vmWndws[h].document.location.replace(u);
        } else {
          if (t) { vmWndws[h].slctTab(vmWndws[h].getTab(t)); }
        }
        vmWndws[h].focus();
        break;
      default:
        break;
    }
    busy = false;
  }

  addRgWndw = function (w) {
    rgWndws[w.name] = w;
  };

  getRgWndw = function (n) {
    if (rgWndws[n] && ! rgWndws[n].closed)
      return rgWndws[n];
    return null;
  };

  closeAll = function () {
    for (var h in vmWndws) {
      if (vmWndws[h] && ! vmWndws[h].closed) {
        vmWndws[h].close();
        delete vmWndws[h];
      }
    }

    for (var n in rgWndws) {
      if (rgWndws[n] && ! rgWndws[n].closed) {
        rgWndws[n].close();
        delete vmWndws[n];
      }
    }
  };
  // Open windows are useless if the user decides to move to another page,
  // close this window or reload.
  lstn(self, "unload", closeAll);
}


// --------------------------------------------------------------------------
//
// initTryToLoadMonitor --
//
//      Load "/monitor/index.pl" into the data frame, if possible. Try to do
//      this once every half a second. After ten tries (c. 5 seconds), give up
//      and reload the main window.
//
//      XXX: Corrects the symptoms of PR 31117. But the root cause is still not
//      clear to me. --mikol September 4, 2003 5:54 PM
//
// --------------------------------------------------------------------------

function initTryToLoadMonitor() {
  var tries = 0;
  var limit = 10;

  tryToLoadMonitor = function () {
    try {
      if (tries < limit) {
        data.doc().location.replace("/monitor/index.pl");
        tries = 0;
      } else {
        self.document.location.replace(self.document.location.href);
      }
    } catch (ex) {
      tries++;
      window.setTimeout("tryToLoadMonitor();", 500);
    }
  }

  tryToLoadMonitor();
}


// --------------------------------------------------------------------------
//
// init --
//
//      Load data and initialize page.
//
// --------------------------------------------------------------------------

function initPage() {
  self.name = "VMwareManagementInterface";
  document.title = document.domain + ": VMware Management Interface";

  initMuiWindow();

  sxPeriodObsrvr = new Obsrvr("sxPeriod");
  sxAddVmObsrvr = new Obsrvr("sxAddVm");
  sxRemVmObsrvr = new Obsrvr("sxRemVm");
  sxCpuObsrvr = new Obsrvr("sxCpu");
  sxVmCpuObsrvr = new Obsrvr("sxVmCpu");
  sxRamObsrvr = new Obsrvr("sxVmRam");
  sxVmRamObsrvr = new Obsrvr("sxRam");
  sxUptimeObsrvr = new Obsrvr("sxUptime");
  sxPathCtxMenuObsrvr = new Obsrvr("sxPathCtxMenu");
  sxVmAutoDfltsObsrvr = new Obsrvr("sxVmAutoDflts");

  vmCfgObsrvr = new Obsrvr("vmCfg");
  vmDisplayNameObsrvr = new Obsrvr("vmDisplayName");
  vmHbObsrvr = new Obsrvr("vmHb");
  vmVcpuObsrvr = new Obsrvr("vmVcpu");
  vmCpuObsrvr = new Obsrvr("vmCpu");
  vmRamObsrvr = new Obsrvr("vmRam");
  vmExecutionStateObsrvr = new Obsrvr("vmExecutionState");
  vmUptimeObsrvr = new Obsrvr("vmUptime");
  vmGuestOsObsrvr = new Obsrvr("vmGuestOs");
  vmGuestStateObsrvr = new Obsrvr("vmGuestState");
  vmToolsReadyObsrvr = new Obsrvr("vmToolsReady");
  vmPidObsrvr = new Obsrvr("vmPid");
  vmWidObsrvr = new Obsrvr("vmWid");
  vmModeObsrvr = new Obsrvr("vmMode");
  vmMaxCpuObsrvr = new Obsrvr("vmMaxCpu");
  vmMinCpuObsrvr = new Obsrvr("vmMinCpu");
  vmMaxRamObsrvr = new Obsrvr("vmMaxRam");
  vmMinRamObsrvr = new Obsrvr("vmMinRam");
  vmMemSizeObsrvr = new Obsrvr("vmMemSize");
  vmIpObsrvr = new Obsrvr("vmIp");
  vmDevObsrvr = new Obsrvr("vmDev");
  vmRemDevObsrvr = new Obsrvr("vmRemDev");
  vmOptObsrvr = new Obsrvr("vmOpt");
  vmOptAutoStartObsrvr = new Obsrvr("vmAutoStart");
  vmEvtObsrvr = new Obsrvr("vmEvt");
  vmConObsrvr = new Obsrvr("vmCon");
  vmRemConObsrvr = new Obsrvr("vmRemCon");
  vmRsrcCpuObsrvr = new Obsrvr("vmRsrcCpu");
  vmRsrcMemObsrvr = new Obsrvr("vmRsrcMem");
  vmRemRsrcDskObsrvr = new Obsrvr("vmRemRsrcDsk");
  vmRsrcDskObsrvr = new Obsrvr("vmRsrcDsk");
  vmRsrcNetObsrvr = new Obsrvr("vmRsrcNet");
  vmWfiObsrvr = new Obsrvr("vmWfi");
  vmLcObsrvr = new Obsrvr("vmLc");
  vmHwvObsrvr = new Obsrvr("vmHwv");
  vmUsrObsrvr = new Obsrvr("vmUsr");

  retryObsrvr = new Obsrvr("retry");
  preUpdateObsrvr = new Obsrvr("preUpdate");
  updateObsrvr = new Obsrvr("update");

  updateObsrvr.lstn("main",
    function(){if(rtry > 0){rtry=0;clearInterval(bto);bto=setInterval("aux();",3000);}lmod=new Date().getTime();idle=idle?idle:new Date().getTime();window.defaultStatus=(idle?"Idle since "+new Date(idle).toString():"Active");});

  auth = obj("auth");
  tabs = obj("tabs");
  page = monitor = obj("monitor");
  data = obj("data");
  op = obj("op");

  wipe = function () {
    try {
      if (ie) {
        ignr(monitor, "unload", parent.document.location.reload);
      } else {
        ignr(monitor, "unload", main.getUpdates);
      }

      data.doc().location.replace("/vmware/blank.html");

      if (tabs.win().ready != null) {
        tabs.win().ready(false);
        tabs.doc().location.replace("/vmware/blank.html");
      }

      if (monitor.win().ready != null) {
        monitor.win().ready(false);
        monitor.doc().location.replace("/vmware/blank.html");
        page = monitor;
        monitor.pos(0, monitor.pos().y, 1);
      }

      if (sxOptions.win().ready != null) {
        sxOptions.win().ready(false);
        sxOptions.doc().location.replace("/vmware/blank.html");
        sxOptions.pos(-99999, sxOptions.pos().y, -1);
        sxOptions = null;
      }
    } catch (err) {
      ; // alert(err);
    }
  }
  lstn(self, "unload", wipe);
  lstn(self, "resize", adjustSize);
  lstn(self, "resize", function () {if (auth.dim) { auth.dim(self.dim()); }} );

  data.dim(self.dim());
  vmCtrlBar = obj("vmCtrlBar");

  cb = initCB;

  initTryToLoadMonitor();
}

openNewMuiTab = function (w, cfg, tab) {
   for (var h in sx.vms) {
      if(cfg == sx.vms[h].cfg()) {
         MuiWindow(w, h, tab);
         break;
      }
   }
}


getVmList = function () { return new Array(); };

