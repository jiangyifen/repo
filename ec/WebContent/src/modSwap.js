var oq, eq;
// Default swap file name
var defFn = "SwapFile.vswp";

function exit(i) {
  self.location.replace("/vmware/en/getSwap.html");
}

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

  if (w.err.length > 0) {
    prev();
    return;
  }

  next();
}

function initPage() {
  var w = parent.op.win();

  // Returns pretty volume labels.
  function vl(v,m) {
    if (v == null) {
      return "";
    }
    m = m ? ": " + hrVal(v.fmb) + " Available" : "";
    if (v.vl) {
      return v.vl + " (" + v.id + ")" + m;
    }
    return v.id + m;
  }

  // Returns an array of pretty volume labels.
  function vls(m) {
    var a = new Array();
    for (var i in w.vols) {
      a.push(vl(w.vols[i], m));
    }
    return a;
  }

  // Returns an array of volume IDs.
  function vvs() {
    var a = new Array();
    for (var i in w.vols) {
      a.push(w.vols[i].id);
    }
    return a;
  }

  // Returns the index of a volume given an ID or label.
  function l2idx(l) {
    for (var i in w.vols) {
      if (w.vols[i].vl == l || w.vols[i].id == l) {
        return i;
      }
    }
    return -1;
  }

  // Returns the corresponding volume ID of a given volume label.
  function l2id(l) {
    var i = l2idx(l);
    if (i > -1) {
      return w.vols[i].id;
    }
    return "";
  }

  // Returns an array of file names, including some place holders if f is true.
  function fns(v,f) {
    var a = new Array();
    a.push(f ? "" : "None");
    if (v != null) {
      for (var i in v.fl) {
        a.push(v.fl[i].fn);
      }
    }
    a.push(f ? "-1" : "Other...");
    return a;
  }

  // Returns the index of file name fn in the list of files on volume v.
  function fn2idx(fn, v) {
    for (var i in v.fl) {
      if (v.fl[i].fn == fn) {
        return i;
      }
    }
    return -1;
  }

  // Returns the size in megabytes of the file name fn on the volume v.
  function fn2mb(fn, v) {
    var i = fn2idx(fn, v);
    if (i > -1) {
      return v.fl[i].mb;
    }
    return 0;
  }

  // Returns a size in megabytes equal to the recommended swap capcity or the
  // available space on volume v.
  function defMb(v) {
    return v.fmb >= w.sgdMb ? w.sgdMb : v.fmb;
  }

  // Handle VMFS volume option selections.
  vlChgd = function (e) {
    eq.cache("chgSwpVl");
    var id = eq.arg("chgSwpVl")[0];

    dom.chgSwpFn.innerHTML = htmlSlct("chgSwpFn",
      fns(w.vols[l2idx(id)], true), w.cfgSwp.fn, fns(w.vols[l2idx(id)]),
      "fnChgd(this);");
    eq.cache("chgSwpFn");

    // If we currently have no swap file, select the default name
    if (w.cfgSwp.fn == "") {
      eq.arg("chgSwpFn", defFn);
    }

    var mb = fn2mb(eq.arg("chgSwpFn")[0], w.vols[l2idx(id)]);
    eq.arg("chgSwpMb", mb > 0 ? mb : defMb(w.vols[l2idx(id)]));
  };

  // Handle file name option selections.
  fnChgd = function (e) {
    var id = eq.arg("chgSwpVl")[0];
    var fn;

    if (e.selectedIndex == e.options.length - 1) {
      // Other... was selected.
      fn = prompt("Please enter a file name.", "SwapFile.vswp");

      while (fn != null && fn.indexOf(".vswp") != fn.length - 5) {
        fn = prompt('Swap file names must end in ".vswp".\n' +
          'Please enter a file name.', fn);
      }

      if (fn != null) {
        // Fake the new file using the request name and a good default size.
        if (fn2idx(fn, w.vols[l2idx(id)]) < 0) {
          w.vols[l2idx(id)].fl.push({"fn":fn,"mb":defMb(w.vols[l2idx(id)])});
        }

        // Generate the new HTMLSelect element.
        dom.chgSwpFn.innerHTML = htmlSlct("chgSwpFn",
          fns(w.vols[l2idx(id)], true), fn, fns(w.vols[l2idx(id)]),
          "fnChgd(this);");
        // Make sure eq preserves the selected value.
        eq.cache("chgSwpFn");
      } else {
        // Other... prompt was cancelled. Revert to the last selection.
        eq.f.chgSwpFn.options[0].selected = true;
        eq.select("chgSwpFn", eq.arg("chgSwpFn")[0], true);
      }
    }

    // Disable the size and activation controls if None is selected
    var dsbl = e.selectedIndex == 0;
    eq.f.chgSwpAp.disabled = dsbl;
    eq.f.chgSwpMb.disabled = dsbl;

    // Grab the value of the selected file name option.
    fn = eq.cache("chgSwpFn")[0];

    var mb = fn2mb(fn, w.vols[l2idx(id)]);
    eq.arg("chgSwpMb", mb > 0 ? mb : defMb(w.vols[l2idx(id)]));
  };

  var dom = {};
  dom.bdy = obj("tbl").getElementsByTagName("tbody")[0];
  dom.chgSwpVl = obj("chgSwpVl");
  dom.chgSwpVlTd0 = obj("chgSwpVlTd0");
  dom.chgSwpVlTd1 = obj("chgSwpVlTd1");
  dom.chgSwpFn = obj("chgSwpFn");
  dom.chgSwpMb = obj("chgSwpMb");

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;

    var vlidx = w.cfgSwp.vl ? l2idx(w.cfgSwp.vl) : 0;

    // Swap files are not created until they are activated (?) so it's possible
    // that a configured swap file will not exist on disk. Look for the
    // configured swap file on the configured volume; fake the file if it is
    // not found, or if there is no swap file configured for the volume.
    for (var x = 0; x < w.vols.length; x++) {
      var fnd = false;

      if (x == vlidx) {
        for (var j = 0; j < w.vols[x].fl.length; j++) {
          if (w.vols[x].fl[j].fn == w.cfgSwp.fn) {
            fnd = true;
            break;
          }
        }
      }

      if (! fnd) {
        if (w.cfgSwp.vl == w.vols[x].id) {
          var mb = w.cfgSwp.mb ? w.cfgSwp.mb : defMb(w.vols[x]);
          w.vols[x].fl.push({"fn":w.cfgSwp.fn,"mb":mb});
        } else {
          // Check if there's already an (inactive) SwapFile.vswp on the disk
          for (var j = 0; j < w.vols[x].fl.length; j++) {
            if (w.vols[x].fl[j].fn == defFn) {
              fnd = true;
              break;
            }
          }
          if (! fnd) {
            w.vols[x].fl.push({"fn":defFn,"mb":defMb(w.vols[x])});
          }
        }
      }
    }

    // VMFS Volume
    if (w.vols.length > 1) {
      dom.chgSwpVl.innerHTML = htmlSlct("chgSwpVl", vvs(), l2id(w.cfgSwp.vl),
        vls(true), "vlChgd(this);");
      objAtt(dom.chgSwpVlTd0, "class", "slctKey");
      objAtt(dom.chgSwpVlTd1, "class", "slctVal");
    } else {
      dom.chgSwpVl.innerHTML = vl(w.vols[0], true) +
        '<input type="hidden" name="chgSwpVl" value="' + w.vols[0].id + '">';
    }

    // The form is incomplete before this point.
    oq = new Query(document.forms[0]);

    // File Name
    dom.chgSwpFn.innerHTML = htmlSlct("chgSwpFn", fns(w.vols[vlidx], true),
      w.cfgSwp.fn, fns(w.vols[vlidx]), "fnChgd(this);");
    oq.arg("chgSwpFn", w.cfgSwp.fn);
    // Swap Capacity
    dom.chgSwpMb.innerHTML = "File Size ("+hrVal(w.sgdMb, 0)+" recommended)";
    oq.arg("chgSwpMb", w.cfgSwp.mb ? w.cfgSwp.mb : defMb(w.vols[vlidx]));

    // Activation Policy
    oq.select("chgSwpAp", "false",
      ! w.cfgSwp.ap && w.cfgSwp.mb > 0 && w.cfgSwp.vl && w.cfgSwp.fn);


    eq = new Query(document.forms[0]);
    eq.sync(oq);

    // If we currently have no swap file, select the default name
    if (w.cfgSwp.fn == "") {
      eq.arg("chgSwpFn", defFn);
    }
  };

  editor.nxt[0] = svgMsg;
  editor.exec = function (i) {
    if (i == 0) {
      var a = eq.arg();
      for (var i = 0; i < a.length; i++) {
        eq.cache(a[i]);
      }

      if(eq.diff(oq) || ! w.cfgSwp.vl) {
        ok = true;
        eq.submit();
      } else {
        exit();
      }
    }
  };

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  if (parent.op.win().cfgSwp == null) {
    parent.loadData("/swap");
  } else {
    next();
  }
}
