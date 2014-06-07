<%@ page language="java" contentType="text/html; charset=gb2312" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>分机状态</title>
<link rel="stylesheet" href="/ec/css/usage.css" type="text/css" />
<link rel="stylesheet" href="/ec/css/page.css" type="text/css" />

<script language="JavaScript">

    	function changepath() {
    		myrefresh();
    	}
    	function myrefresh() {
    		var myForm = document.forms['form1'];
    		myForm.submit();
    	}
    	setInterval('myrefresh()', 5000); //指定1秒刷新一次
</script>

</head>
<body>
<form method="POST" action="getSipStatus.action"  name="form1">
<div class="lst">
<table border="0" cellpadding="0" cellspacing="0">
<tr class="hdr">
<td>登录数/总数</td><td>忙（已登录）</td><td>忙（未登录）</td><td>空闲（已登录）</td><td>长时间空闲（已登录）</td><td>空闲（未登录）</td><td>工作率（【已登录忙+未登录忙】/【已登录忙+已登录空闲+未登录忙】）</td>
</tr>
<tr class="cdr">
<td><s:property value="loginCount"/>/<s:property value="total"/></td>
<td><img src="/ec/imx/status_3_16x16.png"/><s:property value="busy"/></td>
<td><img src="/ec/imx/status_8_16x16.png"/><s:property value="notLoginBusy"/></td>
<td><img src="/ec/imx/status_1_16x16.png"/><s:property value="free"/></td>
<td><img src="/ec/imx/status_7_16x16.png"/><s:property value="longTimeFree"/></td>
<td><img src="/ec/imx/status_9_16x16.png"/><s:property value="notLoginFree"/></td>
<td><s:property value="workRate"/>%</td>
</tr>
</table>
</div>


<div class="lst">
<table border="0" cellpadding="0" cellspacing="0" id="list">
 
<tr class="hdr">
<td>分机</td>
<td>HID</td>
<td>姓名</td>
<td>通话状态</td>
</tr>

<s:iterator value="sipStatus" status="st">
<tr class="cdr">
<td><s:property value="sipName"/></td>
<td><s:property value="hid"/></td>
<td><s:property value="name"/></td>
<td><img src="/ec/imx/status_<s:property value="status"/>_16x16.png"/></td>
</tr>
</s:iterator>
</table>
</div>


		<div class="std" align="right">
		共<s:property value="total"/>条结果&nbsp;&nbsp;&nbsp;&nbsp;第<s:property value="pageIndex"/>页，共 <s:property value="maxPageIndex"/>页&nbsp;&nbsp;&nbsp;&nbsp;跳转到第<s:select name="pageIndex" list="pages" onChange="changepath();"></s:select>页
		</div>

</form>

<jsp:include page="/jsp/about.jsp" flush="true"/>

</body>
</html>