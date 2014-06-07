<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html> 
<head> 
<title>分机管理</title>
<script src="src/init.js"></script> 
<script src="src/xuaLib.js"></script> 
<script src="src/util.js"></script> 
<script src="src/vmCtrlBar.js"></script> 
<script src="src/vmCtxMenu.js"></script> 
<script src="src/sxMonitor.js"></script> 
<link rel="stylesheet" href="css/usage.css" type="text/css" /> 
<link rel="stylesheet" href="css/page.css" type="text/css" /> 
<script language="javascript">
function validate() {
    if (!document.sipadd.name.value) {
        alert("请输入分机号");
        document.sipadd.name.focus();
        return false;
    }
    if(isNaN(document.sipadd.name.value)){
        alert("分机号必须是数字");
        document.sipadd.name.focus();
        return false;
    }
    if (document.sipadd.secret.value != document.sipadd.secret2.value) {
        alert("两次密码输入不一致，请重新输入！");
        document.sipadd.password.focus();
        return false;
    }
    document.sipadd.submit();
    return true;
}
</script>
</head> 
 
<body id="bodyObj">


<div class="lst">

    <div id="sxStatHdr" class="sctHdr">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="100%">分机管理</td>
            </tr>
        </table>
    </div>
    
	<form name="delete" method="post" action="sipDelete.action">
	    <table border="0" cellpadding="0" cellspacing="0" id="userList">
	        <tr class="hdr"> 
	          <td class="left">分机</td>
	          <td class="left"></td> 
	        </tr> 
	        <s:iterator value="sips">
		        <tr class="vms">
		            <td class="user"><a href="sipGetOne.action?id=<s:property value="id"/>"><s:property value="name"/></a></td>
		            <td class="user"><input type="checkbox" name="id" value="<s:property value="id"/>" /></td>	            
		        </tr>
	        </s:iterator>
	    </table>
	</form>
</div>

     <s:url id="url_pre" value="sipsGet.action">
        <s:param name="pageIndex" value="pageIndex-1"></s:param>
     </s:url>
     
	<s:a href="%{url_pre}">上一页</s:a>

     <s:iterator value="pages" status="status">
        <a href="sipsGet.action?pageIndex=<s:property/>"><s:property/></a>
     </s:iterator>
         
     <s:url id="url_next" value="sipsGet.action">
         <s:param name="pageIndex" value="pageIndex+1"></s:param>
     </s:url>
     <s:a href="%{url_next}">下一页</s:a>

<div id="std">
<table border="0" cellpadding="0" cellspacing="0" style="border-top: 0px solid #E6E6E6; margin-top: 24px">
    <tr>
        <td width="100%">&nbsp;</td>
        
        <td class="btnCol">
            <div class="btn" onclick="window.location='sipAdd.jsp'">
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




