// Original query, edited query
var oq, eq;
var submitted = false;

// --------------------------------------------------------------------------

function exit(idx) {
  parent.close();
}

// --------------------------------------------------------------------------

function opCb(w) {
  // The initial server hit will tell us if we're setup; if we are, 
  // kick the user back to the setup page.
  if (w.scriptedInstallSetup != null) {
    if (true == scriptedInstallSetup) {
      self.location.replace("/vmware/en/esxScriptedInstallWiz.html");
      return;
    }
  }

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

  if (submitted) {
    submitted = false;

    if (w.err.length > 0) {
      prev();
      return;
    }
  }

  next();
}


// --------------------------------------------------------------------------

function initPage() {
  var w = parent.op.win();
  var q = new Query(document.forms[0]);

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var doCopy = new Step("doCopy", obj("getParams"));
  var setupComplete = new Step("setupComplete", obj("svgMsg"));

  doCopy.nxt[0] = setupComplete; 
  doCopy.exec = function(i) {
  }; 

  ldgMsg.nxt[0] = doCopy;
  ldgMsg.exec = function (i) {
    q.submit(); 
  };

  setupComplete.nxt[0] = exit;
  setupComplete.exec = function (i) {
  };

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (idx) {
    curStep = curStep.slct(idx);
  };

  parent.loadData("/scriptedinstall");
}
