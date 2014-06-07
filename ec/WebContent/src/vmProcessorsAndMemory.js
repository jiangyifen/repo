/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// Original query, edited query
var oq, eq;
var ok = false;


// --------------------------------------------------------------------------

function exit(i) {
  parent.close();
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
    main.vmDevObsrvr.lstn(obsrvrStr, lstnr, vm);
    main.vmVcpuObsrvr.lstn(obsrvrStr, lstnr, vm);
    main.vmHwvObsrvr.lstn(obsrvrStr, lstnr, vm);
    prev();
    return;
  }

  if (ok == "mod") {
    var vm = main.sx.vms[new Query(parent.location.search).arg("vmid")[0]];
    vm.dev(w.devs[0].id, w.devs[0]);
    vm.vcpu(w.devs[0].num);
  }

  if (ok == "upgrade") {
    var vm = main.sx.vms[new Query(parent.location.search).arg("vmid")[0]];
    vm.hwv(main.defaultHwv);
  }

  next();
}


// --------------------------------------------------------------------------

function initPage() {
  var w = parent.op.win();
  var q = new Query(parent.location.search);
  var vm = main.sx.vms[q.arg("vmid")[0]];
  var dev = vm.dev(q.arg("dev")[0]);
  var obsrvrStr = "vmProcessorsAndMemory_" + vm.hash();

  var dom = {};
  dom.prcRow = obj("prcRow");
  dom.wrnRow = obj("wrnRow");
  dom.wrnMsg = obj("wrnMsg");

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var hwvMsg = new Step("hwvMsg", obj("hwvMsg"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  hidePrcRow = function () {
    dom.wrnRow.css("display", "");
    dom.prcRow.css("display", "none");

    parent.layoutCb();
  };

  showPrcRow = function () {
    dom.wrnRow.css("display", "none");
    dom.prcRow.css("display", "");

    parent.layoutCb();
  };

  var ignrMsg = {"vmUp":false,"vmSmp":false};

  // No, this function can't be called wrnMsg as it clearly should be (PR28212)
  warnMsg = function (im) {
    if (im != null) { ignrMsg[im] = true; }
    var msg = false;

    if (main.sx._prodId == "gsx") {
      dom.wrnMsg.innerHTML = "This product only permits virtual machines " +
        "to be configured with a single processor.";
      msg = true;
    } else if (main.sx._license.maxVcpuNum == 1) {
      dom.wrnMsg.innerHTML = "Your license only permits virtual machines " +
        "to be configured with a single processor.";
      msg = true;
    } else if (vm.os().match(/^winxppro|winnt|nt4|netware\d$/i)) {
      dom.wrnMsg.innerHTML = "Virtual machines running " + hrGos(vm.os()) +
        " may only be configured with a single processor.";
      msg = true;
    } else if (vm.vcpu() > 1 && ! ignrMsg.vmSmp) {
      dom.wrnMsg.innerHTML = "This virtual machine is configured with more " +
        "than one processor. If you choose a single processor configuration " +
        "and the guest operating system is tuned for multiple processors, " +
        "it may not boot and will probably degrade the performance " +
        "of other virtual machines. To continue, " +
        '<a href="javascript:;" onclick="warnMsg(\'vmSmp\');">click here</a>' +
        ".";
      msg = true;
    } else if (main.sx._cpuInfo < 2) {
      dom.wrnMsg.innerHTML = "The current system has a single processor. " +
        "Virtual machines configured with more than one " +
        "processor will not run unless the underlying system has as many " +
        "processors as the virtual machine.";
      msg = true;
    } else if (vm.hwv() < main.defaultHwv) {
      dom.wrnMsg.innerHTML = "This virtual machine is using legacy " +
        "virtual hardware and cannot be configured with more than a single " +
        "processor. To upgrade the virtual machine's hardware, " +
        '<a href="javascript:;" onclick="next(1);">click here</a>' +
        ".";
      msg = true;
    } else if (vm.vcpu() == 1 && ! ignrMsg.vmUp) {
      dom.wrnMsg.innerHTML = "This virtual machine is configured with a " +
        "single processor. The guest operating system may require " +
        "additional tuning if this number is increased. To continue, " +
        '<a href="javascript:;" onclick="warnMsg(\'vmUp\');">click here</a>' +
        ".";
      msg = true;
    }

    msg ? hidePrcRow() : showPrcRow();
  };

  function fillForm () {
    // Make a new Query object to represent the original form.
    oq = new Query(document.forms[0]);
    oq.arg("vmid", vm.hash());
    oq.arg("dev", dev.id);

    // Processors
    warnMsg();
    oq.arg("num", vm.vcpu());

    // Memory
    oq.arg("mb", dev.mb);

    // Make a new Query object to represent the modified form.
    eq = new Query(document.forms[0]);
    eq.sync(oq);
  }

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;

    // Don't use hrVal since the text input is in MB
    obj("memRec").innerHTML = w.memInfo.memRec + " M";
    obj("memMin").innerHTML = w.memInfo.memMin + " M";
    if (main.sx._prodId == "gsx") {
      obj("mxNew").css("display", "none");
    } else {
      obj("mxNew1").innerHTML = w.memInfo.mxnew[1] + " M";
      obj("mxNew2").innerHTML = w.memInfo.mxnew[2] + " M";
    }

    fillForm();

    setTimeout("parent.layoutCb()", 40);
  };

  editor.nxt[0] = svgMsg;
  editor.nxt[1] = hwvMsg;
  editor.exec = function (idx) {
    if (idx == 1) {
      if (confirm("If this virtual machine's hardware is upgraded, it can " +
        "only be powered on with ESX Server version 2.0 or later. If you " +
        "need to run this virtual machine on versions of ESX Server prior " +
        "to 2.0, then click Cancel. Upgrade virtual hardware now?")) {
        var q = new Query();
        q.action("/vm-config/index.pl");
        q.target(parent.op.win());
        q.arg("op", "upgrade");
        q.arg("vmid", vm.hash());
        ok = "upgrade";
        q.submit();

        return true;
      } else {
        return false;
      }
    }

    eq.cache();

    var max = vm.os().match(/^(nt4|winnt|winnetweb)$/i) ? 2048 : 3600;
    var err = null;
    var size = eq.arg("mb")[0];
    if (!size.match(/^\d+$/) || size == 0) {
      err = "Memory size must be a positive integer.";
    } else if (size % 4 != 0) {
      err = "Please specify memory in multiples of 4 megabytes.";
    } else if (size > max) {
      err = "Memory size cannot be larger than " + max + " megabytes.";
    }
    if (err != null) {
      alert("Invalid memory size: " + err);
      eq.f.mb.focus();
      eq.f.mb.select();
      return false;
    }

    // valid mb value here, make sure it's a base 10 integer
    eq.arg("mb",parseInt(eq.arg("mb")[0],10));

    if (eq.diff(oq)) {
      ok = "mod";

      // Params should be qualified with their dev name before being submitted.
      for (var j = 0; j < eq.f.length; j++) {
        if (eq.f[j].name.match(/^(op|vmid|dev)$/i)) { continue; }
        eq.f[j].name = dev.id + "::" + eq.f[j].name;
      }

      main.vmDevObsrvr.ignr(obsrvrStr);
      main.vmVcpuObsrvr.ignr(obsrvrStr);
      main.vmHwvObsrvr.ignr(obsrvrStr);
      eq.submit();
    } else {
      exit();
    }
  };

  hwvMsg.nxt[0] = editor;
  hwvMsg.exec = function () {
    editor.prv = null;
    fillForm();
    setTimeout("parent.layoutCb()", 40);
  };

  lstnr = function (t, o, v) {
    if (v.id == dev.id && confirm("The processor and memory configuration " +
      "has changed. Reload configuration?")) {
      fillForm();
    }
  };
  main.vmDevObsrvr.lstn(obsrvrStr, lstnr, vm);
  main.vmVcpuObsrvr.lstn(obsrvrStr, lstnr, vm);
  main.vmHwvObsrvr.lstn(obsrvrStr, lstnr, vm);

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  w.location.replace("/vm-config/index.pl?vmid=" + vm.hash() + "&dev=" + esc(dev.id));
}
