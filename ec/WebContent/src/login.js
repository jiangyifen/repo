function validate() {
	if (!document.v.u.value) {
		alert("Please enter a username.");
		document.v.u.focus();
		return false;
	}
	return true;
}

function login() {
	if (validate()) {
		document.k.username.value = document.v.u.value;
		document.k.password.value = document.v.p.value;
		// document.k.username.value = toBase64(document.v.u.value);
		// document.k.password.value = toBase64(document.v.p.value);
		document.k.submit();
		document.k.username.value = "";
		document.k.password.value = "";
	}
}

function setup(u, e) {
	obj("login").css("display", "");

	self.focus();
	document.v.u.focus();

	if (u != null) {
		document.v.u.value = fromBase64(u);
		document.v.u.select();
	} else {
		document.v.u.value = "";
	}
	document.v.p.value = "";

	if (e != null) {
		obj("str").innerHTML = e;
		obj("err").css("display", "");
	} else {
		obj("err").css("display", "none");
	}

	var tc = 'vmware.mui.test';
	var cp = document.cookie.indexOf(tc);

	if (cp == -1) {
		obj("cookieless").css("display", "");
	}

	// Set up the download menu
	if (dlmenuShow) {
		var idx = 0;
		obj("dlhelp").css("display", dlhelpShow ? "" : "none");
		for ( var i = 0; i < dlmenu.length; i++) {
			if (dlmenu[i].show) {
				document.d.dlmenu.options[idx++] = new Option(dlmenu[i].name,
						dlmenu[i].url);
			}
		}
		obj("download").css("display", "");
	} else {
		obj("download").css("display", "none");
	}
}

helpurl = "downloadHelp.html";
function download() {
	var dlframe = obj("dlframe");
	var idx = document.d.dlmenu.selectedIndex;
	var url = document.d.dlmenu[idx].value;
	dlframe.win().location.replace(url);
}
