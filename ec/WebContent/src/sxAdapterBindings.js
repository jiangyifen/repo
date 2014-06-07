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
  dom.adpRow = dom.bdy.removeChild(obj("adpRow"));
  objAtt(dom.adpRow, "id", "");
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
    for (var n in data.adapters) {
      dom[n] = {};
      var kri;

      dom[n].hdr = dom.bdy.appendChild(dom.adpRow.cloneNode(true));

      dom[n].lbl = obj("adpLbl");
      dom[n].lbl.att("id", "");

      dom[n].lbl.innerHTML = n + ": " + data.adapters[n].dnm + " (" +
        data.adapters[n].wwpn + ")";

      dom[n].sum = dom.bdy.appendChild(dom.sumRow.cloneNode(true));

      var trgts = 0;
      for (var i in data.adapters[n].trgts) {
        kri = initKeyRow("", i, "SCSI Target ID " + i,
          dom[n], dom.bdy, dom.keyRow);

        dom[n][kri].val.innerHTML = data.adapters[n].trgts[i];

        trgts++;
      }

      if (trgts == 0) {
        kri = initKeyRow("", "noBindings", "None",
          dom[n], dom.bdy, dom.keyRow);
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

  // XXX: Call onload so we guarantee that we move past the ldgMsg, even when
  // this page is the current tab (so parent will not call doUp for us).
  doUp();
}
