<%@page language="java" contentType="text/html; charset=utf-8"%>
<html>
<head>
<script src="/ec/src/init.js"></script>
<script src="/ec/src/xuaLib.js"></script>
<script src="/ec/src/util.js"></script>
<script src="/ec/src/query.js"></script>
<script src="/ec/src/sxOptions.js"></script>
<link rel="stylesheet" href="/ec/css/usage.css" type="text/css" />
<link rel="stylesheet" href="/ec/css/page.css" type="text/css" />

<script language="javascript">
	function popWindow(page) {
		window.open(page, "",
				"width=560,height=650,resizable, scrollbars=yes,location=no");
	}
</script>

</head>

<body id="bodyObj">

<div class="cfg">
<table border="0" cellpadding="0" cellspacing="0">
	<tr class="hdr">
		<td colspan="3">报表</td>
	</tr>

	<tr valign="top">

		<td width="50%" class="lcol">

		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartOutgoingWorkload.jsp')">座席呼出工作量统计</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>统计指定日期内各座席或各部门的呼出次数和时长</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>

		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartWorkloadTimeline.jsp')">工作量时间线</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>统计指定日期内工作量的变化</div>
				</td>
			</tr>
		</table>
		</td>
	</tr>

	<tr valign="top">
		<td width="50%" class="lcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartQueueIncomingWorkload.jsp')">座席呼入工作量统计</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>统计指定日期内各座席或各部门的接听次数和时长</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>

		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartWorkEfficiencyDistribution.jsp')">座席工作效率分布</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>统计“时长小于1分钟”、“间于1至3分钟”和“大于3分钟”的呼叫次数、呼叫时长以及其分布。</div>
				</td>
			</tr>
		</table>
		</td>
	</tr>

	<tr valign="top">
		<td width="50%" class="lcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartIncomingWorkload.jsp')">外线呼入量统计</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>统计指定日期内各外线的呼入次数和时长</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>

		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartMyCustomer.jsp')">意向客户统计</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>统计个人开发的意向客户数量</div>
				</td>
			</tr>
		</table>
		</td>
	</tr>

	<tr valign="top">
		<td width="50%" class="lcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartWorkload.jsp')">综合工作量统计</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>呼入呼出综合工作量统计</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>


		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/dialTaskLogsGet')">自动外呼任务成功率</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>统计每批自动外呼号码的成功率</div>
				</td>
			</tr>
		</table>
		</td>

	</tr>



	<tr valign="top">
		<td width="50%" class="lcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartWorkloadAverageTimeline.jsp')">人均工作量时间线</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>按日期统计人均工作量</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>


		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartMyCustomerTimeline.jsp')">意向客户时间线</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>按日期统计意向客户数量</div>
				</td>
			</tr>
		</table>
		</td>
	</tr>

	<tr valign="top">
		<td width="50%" class="lcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartWeightedWorkload.jsp')">加权工作量</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>按呼叫时长和呼叫次数加权计算出的工作量</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>


		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartAbandon.jsp')">队列放弃次数</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>按日期统计每个队列的放弃次数</div>
				</td>
			</tr>
		</table>
		</td>
	</tr>

	<tr valign="top">
		<td width="50%" class="lcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartWait.jsp')">队列等待时长</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>按日期统计每个队列的等待时长</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>

		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartQueueEntryTimes.jsp')">队列进入次数</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>按日期统计每个队列的进入次数</div>
				</td>
			</tr>
		</table>
		</td>


	</tr>

	<tr valign="top">
		<td width="50%" class="lcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartQueueWaitAndEntryTimes.jsp')">队列进入次数和等待时长混合图</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>按日期统计每个队列的进入次数等待时长</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>

		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartQueueWaitDistribution.jsp')">队列等待时长分布</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>按时段统计队列等待时长的分布情况</div>
				</td>
			</tr>
		</table>
		</td>

	</tr>


	<tr valign="top">
		<td width="50%" class="lcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartSMSendQuantity.jsp')">短信发送量统计</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>按日期统计短信发送量</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>
 
		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartAgentAssessment.jsp')">座席人员考核报表</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>按日期统计座席人员的在线时长、置忙市场、呼入呼出时长</div>
				</td>
			</tr>
		</table>
		</td>

	</tr>
	
	
	
	<tr valign="top">
		<td width="50%" class="lcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartAgentMinLoginTimeAndMaxLogoutTime.jsp')">座席人员登陆登出时间</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>按日期统计每个座席人员最早登陆时间和最终登出时间</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>
 
		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartCustomerSatisfactionInvestigation.jsp')">客户满意度调查</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>按日期统计客户满意度调查数据</div>
				</td>
			</tr>
		</table>
		</td>

	</tr>

	<tr valign="top">
		<td width="50%" class="lcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartManDay.jsp')">人天数统计</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>按日期统计每天耗费的人工</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>
  
		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartAgentLoginLogoutDetail.jsp')">座席登入登出详单</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>按日期统计座席登入登出详单</div>
				</td>
			</tr>
		</table>
		</td>

	</tr>
	
	<tr valign="top">
		<td width="50%" class="lcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartAgentPauseDetail.jsp')">座席置忙置闲详单</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>按日期统计座席置忙置闲详单</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>
   
		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartQueueEntryAndAbandonDistinct.jsp')">队列话务报表</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>按日期统计各队列的进入和放弃量</div>
				</td>
			</tr>
		</table>
		</td>

	</tr>

	<tr valign="top">
		<td width="50%" class="lcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartOverAllReport.jsp')">整体话务报表</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>统计整体接听情况</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>
   
		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartCSRSatisfactionReport.jsp')">CSR个人工作表现</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>统计个人的接听和评分情况</div>
				</td>
			</tr>
		</table>
		</td>

	</tr>
	
	<tr valign="top">
		<td width="50%" class="lcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartQueueAbandonDetail.jsp')">队列放弃详单</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>队列放弃详单</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>
  
		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartConcurrent.jsp')">总体并发量</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>系统总并发量，包括外线和分机</div>
				</td>
			</tr>
		</table>
		</td>
 
	</tr>



	<tr valign="top">
		<td width="50%" class="lcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartIVRNodeQuit.jsp')">IVR节点退出统计</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>IVR节点退出统计</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>
  
