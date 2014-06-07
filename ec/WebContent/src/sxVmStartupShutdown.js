/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// the following will hold the VM for which we set the extra info bit to 2
// This reference is needed to later on toggle that extra info off 

var updatedVm = null;

// --------------------------------------------------------------------------

function initPage() {

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  
  parent.slctBtns("cls");
  
  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;
    parent.ctx = "sxVmStartupShutdown";
    
    var q = new Query(self.location.search);

    if (q.arg("needUpdate")[0] == "false") {
      fillPage();
      setTimeout("parent.layoutCb()", 40);
    } else {
      main.updateObsrvr.lstn("vmStartupShutdown", lstnr);
      
      // XXX hack - one VM's extra bit must be set to 2. 
      // getUpates (in main) sets an "allVms" variable to be 2 if one of 
      // the VM's extra bit contains 2. In turn, the backend will then push 
      // "extra bit 2 (options and devices)" for all registered VMs.

      // set one of the VMs extra bit to 2
      for (var i in main.sx.vms) { 
	// Toggle extra info on.
	if ((main.sx.vms[i].extra() & 2) == 0) {
	  main.sx.vms[i].extra(2);
	  updatedVm = main.sx.vms[i];
	  break;
	} else { // already contains 2, no need to iterate over other VMs
	  break;
	}
      }
      
      main.getUpdates();
    }
  };

  fillPage = function() {

    var dom = new Object();

    dom.allVmAuto = obj("allVmAuto");
    dom.allVmContStart = obj("allVmContStart");
    dom.allVmsContStop = obj("allVmsContStop");

    dom.aSeqEditSpc = obj("aSeqEditSpc");
    dom.aSeqEditAny = obj("aSeqEditAny");
    dom.aDisSeqEditSpc = obj("aDisSeqEditSpc"); 
    dom.aDisSeqEditAny =  obj("aDisSeqEditAny");

    dom.spcOrderList = obj("divSpcOrder");
    dom.anyOrderList = obj("divAnyOrder");

    rndrDefaults(dom);
    rndrSequence(dom);

    main.vmOptAutoStartObsrvr.lstn("vmStartupShutdown", fillPage);

  };

  editVmSequence = function (strPage) {
    switch (strPage) {
    case "vmStartupShutdownDefaults":
      self.location.replace("sxVmStartupShutdownDefaults.html");
      if (main.sx._prodId == "gsx") {
        parent.helpurl = "vserver/sxVmStartupShutdownHelp.html";
      } else {
        parent.helpurl = "esx/sxVmStartupShutdownHelp.html";
      }
      break;
    case "vmSequenceEditor":
      self.location.replace("sxVmSequenceManager.html");
      if (main.sx._prodId == "gsx") {
        parent.helpurl = "vserver/sxVmStartupShutdownHelp.html";
      } else {
        parent.helpurl = "esx/sxVmStartupShutdownHelp.html";
      }
      break;
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

}

function lstnr() {
  // ignore the observer for VM options updates, was used to indicate when main
  // was ready with options info for all VMs 

  main.updateObsrvr.ignr("vmStartupShutdown");

  // toggle off the extra info if was set to 2
  if ( updatedVm != null && ((updatedVm.extra() & 2) == 2) ) {
    updatedVm.extra(2);
  }

  fillPage();
}

function rndrDefaults(objDom) {

  var intMins = "";

  if (main.sx.vmAutoDflts("autostartDelay") == 0) {
    objDom.allVmContStart.innerHTML = "Don't Wait";
  } else {
    intMins = main.sx.vmAutoDflts("autostartDelay")/60;
    objDom.allVmContStart.innerHTML =  intMins + " minute" + 
      ((intMins == 1) ? "" : "s" ); 
    if (main.sx.vmAutoDflts("autostartContOnTools") == 1) {
      objDom.allVmContStart.innerHTML += ", or when VMware Tools starts";
    }
  
  }
  
  if (main.sx.vmAutoDflts("autostopDelay") == 0) {
    objDom.allVmsContStop.innerHTML = "Don't Wait";
  } else {
    intMins = main.sx.vmAutoDflts("autostopDelay")/60;
    objDom.allVmsContStop.innerHTML = intMins + " minute" + 
      ((intMins == 1) ? "" : "s" ) + " at most"; 
  }



  if (main.sx.vmAutoDflts("autostart") == 1) {
    objDom.allVmAuto.innerHTML =  " Enabled";
    objDom.allVmAuto.css("color","");  

  } else {
    objDom.allVmAuto.innerHTML =  " Disabled";
    objDom.allVmAuto.css("color","red");  

    objAtt(objDom.aSeqEditSpc, "class", "dsbld");
    objAtt(objDom.aSeqEditSpc, "href", "javascript:;");
    objAtt(objDom.aSeqEditAny, "class", "dsbld");
    objAtt(objDom.aSeqEditAny, "href", "javascript:;");

    objDom.spcOrderList.css("color","silver");
    objDom.anyOrderList.css("color","silver");
    objDom.allVmContStart.css("color","silver");
    objDom.allVmsContStop.css("color","silver");
  }

}

function rndrSequence(objDom) {

  var arrSort = new Array;
  var i = 0, j = 0;
  var tmpVm;
  
  var strOrdVms = "";
  var strAnyVms = "";
  
  for (tmpVm in main.sx.vms) {
    if (main.sx.vms[tmpVm].hash() != "console" && 
	main.sx.vms[tmpVm].opt("autostart") != "none") {
	arrSort[i++] = main.sx.vms[tmpVm];
    }
  }
    
  if (arrSort != null && arrSort.length != 0) {
    arrSort.sort( function sortByPri(a,b) {
      if (a.opt("autostartOrder") > b.opt("autostartOrder")) return 1;
      if (a.opt("autostartOrder") < b.opt("autostartOrder")) return -1;
      return 0;
    }
		  );
    
    for (j = 0, i = 0; i < arrSort.length; i++) {
      if (arrSort[i].opt("autostartOrder") == 0) {
	strAnyVms += "<div>" + arrSort[i].dn() + "</div>";
      } else {
	strOrdVms += "<div>" + (++j) + ". " + arrSort[i].dn() + "</div>";
      }
    }
  }
  
  
  if (strAnyVms == "") {
    strAnyVms = 
      "None";  
  }
  
  if (strOrdVms == "") {
    strOrdVms = 
      "None";  
  }
  
  objDom.spcOrderList.innerHTML = strOrdVms;
  objDom.anyOrderList.innerHTML = strAnyVms;
  
}
