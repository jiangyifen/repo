<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html> 
<head> 
<title>呼叫队列管理</title>
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

    <div id="sxStatHdr" class="sctHdr">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="100%">呼叫队列管理</td>
            </tr>
        </table>
    </div>
    
<form name="delete" method="post" action="queueDelete.action">
    <table border="0" cellpadding="0" cellspacing="0" id="queueList">
        <tr class="hdr"> 
          <td class="left">队列名</td>
          <td class="left">队列描述</td>
          <td class="left">选中</td> 
        </tr> 
        <s:iterator value="queues">
            <tr class="vms">
                <td class="user"><a href="queueGetOne.action?queuename=<s:property value="name"/>"><s:property value="name"/></a></td>
                <td class="user"><s:property value="description"/></td>
                <td class="user"><input type="checkbox" name="q" value="<s:property value="name"/>" /></td>             
            </tr>
        </s:iterator>
    </table>
</form>
</div>

     <s:url id="url_pre" value="queuesGet.action">
        <s:param name="pageIndex" value="pageIndex-1"></s:param>
     </s:url>
     <s:a href="%{url_pre}">上一页</s:a>

     <s:iterator value="pages" status="status">
        <a href="queuesGet.action?pageIndex=<s:property/>"><s:property/></a>
     </s:iterator>
         
     <s:url id="url_next" value="queuesGet.action">
         <s:param name="pageIndex" value="pageIndex+1"></s:param>
     </s:url>
     <s:a href="%{url_next}">下一页</s:a>

	<div id="std">
	<table border="0" cellpadding="0" cellspacing="0" style="border-top: 0px solid #E6E6E6; margin-top: 24px">
	    <tr>
	        <td width="100%">&nbsp;</td>
	        
	        <td class="btnCol">
	            <div class="btn" onclick="window.location='queueAdd.jsp'">
	            <table>
	                <tr>
	                    <td class="lbl" nowrap>添加</td>
	                </tr>
	            </table>
	            </div>
	        </td>
	        
	        <td class="btnCol">
	            <div class="btn" onclick="document.forms[0].submit();">
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

</body> 
</html> 




