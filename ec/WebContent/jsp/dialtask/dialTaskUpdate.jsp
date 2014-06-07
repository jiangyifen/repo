<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html> 
<head> 
<title>修改外呼任务</title>
<script src="src/init.js"></script>
<script src="src/xuaLib.js"></script>
<script src="src/util.js"></script>
<script src="src/vmCtrlBar.js"></script>
<script src="src/vmCtxMenu.js"></script>
<script src="src/sxMonitor.js"></script>
<link rel="stylesheet" href="css/usage.css" type="text/css" />
<link rel="stylesheet" href="css/page.css" type="text/css" />
<script language="javascript">

function test(){
    var rname = document.getElementById("rname");
    var slct = document.getElementById("queueName");
    slct.value = rname.value;
}

</script>
</head> 
 
<body id="bodyObj" onload="test();"> 

<s:iterator value="dialTasks">
<form method="POST" action="dialTaskUpdate.action"  name="dialTaskUpdate">

    <div id="sxStatHdr" class="sctHdr">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="100%">修改外呼任务</td>
            </tr>
        </table>
    </div>
    

    <div class="cfg">
    <input name="id" type="hidden" value="<s:property value="id"/>"/>
        <table border="0" cellpadding="0" cellspacing="0">
            <tr class="cat">
                <td colspan="2">任&nbsp;务&nbsp;名:</td>
                <td><input name="taskName" type="text" class="text" value="<s:property value="taskName"/>"/></td>
            </tr>
 
            <tr class="cat">
                <td colspan="2">呼叫比例:</td>
                <td><input name="rate" type="text" class="text" value="<s:property value="rate"/>"/></td>
            </tr>

            <tr class="cat">
                <td colspan="2">优&nbsp;先&nbsp;级:</td>
                <td>
                	<select name="perority">
                	
                    	<option value="0" <%if (request.getAttribute("perority")!=null && request.getAttribute("perority").toString().equals("0")){%>selected<%} %>>优先</option>
                        <option value="1" <%if (request.getAttribute("perority")!=null && request.getAttribute("perority").toString().equals("1")){%>selected<%} %>>普通</option>
					</select>
                </td>
            </tr> 
            <tr class="cat">
                <td colspan="2">队&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;列:</td>
                <td>
                <input id="rname" type="hidden" value="<s:property value="queueName"/>"/>
                <s:select theme="simple" list="queues" name="queueName" listKey="name" listValue="description"/>
                </td>
            </tr>
        </table>
    </div>

    
    <div id="std">
        <table border="0" cellpadding="0" cellspacing="0" style="border-top: 0px solid #E6E6E6; margin-top: 24px">
            <tr>
                <td width="100%">&nbsp;</td>
                
                
                <td class="btnColLft">
                    <div class="btn" onclick="history.back(1);">
                        <table>
                            <tr>
                                <td class="lbl" nowrap>后退</td>
                            </tr>
                        </table>
                    </div>
                </td>
                
                
                <td class="btnCol">
                    <div class="btn" onclick="document.forms[0].submit();">
                        <table>
                            <tr>
                                <td class="lbl" nowrap>保存</td>
                            </tr>
                        </table>
                    </div>
                </td>

            </tr>
        </table>
    </div>
</form>


<form method="POST" enctype="MULTIPART/FORM-DATA" action="dialTaskItemImport.action"  name="dialTaskItemImport">

    <div id="sxStatHdr" class="sctHdr">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="100%">电话号码导入</td>
            </tr>
        </table>
    </div>
    

    <div class="cfg">
        <input name="id" type="hidden" value="<s:property value="id"/>"/>
        <table border="0" cellpadding="0" cellspacing="0">
            
            <tr class="cat">
                <td colspan="2">选择要上传的文件:</td>
                <td><input type="file" name="upload" /><input type="submit" value="上传" /></td>
            </tr>
        </table>    
    </div>

     
</form>




<!--  -->


<div class="lst">
<form name="dialTaskItemDelete" method="post" action="dialTaskItemDelete.action">
	<input name="id" type="hidden" value="<s:property value="id"/>"/>
    <table border="0" cellpadding="0" cellspacing="0" id="userList">
        <tr class="hdr"> 
          <td class="left">id</td>
          <td class="name">姓名</td>
          <td class="left">电话号码</td>          
          <td class="left">省份</td>
          <td class="left">城市</td>
          <td class="left">地址</td>
          <td class="left">描述</td>          
          <td class="left">状态</td>
          <td class="left">选中删除</td> 
        </tr> 
        <s:iterator value="dialTaskItems">
	        <tr class="vms">
	            <td class="user"><s:property value="id"/></td>
	            <td class="user"><s:property value="name"/></td>
	            <td class="user"><s:property value="phoneNumber"/></td>
	            <td class="user"><s:property value="province"/></td>
	            <td class="user"><s:property value="city"/></td>
	            <td class="user"><s:property value="address"/></td>
	            <td class="user"><s:property value="info"/></td>
	            <td class="user"><s:property value="status"/></td>
	            <td class="user"><input type="checkbox" name="u" value="<s:property value="id"/>" /></td>
	        </tr>
        </s:iterator>
    </table>
    
    <div class="std" align="right">
		第<s:property value="pageIndex"/>页，共 <s:property value="maxPageIndex"/>页&nbsp;&nbsp;&nbsp;&nbsp;跳转到第<s:select name="pageIndex" list="pages" onChange="document.dialTaskItemDelete.submit()"></s:select>页
	</div>
		
</form>
</div>
</s:iterator>


		

<div id="std">
<table border="0" cellpadding="0" cellspacing="0" style="border-top: 0px solid #E6E6E6; margin-top: 24px">
    <tr>
        <td width="100%">&nbsp;</td>
        
        
        <td class="btnCol">
	        <div class="btn" onclick="document.forms[2].submit()">
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



<jsp:include page="/jsp/about.jsp" flush="true"/>

</body> 
</html> 




