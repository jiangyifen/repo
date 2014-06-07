/* Copyright 1998 VMware, Inc.  All rights reserved. -- VMware Confidential */

var ctx = "editor";

var tabs, page, toc, btns, op, q, vm, helpurl;

var tle = new Object();


function help() {
  if (helpurl == null | ! helpurl) { return; }

  var w = window.open("", "",
    "location,menubar,resizable,scrollbars,status,toolbar,width=730,height=487");
  w.document.location.href = helpurl;
  w.name = "vmware_mui_help";
  w.focus();
  main.addRgWndw(w);
}


function opCb(w) {
  if (page != null && page.win().opCb) page.win().opCb(w);
}


function loginCb(w) {
  if (main != null && main.loginCb != null) {
    main.loginCb(w);
  }
}


var MW = 0; // Minimum width for this window
function layoutCb() {
  if (! tabs || ! page || ! btns) return;

  if (page.win().ready == null || ! page.win().ready()) {
    setTimeout("layoutCb();", 50);
    return;
  }

  var ch = 64; // Pessimistic guess for height chrome will add to a window.

  // XXX: This try clause quiets a seemingly erroneous error message. Mozilla
  // (Gecko/20030428 Mozilla Firebird/0.6) perdiodically posts an error that
  // page.win().dh is not a function. The dialog still loads successfully; MSIE
  // 6.0 does not report any errors AFAICT. --mikol April 30, 2003 4:20 PM
  var tabsH = tabs.win().dh();
  var btnsH = btns.win().dh ? btns.win().dh() : 0;
  var pageH;
  try {
    pageH = page.win().dh();
  } catch (ex) {
    ;
  }

  // Size the window to fit the tabs, status and page content, if possible.
  if (pageH + tabsH + btnsH < screen.availHeight - ch) {
    self.dim(self.dim().w, pageH + tabsH + btnsH);
  } else {
    self.dim(self.dim().w, screen.availHeight - ch);
  }

  tabs.dim(self.dim().w, tabsH);
  btns.dim(self.dim().w, btnsH);

  // Size each frame to fit.
  resize();

  // Stack the page and btns frames under the tabs frame.
  page.pos(0, tabs.dim().h);
  btns.pos(0, self.dim().h - btnsH);
}


function initResize() {
  var SZ = false;

  resize = function (e) {
    if (SZ) {
      SZ = false;
      return;
    }

    if (! tabs || ! page || ! btns) return;

    // Determine minimum width if it has not yet been set.
    if (MW == 0) {
      if (MW < 560) MW = 560 < screen.availWidth ? 560 : screen.availWidth - 32;
    }

    // Set the window width to accomodate the tabs and page content comfortably.
    if (MW > self.dim().w) {
      SZ = true;
      self.dim(MW, self.dim().h);
      tabs.dim(MW, tabs.win().dh());
      btns.dim(MW, btns.win().dh());
    }

    page.dim(self.dim().w, self.dim().h - tabs.dim().h - btns.dim().h);
  };
}
initResize();


function initTglBtns() {
  // If boolean s is true, show the buttons. If boolean s is false, hide them.
  tglBtns = function (s) {
    if ((s == null && btns.css("display") == "none") || (s != null && s)) {
      btns.dim(self.dim().w, 1);
      btns.css("display", "");
    } else if ((s == null && btns.css("display") != "none") || (s != null && !s)) {
      btns.dim(self.dim().w, btns.win().dh ? btns.win().dh() : 0);
      btns.css("display", "none");
    }
  };
}
initTglBtns();


