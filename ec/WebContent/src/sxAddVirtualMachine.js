/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// Original query, edited query
var oq;
var ok = false;
var obsrvrStr;
var hash;

// --------------------------------------------------------------------------

function exit(i) {
  if (i == 1) {
    parent.close();
  }
}


// --------------------------------------------------------------------------

function opCb(w) {
  if (w.wrn.length > 0) {
    var s = "One or more warnings were generated:";
    for (var i in w.wrn) {
      s += "\n\n" + w.wrn[i];
    }
    alert(s);
  }

  if (w.err.length > 0) {
    var s = "One or more errors occurred:";
    for (var i in w.err) {
      s += "\n\n" + w.err[i];
    }
    alert(s);
  }

  if (ok && w.err.length > 0) {
    ok = false;
    prev();
    return;
  }

  if (ok) {
    if (main && main.getUpdates) {
      hash = w.hash;
      if (main.sx.vms[hash]) {
        // The VM ended up in the list before we could listen for it.
        next();
        return;
      }
      main.sxAddVmObsrvr.lstn(obsrvrStr, tryNext);
      main.getUpdates();
    }
    return;
  }

  next();
}


// --------------------------------------------------------------------------

function initPage() {
  if (parent.ready == null || ! parent.ready()) { window.setTimeout(initPage, 50); return; }

  parent.slctBtns("wiz");

  var w = parent.op.win();
  var q = new Query(parent.location.search);
  var home, memInfo, guestInfo;

  obsrvrStr = parent.name;

  var dom = {};
  dom.subHdr = obj("subHdr");
  dom.prcRow = obj("prcRow");
  dom.wrnRow = obj("wrnRow");
  dom.wrnMsg = obj("wrnMsg");

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var stdOpts = new Step("stdOpts", obj("stdOpts"));
  var cfgOpts = new Step("cfgOpts", obj("cfgOpts"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));
  var svdMsg = new Step("svdMsg", obj("svdMsg"));
  var addDsk = new Step("addDsk", null);

  showPrcRow = function () {
      dom.wrnRow.css("display", "none");
      dom.prcRow.css("display", "");
  };

  function wrnMsg(os) {
    var msg = false;

    if (main.sx._prodId == "gsx") {
      dom.wrnMsg.innerHTML = "This product only permits virtual machines " +
        "to be configured with a single processor.";
      msg = true;
    } else if (main.sx._license.maxVcpuNum == 1) {
      dom.wrnMsg.innerHTML = "Your license only permits virtual machines " +
        "to be configured with a single processor.";
      msg = true;
    } else if (guestInfo.maxVcpuNum == 1) {
      dom.wrnMsg.innerHTML = "Virtual machines running " + hrGos(os) +
        " may only be configured with a single processor.";
      msg = true;
    } else if (main.sx._cpuInfo < 2) {
      dom.wrnMsg.innerHTML = "The current system has a single processor. " +
        "Virtual machines configured with more than one " +
        "processor will not run unless the underlying system has as many " +
        "processors as the virtual machine.";
      msg = true;
    }

    dom.wrnRow.css("display", msg ? "" : "none");
    dom.prcRow.css("display", msg ? "none" : "");
  }

  function defVal(k, g, l) {
    var sep = main.sx._os == "win32" ? "\\" : "/";
    switch (k) {
      case "name":
        if (oq.arg("name")[0] == "" || oq.arg("name")[0] == hrGos(l)) {
          oq.arg("name", hrGos(g));
          if (stdOpts.o.css("display") != "none") {
            oq.f.name.focus();
            oq.f.name.select();
          }
        }
        break;
      case "location":
        if ((home != "") &&
            (oq.arg("location")[0] == "" ||
             oq.arg("location")[0] == home + l + sep)) {
          var loc = home + g + sep;
          oq.arg("location", loc);
        }
        break;
      case "sizeMb":
        oq.arg("sizeMb", guestInfo.defSizeMb);
        break;
      case "num":
        if (oq.arg("num")[0] > guestInfo.maxVcpuNum) {
          oq.arg("num", guestInfo.maxVcpuNum);
        }
        break;
      case "scsiCtlr":
        if (main.sx._prodId == "gsx") {
          oq.arg("scsiCtlr", guestInfo.lsi ? "lsilogic" : "buslogic");
        } else {
          oq.arg("scsiCtlr", guestInfo.lsi ? "vmxlsilogic" : "vmxbuslogic");
        }
        break;
      default:
        break;
    }
  }

  fillForm = function () {
    // Make a new Query object to represent the original form.
    if (oq == null) {
      oq = new Query(document.forms[0]);

      oq.f.guestOS.length = 0;
      for (var i = 0; i < main.sx._guests.length; i++) {
        oq.f.guestOS.options[oq.f.guestOS.length] =
          new Option(main.sx._guests[i].name, main.sx._guests[i].key);
      }

      oq.arg("guestOS", main.sx._prodId == "gsx" ? "win2000Pro" : "win2000Serv");
      oq.arg("name", "");
      oq.arg("location", "");
      oq.arg("sizeMb", "");
    }

    var lastOs = oq.arg("guestOS")[0];
    oq.cache();
    var os = oq.arg("guestOS")[0];

    guestInfo = null;
    for (var i = 0; i < main.sx._guests.length; i++) {
      if ((main.sx._guests[i].key == os) || 
          (guestInfo == null && main.sx._guests[i].key == "other")) {
        guestInfo = main.sx._guests[i];
      }
    }

    wrnMsg(os);

    defVal("name", os, lastOs);
    defVal("location", os, lastOs);
    defVal("sizeMb", os, lastOs);
    defVal("num", os, lastOs);
    defVal("scsiCtlr", os, lastOs);

    dom.subHdr.innerHTML = oq.arg("name")[0];

    // Fill in the memory limits
    // Don't use hrVal since the text input is in MB
    obj("memRec").innerHTML = guestInfo.defSizeMb + " M";
    obj("memMin").innerHTML = guestInfo.minSizeMb + " M";
    if (main.sx._prodId == "gsx") {
      obj("mxNew").css("display", "none");
    } else {
      obj("mxNew1").innerHTML = memInfo.mxnew[1] + " M";
      obj("mxNew2").innerHTML = memInfo.mxnew[2] + " M";
    }
  };

  ldgMsg.nxt[0] = stdOpts;
  ldgMsg.exec = function (i) {
    stdOpts.prv = null;

    home = w.home;
    var sep = main.sx._os == "win32" ? "\\" : "/";
    if (home != "" && home.charAt(w.home.length - 1) != sep) {
      home += sep;
    }

    memInfo = clone(w.memInfo);

    fillForm();

    setTimeout("parent.layoutCb()", 40);
  };

  stdOpts.nxt[0] = cfgOpts;
  stdOpts.exec = function () {
    oq.cache();
    if (oq.arg("location")[0] == "") {
      alert("Please specify the virtual machine location.");
      oq.f.location.focus();
      oq.f.location.select();
      return false;
    }

    fillForm();
    setTimeout("parent.layoutCb()", 40);
  };

  cfgOpts.nxt[0] = svgMsg;
  cfgOpts.exec = function () {
    oq.cache();

    var max = oq.arg("guestOS")[0].match(/^(nt4|winnt|winnetweb)$/i) ? 2048 : 3600;
    var err = null;
    var size = parseFloat(oq.arg("sizeMb")[0]);
    if (isNaN(size) || size <= 0) {
      err = "Memory size must be a positive integer.";
    } else if (size % 4 != 0) {
      err = "Please specify memory in multiples of 4 megabytes.";
    } else if (size > max) {
      err = "Memory size cannot be larger than " + max + " megabytes.";
    }
    if (err != null) {
      alert("Invalid memory size: " + err);
      oq.f.sizeMb.focus();
      oq.f.sizeMb.select();
      return false;
    }

    oq.arg("sizeMb",size);

    ok = true;
    oq.submit();
  };

  tryNext = function (ut, sx, vm) {
    if (vm.hash() == hash) { next(); }
  };

  svgMsg.nxt[0] = addDsk;
  svgMsg.exec = function () {
    svdMsg.prv = null;

    parent.ctx = "vmAddDeviceWizard";
    var url = main.sx._prodId == "gsx" ? "vmVirtualDiskGSX.html" : "vmVirtualDisk.html";
    url += "?vmid=" + hash;
    window.setTimeout('self.location.replace("' + url + '")', 250);
  };

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  // XXX: I still think this is a hack, but it works surprisingly well.
  // --mikol June 27, 2003 3:11 PM
  tglBckBtn = function () {
    if (curStep.n == "ldgMsg" || curStep.n == "stdOpts") {
      parent.btns.win().obj("bckBtn").css("display", "none");
    } else {
      parent.btns.win().obj("bckBtn").css("display", "");
    }
  };

  self.setInterval(tglBckBtn, 250);

  w.location.replace("/sx-createvm/index.pl");
}
