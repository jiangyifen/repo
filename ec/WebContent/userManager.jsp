<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html> 
<head> 
<title>用户管理</title>
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
		if (!document.useradd.username.value) {
			alert("请输入用户名");
			document.useradd.username.focus();
			return false;
		}
		if (document.useradd.password.value != document.useradd.password2.value) {
			alert("两次密码输入不一致，请重新输入！");
			document.useradd.password.focus();
			return false;
		}
		document.useradd.submit();
		return true;
	}

	function deleteUser() {
		var msg = "确定要删除吗？";

		if (confirm(msg)) {
			document.forms[1].submit();
		}
	}

	function searchUser() {
		var value = document.getElementById("condition").value;
		location.href = 'usersGet?condition=' + value;
	}

	function toPage(page) {
		var item = document.getElementById("condition").value;
		window.location.href = "usersGet.action?condition="+item+"&pageIndex="+page;
	}
</script>
</head> 
 
<body id="bodyObj"> 


<form method="POST" action="userAdd.action"  name="useradd">

    <div id="sxStatHdr" class="sctHdr">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="100%">添加用户</td>
            </tr>
        </table>
    </div>
    
    
    <div class="cfg">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr class="cat">
                <td colspan="2">用户名:</td>
                <td><input name="username" type="text" class="text"/></td>
            </tr>

            <tr class="cat">
                <td colspan="2">密码:</td>
                <td><input name="password" type="password" class="text"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">确认密码:</td>
                <td><input name="password2" type="password" class="text"/></td>
            </tr>            
            <tr class="cat">
                <td colspan="2">E-Mail:</td>
                <td><input name="email" type="text" class="text"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">姓名:</td>
                <td><input name="name" type="text" class="text"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">HID:</td>
                <td><input name="hid" type="text" class="text"/></td>
            </tr> 
            <tr class="cat">
                <td colspan="2">角色:</td>
                <td class="user">
                
                <s:select theme="simple" list="roles" name="rolename" listKey="rolename" listValue="rolename"/>

                </td>
            </tr>
            <tr class="cat">
                <td colspan="2">部门:</td>
                <td class="user">
                
                <s:select theme="simple" list="departments" name="departmentname" listKey="departmentname" listValue="description"/>

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

<hr>

	<div id="std">
	<table border="0" cellpadding="0" cellspacing="0" style="border-top: 0px solid #E6E6E6; margin-top: 24px">
	    <tr>
	        
	        <td align="right" width="100%" >搜索条件:</td>

	        <td class="btnCol">
				<input id="condition" type="text" class="text" value="<s:property value="condition"/>"/>
	        </td>

	        <td class="btnCol">
		        <div class="btn" onclick="searchUser()">
		        <table>
		            <tr>
		                <td class="lbl" nowrap>搜索</td>
		            </tr>
		        </table>
		        </div>
	        </td>

	        <td class="btnCol">
		        <div class="btn" onclick="deleteUser()">
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


<div class="lst">
<form name="delete" method="post" action="userDelete.action">
    <table border="0" cellpadding="0" cellspacing="0" id="userList">
        <tr class="hdr"> 
          <td class="left">用户名</td>
          <td class="left">姓名</td>
          <td class="left">HID</td>
          <td class="left">角色</td>
          <td class="left">部门</td>
          <td class="left"></td> 
        </tr> 
        <s:iterator value="users">
	        <tr class="vms">
	            <td class="user"><a href="userGetOne.action?username=<s:property value="username"/>"><s:property value="username"/></a></td>
	            <td class="user"><s:property value="name"/></td>
	            <td class="user"><s:property value="hid"/></td>
	            <td class="user"><s:property value="role.rolename"/></td>
	            <td class="user"><s:property value="department.description"/></td>	
	            <td class="user"><input type="checkbox" name="u" value="<s:property value="username"/>" /></td>	            
	        </tr>
        </s:iterator>
    </table>
</form>
</div>

     <s:url id="url_pre" value="usersGet.action">
        <s:param name="pageIndex" value="pageIndex-1"></s:param>
        <s:param name="condition" value="condition"></s:param>
     </s:url>
     

	<s:a href="%{url_pre}">上一页</s:a>

     <s:iterator value="pages" status="status">
        <a href="javascript:toPage(<s:property/>)"><s:property/></a>
     </s:iterator>
         
     <s:url id="url_next" value="usersGet.action">
         <s:param name="pageIndex" value="pageIndex+1"></s:param>
         <s:param name="condition" value="condition"></s:param>
     </s:url>
     <s:a href="%{url_next}">下一页</s:a>



</body> 
</html> 




