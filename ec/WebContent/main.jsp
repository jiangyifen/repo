<%@page language="java" contentType="text/html; charset=utf-8"%>
<html>
<head>

<link rel="stylesheet" href="css/page.css" type="text/css" />
<link rel="stylesheet" href="css/tabs.css" type="text/css" />
<script src="src/menu.js"></script>

<script language="javascript">
function popWindow(page) {
    window.open(page, "", "width=560,height=650,resizable");
}
</script>

</head>

<body id="bodyObj" onload="setIframeTop()">

<table border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="right" class="appIcon"><img src="imx/logo_16x16.png" width="16" height="16"></td>
    <td nowrap class="header"><span id="prod">EC 呼叫中心管理系统</span></td>
  </tr>
</table>

<table id="menu" class="Tabs" width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
	<td id="monitor" class="TabSlct" valign="bottom" nowrap onclick="jump('iframe_monitor',this)"><div>通话状态</div></td>
    <td id="sipstatus" class="Tab" valign="bottom" nowrap onclick="jump('iframe_sipstatus',this)"><div>分机状态</div></td>
    <td id="allqueuestatus" class="Tab" valign="bottom" nowrap onclick="jump('iframe_allqueuestatus',this)"><div>队列概览</div></td>
    <td id="queuestatus" class="Tab" valign="bottom" nowrap onclick="jump('iframe_queuestatus',this)"><div>队列状态</div></td>
	<td id="dialtasks" class="Tab" valign="bottom" nowrap onclick="jump('iframe_dialtasks',this)"><div>外呼任务状态</div></td>
	<td id="batchsmtaskstatus" class="Tab" valign="bottom" nowrap onclick="jump('iframe_batchsmtaskstatus',this)"><div>短信群发任务状态</div></td>
	<td id="sms" class="Tab" valign="bottom" nowrap  onclick="jump('iframe_sms',this)"><div>短信记录</div></td>
	<td id="cdrs" class="Tab" valign="bottom" nowrap  onclick="jump('iframe_cdrs',this)"><div>呼叫记录</div></td>
	<td id="notice" class="Tab" valign="bottom" nowrap  onclick="jump('iframe_notice',this)"><div>公告</div></td>
	<td id="voicemail" class="Tab" valign="bottom" nowrap  onclick="jump('iframe_voicemail',this)"><div>语音留言</div></td>
	<td id="blacklist" class="Tab" valign="bottom" nowrap  onclick="jump('iframe_blacklist',this)"><div>黑名单</div></td>
	<td id="reports" class="Tab" valign="bottom" nowrap onclick="jump('iframe_reports',this)"><div>报表</div></td>
	<td id="options" class="Tab" valign="bottom" nowrap  onclick="jump('iframe_options',this)"><div>系统管理</div></td>
    
    <td width="100%" class="NoTabs" nowrap><div class="options"><a href="javascript:" onClick="popWindow('changePassword.action')">修改密码</a></div></td>
    <td width="100%" class="NoTabs" nowrap><div class="options"><a href="logout.action">注销</a></div></td>
  </tr>
</table>

<iframe id="iframe_monitor" name="monitor" src="systemMonitor.action" width="1600" height="50" scrolling="yes"  frameborder="0" style="position:absolute; top:53px; left:0px; width:100%; height:100%; z-index:10;"></iframe>
<iframe id="iframe_sipstatus" name="sipstatus" src="getSipStatus.action" width="1600" height="50" scrolling="yes"  frameborder="0" style="position:absolute; top:-999999px; left:0px; width:100%; height:100%; z-index:10;"></iframe>
<iframe id="iframe_allqueuestatus" name="queuestatus" src="getQueueStatus.action?allQueueStatus=true" width="1600" height="50" scrolling="yes" frameborder="0" style="position:absolute; top:-999999px; left:0px; width:100%; height:100%; z-index:10;"></iframe>
<iframe id="iframe_queuestatus" name="queuestatus" src="getQueueStatus.action" width="1600" height="50" scrolling="yes" frameborder="0" style="position:absolute; top:-999999px; left:0px; width:100%; height:100%; z-index:10;"></iframe>
<iframe id="iframe_dialtasks" name="dialtasks" src="dialTasksGet.action" width="1600" height="50" scrolling="yes"  frameborder="0" style="position:absolute; top:-999999px; left:0px; width:100%; height:100%; z-index:10;"></iframe>
<iframe id="iframe_batchsmtaskstatus" name="batchsmtaskstatus" src="batchSmTaskStatusGet.action" width="1600" height="50" scrolling="yes"  frameborder="0" style="position:absolute; top:-999999px; left:0px; width:100%; height:100%; z-index:10;"></iframe>
<iframe id="iframe_sms" name="sms" src="getSm.action?beginTime=${sessionScope.today}&endTime=${sessionScope.today}" width="1600" height="50" scrolling="yes" frameborder="0" style="position:absolute; top:-999999px; left:0px; width:100%; height:100%; z-index:10;"></iframe>
<iframe id="iframe_cdrs" name="cdrs" src="getCdr.action?beginTime=${sessionScope.today}&endTime=${sessionScope.today}" width="1600" height="50" scrolling="yes" frameborder="0" style="position:absolute; top:-999999px; left:0px; width:100%; height:100%; z-index:10;"></iframe>
<iframe id="iframe_notice" name="notice" src="noticeGet.action?beginTime=${sessionScope.oneMonthAgo}&endTime=${sessionScope.today}" width="1600" height="50" scrolling="yes" frameborder="0" style="position:absolute; top:-999999px; left:0px; width:100%; height:100%; z-index:10;"></iframe>
<iframe id="iframe_voicemail" name="voicemail" src="getVoicemail.action" width="1600" height="50" scrolling="yes"  frameborder="0" style="position:absolute; top:-999999px; left:0px; width:100%; height:100%; z-index:10;"></iframe>
<iframe id="iframe_blacklist" name="blacklist" src="blackListItemGet.action" width="1600" height="50" scrolling="yes"  frameborder="0" style="position:absolute; top:-999999px; left:0px; width:100%; height:100%; z-index:10;"></iframe>
<iframe id="iframe_reports" name="reports" src="jsp/chart/reports.jsp" width="1600" height="50" scrolling="yes" " frameborder="0" style="position:absolute; top:-999999px; left:0px; width:100%; height:100%; z-index:10;"></iframe>
<iframe id="iframe_options" name="options" src="options.jsp" width="1600" height="50" scrolling="yes"  frameborder="0" style="position:absolute; top:-999999px; left:0px; width:100%; height:100%; z-index:10;"></iframe>
</body>
</html>
