<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<title>外呼任务</title>
<script src="src/init.js"></script>
<script src="src/xuaLib.js"></script>
<script src="src/util.js"></script>
<script src="src/vmCtrlBar.js"></script>
<script src="src/vmCtxMenu.js"></script>
<script src="src/sxMonitor.js"></script>
<link rel="stylesheet" href="css/usage.css" type="text/css" />
<link rel="stylesheet" href="css/page.css" type="text/css" />
<script language="javascript">
	function popWindow(page) {
		window.open(page, "", "scrollbars=yes,width=660,height=450,resizable");
	}
</script>
</head>

<body id="bodyObj">


<form method="POST" action="dialTaskAdd.action" name="add">

<div id="sxStatHdr" class="sctHdr">
<table border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="100%">添加外呼任务</td>
	</tr>
</table>
</div>


<div class="cfg">
<table border="0" cellpadding="0" cellspacing="0">
	<tr class="cat">
		<td colspan="2">任&nbsp;务&nbsp;名:</td>
		<td><input name="taskName" type="text" class="text" /></td>
	</tr> 
    <tr class="cat">
		<td colspan="2">呼叫比例:</td>
		<td><input name="rate" type="text" class="text"/></td>
    </tr>
	<tr class="cat">
		<td colspan="2">优&nbsp;先&nbsp;级:</td>
		<td>
			<select name="perority">
			<option value="0">优先</option>
			<option value="1">普通</option>
			</select>
		</td>
	</tr>

	<tr class="cat">
		<td colspan="2">队&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;列:</td>
		<td class="user"><s:select theme="simple" list="queues"
			name="queueName" listKey="name" listValue="description" /></td>
	</tr>

</table>
</div>


<div id="std">
<table border="0" cellpadding="0" cellspacing="0"
	style="border-top: 0px solid #E6E6E6; margin-top: 24px">
	<tr>
		<td width="100%">&nbsp;</td>

		<td class="btnColLft">
		<div class="btn" onclick="document.forms[0].submit();">
		<table>
			<tr>
				<td class="lbl" nowrap>保存</td>
			</tr>
		</table>
		</div>
		</td>

		<td class="btnCol">
		<div class="btn" onclick="document.forms[0].reset()">
		<table>
			<tr>
				<td class="lbl" nowrap>重置</td>
			</tr>
		</table>
		</div>
		</td>

	</tr>
</table>
</div>
</form>

<div class="lst">
<form name="delete" method="post" action="dialTaskDelete.action">
<table border="0" cellpadding="0" cellspacing="0" id="userList">
	<tr class="hdr">
		<td class="left">任务名</td>
		<!--<td class="left">今日已派单</td>-->
		<td class="left">呼叫比例</td>
		<td class="left">优先级</td>
		<td class="left">队列名</td>

		<td class="left">意向数</td>
		<td class="left">完成数</td>
		<td class="left">进行数</td>
		<td class="left">未分配数</td>
		<td class="left">总数</td>

		<td class="left">成功率（意向客户数/完成数）</td>
		<td class="left">自动外呼状态
		<!--<td class="left">自动派单状态</td> -->
		<td class="left">派单</td>
		<td class="left">派单回收</td>
		<td class="left">短信群发</td>
		<td class="left">选择</td>
	</tr>
	<s:iterator value="dialTasks" status="st">
		<tr class="vms">
			<td class="user"><a
				href="dialTaskGetOne.action?id=<s:property value="id"/>"><s:property
				value="taskName" /></a></td>
			<!--<td class="user" ><s:property value="hasAssignedToday"/></td>-->
			<td class="user" ><s:property value="rate"/></td> 
			<td class="user">
			<%
				if (request.getAttribute("perority") != null
							&& request.getAttribute("perority").toString()
									.equals("0")) {
			%>优先<%
				} else if (request.getAttribute("perority") != null
							&& request.getAttribute("perority").toString()
									.equals("1")) {
			%>普通<%
				} else {
			%> <s:property value="perority" /> <%
 				}
 			%>
			</td>
			<td class="user"><s:property value="queueDescription" /></td>

			<td class="user"><s:property value="myCustomerCount" /></td>
			<td class="user"><s:property value="finishedDialTaskItemCount" /></td>
			<td class="user"><s:property value="manualDialTaskItemCount" /></td>
			<td class="user"><s:property value="readyDialTaskItemCount" /></td>
			<td class="user"><s:property value="dialTaskItemCount" /></td>

			<td class="user"><s:property value="customerRate" />%</td>
			 
			<td class="user" ><a href="dialTaskStatusChange.action?id=<s:property value="id"/>&currentstatus=<s:property value="status"/>"><s:if test="status=='RUNNING'">已开启</s:if><s:else>已关闭</s:else></a></td> 
			<!--
			<td class="user"><a	href="dialTaskAutoAssignChange.action?pageIndex=<s:property value="pageIndex"/>&id=<s:property value="id"/>&currentautoassign=<s:property value="autoAssign"/>"><s:if test="autoAssign==true">已开启</s:if><s:else>已关闭</s:else></a></td>
			-->
			<td class="user"><a
				href="crmDialTaskAssign?dialTaskId=<s:property value="id"/>&pageIndex=<s:property value="pageIndex"/>">执行派单</a></td>
			<td class="user"><a
				href="crmDialTaskRetrieve?dialTaskId=<s:property value="id"/>&pageIndex=<s:property value="pageIndex"/>">执行回收</a></td>
			<td class="user"><a href="javascript:"
				onClick="popWindow('/ec/dialTaskSmSend?send=false&dialTaskId=<s:property value="id"/>&dialTaskName=<s:property value="taskName"/>')">短信群发</a></td>
			<td class="user"><input type="checkbox" name="u"
				value="<s:property value="id"/>" /></td>
		</tr>
	</s:iterator>
</table>
</form>
</div>


<s:url id="url_pre" value="dialTasksGet.action">
	<s:param name="pageIndex" value="pageIndex-1"></s:param>
</s:url>
<s:a href="%{url_pre}">上一页</s:a>

<s:iterator value="pages" status="status">
	<a href="dialTasksGet.action?pageIndex=<s:property/>"><s:property /></a>
</s:iterator>

<s:url id="url_next" value="dialTasksGet.action">
	<s:param name="pageIndex" value="pageIndex+1"></s:param>
</s:url>
<s:a href="%{url_next}">下一页</s:a>

<div id="std">
<table border="0" cellpadding="0" cellspacing="0"
	style="border-top: 0px solid #E6E6E6; margin-top: 24px">
	<tr>
		<td width="100%">&nbsp;</td>

		<td class="btnCol">
		<div class="btn" onclick="location.href='dialTasksGet'">
		<table>
			<tr>
				<td class="lbl" nowrap>刷新</td>
			</tr>
		</table>
		</div>
		</td>

		<td class="btnCol">
		<div class="btn" onclick="document.forms[1].submit()">
		<table>
			<tr>
				<td class="lbl" nowrap>删除</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>
</div>


<jsp:include page="/jsp/about.jsp" flush="true"/>
<p>
<p>
<p>
<p>
<p>
<p>
<p>
</body>
</html>




