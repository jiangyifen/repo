var ok = false;

// --------------------------------------------------------------------------
 
/* --------------------------------------------------------------------------
*
* User messages prior to reboot.
*
* preRebootMsg - an array containing messages to display prior to the user 
* clicking the reboot link.
*  array key - id of message to display
*  array value - message to display;;possible tooltip hint
*
* The following should be done in order for a message to be displayed :
* 1. Add a message to this array with a unique id. Tip is seperated by ;;
*    For example: preRebootMsg["msg1"] = "my first message"; // no tip
*                 preRebootMsg["msg2"] = "my second message;;"; 
*                 preRebootMsg["msg3"] = "my third message;;my third tip"; 
* 2. In the referring page add the "userMsg" parameter to the url with the msg 
*    id to display. 
*    For example:  reboot.html?someParams&userMsg=msg1,msg2,msg3&someParams
*
* ----------------------------------------------------------------------------*/

var preRebootMsg = new Array();

// add user messages below, key is the one passed in the URL by referrer
preRebootMsg["hyperThreading"] = "System hyperthreading settings will take " +
         "effect only if hyperthreading is enabled in the BIOS. Verify " +
         "your hyperthreading settings in the BIOS.";


// --------------------------------------------------------------------------

function exit(i) {
  if (parent.ctx == "wizard") {
    if (i == -1) {
      self.location.replace("/vmware/en/esxHostConfigWizardStartup.html");
    } else {
      self.location.replace("/vmware/en/storageConfiguration.html");
    }
  } else {
    if (i == 1) {
      if (main && main.getUpdates) { main.getUpdates(true); }
    }
    parent.close();
  }
}


// --------------------------------------------------------------------------

function opCb(w) {
  if (ok) {
    next();
    return;
  }
}


// --------------------------------------------------------------------------

function getArg(a) {
  var s = location.search.substr(1);
  s = s.split('&');
  for (var i = 0; i < s.length; i++) {
    var r = s[i].split('=');
    if (r[0] == a) {
      return r[1];
    }
  }
}


// --------------------------------------------------------------------------

function initPage() {
  var w;
  var q = new Query(self.location.search);


  // Dom Objects ------------------------------------------------------------
  var dom = {start:null,reboot:null,setup:null};

  for (var n in dom) {
    dom[n] = obj(n);
  }

  // display pre-reboot user messages
  dom.bdy = obj("msgTbl").getElementsByTagName("tbody")[0];
  dom.msgRow = dom.bdy.removeChild(obj("msgRow"));
  
  // user messages ids are sent via the URL 
  var urlUsrMsgs = q.arg("usrMsg")[0]; 

  if(urlUsrMsgs != ""){ // there are messages to display

    var msgAndTitle;
    var objMsgTr; // the row added to the messages table
    var objMsgBdy;
    var objImgTip;

    // might be required to display multiple messages
    var displayMsgs = urlUsrMsgs.split(','); 

    for(var i = 0; i < displayMsgs.length; i++){
      if(preRebootMsg[displayMsgs[i]]){ // if message exists for this msgId
	msgAndTitle = preRebootMsg[displayMsgs[i]].split(";;");
	// add message row
	objMsgTr = dom.bdy.appendChild(dom.msgRow.cloneNode(true)); 

	// set message attributes
	objMsgBdy = objMsgTr.getElementsByTagName("span")[0];
	objMsgBdy.id =  displayMsgs[i]+"_msgBdy"+i;
	objMsgBdy.name = objMsgBdy.id;
	objMsgBdy.innerHTML = msgAndTitle[0];
	// set tooltip hint if given
	objImgTip = objMsgTr.getElementsByTagName("img")[0];
	objImgTip.id = displayMsgs[i]+"_toolTip"+i;
	objImgTip.name = objImgTip.id; 
	if(msgAndTitle[1]){
	  objImgTip.title += msgAndTitle[1];
	} 
      } 
    }
  }
  

  
  // ------------------------------------------------------------------------

  var start = new Step("start", dom.start);
  var reboot = new Step("reboot", dom.reboot);
  var novmk = parent.ctx != "wizard" ? null : new Step("novmk", obj("novmk"));

  checkForVmk = function (d) {
    if (d.vmk) {
      window.setTimeout("exit(1);", 1000);
    } else {
      prev = function(){exit(-1);};
      next = function(){curStep=curStep.slct();};
      // We need to load some URL that will trigger a log in, but that won't
      // put us into an infinite reload loop.
      parent.loadData("/startup?ctx=wizard");
      window.setTimeout("next();", 1000);
    }

    return true;
  }

  start.nxt[0] = reboot;
  start.exec = function (i) {
    reboot.prv = null;
    if (i == 0) {
      if (parent.ctx != "wizard") {
        main.clearTimeout(main.wto);
        main.clearInterval(main.bto);
      }

      // Lock the wizard controls. XXX: Should disable, but I don't have time.
      prev = next = function () { ; };

      parent.loadData("/reboot?request=reboot&message=Startup%20profile%20changes");

      initTimer();
      initPing(checkForVmk);
      // If we start the ping too early, we may get a false positive.
      window.setTimeout("ping();", 120000);

      obj("msieWrnMsg").css("display", ie ? "" : "none");
    }
  };

  if (parent.ctx == "wizard") {
    reboot.nxt[0] = novmk;
    reboot.exec = function () {
      novmk.prv = null;
    };
  }

  var curStep = start;

  // ------------------------------------------------------------------------

  prev = function () {
    curStep = curStep.slct(-1);
  };

  next = function (i) {
    curStep = curStep.slct(i);
  };

  if (parent.ctx == "wizard") {
    if (q.arg("reboot")[0] == 0) {
      initPing(function (d) {if(parent.ctx=="wizard"&&d.vmk){window.setTimeout("next(1);", 1000);}else{next();}return d.vmdk;});
      ping();
    } else {
      next();
    }
  }
}


function initTimer() {
  var st = new Date().getTime();
  var tm = obj("time");
  var it;

  function pad(s, l, c) {
    s = s+"";
    c = c == null ? 0 : c;
    for (var i = s.length; i < l; i++) s = c + "" + s;
    return s;
  }

  getElapsedTimeStr = function () {
    var s = new Date().getTime() - st;
    s = parseInt(s / 1000);
    var m = parseInt(s / 60);
    s = s % 60;

    if (m > 59) {
      return "one hour";
    } else {
      return m + ":" + pad(s, 2, 0) + " minutes";
    }
  }

  timer = function () {
    var str = getElapsedTimeStr();

    if (str == "one hour") {
      str = "More than " + str;
    }

    tm.innerHTML = str;
  };

  it = window.setInterval("timer();", 1000);
}

