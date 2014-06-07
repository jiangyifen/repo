var ctx = "editor";

var tabs, page, toc, btns, op, q, helpurl;

var tle = new Object();


function help() {
  if (helpurl == null | ! helpurl) { return; }

  var w = window.open("", "",
    "location,menubar,resizable,scrollbars,status,toolbar,width=730,height=487");
  w.document.location.href = helpurl;
  w.name = "vmware_mui_help";
  w.focus();
  main.addRgWndw(w);
}


function dataCb(w) {
  if (page != null && page.win().dataCb) page.win().dataCb(w);
}


function opCb(w) {
  if (page != null && page.win().opCb) page.win().opCb(w);
}


function pingCb(w) {
  if (page != null && page.win().pingCb) page.win().pingCb(w);
}


function layoutCb() {
  var tabsH = tabs.win().dh();
  var btnsH = btns.win().dh();

  tabs.dim(self.dim().w, tabsH);
  btns.dim(self.dim().w, btnsH);

  page.dim(self.dim().w, self.dim().h - (tabsH + btnsH));
  page.pos(0, tabs.dim().h);

  btns.pos(0, self.dim().h - btnsH);
}


function initTglBtns() {
  // If boolean s is true, show the buttons. If boolean s is false, hide them.
  tglBtns = function (s) {
    if ((s == null && btns.css("display") == "none") || (s != null && s)) {
      btns.dim(self.dim().w, 1);
      btns.css("display", "");
    } else if ((s == null && btns.css("display") != "none") || (s != null && !s)) {
      btns.dim(self.dim().w, btns.win().dh());
      btns.css("display", "none");
    }
  };
}
initTglBtns();




function loadData(u) {
  op.win().location.replace(u);
}

function loadPage() {
  var doc = obj("bodyObj", page.win());
  switch (q.arg("unit")[0]) {
    case "startup":
      page.win().location.replace("esxHostConfigWizardStartup.html");
      helpurl = "vserver/HostConfigWizardStartupHelp.html";
      break;
    case "storage":
      page.win().location.replace("storageConfiguration.html");
      helpurl = "vserver/storageConfigurationHelp.html";
      break;
    case "network":
      if (main.sx._prodId == "gsx") {
        page.win().location.replace("sxNetwork.html");
        helpurl = "vserver/sxNetworkHelp.html";
      } else {
        page.win().location.replace("networkConnections.html");
        helpurl = "vserver/networkConnectionsHelp.html";
      }
      break;
    case "swap":
      page.win().location.replace("getSwap.html");
      helpurl = "vserver/getSwapHelp.html";
      break;
    case "users":
      page.win().location.replace("sxUsersAndGroups.html");
      helpurl = "vserver/sxUsersAndGroupsHelp.html";
      break;
    case "san":
      page.win().location.replace("sxSanConfiguration.html");
      helpurl = "vserver/sxSanConfigurationHelp.html";
      break;
    case "snmp":
      page.win().location.replace("sxSnmpConfiguration.html");
      helpurl = "vserver/sxSnmpConfigurationHelp.html";
      break;
    case "security":
      if (main.sx._prodId == "gsx") {
        page.win().location.replace("/security/index.pl");
      } else {
        page.win().location.replace("securitySettings.html");
      }
      helpurl = "vserver/securitySettingsHelp.html";
      break;
    case "advanced":
      page.win().location.replace("/vmkernel-config?mui=new");
      helpurl = "vserver/sxAdvancedHelp.html";
      break;
    case "license":
      page.win().location.replace("esxHostConfigWizardLicense.html");
      helpurl = "vserver/HostConfigWizardLicenseHelp.html";
      break;
    case "scriptedinstall":
      page.win().location.replace("esxScriptedInstallWiz.html");
      helpurl = "vserver/ScriptedInstallWiz.html";
      break;
    case "vmSeqeunce":
      page.win().location.replace("sxVmStartupShutdown.html");
      helpurl = "vserver/sxVmStartupShutdownHelp.html";
      break;
    default:
      doc.innerHTML = "Not Implemented";
      break;
  }
}



function initPage() {
  q = new Query(location.search);
  document.title = document.domain + ": System Configuration";

  tabs = obj("tabs");
  page = obj("page");
  btns = obj("btns");
  op = obj("op");


  layoutCb();
  lstn(self, "resize", layoutCb);

  slctBtns = function (s) {
    if (btns.win().slctBtns == null) {
      window.setTimeout("slctBtns('" + s + "');", 10);
      return;
    }

    return btns.win().slctBtns(s);
  }

  next = function () {
    if (page.win().next) page.win().next(); else self.close();
  };

  prev = function () {
    if (page.win().prev) page.win().prev(); else self.close();
  };

  loadPage();
}
