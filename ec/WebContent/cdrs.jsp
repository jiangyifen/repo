<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html> 
<head> 
<title>呼叫详单</title>
<script src="src/init.js"></script>
<script src="src/xuaLib.js"></script>
<script src="src/util.js"></script>
<script src="src/vmCtrlBar.js"></script>
<script src="src/vmCtxMenu.js"></script>
<script src="src/sxMonitor.js"></script>
<script language="javascript" type="text/javascript" src="src/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" href="css/usage.css" type="text/css" />
<link rel="stylesheet" href="css/page.css" type="text/css" />
<link rel="stylesheet" href="css/calendar.css" type="text/css" />
<script language="javascript">
function setUrl(url){
	document.player.URL = url;
}
</script>
</head> 
 
<body>
	<form name="cdr" action="getCdr.action">
	    <div id="std">
	        <table border="0" cellpadding="0" cellspacing="0" style="border-top: 0px solid #E6E6E6; margin-top: 24px">
                <tr>
                    <td width="100%">
						开始日期:&nbsp;&nbsp;&nbsp;&nbsp;<input name="beginTime" value="<s:property value="beginTime"/>" type="text" onclick="WdatePicker({skin:'blue'})" class="text"/>&nbsp;&nbsp;00:00:00&nbsp;&nbsp;&nbsp;&nbsp;
						结束日期:&nbsp;&nbsp;&nbsp;&nbsp;<input name="endTime" value="<s:property value="endTime"/>" type="text" onclick="WdatePicker({skin:'blue'})" class="text"/>&nbsp;&nbsp;23:59:59&nbsp;&nbsp;&nbsp;&nbsp;      
						主叫号码:&nbsp;&nbsp;&nbsp;&nbsp;<input name="src" value="<s:property value="src"/>" type="text" class="text"/>&nbsp;&nbsp;&nbsp;&nbsp;
						被叫号码:&nbsp;&nbsp;&nbsp;&nbsp;<input name="dst" value="<s:property value="dst"/>" type="text" class="text"/>&nbsp;&nbsp;&nbsp;&nbsp;
						分机号码:&nbsp;&nbsp;&nbsp;&nbsp;<input name="fenji" value="<s:property value="fenji"/>" type="text" class="text"/>&nbsp;&nbsp;&nbsp;&nbsp;
						工号:&nbsp;&nbsp;&nbsp;&nbsp;<input name="hid" value="<s:property value="hid"/>" type="text" class="text"/>&nbsp;&nbsp;&nbsp;&nbsp;            
						           
                    </td>            
                </tr>
                <tr>
                    <td width="100%">
				                    呼叫时长大等于:&nbsp;&nbsp;&nbsp;&nbsp;<input name="moreThen" value="<s:property value="moreThen"/>" type="text" class="text"/>&nbsp;&nbsp;&nbsp;&nbsp;
                                    <select name="moreThenUnit">
                                        <option value="1" <%if (request.getAttribute("moreThenUnit")!=null && request.getAttribute("moreThenUnit").equals("1")){%>selected<%} %>>秒</option>
                                        <option value="60" <%if (request.getAttribute("moreThenUnit")!=null && request.getAttribute("moreThenUnit").equals("60")){%>selected<%} %>>分钟</option>
                                        <option value="3600" <%if (request.getAttribute("moreThenUnit")!=null && request.getAttribute("moreThenUnit").equals("3600")){%>selected<%} %>>小时</option>
                                    </select>&nbsp;&nbsp;&nbsp;&nbsp;            
                        <select name="andor">
                            <option value="and">AND</option>
                            <option value="or">OR</option>
                        </select>
                                                                呼叫时长小等于:&nbsp;&nbsp;&nbsp;&nbsp;<input name="lessThen" value="<s:property value="lessThen"/>" type="text" class="text"/>&nbsp;&nbsp;&nbsp;&nbsp;
                                    <select name="lessThenUnit">
                                        <option value="1" <%if (request.getAttribute("lessThenUnit")!=null && request.getAttribute("lessThenUnit").equals("1")){%>selected<%} %>>秒</option>
                                        <option value="60" <%if (request.getAttribute("lessThenUnit")!=null && request.getAttribute("lessThenUnit").equals("60")){%>selected<%} %>>分钟</option>
                                        <option value="3600" <%if (request.getAttribute("lessThenUnit")!=null && request.getAttribute("lessThenUnit").equals("3600")){%>selected<%} %>>小时</option>
                                    </select>&nbsp;&nbsp;&nbsp;&nbsp;                
				                    呼叫方向:&nbsp;&nbsp;&nbsp;&nbsp;<select name="direction">
                                                            <option value="both" <%if (request.getAttribute("direction")!=null && request.getAttribute("direction").equals("both")){%>selected<%} %>>全部</option>
                                                            <option value="incoming" <%if (request.getAttribute("direction")!=null && request.getAttribute("direction").equals("incoming")){%>selected<%} %>>呼入</option>
                                                            <option value="outgoing" <%if (request.getAttribute("direction")!=null && request.getAttribute("direction").equals("outgoing")){%>selected<%} %>>呼出</option>
                                                        </select>
				                    是否接通:&nbsp;&nbsp;&nbsp;&nbsp;<select name="disposition">
				                    <option value="ALL" <%if (request.getAttribute("disposition")!=null && request.getAttribute("disposition").equals("ALL")){%>selected<%} %>>全部</option>
				                    <option value="ANSWER" <%if (request.getAttribute("disposition")!=null && request.getAttribute("disposition").equals("ANSWER")){%>selected<%} %>>接通</option>
				                    <option value="NO ANSWER" <%if (request.getAttribute("disposition")!=null && request.getAttribute("disposition").equals("NO ANSWER")){%>selected<%} %>>未接通</option>

				                    </select>&nbsp;&nbsp;&nbsp;&nbsp;
				                    <!-- 工号:&nbsp;&nbsp;&nbsp;&nbsp;<input name="hid" value="<s:property value="hid"/>" type="text" class="text"/>&nbsp;&nbsp;&nbsp;&nbsp;  -->
				                    导出Excel<input type="checkbox" value="true" name="excel"/></td>
                    <td class="btnColLft">
                        <div class="btn" onclick="document.cdr.submit()">
                            <table>
                                <tr>
                                    <td class="lbl" nowrap>提交</td>
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
					<td nowrap>开始时间</td>
					<td nowrap>主叫号码</td>
					<td nowrap>被叫号码</td>
					<td nowrap>分机号码</td>
					<td nowrap>呼叫方向</td>
					<td nowrap>呼叫时长</td>
					<td nowrap>接通时长</td>
					<td nowrap>是否接通</td>
					<td nowrap>座席工号</td>
					<td nowrap>座席姓名</td>
					<td nowrap>下载链接</td>
		        </tr> 
			    <s:iterator value="cdr">
				    <tr class="cdr"  title="<s:property value="recUrl"/>" >
						<td class="dn" nowrap><s:property value="calldate"/></td>
						<td class="dn" nowrap><s:property value="src"/></td>
						<td class="dn" nowrap><s:property value="dst"/></td>
						<td class="dn" nowrap><s:property value="fenji"/></td>
						<td class="dn" nowrap><s:property value="dcontext"/></td>
						<td class="dn" nowrap><s:property value="duration"/></td>
						<td class="dn" nowrap><s:property value="billsec"/></td>
						<td class="dn" nowrap><s:property value="disposition"/></td>
						<td class="dn" nowrap><s:property value="hid"/></td>
						<td class="dn" nowrap><s:property value="name"/></td>
						<td class="dn" nowrap><a href="<s:property value="recUrl"/>"><s:property value="recUrl"/></a></td>
				    </tr>
			    </s:iterator>
	        </table>   
		</div>
	
	    <OBJECT id="player" width="100%" height="45" CLASSID="CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6" type="application/x-oleobject">
	        <PARAM name="AutoStart" value="False">
	        <PARAM name="uiMode" value="Full">
	        <PARAM NAME="URL" VALUE="">
	        <param name="ShowDisplay" value="0">
	    </OBJECT>
    	
		<div class="std" align="right">
		共<s:property value="cdrCount"/>条结果&nbsp;&nbsp;&nbsp;&nbsp;第<s:property value="pageIndex"/>页，共 <s:property value="maxPageIndex"/>页&nbsp;&nbsp;&nbsp;&nbsp;跳转到第<s:select name="pageIndex" list="pages" onChange="document.cdr.submit()"></s:select>页
		</div>

	</form>

<br />


<jsp:include page="/jsp/about.jsp" flush="true"/>

</body> 
</html> 