function loadPage() {
  var doc = obj("bodyObj", page.win());
  var u = q.arg("unit")[0];
  var l = page.win().location;
 

  if (u.match(/^sxAddVirtualMachine$/)) {
    l.replace("sxAddVirtualMachine.html");
    helpurl = "vserver/sxAddVirtualMachineHelp.html";
    return;
  }

  if (u.match(/^sxRegisterVirtualMachine$/)) {
    l.replace("sxRegisterVm.html");
    helpurl = "vserver/sxRegisterVmHelp.html";
    return;
  }

  if (u.match(/^vmAddDeviceWizard$/)) {
    l.replace("vmAddDeviceWizard.html");
    return;
  }

  if (u.match(/^vmOptions$/)) {
    l.replace("vmOptionsForm.html");
    helpurl = "vserver/vmOptionsFormHelp.html";
    return;
  }

  if (u.match(/^vmOptionsStartup$/)) {
    l.replace("vmOptionsStartupForm.html");
    helpurl = "vserver/vmOptionsStartupFormHelp.html";
    return;
  }

  if (u.match(/^vmOptionsRaw$/)) {
    l.replace("vmVerboseOptionsForm.html");
    helpurl = "vserver/vmVerboseOptionsFormHelp.html";
    return;
  }

  if (u.match(/^floppy/i)) {
      l.replace("vmFloppy.html");
      helpurl = "vserver/vmFloppyHelp.html";
      return;
  }

  if (u.match(/^parallel/i)) {
      l.replace("vmParallel.html");
      helpurl = "vserver/vmParallelHelp.html";
      return;
  }

  if (u.match(/^serial/i)) {
      l.replace("vmSerial.html");
      helpurl = "vserver/vmSerialHelp.html";
      return;
  }

  if (u.match(/^usb/i)) {
      l.replace("vmUsb.html");
      helpurl = "vserver/vmUsbHelp.html";
      return;
  }

  if (u.match(/^cdrom/i)) {
    l.replace("vmDvdCdRom.html");
    helpurl = "vserver/vmDvdCdRomHelp.html";
    return;
  }

  if (u.match(/^video/i)) {
    l.replace("vmDisplay.html");
    helpurl = "vserver/vmDisplayHelp.html";
    return;
  }

  if (u.match(/^nic/i)) {
    l.replace(main.sx._prodId == "gsx" ? "vmNicGSX.html" : "vmNic.html");
    helpurl = "vserver/vmNicHelp.html";
    return;
  }

  if (u.match(/^disk/i)) {
      l.replace(main.sx._prodId == "gsx" ? "vmVirtualDiskGSX.html" : "vmVirtualDisk.html");
      helpurl = "vserver/vmVirtualDiskHelp.html";
    return;
  }

  if (u.match(/^memory/i)) {
    l.replace("vmProcessorsAndMemory.html");
    helpurl = "vserver/vmProcessorsAndMemoryHelp.html";
    return;
  }

  if (u.match(/^scsiCtlr/i)) {
    l.replace("vmScsiController.html");
    helpurl = "vserver/vmScsiControllerHelp.html";
    return;
  }

  if (u.match(/^passthru/i)) {
    l.replace(main.sx._prodId == "gsx" ? "vmGenericScsiFormGSX.html" : "vmGenericScsiForm.html");
    helpurl = "vserver/vmGenericScsiFormHelp.html";
    return;
  }

  if (u.match(/^rsrc/i)) {
    var dev = q.arg("dev")[0];
    if (dev.match(/^cpu/i)) {
      l.replace("vmRsrcCpu.html");
      helpurl = (vm.hash() == "console") ? "esx/scRsrcHelp.html" : "esx/vmRsrcCpuHelp.html";
      return;
    } else if (dev.match(/^mem/i)) {
      l.replace("vmRsrcMemory.html");
      helpurl = "esx/vmRsrcMemoryHelp.html";
      return;
    } else if (dev.match(/^dsk/i)) {
      l.replace("vmRsrcDisk.html");
      helpurl = (vm.hash() == "console") ? "esx/scRsrcHelp.html" : "esx/vmRsrcDiskHelp.html";
      return;
    } else if (dev.match(/^net/i)) {
      l.replace("vmRsrcNetwork.html");
      helpurl = "esx/vmRsrcNetworkHelp.html";
      return;
    }
  }

  doc.innerHTML = "Not Implemented";
  layoutCb = function () {
    var tabsH = tabs.win().dh();
    var btnsH = btns.win().dh();

    tabs.dim(self.dim().w, tabsH);
    btns.dim(self.dim().w, btnsH);

    page.dim(self.dim().w, self.dim().h - (tabsH + btnsH));
    page.pos(0, tabs.dim().h);

    btns.pos(0, self.dim().h - btnsH);
  };
  layoutCb();
}


function initPage() {
  q = new Query(location.search);
  vm = main.sx.vms[q.arg("vmid")[0]];
  if (vm != null) {
    document.title = document.domain + ": " + vm.dn() + " | Configuration";
  } else {
    document.title = document.domain + ": Add Virtual Machine";
  }

  tabs = obj("tabs");
  page = obj("page");
  btns = obj("btns");
  op = obj("op");

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

  loadPage();
  lstn(self, "resize", layoutCb);
}
