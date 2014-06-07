var ok;

function exit(i) {
  if (parent.ctx == "wizard") {
    if (i == -1) {
      self.location.replace("/vmware/en/storageConfiguration.html");
    } else {
      self.location.replace("/vmware/en/networkConnections.html");
    }
  } else {
    parent.close();
  }
}

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

function initPage() {
  var w = parent.op.win();

  // Returns pretty volume labels.
  function vl(v,m) {
    if (v == null) {
      return "";
    }
    m = m ? ": " + hrVal(v.fmb) + " Available" : "";
    if (v.vl) {
      return v.vl + " (" + v.id + ")" + m;
    }
    return v.id + m;
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

  var dom = {};
  dom.bdy = obj("tbl").getElementsByTagName("tbody")[0];
  dom.actSwp = obj("actSwp");
  dom.noActSwp = obj("noActSwp");
  dom.spcRow = obj("spcRow");
  dom.cfgSwp = obj("cfgSwp");
  dom.actSwpLnk = obj("actSwpLnk");
  dom.chgSwp = obj("chgSwp");
  dom.noCfgSwp = obj("noCfgSwp");
  dom.noDsks = obj("noDsks");
  dom.keyRow = dom.bdy.removeChild(obj("keyRow"));

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var curEnv = new Step("curEnv", obj("curEnv"));
  var actMsg = new Step("actMsg", obj("actMsg"));
  var ugh = new Step("ugh", null);

  ldgMsg.nxt[0] = curEnv;
  ldgMsg.exec = function (i) {
    curEnv.prv = null;

    if (w.cfgSwp == null || w.vols.length < 1) {
      curEnv.o = null;
      dom.chgSwp.innerHTML = "";
      dom.noDsks.css("display", "");
      ldgMsg.o.css("display", "none");
      return;
    }
    dom.noDsks.css("display", "none");

    var kri;

    // Active Swap ----------------------------------------------------------
    if (w.actSwp == null) {
      dom.noActSwp.css("display", "");
    } else {
      dom.noActSwp.css("display", "none");

      // Category Heading
      dom.actSwp.innerHTML = vl(w.vols[l2idx(w.actSwp.vl)]) + ", " + w.actSwp.fn;

      kri = initKeyRow("", "actSwpSpace", "Swap Space",
        dom, dom.bdy, dom.keyRow, dom.spcRow);
      dom[kri].val.innerHTML = (w.actSwp.fmb ? hrVal(w.actSwp.fmb) : "--") +
        " (" + (w.actSwp.mb ? hrVal(w.actSwp.mb) : "--") + " available)";

    }

    // Configured Swap ------------------------------------------------------
    if (! w.cfgSwp.mb && ! w.cfgSwp.vl && ! w.cfgSwp.fn) {
      dom.noCfgSwp.css("display", "");

      // Activate | Edit...
      dom.actSwpLnk.innerHTML = "";
      dom.chgSwp.innerHTML = "Create...";
      dom.chgSwp.att("href",
        "javascript:self.location.replace('/vmware/en/modSwap.html');");
    } else {
      dom.noCfgSwp.css("display", "none");

      // Category Heading
      dom.cfgSwp.innerHTML = vl(w.vols[l2idx(w.cfgSwp.vl)]) + ", " + w.cfgSwp.fn;

      // Activate... | Edit...
      if (w.actSwp == null) {
        dom.actSwpLnk.innerHTML = '<a href="javascript:next(1);">Activate</a> | ';
      }
      dom.chgSwp.att("href",
        "javascript:self.location.replace('/vmware/en/modSwap.html');");

      // Swap Space
      kri = initKeyRow("", "cfgSwpSpace", "Swap Space",
        dom, dom.bdy, dom.keyRow);
      dom[kri].val.innerHTML = w.cfgSwp.mb ? hrVal(w.cfgSwp.mb) : "--";

      // Activation Policy
      kri = initKeyRow("", "cfgSwpPolicy", "Activation Policy",
        dom, dom.bdy, dom.keyRow);

      dom[kri].val.innerHTML = w.cfgSwp.ap ? "Active at system startup" : "Activated manually";
    }
  };

  curEnv.nxt[0] = ugh;
  curEnv.nxt[1] = actMsg;
  curEnv.exec = function (i) {
    if (i == 1) {
      ok = true;
      parent.loadData("/swap?op=activate&ctx=" + parent.ctx);
    } else {
      exit(i);
    }
  };

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  parent.loadData("/swap");
}
