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

  if (ok) {
    ok = false;
    return;
  }

  next();
}


// --------------------------------------------------------------------------

function initPage() {
  if (parent.ready == null || ! parent.ready()) { window.setTimeout(initPage, 50); return; }

  var w = parent.sanOp.win();
  var data;

  var dom = {};
  dom.bdy = obj("tbl").getElementsByTagName("tbody")[0];
  dom.lunRow = dom.bdy.removeChild(obj("lunRow"));
  objAtt(dom.lunRow, "id", "");
  dom.sumRow = dom.bdy.removeChild(obj("sumRow"));
  objAtt(dom.sumRow, "id", "");
  dom.keyRow = dom.bdy.removeChild(obj("keyRow"));
  objAtt(dom.keyRow, "id", "");
  dom.sepRow = dom.bdy.removeChild(obj("sepRow"));
  objAtt(dom.sepRow, "id", "");
  dom.noAdptrs = obj("noAdptrs");

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var viewer = new Step("viewer", obj("viewer"));

  ldgMsg.nxt[0] = viewer;
  ldgMsg.exec = function (idx) {
    if (! defined(parent.sanData)) {
      data = parent.sanData = clone(w.data);
    } else {
      data = parent.sanData;
    }

    var cnt = 0;
    for (var n in data.luns) {
      dom[n] = {};
      var kri, enbld, dsbld, brkn, html;

      dom[n].hdr = dom.bdy.appendChild(dom.lunRow.cloneNode(true));

      dom[n].lbl = obj("lunLbl");
      dom[n].lbl.att("id", "");

      enbld = dsbld = brkn = 0;
      for (var i = 0; i < data.luns[n].length; i++) {
        if (data.luns[n][i].brkn) {
          brkn++;
        } else if (! data.luns[n][i].enbld) {
          dsbld++;
        } else {
          enbld++;
        }
      }
      html = "SAN LUN " + n + " (" + data.luns[n].length + " Paths";
      html += brkn ? ": " + brkn + " Broken" : "";
      html += dsbld ? (brkn ? ", " : ": ") + dsbld + " Disabled" : "";
      html += ", Policy: " + data.plcy[n];
      html += ")";
      dom[n].lbl.innerHTML = html;

      dom[n].sum = dom.bdy.appendChild(dom.sumRow.cloneNode(true));

      for (var i = 0; i < data.luns[n].length; i++) {
        kri = initKeyRow("", data.luns[n][i].id, data.luns[n][i].id,
          dom[n], dom.bdy, dom.keyRow);

        dom[n][kri].val.innerHTML = data.luns[n][i].trgt;

        dom[n][kri].ico = obj("ico");
        dom[n][kri].ico.att("id", "");
        var src = "../imx/info.holder-10x10.png";
        if (data.luns[n][i].actv) { src = "../imx/info-10x10.png"; }
        if (! data.luns[n][i].enbld) { src = "../imx/warning-10x10.png"; }
        if (data.luns[n][i].brkn) { src = "../imx/error-10x10.png"; }
        dom[n][kri].ico.att("src", src);

        dom[n][kri].prfrd = obj("prfrd");
        dom[n][kri].prfrd.att("id", "");
        dom[n][kri].prfrd.innerHTML =
          data.luns[n][i].prfrd && data.plcy[n] == "fixed" ? "Preferred" : "";
      }

      if (main.admin) {
        dom[n].edit = obj("edit");
        dom[n].edit.att("id", "");
        dom[n].edit.innerHTML = "Edit...";
        objAtt(dom[n].edit, "href", "javascript:editFailoverPaths('" + n + "');");
      }

      dom[n].sep = dom.bdy.appendChild(dom.sepRow.cloneNode(true));

      cnt++;
    }

    if (cnt == 0) {
      dom.noAdptrs.css("display", "");
    } else {
      dom.noAdptrs.css("display", "none");
    }

    if (parent.page.att("id") == self.name) {
      parent.setTimeout("layoutCb();", 50);
    }
  };

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  doUp = function () {
    parent.layoutCb();
    if (curStep.n == "ldgMsg") {
      if (! defined(parent.sanData)) {
        w.location.replace("/sx-san");
      } else {
        next();
      }
    }
  }

  editFailoverPaths = function (lun) {
    var q = new Query();
    q.action("/vmware/en/sxEditFailoverPaths.html");
    q.arg("lun", lun);
    q.arg("plcy", data.plcy[lun]);
    self.location.replace(q.toString());
  }

  // XXX: Call onload so we guarantee that we move past the ldgMsg, even when
  // this page is the current tab (so parent will not call doUp for us).
  doUp();
}
