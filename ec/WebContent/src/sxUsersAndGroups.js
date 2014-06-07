/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// Original query, edited query
var oq, eq;
var usrs = {};
var grps = {};
var shiftUsrs = [];
var shiftGrps = [];
var cunm, cgnm, uto, gto;
var ok = false;
var TMP_USR_STR = "addUsrTmpUsr";
var TMP_GRP_STR = "addGrpTmpGrp";
var usrsDisplay = "";
var grpsDisplay = "";

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

    if (cunm != null && ! defined(usrs[cunm])) { cunm = TMP_USR_STR; }
    if (cgnm != null && ! defined(grps[cgnm])) { cgnm = TMP_GRP_STR; }

    errPrev();
    return;
  }

  if (ok) {
    var a, s, u = false;
    if (cunm != null) { a = "usrs"; s = cunm; cunm = null; u = true; }
    if (cgnm != null) { a = "grps"; s = cgnm; cgnm = null; }

    // Delete the temporary user or group object when it is added successfully.
    if (usrs[TMP_USR_STR] != null) { delete usrs[TMP_USR_STR]; }
    if (grps[TMP_GRP_STR] != null) { delete grps[TMP_GRP_STR]; }

    var inClient = self[a][s] != null;
    var inServer = false;

    // Grab a copy of the object before it's too late.
    //
    // XXX: Since self[a][s]'s constructor lives in another window that has
    // been reloaded, MSIE will barf while trying to clone the constructor. So
    // while this isn't exactly what we want, it seems to make MSIE happy.
    // --mikol June 26, 2003 8:17 PM
    var tmp = inClient ? (ie ? self[a][s] : clone(self[a][s], true)) : null;

    // Find the object in the server.
    for (var i = 0; i < w[a].length; i++) {
      if (w[a][i].nm == s) {
        inServer = true;
        self[a][s] = w[a][i];

        if (u) {
          if (inClient) { addUsrRow(s, usrGrpsVal(usrs[s], true)); }
        } else {
          if (inClient) { addGrpRow(s, grpUsrsVal(grps[s], true)); }
        }
        break;
      }
    }

    // Catch removed items first.
    if (u) { syncUsr(w, tmp); } else { syncGrp(w, tmp); }

    // Now look for updates.
    //
    // XXX: Since self[a][s]'s constructor lives in another window that has
    // been reloaded, MSIE will barf while trying to clone the constructor. So
    // while this isn't exactly what we want, it seems to make MSIE happy.
    // --mikol June 26, 2003 8:17 PM
    tmp = ie ? self[a][s] : clone(self[a][s], true);
    if (u) { syncUsr(w, tmp); } else { syncGrp(w, tmp); }

    // If the current name is not part of the list on the server, then we need
    // to remove it.
    if (! inServer && inClient) {
      if (u) {
        remUsrRow(s);
      } else {
        remGrpRow(s);
      }
      delete self[a][s];
    }

    // If the current name is not part of the list on the client, then we need
    // to insert it.
    if (inServer && ! inClient) {
      var keys = [];
      for (var n in self[a]) { keys.push(self[a][n].nm); }
      if (u) {
        insertUsrRow(keys.sort(), s);
      } else {
        insertGrpRow(keys.sort(), s);
      }
    }

    // Now sync the menus.
    rndrMenus();

    ok = false;
  }

  next();
}


// --------------------------------------------------------------------------

