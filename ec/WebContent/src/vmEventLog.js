/* Copyright 1998 VMware, Inc.  All rights reserved. -- VMware Confidential */

// vmEventLog.js


// --------------------------------------------------------------------------
//
// initPage --
//
// --------------------------------------------------------------------------

function initPage() {
  var vm;
  var dom = new Object();

  var s = parent.parent.location.search.substr(1);
  s = s.split('&');
  for (var i = 0; i < s.length; i++) {
    var h = s[i].split('=');
    if (h[0] == 'h') {
      hash = h[1];
      vm = main.sx.vms[hash];
      break;
    }
  }
  
  var obsrvrStr = "vmEventLog_" + vm.hash();

  dom.evtTbl = obj("evtTable").getElementsByTagName("tbody")[0];
  dom.evtRow = dom.evtTbl.removeChild(obj("evtRow"));

  dom.noEvt = obj("noEvt");
  dom.noEvt.css("display", "none");
  
  dom.evts = new Array(15);
  
  
  // ------------------------------------------------------------------------
  function evtIcon(t) {
    if ((t & 4) == 4) return "/vmware/imx/error-10x10.png";
    if ((t & 2) == 2) return "/vmware/imx/warning-10x10.png";
    return "/vmware/imx/spacer.gif";
  };
  
  
  // ------------------------------------------------------------------------
  function pad(t) {
    return t < 10 ? "0" + t : t;
  };
  
  
  // ------------------------------------------------------------------------
  function evtTime(t) {
    var d = new Date(t * 1000);
    var p;
    var h = d.getHours();
    p = h >= 12 ? " PM" : " AM";
    if (h == 0) {
      h = 12;
    } else if (h > 12) {
      h = h - 12;
    }
    var m = d.getMinutes();
    var s = d.getSeconds();
    
    var dd = pad(d.getDate());
    var mm = pad(d.getMonth() + 1);
    var yyyy = pad(d.getFullYear());
    var days = ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];
    
    return days[d.getDay()] + " " + mm + "/" + dd + "/" + yyyy + "<br />" +
      pad(h) + ":" + pad(m) + ":" + pad(s) + p;
  };
  
  
  // ------------------------------------------------------------------------
  deferRenderEvent = function (e) {
    e = vm.evt(e);
    
    dom.evts[e.id].img.src = evtIcon(e.a);
    dom.evts[e.id].key.innerHTML = evtTime(e.b);
    dom.evts[e.id].val.innerHTML = e.c;
    
    // setTimeout("parent.parent.adjustSize()", 40);
  };
  
  
  // ------------------------------------------------------------------------
  renderEvent = function (t, v, e) {
    if (dom.evts[e.id] == null) {
      dom.evts[e.id] = new Object();
      
      dom.evts[e.id].row =
        dom.evtTbl.appendChild(dom.evtRow.cloneNode(true));
      objAtt(dom.evts[e.id].row, "id", "event_" + e.id);
      
      dom.evts[e.id].key = document.getElementById("evtTime");
      objAtt(dom.evts[e.id].key, "id", "");

      dom.evts[e.id].val = document.getElementById("evtMsg");
      objAtt(dom.evts[e.id].val, "id", "");
      
      dom.evts[e.id].img = dom.evts[e.id].row.getElementsByTagName('img')[0];
    }

    if (vm._evt.length < 1) {
      dom.noEvt.css("display","");
    } else {
      dom.noEvt.css("display","none");
    }
    
    setTimeout("deferRenderEvent('" + e.id + "');", 250);
  };

  
  // // ------------------------------------------------------------------------
  // tryUpdates = function () {
  //   if (! main || main.getUpdates == null || ! main.getUpdates()) {
  //     window.setTimeout("tryUpdates();", 250);
  //   }
  // };
  
  
  // ------------------------------------------------------------------------
  doUp = function () {
    // Toggle extra info on.
    // if ((vm.extra() & 4) == 0) {
    //   vm.extra(4);
    // }

    for (var i = 0; i < vm._evt.length; i++) {
      renderEvent(main.vmEvtObsrvr.t, vm, vm.evt(i));
    }
    
    if (vm._evt.length < 1) {
      dom.noEvt.css("display","");
    } else {
      dom.noEvt.css("display","none");
    }
    
    main.vmEvtObsrvr.lstn(obsrvrStr, renderEvent, vm);
  };

  
  // ------------------------------------------------------------------------
  doDn = function () {
    // Toggle extra info off.
    // if ((vm.extra() & 4) == 4) {
    //   vm.extra(4);
    // }
    
    main.vmEvtObsrvr.ignr(obsrvrStr, renderEvent, vm);
  };

  // setTimeout("tryUpdates();", 250);
}
