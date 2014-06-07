/*******************************************************************************
*
* OrderedVmListControl
* 
* - Is the gui control that handles ordering of VM startup/shutdown sequence.
*
* - The control feeds off of an OrderedVmListModel object and ties into 3 DOM
*   frames for painting its contents. 
* - Each frame holds a different view of the model.
*   The "Other" list - A model view presenting all VMs which are not set to autostart 
*     with the sx server.
*   The "Any" list - A model view presenting all VMs which are set to autostart
*     with the sx server, but at no specific (any) order.
*   The "Specified" ("specific") list - A model view presenting all VMs which are 
*     set to autostart with the sx server at a specific start order.
*
* - GUI arrows are used to move VMs between the 3 different lists while invoking 
*     the apprpriate operations on the underlying data model. 
*
* - The following are the ListControl properties :
*  a. mdlVm - the data model for the GUI control (of type OrderedVmListModel)
*  b. frmOther - the frame to present the "Other" list in. 
*  c. frmAnyOrd - the frame to present the "Any" list in. 
*  d. frmSpcOrd - the frame to present the "Specified" list in.
*  e. cntrlSltVms - an array containing at any point of time, the selected GUI
*                    checkbox objects anywhere on the screen.
*  f. frmClicked - the currently clicked frame with which the end user is interacting.
*  g. boolMultSelection - is multiple seletion allowed or not.
* 
* - The following methods are available from the ListControl :
*  1. setModel - set the data model for this control. Model is of type OrderedVmListModel.
*  2. getModel - retrieve the data model for this control.
*  3. clearModel - clears the data model for this control.
*
*  4. selectVms - Handles the selection of VMs on the screen.
*  5. deselectVms - Handles the deselection of VMs on the screen
*  3. getSelectedVms - Retrieves the array of selected VM 
*                      checkboxes on the screen.
*  4. clearSelectedVms - Brute force clears the array of selected 
*                        VM checkboxes on the screen
*  5. guiSelectVm - Handles the highlighting features of a selected VM.
*  6. guiDeselectVm - Takes care of de-highlighting a selected VM.
*  7. guiAllowedOperations - Changes the state of the control's allowed operations.
*  8. guiToggleArrows - Toggles the arrow states (enabled/disabled)
*  9. repaint - repaints all lists in their corresponding frame.
*  10. repaintList - repaints a list in its corresponding frame.
*  11. setGuiClickedFrame - set the clicked frame property of the control.
*  12. getGuiClickedFrame - retrieves the clicked frame property of the control.
*  13. guiSwitchClickedFrame - sets the clicked frame property of the control, 
*        while also toggles the arrow state on screen. 
*  14. clkOtherToSpc - As implied by name. Handler.
*  15. clkSpcToOther - As implied by name. Handler.
*  16. clkOtherToAny - As implied by name. Handler.
*  17. clkAnyToOther - As implied by name. Handler.
*  18. clkSpcToAny - As implied by name. Handler.
*  19. clkAnyToSpc - As implied by name. Handler.
*  20. clkSpcUp - As implied by name. Handler.
*  21. clkSpcDn - As implied by name. Handler.
*
*******************************************************************************/

