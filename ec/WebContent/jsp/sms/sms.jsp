<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<title>短信详单</title>
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
    window.open(page, "", "scrollbars=yes,width=660,height=450,resizable");
}
</script>
</head>

<body>
<form name="sms" action="getSm.action">
<div id="std">
<table border="0" cellpadding="0" cellspacing="0"
	style="border-top: 0px solid #E6E6E6; margin-top: 24px">
	<tr>
		<td width="100%">开始日期:&nbsp;&nbsp;&nbsp;&nbsp;<input
			name="beginTime" value="<s:property value="beginTime"/>" type="text"
			onclick="WdatePicker({skin:'blue'})" class="text" />&nbsp;&nbsp;00:00:00&nbsp;&nbsp;&nbsp;&nbsp;
		结束日期:&nbsp;&nbsp;&nbsp;&nbsp;<input name="endTime"
			value="<s:property value="endTime"/>" type="text"
			onclick="WdatePicker({skin:'blue'})" class="text" />&nbsp;&nbsp;23:59:59&nbsp;&nbsp;&nbsp;&nbsp;
		短信内容:&nbsp;&nbsp;&nbsp;&nbsp;<input name="content"
			value="<s:property value="content"/>" type="text" class="text" />&nbsp;&nbsp;&nbsp;&nbsp;
		手机号码:&nbsp;&nbsp;&nbsp;&nbsp;<input name="mobile"
			value="<s:property value="mobile"/>" type="text" class="text" />&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
	<tr>
		<td>方向:&nbsp;&nbsp;&nbsp;&nbsp;<select name="status">
			<option value=""
				<%if (request.getAttribute("status") != null
					&& request.getAttribute("status").equals("")) {%>
				selected <%}%>>全部</option>
			<option value="RECEIVE"
				<%if (request.getAttribute("status") != null
					&& request.getAttribute("status").equals("RECEIVE")) {%>
				selected <%}%>>接收</option>
			<option value="DELIVRD"
				<%if (request.getAttribute("status") != null
					&& request.getAttribute("status").equals("DELIVRD")) {%>
				selected <%}%>>发送</option>

		</select>&nbsp;&nbsp;&nbsp;&nbsp; 发送者id:&nbsp;&nbsp;&nbsp;&nbsp;<input
			name="senderId" value="<s:property value="senderId"/>" type="text"
			class="text" />&nbsp;&nbsp;&nbsp;&nbsp; 批次:&nbsp;&nbsp;&nbsp;&nbsp;<input
			name="batchNumber" value="<s:property value="batchNumber"/>"
			type="text" class="text" />&nbsp;&nbsp;&nbsp;&nbsp; 导出Excel<input
			type="checkbox" value="true" name="excel" /></td>
			
		<td class="btnColLft">
		<div class="btn" onClick="document.sms.submit()">
		<table>
			<tr>
				<td class="lbl" nowrap>提交</td>
			</tr>
		</table>
		</div>
		</td>
		
		<td>&nbsp;</td>
		
		<td class="btnColLft">
		<div class="btn" onclick="popWindow('jsp/sms/smSend.jsp');">
		<table>
			<tr>
				<td class="lbl" nowrap>发送短信</td>
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
		<td nowrap>发送时间</td>
		<td nowrap>短信内容</td>
		<td nowrap>手机号码</td>
		<td nowrap>通道号码</td>
		<td nowrap>状态</td>
		<td nowrap>发送者id</td>
		<td nowrap>批次</td>
	</tr>
	<s:iterator value="smTasks">
		<tr class="cdr">
			<td class="dn" nowrap><s:property value="timestamp" /></td>
			<td class="dn" nowrap><s:property value="content" /></td>
			<td class="dn" nowrap><s:property value="mobile" /></td>
			<td class="dn" nowrap><s:property value="srcTermId" /></td>
			<td class="dn" nowrap><s:property value="status" /></td>
			<td class="dn" nowrap><s:property value="senderId" /></td>
			<td class="dn" nowrap><s:property value="batchNumber" /></td>
		</tr>
	</s:iterator>
</table>
</div>

<div class="std" align="right">
共<s:property value="smCount" />条结果&nbsp;&nbsp;&nbsp;&nbsp;
第<s:property value="pageIndex" />页，共 <s:property value="maxPageIndex" />页&nbsp;&nbsp;&nbsp;&nbsp;
<input name="lastornext" type="hidden" value="0"/>
<a href="javascript:document.sms.lastornext.value=document.sms.lastornext.value - 1;document.sms.submit();">上一页</a>
跳转到第<s:select name="pageIndex" list="pages" onChange="document.sms.submit()"></s:select>页
<a href="javascript:document.sms.lastornext.value=document.sms.lastornext.value - -1;document.sms.submit();">下一页</a>

</div>

</form>

<br />

<jsp:include page="/jsp/about.jsp" flush="true"/>

</body>
</html>




