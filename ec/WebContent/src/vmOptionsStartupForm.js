/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// Original query, edited query
var oq, eq;
var ok = false;
var obsrvrStr;

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

  if (ok && w.err.length > 0) {
    ok = false;
    if (parent.ctx.match(/^editor$/i)) {
      main.vmOptObsrvr.lstn(obsrvrStr, lstnr, vm);
      main.vmExecutionStateObsrvr.lstn(obsrvrStr, rndrMode, vm);
      main.vmModeObsrvr.lstn(obsrvrStr, rndrMode, vm);
    }
    prev();
    return;
  }

  if (ok) {
    
    var vm = main.sx.vms[new Query(parent.location.search).arg("vmid")[0]];
    for (var i = 0; i < w.opts.length; i++) {
      vm.opt(w.opts[i].k, w.opts[i].v);
      if (w.opts[i].k == "name") { vm.dn(w.opts[i].v); }
    }

  }

  next();
}


// --------------------------------------------------------------------------

function initPage() {
  var w = parent.op.win();
  var q = new Query(parent.location.search);
  var vm = main.sx.vms[q.arg("vmid")[0]];
  
  //  obj("divAutostartOrder").innerHTML = strListOrderedVms(vm); // XXX VMs sequence

  obsrvrStr = "vmOptionsStartup" + parent.ctx + "_" + vm.hash();

  //  dom = new Object();
  //  dom.trContTools = obj("trContTools);

  // var dom = {};

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  rndrMode = function () {
    if (vm.es() == "suspended") {
      window.alert("This virtual machine has been suspended. Resource" + 
		   " settings cannot be changed while the virtual machine is" + 
		   " suspended.\n\nClick OK to close the editor window.");
      exit();
    }
  };

  function fillForm () {


    // Make a new Query object to represent the original form.
    oq = new Query(document.forms[0]);
    oq.arg("vmid", vm.hash());

    var autostart = (vm.opt("autostart") != "none");
    oq.select("autostart", 1, autostart);
    oq.select("autostart", 0, !autostart);
    guiToggleAutoStartup(autostart);

    var cwtr = vm.opt("autostartContinueWhenToolsRun"); 
    oq.select("autostartContinueWhenToolsRun", 1, cwtr);
    oq.select("autostartContinueWhenToolsRun", 0, !cwtr);

    if (vm.opt("autostop") == "none") {
      oq.arg("autostop", "poweroff");
    } else if (vm.opt("autostop") == "suspend") {
      // Add an otherwise-unsupported suspend option
      var suspendOpt = new Option("Suspend virtual machine", "suspend");
      obj("autostop").options[obj("autostop").options.length] = suspendOpt;
      oq.arg("autostop", "suspend");
    } else {
      oq.arg("autostop", vm.opt("autostop"));
    }
    //    oq.arg("autostartOrder", "last");
    oq.arg("autostartDelay", vm.opt("autostartDelay"));
    oq.arg("autostopDelay", vm.opt("autostopDelay"));
    oq.arg("autostartContinueWhenToolsRun", vm.opt("autostartContinueWhenToolsRun"));

    //  to represent the modified form.
    eq = new Query(document.forms[0]);
    eq.sync(oq);

  };
    
  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;
    fillForm();
    setTimeout("parent.layoutCb()", 40);
  };

  editor.nxt[0] = svgMsg;
  editor.exec = function () {
    eq.cache();

    // Negating versions of the autostart checkboxes are hidden --
    // toggle like radiobuttons
    eq.select("autostart", 0, !eq.select("autostart", 1));
    eq.select("autostartContinueWhenToolsRun", 0,
              !eq.select("autostartContinueWhenToolsRun", 1));
    
    if (eq.diff(oq)) {
      ok = true;
      
      if (parent.ctx.match(/^editor$/i)) {
        main.vmOptObsrvr.ignr(obsrvrStr);
        main.vmExecutionStateObsrvr.ignr(obsrvrStr, rndrMode, vm);
        main.vmModeObsrvr.ignr(obsrvrStr, rndrMode, vm);
      }
      eq.submit();
    } else {
      exit();
    }
  };

  lstnr = function (t, o, v) {
    if (v.id == dev.id && confirm("The virtual machine options " +
      "have changed. Reload configuration?")) {
      fillForm();
    }
  };

  lstnrAutoStart = function(){
    // the only change this edit window cares about is the autostart. Start/stop
    // delays are mentioned as system default, and therfor their value does not
    // change in this window.
    if(main.sx.vmAutoDflts("autostart") == 0){
      window.alert("Virtual machine autostart has been disabled system wide!\n" +
		 "You can enable it again using the link in the main screen's" +
		 " options tab.\nThis window will now close.");
      main.sxVmAutoDfltsObsrvr.ignr(obsrvrStr);
      exit();
    }
  }

  if (parent.ctx.match(/^editor$/i)) {
    main.vmOptObsrvr.lstn(obsrvrStr, lstnr, vm);
    main.vmExecutionStateObsrvr.lstn(obsrvrStr, rndrMode, vm);
    main.vmModeObsrvr.lstn(obsrvrStr, rndrMode, vm);
  }

  main.sxVmAutoDfltsObsrvr.lstn(obsrvrStr,lstnrAutoStart);

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  w.location.replace("/vm-config/index.pl?op=getVmDiskDevData");

  initSelectOther();
  addOption(obj("autostopDelay"),vm.opt("autostopDelay")/60);
  disableToolsChkBx(addOption(obj("autostartDelay"),vm.opt("autostartDelay")/60));
}

