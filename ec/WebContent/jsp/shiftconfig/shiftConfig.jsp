<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<title>用户管理</title>
<script src="src/init.js"></script>
<script src="src/xuaLib.js"></script>
<script src="src/util.js"></script>
<script src="src/vmCtrlBar.js"></script>
<script src="src/vmCtxMenu.js"></script>
<script src="src/sxMonitor.js"></script>
<link rel="stylesheet" href="css/usage.css" type="text/css" />
<link rel="stylesheet" href="css/page.css" type="text/css" />
</head>

<body id="bodyObj">

	<div class="lst">
		<form name="form1" method="post" action="shiftConfigAdd">
			分组：<input name="shiftGroup" type="text" class="text" />姓名：<input name="name" type="text" class="text" />电话号码：<input name="phoneNumber" type="text" class="text" /> <input type="submit" value="添加">
		</form>
	</div>


	<div class="lst">
		<form name="form1" method="post" action="shiftConfigUpdate">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr class="hdr">
					<td class="user">id</td>
					<td class="user">分组</td>
					<td class="user">姓名</td>
					<td class="user">电话</td>
					<td class="user">星期日</td>
					<td class="user">星期一</td>
					<td class="user">星期二</td>
					<td class="user">星期三</td>
					<td class="user">星期四</td>
					<td class="user">星期五</td>
					<td class="user">星期六</td>
					<td class="user">删除</td>
				</tr>

				<s:iterator value="shiftConfigList">
					<tr class="cdr">
						<td class="user"><s:property value="id" /></td>
						<td class="user"><s:property value="shiftGroup" /></td>
						<td class="user"><s:property value="name" /></td>
						<td class="user"><s:property value="phoneNumber" /></td>
						<td class="user"><input type="checkbox" name="listCheck" value="<s:property value="id" />_sun" <s:if test="sun==true">checked</s:if>><br></td>
						<td class="user"><input type="checkbox" name="listCheck" value="<s:property value="id" />_mon" <s:if test="mon==true">checked</s:if>><br></td>
						<td class="user"><input type="checkbox" name="listCheck" value="<s:property value="id" />_tue" <s:if test="tue==true">checked</s:if>><br></td>
						<td class="user"><input type="checkbox" name="listCheck" value="<s:property value="id" />_wed" <s:if test="wed==true">checked</s:if>><br></td>
						<td class="user"><input type="checkbox" name="listCheck" value="<s:property value="id" />_thu" <s:if test="thu==true">checked</s:if>><br></td>
						<td class="user"><input type="checkbox" name="listCheck" value="<s:property value="id" />_fri" <s:if test="fri==true">checked</s:if>><br></td>
						<td class="user"><input type="checkbox" name="listCheck" value="<s:property value="id" />_sat" <s:if test="sat==true">checked</s:if>><br></td>
						<td class="user"><a href="/ec/shiftConfigDelete?id=<s:property value="id" />">删除</a></td>
					</tr>
				</s:iterator>
			</table>
			<input type="submit" value="保存">
		</form>
	</div>


</body>
</html>




