function cfgWndw(l) {
  var u = "/vmware/en/sxCfgContainer.html?unit=" + l;
  var n = main.user + "@" + main.sx._name + ":sxHostConfig" + l;
  n = escJs(n);
  var w = main.getRgWndw(n);

  if (w != null && ! w.closed) {
      w.focus();
  } else {
    w = window.open("", "", "width=560,height=580,resizable");
    w.document.location.replace(u);
    w.name = n;
    main.addRgWndw(w);
  }
}

function logWndw(l) {
  var u = "sxLogViewer.html";
  if (l != null) { u += "?tab=" + esc(l); }
  // If we're in the host config wizard, main.sx will not be defined, but
  // parent.sx will be.
  var n = main.sx != null ? main.user + "@" + main.sx._name + ":sxLogViewer" :
    parent.user + "@" + parent.sx._name + ":sxLogViewer";

  n = escJs(n);
  var w = main.getRgWndw(n);

  if (w != null && ! w.closed) {
    w.focus();
    if (l) {
      w.slctTab(w.getTab(l));
    } else {
      l = w.getSlctdTabId();
      if (l != null && l == "vmksummary") {
        w.slctTab(w.getTab("vmkwarning"));
      }
    }
  } else {
    var p = "width=730,height=155,resizable" +
      (ie ? ",top=16,left=16" : ",screenX=16,screenY=16");
    w = window.open("", "", p);
    w.document.location.replace(u);
    w.name = n;
    main.addRgWndw(w);
  }
}


function scWndw(l) {
  var u = "sxStorageManager.html";
  if (l != null) { u += "?tab=" + esc(l); }
  // If we're in the host config wizard, main.sx will not be defined, but
  // parent.sx will be.
  var n = main.sx != null ? main.user + "@" + main.sx._name + ":sxStorageManager" :
  parent.user + "@" + parent.sx._name + ":sxStorageManager";

  n = escJs(n);
  var w = main.getRgWndw(n);

  if (w != null && ! w.closed) {
    w.focus();
    if (l) {
      w.slctTab(w.getTab(l));
    }
  } else {
    var p = "width=560,height=580,resizable" +
      (ie ? ",top=16,left=16" : ",screenX=16,screenY=16");
    w = window.open("", "", p);
    w.document.location.replace(u);
    w.name = n;
    main.addRgWndw(w);
  }
}


function shutdownCb(w) {
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

  if (w.err.length > 0) {
    return;
  }

  main.opCb = null;
  main.logout("");
}

function shutdown(r) {
  var str = "Please enter your reason for " +
    (r != null && r ? "restarting the system:" : "shutting the system down:");
  var msg = prompt(str, "");

  if (msg != null) {
    var q = new Query();
    q.action("/sx-shutdown");
    q.target(main.op.win());
    q.arg("req", r != null && r ? "reboot" : "halt");
    q.arg("msg", msg);

    main.opCb = shutdownCb;
    q.submit();
  }
}
