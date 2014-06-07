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
<title>工作效率分布</title>

<script language="javascript">
function validate() {
    if (!document.chartform.beginTime.value) {
        alert("请输入日期");
        document.chartform.beginTime.focus();
        return false;
    }
    if (!document.chartform.endTime.value) {
        alert("请输入日期");
        document.chartform.endTime.focus();
        return false;
    }

    var beginTime = document.chartform.beginTime.value;
    var endTime = document.chartform.endTime.value;
    var average = document.chartform.average.checked;
    var excel = document.chartform.excel.checked;
    if(excel){
        document.chartform.submit();
    }    
    document.chartTimes.src="getChartWorkEfficiencyDistribution.action?beginTime="+beginTime+"&endTime="+endTime+"&average="+average;
    document.chartTimelen.src="getChartWorkEfficiencyDistribution.action?beginTime="+beginTime+"&endTime="+endTime + "&timelen=true"+"&average="+average;
}
</script>

</head>
<body>
<form method="POST" action="getChartWorkEfficiencyDistribution.action"  name="chartform">

    <div id="sxStatHdr" class="sctHdr">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="100%">工作效率分布</td>
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
                <td colspan="2"><div>求日平均值:<input name="average"  type="checkbox" /></div></td>
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

<img name="chartTimes" src="getChartWorkEfficiencyDistribution.action?beginTime=${sessionScope.today}&endTime=${sessionScope.today}"/>
<img name="chartTimelen" src="getChartWorkEfficiencyDistribution.action?beginTime=${sessionScope.today}&endTime=${sessionScope.today}&timelen=true"/>


</body>
</html>
