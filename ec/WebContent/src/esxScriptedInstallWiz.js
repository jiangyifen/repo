var submitted = false;

function check_rs() {
  document.forms[0].remote_server.disabled =
   ( document.forms[0].install_method.value != 'remote' ? true : false );
  document.forms[0].network.disabled =
   ( document.forms[0].install_method.value == 'cdrom' ? true : false );
}

function check_mbr() {
  document.forms[0].mbr_partition.disabled =
   ( document.forms[0].mbr.value != 'custom' ? true : false );
}

// --------------------------------------------------------------------------

function exit(idx) {
  parent.close();
}

// --------------------------------------------------------------------------

function opCb(w) {
  if (w.scriptedInstallSetup != null) {
    if (false == w.scriptedInstallSetup) {
      self.location.replace("/vmware/en/esxScriptedInstallSetup.html");
      return;
    }
  }

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

  if (submitted) {
    submitted = false;

    if (w.err.length > 0) {
      prev();
      return;
    }
  }

  next();
}


// --------------------------------------------------------------------------

function initPage() {
  var w = parent.op.win();
  //q = new Query(document.forms[0]);

  var ldgMsg = new Step("ldgMsg", obj("ldgMsg"));
  var getInstallParams = new Step("getInstallParams", obj("getInstallParams"));
  var getNetworkParams = new Step("getNetworkParams", obj("getNetworkParams"));
  var getPartitionParams = new Step("getPartitionParams", 
   obj("getPartitionParams"));
  var svgMsg = new Step("svgMsg", obj("svgMsg"));

  getInstallParams.nxt[0] = getPartitionParams;
  getInstallParams.nxt[1] = getNetworkParams;
  getInstallParams.exec = function (i) {
    var q = new Query(document.forms[0]);
    if (q.arg('passwd1')[0] != q.arg('passwd2')[0]) {
      alert("Root passwords must match.");
      return false;
    }

    if (! (q.arg('passwd1')[0]).match(/^\S{6,}$/)) {
      alert("Root passwords must be longer than six characters");
      return false;
    }

    if (q.arg('network')[0] == 'dhcp') {
      parent.slctBtns("wizNoNext");
    }
    else {
      parent.slctBtns("wiz");
    }
  };

  getNetworkParams.nxt[0] = getPartitionParams;
  getNetworkParams.exec = function (i) {
    parent.slctBtns("wizNoNext");
  }

  var MAX_PARTITIONS = 10;
  getPartitionParams.nxt[0] = svgMsg;
  getPartitionParams.exec = function (i) {
    var q = new Query(document.forms[0]);
    var haveRoot = false;
    var haveSwap = false;

    for (id = 0; id < MAX_PARTITIONS; id++) {
      var drive = q.arg('drive' + id)[0];
      var mount = q.arg('mount' + id)[0];
      var size = q.arg('size' + id)[0];
      var type = q.arg('type' + id)[0];
      var grow = q.arg('grow' + id)[0];

      // Only do checking if we have a valid mount name
      if (mount.match(/\S+/)) {
        if (mount.match(/\s+/)) {
          alert("Mount points cannot have spaces in their names");
          return false; 
        }

        if (! mount.match(/^\/[\w\d]*/) && mount != 'swap') {
          alert("Partition names " + mount + " must start with a '/'");
          return false;
        }

        var verifySize = parseInt(size);
        if (isNaN(verifySize) || verifySize <= 0) {
          alert("Partition sizes must be whole, positive integers");
          return false;
        }
      }

      if (mount == '/')
        haveRoot = true;

      if (mount == 'swap')
        haveSwap = true;
    }

    if (! haveRoot) {
      alert("You must define a root partition");
      return false;
    }

    if (! haveSwap) {
      alert("You must define a swap partition");
      return false;
    }

    submitted = true;
    q.submit();
    parent.slctBtns("std");
  };

  ldgMsg.nxt[0] = getInstallParams;
  ldgMsg.exec = function (i) {
    q = new Query(document.forms[0]);
    parent.slctBtns("wizNoBack");
  };

  svgMsg.nxt[0] = exit;
  svgMsg.exec = function(idx) {
    exit();
  }

  var curStep = ldgMsg;

  prev = function () {
    curStep = curStep.slct(-1);

    if (curStep == getInstallParams)
      parent.slctBtns("wizNoBack");
  };

  next = function (idx) {
    if (curStep == getInstallParams) {
      if (q.arg('network')[0] != 'dhcp') {
        curStep = curStep.slct(1);
        return;
      } 
    }

    curStep = curStep.slct(idx);
  };

  check_rs();
  check_mbr();

  parent.loadData("/scriptedinstall");
}
