<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html> 
<head> 
<title>修改用户</title>
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
    if (document.userupdate.password.value != document.userupdate.password2.value) {
        alert("两次密码输入不一致，请重新输入！");
        document.userupdate.password.focus();
        return false;
    }
    document.userupdate.submit();
    return true;
}

function test(){
    var rname = document.getElementById("rname");
    var slct1 = document.getElementById("rolename");
    slct1.value = rname.value;
    
    var dpmtname = document.getElementById("dpmtname");
    var slct2 = document.getElementById("departmentname");
    slct2.value = dpmtname.value;
}

</script>
</head> 
 
<body id="bodyObj" onload="test();"> 


<form method="POST" action="userUpdate.action"  name="userupdate">

    <div id="sxStatHdr" class="sctHdr">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="100%">修改用户</td>
            </tr>
        </table>
    </div>
    
<s:iterator value="users">    
    <div class="cfg">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr class="cat">
                <td colspan="2">用&nbsp;户&nbsp;名:</td><td><s:property value="username"/><input name="username" type="hidden" class="text" value="<s:property value="username"/>"/></td>
            </tr>

            <tr class="cat">
                <td colspan="2">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:</td>
                <td><input name="password" type="password" class="text" value="<s:property value="password"/>"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">确认密码:</td>
                <td><input name="password2" type="password" class="text" value="<s:property value="password"/>"/></td>
            </tr>
                        
            <tr class="cat">
                <td colspan="2">E-Mail:</td>
                <td><input name="email" type="text" class="text" value="<s:property value="email"/>"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:</td>
                <td><input name="name" type="text" class="text" value="<s:property value="name"/>"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">HID:</td>
                <td><input name="hid" type="text" class="text" value="<s:property value="hid"/>"/></td>
            </tr> 
            <tr class="cat">
                <td colspan="2">角&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;色:</td>
                <td>
                <input id="rname" type="hidden" value="<s:property value="role.rolename"/>"/>
                	<s:select theme="simple" list="rs" name="rolename" listKey="rolename" listValue="rolename"/>
                </td>
            </tr>
            <tr class="cat">
                <td colspan="2">部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;门:</td>
                <td class="user">
                <input id="dpmtname" type="hidden" value="<s:property value="department.departmentname"/>"/>
                <s:select theme="simple" list="departments" name="departmentname" listKey="departmentname" listValue="description"/>

                </td>
            </tr>
         
        </table>
    </div>
</s:iterator>
    
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

            </tr>
        </table>
    </div>
</form>

</body> 
</html> 