function OrderedVmListControl(mdlListModel, frmOtherList, frmAnyList, frmSpcList) {
  
  //---------- constants --------------------

  //---------- declarations -----------------

  this.mdlVm = null;

  this.frmOther = null;
  this.frmAnyOrd = null;
  this.frmSpcOrd = null;

  this.cntrlSltVms = new Array;
  this.frmClicked = null;
  this.boolMultSelection = false;
  
  this.getModel = OrderedVmListControl_getModel;
  this.setModel = OrderedVmListControl_setModel;
  this.clearModel = OrderedVmListControl_clearModel;


  //---------- public declarations  -----------------

  this.selectVms = OrderedVmListControl_selectVms;
  this.deselectVms = OrderedVmListControl_deselectVms;
  this.getSelectedVms = OrderedVmListControl_getSelectedVms;
  this.clearSelectedVms = OrderedVmListControl_clearSelectedVms;

  this.guiSelectVm = OrderedVmListControl_guiSelectVm;
  this.guiDeselectVm = OrderedVmListControl_guiDeselectVm;
  this.guiAllowedOperations = OrderedVmListControl_guiAllowedOperations;
  this.guiToggleArrows = OrderedVmListControl_guiToggleArrows;
  this.repaint = OrderedVmListControl_repaint;
  this.repaintList = OrderedVmListControl_repaintList;
  this.setGuiClickedFrame = OrderedVmListControl_setGuiClickedFrame;
  this.getGuiClickedFrame = OrderedVmListControl_getGuiClickedFrame;
  this.guiSwitchClickedFrame = OrderedVmListControl_guiSwitchClickedFrame;

  this.clkOtherToSpc = OrderedVmListControl_clkOtherToSpc;
  this.clkSpcToOther = OrderedVmListControl_clkSpcToOther;
  this.clkOtherToAny = OrderedVmListControl_clkOtherToAny;
  this.clkAnyToOther = OrderedVmListControl_clkAnyToOther;
  this.clkSpcToAny = OrderedVmListControl_clkSpcToAny;
  this.clkAnyToSpc = OrderedVmListControl_clkAnyToSpc;
  this.clkSpcUp = OrderedVmListControl_clkSpcUp;
  this.clkSpcDn = OrderedVmListControl_clkSpcDn;


  // Private declarations

  this.cleanSelectedVms = OrderedVmListControl_cleanSelectedVms;
  this.saveSelectedVms = OrderedVmListControl_saveSelectedVms;


  //---------- initialization -------------------

  if (arguments.length == 0 || mdlListModel == null) {
    //    window.alert("devTime Alert : While creating a VMList control - " + 
    //		 "no data model was given !");
    return null;
  } else {
    this.mdlVm = mdlListModel;
  }

  if (frmOtherList != null && frmAnyList != null && frmSpcList != null) {
    this.frmOther = frmOtherList;
    this.frmAnyOrd = frmAnyList;
    this.frmSpcOrd = frmSpcList;
  }


  //---------- "private" functions -------------

  // "Javascript world" will let you call these functions, there's no real need
  // for you to do so. Please don't, these are for internal use.
  // Regardless, no harm will come to the system if you do call the below functions.


  // Cleans any null slots in the selected VMs array, created by previous 
  // successful deselect calls

  function OrderedVmListControl_cleanSelectedVms() {
    var arrTmp = new Array;
    var i,j;
    for (i = 0, j = 0; i < this.cntrlSltVms.length ; i++) {
      if ( this.cntrlSltVms[i] != null ) {
	arrTmp[j] = this.cntrlSltVms[i];
	j++;
      }
    }
    this.clearSelectedVms();
    this.cntrlSltVms = arrTmp;
  }


    // Returns a string comprising of all currently selected VMs in the control,
    // delimited by "," character. 

  function OrderedVmListControl_saveSelectedVms() {
    var strSave = "";
    if (this.cntrlSltVms != null && this.cntrlSltVms.length != 0) {
      for (var k =0; k < this.cntrlSltVms.length; k++) {
	strSave += this.cntrlSltVms[k].id + ",";
      }
    }
    return strSave;
  }



  //---------- "public" functions -------------
  // Use freely.


// -------------------------------------------------------------------------

    // Set the data model for this control. Data model should be of
    // type OrderedVmListModel.
    // Returns true if succeeded, false otherwise.

  function OrderedVmListControl_setModel(mdlListModel) {
    if (mdlListModel == null) {
      return false;
    } else {
      this.clearModel();
      this.mdlVm = mdlListModel;
    }
    return true;
  }


// -------------------------------------------------------------------------

    // Retrieves the data model for this control

  function OrderedVmListControl_getModel() {
    return this.mdlVm;
  }


// -------------------------------------------------------------------------

    // Brute force clears the data model for this control.

  function OrderedVmListControl_clearModel() {
    this.mdlVm = null;
  }


// -------------------------------------------------------------------------

    // Select a VM that was just clicked.
    // Flow :
    //  1. If needed, deselect all currently selected VMs on the screen.
    //  2. Add a reference to the VMs related checkbox control
    //  3. GUI select (highlight, etc) the related DOM controls of all selected VMs
    //  4. Toggle the allowed operations (arrow states).

  function OrderedVmListControl_selectVms(strVmId,strFrame,boolMultSelection) {

    // Sanity check
    //    if ( !this.mdlVm.exists(strVmId) ) {
    //return false;
    //  }


    // tell future selection what mode of selection is permitted (multiple/single)
    // boolMultSelection true will mean that further selections can be made
    // on top of the currently selected VMs. MultSelection false will mean that
    // next selection must deselect the currently selected.
    // default and  error recovery value is false

    var firstMultiSelect;

    if (boolMultSelection != null && boolMultSelection == true) {
      if (!this.boolMultSelection) {
	firstMultiSelect = true;
      }
      this.boolMultSelection = boolMultSelection;
    } else {
      this.boolMultSelection = false;
    }

    // are there any VMs currently selected ? 
    // If so, there's a need to deselect them if either 
    //  multiple selection is not allowed or
    //  this is the first of a new multi selection or
    //  a different frame has just been clicked  

    if ( !this.boolMultSelection || 
	 firstMultiSelect ||        
	 (this.frmClicked != null && strFrame != this.frmClicked) 
	  ) {

      if (this.cntrlSltVms.length != 0) { // there are VMs currently selected
	// deselect the currently selected VMs
	for (var j = 0; j < this.cntrlSltVms.length ; j++) {
	  if (this.cntrlSltVms[j] != null) this.guiDeselectVm(this.cntrlSltVms[j]);
	}
	this.clearSelectedVms();
      }

    }
    
    this.frmClicked = strFrame;

    // add the VM to the selected VMs collection, by adding a reference to the 
    // GUI checkbox related to it
    this.cntrlSltVms[this.cntrlSltVms.length] = eval("window.frames." + 
						     strFrame + ".document." +
						     "getElementsByTagName('input')" + 
						     "['" + strVmId +"']");

    // guiSelect all selected VMs
    for (var k = 0; k < this.cntrlSltVms.length ; k++) {
      if (this.cntrlSltVms[k] != null) this.guiSelectVm(this.cntrlSltVms[k]);
    }
    
    // guiUpdate allowed operations, manifests in toggling the arrows on screen
    this.guiAllowedOperations(strFrame);

    return true;
  }


// -------------------------------------------------------------------------

    // Deselect a VM that was just clicked.
    // If the deselected VM was the only one selected, the allowed operations 
    // switch to none, since no object can be worked on. Meaning all arrows will 
    // be deactivated.
  
  function OrderedVmListControl_deselectVms(strVmId,strFrame) {
    var i;
    for (i = 0; i < this.cntrlSltVms.length ; i++) {
      if (this.cntrlSltVms[i] != null ) {
	if (this.cntrlSltVms[i].id == strVmId) {
	  this.guiDeselectVm(this.cntrlSltVms[i]);
	  this.cntrlSltVms[i]=null;
	} else {
	  this.guiSelectVm(this.cntrlSltVms[i]);	
	}
      }
    }
    this.cleanSelectedVms();
    if ( this.getSelectedVms() == null ) { 
      // VM deselected was the last selected VM
      this.guiAllowedOperations("none");
    }
  }


// -------------------------------------------------------------------------

    // deselects a VM on the screen by removing its highlight features.
    // XXX Better use switching CSS classes

  function OrderedVmListControl_guiDeselectVm(objToDeselect) {
    objToDeselect.checked=false;
    var vmRow = objToDeselect.parentNode.parentNode;
    vmRow.style.color = "black";
    vmRow.style.background = "white";
  }


// -------------------------------------------------------------------------

    // selects a VM on the screen by adding highlight features to it
    // XXX Better use switching CSS classes

  function OrderedVmListControl_guiSelectVm(objToSelect) {
    objToSelect.checked=true;
    var vmRow = objToSelect.parentNode.parentNode;
    vmRow.style.color = "white";
    vmRow.style.background = "navy";
  }


// -------------------------------------------------------------------------

    // Brute force clears the array of selected VM checkboxes 

  function OrderedVmListControl_clearSelectedVms() {
    this.cntrlSltVms = null;
    this.cntrlSltVms = new Array;
  }


// -------------------------------------------------------------------------

    // Retrieves the array of selected VM checkboxes 
    // returns null if array is empty (Mozilla/IE crossB)

  function OrderedVmListControl_getSelectedVms() {
    return (this.cntrlSltVms.length == 0) ? null : this.cntrlSltVms;
  }


// -------------------------------------------------------------------------

    // Handles the GUI repainting of a list presentation
    // - The strSaved is given in order for the list to reselect 
    // previously selected VMs after redrawing the list.

  function OrderedVmListControl_repaintList(strListName,strSaved) {
    var strHtml = "";
    var i = 0;
    var tmpVm;
    var arrList;
    var docBody;
    var multiSelect;
    var numbered;


    // The "specified" list differs from the rest in the fact that it numbers
    // its VMs when listing them.

    switch(strListName) {
      case this.frmAnyOrd.name :
	arrList = this.mdlVm.getAutoNotOrdered();
	doc = this.frmAnyOrd.document;
	multiSelect = true;
	numbered = false;
	break;
      case this.frmOther.name :
	arrList = this.mdlVm.getNoAutoVms();
	doc = this.frmOther.document;
	multiSelect = true;
	numbered = false;
	break;
      case this.frmSpcOrd.name :
	arrList = this.mdlVm.getOrderedVms();
	doc = this.frmSpcOrd.document;
	multiSelect = true;
	numbered = true;
	break;
      default :
	// XXX
    }

    if (arrList != null) {
      for (i = 0; i < arrList.length; i++) {
	tmpVm = arrList[i];
	strHtml += "<tr id=\"tr"+tmpVm.getVmHash() +
	  "\" name=\"tr"+tmpVm.getVmHash()+"\">";
	strHtml += " <td><input id=\""+tmpVm.getVmHash() + 
	  "\" name=\""+tmpVm.getVmHash()+"\"" + 
	  " type=\"checkbox\" onclick=\"toggleSelection(this,"+multiSelect+")\" />";
	strHtml += "</td><td width=\"100%\"><span id=\""+tmpVm.getName()+"\">";
	if (numbered) strHtml += (i+1) + ". ";
	strHtml += tmpVm.getName() + "</span></td></tr>";
      }
    }

      // now repaint
    doc.body.innerHTML = "<table id='tblVmList' name='tblVmList'" +
      " border='0' cellpadding='0' cellspacing='0'>" +
      strHtml + 
      "</table>";

    //***********************************
    var arrReselect;
    if ( strSaved != null ) {
      arrReselect = doc.getElementsByTagName("input");
      for (var m=0; m < arrReselect.length; m++) {
	if (strSaved.match(arrReselect[m].id+",")) {
	  this.cntrlSltVms[this.cntrlSltVms.length] = arrReselect[m];
	}
      }

      // guiSelect all selected VMs
      for (var k = 0; k < this.cntrlSltVms.length ; k++) {
	if (this.cntrlSltVms[k] != null) this.guiSelectVm(this.cntrlSltVms[k]);
      }

      // scroll to the selected VM so it's visible.

      // XXX focus() is used here rather than scrollTo methods in order
      // to support crossB. Works fine in IE, need to find a better solution 
      // for mozilla   
      this.cntrlSltVms[0].focus(); 
    }
    //************************************
  }


// -------------------------------------------------------------------------

    // Repaint the whole control.

  function OrderedVmListControl_repaint() {
    this.repaintList(this.frmOther.name);
    this.repaintList(this.frmAnyOrd.name);
    this.repaintList(this.frmSpcOrd.name);
  }


// -------------------------------------------------------------------------

    // Allowed operations state machine

    /* allowed operations bitmap
     *
     * lsb
     * x - up priority 1
     * x - down priority 2  
     * x - specified to other 4
     * x - other to specified 8
     * x - any to specified 16
     * x - specified to any 32
     * x - any to other 64
     * x - other to any 128
     * msb
     *
     * for example when clicking a VM on the "other" list, the operations allowed
     * are moving out of this list and moving into it. Allowed opeartions bit map would
     * be 10001000 or 136
     */


  // These strings are used to refer to the arraows on the HTML page, and so should
  // be changed together.

  var opState = new Array;
  opState[1]   = "UpPri";
  opState[2]   = "DnPri";
  opState[4]   = "SpcToOther";
  opState[8]   = "OtherToSpc";
  opState[16]  = "AnyToSpc";
  opState[32]  = "SpcToAny";
  opState[64]  = "AnyToOther";
  opState[128] = "OtherToAny";


  function OrderedVmListControl_guiAllowedOperations(strFrame) {
    var strFrm = (strFrame == null) ? "none" : strFrame;
    switch(strFrm) {
      case this.frmOther.name : // 10001000
	this.guiToggleArrows(136);
	break;
      case this.frmSpcOrd.name : // 00100111
	this.guiToggleArrows(39);
	break;
      case this.frmAnyOrd.name : // 01010000
	this.guiToggleArrows(80);
	break;
      case "none" : // 00000000
      default:
	this.guiToggleArrows(0);
	break;
    }
  }


// -------------------------------------------------------------------------

    // Toggles the arrow states on the screen.
    // Does not change the hrefs of the controls, but rather hides and shows
    // the active arrow link (+ image) and the non active arrow link (+image).

  function OrderedVmListControl_guiToggleArrows(bitMap) {
    var docLinks = window.document.getElementsByTagName("a");

    for (var i = 0; i <= 7; i++) {
      if ((Math.pow(2,i)&bitMap) == 0) {
        docLinks["a"+opState[Math.pow(2,i)]].style.display="none";
        docLinks["aDis"+opState[Math.pow(2,i)]].style.display="";
      } else {
        docLinks["a"+opState[Math.pow(2,i)]].style.display="";
        docLinks["aDis"+opState[Math.pow(2,i)]].style.display="none";
      }
    } 

  }


    // XXX 
    // In general the following arrow click methods could be condensed into much fewer 
    // lines of code and into a couple of methods. At this point, we are not 
    // clear on the evolution of this control, and therefor they are kept apart
    // for dev felxibility in the near future.
    // XXX

// -------------------------------------------------------------------------

    // Handles moving a VM from no autostart list to the autostart with 
    // specified order list.
    //  - Operations on the model would be 
    //  1. Setting the VMs autostart to true
    //  2. Pushing the VM to the end of the startup order
    //  - Both frames need repainting. The specified order frame needs to 
    //    GUI reselect the VMs that were just added to it.

  function OrderedVmListControl_clkOtherToSpc() {
    var vmId;
    if (this.cntrlSltVms.length == 0) { // no VMs selected in list
      return;
    } else {
      for (var i=0; i < this.cntrlSltVms.length; i++) {
	vmId = this.cntrlSltVms[i].id;
	this.mdlVm.setVmAuto(vmId,true);
	this.mdlVm.priVmLast(vmId);
      }
    }
    var strSave = this.saveSelectedVms();
    this.clearSelectedVms()
    this.repaintList(this.frmOther.name);
    this.repaintList(this.frmSpcOrd.name,strSave);
    this.guiSwitchClickedFrame(this.frmSpcOrd.name);
  }


// -------------------------------------------------------------------------

    // Handles moving a VM from the autostart with specified order list to 
    // the non autostarting VMs list.
    //  - Operations on the model would be 
    //  1. Setting the VMs autostart to false
    //  2. Removing its autostart order priority
    //  - Both frames need repainting. The "other" frame needs to 
    //    GUI reselect the VMs that were just added to it.

  function OrderedVmListControl_clkSpcToOther() {
    var vmId;
    if (this.cntrlSltVms.length == 0) { // no VMs selected in list
      return;
    } else {
      for (var i = 0; i < this.cntrlSltVms.length; i++) {
	vmId = this.cntrlSltVms[i].id;
	this.mdlVm.unpriVm(vmId);
	this.mdlVm.setVmAuto(vmId,false);
      }
    }
    var strSave = this.saveSelectedVms();
    this.clearSelectedVms()
    this.repaintList(this.frmSpcOrd.name);
    this.repaintList(this.frmOther.name,strSave);
    this.guiSwitchClickedFrame(this.frmOther.name);
  }



// -------------------------------------------------------------------------

    // Handles moving a VM from the non autostarting VMs list to 
    // the autostarting VMs list.
    //  - Operations on the model would be 
    //  1. Setting the VMs autostart to true
    //  - Both frames need repainting. The "any" frame needs to 
    //    GUI reselect the VMs that were just added to it.

  function OrderedVmListControl_clkOtherToAny() {
    var vmId;
    if (this.cntrlSltVms.length == 0) { // no VMs selected in list
      return;
    } else {
      for (var i = 0; i < this.cntrlSltVms.length; i++) {
	vmId = this.cntrlSltVms[i].id;
	this.mdlVm.setVmAuto(vmId,true);
      }
    }
    var strSave = this.saveSelectedVms();
    this.clearSelectedVms()
    this.repaintList(this.frmOther.name);
    this.repaintList(this.frmAnyOrd.name,strSave);
    this.guiSwitchClickedFrame(this.frmAnyOrd.name);
  }


// -------------------------------------------------------------------------

    // Handles moving a VM from the autostarting VMs list to 
    // the non autostarting VMs list.
    //  - Operations on the model would be 
    //  1. Setting the VMs autostart to false
    //  - Both frames need repainting. The "other" frame needs to 
    //    GUI reselect the VMs that were just added to it.

  function OrderedVmListControl_clkAnyToOther() {
    var vmId;
    if (this.cntrlSltVms.length == 0) { // no VMs selected in list
      return;
    } else {
      for (var i = 0; i < this.cntrlSltVms.length; i++) {
	vmId = this.cntrlSltVms[i].id;
	this.mdlVm.setVmAuto(vmId,false);
      }
    }
    var strSave = this.saveSelectedVms();
    this.clearSelectedVms()
    this.repaintList(this.frmAnyOrd.name);
    this.repaintList(this.frmOther.name,strSave);
    this.guiSwitchClickedFrame(this.frmOther.name);
  }


// -------------------------------------------------------------------------

    // Handles moving a VM from the sequential autostarting VMs list to 
    // the autostarting with no order VMs list.
    //  - Operations on the model would be 
    //  1. Unprioritizing the VM
    //  - Both frames need repainting. The "any" frame needs to 
    //    GUI reselect the VMs that were just added to it.

  function OrderedVmListControl_clkSpcToAny() {
    var vmId;
    if (this.cntrlSltVms.length == 0) { // no VMs selected in list
      return;
    } else {
      for (var i = 0; i < this.cntrlSltVms.length; i++) {
	vmId = this.cntrlSltVms[i].id;
	this.mdlVm.unpriVm(vmId);
      }
    }
    var strSave = this.saveSelectedVms();
    this.clearSelectedVms()
    this.repaintList(this.frmSpcOrd.name);
    this.repaintList(this.frmAnyOrd.name,strSave);
    this.guiSwitchClickedFrame(this.frmAnyOrd.name);
  }


// -------------------------------------------------------------------------

    // Handles moving a VM from the autostarting VMs list to 
    // the sequential autostarting VMs list.
    //  - Operations on the model would be 
    //  1. Pushing the selected VMs to the end of the autostart order
    //  - Both frames need repainting. The "specified order" frame needs to 
    //    GUI reselect the VMs that were just added to it.

  function OrderedVmListControl_clkAnyToSpc() {
    var vmId;
    if (this.cntrlSltVms.length == 0) { // no VMs selected in list
      return;
    } else {
      for (var i = 0; i < this.cntrlSltVms.length; i++) {
	vmId = this.cntrlSltVms[i].id;
	this.mdlVm.priVmLast(vmId);
      }
    }
    var strSave = this.saveSelectedVms();
    this.clearSelectedVms()
    this.repaintList(this.frmAnyOrd.name);
    this.repaintList(this.frmSpcOrd.name,strSave);
    this.guiSwitchClickedFrame(this.frmSpcOrd.name);
  }


// -------------------------------------------------------------------------

    // Handles moving a VM up one spot in the autostart order  
    // Note that if one of the selected VMs is already highest in the order
    // No VMs will move up.

  function OrderedVmListControl_clkSpcUp() {
    var vmId;
    var arrTmp;
    var intHighestPri = -1;
    var highestSelected = false;
    if (this.cntrlSltVms.length == 0) { // no VMs selected in list
      return;
    } else { // check if any of the selected VMs is already first in order
      tmpArr = new Array;
      intHighestPri = this.mdlVm.getHighestPriority();
      for (var i = 0; i < this.cntrlSltVms.length; i++) {
	if (this.mdlVm.getVm(this.cntrlSltVms[i].id).getPriority() ==
	    intHighestPri ) {
	  highestSelected = true;
	  break;
	} else {
	  tmpArr[tmpArr.length] = this.mdlVm.getVm(this.cntrlSltVms[i].id);
	}
      }
      if (!highestSelected) { // if none of the machines is the first in order
	// sorting must take place first, so that atomic moving up will be performed
	// on higher priority machines first.
	tmpArr.sort(  function sortByPri(a, b) {
	                if (a.getPriority() < b.getPriority()) return -1;
			if (a.getPriority() > b.getPriority()) return 1;
			return 0;
	              }
		   );
	// call on the model to move up the priority of each selected VM
	for (var j = 0; j < tmpArr.length; j++) {
	  this.mdlVm.priUpVm(tmpArr[j].getVmHash());
	}
	var strSave = this.saveSelectedVms();
	this.clearSelectedVms();
	this.repaintList(this.frmSpcOrd.name,strSave);
	this.repaintList(this.frmAnyOrd.name); //XXX
	this.guiAllowedOperations(this.frmSpcOrd.name);
      }
    }
  }


// -------------------------------------------------------------------------

    // Handles moving a VM down one spot in the autostart order  
    // Note that if one of the selected VMs is already lowest no selected VMs 
    // will move down.

  function OrderedVmListControl_clkSpcDn() {
    var vmId;
    var arrTmp;
    var intLowestPri = -1;
    var lowestSelected = false;
    if (this.cntrlSltVms.length == 0) { // no VMs selected in list
      return;
    } else { // check if any of the selected VMs is already the lowest in the order
      tmpArr = new Array;
      intLowestPri = this.mdlVm.getLowestPriority();
      for (var i = 0; i < this.cntrlSltVms.length; i++) {
	if (this.mdlVm.getVm(this.cntrlSltVms[i].id).getPriority() ==
	    intLowestPri ) {
	  lowestSelected = true;
	  break;
	} else {
	  tmpArr[tmpArr.length] = this.mdlVm.getVm(this.cntrlSltVms[i].id);
	}
      }

      if (!lowestSelected) { // none of the selected is lowest in the order
	// sorting must be performed, so that lower priority VMs will be moved
	// down first.
	tmpArr.sort(  function sortByPri(a, b) {
	                if (a.getPriority() > b.getPriority()) return -1;
			if (a.getPriority() < b.getPriority()) return 1;
			return 0;
	              }
		   );
	// call on the model to lower the priority of each selected VM
	for (var j = 0; j < tmpArr.length; j++) {
	  this.mdlVm.priDnVm(tmpArr[j].getVmHash());
	}

	var strSave = this.saveSelectedVms();
	this.clearSelectedVms();
	this.repaintList(this.frmSpcOrd.name,strSave);
	//XXX The any list needs repainting if the down arrow moves the last VM
	// in the order to from the specified order list to the any order list
	this.repaintList(this.frmAnyOrd.name); 
	this.guiAllowedOperations(this.frmSpcOrd.name);
      }
    }
  }


// -------------------------------------------------------------------------

    // sets the control's clicked frame property

  function OrderedVmListControl_setGuiClickedFrame(frmUsed) {
    this.frmClicked = frmUsed;
  }


// -------------------------------------------------------------------------

    // retrieves the control's clicked frame property
    
  function OrderedVmListControl_getGuiClickedFrame() {
    return this.frmClicked;
  }


// -------------------------------------------------------------------------

    // Sets the control's clicked frame property, while also toggling the allowed
    // operations and arrows state.

  function OrderedVmListControl_guiSwitchClickedFrame(strFrame) {
    this.guiAllowedOperations(strFrame);
    this.setGuiClickedFrame(strFrame);
  }

}  // end OrderedVmListControl object
