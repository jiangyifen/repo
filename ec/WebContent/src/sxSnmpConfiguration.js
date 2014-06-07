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
    fillForm();
    prev();
    return;
  }

  next();
}


// --------------------------------------------------------------------------

function initPage() {
  if (parent.ready == null || ! parent.ready()) { window.setTimeout(initPage, 50); return; }

  parent.slctBtns("cls");

  var w = parent.op.win();
  var data;

  var dom = {};
  // Master SNMP Agent
  dom.mstrStopped = obj("mstrStopped");
  dom.mstrStoppedCtrl = obj("mstrStoppedCtrl");
  dom.mstrRunning = obj("mstrRunning");
  dom.mstrRunningCtrl = obj("mstrRunningCtrl");
  dom.autoStart = obj("autoStart");
  dom.autoStartCtrl = obj("autoStartCtrl");
  dom.handStart = obj("handStart");
  dom.handStartCtrl = obj("handStartCtrl");
  dom.scriptLoc = obj("scriptLoc");
  dom.configLoc = obj("configLoc");
  // VMware SNMP SubAgent
  dom.enblSubAgent = obj("enblSubAgent");
  dom.enblSubAgentCtrl = obj("enblSubAgentCtrl");
  dom.dsblSubAgent = obj("dsblSubAgent");
  dom.dsblSubAgentCtrl = obj("dsblSubAgentCtrl");
  dom.enblTraps = obj("enblTraps");
  dom.enblTrapsCtrl = obj("enblTrapsCtrl");
  dom.dsblTraps = obj("dsblTraps");
  dom.dsblTrapsCtrl = obj("dsblTrapsCtrl");

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  fillForm = function () {
    if (w.data != null) { data = clone(w.data); }
    if (data == null) { return; }

    dom.mstrStopped.css("display", data.agent.running ? "none" : "");
    dom.mstrStoppedCtrl.css("display", data.agent.running ? "none" : "");
    dom.mstrRunning.css("display", ! data.agent.running ? "none" : "");
    dom.mstrRunningCtrl.css("display", ! data.agent.running ? "none" : "");

    dom.handStart.css("display", data.agent.autoStart ? "none" : "");
    dom.handStartCtrl.css("display", data.agent.autoStart ? "none" : "");
    dom.autoStart.css("display", ! data.agent.autoStart ? "none" : "");
    dom.autoStartCtrl.css("display", ! data.agent.autoStart ? "none" : "");

    dom.scriptLoc.innerHTML = data.agent.scriptLoc;

    dom.configLoc.innerHTML = data.agent.configLoc;

    dom.dsblSubAgent.css("display", data.subagent.enabled ? "none" : "");
    dom.dsblSubAgentCtrl.css("display", data.subagent.enabled ? "none" : "");
    dom.enblSubAgent.css("display", ! data.subagent.enabled ? "none" : "");
    dom.enblSubAgentCtrl.css("display", ! data.subagent.enabled ? "none" : "");

    dom.dsblTraps.css("display", data.subagent.traps.enabled ? "none" : "");
    dom.dsblTrapsCtrl.css("display", data.subagent.traps.enabled ? "none" : "");
    dom.enblTraps.css("display", ! data.subagent.traps.enabled ? "none" : "");
    dom.enblTrapsCtrl.css("display", ! data.subagent.traps.enabled ? "none" : "");
  };

  function getQ() {
    var q = new Query();
    q.action("/sx-snmp");
    q.target(parent.op.win());
    return q;
  }

  masterStatus = function (v) {
    var q = getQ();
    q.arg("masterstatus", v);
    ok = true;
    q.submit();
    next();
  };

  runAtSystemStartup = function (v) {
    var q = getQ();
    q.arg("runatsystemstartup", v);
    ok = true;
    q.submit();
    next();
  };

  startupScript = function () {
    var v = prompt("Specify the location of your SNMP startup script:",
      data.agent.scriptLoc);
    if (v != null && v != "" && v != data.agent.scriptLoc) {
      var q = getQ();
      q.arg("startupscript", v);
      ok = true;
      q.submit();
      next();
    }
  };

  configFile = function () {
    var v = prompt("Specify the location of your SNMP configuration file:",
      data.agent.configLoc);
    if (v != null && v != "" && v != data.agent.configLoc) {
      var q = getQ();
      q.arg("configfile", v);
      ok = true;
      q.submit();
      next();
    }
  };

  runSubAgent = function (v) {
    var q = getQ();
    q.arg("runsubagent", v);
    ok = true;
    q.submit();
    next();
  };

  vmwareTraps = function (v) {
    var q = getQ();
    q.arg("vmwareTraps", v);
    ok = true;
    q.submit();
    next();
  };

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;

    fillForm();
  };

  editor.nxt[0] = svgMsg;

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  w.location.replace("/sx-snmp");
}
