/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// --------------------------------------------------------------------------

function exit(i) {
  parent.close();
}


// --------------------------------------------------------------------------

function initPage() {
  if (parent.ready == null || ! parent.ready()) { window.setTimeout(initPage, 50); return; }

  var w = parent.op.win();
  var q = new Query(parent.location.search);
  var vm = main.sx.vms[q.arg("vmid")[0]];
  
  if (main.sx._prodId == "gsx") {
    obj("diskInfoGSX").css("display", "");
    obj("parallel").css("display", "");
    obj("serial").css("display", "");
    obj("usb").css("display", "");
  } else {
    obj("diskInfoESX").css("display", "");
  }

  var occupiedSlots = [];
  var nicsCount = 0; // number of Network cards in use.
  var scsiCount = 0; // number of scsi ctlrs in use.
  var re = new RegExp("scsidev(\\d:\\d)", "i");
  for (i in vm._dev) {
     var d = vm.dev(i);
    if (d.id.match(/^(nic)/i)) {
      ++nicsCount;
    } else if (d.id.match(/^(scsiCtlr)/i)) {
      ++scsiCount;
    }
    var m;
    if ((m = d.id.match(re)) != null) {
      occupiedSlots.push(m[1]);
    }
  }
  
  var scsiCtlr0 = grep(occupiedSlots, new RegExp("^0:\\d+$"));
  var scsiCtlr1 = grep(occupiedSlots, new RegExp("^1:\\d+$"));

  // There can be a maximum of 5 PCI devices.  Also,
  // there can be no more than 4 network cards.
  if (nicsCount == 4 || nicsCount + scsiCount >= 5) {
    var nic = obj("nic");
    objAtt(nic, "href", "javascript:;");
    objAtt(nic, "class", "dsbld");
  }
  
  // The maximum number of slots per scsi controller is 16.
  // However, MUI handles only 7 per SCSI controller.
  // Since MUI acknowledges SCSI ctlrs 0 and 1 only, the maximum
  // number of slots available in the best case scenario will be 14.
  // If there are already four Network cards then one should not
  // be able to add more than one SCSI ctlr since there can 
  // be only 5 PCI devices.
  var maxSlots = nicsCount < 4 ? 14 : 7;
  if ((scsiCtlr0.length + scsiCtlr1.length) == maxSlots) {
    var disk = obj("disk");
    var scsi = obj("scsi");
    objAtt(disk, "href", "javascript:;");
    objAtt(disk, "class", "dsbld");
    objAtt(scsi, "href", "javascript:;");
    objAtt(scsi, "class", "dsbld");
  }
  
  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var editor = new Step("editor", obj("editor"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  ldgMsg.nxt[0] = editor;
  ldgMsg.exec = function (i) {
    editor.prv = null;
    parent.ctx = "vmAddDeviceWizard";
    parent.tglBtns(false);
    setTimeout("parent.layoutCb()", 40);
  };

  add = function (s) {
    switch (s) {
      case "disk":
        if (main.sx._prodId == "gsx") {
          self.location.replace("vmVirtualDiskGSX.html");
          parent.helpurl = "vserver/vmVirtualDiskHelp.html";
	} else {
          self.location.replace("vmVirtualDisk.html");
	  parent.helpurl = "esx/vmVirtualDiskHelp.html";
        } 
        break;
      case "floppy":
        self.location.replace("vmFloppy.html");
        if (main.sx._prodId == "gsx") {
          parent.helpurl = "vserver/vmFloppyHelp.html";
	} else {
          parent.helpurl = "esx/vmFloppyHelp.html";
        } 
        break;
      case "cdrom":
        self.location.replace("vmDvdCdRom.html");
        if (main.sx._prodId == "gsx") {
          parent.helpurl = "vserver/vmDvdCdRomHelp.html";
	} else {
          parent.helpurl = "esx/vmDvdCdRomHelp.html";
        } 
        break;
      case "nic":
        if (main.sx._prodId == "gsx") {
          self.location.replace("vmNicGSX.html");
          parent.helpurl = "vserver/vmNicHelp.html";
	} else {
          self.location.replace("vmNic.html");
          parent.helpurl = "esx/vmNicHelp.html";
        } 
        break;
      case "genericScsi":
        if (main.sx._prodId == "gsx") {
          self.location.replace("vmGenericScsiFormGSX.html");
          parent.helpurl = "vserver/vmGenericScsiFormHelp.html";
	} else {
          self.location.replace("vmGenericScsiForm.html");
          parent.helpurl = "esx/vmGenericScsiFormHelp.html";
        } 
        break;
      case "parallel":
        self.location.replace("vmParallel.html");
        if (main.sx._prodId == "gsx") {
          parent.helpurl = "vserver/vmParallelHelp.html";
	} else {
          parent.helpurl = "esx/vmParallelHelp.html";
        } 
        break;
      case "serial":
        self.location.replace("vmSerial.html");
        if (main.sx._prodId == "gsx") {
          parent.helpurl = "vserver/vmSerialHelp.html";
	} else {
          parent.helpurl = "esx/vmSerialHelp.html";
        } 
        break;
      case "usb":
        self.location.replace("vmUsb.html");
        if (main.sx._prodId == "gsx") {
          parent.helpurl = "vserver/vmUsbHelp.html";
	} else {
          parent.helpurl = "esx/vmUsbHelp.html";
        } 
        break;
      default:
        alert("Not implemented");
        return;
    }

    parent.tglBtns(true);
  }

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  next();
}
