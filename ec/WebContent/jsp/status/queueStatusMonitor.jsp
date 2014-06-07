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
	setInterval('myrefresh()', 10000); //指定1秒刷新一次
</script>

<body>

<form method="POST" action="getQueueStatus.action"  name="form1">
队列名：
<s:select theme="simple" list="queues" name="queueName" listKey="name" listValue="description" onblur="changepath();" onchange="changepath();"></s:select>

<img src="/ec/imx/status_1_16x16.png"/>空闲； <img src="/ec/imx/status_3_16x16.png"/>工作中； <img src="/ec/imx/status_5_16x16.png"/>离线； <img src="/ec/imx/status_6_16x16.png"/>振铃； <img src="/ec/imx/status_4_16x16.png"/>未知
<p>




<div id="queueStatus">


<div class="lst">
<table border="0" cellpadding="0" cellspacing="0">
 
<tr class="hdr">
<td>接听数/放弃数</td><td>排队中/最大排队人数</td><td>未通话/振铃/通话中/不在线</td><td>空闲队列成员数/置忙队列成员数/队列成员总数</td><td>在线空闲人数/在线置忙人数/在线总人数</td>
</tr>

<tr class="cdr">
<td><s:property value="queueParamsEvent.completed"/>/<s:property value="queueParamsEvent.abandoned"/></td>
<td><s:property value="queueParamsEvent.calls"/>/<s:property value="queueParamsEvent.max"/></td>
<td><s:property value="free"/>/<s:property value="ringing"/>/<s:property value="busy"/>/<s:property value="unavailable"/></td>
<td><s:property value="unpausedMemberCount"/>/<s:property value="pausedMemberCount"/>/<s:property value="queueMemberCount"/></td>
<td><s:property value="loginUnpausedMemberCount"/>/<s:property value="loginPausedMemberCount"/>/<s:property value="loginMemberCount"/></td>
</tr>
</table>
</div>


<div class="lst">
<table border="0" cellpadding="0" cellspacing="0" id="list">
<tr class="hdr">
<td>队列</td>
<td>位置</td>
<td>通道</td>
<td>号码</td>
<td>等待时间</td>
</tr>

<s:iterator value="queueEntryList" status="st">
<tr class="cdr">
<td><s:property value="queue"/></td>
<td><s:property value="position"/></td>
<td><s:property value="channel"/></td>
<td><s:property value="callerId"/></td>
<td><s:property value="wait"/></td>
</tr>
</s:iterator>
</table>
</div>



<div class="lst">
<table border="0" cellpadding="0" cellspacing="0" id="list">
 
<tr class="hdr">
<td>分机</td>
<td>工号</td>
<td>姓名</td>
<td>通话状态</td>
<td>电话号码</td>
<td>通话时长</td>
<td>接听数</td>
<td>状态</td>
</tr>

<s:iterator value="queueMemberList" status="st">
<tr class="cdr">
<td><s:property value="location"/></td>
<td><s:property value="memberName"/></td>
<td><s:property value="name"/></td>
<td><img src="/ec/imx/status_<s:property value="status"/>_16x16.png"/></td>
<td><s:property value="internalActionId"/></td><!-- ? -->
<td><s:property value="membership"/></td><!-- ? -->
<td><s:property value="callsTaken"/></td>
<!-- <td><a href="/ec/queueMemberPause?u=ifaceuser&p=123456&iface=<s:property value="location"/>&paused=<s:property value="paused"/>"><img src="/ec/imx/paused_<s:property value="paused"/>.gif"/></a></td> -->
<td><img src="/ec/imx/<s:property value="actionId"/>.gif"/></td>
</tr>
</s:iterator>
</table>
</div>


		<div class="std" align="right">
		共<s:property value="queueMemberCount"/>条结果&nbsp;&nbsp;&nbsp;&nbsp;第<s:property value="pageIndex"/>页，共 <s:property value="maxPageIndex"/>页&nbsp;&nbsp;&nbsp;&nbsp;跳转到第<s:select name="pageIndex" list="pages" onChange="changepath();"></s:select>页
		</div>



</div>
</form>


<p> 


<jsp:include page="/jsp/about.jsp" flush="true"/>

</body>
</html>