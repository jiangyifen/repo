<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<title>新增公告</title>
<script src="src/init.js"></script>
<script src="src/xuaLib.js"></script>
<script src="src/util.js"></script>
<script src="src/vmCtrlBar.js"></script>
<script src="src/vmCtxMenu.js"></script>
<script src="src/sxMonitor.js"></script>

<link rel="stylesheet" href="css/usage.css" type="text/css" />
<link rel="stylesheet" href="css/page.css" type="text/css" />
<link rel="stylesheet" href="css/calendar.css" type="text/css" />

</head>

<body>
<form name="notice" action="noticeAdd.action" method="post">
<div id="std">
<table border="0" cellpadding="0" cellspacing="0" style="border-top: 0px solid #E6E6E6; margin-top: 24px">

	<tr>
		<td width="100%">
		<s:iterator value="dpmts">
			<input type="checkbox" name="dpmts" value="<s:property value="departmentname"/>" /><s:property value="description"/><br>
		</s:iterator>
		</td>
	</tr>
	<tr>
		<td>
		标题：
		</td>
	</tr>
	<tr>
		<td width="100%">
		<input name="title" type="text" class="text" style="overflow-x:visible;width:560;"/>
		</td>
	</tr>
	<tr>
		<td>
		内容：
		</td>
	</tr>
	<tr>
		<td width="100%">
		<textarea  name="content"  class="text" rows="30" cols="100"></textarea>
		</td>
	</tr>
	<tr>
		<td class="btnColLft">
		<div class="btn" onClick="document.notice.submit()">
		<table>
			<tr>
				<td class="lbl" nowrap>提交</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>
</div>


</form>

<br />

<jsp:include page="/jsp/about.jsp" flush="true"/>

</body>
</html>




