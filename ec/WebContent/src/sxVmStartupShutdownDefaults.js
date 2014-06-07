/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// Original query, edited query
var oq, eq;
var ok = false;


// --------------------------------------------------------------------------

function exit(i) {
  if (parent.ctx == "sxVmStartupShutdown") {
    if (i == -1) {
      self.location.replace("/vmware/en/sxVmStartupShutdown.html?needUpdate=false");
	//
    } else {
      parent.close();
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
    prev();
    return;
  }

  if (ok) {
    // all ok, update on the client side, no need to round trip to the server

    main.sx.vmAutoDflts("autostart",
			(eq.arg("autostartAll")[0] != 1) ? 0 : 1
			);

    if (main.sx.vmAutoDflts("autostart") != 0 ) { 
      // backend logic does not update rest of fields of autostart was disabled
      main.sx.vmAutoDflts("autostartDelay", eq.arg("autostartDelayAll")[0]);
      main.sx.vmAutoDflts("autostartContOnTools", 
			  (eq.arg("autostartContinueWhenToolsRun")[0] != 1) ? 0 : 1
			  );

      main.sx.vmAutoDflts("autostopDelay", eq.arg("autostopDelayAll")[0]);
    }

    exit(-1);
  }

}


// --------------------------------------------------------------------------

function initPage() {
  parent.slctBtns("std");

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));


  fillForm = function () {
    // Make a new Query object to represent the original form.
    oq = new Query(document.forms[0]);

    // modify oq to represent current state

    // checkbox enable/disable autostart

    if (main.sx.vmAutoDflts("autostart")) {
      oq.select("autostartAll","1",true);
    } else {
      oq.select("autostartAll","1",false);
    }
    
    
    // select list for start delay
    oq.select("autostartDelayAll",main.sx.vmAutoDflts("autostartDelay"),true);

    // checkbox for autostart upon tools start
    if (main.sx.vmAutoDflts("autostartContOnTools") == null || 
       main.sx.vmAutoDflts("autostartContOnTools") == 0) {
      oq.select("autostartContinueWhenToolsRun","1",false);
    } else {
      oq.select("autostartContinueWhenToolsRun","1",true);
    }

    // select list for stop delay
    oq.select("autostopDelayAll",main.sx.vmAutoDflts("autostopDelay"),true);

    // Make a new Query object to represent the modified form.
    eq = new Query(document.forms[0]);
    eq.sync(oq);

    // now toggle the disabled/enabled state according to the overall enable of startup/shutdown
    toggleOptions(oq.arg("autostartAll")[0] == 1);
  }

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;
    fillForm();
    setTimeout("parent.layoutCb()", 40);
  };

  editor.nxt[0] = svgMsg;
  editor.exec = function () {

    eq.cache();

    // Submit the form if there are diffs 
    if (eq.diff(oq)) {
      ok = true;
      eq.submit();
    } else {
      exit(-1);
    }
  };



  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  next();

  initSelectOther();
  addOption(obj("autostopDelayAll"),main.sx.vmAutoDflts("autostopDelay")/60);
  if (addOption(obj("autostartDelayAll"),main.sx.vmAutoDflts("autostartDelay")/60) == 0) {
    // "Don't wait" is selected, VMware Tools is irrelevant, disable it.
    obj("autostartContinueWhenToolsRun").disabled = true;
  }
}

// ------------------------------------------------------

function toggleOptions(boolChecked) {
  obj("autostopDelayAll").disabled = !boolChecked;
  obj("autostartDelayAll").disabled = !boolChecked;
  if (obj("autostartDelayAll").selectedIndex != 0) {
    obj("autostartContinueWhenToolsRun").disabled = !boolChecked;
  }
}

// ------------------------------------------------------

