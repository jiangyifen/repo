// Original query, edited query
var oq, eq;
var ok = false;
var needReboot = false;

// --------------------------------------------------------------------------

function exit(i) {
  if (parent.ctx == "wizard") {
    if (i == -1) {
      self.location.replace("/vmware/en/getSwap.html");
    } else {
      self.location.replace("/vmware/en/securitySettings.html");
    }
  } else {
    if (i == -1 || i == 2 || ! needReboot) {
      parent.close();
    } else {
      self.location.replace("/vmware/en/reboot.html");
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
    for (var i in w.err) {
      s += "\n\n" + w.err[i];
    }
    alert(s);
  }

  if (ok) {
     if (w.err.length < 1) {
       exit();
     } else {
       prev();
       ok = false;
     }
     return;
  }

  next();
}


// --------------------------------------------------------------------------

function getArg(a) {
  var s = location.search.substr(1);
  s = s.split('&');
  for (var i = 0; i < s.length; i++) {
    var r = s[i].split('=');
    if (r[0] == a) {
      return r[1];
    }
  }
}


// --------------------------------------------------------------------------

function initPage() {
  var w = parent.op.win();

  // Dom Objects ------------------------------------------------------------
  var dom = new Object();

  dom.bdy = obj("tbl").getElementsByTagName("tbody")[0];
  dom.nicRow = dom.bdy.removeChild(obj("nicRow"));
  dom.keyRow = dom.bdy.removeChild(obj("keyRow"));
  dom.sepRow = obj("sepRow");
  dom.noNics = obj("noNics");
  dom.ldgMsg = obj("ldgMsg");
  dom.svgMsg = obj("svgMsg");

  hideLdgMsg = function () {
    dom.ldgMsg.css("display", "none");
  }

  function getNicByVnm(vnm) {
    for (var i = 0; i < w.nics.length; i++) {
      if (w.nics[i].vnm == vnm) {
        return w.nics[i];
      }
    }

    for (var i = 0; i < w.nicTeams.length; i++) {
      if (w.nicTeams[i] == vnm) {
        return {"vnm":w.nicTeams[i]};
      }
    }

    return null;
  }

  function initIsVnmVmnic() {
    var re = new RegExp("vmnic", "i");

    isVnmVmnic = function (vnm) {
      return vnm.match(re);
    };
  }
  initIsVnmVmnic();

  function fillNicTeamSlctCtrl(nic) {
    w = w ? w : parent.op.win();

    var kri = nic.id + ":team";
    // Cache the current form state.
    if (defined(eq)) { eq.cache(kri); }

    if (nic.active) {
      var nicName = w.nicNames[nic.team] ? w.nicNames[nic.team] : nic.team;
      if (nicName == "none") {
        nicName = w.nicNames[nic.vnm] ? w.nicNames[nic.vnm] : nic.vnm;
      }

      dom[kri].val.innerHTML = nicName +
        '<input type="hidden" name="' + kri +
        '" value="' + nic.team + '">';
    } else {
      var nicNames = clone(w.nicTeams, true);
      for (var i = 0; i < nicNames.length; i++) {
        nicNames[i] = w.nicNames[nicNames[i]] ? w.nicNames[nicNames[i]]
                                              : defnm(nicNames[i]);

        if (nicNames[i] == "none") {
          nicNames[i] = w.nicNames[nic.vnm] ? w.nicNames[nic.vnm]
                                            : defnm(nic.vnm);
        }
      }

      dom[kri].val.innerHTML = htmlSlct(kri, w.nicTeams, nic.team, nicNames,
        "hndlNetChng(this);");
      objAtt(dom[kri].keyTd, "class", "slctKey");
      objAtt(dom[kri].valTd, "class", "slctVal");
    }

    // Return the form to its state before the select control was changed.
    if (defined(eq)) { eq.dump(kri); }
  }

  hndlNicNameChng = function (ele, vnm) {
    var nic = getNicByVnm(vnm);

    if (nic == null) { return; }

    w.nicNames[nic.vnm] = ele.value;

    for (var n in w.nics) { fillNicTeamSlctCtrl(w.nics[n]); }
  };

  function isVmnicFreeAfterEdit(vmnic) {
    eq.cache();

    var kri = getNicByVnm(vmnic).id + ":team";

    return eq.arg(kri)[0] == "none";
  }

  function doesBondHaveSlavesAfterEdit(bond) {
    eq.cache();

    for (var n in w.nics) {
      var kri = w.nics[n].id + ":team";
      if (eq.arg(kri)[0] == bond) {
        return true;
      }
    }

    return false;
  }

  function defnm(v) {
    m = v.match(/^vmnic(.+)/i);
    if (m != null) {
      return "Adapter" + m[1] + " Network";
    }

    m = v.match(/^bond(.+)/i);
    if (m != null) {
      return "Bond" + m[1] + " Network";
    }

    m = v.match(/^vmnet_(.+)/i);
    if (m != null) {
      return "Internal Network " + (parseInt(m[1]) + 1);
    }

    return v;
  }

  hndlNetChng = function (ele) {
    w = w ? w : parent.op.win();
    var v = ele.value;

    if (v != "none") {
      var kri = v + ":net";
      if ((! w.nicNames[v] || w.nicNames[v] == v) &&
        (! defined(dom[kri]) || objCss(dom[kri].keyRow, "display") == "none")) {
        var lbl =
          prompt("Please provide a meaningful label for the selected network.",
            defnm(v));

        if (defined(lbl)) {
          w.nicNames[v] = lbl || v;

          rndrNicName(v, w.nicNames[v] ? w.nicNames[v] : defnm(v));
        } else {
          eq.dump();
        }

        for (var n in w.nics) { fillNicTeamSlctCtrl(w.nics[n]); }
      } else {
        eq.f[kri].disabled = false;
        objCss(dom[kri].keyRow, "display", "");
      }
    }

    for (var i = 0; i < w.nicTeams.length; i++) {
      if (! doesBondHaveSlavesAfterEdit(w.nicTeams[i])) {
        var kri = w.nicTeams[i] + ":net";

        if (defined(dom[kri])) {
          eq.f[kri].disabled = true;
          objCss(dom[kri].keyRow, "display", "none");
        }
      }
    }

    for (var i = 0; i < w.nics.length; i++) {
      var kri = w.nics[i].vnm + ":net";

      if (defined(eq.f[kri])) {
        if (isVmnicFreeAfterEdit(w.nics[i].vnm)) {
          eq.f[kri].disabled = false;
          objCss(dom[kri].keyRow, "display", "");
        } else {
          eq.f[kri].disabled = true;
          objCss(dom[kri].keyRow, "display", "none");
        }
      } else {
        if (isVmnicFreeAfterEdit(w.nics[i].vnm)) {
          rndrNicName(w.nics[i].vnm,
            w.nicNames[w.nics[i].vnm] ? w.nicNames[w.nics[i].vnm] : defnm(w.nics[i].vnm));
        }
      }
    }
  };

  // Network Adapters -------------------------------------------------------
  rndrNic = function (data, nic) {
    w = data;
    var id = nic.id;
    dom[id] = new Object();

    // Network Adapter ------------------------------------------------------
    dom[id].nicRow = dom.bdy.insertBefore(dom.nicRow.cloneNode(true), dom.sepRow);
    objAtt(dom[id].nicRow, "id", id);

    dom[id].nicLbl = document.getElementById("nicLbl");
    objAtt(dom[id].nicLbl, "id", "");

    dom[id].nicLbl.innerHTML = nic.dnm;

    // Virtual Device Name --------------------------------------------------
    var kri = initKeyRow(id, "vnm", "Virtual Device Name",
      dom, dom.bdy, dom.keyRow, dom.sepRow);
    dom[kri].val.innerHTML = nic.vnm;

    // Driver ---------------------------------------------------------------
    kri = initKeyRow(id, "drvr", "Driver",
      dom, dom.bdy, dom.keyRow, dom.sepRow);
    dom[kri].val.innerHTML = nic.drvr;

    // Configured Speed, Duplex ---------------------------------------------
    kri = initKeyRow(id, "cfgSpd", "Configured Speed, Duplex",
      dom, dom.bdy, dom.keyRow, dom.sepRow);

    if (w.drvrSpdDplx[nic.drvr].length > 0) {
      dom[kri].val.innerHTML = htmlSlct(kri, w.drvrSpdDplx[nic.drvr],
        w.drvrSpdDplx[nic.drvr][nic.cfgSpd], w.nicSpdDplxs);
      objAtt(dom[kri].keyTd, "class", "slctKey");
      objAtt(dom[kri].valTd, "class", "slctVal");
    } else {
      dom[kri].val.innerHTML = w.nicSpdDplxs[nic.cfgSpd] +
        '<input type="hidden" name="' + kri +
        '" value="' + w.drvrSpdDplx[nic.drvr][nic.cfgSpd] + '">';
    }

    // Actual Speed, Duplex -------------------------------------------------
    kri = initKeyRow(id, "curSpd", "Actual Speed, Duplex",
      dom, dom.bdy, dom.keyRow, dom.sepRow);
    dom[kri].val.innerHTML = nic.curSpd;

    // Interrupt Clustering -------------------------------------------------
    // Not configurable through the MUI for ESX 2.0
    // kri = initKeyRow(id, "clstr", "Interrupt Clustering",
    //   dom, dom.bdy, dom.keyRow);
    //
    // if (nic.clstr != null) {
    //   dom[kri].val.innerHTML = htmlSlct(kri, ["enbl", "dsbl"], nic.clstr,
    //     ["Enabled", "Disabled"]);
    //   objAtt(dom[kri].keyTd, "class", "slctKey");
    //   objAtt(dom[kri].valTd, "class", "slctVal");
    // } else {
    //   dom[kri].val.innerHTML = "Not Supported";
    // }

    // NIC Teaming ----------------------------------------------------------
    kri = initKeyRow(id, "team", "Network",
      dom, dom.bdy, dom.keyRow, dom.sepRow);
    fillNicTeamSlctCtrl(nic);

    // PCI Bus:Device.Function ----------------------------------------------
    kri = initKeyRow(id, "pci", "PCI Bus:Device.Function",
      dom, dom.bdy, dom.keyRow, dom.sepRow);
    dom[kri].val.innerHTML = nic.bus + ":" + nic.dev + "." + nic.fun;
  };

  addVmnet = function () {
    var filled = [];
    var re = new RegExp("vmnet_(.+)", "i");

    for (var n in w.nicNames) {
      var m = n.match(re);
      if (m != null) {
        filled[m[1]] = true;
      }
    }

    var cnt = 0;

    while (cnt < 256) {
      if (! filled[cnt]) {
        break;
      }
      cnt++;
    }

    if (cnt >= 256) {
      alert("No more than 256 internal networks can be created.");
    }

    var vmnet = prompt("Please supply a meaningful label for a new internal " +
      "network.", defnm("vmnet_" + cnt));

    if (vmnet != null) {
      rndrNicName("vmnet_" + cnt, vmnet);
      w.nicNames["vmnet_" + cnt] = vmnet;
      eq.cache();
      checkForDups();
    }
  };

  rndrNicName = function (nic, name) {
    kri = initKeyRow(nic, "net", nic,
      dom, dom.bdy, dom.keyRow);

    dom[kri].val.innerHTML = htmlTxtInpt(kri, name,
      "hndlNicNameChng(this, '" + nic + "');");
  };

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var start = new Step("start", obj("start"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  ldgMsg.nxt[0] = start;
  ldgMsg.exec = function (i) {
    start.prv = null;

    var cnt = 0;
    for (var n in w.nics) {
      rndrNic(w, w.nics[n]);
      cnt++;
    }

    if (! cnt) {
      start.o = null;
      dom.noNics.css("display", "");
      ldgMsg.o.css("display", "none");
    } else {
      for (var n in w.nicNames) {
        rndrNicName(n, w.nicNames[n]);
      }
      dom.noNics.css("display", "none");
    }

    oq = new Query(document.forms[0]);
    eq = new Query(document.forms[0]);
  };

  function chkReboot () {
    var a = eq.arg();
    for (var i = 0; i < a.length; i++) {
      if (a[i].match(/cfgSpd$/) && eq.arg(a[i])[0] != oq.arg(a[i])[0]) {
        return true;
      }
    }

    return false;
  };

  function checkForDups() {
    var lbls = {};

    for (var n in w.nicNames) {
      var kri = n + ":net";

      if (eq.f[kri] == null || eq.f[kri].disabled) {
        continue;
      }

      var lbl = eq.arg(kri)[0];

      if (lbls[lbl] == null) {
        if (lbl != null && lbl != "") { lbls[lbl] = n; }
      } else {
        alert("Two or more adapters share the same label: " + lbl +
          ". Please ensure that each adapter has a unique network label.");
        return false;
      }
    }

    return true;
  };

  function validate() {
    if (! checkForDups()) { return false; }
    return true;
  };

  start.nxt[0] = svgMsg;
  start.exec = function (i) {
    eq.cache();

    if (eq.diff(oq)) {
      needReboot = chkReboot();

      if (! validate()) { return false; }

      ok = true;
      eq.submit();
    } else {
      exit(2);
    }
  };

  var curStep = ldgMsg;


  // ------------------------------------------------------------------------

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  parent.loadData("/network?ctx=wizard");
}
