<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html> 
<head> 
<title>短信群发任务状态</title>
<script src="src/init.js"></script> 
<script src="src/xuaLib.js"></script> 
<script src="src/util.js"></script> 
<script src="src/vmCtrlBar.js"></script> 
<script src="src/vmCtxMenu.js"></script> 
<script src="src/sxMonitor.js"></script> 
<link rel="stylesheet" href="css/usage.css" type="text/css" /> 
<link rel="stylesheet" href="css/page.css" type="text/css" /> 
<script language="JavaScript"> 
	var pageIndex = 1;
	var path = "batchSmTaskStatusGet.action?pageIndex="+pageIndex;
	function changepath() {
		path = "batchSmTaskStatusGet.action?pageIndex="+document.getElementById("pageIndex").value;
		window.location=path;
	}
	
</script> 
</head> 
 
<body id="bodyObj"> 



<div class="lst">
<table border="0" cellpadding="0" cellspacing="0" id="list">
 
<tr class="hdr">
<td>短信群发任务批次</td>
<td>总数</td>
<td>等待发送数量</td>
<td>正在发送数量</td>
<td>成功发送数量</td>
<td>发送失败数量</td>
</tr>

<s:iterator value="batchSmTaskStatusList" status="st">
<tr class="cdr">
<td><s:property value="batchNumber"/></td>
<td><s:property value="totalCount"/></td>
<td><s:property value="readyCount"/></td>
<td><s:property value="waitingCount"/></td>
<td><s:property value="delivrdCount"/></td>
<td><s:property value="otherCount"/></td>
</tr>
</s:iterator>
</table>
</div>


		<div class="std" align="right">
		共<s:property value="total"/>条结果&nbsp;&nbsp;&nbsp;&nbsp;第<s:property value="pageIndex"/>页，共 <s:property value="maxPageIndex"/>页&nbsp;&nbsp;&nbsp;&nbsp;跳转到第<s:select name="pageIndex" list="pages" onChange="changepath();"></s:select>页
		</div>

<jsp:include page="/jsp/about.jsp" flush="true"/>

</body> 
</html> 




