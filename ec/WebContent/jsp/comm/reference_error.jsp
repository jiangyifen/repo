<%@page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<script src="/ec/src/init.js"></script>
<script src="/ec/src/xuaLib.js"></script>
<script src="/ec/src/util.js"></script>
<script src="/ec/src/query.js"></script>
<script src="/ec/src/sxOptions.js"></script>
<link rel="stylesheet" href="/ec/css/usage.css" type="text/css" />
<link rel="stylesheet" href="/ec/css/page.css" type="text/css" />
<link rel="stylesheet" href="/ec/css/calendar.css" type="text/css" />

<title>ERROR</title>
</head>
<body>

<s:iterator value="errStrings">
	<s:property />
	<p />
</s:iterator>

</body>
</html>
