var ok = false;

// --------------------------------------------------------------------------

function exit(i) {
  if (parent.ctx == "wizard") {
    if (i == -1) {
      self.location.replace("/vmware/en/esxHostConfigWizardStartup.html");
    } else {
      self.location.replace("/vmware/en/getSwap.html");
    }
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
    self.location.reload();
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

  var w = parent.op.win();

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var display = new Step("display", obj("display"));


  // Dom Objects ------------------------------------------------------------
  var dom = {};
  dom.bdy = obj("tbl").getElementsByTagName("tbody")[0];
  dom.hdrRow = dom.bdy.removeChild(obj("hdrRow"));
  dom.sumRow = dom.bdy.removeChild(obj("sumRow"));
  dom.prtRow = dom.bdy.removeChild(obj("prtRow"));
  dom.keyRow = dom.bdy.removeChild(obj("keyRow"));
  dom.sepRow = dom.bdy.removeChild(obj("sepRow"));
  dom.noDsks = obj("noDsks");

  reflow = function () {
    parent.DH = self.dh();

    if (parent.page.att("id") != self.name) {
      return;
    }

    // Try to make the window tall enough to fit the content, but don't worry
    // about shrinking to fit (XXX: for the momemnt) since that creates a
    // stuttering effect when this page is revisited.
    if (parent[self.name].dim().h < parent.DH) { parent.layoutCb(); }
  };

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

  // Disks ------------------------------------------------------------------
  addDisk = function (dsk) {
    var id = dsk.id;
    dom[id] = new Object();

    // Disk Header ----------------------------------------------------------
    dom[id].hdrRow =
      dom.bdy.appendChild(dom.hdrRow.cloneNode(true));
    objAtt(dom[id].hdrRow, "id", id);

    dom[id].dskLbl = document.getElementById("dskLbl");
    objAtt(dom[id].dskLbl, "id", "");

    var r = cntPrts(dsk);
    var rn = r.p + r.l + (r.e == 0 ? 0 : 1);

    if (rn == 0) {
      dom[id].dskLbl.innerHTML = "Disk " + id + ": (100% of " +
        hrVal(dsk.mb, 2) + " Available)";
    } else if (dsk.fs > 0) {
      var pct = parseInt(100 * (dsk.fs / dsk.mb));
      dom[id].dskLbl.innerHTML = "Disk " + id + ": (" + pct +
        "% of " + hrVal(dsk.mb, 2) + " Available)";
    } else {
      dom[id].dskLbl.innerHTML = "Disk " + id + ": (0% of " +
        hrVal(dsk.mb, 2) + " Available)";
    }

//     // Summary Info -------------------------------------------------------
//     dom[id].sumRow = dom.bdy.appendChild(dom.sumRow.cloneNode(true));
//     objAtt(dom[id].sumRow, "id", "");

    var kri;
    // Cylinders, Heads, Sectors
    // var kri = initKeyRow(id, "chs", "Cylinders, Heads, Sectors");
    // dom[kri].val.innerHTML = dsk.c + ", " + dsk.h + ", " + dsk.s;

//     // Partition Counts
//     for (var n in r) {
//       if (r[n][0] != 0 || n == "p") {
//         var k = "Primary Partitions";
//         if (n == "e") k = "Extended Partition";
//         if (n == "l") k = "Logical Partitions";
//         var v = r[n][0]+" ("+hrVal(r[n][1], 2)+")"; // N (X.X G)

//         kri = initKeyRow(id, n, k,
//           dom, dom.bdy, dom.keyRow);
//         dom[kri].val.innerHTML = v;
//       }
//     }

    // Partition Table ------------------------------------------------------
    var prts = cntPrts(dsk);
    var numPrtns = prts.p[0] + prts.e[0];
    for (var n in dsk.pt) {
      var pn = dsk.pt[n].id;
      var p = dom[pn] = new Object();

      var spnd = dsk.pt[n].sp.id && dsk.pt[n].id != dsk.pt[n].sp.id;

      if (dsk.pt[n].tc == null && dsk.pt[n].id == "") {
        pn = "fs" + dsk.pt[n].sc + ":" + dsk.pt[n].ec;
      }

      // Partition Header ---------------------------------------------------
      p.prtRow = dom.bdy.appendChild(dom.prtRow.cloneNode(true));
      objAtt(p.prtRow, "id", pn);

      // Partition vmhbaX:X:X:X/Free Space ----------------------------------
      p.prtLbl = document.getElementById("prtLbl");
      objAtt(p.prtLbl, "id", "");
      if (dsk.pt[n].tc == null && dsk.pt[n].id == "") {
        if (dsk.pt[n].tn == "logical") {
          p.prtLbl.innerHTML = "Logical Free Space";
        } else {
          p.prtLbl.innerHTML = "Free Space";
        }
      } else {
        var re = /(\d+)$/;
        var m = pn.match(re);

        var vl = dsk.pt[n].tc == w.tc.vmfs && ! spnd && dsk.pt[n].vl
        ? dsk.pt[n].vl : "";

        var fs = dsk.pt[n].tc == w.tc.vmfs && ! dsk.pt[n].fs
        ? "Unformatted " : "";
        fs += ! spnd && dsk.pt[n].tc != null ? dsk.pt[n].tn : "";

        if (w.isExtd(dsk.pt[n].tc)) {
          p.prtLbl.innerHTML = m[1] + ". Extended Partition";
        } else  {
          var s = "";

          if (! spnd) {
            s = (m[1] >= 5 ? "Logical " : "") + (fs.length > 0 ? fs : "") +
              (dsk.pt[n].tc != 0xfc ? " Volume" : "");
            if (vl.length > 0) {
              s = vl + " (" + s + ")";
            }
          } else {
            s = " Extent of " + dsk.pt[n].sp.id;
          }

          p.prtLbl.innerHTML = m[1] + ". " + s;
        }
      }

      // Edit.../Create Volume...
      p.actLbl = document.getElementById("actLbl");
      p.scdSpn = document.getElementById("scdSpn");
      p.scdLbl = document.getElementById("scdLbl");
      objAtt(p.actLbl, "id", "");
      objAtt(p.scdSpn, "id", "");
      objAtt(p.scdLbl, "id", "");

      if (dsk.pt[n].tc == null && dsk.pt[n].id == "") {
        p.actLbl.innerHTML = "Create Volume...";
        // Disallow partition creation if the number of partitions
        // ( primary + extented ) is maxed out. However, allow
        // partitioning for logical free space.
        if (numPrtns < 4 || dsk.pt[n].tn == "logical") {
          var t = new Query();
          t.action("/vmware/en/addPartition.html");
          t.arg("disk", dsk.id);
          t.arg("dump", w.cdp ? "false" : "true");
          t.arg("defCdpMb", w.defCdpMb);
          t.arg("sc", dsk.pt[n].sc);
          t.arg("ec", dsk.pt[n].ec);
          t.arg("mb", dsk.pt[n].mb);
          t.arg("part", dsk.pt[n].id);
          var u = "javascript:self.location.replace('"+t.toString()+"');";
          objAtt(p.actLbl, "href", u);
        } else {
          objAtt(p.actLbl, "class", "dsbld");
          var msg =
           "Disks are limited to four physical volumes. If you want to have " +
           "more than four volumes, you should create three physical " +
           "volumes, one extended partition and two or more logical volumes."
          objAtt(p.actLbl, "href", "javascript:alert('" + msg + "');");
        }
      } else {
        if (w.isExtd(dsk.pt[n].tc)) {
          p.actLbl.innerHTML = "Remove...";
          var msg = "Removing an extended partition will also " +
            "remove all of the logical drives it contains. " +
            "Remove " + hrPartStr(dsk.pt[n].id) + "?";
          var u = "/storage?op=rem&disk="+id+"&part="+dsk.pt[n].id;
          var op = "ok=true;parent.loadData('"+u+"');";
          objAtt(p.actLbl, "href", "javascript:if(confirm('"+msg+"')){"+op+"}");
        } else if (dsk.pt[n].tc == w.tc.vmkd) {
          p.actLbl.innerHTML = "Remove...";
          if (canDelete(dsk.pt[n])) {
            var msg = "It is recommended that you include a core dump " +
              "partition on at least one local hard disk. " +
              "Remove " + hrPartStr(dsk.pt[n].id) + "?";
            var u = "/storage?op=rem&disk="+id+"&part="+dsk.pt[n].id;
            var op = "ok=true;parent.loadData('"+u+"');";
            objAtt(p.actLbl, "href", "javascript:if(confirm('"+msg+"')){"+op+"}");
          } else {
            objAtt(p.actLbl, "class", "dsbld");
            var msg = "Logical volumes must be removed in order from last to first.";
            objAtt(p.actLbl, "href", "javascript:alert('" + msg + "');");
          }
        } else if (dsk.pt[n].tc == w.tc.vmfs && ! spnd) {
          p.actLbl.innerHTML = "Edit...";
          var t = new Query();
          t.action("/vmware/en/editPartition.html");
          t.arg("op", "save");
          t.arg("disk", dsk.id);
          t.arg("part", dsk.pt[n].id);
          t.arg("sc", dsk.pt[n].sc);
          t.arg("ec", dsk.pt[n].ec);
          t.arg("mb", dsk.pt[n].mb);
          t.arg("tc", dsk.pt[n].tc);
          t.arg("tn", dsk.pt[n].tn);
          t.arg("fs", dsk.pt[n].fs);
          t.arg("bs", dsk.pt[n].bs);
          t.arg("vl", dsk.pt[n].vl);
          t.arg("ca", dsk.pt[n].ca);
          t.arg("sp.id", dsk.pt[n].sp.id);
          t.arg("sp.ex", dsk.pt[n].sp.ex.toString());
          var u = "javascript:self.location.replace('"+t.toString()+"');";
          objAtt(p.actLbl, "href", u);
        } else if (dsk.pt[n].tc != w.tc.vmfs) {
          p.scdLbl.innerHTML = "Remove...";

          if (canDelete(dsk.pt[n])) {
            var msg = "Removing a volume may destroy all the data it " +
              "contains. Remove " + hrPartStr(dsk.pt[n].id) + "?";
            var t = new Query();
            t.action("/storage");
            t.arg("op", "rem");
            t.arg("disk", dsk.id);
            t.arg("part", dsk.pt[n].id);
            var op = "ok=true;parent.loadData('" + t.toString() + "');";
            objAtt(p.scdLbl, "href", "javascript:if(confirm('"+msg+"')){"+op+"}");
            objCss(p.scdSpn, "display", "");
          } else {
            objAtt(p.scdLbl, "class", "dsbld");
            var msg = "Logical volumes must be removed in order from last to first.";
            objAtt(p.scdLbl, "href", "javascript:alert('" + msg + "');");
            objCss(p.scdSpn, "display", "");
          }

          p.actLbl.innerHTML = "Format as VMFS-2...";
          t = new Query();
          t.action("/vmware/en/addPartition.html");
          t.arg("op", "alien");
          t.arg("disk", dsk.id);
          t.arg("part", dsk.pt[n].id);
          t.arg("dump", "false");
          t.arg("sc", dsk.pt[n].sc);
          t.arg("ec", dsk.pt[n].ec);
          t.arg("mb", dsk.pt[n].mb);
          var u = "javascript:self.location.replace('" + t.toString() + "');";
          objAtt(p.actLbl, "href", u);
        } else {
          p.actLbl.innerHTML = "&nbsp;";
        }
      }

      // Don't generate more info for extended partitions.
      if (w.isExtd(dsk.pt[n].tc)) continue;

      // Partition Info -----------------------------------------------------

      // Capacity/Total Capacity --------------------------------------------
      var tckri;
      if (dsk.pt[n].sp.ex.length > 1) {
        tckri = initKeyRow(id, "tmb", "Total Capacity",
          dom, dom.bdy, dom.keyRow);
      } else {
        kri = initKeyRow(id, "mb", "Capacity",
          dom, dom.bdy, dom.keyRow);
        dom[kri].val.innerHTML = hrVal(dsk.pt[n].mb, 2);
      }

      if (dsk.pt[n].tc == w.tc.vmfs && dsk.pt[n].fs && ! spnd) {
        // Maximum File Size ------------------------------------------------
        kri = initKeyRow(id, "mfs", "Maximum File Size",
          dom, dom.bdy, dom.keyRow);
        var fs = dsk.pt[n].tn.toUpperCase() == "VMFS-1" ? "vmfs1" : "vmfs2";
        var ms = dsk.pt[n].bs * w.bsm[fs];
        if (w.ams[fs] && ms > w.ams[fs]) {
          ms = w.ams[fs];
        }
        dom[kri].val.innerHTML = hrVal(ms, 2, "g");
      }

      if (dsk.pt[n].tc == w.tc.vmfs && ! spnd &&
          dsk.pt[n].ca && dsk.pt[n].ca != "null") {
        // Access Mode ------------------------------------------------------
        kri = initKeyRow(id, "ca", "Access Mode", dom, dom.bdy, dom.keyRow);
        dom[kri].val.innerHTML =
          dsk.pt[n].ca.substr(0,1).toUpperCase() + dsk.pt[n].ca.substr(1);
        if (dsk.pt[n].ca.match(/private/)) {
          var msg = "'The Private access mode has been deprecated. " +
            "We recommend that you change the access mode of this volume to Public. " +
            "To change this setting, click the Edit... link for this volume.'";
          dom[kri].val.innerHTML +=
            ' (<a href="javascript:alert(' + msg + ');">deprecated mode</a>)'
        }
      }

      // Spanned Partitions -------------------------------------------------
      if (dsk.pt[n].sp.ex.length > 1) {

        kri = initKeyRow(id, "mb0", "Spanned Extents",
          dom, dom.bdy, dom.keyRow);
        dom[kri].val.innerHTML = dsk.pt[n].id;
        dom[kri].valTd3.innerHTML = hrVal(dsk.pt[n].mb, 2);
        var mb = dsk.pt[n].mb;

        for (var j = 1; j < dsk.pt[n].sp.ex.length; j++) {
          var ex = dsk.pt[n].sp.ex[j];
          var dk = ex.substr(0, ex.length-2);
          for (var k = 0; k < w.dsks.length; k++) {
            if (w.dsks[k].id == dk) {
              dk = w.dsks[k];
              break;
            }
          }
          for (var k = 0; k < dk.pt.length; k++) {
            if (dk.pt[k].id == ex) {
              ex = dk.pt[k];
              break;
            }
          }

          kri = initKeyRow(id, "mb"+j, "&nbsp;",
            dom, dom.bdy, dom.keyRow);
          dom[kri].val.innerHTML = ex.id;
          dom[kri].valTd3.innerHTML = hrVal(ex.mb, 2);

          mb += ex.mb;
        }

        dom[tckri].val.innerHTML = hrVal(mb, 2);
      }
    }

    // Separate disks with some white space.
    dom[id].sepRow = dom.bdy.appendChild(dom.sepRow.cloneNode(true));
    if (parent.ctx == "editor" && parent.layoutCb) {
      setTimeout("reflow()", 25);
    }
  };


  // ------------------------------------------------------------------------

  function cntPrts(d) {
    var pp = [0,0];
    var ep = [0,0];
    var lp = [0,0];
    var re = /(\d+)$/;
    for (var i in d.pt) {
      var m = d.pt[i].id.match(re);
      if (m == null) continue;
      if (w.isExtd(d.pt[i].tc)) {
        ep[0]++;
        ep[1] += d.pt[i].mb;
      } else if (m[1] < 5) {
        pp[0]++;
        pp[1] += d.pt[i].mb;
      } else {
        lp[0]++;
        lp[1] += d.pt[i].mb;
      }
    }

    return {p:pp,e:ep,l:lp};
  }

  var shiftDisks = [];
  addDisks = function () {
    if (shiftDisks.length > 0 && shiftDisks[0] != null) {
      var d = shiftDisks.shift();

      addDisk(d);
      setTimeout(addDisks, 1);
    }

    return true;
  };

  ldgMsg.nxt[0] = display;
  ldgMsg.exec = function (i) {
    display.prv = null;

    var cnt = 0;
    for (var d in w.dsks) {
      shiftDisks.push(w.dsks[d]);
      cnt++;
    }

    if (! cnt) {
      dom.noDsks.css("display", "");
    } else {
      dom.noDsks.css("display", "none");
      addDisks();
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
        parent.loadData("/storage");
    }
  }

  // XXX: Call onload so we guarantee that we move past the ldgMsg, even when
  // this page is the current tab (so parent will not call doUp for us).
  doUp();
}