function initPage() {
  if (parent.op == null) { window.setTimeout(initPage, 50); return; }

  var w = parent.op.win();

  var dom = {};
  dom.bdy = obj("tbl").getElementsByTagName("tbody")[0];
  dom.keyRow = dom.bdy.removeChild(obj("keyRow"));
  dom.sepRow = obj("sepRow");

  dom.usrs = {};
  dom.grps = {};

  dom.modUsr = {};
  dom.modUsr.bdy = obj("modUsrTbl").getElementsByTagName("tbody")[0];
  dom.modUsr.keyRow = dom.modUsr.bdy.removeChild(obj("grpKey"));
  dom.modUsr.sepRow = obj("grpAdd");
  objCss(dom.sepRow, "display", usrsDisplay);
  dom.modUsr.usrNm = obj("usrNm");
  dom.modUsr.grpSlct = obj("grpSlct");
  dom.modUsr.grps = {};

  dom.modGrp = {};
  dom.modGrp.bdy = obj("modGrpTbl").getElementsByTagName("tbody")[0];
  dom.modGrp.keyRow = dom.modGrp.bdy.removeChild(obj("usrKey"));
  dom.modGrp.sepRow = obj("usrAdd");
  dom.modGrp.grpNm = obj("grpNm");
  dom.modGrp.usrSlct = obj("usrSlct");
  dom.modGrp.usrs = {};

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var viewer = new Step("viewer", obj("viewer"));

  var ldgUsr = new Step("ldgUsr", obj("ldgUsr"));
  var modUsr = new Step("modUsr", obj("modUsr"));
  var svgUsr = new Step("svgUsr", obj("svgUsr"));

  var ldgGrp = new Step("ldgGrp", obj("ldgGrp"));
  var modGrp = new Step("modGrp", obj("modGrp"));
  var svgGrp = new Step("svgGrp", obj("svgGrp"));

  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  dom.usrRow = obj("usrRow");
  dom.usrsCtrl = obj("usrsCtrl");
  dom.grpRow = obj("grpRow");
  dom.grpsCtrl = obj("grpsCtrl");

  tglUsrsDisplay = function () {
    if (usrsDisplay == "none") {
      usrsDisplay = "";
    } else {
      usrsDisplay = "none";
    }

    dom.usrsCtrl.att("src", usrsDisplay == "none" ? "../imx/expand.png" : "../imx/collapse.png");
    dom.usrRow.css("display", usrsDisplay);
    dom.sepRow.css("display", usrsDisplay);

    for (var n in dom.usrs) {
      if (ie) {
        objCss(dom.usrs[n].keyRow, "display", usrsDisplay);
      } else {
        window.setTimeout(objCss, 1, dom.usrs[n].keyRow, "display", usrsDisplay);
      }
    }
  }

  tglGrpsDisplay = function () {
    if (grpsDisplay == "none") {
      grpsDisplay = "";
    } else {
      grpsDisplay = "none";
    }

    dom.grpsCtrl.att("src", grpsDisplay == "none" ? "../imx/expand.png" : "../imx/collapse.png");
    dom.grpRow.css("display", grpsDisplay);


    for (var n in dom.grps) {
      if (ie) {
        objCss(dom.grps[n].keyRow, "display", grpsDisplay);
      } else {
        window.setTimeout(objCss, 1, dom.grps[n].keyRow, "display", grpsDisplay);
      }
    }
  }


  // Users ------------------------------------------------------------------

  doModUsr = function (e) {
    cunm = getTrgt(eObj(e)).usr;
    next(1);
  };

  // ------------------------------------------------------------------------
  addUsrGrp = function () {
    if (cunm == null) { throw "No user selected."; }
    if (usrs[cunm] == null) { throw "User not found: " + cunm + "."; }

    var f = document.forms.usrForm;
    var e = f.grpSlct;
    var g = e.options[e.selectedIndex].value;
    var u = usrs[cunm];

    // Reset the control.
    e.options[0].selected = true;
    obj('grpAdd').css('display', 'none');

    if (g == "" || g == -1) { return; }

    if (u.ag == null) { u.ag = []; }

    addUsrGrpRow(g);

    // Don't do anything if the user is already a member of, or is set to be
    // added to, group g.
    if (search(u.grps, g) > -1 || search(u.ag.sort(), g) > -1) { return; }

    // Make sure the user is not set to be removed from group g.
    if (u.rg != null && u.rg.length > 0) {
      var a = [];
      for (var i = 0; i < u.rg.length; i++) {
        if (u.rg[i] != g) {
          a.push(u.rg[i]);
        }
      }
      u.rg = a;
    }

    u.ag.push(g);
  };

  function addUsrGrpRow(g) {
    if (dom.modUsr.grps[g] == null && grps[g] != null) {
      initKeyRow("", g, g, dom.modUsr.grps,
        dom.modUsr.bdy, dom.modUsr.keyRow, dom.modUsr.sepRow);
      dom.modUsr.grps[g].val.innerHTML = grpUsrsVal(grps[g], true);
      dom.modUsr.grps[g].key.ctrl = true;
      dom.modUsr.grps[g].key.grp = g;

      var lnk = obj("docRemUsrGrp");
      lnk.ctrl = true;
      lnk.grp = g;
      lstn(lnk, "click", remUsrGrp);
      lnk.att("id", "");
    }
  }

  // ------------------------------------------------------------------------
  remUsrGrp = function (e) {
    var g = getTrgt(eObj(e)).grp;

    if (cunm == null) { throw "No user selected."; }
    if (usrs[cunm] == null) { throw "User not found: " + cunm + "."; }

    var u = usrs[cunm];

    if (u.rg == null) { u.rg = [] };

    // Get rid of the group row.
    remUsrGrpRow(g);

    // Make sure the user is not set to be added to group g.
    if (u.ag != null && u.ag.length > 0) {
      var a = [];
      for (var i = 0; i < u.ag.length; i++) {
        if (u.ag[i] != g) {
          a.push(u.ag[i]);
        }
      }
      u.ag = a;
    }

    // Only set user u to be removed from group g if u is a member of g.
    if (u.grp == g || search(u.grps, g) > -1) {
      u.rg.push(g);
    }
  };

  function remUsrGrpRow(g) {
    if (dom.modUsr.grps[g].keyRow != null) {
      dom.modUsr.bdy.removeChild(dom.modUsr.grps[g].keyRow);
      delete dom.modUsr.grps[g];
    }
  }

  insertUsrRow = function (a, u) {
    var gt;
    gt = search(a, u) + 1;

    if (gt == a.length) {
      addUsrRow(u, usrGrpsVal(usrs[u], true));
    } else {
      addUsrRow(u, usrGrpsVal(usrs[u], true), dom.usrs[a[gt]].keyRow);
    }
  }

  addUsrRow = function (a, b, sep) {
    if (dom.usrs[a] == null) {
      initKeyRow("", a, a,
        dom.usrs, dom.bdy, dom.keyRow, sep == null ? dom.sepRow : sep);
      main.status = "Adding user: " + a;
      objCss(dom.usrs[a].keyRow, "display", usrsDisplay);
      dom.usrs[a].key.ctrl = true;
      dom.usrs[a].key.usr = a;
      lstn(dom.usrs[a].key, "click", doModUsr);
    }

    dom.usrs[a].val.innerHTML = b;
  }

  // Return a display string of user u's groups; set h to true for html.
  usrGrpsVal = function (u, h) {
    var s = h ? '<span class="note"><span class="note">None</span></span>' : "None";

    if (u.grp) {
      s = u.grp;
    }

    if (u.grps.length > 4 - (u.grp ? 1 : 0)) {
      s += (s ? "; " : "") + u.grps[0] + " ... " + u.grps[u.grps.length-1];
      s += " (" + (u.grps.length + (u.grp ? 1 : 0)) + ")";
    } else if (u.grps.length > 0) {
      s += s ? "; " : "";
      for (var i = 0; i < u.grps.length; i++) {
        s += u.grps[i] + (i < u.grps.length-1 ? ", " : "");
      }
    }

    return s;
  }

  remUsrRow = function (u) {
    dom.bdy.removeChild(dom.usrs[u].keyRow);
    delete dom.usrs[u];
  }

  function rndrUsrs() {
    if (shiftUsrs.length > 0 && shiftUsrs[0] != null) {
      var nm = shiftUsrs[0].nm;
      shiftUsrs.shift();

      addUsrRow(nm, usrGrpsVal(usrs[nm], true));
      setTimeout(rndrUsrs, 1);
    }

    return true;
  }

  syncUsr = function (w, tmp) {
    // Look for groups associated with the current user and update them.
    if (tmp != null && tmp.grps != null) {
      for (var i = 0; i < w.grps.length; i++) {
        var re = new RegExp("^" + w.grps[i].nm + "$");
        var r = grep(tmp.grps, re);
        if (r.length > 0) {
          // Grab the updated group state from the server.
          grps[w.grps[i].nm] = w.grps[i];
          // Update the presentation.
          addGrpRow(w.grps[i].nm, grpUsrsVal(grps[w.grps[i].nm], true));
        }
      }
    }
  };

  // Groups -----------------------------------------------------------------

  doModGrp = function (e) {
    cgnm = getTrgt(eObj(e)).grp;
    next(3);
  };

  // ------------------------------------------------------------------------
  addGrpUsr = function () {
    if (cgnm == null) { throw "No group selected."; }
    if (grps[cgnm] == null) { throw "Group not found: " + cgnm + "."; }

    var f = document.forms.grpForm;
    var e = f.usrSlct;
    var u = e.options[e.selectedIndex].value;
    var g = grps[cgnm];

    // Reset the control.
    e.options[0].selected = true;
    obj('usrAdd').css('display', 'none');

    if (u == "" || u == -1) { return; }

    if (g.au == null) { g.au = []; }

    addGrpUsrRow(u);

    // Don't do anything if the user is already a member of, or is set to be
    // added to, group g.
    if (search(g.usrs, u) > -1 || search(g.au.sort(), u) > -1) { return; }

    // Make sure the user is not set to be removed from group g.
    if (g.ru != null && g.ru.length > 0) {
      var a = [];
      for (var i = 0; i < g.ru.length; i++) {
        if (g.ru[i] != u) {
          a.push(g.ru[i]);
        }
      }
      g.ru = a;
    }

    g.au.push(u);
  };

  function addGrpUsrRow(u) {
    if (dom.modGrp.usrs[u] == null && usrs[u] != null) {
      initKeyRow("", u, u, dom.modGrp.usrs,
        dom.modGrp.bdy, dom.modGrp.keyRow, dom.modGrp.sepRow);
      dom.modGrp.usrs[u].val.innerHTML = usrGrpsVal(usrs[u], true);

      var lnk = obj("docRemGrpUsr");
      lnk.ctrl = true;
      lnk.usr = u;
      lstn(lnk, "click", remGrpUsr);
      lnk.att("id", "");
    }
  }

  // ------------------------------------------------------------------------
  remGrpUsr = function (e) {
    var u = getTrgt(eObj(e)).usr;

    if (cgnm == null) { throw "No group selected."; }
    if (grps[cgnm] == null) { throw "Group not found: " + cgnm + "."; }

    var g = grps[cgnm];

    if (g.ru == null) { g.ru = [] };

    // Get rid of the user row.
    remGrpUsrRow(u);

    // Make sure the user is not set to be added to group g.
    if (g.au != null && g.au.length > 0) {
      var a = [];
      for (var i = 0; i < g.au.length; i++) {
        if (g.au[i] != u) {
          a.push(g.au[i]);
        }
      }
      g.au = a;
    }

    // Only set user u to be removed from group g if u is a member of g.
    if (search(g.usrs, u) > -1) {
      g.ru.push(u);
    }
  };

  function remGrpUsrRow(u) {
    if (dom.modGrp.usrs[u].keyRow != null) {
      dom.modGrp.bdy.removeChild(dom.modGrp.usrs[u].keyRow);
      delete dom.modGrp.usrs[u];
    }
  }

  insertGrpRow = function (a, g) {
    var gt;
    gt = search(a, g) + 1;

    if (gt == a.length) {
      addGrpRow(g, grpUsrsVal(grps[g], true));
    } else {
      addGrpRow(g, grpUsrsVal(grps[g], true), dom.grps[a[gt]].keyRow);
    }
  }

  addGrpRow = function (a, b, sep) {
    if (dom.grps[a] == null) {
      initKeyRow("", a, a,
        dom.grps, dom.bdy, dom.keyRow, sep);
      main.status = "Adding group: " + a;
      objCss(dom.grps[a].keyRow, "display", grpsDisplay);
      dom.grps[a].key.ctrl = true;
      dom.grps[a].key.grp = a;
      lstn(dom.grps[a].key, "click", doModGrp);
    }

    dom.grps[a].val.innerHTML = b;
  }

  // Return a display string of group g's users; set h to true for html.
  grpUsrsVal = function (g, h) {
    var s = h ? '<span class="note"><span class="note">None</span></span>' : "None";

    if (g.usrs.length > 4) {
      s = g.usrs[0] + " ... " + g.usrs[g.usrs.length-1];
      s += " (" + g.usrs.length + ")";
    } else if (g.usrs.length > 0) {
      s = g.usrs.join(", ");
    }

    return s;
  }

  remGrpRow = function (g) {
    dom.bdy.removeChild(dom.grps[g].keyRow);
    delete dom.grps[g];
  }

  function rndrGrps() {
    if (shiftGrps.length > 0 && shiftGrps[0] != null) {
      var nm = shiftGrps[0].nm;
      shiftGrps.shift();

      addGrpRow(nm, grpUsrsVal(grps[nm], true));
      setTimeout(rndrGrps, 1);
    }

    return true;
  }

  syncGrp = function (w, tmp) {
    // Look for users associated with the current group and update them.
    if (tmp != null && tmp.usrs != null) {
      for (var i = 0; i < w.usrs.length; i++) {
        var re = new RegExp("^" + w.usrs[i].nm + "$");
        if (grep(tmp.usrs, re).length > 0) {
          // Grab the updated user state from the server.
          usrs[w.usrs[i].nm] = w.usrs[i];
          // Update the presentation.
          addUsrRow(w.usrs[i].nm, usrGrpsVal(usrs[w.usrs[i].nm], true));
        }
      }
    }
  };

  // ------------------------------------------------------------------------

  rndrMenus = function () {
    var vals = ["",-1];
    var lbls = ["Select a group...","None"];
    for (var x = 0; x < w.grps.length; x++) {
      grps[w.grps[x].nm] = w.grps[x];
      shiftGrps.push(w.grps[x]);

      vals.push(w.grps[x].nm);
      lbls.push(w.grps[x].nm + " . . . . . [" +
        (w.grps[x].usrs.length ? grpUsrsVal(w.grps[x]) : "0") + "]");
    }
    dom.modUsr.grpSlct.innerHTML = htmlSlct("grpSlct", vals, "", lbls,
      "addUsrGrp();");

    var vals = ["",-1];
    var lbls = ["Select a user...","None"];
    for (var x = 0; x < w.usrs.length; x++) {
      usrs[w.usrs[x].nm] = w.usrs[x];
      shiftUsrs.push(w.usrs[x]);

      vals.push(w.usrs[x].nm);
      lbls.push(w.usrs[x].nm + " . . . . . [" + usrGrpsVal(w.usrs[x]) + "]");
    }
    dom.modGrp.usrSlct.innerHTML = htmlSlct("usrSlct", vals, "", lbls,
      "addGrpUsr();");
  };

  ldgMsg.nxt[0] = viewer;
  ldgMsg.exec = function (i) {
    viewer.prv = null;

    rndrMenus();
    rndrUsrs();
    rndrGrps();
  };


  viewer.nxt[0] = null;
  viewer.nxt[1] = ldgUsr;
  viewer.nxt[2] = modUsr;
  viewer.nxt[3] = ldgGrp;
  viewer.nxt[4] = modGrp;
  viewer.exec = function (i) {
    if (i == 1 || i == 3) { window.setTimeout(next, 1, 0); }

    // New User
    if (i == 2) {
      cunm = TMP_USR_STR;
      var Usr = parent.op.win().Usr;
      usrs[cunm] = new Usr("","","",[]);
      var u = usrs[cunm];
      dom.modUsr.usrNm.innerHTML = "New User";

      // Show the user name field.
      obj("unm").css("display", "");
      // But hide the remove user link.
      obj("remUsr").css("display", "none");

      // Remove any group rows from prior user edits.
      for (var g in dom.modUsr.grps) {
        remUsrGrpRow(g);
      }

      // Fill the form
      oq = new Query(document.forms.usrForm);
      oq.arg("op", "addUsr");
      oq.arg("nm", "");
      oq.arg("hd", "");
      oq.arg("pw", "");
      oq.arg("pwc", "");
      oq.arg("rg", "");
      oq.arg("ag", "");

      // Make a new Query object to represent the modified form.
      eq = new Query(document.forms.usrForm);
      eq.sync(oq);
    }

    // New Group
    if (i == 4) {
      cgnm = TMP_GRP_STR;
      var Grp = parent.op.win().Grp;
      grps[cgnm] = new Grp("",[]);
      var g = grps[cgnm];
      dom.modGrp.grpNm.innerHTML = "New Group";

      // Show the group name field.
      obj("gnm").css("display", "");
      // But hide the remove group link.
      obj("remGrp").css("display", "none");

      // Remove any user rows from prior group edits.
      for (var u in dom.modGrp.usrs) {
        remGrpUsrRow(u);
      }

      // Fill the form
      oq = new Query(document.forms.grpForm);
      oq.arg("op", "addGrp");
      oq.arg("nm", "");
      oq.arg("ru", "");
      oq.arg("au", "");

      // Make a new Query object to represent the modified form.
      eq = new Query(document.forms.grpForm);
      eq.sync(oq);
    }
  };

  ldgUsr.nxt[0] = modUsr;
  ldgUsr.exec = function (i) {
    var u = usrs[cunm];
    dom.modUsr.usrNm.innerHTML = u.nm;

    // Hide the user name field.
    obj("unm").css("display", "none");
    // And show the remove user link.
    obj("remUsr").css("display", "");

    // Remove any group rows from prior user edits.
    for (var g in dom.modUsr.grps) {
      remUsrGrpRow(g);
    }
    // Including any stray add group control:
    addUsrGrp();

    // Fill the form
    oq = new Query(document.forms.usrForm);
    oq.arg("op", "setUsr");
    oq.arg("nm", u.nm);
    oq.arg("hd", u.hd);
    oq.arg("pw", "");
    oq.arg("pwc", "");
    oq.arg("rg", "");
    oq.arg("ag", "");
    oq.arg("grpSlct", "");

    // Fill in groups
    if (u.grp) { addUsrGrpRow(u.grp); }
    for (var x = 0; x < u.grps.length; x++) {
      addUsrGrpRow(u.grps[x]);
    }

    // Make a new Query object to represent the modified form.
    eq = new Query(document.forms.usrForm);
    eq.sync(oq);

    modUsr.prv = viewer;
  };

  function validateUsrForm() {
    var f = document.forms.usrForm;

    if (f.pw.value != f.pwc.value) {
      alert("New password is different than confirmed password.");
      return false;
    }

    return true;
  }

  modUsr.nxt[0] = svgUsr;
  modUsr.nxt[1] = svgUsr;
  modUsr.exec = function (i) {
    if (i == 1) {
      if (confirm("Remove user: " + cunm + "?")) {
        eq = new Query();
        eq.action("/sx-users");
        eq.target(parent.op.win());
        eq.arg("op", "remUsr");
        eq.arg("nm", cunm);

        ok = true;
        eq.submit();
      } else {
        return false;
      }

      return;
    }

    eq.cache();

    if (usrs[cunm].ag != null) {
      eq.arg("ag", usrs[cunm].ag.join(","));
    }

    if (usrs[cunm].rg != null) {
      eq.arg("rg", usrs[cunm].rg.join(","));
    }

    if (eq.diff(oq)) {
      if (validateUsrForm()) {
        if (cunm == TMP_USR_STR) {
          cunm = eq.arg("nm")[0];
        }

        ok = true;
        eq.submit();
      } else {
        return false;
      }
    } else {
      window.setTimeout(next, 250, 0);
    }
  };

  ldgGrp.nxt[0] = modGrp;
  ldgGrp.exec = function (i) {
    var g = grps[cgnm];
    dom.modGrp.grpNm.innerHTML = g.nm;

    // Hide the group name field.
    obj("gnm").css("display", "none");
    // And show the remove group link.
    obj("remGrp").css("display", "");

    // Remove any user rows from prior group edits.
    for (var u in dom.modGrp.usrs) {
      remGrpUsrRow(u);
    }
    // Including any stray add user control:
    addGrpUsr();

    // Fill the form
    oq = new Query(document.forms.grpForm);
    oq.arg("op", "setGrp");
    oq.arg("nm", g.nm);
    oq.arg("ru", "");
    oq.arg("au", "");
    oq.arg("usrSlct", "");

    // Fill in groups
    for (var x = 0; x < g.usrs.length; x++) {
      addGrpUsrRow(g.usrs[x]);
    }

    // Make a new Query object to represent the modified form.
    eq = new Query(document.forms.grpForm);
    eq.sync(oq);

    modGrp.prv = viewer;
  };

  modGrp.nxt[0] = svgGrp;
  modGrp.nxt[1] = svgGrp;
  modGrp.exec = function (i) {
    if (i == 1) {
      if (confirm("Remove Group: " + cgnm + "?")) {
        eq = new Query();
        eq.action("/sx-users");
        eq.target(parent.op.win());
        eq.arg("op", "remGrp");
        eq.arg("nm", cgnm);

        ok = true;
        eq.submit();
      } else {
        return false;
      }

      return;
    }

    eq.cache();

    if (grps[cgnm].au != null) {
      eq.arg("au", grps[cgnm].au.join(","));
    }

    if (grps[cgnm].ru != null) {
      eq.arg("ru", grps[cgnm].ru.join(","));
    }

    if (eq.diff(oq)) {
      if (cgnm == TMP_GRP_STR) {
        cgnm = eq.arg("nm")[0];
      }

      ok = true;
      eq.submit();
    } else {
      window.setTimeout(next, 250, 0);
    }
  };

  svgMsg.nxt[0] = svgUsr.nxt[0] = svgGrp.nxt[0] = viewer;

  svgMsg.exec = svgUsr.exec = svgGrp.exec = function (i) {
    viewer.prv = null;
  };

  var curStep = ldgMsg;

  errPrev = function () {
    curStep = curStep.slct(-1);
  };

  prev = function () {
    // Make sure to zero out selections if an edit is cancelled.
    cunm = cgrp = null;
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  parent.loadData("/sx-users");
}
