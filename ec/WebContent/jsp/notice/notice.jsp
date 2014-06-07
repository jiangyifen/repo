<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<title>公告</title>
<script src="src/init.js"></script>
<script src="src/xuaLib.js"></script>
<script src="src/util.js"></script>
<script src="src/vmCtrlBar.js"></script>
<script src="src/vmCtxMenu.js"></script>
<script src="src/sxMonitor.js"></script>
<script language="javascript" type="text/javascript"
	src="src/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" href="css/usage.css" type="text/css" />
<link rel="stylesheet" href="css/page.css" type="text/css" />
<link rel="stylesheet" href="css/calendar.css" type="text/css" />
<script language="javascript">
function popWindow(page) {
    window.open(page, "", "scrollbars=yes,width=880,height=750,resizable");
}
</script>
</head>

<body>
<form name="notice" action="noticeGet.action">
<div id="std">
<table border="0" cellpadding="0" cellspacing="0"
	style="border-top: 0px solid #E6E6E6; margin-top: 24px">
	<tr>
		<td width="100%">
		开始日期:&nbsp;&nbsp;&nbsp;&nbsp;<input name="beginTime" value="<s:property value="beginTime"/>" type="text" onclick="WdatePicker({skin:'blue'})" class="text" />&nbsp;&nbsp;00:00:00&nbsp;&nbsp;&nbsp;&nbsp;
		结束日期:&nbsp;&nbsp;&nbsp;&nbsp;<input name="endTime" value="<s:property value="endTime"/>" type="text" onclick="WdatePicker({skin:'blue'})" class="text" />&nbsp;&nbsp;23:59:59&nbsp;&nbsp;&nbsp;&nbsp;
		标题:&nbsp;&nbsp;&nbsp;&nbsp;<input name="title" value="<s:property value="title"/>" type="text" class="text" />&nbsp;&nbsp;&nbsp;&nbsp;
		内容:&nbsp;&nbsp;&nbsp;&nbsp;<input name="content" value="<s:property value="content"/>" type="text" class="text" />&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
		
		<td class="btnColLft">
		<div class="btn" onClick="document.notice.submit()">
		<table>
			<tr>
				<td class="lbl" nowrap>查询</td>
			</tr>
		</table>
		</div>
		</td>
		
		<td></td>
		
		
		<td class="btnColLft">
		<div class="btn" onClick="popWindow('noticeGetDpmt.action')" >
		<table>
			<tr>
				<td class="lbl" nowrap>新增</td>
			</tr>
		</table>
		</div>
		</td>
		
	</tr>
</table>
</div>

<div class="lst">
<table border="0" cellpadding="0" cellspacing="0" id="list">
	<tr class="hdr">
		<td nowrap>公告标题</td>
		<td nowrap>发布日期</td>
		<td nowrap>删除</td>
	</tr>
	<s:iterator value="noticeList">
		<tr class="cdr">
			<td class="dn" nowrap><a href="javascript:" onClick="popWindow('noticeDetailGet?id=<s:property value="id" />')" ><s:property value="title" /></a></td>
			<td class="dn" nowrap><s:property value="date" /></td>
			<td class="dn" nowrap><a href="noticeDelete?id=<s:property value="id" />">删除</a></td>
		</tr>
	</s:iterator>
</table>
</div>

<div class="std" align="right">共<s:property value="noticeCount" />条结果&nbsp;&nbsp;&nbsp;&nbsp;第<s:property
	value="pageIndex" />页，共 <s:property value="maxPageIndex" />页&nbsp;&nbsp;&nbsp;&nbsp;跳转到第<s:select
	name="pageIndex" list="pages" onChange="document.sms.submit()"></s:select>页
</div>

</form>

<br />

<jsp:include page="/jsp/about.jsp" flush="true"/>

</body>
</html>




