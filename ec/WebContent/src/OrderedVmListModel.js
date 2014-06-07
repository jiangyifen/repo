/*
 * @todo
 * 1. define protocol with backend, and provide collect method for creating the 
 * client->server operations string.
 * 2. If tailored speicifically to this UI screen, consider the construction of 
 *    3 arrays : non autostarted, autostarted no priority, and 
 *    autostarted prioritized. All actions will then act upon moving between these 
 *    arrays, the unite of which constitues the whole VM pool
 */


var VM_PRIORITY_GAP = 10;
var VM_PRIORITY_DIF = 0.5;


/*******************************************************************************
*
* OrderedVmListModel Object
* 
* - Is the data model for the GUI control that handles ordering of 
* VM startup/shutdown sequence.
*
* - The model consists of an array of OrderedVm objects, and provides 
*   the following :
* 
*  1. addVm - add a virtual machine object (OrderedVm) to the model.
*  2. exists - checks if a VM exists in the model
*  3. existsIndex - checks if a VM exists in the model and returns its index
*  4. getVm - retreives an VM object from the model
*  6. delVm - deletes a VM object from the model
*  7. setVmAuto - toggles the autostart feature of the VM between true and false
*  8. unpriVM - unsets the VMs priority in the startup/shutdown sequence
*  9. priVMFirst - sets the VM to startup first and shutdown last
*  10. priVmLast - sets the VM to startup last and shutdown first
*  11. priUpVm - moves the VM one priority higher in the autostart sequence
*  12. priDnVm - moves the VM one priority lower in the autostart sequence
*  
*  13. clear - clears the model of all VMs
*  14. clean - cleans the model of any previously deleted VMs
*  15. toString - returns a human readable string reperesenting the model
*  16. sortByPriority - sorts the model by VM startup/shutdown priorities
*  17. sortByVmName - sorts the model by VM names
*  18. getNumVms - returns the number of VMs in the model
*  19. getHighestPriority - returns the highest priority in the model
*  20. getLowestPriority - returns the lowest priority in the model
*
*  21. getAllVms - returns all the VMS in the model
*  22. getOrderedVms - returns an array containing all VMs in the sequence
*  23. getUnorderedVms - returns an array of all VMS not in the sequence
*  24. getAutoVms - returns an array of all VMs which autostart/stop
*  25. getNoAutoVms - returns an array of all VMs which do not autostart/stop
*  26. getAutoNotOrdered - returns an array containing all VMs that autostart with 
*                         no specific order
*
*
********************************************************************************/

