<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
<title>operational data</title>

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
    var excel = document.form1.excel.checked;
    if(excel){
        document.form1.submit();
    }

}
</script>

</head>
<body>
<form name="form1" action="getChartOperationalData">

    <div id="sxStatHdr" class="sctHdr">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="100%">operational data</td>
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
                <td colspan="2"><div>导出Excel<input type="checkbox" value="true" name="excel" checked/></div></td>
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

</body>
</html>
