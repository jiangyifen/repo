<%@page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title>EC CTI Server</title>
<script>
<!--
if (document.all) {
  if (parseFloat(navigator.appVersion) < 4 ||
    (navigator.userAgent.toLowerCase()).indexOf("msie 4") > -1 ||
    (navigator.userAgent.toLowerCase()).indexOf("msie 5.0") > -1) {
    alert("Microsoft Internet Explorer versions prior to 5.5 are not supported.");
  }
} else if (document.layers) {
  alert("Netscape Navigator versions prior to 7.0 are not supported.");
} else if (document.getElementById == null) {
  alert("Browsers that do not implement the Document Object Model standard are not supported.");
} else if (navigator.userAgent.match(/netscape6/i)) {
  alert("Netscape versions prior to 7.0 are not supported.");
} else if (navigator.userAgent.match(/gecko/i)) {
  var m = navigator.userAgent.match(/rv:(\d+)(.*?)\)/i);
  if (m == null) {
    alert("Your browser is based on Mozilla/Gecko technology, but its vintage could not be determined consequently your mileage may vary.");
  } else if (m[1] < 1) {
    alert("Your browser is based on Mozilla/Gecko technology, but its version (" + m[1] + m[2] + ") is not supported.");
  }
}

var isMain = true;


function initIndexPage(){
    if('${sessionScope.login}' == 'true'){
        document.getElementById('iframe_main').style.top = '0';
    }else{
    	document.getElementById('iframe_login').style.top = '0';
    }
}
//-->
</script>
<script src="../src/init.js"></script>
<script src="../src/util.js"></script>
<script src="../src/query.js"></script>
<script src="../src/xuaLib.js"></script>
<script src="../src/query.js"></script>
<script src="../src/main.js"></script>
<script src="src/menu.js"></script>


<link rel="stylesheet" href="../css/page.css" type="text/css" />
<link rel="stylesheet" href="../css/frames.css" type="text/css" />
<style type="text/css">
<!--
body {
  margin: 0px;
  overflow: hidden;
  width: 100%;
  height: 100%;
}
-->
</style>
</head>

<body id="bodyObj" onload="initIndexPage()">

<iframe id="iframe_login" name="login" src="login.jsp" scrolling="no"  frameborder="0" style="position:absolute; top:-99999px; left:0px; width:100%; height:100%; z-index:10;"></iframe>
<iframe id="iframe_main" name="main" src="main.jsp" scrolling="no"  frameborder="0" style="position:absolute; top:-999999px; left:0px; width:100%; height:100%; z-index:10;"></iframe>


</body>
</html>
