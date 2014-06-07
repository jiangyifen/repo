<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>动态队列管理</title>
<script src="src/multiselect.js"></script>
<link rel="stylesheet" href="css/multiselect.css" type="text/css" />
<link rel="stylesheet" href="css/usage.css" type="text/css" />
<link rel="stylesheet" href="css/page.css" type="text/css" />
<script type="text/javascript">
	function refresh_selected() {

		path = "queueUserLinkGet?queueName="
				+ document.getElementById("queueName").value;

		window.location = path;
	}

	function submitme() {
		var count = document.getElementById("toBox").options.length;
		for ( var i = 0; i < count; i++) {
			document.getElementById("toBox").options[i].selected = true;
		}
		document.getElementById("form1").submit();
	}
</script>

</head>

<body>



<form id="form1" method="post" action="queueUserLinkUpdate">
<div id="sxStatHdr" class="sctHdr">
<table border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="100%">动态队列管理</td>
	</tr>
</table>
</div>

<div class="cfg">
<table border="0" cellpadding="0" cellspacing="0">
	<tr class="cat">
		<td colspan="2">队列名:<s:select theme="simple" list="queues"
			name="queueName" listKey="name" listValue="description"
			onchange="refresh_selected();" /></td>
	</tr>
	<tr class="cat">
		<td colspan="2"><select multiple name="fromBox" id="fromBox">
			<s:iterator value="fromBoxUser">
				<option value="<s:property value="username"/>"><s:property value="department.departmentname"/>-<s:property value="name"/>-[<s:property value="username"/>]</option>
			</s:iterator>
		</select> <select multiple name="toBox" id="toBox">
			<s:iterator value="toBoxUser">
				<option value="<s:property value="username"/>"><s:property value="department.departmentname"/>-<s:property value="name"/>-[<s:property value="username"/>]</option>
			</s:iterator>
		</select> <input type="button" value="SAVE" onclick="submitme();" /></td>
	</tr>
</table>
</div>




</form>

<script type="text/javascript">
	createMovableOptions("fromBox", "toBox", 500, 300, '可选的用户', '已选择的用户');
</script>

</body>
</html>