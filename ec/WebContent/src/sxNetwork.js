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

  hndlNetNameChng = function (ele, net) {
    net.name = ele.value;
  };

  // Network Adapters -------------------------------------------------------
  rndrNic = function (nic) {
    var id = nic.id;
    dom[id] = new Object();

    // Network Adapter ------------------------------------------------------
    dom[id].nicRow = dom.bdy.insertBefore(dom.nicRow.cloneNode(true), dom.sepRow);
    objAtt(dom[id].nicRow, "id", id);

    dom[id].nicLbl = document.getElementById("nicLbl");
    objAtt(dom[id].nicLbl, "id", "");

    dom[id].nicLbl.innerHTML = nic.name;
  };

  rndrNet = function (net) {
    var lbl = net.dev;

    if (net.type == "hostonly") {
      lbl += " <em>(Host-only)</em>";
    } else if (net.type == "bridged") {
      lbl += " <em>(Bridged)</em>";
    } else if (net.type == "nat") {
      lbl += " <em>(NAT)</em>";
    }

    kri = initKeyRow(net.dev, "net", lbl, dom, dom.bdy, dom.keyRow);

    dom[kri].val.innerHTML = htmlTxtInpt(kri, net.name,
      "hndlNetNameChng(this, '" + net + "');");
  };

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var start = new Step("start", obj("start"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  ldgMsg.nxt[0] = start;
  ldgMsg.exec = function (i) {
    start.prv = null;

    dom.noNics.css("display", w.nics.length == 0 ? "" : "none");
    for (var n in w.nics) {
      rndrNic(w.nics[n]);
    }

    for (var n in w.nets) {
      if (w.nets[n].present) {
        rndrNet(w.nets[n]);
      }
    }

    oq = new Query(document.forms[0]);
    eq = new Query(document.forms[0]);
  };

  function checkForDups() {
    var lbls = {};

    for (var n in w.nets) {
      var kri = w.nets[n].dev + ":net";

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

  parent.loadData("/network/index.pl");
}
