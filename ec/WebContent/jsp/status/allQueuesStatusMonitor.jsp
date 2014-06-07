<%@ page language="java" contentType="text/html; charset=gb2312" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
    <title></title>
    
    <link rel="stylesheet" href="/ec/css/usage.css" type="text/css" />
    <link rel="stylesheet" href="/ec/css/page.css" type="text/css" />
</head>

<script language="JavaScript">

	function changepath() {
		myrefresh();
	}
	function myrefresh() {
		var myForm = document.forms['form1'];
		myForm.submit();
	}
	setInterval('myrefresh()', 5000); //指定5秒刷新一次
</script>

<body>

<form method="POST" action="getQueueStatus.action"  name="form1">

<p>

<div id="queueStatus">


	<div class="lst">
	<table border="0" cellpadding="0" cellspacing="0">

		<tr class="hdr">
			<td>队列名</td>
			<td>接听数/放弃数</td>
			<td>排队中/最大排队人数</td>
			<td>未通话/振铃/通话中/不在线</td>
			<td>空闲队列成员数/置忙队列成员数/队列成员总数</td>
			<td>在线空闲人数/在线置忙人数/在线总人数</td>
		</tr>
		
		<s:iterator value="queueStatusList" status="st">
		<tr class="cdr">
			<td><s:property value="description" /></td>
			<td><s:property value="queueParamsEvent.completed" />/<s:property value="queueParamsEvent.abandoned" /></td>
			<td><s:property value="queueParamsEvent.calls" />/<s:property value="queueParamsEvent.max" /></td>
			<td><s:property value="free" />/<s:property value="ringing" />/<s:property value="busy" />/<s:property value="unavailable" /></td>
			<td><s:property value="unpausedMemberCount" />/<s:property value="pausedMemberCount" />/<s:property value="queueMemberCount" /></td>
			<td><s:property value="loginUnpausedMemberCount" />/<s:property	value="loginPausedMemberCount" />/<s:property value="loginMemberCount" /></td>
		</tr>
		</s:iterator>
		
	</table>
	</div>


</div>

<input name="allQueueStatus" type="hidden" class="text" value="true"/>
</form>


<p> 


<jsp:include page="/jsp/about.jsp" flush="true"/>

</body>
</html>