function OrderedVmListModel() {

  //---------- constants --------------------

  //---------- declarations -----------------

  this.arrVm = new Array;

  this.addVm = VmOrderControlModel_addVm;
  this.exists = VmOrderControlModel_exists;
  this.existsIndex = VmOrderControlModel_existsIndex;
  this.getVm = VmOrderControlModel_getVm;
  this.delVm = VmOrderControlModel_delVm; 
  this.setVmAuto = VmOrderControlModel_setVmAuto;
  this.unpriVm = VmOrderControlModel_unpriVm;
  this.priVmFirst = VmOrderControlModel_priVmFirst; 
  this.priVmLast = VmOrderControlModel_priVmLast;
  this.priUpVm = VmOrderControlModel_priUpVm;
  this.priDnVm = VmOrderControlModel_priDnVm;
  this.cleanPriorities = VmOrderControlModel_cleanPriorities;

  this.clear = VmOrderControlModel_clear;
  this.clean = VmOrderControlModel_clean;
  this.toString = VmOrderControlModel_toString;
  this.sortByPriority = VmOrderControlModel_sortByPriority;
  this.sortByVmName = VmOrderControlModel_sortByVmName;
  this.getNumVms = VmOrderControlModel_getNumVms;
  this.getHighestPriority = VmOrderControlModel_getHighestPriority;
  this.getLowestPriority = VmOrderControlModel_getLowestPriority;

  this.getAllVms = VmOrderControlModel_getAllVms;
  this.getOrderedVms = VmOrderControlModel_getOrderedVms;
  this.getUnorderedVms = VmOrderControlModel_getUnorderedVms;
  this.getAutoVms = VmOrderControlModel_getAutoVms;
  this.getNoAutoVms = VmOrderControlModel_getNoAutoVms;
  this.getAutoNotOrdered = VmOrderControlModel_getAutoNotOrdered;


  //---------- "private" functions -------------


    // compare function to use when sorting the model by VM names

  function VmOrderControlModel_P_CompareNames(a,b) {
    if ( a.getName() < b.getName() ) return -1;
    if ( a.getName() > b.getName() ) return 1;
    return 0;
  }

    // compare function to use when sorting the model by VM priorities

  function VmOrderControlModel_P_ComparePriorities(a,b) {
    if ( a.getPriority() < b.getPriority() ) return -1;
    if ( a.getPriority() > b.getPriority() )  return 1;
    return 0;
  }

  //---------- "public" functions -------------


  // Add a VM Object to the model 
  // Return the array index if succeeded, -1 if already exists
  //
  // XXX consider returning the VM object itself, might simplify caller's code
  // XXX consider arranging all priorities at this point. No real need. Ops which 
  // care about priorities will take care of cleaning at their own time and will.

  function VmOrderControlModel_addVm(objOrderedVm) {
    if ( !this.exists(objOrderedVm.getVmHash()) ) {
      var i = this.arrVm.length;
      this.arrVm[i] = objOrderedVm;
      return i;
    } else {
      return -1;
    }
  }
 

    // Check if VM exists in the model by VM id.
    // Return true if exists, false if not.

  function VmOrderControlModel_exists(strVmHash) {
    if (this.existsIndex(strVmHash) == -1) {
      return false;
    } else {
      return true;
    }
  }


    // Check if VM exists in the model by VM id.
    // Return VM index in the model if exists, -1 if not.

  function VmOrderControlModel_existsIndex(strVmHash) {
    var i;
    for (i=0 ; i < this.arrVm.length ; i++) {
      if ( (this.arrVm[i] != null) && (this.arrVm[i].getVmHash() == strVmHash) ) return i;
    }
    return -1;
  }


    // Retrieve the VM object from the model by VM id.
    // Return null if VM object not in model, or the object if found

  function VmOrderControlModel_getVm(strVmHash) {
    var i = this.existsIndex(strVmHash);
    if (i != -1) {
      return this.arrVm[i];
    } else {
      return null;
    }
  }
  

    // Delete a virtual machine from the model
    // Optionally clean the model.
    // Return true if VM deleted, false if not found in the first place.
    //
    // XXX consider rearranging startup priorities here if the VM was 
    // autostarting in a specific order. No real need, even if was autostarting
    // in a seqeunce, the order is not disturbed by the removal of the VM.

  function VmOrderControlModel_delVm(strVmHash,boolClean) {
    var vmIndex = this.existsIndex(strVmHash);
    if (vmIndex!=-1) {
      this.arrVm[vmIndex]=null;
      if ( boolClean != null && boolClean == true) this.clean();
      return true;
    } else {
      return false;
    }
  }

    // Set the autostart/stop feature in the VM by id
    // return true if succeeded, false if failed
    // Note (!) that a VM will internally set its priority to 0 if its
    // autostart is turned off (see also : OrderedVm Object)
    //
    // XXX if does set priority to 0, consider cleaning rearranging the order
    // at this time. No real need, the startup order will not be disturbed by a 
    // removal of a VM from it.

  function VmOrderControlModel_setVmAuto(strVmHash,boolAuto) {
    if (strVmHash == null) { // no VM id given
      //  window.alert("devTime alert :while setting a VM autostart property, no VM id given!");
      return false;
    } else {
      var vmIndex = this.existsIndex(strVmHash);
      if (vmIndex != -1) {
	var result = this.arrVm[vmIndex].setAutoStartStop(boolAuto);
	return result;
      } else { // vm does not exist
	//  window.alert("devTime alert : while setting a VM (" + strVmHash +
	//		     ") " + "autostart property - VM not found in model !");
	return false;
      }
    }
  }


    // Unset the priority of the VM
    //
    // XXX consider handling other VM priorities here. No real need since the order 
    // is still intact.

  function VmOrderControlModel_unpriVm(strVmHash) {
    if (strVmHash == null) { // no VM name given
      //      window.alert("devTime alert :while trying to unprioritize a VM, no VM id given!");
      return false;
    } else {
      var vmIndex = this.existsIndex(strVmHash);
      if (vmIndex != -1) {
	this.arrVm[vmIndex].unPrioritize();
	return true;
      } else { // vm does not exist
	//	window.alert("devTime alert : while unprioritizing a VM (" + 
	//		     strVmHash + ") -" + " VM not found in model !");
	return false;
      }
    } 
  }


    // Make the VM priority the highest (lowest number).
    // Flow : 
    //   - The highest priority existing in the model is 1.
    //   - RePrioritize all other virtual machines using the VM_PRIORITY_GAP
    //   - Set our VMs priority to be 1.
    //
    // Returns : true if succeeded, false otherwise

  function VmOrderControlModel_priVmFirst(strVmHash) {

    if (strVmHash == null) { // no VM name given
      //  window.alert("devTime alert :while trying to prioritize VM as first, no VM id given!");
      return false;
    } else {
      var vmIndex = this.existsIndex(strVmHash);
      if (vmIndex != -1) {
	this.arrVm[vmIndex].setPriority(VM_PRIORITY_DIF); // make the VM most important
	var arrOrdered = this.getOrderedVms();
	if (arrOrdered != null) { // there are already other ordered VMs 
	  var tmpPri = VM_PRIORITY_GAP;
	  for (var i = 1 ; i < arrOrdered.length ; i++) {
	    arrOrdered[i].setPriority(tmpPri);
	    tmpPri += VM_PRIORITY_GAP;
	  }
	}
	this.arrVm[vmIndex].setPriority(1);
	return true;
      } else { // vm does not exist
	//	window.alert("devTime alert : while prioritizing a VM ("+ strVmHash + 
	//		     ") " + "to be first - VM not found in model !");
	return false;
      }
    } 
  }


    // Make the VM priority the lowest (highest number).
    // Returns : true if succeeded, false otherwise
    // NOTE (!) no need to rearrange other VMs priorities.
    
  function VmOrderControlModel_priVmLast(strVmHash) {
    if (strVmHash == null) { // no VM name given
      //      window.alert("devTime alert :while trying to prioritize VM as last, no VM id given!");
      return false;
    } else {
      var vmIndex = this.existsIndex(strVmHash);
      if (vmIndex != -1) {
	var arrOrdered = this.getOrderedVms();
	if (arrOrdered == null) { // there are currently no other ordered VMs
	  this.arrVm[vmIndex].setPriority(1);
	} else {
	  var priLastVm = arrOrdered[arrOrdered.length-1].getPriority();
	  this.arrVm[vmIndex].setPriority(priLastVm+VM_PRIORITY_GAP); 
	}
	// no need to do anything further with other VM priorities
	return true;
      } else { // vm does not exist
	//	window.alert("devTime alert : while prioritizing a VM (" + strVmHash + 
	//		     ") " + "to be last - VM not found in model !");
	return false;
      }
    }
  }


    // Move a VM one priority higher (lower number)
    // Returns true if succeeded, false otherwise

  function VmOrderControlModel_priUpVm(strVmHash) {
    if (strVmHash == null) { // no VM name given
      //  window.alert("devTime alert :while trying to up the priority of a VM, no VM id given!");
      return false;
    } else {
      var vmIndex = this.existsIndex(strVmHash);
      if (vmIndex != -1) { // VM found in the model
	if (!this.arrVm[vmIndex].isPrioritized()) { 
	  // vm has no current priority - make it last
	  this.priVmLast(strVmHash);
	  return true;
	} else {
	  var arrOrdered = this.getOrderedVms();
	  if (arrOrdered == null) { // there are currently no other ordered VMs
	    this.arrVm[vmIndex].setPriority(1);
	  } else {
	    this.cleanPriorities();
	    var curPri = this.arrVm[vmIndex].getPriority();
	    if (curPri == 1) return true; // means it is ordered first anyway
	    // move up by moving a dif more than the gap, than rearranging the array
	    // using cleanPriorities.
	    this.arrVm[vmIndex].setPriority(curPri-VM_PRIORITY_GAP-VM_PRIORITY_DIF);
	    this.cleanPriorities();
	  }
	  return true;
	}
      } else { // vm does not exist
	//	window.alert("devTime alert : while trying to up the VM's priority (" + 
	//		     strVmHash+") " + "- VM not found in model !");
	return false;
      }
    }
  }


    // Move a VM one priority lower (higher number)
    // Returns true if succeeded, false otherwise

  function VmOrderControlModel_priDnVm(strVmHash) {
    if (strVmHash == null) { // no VM name given
      //  window.alert("devTime alert :while trying to down the priority of a VM, no VM id given!");
      return false;
    } else {
      var vmIndex = this.existsIndex(strVmHash);
      if (vmIndex != -1) { // VM found in the model
	if (!this.arrVm[vmIndex].isPrioritized()) { 
	  // vm has no current priority - no significance to moving it down
	  return false;
	} else {
	  var arrOrdered = this.getOrderedVms();
	  if (arrOrdered == null) { // there are currently no other ordered VMs
	    // will never reach here, since at least this VM is prioritized
	  } else {
	    this.cleanPriorities();
	    var curPri = this.arrVm[vmIndex].getPriority();
	    if ( curPri == arrOrdered[arrOrdered.length-1].getPriority() ) {
	      // this is the least imporant VM, moving it down means removing
	      // it from the priority list

	      // XXX commented the below line, does not make sense when using the GUI
	      //     there are GUI controls to unprioritize, no need for the data model
	      //     to automatically do so by itself

	      // this.arrVm[vmIndex].unPrioritize();

	    } else {
	      // move down by moving a dif more than the gap, than rearranging the array
	      // using cleanPriorities.
	      this.arrVm[vmIndex].setPriority(curPri+VM_PRIORITY_GAP+VM_PRIORITY_DIF);
	      this.cleanPriorities();
	    }
	  }
	  return true;
	}
      } else { // vm does not exist
	//	window.alert("devTime alert : while trying to down the VM's priority (" + 
	//		     strVmHash+") " + "- VM not found in model !");
	return false;
      }
    }
  }


    // Cleans the ordered array of VMs by setting the first priority to 1
    // and any sequential priorities by adding the VM_PRIORITY_GAP.
    // returns a string indicating duplicate startup priorities, or empty 
    // string if no conflicts found.
    //
    // NOTE (!) that if any conflicting priorities are found, they will be 
    // recorder in a strConflicts, but will still be differentiated by 
    // a VM_PRIORITY_GAP. The caller can use the returned string to notify the 
    // user of the conflicts.

  function VmOrderControlModel_cleanPriorities() {
    var arrOrdered = this.getOrderedVms();
    var strConflicts = "";

    if (arrOrdered == null) return strConflicts;

    var i = 0;
    var curPri = 1;
    for ( i = 0 ; i < arrOrdered.length ; i++) {
      if (i <= parseInt(arrOrdered.length-2) 
	  && arrOrdered[i].getPriority() == arrOrdered[i+1].getPriority() ) {
	strConflicts += "\n" + arrOrdered[i].getName() +  " and " 
	  + arrOrdered[i+1].getName();
      }
      arrOrdered[i].setPriority(curPri);
      curPri +=  VM_PRIORITY_GAP;
    }

    return strConflicts;
  }


    // Brute force clear the model

  function VmOrderControlModel_clear() {
    this.arrVm = null;
    this.arrVm = new Array;
  }


    // Return an array of all Ordered VMs sorted by VM priorities
    // null will be returned if no ordered VMs are found (mozila/IE crossB)

  function VmOrderControlModel_getOrderedVms() {
    var arrOrdered = new Array;
    var tmpVm;
    var i;
    for (i=0 ; i < this.arrVm.length ; i++) {
      tmpVm = this.arrVm[i];
      if ( (tmpVm != null) && (tmpVm.isPrioritized()) ) {
	arrOrdered[arrOrdered.length] = tmpVm;
      }
    }
    if (arrOrdered.length == 0) {
      return null;
    } else {
      return arrOrdered.sort(VmOrderControlModel_P_ComparePriorities);
    }

  }


    // Return an array of all Unordered VMs sorted by VM names
    // null will be returned if no ordered VMs are found (mozila/IE crossB)
    
  function VmOrderControlModel_getUnorderedVms() {
    var arrUnordered = new Array;
    var tmpVm;
    var i;
    for (i=0 ; i < this.arrVm.length ; i++) {
      tmpVm = this.arrVm[i];
      if ( (tmpVm != null) && (!tmpVm.isPrioritized()) ) {
	arrUnordered[arrUnordered.length] = tmpVm;
      }
    }

    if (arrUnordered.length==0) {
      return null;
    } else {
      return arrUnordered.sort(VmOrderControlModel_P_CompareNames);
    }

  }


    // return an array of all VMs which are set to autostart/stop
    // return null if no such VMs exist (mozila/IE crossB)

  function VmOrderControlModel_getAutoVms() {
    var arrAuto = new Array;
    var tmpVm;
    var i;
    for (i=0 ; i < this.arrVm.length ; i++) {
      tmpVm = this.arrVm[i];
      if ( (tmpVm != null) && (tmpVm.getAutoStartStop()) ) {
	arrAuto[arrAuto.length] = tmpVm;
      }
    }

    if (arrAuto.length==0) {
      return null;
    } else {
      return arrAuto.sort(VmOrderControlModel_P_CompareNames);
    }
  }


    // return an array of all VMs which are set to autostart/stop but
    // have no specific order to do so.
    // return null if no such VMs exist.

  function VmOrderControlModel_getAutoNotOrdered() {
    var arrAutoNoOrd = new Array;
    var tmpVm;
    var i;
    for (i=0 ; i < this.arrVm.length ; i++) {
      tmpVm = this.arrVm[i];
      if ( (tmpVm != null) && (tmpVm.getAutoStartStop()) && !tmpVm.isPrioritized() ) {
	arrAutoNoOrd[arrAutoNoOrd.length] = tmpVm;
      }
    }

    if (arrAutoNoOrd.length==0) {
      return null;
    } else {
      return arrAutoNoOrd.sort(VmOrderControlModel_P_CompareNames);
    }
  }




    // return an array of all VMs which are set not to autostart/stop
    // return null if no such VMs exist

  function VmOrderControlModel_getNoAutoVms() {
    var arrNoAuto = new Array;
    var tmpVm;
    var i;
    for (i=0 ; i < this.arrVm.length ; i++) {
      tmpVm = this.arrVm[i];
      if ( (tmpVm != null) && (!tmpVm.getAutoStartStop()) ) {
	arrNoAuto[arrNoAuto.length] = tmpVm;
      }
    }

    if (arrNoAuto.length==0) {
      return null;
    } else {
      return arrNoAuto.sort(VmOrderControlModel_P_CompareNames);
    }

  }

    // Clean the model of any empty slots (created by previous deletes)

  function VmOrderControlModel_clean() {
    var arrTmp = new Array;
    var i, k;
    for (i=0, k=0 ; i < this.arrVm.length ; i++) {
      if ( this.arrVm[i] != null ) {
	arrTmp[k] = this.arrVm[i];
	k++;
      }
    }
    this.clear();
    this.arrVm = arrTmp;
  }


    // Sort the model by VM priorities
    // Note that javascript sort works on the array itself, not on a temp one.
    // This means the array is now ireversably sorted.

  function VmOrderControlModel_sortByPriority() {
    this.clean();
    this.arrVm.sort(VmOrderControlModel_P_ComparePriorities);
  }


    // Sort the model by VM names
    // Note that javascript sort works on the array itself, not on a temp one.
    // This means the array is now ireversably sorted.

  function VmOrderControlModel_sortByVmName() {
    this.clean();
    this.arrVm.sort(VmOrderControlModel_P_CompareNames);
  }
  

    // Return a human readable string representing the VMs in the model
 
  function VmOrderControlModel_toString() {
    var str = "";
    var i;
    if (this.arrVm.length == 0) {
      str += "<br>Model is Empty !";
    } else {
      for (i=0 ; i < this.arrVm.length ; i++) {
	if (this.arrVm[i] == null) {
	  str += "<br>"+i+". Empty Slot !";
	} else {
	  str += "<br>"+i+". "+this.arrVm[i].toString();
	}
      }
    }
    return str;
  }


    // Returns the total number of VMs in the model 
  
  function VmOrderControlModel_getNumVms() {
    this.clean();
    return this.arrVm.length;
  }


    // Returns the array of VMs or null if no VMs exist in the model
 
  function VmOrderControlModel_getAllVms() {
    this.clean();
    return  ( this.arrVm.length == 0 ) ? null : this.arrVm; 
  }


    // Retrieves the highest priority of all VMs in the model 

  function VmOrderControlModel_getHighestPriority() {
    var arrOrd = this.getOrderedVms();
    if (arrOrd == null) return -1;
    return arrOrd[0].getPriority();
  }


    // Retrieves the lowest priority of all VMs in the model 

  function VmOrderControlModel_getLowestPriority() {
    var arrOrd = this.getOrderedVms();
    if (arrOrd == null) return -1;
    return arrOrd[arrOrd.length-1].getPriority();
  }


}  // End orderedVmListModel object


