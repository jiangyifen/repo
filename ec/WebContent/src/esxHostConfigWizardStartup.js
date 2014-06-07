// Original query, edited query, phantom query (the unseen form that will
// ultimately be submitted)
var oq, eq, pq;
var data;
var ok = false;

var pinged = false;
var forceSave = false;

// --------------------------------------------------------------------------

function initOther() {
  var slctIdx;

  other = function (e) {
    if (e.options[e.selectedIndex].value == -1) {
      // Absolute and advisory minimums
      var absMin = 32;
      var advMin = 192;
      var max = parseInt(data.ram);
      max = max > data.maxCosRam ? data.maxCosRam : max;

      var i = prompt("Please specify a reserved memory value " +
        "between " + advMin + " and " + max + " megabytes.", "");
      var err = null;

      if (i == null) {
        e.selectedIndex = slctIdx;
      } else if (!i.match(/^\d+$/)) {
        err = "Reserved memory must be a positive integer.";
      } else if (parseInt(i) < absMin || parseInt(i) > max) {
        err = "Reserved memory must be between " + absMin + " and " + max + " megabytes.";
      } else if (parseInt(i) % 4 != 0) {
        err = "Reserved memory must be a multiple of four.";
      } else {
        var fnd = false;
        for (var j = 0; j < e.options.length; j++) {
          if (e.options[j].value == i) {
            e.selectedIndex = j;
            fnd = true;
            break;
          }
        }

        if (fnd) {
          ;
        } else if (e.options.length == 5) {
          var valOpt = new Option(hrVal(i, 0), i, false, true);
          var o = e.options[e.options.length - 1];
          var othOpt = new Option(o.text, o.value, false, false)
          e.options[e.options.length - 1] = valOpt;
          e.options[e.options.length] = othOpt;
        } else {
          e.options[e.options.length - 2].value = i;
          e.options[e.options.length - 2].selected = true;
          e.options[e.options.length - 2].text = hrVal(i, 0);
        }
      }
      if (err != null) {
        alert('The specified reserved memory value "'+i+'" cannot be used. ' + err);
        e.selectedIndex = slctIdx;
      }
    }
    slctIdx = e.selectedIndex;
  };
}


// --------------------------------------------------------------------------

function exit(i) {
  if (parent.ctx == "wizard") {
    if (i == -1) {
      self.location.replace("/vmware/en/esxHostConfigWizardLicense.html");
    } else {
      self.location.replace("/vmware/en/esxHostConfigWizardReboot.html?reboot=1&usrMsg=hyperThreading");
    }
  } else {
    if (i == 1) {
      self.location.replace("/vmware/en/reboot.html?usrMsg=hyperThreading");
    } else {
      parent.close();
    }
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
    for (var i in w.msg) {
      s += "\n\n" + w.msg[i];
    }
    alert(s);
  }

  if (ok) {
    if (w.err.length < 1) {
      next();
    } else {
      w.history.go(-1);
      ok = false;
      prev();
    }

    // update immediately instead of refreshing from the server
    var hprThreadEnbld = ( !eq.arg("hprThrdTgl")[0] ) ? false : true;
    if ( main != null && main.sx != null ) {
      main.sx._htEnbl = hprThreadEnbld;
    } else {
      parent.sx._htEnbl = hprThreadEnbld;
    }
    
    return;
  }

  self.data = w;
  next();
}


// --------------------------------------------------------------------------

