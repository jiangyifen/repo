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
	<form name="voicemail" action="getVoicemail.action">
	    <div id="std">
	        <table border="0" cellpadding="0" cellspacing="0" style="border-top: 0px solid #E6E6E6; margin-top: 24px">
	            <tr>
	                <td width="100%">
	                开始日期:&nbsp;&nbsp;&nbsp;&nbsp;<input name="beginTime" value="<s:property value="beginTime"/>" type="text" onclick="WdatePicker({skin:'blue'})" class="text"/>&nbsp;&nbsp;00:00:00&nbsp;&nbsp;&nbsp;&nbsp;
	                结束日期:&nbsp;&nbsp;&nbsp;&nbsp;<input name="endTime" value="<s:property value="endTime"/>" type="text" onclick="WdatePicker({skin:'blue'})" class="text"/>&nbsp;&nbsp;23:59:59&nbsp;&nbsp;&nbsp;&nbsp;
	                主叫号码:&nbsp;&nbsp;&nbsp;&nbsp;<input name="src" value="<s:property value="src"/>" type="text" class="text"/>&nbsp;&nbsp;&nbsp;&nbsp;
	                语音信箱:&nbsp;&nbsp;&nbsp;&nbsp;<input name="mailbox" value="<s:property value="mailbox"/>" type="text" class="text"/>&nbsp;&nbsp;&nbsp;&nbsp;
	                </td>               
	                <td class="btnColLft">
	                    <div class="btn" onclick="document.voicemail.submit()">
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
		    <table border="0" cellpadding="0" cellspacing="0" id="list">
		        <tr class="hdr">
	                <td nowrap>编号</td>
	                <td nowrap>留言时间</td>
	                <td nowrap>语音信箱</td>
	                <td nowrap>主叫号码</td>
	                <td nowrap>时长</td>
			        <td nowrap>文件名</td>
		        </tr> 
			    <s:iterator value="vm">
				    <tr class="cdr"  title="<s:property value="vmUrl"/>" >
				        <td class="dn" nowrap><s:property value="id"/></td>
	                    <td class="dn" nowrap><s:property value="origdate"/></td>
	                    <td class="dn" nowrap><s:property value="origmailbox"/></td>
	                    <td class="dn" nowrap><s:property value="callerid"/></td>
	                    <td class="dn" nowrap><s:property value="duration"/></td>
	                    <td class="dn" nowrap><s:property value="vmUrl"/></td>
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
		
		共<s:property value="vmCount"/>条结果&nbsp;&nbsp;&nbsp;&nbsp;第<s:property value="pageIndex"/>页，共 <s:property value="maxPageIndex"/>页&nbsp;&nbsp;&nbsp;&nbsp;跳转到第<s:select name="pageIndex" list="pages" onChange="document.cdr.submit()"></s:select>页
		</div>
	
	</form>

<br />
	
<jsp:include page="/jsp/about.jsp" flush="true"/>

</body> 
</html> 