function guiToggleAutoStartup(boolEnabled) {
  var disColor = (boolEnabled) ? "" : "#c0c0c0";
  obj("divLblStartDelay").css("color",disColor);
  obj("autostartDelay").disabled = !boolEnabled;
  if ( !( obj("autostartDelay").selectedIndex == 1 || 
	  (obj("autostartDelay").selectedIndex == 0 && 
	   main.sx.vmAutoDflts("autostartDelay") == 0)
	  )
       ){
    obj("spnToolsStart").css("color",disColor);
    obj("autostartContinueWhenToolsRun").disabled = !boolEnabled;
  }

  // XXX Vms Sequence
  //  obj("divLblStartBefore").css("color",disColor);
  //  document.getElementsByTagName("select")["autostartDelay"].disabled = !boolEnabled;
  // XXX Vms Sequence

}

function disableToolsChkBx(intIdx) {
  if (intIdx == 1 || 
      (intIdx == 0 && main.sx.vmAutoDflts("autostartDelay") == 0)
      ){
    // "Don't wait" or system default of Don't Wait is selected :  VMware Tools
    //  is irrelevant, disable it.
    obj("spnToolsStart").css("color","#c0c0c0");
    obj("autostartContinueWhenToolsRun").disabled = true;
  } else {
    obj("spnToolsStart").css("color","");
    obj("autostartContinueWhenToolsRun").disabled = false;
  }
}

// ------------------------------------------------------

function initSelectOther() {

  var strPromptBase = "Please specify the number of minutes that the system should wait";
  var strPromptPs = {
    "autostopDelay":" before it attempts to stop other virtual machines." ,
    "autostartDelay":" before it starts other virtual machines."
  }

  var prevSlt = {
    "autostopDelay":null,
    "autostartDelay":null
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
    if (objSelect.id == "autostopDelay") {
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
      if (objSelect.id == "autostartDelay"){
	disableToolsChkBx(objSelect.selectedIndex);
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
	addOption(objSelect,intOther);
	if (objSelect.id == "autostartDelay"){
	  obj("autostartContinueWhenToolsRun").disabled = 
	    (objSelect.options[objSelect.selectedIndex].value == 0 ||
	     (objSelect.options[objSelect.selectedIndex].value == -1 &&
	      main.sx.vmAutoDflts("autostartDelay") == 0)
	     ) ? true : false; 
	} 
	return; 
      }
    }

    // User has chosen to  cancel or close the prompt
    objSelect.selectedIndex = prevSlt[objSelect.id];
  };
}

/* XXX VMs sequence - taken out for now
function strListOrderedVms(objVm) {
    
  var itVm;
  var sortArray = new Array;
  
  var arrOrdVmPri = new Array;
  var arrOrdVmLbl = new Array;
  
  arrOrdVmPri.push(0);
  arrOrdVmLbl.push("No Specific Order");
  
  var i = 0;
  for (itVm in main.sx.vms) {
    if (main.sx.vms[itVm]._hash!="console" && 
	main.sx.vms[itVm]._opt["autostartOrder"] > 0) {
      sortArray[i++] = main.sx.vms[itVm]._opt["autostartOrder"] + "," +
	main.sx.vms[itVm]._hash + "," + main.sx.vms[itVm]._dn;
    }
  }
  
  var ordSelection;
  var valBefore = "last";
  
  if ( sortArray.length > 1) { // this ordered machine is not the only one ordered
    
    // space
    //      arrOrdVmPri.push(-1);
    //      arrOrdVmLbl.push(" ");
    
    sortArray.sort(
		   function sortByPri(a,b) {
		     if (a.split(",")[0] < b.split(",")[0]) return -1;
		     if (a.split(",")[0] > b.split(",")[0]) return 1;
		     return 0;
		   }
		   ); 
    
    for (i = 0; i < sortArray.length ; i++) {
      if (sortArray[i].split(",")[1] == objVm._hash) {
	if (i < (sortArray.length-1)) {
	  valBefore = sortArray[i+1].split(",")[1];
	}
      } else {
	arrOrdVmPri.push(sortArray[i].split(",")[1]);
	arrOrdVmLbl.push((i+1) + ". "+sortArray[i].split(",")[2]);
      }
    }
    
    // space
    //      arrOrdVmPri.push(-1);
    //      arrOrdVmLbl.push(" ");
    
  }
  
  arrOrdVmPri.push("last");
  arrOrdVmLbl.push("All Unordered Virtual Machines");
  
  
  if (objVm._opt["autostartOrder"] == 0) {
    ordSelection = 0;
  } else {
    ordSelection = valBefore;
  }
  
  return htmlSlct("autostartOrder",arrOrdVmPri,ordSelection,arrOrdVmLbl);
}
*/
