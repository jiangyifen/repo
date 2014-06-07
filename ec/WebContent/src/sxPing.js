function initPing(m) {
  // temp iframe pf, timeout id pt; if ping succeeds, call method pm w/data.
  // retry count rc, max retries mr, retry interval ri.
  var pf, pt, pm, rc, mr, ri;
  pm = m;
  rc = 0;
  mr = 70;
  ri = 9000;

  // Stop ping and clean up.
  unping = function () {
    try { window.clearTimeout(pt); } catch (ex) { ; }
  };

  // Start ping. m or pm must be set since it judges ping success.
  ping = function (m) {
    if (rc > mr) {
      if (getElapsedTimeStr) {
        var str = getElapsedTimeStr();
        if (confirm("Elapsed time: More than " + str + ". " +
          "You may need to check your system. Click OK to continue waiting, " +
          "or click Cancel to end the configuration process.")) {
          rc = 0;
        } else {
          parent.close();
        }
      } else {
        return;
      }
    }

    if (m != null) {
      pm = m;
    }

    if (pm == null || typeof pm != "function") {
      throw("No callback method defined.");
    }

    rc++;

    unping();

    if (pf == null) { pf = parent.getTmpFrame(); }
    try {
      pf.doc().location.replace("/sx-ping");
    } catch (ex) {
      try { parent.remTmpFrame(pf.lu); } catch (ex) { ; }
      pf = parent.getTmpFrame();
    }

    pt = window.setTimeout("ping();", ri);
  };

  // Pass ping callback to pm.
  pingCb = function (w) {
    unping();

    if (pm == null || typeof pm != "function") {
      throw("No callback method defined.");
    }

    if (! pm(w.data)) {
      pt = window.setTimeout("ping();", ri);
    } else {
      if (ie) { try { parent.remTmpFrame(pf.lu); } catch (ex) { ; } }
      pf = null;
      pm = null;
    }
  }
}
