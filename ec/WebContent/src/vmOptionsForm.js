/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// Original query, edited query
var oq, eq, vq;
var ok = false;
var obsrvrStr;

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
    if (parent.ctx.match(/^editor$/i)) {
      main.vmOptObsrvr.lstn(obsrvrStr, lstnr, vm);
      main.vmExecutionStateObsrvr.lstn(obsrvrStr, rndrMode, vm);
      main.vmModeObsrvr.lstn(obsrvrStr, rndrMode, vm);
    }
    prev();
    return;
  }

  if (ok) {
    var vm = main.sx.vms[new Query(parent.location.search).arg("vmid")[0]];
    for (var i = 0; i < w.opts.length; i++) {
      vm.opt(w.opts[i].k, w.opts[i].v);
      if (w.opts[i].k == "name") { vm.dn(w.opts[i].v); }
    }
  }

  next();
}


// --------------------------------------------------------------------------

function initPage() {
  var w = parent.op.win();
  var q = new Query(parent.location.search);
  var vm = main.sx.vms[q.arg("vmid")[0]];

  obsrvrStr = "vmOptions" + parent.ctx + "_" + vm.hash();

  // var dom = {};

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  // Returns pretty volume labels.
  function vl(v,m) {
    if (v == null) {
      return "";
    }
    m = m ? ": " + hrVal(v.fmb) + " free" : "";
    if (v.vl) {
      return v.vl + " (" + v.id + ")" + m;
    }
    return v.id + m;
  }

  // Returns an array of pretty volume labels.
  function vls(m) {
    var a = new Array();
    for (var i in w.vols) {
      a.push(vl(w.vols[i], m));
    }
    return a;
  }

  // Returns an array of volume IDs.
  function vvs() {
    var a = new Array();
    for (var i in w.vols) {
      a.push("/vmfs/" + w.vols[i].id);
    }
    return a;
  }

  // Returns the index of a volume given an ID or label.
  function l2idx(l) {
    for (var i in w.vols) {
      if (w.vols[i].vl == l || w.vols[i].id == l) {
        return i;
      }
    }
    return -1;
  }

  // Returns the corresponding volume ID of a given volume label.
  function l2id(l) {
    var i = l2idx(l);
    if (i > -1) {
      return w.vols[i].id;
    }
    return "";
  }

  var vlval, vlidx;

  rndrMode = function () {
    if ((vm.mode() & 2) == 2 && vm.es() == esOff) {
      oq.f.guestOS.disabled = false;
      oq.f.suspendDirectory.disabled = false;
      oq.f.suspendDirectoryGSX.disabled = false;

      var l = oq.list("enableLogging");
      for (var i = 0; i < l.length; i++) {
        oq.f[l[i]].disabled = false;
      }

      var l = oq.list("runWithDebugInfo");
      for (var i = 0; i < l.length; i++) {
        oq.f[l[i]].disabled = false;
      }
    } else {
      oq.f.guestOS.disabled = true;
      oq.f.suspendDirectory.disabled = true;
      oq.f.suspendDirectoryGSX.disabled = true;

      var l = oq.list("enableLogging");
      for (var i = 0; i < l.length; i++) {
        oq.f[l[i]].disabled = true;
      }

      var l = oq.list("runWithDebugInfo");
      for (var i = 0; i < l.length; i++) {
        oq.f[l[i]].disabled = true;
      }
    }
  };

  function fillForm () {
    // Make a new Query object to represent the original form.
    oq = new Query(document.forms[0]);
    oq.arg("vmid", vm.hash());

    oq.arg("name", vm.opt("name"));

    // init the custom slection depending on the host
    oq.f.guestOS.length = 0;
    for (var i = 0; i < main.sx._guests.length; i++) {
      oq.f.guestOS.options[oq.f.guestOS.length] =
        new Option(main.sx._guests[i].name, main.sx._guests[i].key);
    }

    if (main.sx._prodId == "gsx") {
        obj("suspendGSX").css("display", "");
    } else {
        obj("suspendESX").css("display", "");
    }

    // Handle "Other" guest OSes.
    var fnd = false;
    for (var i = 0; i < oq.f.guestOS.options.length; i++) {
      if (oq.f.guestOS.options[i].value == vm.opt("guestOS")) {
        fnd = true;
        break;
      }
    }

    if (! fnd) {
      var lbl = hrGos(vm.opt("guestOS"));
      if (! vm.opt("guestOS").match(/^(win2000|nt4)$/i)) {
        // win2000 and nt4 is how ESX used to specify these guest OSes.
        // Anything else is not supported.
        lbl += " (Not Supported)";
      }

      oq.f.guestOS.options[oq.f.guestOS.options.length] = new Option(lbl,
        vm.opt("guestOS"), true, true);
    }

    oq.arg("guestOS", vm.opt("guestOS"));

    oq.arg("suspendDirectory", vm.opt("suspendDirectory"));
    oq.arg("suspendDirectoryGSX", vm.opt("suspendDirectoryGSX"));
    oq.select("enableLogging", 1, vm.opt("enableLogging"));
    oq.select("enableLogging", 0, ! vm.opt("enableLogging"));

    oq.select("runWithDebugInfo", 1, vm.opt("runWithDebugInfo"));
    oq.select("runWithDebugInfo", 0, ! vm.opt("runWithDebugInfo"));

    //  to represent the modified form.
    eq = new Query(document.forms[0]);
    eq.sync(oq);

    // Make a new Query object to represent the state of the virtual machine.
    vq = new Query();
    vq.sync(eq);
    oq.arg("name", vm.opt("name"));
    oq.arg("guestOS", vm.opt("guestOS"));
    oq.arg("suspendDirectory", vm.opt("suspendDirectory"));
    oq.arg("enableLogging", vm.opt("enableLogging") ? 1 : 0);
    oq.arg("runWithDebugInfo", vm.opt("runWithDebugInfo") ? 1 : 0);
  }

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;

    vlval = vm.opt("suspendDirectory");
    vlidx = vlval ? l2idx(vlval) : 0;

    var vals = vvs();
    var lbls = vls(true);

    // .arg was failing when I tried to set the suspendDirectory select element
    // via innerHTML = htmlSlct. I don't have time to find out why. And I think
    // I like this approache better anyway. --mikol May 12, 2003 11:03 PM
    for (var i = 0; i < vals.length; i++) {
      var slctd = vals[i] == vlval;
      e.options[e.options.length] = new Option(lbls[i], vals[i], slctd, slctd);
    }

    fillForm();
    rndrMode();

    setTimeout("parent.layoutCb()", 40);
  };

  editor.nxt[0] = svgMsg;
  editor.exec = function () {
    eq.cache();

    // Negating versions of the enableLogging and runWithDebugInfo checkboxes
    // are hidden. Toggle them like radio buttons.
    eq.select("enableLogging", 0, ! eq.select("enableLogging", "1"));
    eq.select("runWithDebugInfo", 0, ! eq.select("runWithDebugInfo", "1"));

    if (eq.diff(oq) || eq.diff(vq)) {
      ok = true;

      if (parent.ctx.match(/^editor$/i)) {
        main.vmOptObsrvr.ignr(obsrvrStr);
        main.vmExecutionStateObsrvr.ignr(obsrvrStr, rndrMode, vm);
        main.vmModeObsrvr.ignr(obsrvrStr, rndrMode, vm);
      }
      eq.submit();
    } else {
      exit();
    }
  };

  lstnr = function (t, o, v) {
    if (v.id == dev.id && confirm("The virtual machine options " +
      "have changed. Reload configuration?")) {
      fillForm();
    }
  };
  if (parent.ctx.match(/^editor$/i)) {
    main.vmOptObsrvr.lstn(obsrvrStr, lstnr, vm);
    main.vmExecutionStateObsrvr.lstn(obsrvrStr, rndrMode, vm);
    main.vmModeObsrvr.lstn(obsrvrStr, rndrMode, vm);
  }

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  w.location.replace("/vm-config/index.pl?op=getVmDiskDevData");
}
