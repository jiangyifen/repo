var ctx = "wizard";

var tabs, page, toc, btns, op, auth, sx, user;

var tle = new Object();

function initHelp() {
  function getBaseName(s) {
    s = s.split("?")[0];

    var pos = s.lastIndexOf("/");

    if (pos == -1) {
      // No slash
      return s;
    }

    s = s.substr(pos + 1).replace(/(.*)(\.html)(.*)/, "$1Help$2$3");

    return s;
  }

  help = function () {
    var helpurl = getBaseName(page.doc().location.href);

    if (helpurl == null || ! helpurl) { return; }

    var w = window.open("", "",
      "location,menubar,resizable,scrollbars,status,toolbar,width=730,height=487");
    w.document.location.href = helpurl;
    w.name = "vmware_mui_help";
    w.focus();
    main.addRgWndw(w);
  };
}
initHelp();


function initSteps() {
  next = function () {
    if (page.win().next) { page.win().next(); }
  };

  prev = function () {
    if (page.win().prev) { page.win().prev(); }
  };
}


function dataCb(w) {
  if (page.win().dataCb) { page.win().dataCb(w); }
}


function cb(w) {
  sx = {};
  sx._name = w.sx.a;
  sx._prodName = w.sx.c;
  user = w.user;
  sx._ht = w.sx.n;

  auth.pos(-99999, -99999);
  loadData();
}


function opCb(w) {
  if (page != null && page.win().opCb) { page.win().opCb(w); }
}


function pingCb(w) {
  if (page != null && page.win().pingCb) { page.win().pingCb(w); }
}


function loginCb(w) {
  try {
    auth.pos(0, 0);
    auth.win().setup(w.user, w.str);
  } catch(ex) {
    ;
  }
}


function layoutCb() {
  var tabsH = tabs.win().dh();
  var btnsH = btns.win().dh();

  auth.dim(self.dim().w, self.dim().h);

  // XXX: It would be better to calculate the width required to display the
  // table of contents without wrapping.
  var tocW = 200;

  tabs.dim(self.dim().w, tabsH);
  btns.dim(self.dim().w, btnsH);

  toc.dim(tocW, self.dim().h - (tabsH + btnsH));
  toc.pos(0, tabs.dim().h);

  page.dim(self.dim().w - tocW, self.dim().h - (tabsH + btnsH));
  page.pos(tocW, tabs.dim().h);

  btns.pos(0, self.dim().h - btnsH);
}

function initLoadData() {
  var loc;
  loadData = function (u) {
    if (u == null) u = loc;
    if (u == null) return;
    op.win().location.replace(u);
    loc = u;
  };
}
initLoadData();


function initPage() {
  document.title = document.domain + ": System Configuration Wizard";

  auth = obj("auth");
  tabs = obj("tabs");
  page = obj("page");
  btns = obj("btns");
  toc = obj("toc");
  op = obj("op");

  auth.pos(-99999, -99999);
  for (var i = 0; i < auth.doc().forms.length; i++) {
    auth.doc().forms[i].target = "op";
  }

  layoutCb();

  lstn(self, "resize", layoutCb);
  lstn(self, "unload", function () {main.logout("");});

  initSteps();

  op.doc().location.replace("/monitor/index.pl");
}