function initPage() {
  initOther();
  var w = parent.op.win();
  var vmhbaDevGrps = {};

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  var dom = new Object();
  dom.bdy = obj("tbl").getElementsByTagName("tbody")[0];

  /* 2 cases where the user meets this page. Most of the time the page'll 
     have the data availale through the main frame. In the case of first 
     time system startup, the user is taken through the startup wizard 
     at which point there is no main frame yet.
  */
  var htAllowed = (main != null && main.sx != null) ? main.sx._ht : parent.sx._ht;
  if ( htAllowed == 0 ) {
    dom.bdy.removeChild(obj("htRow"));
    /* XXX If hyperthreading is removed, the system wide  category is removed 
       as well. Future scenarios might have more than hThreading in this 
       category, do not remove if so. 
    */
    dom.bdy.removeChild(obj("sstmWideCat"));
  } 

  dom.devRow = dom.bdy.removeChild(obj("devRow"));
  dom.keyRow = dom.bdy.removeChild(obj("keyRow"));
  dom.cosNic = dom.bdy.removeChild(obj("cosNic"));
  dom.phorm = obj("phorm");

  function rndrVmhbaDevGrp(dev) {
    var drvr = dev.drvrs[dev.slctDrvr];
    if (vmhbaDevGrps[drvr] == null || vmhbaDevGrps[drvr].length < 1) return;

    dom[drvr] = new Object();

    dom[drvr].devRow =
      dom.bdy.appendChild(dom.devRow.cloneNode(true));
    objAtt(dom[drvr].devRow, "id", drvr);

    dom[drvr].dev = document.getElementById("dev");
    objAtt(dom[drvr].dev, "id", "");

    dom[drvr].dev.innerHTML = dev.cls + " group: " + drvr;

    var kri; // Key Row ID.

    // Allocation
    kri = initKeyRow(drvr, "owner", "Dedicated To",
      dom, dom.bdy, dom.keyRow);
    var val = new Array(w.devAllocTypes.host, w.devAllocTypes.vm);
    var lbl = new Array("Service Console", "Virtual Machines");
    var da = dev.alloc == w.devAllocTypes.shared ? w.devAllocTypes.vm : dev.alloc;

    dom[kri].val.innerHTML = htmlSlct(kri, val, da, lbl, "initVmhbaDevGrps();");
    objAtt(dom[kri].keyTd, "class", "slctKey");
    objAtt(dom[kri].valTd, "class", "slctVal");

    // Driver
    kri = initKeyRow(drvr, "drvr", "Driver",
      dom, dom.bdy, dom.keyRow);

    dom[kri].val.innerHTML = dev.drvrs[0];

    while (vmhbaDevGrps[drvr].length > 0) {
      dev = vmhbaDevGrps[drvr].pop();
      var id = dev.bus + ":" + dev.dev + "." + dev.fun;

      // Owner
      var hkri = initKeyRow(id, "owner:host", dev.dnm + " (" +
        dev.bus + ":" + dev.dev + "." + dev.fun + ")",
        dom, dom.bdy, dom.keyRow);

      var vkri = initKeyRow(id, "owner:vmkernel", dev.dnm + " (" +
        dev.bus + ":" + dev.dev + "." + dev.fun + ")",
        dom, dom.bdy, dom.keyRow);

      kri = id + ":owner";

      // One of these will be hidden when initVmhbaDevGrps is called.
      dom[hkri].val.innerHTML = '<input type="checkbox"' +
        ' disabled style="margin:0px;margin-right:4px;" />' +
        '<span class="note"><span class="note">' +
        'Shared with Service Console</span></span>';
      dom[vkri].val.innerHTML = '<input type="checkbox" name="' + kri +
        '" value="' + w.devAllocTypes.shared + '"' +
        (dev.alloc == w.devAllocTypes.shared ? ' checked' : '') +
        ' style="margin:0px;margin-right:4px;" />' +
        "Shared with Service Console";
    }
  }

  function rndrPciDev(dev) {
    // XXX: As things stand today, there are two types of vmkernel devices:
    // vmhba and vmnic; only vmhba devices can be shared between the console
    // and the vmkernel so the following test accurately separates vmhba
    // devices from vmnic devices. If we ever allow vmnics to be shared
    // (however unlikely that may be), this will not have the intended effect.
    if ((dev.mode & 1) == 1) {
      rndrVmhbaDevGrp(dev);
      return;
    }

    var id = dev.bus + ":" + dev.dev + "." + dev.fun;

    // Device class: Name
    dom[id] = new Object();

    dom[id].devRow =
      dom.bdy.appendChild(dom.devRow.cloneNode(true));
    objAtt(dom[id].devRow, "id", id);

    dom[id].dev = document.getElementById("dev");
    objAtt(dom[id].dev, "id", "");

    dom[id].dev.innerHTML = dev.cls + ": " + dev.dnm;

    var kri; // Key Row ID.

    // Allocation
    kri = initKeyRow(id, "owner", "Dedicated To",
      dom, dom.bdy, dom.keyRow);
    var val = new Array();
    var lbl = new Array();
    if ((dev.mode & 2) == 2) {
      val.push(w.devAllocTypes.host);
      lbl.push("Service Console");
    }
    if ((dev.mode & 4) == 4) {
      val.push(w.devAllocTypes.vm);
      lbl.push("Virtual Machines");
    }
    if ((dev.mode & 1) == 1) {
      val.push(w.devAllocTypes.shared);
      lbl.push("Shared");
    }

    if (val.length > 1) {
      dom[kri].val.innerHTML = htmlSlct(kri, val, dev.alloc, lbl);
      objAtt(dom[kri].keyTd, "class", "slctKey");
      objAtt(dom[kri].valTd, "class", "slctVal");
    } else {
      dom[kri].val.innerHTML = lbl[0] +
        '<input type="hidden" name="' + kri + '" value="' + dev.alloc + '">';
    }

    if (w.cosNic!=null && w.cosNic.bus==dev.bus && w.cosNic.dev==dev.dev &&
        (dev.mf || w.cosNic.fun==dev.fun)) {
      dom.cosNic = dom.bdy.appendChild(dom.cosNic);
      objCss(dom[kri].keyRow, "display", "none");

      self.cosNicKri = kri;

      tglCosNic = function () {
        objCss(dom[cosNicKri].keyRow, "display", "");
        objCss(dom.cosNic, "display", "none");
      }
    }

    // Driver
    kri = initKeyRow(id, "drvr", "Driver",
      dom, dom.bdy, dom.keyRow);

    if (dev.drvrs > 1) {
      dom[kri].val.innerHTML = htmlSlct(kri, dev.drvrs, dev.slctDrvr);
      objAtt(dom[kri].keyTd, "class", "slctKey");
      objAtt(dom[kri].valTd, "class", "slctVal");
    } else {
      dom[kri].val.innerHTML = dev.drvrs[0] +
        '<input type="hidden" name="' + kri + '" value="' + dev.drvrs[0] + '">';
    }

    // PCI Bus:Device.Function
    kri = initKeyRow(id, "busdev", "PCI Bus:Device.Function",
      dom, dom.bdy, dom.keyRow);
    dom[kri].val.innerHTML = dev.bus + ":" + dev.dev + "." + dev.fun;
  }


  initVmhbaDevGrps = function () {
    // First, figure out the global owners for each device group.
    var a = eq.arg();
    var dgo = {};
    for (var i = 0; i < a.length; i++) {
      eq.cache(a[i]);
      var dg = a[i].split(":");
      // Group ownership will be in an element named <module.o>:owner. Device
      // ownership will be in an element named bus:dev.fun:owner.
      if (a[i].indexOf(":owner") > -1 && dg.length == 2) {
        dgo[dg[0]] = eq.arg(a[i])[0];
      }
    }
    // The phantom form should be the same as the edit form.
    pq.sync(eq);

    // Second, map the global device group owner to an owner for each device.
    var prfl = w.prfls[0];
    for (var n in prfl.devs) {
      var dev = prfl.devs[n];
      var drvr = dev.drvrs[dev.slctDrvr];
      if (dgo[drvr] != null) {
        var kri = dev.bus + ":" + dev.dev + "." + dev.fun + ":owner";
        // If the global owner is host, make sure that the corresponding shared
        // ownership checkbox is unchecked.
        if (dgo[drvr] == w.devAllocTypes.host) {
          eq.select(kri, w.devAllocTypes.shared, false);
          objCss(dom[kri+":vmkernel"].keyRow, "display", "none");
          objCss(dom[kri+":host"].keyRow, "display", "");
        } else {
          objCss(dom[kri+":vmkernel"].keyRow, "display", "");
          objCss(dom[kri+":host"].keyRow, "display", "none");
        }

        // If the device owner is not set in the edited form, align it with the
        // global owner in the phantom form.
        if (eq.arg(kri)[0] == null ||
          eq.arg(kri)[0] != w.devAllocTypes.shared ||
            dgo[drvr] == w.devAllocTypes.host) {
          pq.arg(kri, dgo[drvr] ? dgo[drvr] : 0);
        }
      }
    }
  }


  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;
    var prfl = w.prfls[0];

    // Device Groups --------------------------------------------------------
    for (var n in prfl.devs) {
      var dev = prfl.devs[n];
      // Only vmhbas are sharable and must be grouped.
      if (dev.mode & 1) {
        var drvr = dev.drvrs[dev.slctDrvr];
        if (vmhbaDevGrps[drvr] == null) {
          vmhbaDevGrps[drvr] = [];
        }
        vmhbaDevGrps[drvr].push(dev);
      }
    }

    // System Startup -------------------------------------------------------

    document.form.oldLabel.value = prfl.name;

    // hyperThreading toggle 
    
    if ( htAllowed==1 ) {
      obj("hprThrdTgl").checked=w.cpuHt;
    }
    
    // Reserved Memory

    obj("cosMemSizeKey").innerHTML += " (" + hrVal(w.ram) + " Available)";

    var sele = document.form.ram;
    var fnd = false;
    for (var j = 0; j < sele.options.length; j++) {
      if (sele.options[j].value == w.prfls[0].ram) {
        sele.selectedIndex = j;
        fnd = true;
        break;
      }
    }
    if (! fnd) {
      var valOpt = new Option(hrVal(prfl.ram, 0),
        w.prfls[0].ram, false, true);
      var o = sele.options[sele.options.length - 1];
      var othOpt = new Option(o.text, o.value, false, false)
      sele.options[sele.options.length - 1] = valOpt;
      sele.options[sele.options.length] = othOpt;
      sele.options[sele.options.length - 2].selected = true;
    }

    // Service Console Kernel
    if (w.krnls.length > 1) {
      obj("cosKrnl").innerHTML = htmlSlct("krnl", w.krnls, prfl.krnl);
      obj("cosKrnlKeyTd").att("class", "slctKey");
      obj("cosKrnlValTd").att("class", "slctVal");
    } else {
      obj("cosKrnl").innerHTML = w.krnls[0] +
        '<input type="hidden" name="krnl" value="' + w.krnls[0] + '">';
    }

    // Hardware Profile -----------------------------------------------------
    for (var n in prfl.devs) {
      rndrPciDev(prfl.devs[n]);
    }

    eq = new Query(document.forms[0]);
    // Make sure that the phantom form is complete.
    pq = new Query(document.forms[1]);
    pq.sync(eq);
    var a = pq.arg();
    for (var n in a) {
      if (pq.list(a[n]).length == 0) {
        dom.phorm.innerHTML += htmlHdnInpt(a[n]);
        pq.dump(a[n]);
      }
    }
    initVmhbaDevGrps();

    // The old query should be the same as the edit form.
    oq = new Query();
    oq.sync(eq);
  };

  editor.nxt[0] = svgMsg;
  editor.exec = function () {
    if (! pinged) {
      window.setTimeout("next();", 50);
      return false;
    }

    initVmhbaDevGrps();

    if (eq.diff(oq) || forceSave) {
      var a = eq.arg();
      ok = true;
      pq.submit();
    } else {
      if (parent.ctx == "wizard") {
        self.location.replace("/vmware/en/esxHostConfigWizardReboot.html?reboot=0&usrMsg=hyperThreading");
      } else {
        parent.close();
      }
    }
  };

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  if (parent.ctx == "wizard") {
    initPing(function (d) {forceSave = ! d.vmk; pinged = true; return true;});
    ping();
  } else {
    pinged = true;
  }

  // ctx=wizrd seems to be what we want, even in the editor context.
  parent.loadData("/startup?ctx=wizard");
}
