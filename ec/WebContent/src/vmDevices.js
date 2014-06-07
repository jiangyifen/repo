/* Copyright 2002 VMware, Inc.  All rights reserved. -- VMware Confidential */

var ok = false;

// --------------------------------------------------------------------------
//
// initPage --
//
// --------------------------------------------------------------------------

function initPage() {
  var vm;
  var dom = new Object();

  // keys[] defines the sort order of device attributes. id must be the first
  // key in the array. Let renderDevice() add any unanticipated keys.
  var keys = ["id","rdcs","rdsc","hi","loc","netnm","devnm","vdev","mode","depth",
    "vnet","num","mb","shbus"];

  var s = parent.location.search.substr(1);
  s = s.split('&');
  for (var i = 0; i < s.length; i++) {
    var h = s[i].split('=');
    if (h[0] == 'h') {
      hash = h[1];
      vm = main.sx.vms[hash];
      break;
    }
  }

  var obsrvrStr = "vmDevices_" + vm.hash();

  dom.remDev = obj("remDev").getElementsByTagName("tbody")[0];
  dom.devRow = dom.remDev.removeChild(obj("devRow"));
  dom.keyRow = dom.remDev.removeChild(obj("keyRow"));
  dom.othDev = obj("othDev").getElementsByTagName("tbody")[0];
  dom.addDevLink = obj("addDevLink");
  dom.upgrade = obj("upgrade");
  dom.upgradeLink = obj("upgradeLink");

  // ------------------------------------------------------------------------
  //
  // removable --
  //
  //    Define removable devices.
  //
  // Prototype:
  //
  //    object removable(d)
  //
  // Arguments:
  //
  //    d = device object
  //
  // Results:
  //
  //    Returns true or false.
  //
  // ------------------------------------------------------------------------
  function removable(d) {
    if (d != null && d.rdcs != null) { return true; }
    return false;
  };

  function showRemDevLink(d) {
    if (d.id.match(/^(scsiCtlr|video|memory)/i)) {
      return false;
    }

    return true;
  }

  function showDev(d) {
    // Don't show SCSI controller info in GSX
    if (main.sx._prodId == "gsx" && d.id.match(/^scsiCtlr/i)) {
      return false;
    }

    return true;
  }

  // ------------------------------------------------------------------------
  //
  // iconDev --
  //
  //    Define removable devices.
  //
  // Prototype:
  //
  //    object iconDev(d)
  //
  // Arguments:
  //
  //    d = device object
  //
  // Results:
  //
  //    Returns URI to image location for the given device.
  //
  // ------------------------------------------------------------------------
  function iconDev(d) {
    var s = "/vmware/imx/";
    // DVD/CD-ROM Drive or Hard Disk Drive
    if (d.id.match(/^(scsi|ide)dev/i)) {
      if (d.hi != null) {
        if (d.hi.match(/^disk/i)) { return s + "vmdk-16x16.png"; }
        if (d.hi.match(/^cdrom/i)) { return s + "cdrom-16x16.png"; }
        if (d.hi.match(/^passthru/i)) { return s + "scsi-16x16.png"; }
      }
    }

    // Floppy Drive
    if (d.id.match(/^floppy/i)) {
      return s + "floppy-16x16.png";
    }

    // SCSI Controller
    if (d.id.match(/^scsiCtlr/i)) {
      return s + "scsi-16x16.png";
    }

    // Memory
    if (d.id.match(/^memory/i)) {
      return s + "memory-16x16.png";
    }

    // Network Adapter
    if (d.id.match(/^nic/i)) {
      return s + "network-16x16.png";
    }

    // Serial Port
    if (d.id.match(/^serial/i)) {
      return s + "serial-16x16.png";
    }

    // Parallel Port
    if (d.id.match(/^parallel/i)) {
      return s + "parallel-16x16.png";
    }

    // USB
    if (d.id.match(/^usbCtlr/i)) {
      return s + "usb-16x16.png";
    }

    // Remote Display
    if (d.id.match(/^video/i)) {
      return s + "display-16x16.png";
    }

    return s + "vmware_boxes-16x16.png";
  };


  // ------------------------------------------------------------------------
  function niceKey(d, k) {
    switch (k) {
      case "hi":
        return "Device";
      case "loc":
        return "Location";
      case "netnm":
      case "devnm":
        return "Network Connection";
      case "vdev":
        return "Virtual Device";
      case "rdsc":
        return "Connect at Power On";
      case "rdcs":
        return "Connected";
      case "mode":
        return "Mode";
      case "num":
        return "Processors";
      case "mb":
        return "Memory";
      case "depth":
        return "Colors";
      case "shbus":
        return "Bus Sharing";
      case "size":
        return "Size";
      case "autoc": 
        return "Auto Connect Devices";
      case "clientDvc":
        return "Client Device";
      default:
        return k;
    }
  };


  // ------------------------------------------------------------------------
  function niceVal(d, k) {
    var l = k.toLowerCase();
    if (l.match(/^(rdsc|rdcs|autoc|clientdvc)$/)) {
      if (d.id.match(/^ethernet/i))
        return "";
      return d[k] ? "Yes" : "No";
    }

    if (l == "devnm") {
      if (d["netnm"]) {
        return d["netnm"];
      }

      return d[k];
    }

    if (l == "mode") {
      if (d.id.match(/^(scsi|ide)dev/i)) {
        return d[k].substr(0,1).toUpperCase() + d[k].substr(1);
      }

      return d[k];
    }

    if (l == "hi") {
      if (d.id.match(/^(scsi|ide)dev/i)) {
        if (d.hi.match(/^disk:file$/i)) {
          if (d.loc.match(/vmhba\d+:\d+:\d+:0:?$/)) {
            return "System LUN/Disk";
          } else {
            return "VMware Disk Image";
          }
        }
        if (d.hi.match(/^disk:device$/i)) {
            return "Physical Disk";
	}

        if (d.hi.match(/^passthru/i))
          return "System SCSI Device";

        if (d.hi.match(/^cdrom:device$/i))
          return "System DVD/CD-ROM Drive";

        if (d.hi.match(/^cdrom:image$/i))
          return "ISO Image";
      }

      if (d.id.match(/^floppy/i)) {
        if (d.hi.match(/^image$/i)) { return "Floppy Image"; }
        return "System Floppy Drive";
      }

      if (d.id.match(/^serial/i)) {
        if (d.hi.match(/^file$/i)) { return "File"; }
        if (d.hi.match(/^pipe$/i)) { return "Named Pipe"; }
        return "System Serial Port";
      }

      if (d.id.match(/^parallel/i)) {
        if (d.hi.match(/^file$/i)) { return "File"; }
        return "System Parallel Port";
      }

      if (d.id.match(/^nic/i)) {
        if (d.hi.match(/^named/i)) { return d.netnm; }
        if (d.hi.match(/^bridged/i)) { return "Bridged"; }
        if (d.hi.match(/^nat/i)) { return "NAT"; }
        if (d.hi.match(/^hostOnly/i)) { return "Host-only"; }
        return "Custom";
      }

      return d[k];
    }

    if (l == "depth") {
      switch (parseInt(d[k])) {
        case 8:
          return "256 Colors (8 bit)";
        case 15:
        case 16:
          return "Thousands of Colors (" + d[k] + " bit)";
        case 24:
          return "Millions of Colors (24 bit)";
        default:
          return (1 << d[k]) + " Colors (" + d[k] + " bit)";
      }
    }

    if (l == "mb") {
      return hrVal(d[k], 0, "m", "m");
    }

    if (l == "size") {
      return hrVal(d[k], 1, "v", "g");
    }

    return d[k];
  };

  // ------------------------------------------------------------------------
  function enableEditLink(d, s, m) {
    if ((m & 2) != 2) { 
       return false; 
    }

    if (s == esSusp) {
       return false;
    }

    if (d.id.match(/^(scsiCtlr|video|memory|usbCtlr)/i)) { 
      return s == esOff; 
    }

    if (d.id.match(/^(scsi|ide)dev/i) && d.hi != null) {
      if (d.hi.match(/^disk/i)) { 
         return s == esOff; 
      }
    }

    return true;
  }

  function enableAddRemLink(s, m) {
    return (m & 2) == 2 && s == esOff;
  }

  rndrDevMode = function (d, es, mode) {
    if (!showDev(d)) {
      return;
    }

    if (showRemDevLink(d)) {
      if (enableAddRemLink(es, mode)) {
        objAtt(dom[d.id].rem, "href", "javascript:rem('" + d.id + "');");
        objAtt(dom[d.id].rem, "class", "");
      } else {
        objAtt(dom[d.id].rem, "href", "javascript:;");
        objAtt(dom[d.id].rem, "class", "dsbld");
      }
    }

    if (enableEditLink(d, es, mode)) {
      objAtt(dom[d.id].mod, "href", "javascript:cfgWndw('" + d.id + "');");
      objAtt(dom[d.id].mod, "class", "");
    } else {
      objAtt(dom[d.id].mod, "href", "javascript:;");
      objAtt(dom[d.id].mod, "class", "dsbld");
    }
  };

  rndrAddRemMode = function (es, mode, hwv) {
    if (enableAddRemLink(es, mode)) {
      dom.addDevLink.att("href", "javascript:wizWndw();");
      dom.addDevLink.att("class", "");

      dom.upgradeLink.att("href", "javascript:upgradeHwv();");
      dom.upgradeLink.att("class", "");
    } else {
      dom.addDevLink.att("href", "javascript:;");
      dom.addDevLink.att("class", "dsbld");

      dom.upgradeLink.att("href", "javascript:;");
      dom.upgradeLink.att("class", "dsbld");
    }

    if (hwv < main.defaultHwv) {
      dom.upgrade.css("display", "");
    } else {
      dom.upgrade.css("display", "none");
    }
  };

  rndrMode = function (ut, vm, v) {
    var es = vm.es();
    var mode = vm.mode();

    for (var d in vm._dev) {
      rndrDevMode(vm._dev[d], es, mode);
    }

    rndrAddRemMode(es, mode, vm.hwv());
  };

  // ------------------------------------------------------------------------
  deferRender = function (d) {
    d = vm.dev(d);
    var rd = removable(d);
    var col = rd ? "remDev" : "othDev";

    for (var i = 0; i < keys.length; i++) {
      var k = keys[i];

      if (d[k] == null) {
        continue;
      } else if (k == "id") {
        dom[d[k]].img.src = iconDev(d);
        dom[d[k]].dev.innerHTML = hrDev(d);
        rndrDevMode(d, vm.es(), vm.mode());
      } else {
        if (k == "rdsc" && ! rd) continue;
        if (k == "netnm") continue;
        if (k == "vmnet") continue;
        if (k == "bidi") continue;
        if (k == "tnrl") continue;
        if (k == "endp") continue;
        if (k == "poll") continue;
        if (main.sx._prodId == "gsx" && k == "devnm") continue;
        if (main.sx._prodId == "gsx" && k == "mode") continue;

        var s = niceVal(d, k);
        if (s == "" && s != d[k] && dom[d.id + "_" + k].keyRow != null) {
          dom[col].removeChild(dom[d.id + "_" + k].keyRow);
          dom[d.id + "_" + k].keyRow = null;
          continue;
        }

        if (s.length > 29) {
          var t = s.substr(0, 14) + "...";
          t += s.substr(s.length - 15);
          s = t;

          objAtt(dom[d.id + "_" + k].val, "title", d[k]);
        }

        dom[d.id + "_" + k].key.innerHTML = niceKey(d, k);
        dom[d.id + "_" + k].val.innerHTML = s;
      }
    }
    setTimeout("parent.adjustSize()", 40);
  };


  // ------------------------------------------------------------------------
  renderDevice = function(t, v, d) {
    var rd = removable(d);
    var col = rd ? "remDev" : "othDev";

    if (!showDev(d)) {
      return;
    }

    // Add any missing keys to keys array
    for (var n in vm._dev) {
      for (var k in vm.dev(n)) {
        var has = false;
        for (var i = 0; i < keys.length; i++) {
          if (keys[i] == k) {
            has = true;
            break;
          }
        }

        if (! has) {
          keys.push(k);
        }
      }
    }

    // Render devices.
    for (var i = 0; i < keys.length; i++) {
      var k = keys[i];

      if (d[k] == null) {
        continue;
      } else if (k == "id") {
        if (dom[d[k]] != null) continue;

        dom[d[k]] = new Object();

        dom[d[k]].devRow = dom[col].appendChild(dom.devRow.cloneNode(true));
        objAtt(dom[d[k]].devRow, "id", d[k]);

        dom[d[k]].dev = document.getElementById("dev");
        objAtt(dom[d[k]].dev, "id", "");

        dom[d[k]].mod = document.getElementById("mod");
        objAtt(dom[d[k]].mod, "id", "");
        objAtt(dom[d[k]].mod, "href", "javascript:cfgWndw('" + d[k] + "');");

        dom[d[k]].rem = document.getElementById("rem");
        objAtt(dom[d[k]].rem, "id", "");

        dom[d[k]].remDevLink = document.getElementById("remDevLink");
        objAtt(dom[d[k]].remDevLink, "id", "");

        if (showRemDevLink(d)) {
          objAtt(dom[d[k]].rem, "href", "javascript:rem('" + d[k] + "');");
        } else {
          objCss(dom[d[k]].remDevLink, "display", "none");
        }

        dom[d[k]].img = dom[d[k]].devRow.getElementsByTagName('img')[0];
      } else {
        if (k == "rdsc" && ! rd) continue;
        if (k == "netnm") continue;
        if (k == "vmnet") continue;
        if (k == "bidi") continue;
        if (k == "tnrl") continue;
        if (k == "endp") continue;
        if (k == "poll") continue;
        if (main.sx._prodId == "gsx" && k == "devnm") continue;
        if (main.sx._prodId == "gsx" && k == "mode") continue;

        if (dom[d.id + "_" + k] != null) continue;

        dom[d.id + "_" + k] = new Object();

        dom[d.id + "_" + k].keyRow =
        dom[col].appendChild(dom.keyRow.cloneNode(true));
        objAtt(dom[d.id + "_" + k].keyRow, "id", d.id + "_" + k);

        dom[d.id + "_" + k].key = document.getElementById("key");
        objAtt(dom[d.id + "_" + k].key, "id", "");

        dom[d.id + "_" + k].val = document.getElementById("val");
        objAtt(dom[d.id + "_" + k].val, "id", "");
      }
    }
    setTimeout("deferRender('" + d.id + "');", 250);
  }

  // ------------------------------------------------------------------------
  opCb = function (w) {
    var q = new Query(w.location.search);
    d = q.arg("dev")[0];

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
      return;
    }

    if (ok == "rem") {
      main.vmRemDevObsrvr.exec(vm, vm._dev[d]);
      delete vm._dev[d];
    }

    if (ok == "upgrade") {
      vm.hwv(main.defaultHwv);
    }
  };

  // ------------------------------------------------------------------------
  rem = function (d) {
    var q = new Query();
    q.action("/vm-config/index.pl");
    q.target(parent.op.win());
    q.arg("op", "rem");
    q.arg("vmid", vm.hash());
    q.arg("dev", d);

    if (confirm('Remove "' + hrDev(vm.dev(d)) + '"?')) {
      ok = "rem";
      q.submit(true);
    }
  };

  upgradeHwv = function () {
    var text;
    if (main.sx._prodId == "gsx") {
       text = "If this virtual machine's hardware is upgraded, it can " +
              "only be powered on with VMware GSX Server version 3.0 or later. If you " +
              "need to run this virtual machine on versions of VMware GSX Server prior " +
              "to 3.0, then click Cancel. Upgrade virtual hardware now?"
    } else {
       text = "If this virtual machine's hardware is upgraded, it can " +
              "only be powered on with VMware ESX Server version 2.0 or later. If you " +
              "need to run this virtual machine on versions of VMware ESX Server prior " +
              "to 2.0, then click Cancel. Upgrade virtual hardware now?"
    }
    if (confirm(text)) {
      var q = new Query();
      q.action("/vm-config/index.pl");
      q.target(parent.op.win());
      q.arg("op", "upgrade");
      q.arg("vmid", vm.hash());
      ok = "upgrade";
      q.submit();
    }
  }


  // ------------------------------------------------------------------------
  remDevice = function (t, v, d) {
    var rd = removable(d);
    var col = rd ? "remDev" : "othDev";

    // Find dom objects for device d.
    var fnd = [];
    var pat = new RegExp("^" + d.id + "_?");
    for (var n in dom) {
      if (n.match(pat) && dom[n] != null) {
        fnd.push(n);
      }
    }

    // Remove dom objects for device d.
    for (var i = 0; i < fnd.length; i++) {
      if (dom[fnd[i]].devRow != null) {
        dom[col].removeChild(dom[fnd[i]].devRow);
      } else if (dom[fnd[i]].keyRow != null) {
        dom[col].removeChild(dom[fnd[i]].keyRow);
      }
      dom[fnd[i]] = null;
      delete dom[fnd[i]];
    }

    setTimeout("parent.adjustSize()", 40);
  };

  // ------------------------------------------------------------------------
  cfgWndw = function (d) {
    d = vm.dev(d);

    var s = d.id;
    if (d.id.match(/^(scsi|ide)dev/i)) {
      s = d.hi;
    }

    var u = "/vmware/en/vmCfgContainer.html?unit=" + s +
      "&vmid=" + vm.hash() + "&dev=" + d.id;

    var n = main.user + "@" + main.sx._name + ":" + vm.cfg() + ":" + d.id;

    n = escJs(n);
    var w = main.getRgWndw(n);

    if (w != null && ! w.closed) {
      w.focus();
    } else {
      w = window.open("", "", "width=560,height=200,resizable");
      w.document.location.replace(u);
      w.name = n;
      main.addRgWndw(w);
    }
  };

  // ------------------------------------------------------------------------
  wizWndw = function () {
    var s = "vmAddDeviceWizard";
    var u = "/vmware/en/vmCfgContainer.html?unit=" + s +
      "&vmid=" + vm.hash();

    var n = main.user + "@" + main.sx._name + ":" + vm.cfg() + ":" + s;

    n = escJs(n);
    var w = main.getRgWndw(n);

    if (w != null && ! w.closed) {
      w.focus();
    } else {
      w = window.open("", "", "width=560,height=200,resizable");
      w.document.location.replace(u);
      w.name = n;
      main.addRgWndw(w);
    }
  };

  // ------------------------------------------------------------------------
  tryUpdates = function () {
    if (! main || main.getUpdates == null || ! main.getUpdates()) {
      setTimeout("tryUpdates();", 250);
    }
  };


  // ------------------------------------------------------------------------
  doUp = function () {
    // Toggle extra info on.
    if ((vm.extra() & 2) == 0) {
      vm.extra(2);
    }

    for (var n in vm._dev) {
      renderDevice(main.vmDevObsrvr.t, vm, vm.dev(n));
    }
    rndrAddRemMode(vm.es(), vm.mode(), vm.hwv());

    main.vmDevObsrvr.lstn(obsrvrStr, renderDevice, vm);
    main.vmExecutionStateObsrvr.lstn(obsrvrStr, rndrMode, vm);
    main.vmModeObsrvr.lstn(obsrvrStr, rndrMode, vm);
    main.vmHwvObsrvr.lstn(obsrvrStr, rndrMode, vm);
    main.vmRemDevObsrvr.lstn(obsrvrStr, remDevice, vm);
  };


  // ------------------------------------------------------------------------
  doDn = function () {
    // Toggle extra info off.
    if ((vm.extra() & 2) == 2) {
      vm.extra(2);
    }

    main.vmDevObsrvr.ignr(obsrvrStr, renderDevice, vm);
    main.vmExecutionStateObsrvr.ignr(obsrvrStr, rndrMode, vm);
    main.vmModeObsrvr.ignr(obsrvrStr, rndrMode, vm);
    main.vmHwvObsrvr.ignr(obsrvrStr, rndrMode, vm);
    main.vmRemDevObsrvr.ignr(obsrvrStr, remDevice, vm);
  };

  setTimeout("tryUpdates();", 250);
}
