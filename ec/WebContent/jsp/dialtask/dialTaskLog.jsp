<%@page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<script src="/ec/src/init.js"></script>
<script src="/ec/src/xuaLib.js"></script>
<script src="/ec/src/util.js"></script>
<script src="/ec/src/query.js"></script>
<script src="/ec/src/sxOptions.js"></script>
<script language="javascript" type="text/javascript"
	src="/ec/src/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" href="/ec/css/usage.css" type="text/css" />
<link rel="stylesheet" href="/ec/css/page.css" type="text/css" />
<title>自动外呼任务统计</title>

<script language="javascript">
function validate() {
    if (!document.form1.beginTime.value) {
        alert("请输入日期");
        document.form1.beginTime.focus();
        return false;
    }
    if (!document.form1.endTime.value) {
        alert("请输入日期");
        document.form1.endTime.focus();
        return false;
    }

	var beginTime = document.form1.beginTime.value;
	var endTime = document.form1.endTime.value;

	document.form1.submit();
}
</script>

</head>
<body>
<form name="form1" action="dialTaskLogsGet.action">

<div id="sxStatHdr" class="sctHdr">
<table border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="100%">自动外呼任务统计</td>
	</tr>
</table>
</div>


<div class="cfg">
<table border="0" cellpadding="0" cellspacing="0">
	<tr class="cat">
		<td colspan="2">
		<div>开始日期:&nbsp;&nbsp;&nbsp;&nbsp;<input name="beginTime" type="text" onclick="WdatePicker({skin:'blue'})" class="text" value="<s:property value="beginTime"/>"/>&nbsp;&nbsp;00:00:00</div>                        
		</td>
	</tr>

	<tr class="cat">
		<td colspan="2">
		<div>结束日期:&nbsp;&nbsp;&nbsp;&nbsp;<input name="endTime"	type="text" onclick="WdatePicker( {skin : 'blue'})" class="text" value="<s:property value="endTime"/>" />&nbsp;&nbsp;23:59:59</div>
		</td>
	</tr>

	<tr class="cat">
		<td colspan="2">
		<div>任&nbsp;&nbsp;务&nbsp;&nbsp;名:&nbsp;&nbsp;&nbsp;<input name="dialTaskName" type="text" class="text"  value="<s:property value="dialTaskName"/>"/></div>
		</td>
	</tr>
</table>
</div>


<div id="std">
<table border="0" cellpadding="0" cellspacing="0"
	style="border-top: 0px solid #E6E6E6; margin-top: 24px">
	<tr>
		<td width="100%">&nbsp;</td>
		<td class="btnColLft">
		<div class="btn" onclick="document.form1.submit();">
		<table>
			<tr>
				<td class="lbl" nowrap>查询</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>
</div>


<div class="lst">
<table border="0" cellpadding="0" cellspacing="0">
	<tr class="hdr">
		<td>ID</td>
		<td>任务名</td>
		<td>意向客户数</td>
		<td>完成数</td>
		<td>总数</td>
		<td>完成率</td>
		<td>成功率</td>
		<td>最后更新日期</td>
	</tr>

	<s:iterator value="dialTaskLogs">
		<tr class="cdr">
			<td><s:property value="dialTaskId" /></td>
			<td><s:property value="dialTaskName" /></td>
			<td><s:property value="customerCount" /></td>
			<td><s:property value="dialTaskItemFinishedCount" /></td>
			<td><s:property value="dialTaskItemTotalCount" /></td>
			<td><s:property value="finishRate" />%</td>
			<td><s:property value="customerRate" />%</td>
			<td><s:property value="date" /></td>
		</tr>
	</s:iterator>
</table>
</div>


<div class="std" align="right">共<s:property value="cdrCount" />条结果&nbsp;&nbsp;&nbsp;&nbsp;第<s:property
	value="pageIndex" />页，共 <s:property value="maxPageIndex" />页&nbsp;&nbsp;&nbsp;&nbsp;跳转到第<s:select
	name="pageIndex" list="pages" onChange="document.form1.submit();"></s:select>页
</div>

</form>
</body>
</html>
