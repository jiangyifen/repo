<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html> 
<head> 
<title>部门管理</title>
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
    if (!document.departmentadd.departmentname.value) {
        alert("请输入部门名");
        document.departmentadd.departmentname.focus();
        return false;
    }
    document.departmentadd.submit();
    return true;
}

</script>
</head> 
 
<body id="bodyObj"> 


<form method="POST" action="departmentAdd.action"  name="departmentadd">

    <div id="sxStatHdr" class="sctHdr">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="100%">添加部门</td>
            </tr>
        </table>
    </div>
    
    
    <div class="cfg">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr class="cat">
                <td colspan="2">部&nbsp;门&nbsp;名:</td>
                <td><input name="departmentname" type="text" class="text"/></td>
            </tr>

            <tr class="cat">
                <td colspan="2">描&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;述:</td>
                <td><input name="description" type="text" class="text"/></td>
            </tr>
            
            <tr class="cat">
                <td colspan="2">上级部门:</td>
                <td class="user">
                <s:select theme="simple" list="pdpmts" name="pdname" listKey="departmentname" listValue="description"/>
                </td>
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
                                <td class="lbl" nowrap>保存</td>
                            </tr>
                        </table>
                    </div>
                </td>
                
                <td class="btnCol">
                    <div class="btn" onclick="document.forms[0].reset()">
                        <table>
                            <tr>
                                <td class="lbl" nowrap>重置</td>
                            </tr>
                        </table>
                    </div>
                </td>
                
            </tr>
        </table>
    </div>
</form>

<div class="lst">
<form name="delete" method="post" action="departmentDelete.action">
    <table border="0" cellpadding="0" cellspacing="0" id="userList">
        <tr class="hdr"> 
          <td class="left">部门</td>
          <td class="left">描述</td>
          <td class="left">上级部门</td>
          <td class="left"></td>
        </tr> 
        <s:iterator value="departments">
	        <tr class="vms">
	            <td class="user"><a href="departmentGetOne.action?departmentname=<s:property value="departmentname"/>"><s:property value="departmentname"/></a></td>
	            <td class="user"><s:property value="description"/></td>
	            <td class="user"><s:property value="parent.description"/></td>
	            <td class="user"><input type="checkbox" name="u" value="<s:property value="departmentname"/>" /></td>	            
	        </tr>
        </s:iterator>
    </table>
</form>
</div>

     <s:url id="url_pre" value="departmentsGet.action">
        <s:param name="pageIndex" value="pageIndex-1"></s:param>
     </s:url>
     <s:a href="%{url_pre}">上一页</s:a>

     <s:iterator value="pages" status="status">
        <a href="departmentsGet.action?pageIndex=<s:property/>"><s:property/></a>
     </s:iterator>
         
     <s:url id="url_next" value="departmentsGet.action">
         <s:param name="pageIndex" value="pageIndex+1"></s:param>
     </s:url>
     <s:a href="%{url_next}">下一页</s:a>

<div id="std">
<table border="0" cellpadding="0" cellspacing="0" style="border-top: 0px solid #E6E6E6; margin-top: 24px">
    <tr>
        <td width="100%">&nbsp;</td>
        
        <td class="btnCol">
	        <div class="btn" onclick="document.forms[1].submit()">
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




