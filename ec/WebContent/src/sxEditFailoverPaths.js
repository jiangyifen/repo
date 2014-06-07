/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// Original query, edited query
var oq, eq;
var ok = false;
var trgt = null;

// --------------------------------------------------------------------------
function exit() {
  if (parent.ctx == "editor") {
    parent.tglBtns(false);
  }

  // XXX: Netscape 7.02 won't execute the following code if it is not called
  // from the timeout.
  window.setTimeout('self.location.replace("sxFailoverPaths.html")', 0);
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
    parent.sanData = null; // invalidate the cache
    prev();
    return;
  }

  next();
}

function clik(e) {
  e = eObj(e);
  trgt = getTrgt(e);
  if (trgt && trgt.ctrl == "menu") {
    pathCtxMenuHndlr(e);
  }
}

// --------------------------------------------------------------------------

function initPage() {
  if (parent.ready == null || ! parent.ready()) {
    window.setTimeout(initPage, 50);
    return; 
  }

  oq = new Query(location.search);
   
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
  dom.plcyRow = dom.bdy.removeChild(obj("plcyRow"));
  objAtt(dom.plcyRow, "id", "");
  
  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));
 
  dom.fixed = null;
  dom.mru   = null;

  var lun  = 0;
  var policy = "fixed";
  
  // variables used in setting query values.
  var prfrdPath  = "";
  var actvPath   = "";
  var idlePaths  = [];
  var dsbldPaths = [];

  // --------------------------------------------------------
  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (idx) {
    // Only interested in the selected LUN;
    lun = oq.arg("lun")[0];
    policy = oq.arg("plcy")[0];
    paths = clone(parent.sanData.luns[lun], true);
    dom[lun] = {};
    var kri, enbld, dsbld, brkn, html;

    dom[lun].hdr = dom.bdy.appendChild(dom.lunRow.cloneNode(true));

    dom[lun].lbl = obj("lunLbl");
    dom[lun].lbl.att("id", "");

    enbld = dsbld = brkn = 0;
    for (var i = 0; i < paths.length; i++) {
      if (paths[i].brkn) {
        brkn++;
      } else if (! paths[i].enbld) {
        dsbld++;
      } else {
        enbld++;
      }
    }
    html = "SAN LUN " + lun + " (" +paths.length + " Paths";
    html += brkn ? ": " + brkn + " Broken" : "";
    html += dsbld ? (brkn ? ", " : ": ") + dsbld + " Disabled" : "";
    html += ", Policy: " + policy;
    html += ")";
    dom[lun].lbl.innerHTML = html;

    dom[lun].sum = dom.bdy.appendChild(dom.sumRow.cloneNode(true));
   
    for (var i = 0; i < paths.length; i++) {
      kri = initKeyRow("", paths[i].id, paths[i].id,
        dom[lun], dom.bdy, dom.keyRow);
      dom[lun][kri].val.innerHTML = paths[i].trgt;
      
      dom[lun][kri].trgtTd.ctrl = "menu";
      dom[lun][kri].trgtTd.path = paths[i];

      dom[lun][kri].ico = obj("ico");
      dom[lun][kri].ico.att("id", "");
      var src = "../imx/info.holder-10x10.png";
      if (paths[i].actv) { src = "../imx/info-10x10.png"; }
      if (!paths[i].enbld) { src = "../imx/warning-10x10.png"; }
      if (paths[i].brkn) { src = "../imx/error-10x10.png"; }
      dom[lun][kri].ico.att("src", src);
 
      dom[lun][kri].prfrd = obj("prfrdVal");
      dom[lun][kri].prfrd.att("id", "");
      if(paths[i].prfrd && policy == "fixed") {
        dom[lun][kri].prfrd.css("visibility","visible");
      }
    }
    dom[lun].sep = dom.bdy.appendChild(dom.sepRow.cloneNode(true));
    dom[lun].plcy = dom.bdy.appendChild(dom.plcyRow.cloneNode(true));
    
    dom.fixed = obj("fixed");
    dom.mru   = obj("mru");
    dom[policy].att("class", "slctd");
    
    // Make a new Query object to represent the modified form.
    eq = new Query(document.forms[0]);
    eq.sync(oq);
    oq.sync(eq);
    
    if (parent.ctx == "editor") {
      parent.slctBtns("std");
      parent.tglBtns(true);
      window.setTimeout(parent.layoutCb, 25);
    }

  };
  
  // --------------------------------------------------------
  editor.nxt[0] = svgMsg;
  editor.exec = function () {
    eq.arg("plcy", policy); 
    eq.arg("prfrd", prfrdPath);
    eq.arg("actv", actvPath);
    if (idlePaths.length > 0) {
      // Go through the list of idle paths and create
      // a string that contains semicolon separated paths
      // because the MUI backend expects such a string.
      // Note: Don't bother to add a semicolon for the
      // last path or if it's the only path in the list.
      var val = "";
      var len = idlePaths.length;
      for (var i = 0; i < len - 1; i++) {
        val += idlePaths[i] + ";"; 
      }
      val += idlePaths[i];
      eq.arg("idle", val);
    }
    if (dsbldPaths.length > 0) {
      // Go through the list of disabled paths and create
      // a string that contains semicolon separated paths
      // because the MUI backend expects such a string.
      // Note: Don't bother to add a semicolon for the
      // last path or if it's the only path in the list.
      var val = "";
      var len = dsbldPaths.length;
      for (var i = 0; i < len - 1; i++) {
        val += dsbldPaths[i] + ";"
      }
      val += dsbldPaths[i];
      eq.arg("dsbld", val);
    }
    if (oq.diff(eq)) {
      ok = true;
      eq.submit();
    } else {
      // Nothing has changed. Just go back to the cached page.
      editor.nxt[0] = null;
      prev();
    }
  };
  
  
  // --------------------------------------------------------
  // 
  //  plcyChng -- 
  //
  // This is triggered when the user chooses to change the policy
  //
  // Prototype:
  //
  //    void plcyChng(string newPlcy)
  //
  // Arguments:
  //
  //    newPlcy - fixed | mru
  //
  // Result:
  //
  //    Policy is changed.  
  //
  // --------------------------------------------------------
  
  plcyChng = function (newPlcy) {
    policy = newPlcy;
    dom.fixed.att("class", "");
    dom.mru.att("class", "");
    // highlight the selectced policy.
    dom[policy].att("class", "slctd");

    var ppi = getPathIndexByKey("prfrd", true); // preferred path index
    if (ppi < 0) {
      // No extra processing required if a preferred path is not found
      return; 
    }
    var path = paths[ppi].id; // Preferred path
    if (policy == "fixed") {
      // If the preferred path happens to be in the idle state,
      // when the policy is switched from mru to fixed, then
      // make the preferred path active. 
      if (paths[ppi].actv == false && paths[ppi].enbld == true) {
        var api = getPathIndexByKey("actv", true); // active path index
        if (api >= 0) {
          paths[api].actv = false;
          dom[lun][paths[api].id].ico.src = "../imx/info.holder-10x10.png";
        }
        paths[ppi].actv = true;
        dom[lun][path].ico.src = "../imx/info-10x10.png";
      }
      dom[lun][path].prfrd.css("visibility","visible");
    } else {
      // In the case of mru policy users should not be 
      // allowed to select a preferred path. 
      dom[lun][path].prfrd.css("visibility","hidden");
    }
  };
  
  
  // --------------------------------------------------------
  getCurrentPolicy = function() {
    return policy;
  }
  
  
  // ----------------- BEGIN PRIVATE METHIODS ---------------
  //
  // menuCb -- 
  //
  // This is a callback method that gets notified by the LUN control
  // menu observer when the user makes a menu selection.
  //
  // Prototype:
  //
  //    void menuCb(string selection)
  //
  // Arguments:
  //
  //    selection - prfrd | actv | idle | dsbld
  //
  // Result:
  //
  //    The previous and current selections are updated.
  // --------------------------------------------------------
  
  function menuCb(type, ctx, selection) {
    switch(selection) {
      case "lunOpPrfrd":
        // --- begin updating the previous selection ---
        var i = getPathIndexByKey("prfrd", true); // Check if there is a prfrd state
        if (i >= 0) {
          var path = paths[i].id;
          paths[i].prfrd = false;
          dom[lun][path].prfrd.css("visibility","hidden");
        }
        
        // Note: When a path is selected to be prfrd then it should
        // become active as well. Therefore, deactivate the path that
        // is currently active ( if there is one ).
        i = getPathIndexByKey("actv", true);
        if (i >= 0) {
          path = paths[i].id;
          paths[i].actv = false;
          if (paths[i].enbld == true) {
            dom[lun][path].ico.src = "../imx/info.holder-10x10.png";
          }
        }
        
        // --- begin updating the current selection ---
        // Make the selected path prfrd and remember to make it active.
        prfrdPath = actvPath = trgt.path.id;
        trgt.path.prfrd = true;
        trgt.path.actv  = true;
        trgt.path.enbld = true;
        dom[lun][prfrdPath].ico.src = "../imx/info-10x10.png";
        dom[lun][prfrdPath].prfrd.css("visibility","visible");
        break;
      case "lunOpActv":
        // --- begin updating the previous selection ---
        // Note: When a path is selected to be prfrd then it should
        // become active as well. Therefore, deactivate the path that
        // is active ( if there is one ).
        var i = getPathIndexByKey("actv", true);
        if (i >= 0) {
          var path = paths[i].id;
          paths[i].actv = false;
          // if the path is actv and prfrd then it should be disabled
          // otherwise just leave it idle.
          if (policy == "fixed" && paths[i].prfrd == true) {
            paths[i].enbld = false;
            dom[lun][path].ico.src = "../imx/warning-10x10.png";
           
            // a disable path should not be in the list of idle paths
            removeItem(idlePaths, path);
            dsbldPaths.push(path);
          } else {
            dom[lun][path].ico.src = "../imx/info.holder-10x10.png";
          }
        }
        
        // --- begin updating the current selection ---
        // Make the selected path actv and remember to enable it
        // because an active path should always be enabled.
        actvPath = trgt.path.id;
        trgt.path.actv  = true;
        trgt.path.enbld = true;
        dom[lun][actvPath].ico.src = "../imx/info-10x10.png";
        break;
      case "lunOpIdle":
        // --- begin updating the current selection ---
        var path = trgt.path.id;
        trgt.path.enbld = true;
        dom[lun][path].ico.src = "../imx/info.holder-10x10.png";
       
        // an idle path should not be in the list of disabled paths
        removeItem(dsbldPaths, path);
        idlePaths.push(path);
        break;
      case "lunOpDsbld":
        trgt.path.enbld = false; // do this to find the next idle path.
        // --- begin updating the previous selection ---
        // If the current path is active then disabling it should
        // result in a new active path; the next path that is idle.
        if (trgt.path.actv == true) {
          var i = getPathIndexByKey("enbld", true);
          if (i >= 0) {
            var path = paths[i].id;
            paths[i].actv = true;
            dom[lun][path].ico.src = "../imx/info-10x10.png";
          }
        }
        
        // --- begin updating the current selection ---
        path = trgt.path.id
        trgt.path.actv  = false;
        dom[lun][path].ico.src = "../imx/warning-10x10.png";
        
        // a disable path should not be in the list of idle paths
        removeItem(idlePaths, path);
        dsbldPaths.push(path);
        break;
      default:
        break;
    }
  };
  
  
  // --------------------------------------------------------
  //
  // removeItem --
  //
  // Removes an item from the given array.
  //
  // Prototype:
  //
  //   void remove(Array array, string item)
  //
  // Arguments:
  //
  //    array -
  //    item - The item that is to be removed from the array
  //
  // Result:
  //
  //     Item, if present in the array, is removed.
  //
  // --------------------------------------------------------
  
  function removeItem(array, item) {
    var i = 0;
    var len = array.length;
    while(i < len) {
      if(array[i] == item) {
        array.splice(i,1);
      }
      else {
        i++;
      }
    }
  }
  
  
  // --------------------------------------------------------
  //
  // getPathIndexByKey --
  //
  // Retrieve the position of the path that mathches the given
  // key-value pair.
  //
  // Prototype:
  //
  //     int getPathIndexByKey(string key, variant value)
  //
  // Arguments:
  //
  //     key   => prfrd | actv | enbld.
  //     value - a variant
  //
  // Result:
  //
  //     If the key, in question, is found in the list
  //     and if it's set to true then return the key's
  //     position otherwise return -1 ( not found ).
  //
  // --------------------------------------------------------
  
  function getPathIndexByKey(key, value) {
    for (var i = 0; i < paths.length; i++) {
      if (paths[i][key] == value) {
        return i;
      }
    }
    return -1;
  }

  // --------------------- END PRIVATE METHIODS -------------------------
  
  initPathCtxMenu(menuCb);
  
  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-2);
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
  
  next();
}
