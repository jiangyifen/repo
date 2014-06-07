/* Copyright 1998 VMware, Inc.  All rights reserved. -- VMware Confidential */

var prod, user, opts;

function initSx() {
  if (main == null || ! main.sx) {
    window.setTimeout("initSx();", 50);
    return;
  }

  prod.innerHTML = main.sx._prodName;
  user.innerHTML = "| " + main.user + "@" + main.sx._name;

  if (opts != null) {
    if (main.admin) {
      Style.set(opts, Style.NRML);
    }
    main.initTabs();
  }
}

function initPage() {
  prod = obj("prod");
  user = obj("user");
  opts = obj("sxOptions");

  initSx();
}

function escapeJS(str) {
  bad='\'",.<>?/:;|\\[]{}=+-()*&%!~@#$^'.split('');
  for(var i=1;i<bad.length;i++){
    str=str.split(bad[i]);
    str=str.join(bad[i].charCodeAt(0));
  }
  return str;
}

function wndwName(s) {
  s += '_' + window.location.protocol.split(':').join('');
  s += '_' + window.location.hostname.split('.').join('_');
  s += window.location.port ? '_' + window.location.port : '';

  return escapeJS(s);
}

function fmWndw() {
  var u = "/showdir";
  var n = wndwName("VMwareMuiFileManager");
  var w = main.getRgWndw(n);

  if (w != null && ! w.closed) {
      w.focus();
  } else {
    w = window.open("", "", "width=800,height=536,resizable");
    w.document.location.replace(u);
    w.name = n;
    main.addRgWndw(w);
  }
}
