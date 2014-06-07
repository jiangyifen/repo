var args = new Array();
var ok = false;

// --------------------------------------------------------------------------

function exit(i) {
  if (parent.ctx == "editor") {
    parent.tglBtns(false);
  }
  // XXX: Netscape 7.02 won't execute the following code if it is not called
  // from the timeout. -- mikol August 21, 2003 7:05 PM
  window.setTimeout('self.location.replace("storageConfiguration.html")', 0);
}


// --------------------------------------------------------------------------

function argStr(p) {
  var u = "";

  for (var n in args) {
    u += (u ? "&" : "?") + n + "=" + args[n];
  }

  return p + u;
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
       ok = false;
       prev();
     }
     return;
  }
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

  if (parent.ctx != "editor") {
    obj("pageHdr").css("display", "");
    objCss(document.body, "margin", 9);
  }

  var lq = new Query(location.search);

  // Don't offer the choice to create a logical core dump partition.
  var needCdp = lq.arg("dump")[0] == "true";

  // Do not resize this iframe unless its document is too tall to fit without
  // scrolling.
  self.rdh = self.dh;
  dh = function () {
    var rh = self.rdh();
    if (rh <= parent.DH) {
      return parent.DH;
    }
    parent.DH = rh;
    return rh;
  }

  args["op"] = "typical";
  args["dump"] = needCdp;
  args["sc"] = getArg("sc");
  args["ec"] = getArg("ec");
  args["disk"] = getArg("disk");

  var oq;

  var dom = {};
  dom.vlRow = obj("vlRow");
  dom.bsRow = obj("bsRow");
  dom.caRow = obj("caRow");
  dom.tc = obj("tc");
  dom.vl = obj("vl");

  var check = new Step("check", obj("check"));
  var start = new Step("start", obj("start"));
  var cdpq = new Step("cdpq", obj("cdpq"));
  var editor = new Step("editor", obj("editor"));
  var finish = new Step("finish", obj("finish"));

  updateBtns = function () {
    switch (curStep.n) {
      case "check":
      case "start":
        parent.slctBtns("wizNoBack");
        break;
      case "cdpq":
      case "editor":
      case "finish":
        parent.slctBtns("wizNoNext");
        break;
      default:
        parent.tglBtns(false);
    }
  }

  check.nxt[0] = lq.arg("op")[0] == "alien" ? editor : start;
  check.exec = function () {
    check.nxt[0].prv = null;

    if (parent.ctx == "editor") {
      parent.tglBtns(true);
      window.setTimeout(updateBtns, 250);
      window.setTimeout(parent.layoutCb, 25);
    }

    if (lq.arg("op")[0] == "alien") {
      fillForm();
    }
  };

  // Verify that the supplied label does not already exist.
  function isUnique(vl) {
    // We don't have the necessary data to validate the volume label.
    if (w == null || w.dsks == null) { return true; }

    for (var i = 0; i < w.dsks.length; i++) {
      for (var j = 0; j < w.dsks[i].pt.length; j++) {
        if (w.dsks[i].pt[j].vl != "" && w.dsks[i].pt[j].vl == vl) {
          alert("Volume label is not unique: " + vl + ".");
          return false;
        }
      }
    }

    return true;
  }

  // Yes is the first choice, No is the second.
  cdpq.nxt[0] = finish;
  cdpq.nxt[1] = finish;
  cdpq.exec = function (i) {
    args["dump"] = i == 0;

    var vl = "";
    for (;;) {
      vl = prompt("Please provide a unique label for your new VMFS volume:",
        "");

      if (vl == null) { return false; }

      // Make sure that the volume label does not contain illegal characters.
      var ch = vl.match(/:|\//);
      if (! ch) {
        // Make sure that the label is unique.
        if (! isUnique(vl)) {
          continue;
        }

        break;
      }
      alert("Character (" + ch + ") cannot be used in a VMFS volume label.");
    }

    if (vl != null) {
      args["vl"] = esc(vl);
    }

    ok = true;
    var u = argStr("/storage");
    parent.op.win().location.replace(u);
    return true;
  };

  adjustTcWidth = function () {
    if (dom.vl.dim().w > dom.tc.dim().w) {
      dom.tc.dim(dom.vl.dim().w, dom.tc.dim().h);
    }
  }

  function fillForm() {
    var w = parent.op.win();
    oq = new Query(document.forms[0]);

    oq.arg("disk", getArg("disk"));
    oq.arg("sc", getArg("sc"));
    oq.arg("ec", getArg("ec"));

    // Capacity -----------------------------------------------------------
    oq.arg("mb", hrVal(getArg("mb"), 1, "m", "m", 1024,
      ["", "", "", "", ""]));

    // File System --------------------------------------------------------
    var hasExtdPart = false;
    var dskid = lq.arg("disk")[0];
    for (var i = 0; i < w.dsks.length; i++) {
      if (dskid == w.dsks[i].id) {
        for (var j = 0; j < w.dsks[i].pt.length; j++) {
          if (w.isExtd(w.dsks[i].pt[j].tc)) {
            hasExtdPart = true;
            break;
          }
        }
        break;
      }
    }

    if (! needCdp || hasExtdPart) {
      var opts = oq.f.tc.options;
      for (var i = 0; i < opts.length; i++) {
        if ((! needCdp && opts[i].value == w.tc.vmkd) ||
          (hasExtdPart && w.isExtd(opts[i].value))) {
          opts[i] = null;
          i--;
        }
      }
    }

    if (oq.f.tc.options.length <= 1) {
      oq.f.tc.disabled = true;
    }

    if (lq.arg("op")[0] == "alien") {
      window.setTimeout('slctFs("0xfb");', 50);
    } else if (needCdp) {
      window.setTimeout('slctFs("0xfc");', 50);
    }

    // Maximum File Size --------------------------------------------------
    var fs = "vmfs2";
    var cbs = 1;

    // Current block size is less than max block size for the file system.
    while (cbs <= w.bsl[fs]) {
      // Current block size times block size modifier for the file system.
      var lbl = hrVal(cbs * w.bsm[fs], 2, "g");
      oq.f.bs.options[oq.f.bs.options.length] = new Option(lbl, cbs,
        false, false);
      cbs = cbs * 2;
    }

    // Access Mode --------------------------------------------------------
    for (var p in w.ca) {
      oq.f.ca.options[oq.f.ca.options.length] = new Option(w.ca[p], p,
        false, false);
    }

    window.setTimeout(adjustTcWidth, 25);
  }

  // Typical is the first choice, Custom is the second.
  if (needCdp) {
    start.nxt[0] = cdpq;
  } else {
    start.nxt[0] = finish;
  }
  start.nxt[1] = editor;
  start.exec = function (i) {
    // Typical --------------------------------------------------------------
    if (i == 0) {
      args["op"] = "typical";
      if (! needCdp && !cdpq.exec(1))
        return false;
    }

    // Custom ---------------------------------------------------------------
    if (i == 1) {
      fillForm();
    }

    if (parent.ctx == "editor") {
      window.setTimeout(parent.layoutCb, 25);
    }
  };


  function showNote(note) {
    var element = obj("note");
    element.innerHTML = note;
    objAtt(element, "class", "note");
    objCss(element, "display", "");
  }


  // Validate form fields before submission.
  var availableCapacity = 0;
  var requestedCapacity = 0;
  function validate() {
    availableCapacity = new Number(lq.arg("mb")[0]).toFixed(1);

    var mb = oq.arg("mb")[0];

    if (mb) {
      var ch = mb.match(/\D/);
      if (ch && ch != '.') {
         alert("Non digit character (" + ch + ") cannot be used for Capacity.");
         oq.f.mb.select();
         showNote("(Available capacity: " + availableCapacity + "M)");
         return false;
      }
      requestedCapacity = parseFloat(mb);
    }
    if (!mb || requestedCapacity == 0) {
      alert("Please enter a value" +
            (mb? " greater than zero " : " ") +
            "for the Capacity field.");
      oq.f.mb.select();
      showNote("(Available capacity: " + availableCapacity + "M)");
      return false;
    }
    if (requestedCapacity > availableCapacity) {
      alert("Capacity (" + requestedCapacity +
            " M) exceeds available free space ("
            + availableCapacity + " M).");
      oq.f.mb.select();
      showNote("(Available capacity: " + availableCapacity + "M)");
      return false;
    }
    // Make sure that the label is unique.
    if (! isUnique(oq.arg("vl")[0])) {
      return false;
    }
    // XXX add more validation as required.
    return true;
  };

  editor.nxt[0] = finish;
  editor.exec = function () {
    if (lq.arg("op")[0] == "alien") {
      oq.arg("op", "alien");
      oq.arg("part", lq.arg("part")[0]);
    }
    oq.cache();
    if (validate() &&
        (lq.arg("op")[0] != "alien" ||
        confirm('Converting this volume will destroy all the data it ' +
                'contains. Convert volume "' + oq.arg("part")[0] + '"?'))) {
      oq.f.tc.disabled = false;
      if (requestedCapacity == availableCapacity) {
        // XXX There seems to be rounding errors that can result in
        // bogus free space information to be reported. Ref. bug 31718.
        // Avoid the line, my $cylinderLength = int($mbCapacity * 1024 / $kpc);
        // in StorageConfiguration.pm, that is responsible for the rounding
        // error by nulling out the form field when the requested capacity
        // is not different from available capacity.
        oq.arg("mb", null);
      }
      oq.submit();
      ok = true;
      return true;
    }
    return false;
  };

  var curStep = check;

  prev = function () {
    curStep = curStep.slct(-1);
    if (parent.ctx == "editor") {
      window.setTimeout(updateBtns, 250);
    }
  };

  next = function (i) {
    curStep = curStep.slct(i);
    if (parent.ctx == "editor") {
      window.setTimeout(updateBtns, 250);
    }
  };

  slctFs = function (v) {
    oq.cache("tc");
    if (v == null) {
      v = oq.arg("tc")[0];
    } else {
      oq.arg("tc", v);
    }

    switch (v) {
      case "0xfb":
        dom.vlRow.css("display", "");
        dom.bsRow.css("display", "");
        dom.caRow.css("display", "");
        oq.f.mb.disabled = lq.arg("op")[0] == "alien" ? true : false;
        oq.f.tc.disabled = lq.arg("op")[0] == "alien" ? true :
          (oq.f.tc.options.length <= 1 ? true : false);
        oq.arg("mb", hrVal(getArg("mb"), 1, "m", "m", 1024,
          ["", "", "", "", ""]));
        break;
      case "0xfc":
        dom.vlRow.css("display", "none");
        dom.bsRow.css("display", "none");
        dom.caRow.css("display", "none");
        oq.arg("mb", hrVal(getArg("defCdpMb"), 1, "m", "m", 1024,
          ["", "", "", "", ""]));
        oq.f.mb.disabled = true;
        break;
      case "0x05":
        dom.vlRow.css("display", "none");
        dom.bsRow.css("display", "none");
        dom.caRow.css("display", "none");
        oq.f.mb.disabled = false;
        oq.arg("mb", hrVal(getArg("mb"), 1, "m", "m", 1024,
          ["", "", "", "", ""]));
        break;
    }

    if (parent.ctx == "editor") {
      window.setTimeout(parent.layoutCb, 25);
    }
  }

  doUp = function () {
    parent.tglBtns(true);
    parent.layoutCb();
  };

  doDn = function () {
    parent.tglBtns(false);
  };

  next();
}
