/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// ------------------------------------------------------------------------

function hrDev(d) {
  var s = "";

  // Drives: Virtual, Disk, DVD, CD-ROM Drive
  if (d.id.match(/^(scsi|ide)dev/i)) {
    var n = "";
    s = d.id.replace(/(ide|scsi)(dev)/i, "$1 ").toUpperCase();

    if (d.hi != null) {
      if (d.hi.match(/^disk/i)) {
        n = "Virtual Disk";
      } else if (d.hi.match(/^cdrom/i)) {
        n = "DVD/CD-ROM Drive";
      } else if (d.hi.match(/^passthru/i)) {
        n = "Generic Device";
      }
    }

    return n ? n + " (" + s + ")" : s;
  }

  // Floppy Drive
  if (d.id.match(/^floppy/i)) {
    s = parseInt(d.id.replace(/^.*(\d+)$/, "$1"));
    return "Floppy Drive" + (s > 0 ? " " + (s + 1) : "");
  }

  // SCSI Controller
  if (d.id.match(/^scsiCtlr/i)) {
    s = d.id.replace(/^scsiCtlr(\d+)$/i, "SCSI Controller $1");
    return s;
  }

  // Memory
  if (d.id.match(/^memory/i)) {
    return "Processors and Memory";
  }

  // Network Adapter
  if (d.id.match(/^nic/i)) {
    s = parseInt(d.id.replace(/^.*(\d+)$/, "$1"));
    return "Network Adapter" + (s > 0 ? " " + (s + 1) : "");
  }

  // Serial Port
  if (d.id.match(/^serial/i)) {
    s = parseInt(d.id.replace(/^.*(\d+)$/, "$1"));
    return "Serial Port" + (s > 0 ? " " + (s + 1) : "");
  }

  // Parallel Port
  if (d.id.match(/^parallel/i)) {
    s = parseInt(d.id.replace(/^.*(\d+)$/, "$1"));
    return "Parallel Port" + (s > 0 ? " " + (s + 1) : "");
  }

  // USB
  if (d.id.match(/^usb/i)) {
    return "Universal Serial Bus (USB)";
  }

  // Remote Display
  if (d.id.match(/^video/i)) {
    return "Display";
  }

  return d.id;
}

function hrGos(s) {
  for (var i = 0; i < main.sx._guests.length; i++) {
    if (main.sx._guests[i].key == s) {
      return main.sx._guests[i].name;
    }
  }
  return "Unknown";
}