/*******************************************************************************
*
* OrderedVm Object
*
* Is the basic object used by the data model.
* 
* - An Ordered VM currently holds :
*   a. strVmHash - the VM id as constrcuted in the ESX server
*   b. strVmName - the VM human readable name
*   c. intVmPriority - the VM priority 
*   d. boolAutoStartStop - VM flag indicating autostart/stop status
*   e. intVmOldPriority - the original VM priority before any changes were made
*   f. boolOldAutoStartStop - original flag of autostart/stop before any changes were made
* 
* - Currently the following is available from the OrderedVm object :
*
*  1. setPriority - set the priority of the VM.
*  2. getPriority - retrieve the priority of the VM.
*  3. getVmHash - retrieve the VM id.
*  4. getName - retrieve the VM name.
*  5. setAutoStartStop - toggles the VMs autostart/stop feature
*       Setting an autostart to stop will also unprioritize the VM.
*  6. getAutoStartStop - retrieve the autostart/stop flag
*  7. isPrioritized - as implied
*  8. unPrioritize - sets the VM priority to 0
*  9. toString - returns a human readable string reperesenting the VM attributes
*
*  10. isUpdated - have the VM values been changed  
*  11. getOldAutoStartStop - retrieve the original value the VM was created with
*  12. getOldPriority - retrieve the original value the VM was created with
/*******************************************************************************/

