// Original query, edited query
var oq, eq;
var ok = false;
var button = null;


// --------------------------------------------------------------------------

function exit() {
  if (parent.ctx == "editor") {
    parent.tglBtns(false);
  }

  if (button) {
    // Reset button to its original value
    parent.obj("btns").win().obj("ok").innerHTML = button;
    button = null;
  }

  // XXX: Netscape 7.02 won't execute the following code if it is not called
  // from the timeout. -- mikol August 21, 2003 7:05 PM
  window.setTimeout('self.location.replace("storageConfiguration.html")', 0);
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
    prev();
    return;
  }

  next();
}


// --------------------------------------------------------------------------

function hrPartStr(p) {
  var a = p.split(":");
  p = a.pop();

  return "volume " + p + " from disk " + a.join(":");
}


// --------------------------------------------------------------------------

function initPage() {
  if (parent.ctx != "editor") {
    obj("pageHdr").css("display", "");
    objCss(document.body, "margin", 9);
  }

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


  var w = parent.op.win();
  oq = new Query(location.search);

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));
  var cvtMsg = new Step("cvtMsg", obj("svgMsg"));
  var dltMsg = new Step("dltMsg", obj("svgMsg"));

  var dom = new Object();
  dom.bdy = obj("tbl").getElementsByTagName("tbody")[0];
  dom.dskLbl = obj("dskLbl");
  dom.prtLbl = obj("prtLbl");
  dom.actLbl = obj("actLbl");
  dom.keyRow = dom.bdy.removeChild(obj("keyRow"));
  dom.spnCat = dom.bdy.removeChild(obj("spnCat"));
  dom.avlCat = dom.bdy.removeChild(obj("avlCat"));
  dom.exNote = dom.bdy.removeChild(obj("exNote"));
  dom.note   = obj("note");

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;

    // Disk Header ----------------------------------------------------------
    dom.dskLbl.innerHTML = "Disk " + oq.arg("disk")[0];

    // Partition Header -----------------------------------------------------
    dom.prtLbl.innerHTML = "Volume " + oq.arg("part")[0];

    // canDelete ----------------------------------------------------------
    //
    // XXX: Determines whether or not partition pt can be deleted. If pt is the
    // last logical partition or a phsyical partition, then it can be deleted.
    // Temporarily required until the disk partitioning back end is able to
    // delete and reorder logical partitions without losing data.
    function initCanDelete() {
      var re = /(.*):(\d+)$/;
      var FIRST_LOGICAL_PART_NUM = 5;

      function getDiskById(id) {
        for (var d in w.dsks) {
          if (w.dsks[d].id == id) {
            return w.dsks[d];
          }
        }

        return null;
      }

      canDelete = function (pt) {
        var m = pt.id.match(re);

        if (m == null) { return false; }
        if (m[2] < FIRST_LOGICAL_PART_NUM) { return true; }

        var dsk = getDiskById(m[1]);
        if (dsk == null) { return false; }

        var logicalParts = [];
        for (p in dsk.pt) {
          var mm = dsk.pt[p].id.match(re);
          if (mm != null && mm[2] >= FIRST_LOGICAL_PART_NUM) {
            logicalParts.push(mm[2]);
          }
        }

        return m[2] - logicalParts.length == 4;
      };
    }
    initCanDelete();

    // For an unformatted partion sp.ex may not get populated.
    // Provide the option to remove an unformatted partion.
    if (oq.arg("sp.ex").length <= 1) {
      if (canDelete(getVmfsInfo())) {
        dom.actLbl.innerHTML = "Remove...";
        var op = "next(2);";
        objAtt(dom.actLbl, "href", "javascript:" + op);
      } else {
        dom.actLbl.innerHTML = "Remove...";
        objAtt(dom.actLbl, "class", "dsbld");
        var msg = "Logical volumes must be removed in order from last to first.";
        objAtt(dom.actLbl, "href", "javascript:alert('" + msg + "');");
      }
    }

    var formatted = oq.arg("fs")[0] == "true" ? true : false;
    if (!formatted) {
      // Save the original value of the button for post formatting.
      var origVal = parent.obj("btns").win().obj("ok");
      // Note: If the "OK" button is not present then we are
      // in the host configuration wizard. One should be able
      // to format  the unformatted partition by clicking the
      // Next button. - Fix bug 32946
      if (origVal) {
        button = origVal.innerHTML;
        // Make user aware of an unformatted partition.
        origVal.innerHTML = "Format Volume";
      } else {
        dom.note.css("display", "");
      }
    }

    // Volume Label ---------------------------------------------------------
    kri = initKeyRow("", "vl", "Volume Label", dom, dom.bdy, dom.keyRow);
    var m = "eq.cache('" + kri + "')";
    dom[kri].val.innerHTML = htmlTxtInpt(kri, oq.arg("vl")[0], m);
    objAtt(dom[kri].keyTd, "class", "slctKey");
    objAtt(dom[kri].valTd, "class", "slctVal");

    // File System ----------------------------------------------------------
    kri = initKeyRow("", "fs", "File System", dom, dom.bdy, dom.keyRow);
    if (oq.arg("tn")[0].toUpperCase() == "VMFS-1") {
      var js = "next(1)";
      dom[kri].val.innerHTML = oq.arg("tn")[0] + " | " +
        '<a href="javascript:;" onclick="' + js + '">Convert to VMFS-2...</a>';
    } else {
      dom[kri].val.innerHTML = oq.arg("tn")[0];
    }

    // Access Mode ----------------------------------------------------------
    if (oq.arg("tc")[0] == w.tc.vmfs) {
      kri = initKeyRow("", "ca", "Access Mode", dom, dom.bdy, dom.keyRow);
      var vals = [];
      var lbls = [];
      for (var p in w.ca) {
        vals.push(p);
        lbls.push(w.ca[p]);
      }
      var m = "eq.cache('" + kri + "')";
      dom[kri].val.innerHTML = htmlSlct(kri, vals, oq.arg("ca")[0], lbls, m);
      objAtt(dom[kri].keyTd, "class", "slctKey");
      objAtt(dom[kri].valTd, "class", "slctVal");
    }

    // Block Size -----------------------------------------------------------
    if (oq.arg("tc")[0] == w.tc.vmfs) {
      kri = initKeyRow("", "bs", "Maximum File Size", dom, dom.bdy, dom.keyRow);

      var fs = oq.arg("tn")[0].toUpperCase() == "VMFS-1" ? "vmfs1" : "vmfs2";
      var ms = formatted ? oq.arg("bs")[0] * w.bsm[fs] : 0;
      if (w.ams[fs] && ms > w.ams[fs]) {
        ms = w.ams[fs];
      }

      if (oq.arg("sp.ex").length == 1 ||
          !formatted) { // Give user a chance to format an unformatted partition
        var vals = [];
        var lbls = [];
        var cbs = 1;
        while (cbs <= w.bsl[fs]) {
          vals.push(cbs);
          var lbl = cbs * w.bsm[fs];
          if (w.ams[fs] && lbl > w.ams[fs]) {
            ms = w.ams[fs];
          }
          lbls.push(hrVal(lbl, 2, "g"));
          cbs = cbs * 2;
        }

        // It's possible that a file system will be formatted with a block size
        // larger than we are prepared to proffer in the MUI. If that is the
        // case, append the rogue value to the pulldown.
        if (oq.arg("bs")[0] > w.bsl[fs]) {
          vals.push(oq.arg("bs")[0]);
          lbls.push(hrVal(ms, 2, "g"));
        }

        var m = "eq.cache('" + kri + "')";
        dom[kri].val.innerHTML = htmlSlct(kri, vals, oq.arg("bs")[0], lbls, m);
        objAtt(dom[kri].keyTd, "class", "slctKey");
        objAtt(dom[kri].valTd, "class", "slctVal");
      } else {
        dom[kri].val.innerHTML = hrVal(ms, 2, "g");
      }
    }

    // Capacity -------------------------------------------------------------
    kri = initKeyRow("", "mb", "Capacity", dom, dom.bdy, dom.keyRow);
    dom[kri].val.innerHTML = hrVal(oq.arg("mb")[0], 2);

    // Spanning Information -------------------------------------------------
    if (oq.arg("tn")[0] == "VMFS-2" && oq.arg("sp.ex").length >= 1) {
      // Construct a list of partitions.
      var ex = oq.arg("sp.ex");

      // List current extents.
      if (ex.length > 1) {
        dom.spnCat = dom.bdy.appendChild(dom.spnCat);

        var mb = 0;

        for (var x = 0; x < ex.length; x++) {
          kri = initKeyRow("ex", ex[x], ex[x], dom, dom.bdy, dom.keyRow);

          var e = ex[x];
          var d = e.substr(0, e.length-2);
          for (var k = 0; k < w.dsks.length; k++) {
            if (w.dsks[k].id == d) {
              d = w.dsks[k];
              break;
            }
          }
          for (var k = 0; k < d.pt.length; k++) {
            if (d.pt[k].id == e) {
              e = d.pt[k];
              break;
            }
          }

          dom[kri].val.innerHTML = hrVal(e.mb, 2);
          objAtt(dom[kri].valTd, "align", "right");

          mb += e.mb;
        }

        kri = initKeyRow("", "tmb", "Total Capacity",
          dom, dom.bdy, dom.keyRow);
        dom[kri].val.innerHTML = hrVal(mb, 2);
        objAtt(dom[kri].valTd, "align", "right");
      }

      // List potential extents.
      dom.avlCat = dom.bdy.appendChild(dom.avlCat);
      dom.exNote = dom.bdy.appendChild(dom.exNote);

      var cnt = 0;
      for (var d in w.dsks) {
        for (var p in w.dsks[d].pt) {
          // Skip the current partition.
          if (w.dsks[d].pt[p].sp.id == oq.arg("part")[0])
            continue;

          if (w.dsks[d].pt[p].sp.id == w.dsks[d].pt[p].id &&
            w.dsks[d].pt[p].sp.ex.length == 1) {
            var s = w.dsks[d].pt[p].id + " (";
            s += (w.dsks[d].pt[p].fs ? w.dsks[d].pt[p].tn : "Unformatted");
            s += ", " + hrVal(w.dsks[d].pt[p].mb, 2) + ")";
            kri = initKeyRow("ex", w.dsks[d].pt[p].id, s,
              dom, dom.bdy, dom.keyRow);

            dom[kri].val.innerHTML = '<input type="checkbox" name="' + kri +
              '" value="1" style="margin:0px;margin-right:4px;" />' +
              "Span";

            cnt++;
          }
        }
      }

      if (cnt == 0) {
        dom.avlCat = dom.bdy.removeChild(dom.avlCat);
        dom.exNote = dom.bdy.removeChild(dom.exNote);
      }
    }
    kri = initKeyRow("", "extra", "...", dom, dom.bdy, dom.keyRow);
    objCss(dom[kri].keyRow, "display", "none");

    // Make a new Query object to represent the edit form.
    eq = new Query(document.forms[0]);
    // Synchronize the original args and values with the edit form.
    eq.sync(oq); // eq should have every arg that oq does.
    oq.sync(eq); // oq should have every arg that eq does.

    // Make sure that there are form elements for every arg.
    var a = eq.arg();
    for (var n in a) {
      if (eq.list(a[n]).length == 0) {
        dom[kri].val.innerHTML += htmlHdnInpt(a[n]);
        eq.dump(a[n]);
      }
    }

    if (parent.ctx == "editor") {
      parent.slctBtns("std");
      parent.tglBtns(true);
      window.setTimeout(parent.layoutCb, 25);
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

  function validate() {
    var vl = eq.arg("vl")[0];
    var ch = vl.match(/:|\//);
    if (ch) {
      alert("Character (" + ch + ") cannot be used in a VMFS volume label.");
      return false;
    }

    if (! isUnique(vl)) {
      return false;
    }
    // XXX add more validation as required
    return true;
  }

  editor.nxt[0] = svgMsg;
  editor.nxt[1] = cvtMsg;
  editor.nxt[2] = dltMsg;
  editor.exec = function (idx) {
    // XXX - For now no need to validate before removing a partition.
    if (idx != 2 && !validate())
      return false;

    if (idx == 0) {
      var a = eq.arg();
      for (var x = 0; x < a.length; x++) {
        eq.cache(a[x]);
      }

      var formatted = oq.arg("fs")[0] == "true" ? true : false;
      if (oq.diff(eq) || !formatted) {
        var s = "In order to change the maximum file size, the partition " +
          "must be reformatted; all data on the file system will be lost. " +
          "Reformat partition?";
        // Note: No need to pop up a confirmation dialog
        // to format an unformatted partition.
        if (!formatted || eq.arg("bs")[0] == oq.arg("bs")[0] || confirm(s)) {
          // If a note was displayed in the edit page then now is the time to
          // hide it. Please refer to bug 32946 to understand this change.
          dom.note.css("display", "none");
          ok = true;
          eq.submit();
        } else {
          return false;
        }
      } else {
        exit();
      }
    } else if (idx == 1) {
      var q = new Query();
      q.sync(oq);
      q.action("/storage");
      q.target(parent.op.win());
      q.arg("op", "convertToVmfs2");

      ok = true;
      q.submit();
    } else if (idx == 2) {
      var s = 'Removing a volume may destroy all the data it contains. ' +
        'Remove ' + hrPartStr(oq.arg("part")[0]) + '?';
      if (confirm(s)) {
        var q = new Query();
        q.action("/storage");
        q.target(parent.op.win());
        q.arg("op", "rem");
        q.arg("disk", oq.arg("disk")[0]);
        q.arg("part", oq.arg("part")[0]);

        ok = true;
        q.submit();
      } else {
        return false;
      }
    }
  };

  function getVmfsInfo() {
    for (var d in w.dsks) {
      for (var p in w.dsks[d].pt) {
        if (w.dsks[d].pt[p].sp.id == oq.arg("part")[0])
          return w.dsks[d].pt[p];
      }
    }

    return null;
  }

  cvtMsg.nxt[0] = editor;
  cvtMsg.exec = function () {
    editor.prv = null;

    var pt = getVmfsInfo();

    if (pt != null && pt.tn.toUpperCase() == "VMFS-2") {
      dom.fs.val.innerHTML = "VMFS-2";
      parent.op.win().history.go(-1);
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
    parent.tglBtns(true);
    parent.layoutCb();
  };

  doDn = function () {
    parent.tglBtns(false);
  };

  if (parent.op.win().dsks == null || parent.op.win().dsks.length == 0) {
    parent.loadData("/storage");
  } else {
    next();
  }
}