<!-- 		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/chartConcurrent.jsp')">总体并发量</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>系统总并发量，包括外线和分机</div>
				</td>
			</tr>
		</table>
		</td> -->
 
	</tr>
	
<!-- ganso -->
	<tr valign="top">
		<td width="50%" class="lcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/faqCount.jsp')">FAQ统计</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>FAQ统计</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>
  
		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/faqDetail.jsp')">FAQ详单</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>FAQ详单</div>
				</td>
			</tr>
		</table>
		</td>
 
	</tr>
	
	
	<tr valign="top">
		<td width="50%" class="lcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/fenduanHour.jsp')">座席分段报表（小时）</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>座席分段报表（小时）</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>
  
		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/fenduanDay.jsp')">座席分段报表（天）</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>座席分段报表（天）</div>
				</td>
			</tr>
		</table>
		</td>
 
	</tr>	
	
	
	<tr valign="top">
		<td width="50%" class="lcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/fenduanMonth.jsp')">座席分段报表（月）</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>座席分段报表（月）</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>

		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/operational_data.jsp')">Operational Data</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>Operational Data</div>
				</td>
			</tr>
		</table>
		</td>
 
	</tr>	
	
	
	<tr valign="top">
		<td width="50%" class="lcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/30s_detail.jsp')">30 seconds detail</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>30 seconds detail</div>
				</td>
			</tr>
		</table>
		</td>

		<td class="vr"><img src="/ec/imx/spacer.gif"></td>
 <!-- 
		<td width="50%" class="rcol">
		<table id="remDev" border="0" cellpadding="0" cellspacing="0">
			<tr id="devRow" class="cat">
				<td width="1%" valign="bottom" nowrap><img
					src="/ec/imx/diskFile-16x16.png" /></td>
				<td nowrap>
				<div><a href="javascript:"
					onClick="popWindow('/ec/jsp/chart/fenduanDay.jsp')">座席分段报表（天）</a></div>
				</td>
			</tr>
			<tr id="keyRow">
				<td colspan="2" class="key">
				<div>座席分段报表（天）</div>
				</td>
			</tr>
		</table>
		</td>
 
	</tr>	
<!-- ganso -->	
		
</table>
</div>

<br />

<jsp:include page="/jsp/about.jsp" flush="true"/>

<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>

</body>
</html>
