<%@page language="java" contentType="text/html; charset=utf-8"%>
<html>
<head>
<script src="/ec/src/init.js"></script>
<script src="/ec/src/xuaLib.js"></script>
<script src="/ec/src/util.js"></script>
<script src="/ec/src/query.js"></script>
<script src="/ec/src/sxOptions.js"></script>
<script language="javascript" type="text/javascript" src="/ec/src/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" href="/ec/css/usage.css" type="text/css" />
<link rel="stylesheet" href="/ec/css/page.css" type="text/css" />
<title>呼出工作量统计</title>

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
	var groupByName = document.form1.groupByName.checked;
	var groupByTeam = document.form1.groupByTeam.checked;
	//var average = document.form1.average.checked;
	var orderByWorkload = document.form1.orderByWorkload.checked;
	var excel = document.form1.excel.checked;
	if(excel){
		document.form1.submit();
	}
    document.chartOutgoingWorkloadTimes.src="getChartOutgoingWorkload.action?beginTime="+beginTime+"&endTime="+endTime+"&groupByName="+groupByName+"&groupByTeam="+groupByTeam+"&orderByWorkload="+orderByWorkload;
    //document.chartOutgoingWorkloadTimelen.src="getChartOutgoingWorkload.action?beginTime="+beginTime+"&endTime="+endTime+"&groupByName="+groupByName+"&groupByTeam="+groupByTeam+"&average="+average+"&orderByWorkload="+orderByWorkload + "&timelen";
    //document.chartOutgoingWorkloadEfficiency.src="getChartOutgoingWorkload.action?beginTime="+beginTime+"&endTime="+endTime+"&groupByName="+groupByName+"&groupByTeam="+groupByTeam+"&average="+average+"&orderByWorkload="+orderByWorkload + "&efficiency";
    //document.chartOutgoingWorkloadDistinctDstTimes.src="getChartOutgoingWorkloadDistinctDst.action?beginTime="+beginTime+"&endTime="+endTime+"&groupByName="+groupByName+"&groupByTeam="+groupByTeam+"&average="+average+"&orderByWorkload="+orderByWorkload;
    //document.chartOutgoingWorkloadDistinctDstTimelen.src="getChartOutgoingWorkloadDistinctDst.action?beginTime="+beginTime+"&endTime="+endTime+"&groupByName="+groupByName+"&groupByTeam="+groupByTeam+"&average="+average+"&orderByWorkload="+orderByWorkload + "&timelen";
    //document.chartOutgoingWorkloadDistinctDstEfficiency.src="getChartOutgoingWorkloadDistinctDst.action?beginTime="+beginTime+"&endTime="+endTime+"&groupByName="+groupByName+"&groupByTeam="+groupByTeam+"&average="+average+"&orderByWorkload="+orderByWorkload + "&efficiency";
}
</script>

</head>
<body>
<form name="form1" action="getChartOutgoingWorkload.action">

    <div id="sxStatHdr" class="sctHdr">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="100%">座席/团队工作量统计</td>
            </tr>
        </table>
    </div>
    
    
    <div class="cfg">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr class="cat">
                <td colspan="2"><div>开始日期:&nbsp;&nbsp;&nbsp;&nbsp;<input name="beginTime"  type="text" onclick="WdatePicker({skin:'blue'})" class="text" value="${sessionScope.today}"/>&nbsp;&nbsp;00:00:00</div></td>
            </tr>

            <tr class="cat">
                <td colspan="2"><div>结束日期:&nbsp;&nbsp;&nbsp;&nbsp;<input name="endTime"  type="text" onclick="WdatePicker({skin:'blue'})" class="text" value="${sessionScope.today}"/>&nbsp;&nbsp;23:59:59</div></td>
            </tr>

            <tr class="cat">
                <td colspan="2"><div>按姓名统计:<input name="groupByName"  type="checkbox"/></div></td>
            </tr>
            <tr class="cat">
                <td colspan="2"><div>按团队分组:<input name="groupByTeam"  type="checkbox"/></div></td>
            </tr>
            <!--  
            <tr class="cat">
                <td colspan="2"><div>求日平均值:<input name="average"  type="checkbox" /></div></td>
            </tr>
            -->
            <tr class="cat">
                <td colspan="2"><div>按工作量排序:<input name="orderByWorkload"  type="checkbox" /></div></td>
            </tr>
            <tr class="cat">
                <td colspan="2"><div>导出Excel<input type="checkbox" value="true" name="excel"/></div></td>
            </tr>            
            
        </table>
    </div>

    
    <div id="std">
        <table border="0" cellpadding="0" cellspacing="0" style="border-top: 0px solid #E6E6E6; margin-top: 24px">
            <tr>
                <td width="100%">&nbsp;</td>               
                <td class="btnColLft">
                    <div class="btn" onclick="validate();">
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
</form>
<img name="chartOutgoingWorkloadTimes" src="getChartOutgoingWorkload.action?beginTime=${sessionScope.today}&endTime=${sessionScope.today}"/>
<!--  
<img name="chartOutgoingWorkloadTimelen" src="getChartOutgoingWorkload.action?beginTime=${sessionScope.today}&endTime=${sessionScope.today}&timelen"/>
<img name="chartOutgoingWorkloadEfficiency" src="getChartOutgoingWorkload.action?beginTime=${sessionScope.today}&endTime=${sessionScope.today}&efficiency"/>
<img name="chartOutgoingWorkloadDistinctDstTimes" src="getChartOutgoingWorkloadDistinctDst.action?beginTime=${sessionScope.today}&endTime=${sessionScope.today}"/>
<img name="chartOutgoingWorkloadDistinctDstTimelen" src="getChartOutgoingWorkloadDistinctDst.action?beginTime=${sessionScope.today}&endTime=${sessionScope.today}&timelen"/>
<img name="chartOutgoingWorkloadDistinctDstEfficiency" src="getChartOutgoingWorkloadDistinctDst.action?beginTime=${sessionScope.today}&endTime=${sessionScope.today}&efficiency"/>
-->
</body>
</html>
