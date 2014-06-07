
var pq; 
var mdlVms = null;
var cntrlMdlVms = null;
var genNum = -1;
var arrOrdered = new Array;
var arrUnordered = new Array;
var arrDisabled = new Array;

var ok = false; 
// true - ok button was clicked 

var data = false;
// false - data was not retrieved from the server yet, 
// true - data is ready to be worked on


// --------------------------------------------------------------------------

function exit(i) {
  if (parent.ctx == "sxVmStartupShutdown") {
    if (i == -1) {
      self.location.replace("/vmware/en/sxVmStartupShutdown.html?needUpdate=false");
    }  else if (i == -2) {
      self.location.replace("/vmware/en/sxVmStartupShutdown.html");
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
    ok = false; // XXX
    prev();
    cntrlMdlVms.repaint();
    //    exit(-1);
    return;
  }

  if (data && w.err.length > 0) { 
    // XXX
    data = false;
    cntrlMdlVms.repaint();
    return;
  }

  if (data) {
    data = false;
    pq.arg("hdnGenNum",w.genNum);
    initVmSeqControl(w);
    cntrlMdlVms.repaint();
  }
  
  if (ok) {

    // imediately update the main.sx.vms array with the new priorities
    // XXX do not implement, this page works directly with the backend, and best that 
    // XXX the parent page initiate getupdates so that main.sx.vms be correct !!!

    //   syncVmSeqControl(); 

    main.vmOptAutoStartObsrvr.ignr("sxVmSeqeuenceManager");
    exit(-2);
  }

}


// ---------------------------------------------------------------------------

function initPage() {

  window.top.resizeTo(725,580);

  parent.slctBtns("std");

  pq = new Query(document.forms[0]);

  //  initVmSeqControl();

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));


  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) { // executed when page loads
    editor.prv = null;
    setTimeout("parent.layoutCb()", 40);
    pq.arg("hdnVmStartupOp","editorRetrieve");
    data = true;
    pq.submit();
  };

  editor.nxt[0] = svgMsg;
  editor.exec = function () { // called upon OK button clicked

    //    pq.arg("hdnVms",stringMdlToSubmit());
    pq.arg("hdnVmStartupOp","editorSet");
    pq.arg("hdnOrdVms",stringMdlToSubmit("ordered"));
    pq.arg("hdnUnordVms",stringMdlToSubmit("unordered"));
    pq.arg("hdnDsblVms",stringMdlToSubmit("disabled"));
    // XXX right now they're always updated, and so this always shows up as a submit
    if ( pq.arg("hdnOrdVms")[0] == "" && 
	 pq.arg("hdnUnordVms")[0] == "" &&
	 pq.arg("hdnDsblVms")[0] == "" ) {  // no changes were made to the VMs order
      exit(-1);
    } else {
      ok = true; 
      pq.submit();
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
  //  cntrlMdlVms.repaint();

  main.vmOptAutoStartObsrvr.lstn("sxVmSeqeuenceManager", reloadVms);

}

// -------------------------------------------

function stringMdlToSubmit(strList) {
  
  var strHash = "";
  var nq = new Query();
  var arrVms; 
  var tmpVm;
  var tmpValStr;
  
  switch(strList) {
  case "ordered" :
    arrVms = mdlVms.getOrderedVms();
    break;
  case "unordered" :
    arrVms = mdlVms.getAutoNotOrdered();
    break;
  case "disabled" :
    arrVms = mdlVms.getNoAutoVms();
    break;
  }
  
  if (arrVms != null) {
    for (var j = 0; j < arrVms.length; j++) {
      tmpVm = arrVms[j];
      
      // XXX current backened implmenetation requires all VMs to be returned
      //	if ( tmpVm.isUpdated() ) { 
      
      // format as : vmdbPath, autostart
      tmpValStr = tmpVm.getVmHash() + "," + tmpVm.getAutoStartStop();
      
      nq.arg(j,tmpValStr);
      
      // XXX current backened implmenetation requires all VMs to be returned
      //	}
    }
    strHash = nq.toString();
  }
  
  return strHash;

}

function reloadVms(){
  window.alert("The virtual machine startup sequence has changed. " + 
	       "\nThe settings displayed here will be updated.");
  self.location.replace(self.location);
}

// ----------------------------------------------

function initVmSeqControl(w) {

  mdlVms = new OrderedVmListModel();

  mdlVms.clear();

  for(var i = 0; i < w.arrOrdered.length; i++){
    mdlVms.addVm(
		 new OrderedVm(
			       w.arrOrdered[i].id,
			       w.arrOrdered[i].name,
			       true)
		 );
    mdlVms.priVmLast(w.arrOrdered[i].id);
  }

  for(var i = 0; i < w.arrUnordered.length; i++){
    mdlVms.addVm(
		 new OrderedVm(
			       w.arrUnordered[i].id,
			       w.arrUnordered[i].name,
			       true)
		 );
  }
  
  for(var i = 0; i < w.arrDisabled.length; i++){
    mdlVms.addVm(
		 new OrderedVm(
			       w.arrDisabled[i].id,
			       w.arrDisabled[i].name
			       )
		 );
  }
  
  cntrlMdlVms = new OrderedVmListControl(mdlVms,
					 window.frames.frmOther,
					 window.frames.frmAny,
					 window.frames.frmSpecific
					 );
  
  /*
  var tmpVm;
  
  for (tmpVm in main.sx.vms) {
    if ( main.sx.vms[tmpVm]._hash != "console") {
      mdlVms.addVm(  
        new OrderedVm(
	  main.sx.vms[tmpVm]._hash,
	  main.sx.vms[tmpVm]._dn,
	  ((main.sx.vms[tmpVm].opt("autostart") == "none") ? false: true ),
	  main.sx.vms[tmpVm].opt("autostartOrder")
	)
      );
    }
  }

  var strOrderConflict = mdlVms.cleanPriorities();
  if (strOrderConflict != "") {
    window.alert("Some virtual machines in the server are set to" + 
      " autostart and autostop in a specific order, but have had the same order set !\n\n" +
      "The Management system has fixed the order, but" +
      " it is strongly suggested that you verify the new order is the desired one.\n\n" + 
      "The following is a list of virtual machines that were originally found to" +
      " start in the same order : \n" +
      strOrderConflict);
  }

  */

}

/* XXX was used when the page was updating the client side to save the roundtrip after
       a successful submition. Currently this is not used, as the main.sx.vms array is not
       used anymore.

function syncVmSeqControl() {

  var arrAllVms;
  var tmpVm;

  if (mdlVms == null || (arrAllVms = mdlVms.getAllVms()) == null) return;

  for (var i = 0; i < arrAllVms.length; i++) {
    tmpVm = arrAllVms[i];
    if (tmpVm.isUpdated() && main.sx.vms[tmpVm.getVmHash()]) {

      if ( !tmpVm.getAutoStartStop() ) { // vm was set NOT to automatically startup and stop
	main.sx.vms[tmpVm.getVmHash()].opt("autostart","none");
	main.sx.vms[tmpVm.getVmHash()].opt("autostartOrder",0);
	
	// don't touch autostop ! if it was none then nothing needs to be done,
	// if it was suspend, softpoweroff or poweroff, leave as is

	// main.sx.vms[tmpVm.getVmHash()].opt("autostop","none"); // XXX
	// main.sx.vms[tmpVm.getVmHash()].opt("autostopOrder",0);

      } else { // vm will automatically start and stop
	main.sx.vms[tmpVm.getVmHash()].opt("autostart","poweron");
	main.sx.vms[tmpVm.getVmHash()].opt("autostartOrder",tmpVm.getPriority());

	// if the autostop was already set to stop or suspend, leave as is
	if ( main.sx.vms[tmpVm.getVmHash()].opt("autostop") == "none") {
	  main.sx.vms[tmpVm.getVmHash()].opt("autostop","softpoweroff");
	}
	main.sx.vms[tmpVm.getVmHash()].opt("autostopOrder",tmpVm.getPriority());
      }

    }
  }

}
*/
