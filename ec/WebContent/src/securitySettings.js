// Normal config, high config, low config, original query, edited query
var nq, hq, lq, oq, eq;
var ok = false;


// --------------------------------------------------------------------------

function exit(i) {
  if (parent.ctx == "wizard") {
    if (i == -1) {
      self.location.replace("/vmware/en/networkConnections.html");
    } else {
      main.logout("Your configuration is complete. " +
        "Please log in to use the Management Interface.", true);
      parent.close();
    }
  } else {
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

  if (ok) {
    if (w.err.length > 0) {
      ok = false;
    } else {
      // Sleep awhile to allow Apache to restart.
      setTimeout('next()', 15000);
    }
    return;
  }

  next();
}


// --------------------------------------------------------------------------

function initPage() {
  var w = parent.op.win();

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var simple = new Step("simple", obj("simple"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  var dom = new Object();
  dom.normal = obj("normal");
  dom.high = obj("high");
  dom.low = obj("low");
  dom.custom = obj("custom");

  ldgMsg.nxt[0] = simple;
  ldgMsg.exec = function (i) {
    simple.prv = null;

    // Normal Configuration -------------------------------------------------
    nq = new Query(document.forms[0]);
    nq.select("https", "no", false);
    nq.select("rcssl", "no", false);
    nq.select("ssh", "yes", true);
    nq.select("ftp", "yes", true);
    nq.select("telnet", "yes", true);
    nq.select("nfs", "yes", true);

    // High Configuration ---------------------------------------------------
    hq = new Query(document.forms[0]);
    hq.select("https", "no", false);
    hq.select("rcssl", "no", false);
    hq.select("ssh", "yes", true);
    hq.select("ftp", "yes", false);
    hq.select("telnet", "yes", false);
    hq.select("nfs", "yes", false);

    // Low Configuration ----------------------------------------------------
    lq = new Query(document.forms[0]);
    lq.select("https", "no", true);
    lq.select("rcssl", "no", true);
    lq.select("ssh", "yes", true);
    lq.select("ftp", "yes", true);
    lq.select("telnet", "yes", true);
    lq.select("nfs", "yes", true);

    // Current Configuration ------------------------------------------------
    oq = new Query(document.forms[0]);
    oq.select("https", "no", !w.lvl.https);
    oq.select("rcssl", "no", !w.lvl.rcssl);
    oq.select("ssh", "yes", w.lvl.ssh);
    oq.select("ftp", "yes", w.lvl.ftp);
    oq.select("telnet", "yes", w.lvl.telnet);
    oq.select("nfs", "yes", w.lvl.nfs);

    // Modified Configuration -----------------------------------------------
    eq = new Query(document.forms[0]);

    hilite();
  };

  hilite = function () {
    dom.normal.innerHTML = "Medium";
    dom.high.innerHTML = "High";
    dom.low.innerHTML = "Low";
    dom.custom.innerHTML = "Custom";

    if (! eq.diff(nq)) {
      dom.normal.innerHTML = "<b>Medium</b>";
    } else if (! eq.diff(hq)) {
      dom.high.innerHTML = "<b>High</b>";
    } else if (! eq.diff(lq)) {
      dom.low.innerHTML = "<b>Low</b>";
    } else {
      dom.custom.innerHTML = "<b>Custom</b>";
    }
  };

  slct = function (q) {
    eq.sync(q);
    hilite();
  };

  simple.nxt[0] = svgMsg;
  simple.nxt[1] = editor;
  simple.exec = function (i) {
    var a = eq.arg();
    for (var j = 0; j < a.length; j++) {
      eq.dump(a[j]);
    }

    if (i == 0) {
      eq.diff(oq) ? eq.submit() : exit();
    }
  };

  editor.nxt[0] = simple;
  editor.exec = function () {
    simple.prv = null;
    var a = eq.arg();
    for (var i = 0; i < a.length; i++) {
      eq.cache(a[i]);
    }

    hilite();
  };

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  parent.loadData("/security");
}
