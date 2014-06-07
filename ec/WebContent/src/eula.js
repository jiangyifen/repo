// Original query, edited query, eula iframe
var oq, eq, eula;
var ok = false;


// --------------------------------------------------------------------------

function exit(i) {
  if (parent.ctx == "wizard") {
    if (i == -1) {
      self.location.replace("/vmware/en/esxHostConfigWizardIntro.html");
    } else {
      self.location.replace("/vmware/en/esxHostConfigWizardStartup.html");
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

  if (ok && w.err.length > 0) {
    ok = false;
    if (eula.doc() == null) {
      eula = obj("eula");
      window.setTimeout("eula.doc().location.replace('/eula');", 100);
    }
    prev();
    return;
  }

  if (w.reboot) {
    self.location.replace("/vmware/en/reboot.html");
  } else {
    next();
  }
}


// --------------------------------------------------------------------------

function initPage() {
  var w = parent.op.win();

  var dom = {};

  dom.tbl = obj("tbl");
  dom.bdy = dom.tbl.getElementsByTagName("tbody")[0];
  dom.keyRow = dom.bdy.removeChild(obj("keyRow"));

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  eula = obj("eula");

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;
    // XXX: Some Mozilla based clients return null for eula.doc() if the iframe
    // or its parent css display property is "none". Wait a fraction of a
    // second to ensure that eula's parent (editor) css diplay property will
    // not be "none".
    window.setTimeout("eula.doc().location.replace('/eula');", 100);

    // Make a new Query object to represent the eula form.
    oq = new Query(document.forms[0]);
    // Make a new Query object to represent the completed eula form.
    eq = new Query(document.forms[0]);

    for (var i = 0; i < 2; i++) {
      if (w.sn[i] && w.sn[i].length == 23) {
        var sn = w.sn[i].split("-");
        for (var x = 0; x < sn.length; x++) {
          eq.arg("sn" + (i+1), sn[x], x);
        }

        // Only consider EULA accepted and show license
        // information if base serial number is set
        if (i == 0) {
          eq.select("accept", "1", true);
          if (w.ed != null) {
            kri = initKeyRow("", "ed", "Expiration Date",
              dom, dom.bdy, dom.keyRow);
            dom[kri].val.innerHTML = w.ed;
          }

          if (w.vms != null) {
            kri = initKeyRow("", "vms", "Number of Virtual Machines",
              dom, dom.bdy, dom.keyRow);
            dom[kri].val.innerHTML = w.vms;
          }

          if (w.pcpus != null) {
            kri = initKeyRow("", "pcpus", "Number of Host Processors",
              dom, dom.bdy, dom.keyRow);
            dom[kri].val.innerHTML = w.pcpus;
          }

          if (w.vcpus != null) {
            kri = initKeyRow("", "vcpus", "Number of Processors per Virtual Machine",
              dom, dom.bdy, dom.keyRow);
            dom[kri].val.innerHTML = w.vcpus;
          }
        }
      } else if (i == 0) {
        obj("authLbl").css("display", "none");
      }
    }
  };

  function validate() {
    if (! eq.f.accept.checked) {
      alert("Please read and accept the End User License Agreement before " +
        "continuing.");
      return false;
    }

    return true;
  }

  editor.nxt[0] = svgMsg;
  editor.exec = function () {
    eq.cache();

    if (validate()) {
      if (eq.diff(oq)) {
        ok = true;
        eq.submit();
      } else {
        next();
      }
    } else {
      return false;
    }
  };

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  parent.loadData("/eula?op=info");
}