var VM_NO_PRIORITY = 0;
var VM_NO_AUTO = false;

function OrderedVm(strHash, strName, boolAuto, intPriority) {
  
  //---------- declarations -----------------

  this.strVmHash;
  this.strVmName;
  this.intVmPriority = VM_NO_PRIORITY;
  this.boolOldAutoStartStop = VM_NO_AUTO; 
  this.intVmOldPriority = VM_NO_PRIORITY;
  this.boolAutoStartStop = VM_NO_AUTO;
  
  this.setPriority = OrderedVm_setPriority;
  this.getPriority = OrderedVm_getPriority;
  this.getVmHash = OrderedVm_getVmHash;
  this.getName = OrderedVm_getName;
  this.setAutoStartStop = OrderedVm_setAutoStartStop;
  this.getAutoStartStop = OrderedVm_getAutoStartStop;
  this.isPrioritized = OrderedVm_isPrioritized;
  this.unPrioritize = OrderedVm_unPrioritize;
  this.toString = OrderedVm_toString;
  this.isUpdated = OrderedVm_isUpdated;
  this.getOldAutoStartStop = OrderedVm_getOldAutoStartStop;
  this.getOldPriority = OrderedVm_getOldPriority;
  
  //---------- initialization -------------------
  
  if (arguments.length == 0 || strHash == "") {
    //    window.alert("devTime alert : while creating an OrderedVm - no VM id given !");
    return null;
  } else {
    this.strVmHash = strHash;
  }

  if (arguments.length < 2 || strName == "") {
    //    window.alert("devTime alert : while creating an OrderedVm - no VM name given !");
    return null;
  } else {
    this.strVmName = strName;
  }

  if (arguments.length > 2 && boolAuto != null) {
    if ( boolAuto == true || boolAuto == false) {
      this.boolOldAutoStartStop = boolAuto;
      this.boolAutoStartStop = boolAuto;
    } else {
      //      window.alert("devTime alert : while creating an OrderedVM ("+strName+") -"+
      //		   " non valid autostart parameter given("+boolAuto+") !");

      // do not fail, there's already a default value of false in the autoStartStop field
    }
  }

  if (arguments.length > 3 && intPriority != null && this.boolAutoStartStop) {
    // note that if the vm does not auto start/stop the VM is not prioritized by definition
    if (isNaN(parseInt(intPriority)) || parseInt(intPriority) < 0 ) {
      //      window.alert("devTime alert : while creating an OrderedVm ("+strName+") -"+
      //		   " non valid priority given ("+intPriority+") !");

      // do not fail, there's already a default value of "no priority" in the priority field
    } else {
      this.intVmOldPriority = intPriority;
      this.intVmPriority = intPriority;
    }
  }

  return this;


  //---------- "public" functions -------------



  // Set the startup priority of the VM  

  function OrderedVm_setPriority(intPriority) {
    if (!isNaN(parseInt(intPriority)) && intPriority >= 0) {
      if (this.boolAutoStartStop) {
	this.intVmPriority = intPriority;
	return true;
      } else {
	// window.alert("devTime alert : While trying to set a priority for an OrderedVm - " +
	//	"the VM was not set to autostart and therefor priority is insignificany !");
	return false;
	
      }
    } else {
      //      window.alert("devTime alert : While trying to set a priority for an OrderedVm - " +
      //	   "non numeric or negative priority was given ("+intPriority+") !");
      return false;
    }
  }


    // Retrieve the startup priority of the VM

  function OrderedVm_getPriority() {
    return this.intVmPriority;
  }


    // Retrieve the VM's id
    
  function OrderedVm_getVmHash() {
    return this.strVmHash;
  }


    // Retrieve the VM's name

  function OrderedVm_getName() {
    return this.strVmName;
  } 


    // Set the VM to autostart or not to autostart with the SX server
    // Returns true if succeeded, false otherwise

  function OrderedVm_setAutoStartStop (boolAuto) {
    if (boolAuto == true || boolAuto == false ) {
      this.boolAutoStartStop = boolAuto;
      if ( boolAuto==false ) {
	this.unPrioritize();
      }
      return true;
    } else {
      //      window.alert("devTime alert : While trying to set autostart/stop for a VM non boolean was given");
      return false;
    }
  }



    // Retrieve the autostartstop property of the VM

  function OrderedVm_getAutoStartStop() {
    return this.boolAutoStartStop;
  }


    // Does the VM hold any startup priority.
    // Return true/false

  function OrderedVm_isPrioritized() {
    return (this.intVmPriority!=VM_NO_PRIORITY);
  }


    // Unsets the VMs startup priority. There's no significance to when 
    // the VM autostarts/stops

  function OrderedVm_unPrioritize() {
    this.intVmPriority = VM_NO_PRIORITY;
  }


    // Returns a human readable string representing the VM's data
  
  function OrderedVm_toString() {
    strVm = "";
    strVm += "vmHash : " +this.strVmHash + " ; " +
      "vmName : " +this.strVmName + " ; " +
      "vmAutoStart/Stop : " + this.boolAutoStartStop + " ; " +
      "vmPriority : " + this.intVmPriority;
    if (this.isUpdated()) {
      strVm += " ;; Is Updated ! old values were (" 
	+this.boolOldAutoStartStop + ":" + 
	this.intVmOldPriority+")";
    }
    return strVm;
  }


    // Check to see if any of the VM's priority or autostart 
    // attribues have changed.
    // Returns true if modified, false otherwise

  function OrderedVm_isUpdated() {
    if (this.intVmOldPriority == this.intVmPriority &&
	this.boolOldAutoStartStop == this.boolAutoStartStop) {
      return false; 
    } else {
      return true;
    }
  }


    // Retrieves the original autostart/stop attribute with which the VM
    // was created

  function OrderedVm_getOldAutoStartStop() {
    return this.boolOldAutoStartStop;
  }


    // Retrieves the original startup priority attribute with which the VM
    // was created

  function OrderedVm_getOldPriority() {
    return this.intVmOldPriority;
  }

} // End OrderedVm object