function initSelectOther() {

  var strPromptBase = "Please specify the number of minutes that the system should wait";
  var strPromptPs = {
    "autostopDelayAll":" before it attempts to stop the next virtual machine." ,
    "autostartDelayAll":" before it starts the next virtual machine."
  }

  var prevSlt = {
    "autostopDelayAll":null,
    "autostartDelayAll":null
  }

  addOption = function (objSelect, intOther) {
    // see if Other option is already in the list, select it, return its index
    for (var j=0; j < objSelect.options.length; j++) {
      if (objSelect.options[j].value == intOther*60) {
	objSelect.selectedIndex = j;
	prevSlt[objSelect.id] = objSelect.selectedIndex; 
	return objSelect.selectedIndex;
      }
    }
    // option not found in list, add it and return its index
    var objOtherOpt = new Option(
			    objSelect.options[objSelect.options.length-1].text,
			    objSelect.options[objSelect.options.length-1].value);

    var objNewOpt = new Option("",intOther*60);
    objSelect.options[objSelect.options.length-1] = objNewOpt;
    objNewOpt.text = intOther + " minutes"; 
    if (objSelect.id == "autostopDelayAll") {
      objNewOpt.text += " at most";
    }
    // add the "Other..." option back in
    objSelect.options[objSelect.options.length] = objOtherOpt;
    // select the new entry created
    objSelect.selectedIndex = (objSelect.length-2);
    // remember the entry for next selects
    prevSlt[objSelect.id] = objSelect.selectedIndex; 
    return objSelect.selectedIndex;
  }

  selectOther =  function (objSelect) {
    // predefined value was picked from the list
    if (objSelect.options[objSelect.selectedIndex].value != "other") { 
      // If don't wait was selected the tools checkbox is irrelevant
      if (objSelect.id == "autostartDelayAll"){
	obj("autostartContinueWhenToolsRun").disabled = 
	  (objSelect.options[objSelect.selectedIndex].value == 0) ? true : false; 
      }
      // save the currently picked value for future rollbacks
      prevSlt[objSelect.id] = objSelect.selectedIndex;
      return;
    }

    // "Other" entry was selected
    var intOther;
    // prompt for valid user input
    while ((intOther= window.prompt((strPromptBase + strPromptPs[objSelect.id]),"")) != null) {
      if (isNaN(intOther) || intOther < 0 || intOther == "") {
	window.alert("Please enter a positive number of minutes.");
      } else {
	var addedOptIdx = addOption(objSelect,intOther);
	if (objSelect.id == "autostartDelayAll"){
	  obj("autostartContinueWhenToolsRun").disabled = 
	    (objSelect.options[objSelect.selectedIndex].value == 0) ? true : false; 
	} 
	return; 
      }
    }

    // User has chosen to  cancel or close the "Other" prompt
    objSelect.selectedIndex = prevSlt[objSelect.id];
  };
}

// ------------------------------------------------------

function initSelectOther() {

  var strPromptBase = "Please specify the number of minutes that the system should wait";
  var strPromptPs = {
    "autostopDelayAll":" before it attempts to stop the next virtual machine." ,
    "autostartDelayAll":" before it starts the next virtual machine."
  }

  var prevSlt = {
    "autostopDelayAll":null,
    "autostartDelayAll":null
  }

  addOption = function (objSelect, intOther) {
    // see if Other option is already in the list, select it, return its index
    for (var j=0; j < objSelect.options.length; j++) {
      if (objSelect.options[j].value == intOther*60) {
	objSelect.selectedIndex = j;
	prevSlt[objSelect.id] = objSelect.selectedIndex; 
	return objSelect.selectedIndex;
      }
    }
    // option not found in list, add it and return its index
    var objOtherOpt = new Option(
			    objSelect.options[objSelect.options.length-1].text,
			    objSelect.options[objSelect.options.length-1].value);

    var objNewOpt = new Option("",intOther*60);
    objSelect.options[objSelect.options.length-1] = objNewOpt;
    objNewOpt.text = intOther + " minutes"; 
    if (objSelect.id == "autostopDelayAll") {
      objNewOpt.text += " at most";
    }
    // add the "Other..." option back in
    objSelect.options[objSelect.options.length] = objOtherOpt;
    // select the new entry created
    objSelect.selectedIndex = (objSelect.length-2);
    // remember the entry for next selects
    prevSlt[objSelect.id] = objSelect.selectedIndex; 
    return objSelect.selectedIndex;
  }

  selectOther =  function (objSelect) {
    // predefined value was picked from the list
    if (objSelect.options[objSelect.selectedIndex].value != "other") { 
      prevSlt[objSelect.id] = objSelect.selectedIndex;
      return;
    }
    // "Other" entry was selected
    var intOther;
    // prompt for valid user input
    while ((intOther= window.prompt((strPromptBase + strPromptPs[objSelect.id]),"")) != null) {
      if (isNaN(intOther) || intOther < 0 || intOther == "") {
	window.alert("Please enter a positive number of minutes.");
      } else {
	addOption(objSelect,intOther); 
	return; 
      }
    }
    // User has chosen to  cancel or close the prompt
    objSelect.selectedIndex = prevSlt[objSelect.id];
  };
}
