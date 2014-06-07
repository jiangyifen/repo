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

<form method="POST" action="sipAdd.action"  name="sipadd">

    <div id="sxStatHdr" class="sctHdr">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="100%">添加分机</td>
            </tr>
        </table>
    </div>
    
    
    <div class="cfg">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr class="cat">
                <td colspan="2">分&nbsp;机&nbsp;号:</td><td><input name="name" type="text" class="text"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;门:</td>
                <td><input name="dpmt" type="text" class="text"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:</td><td><input name="secret" type="password" class="text"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">确认密码:</td><td><input name="secret2" type="password" class="text"/></td>
            </tr>            
            <tr class="cat">
                <td colspan="2">Context:</td><td><input name="context" type="text" class="text" value="outgoing"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">Type:</td><td><select name="type" class="text"><option value="friend" selected>Friend</option><option value="user">User</option><option value="peer">Peer</option></select></td>
            </tr>
            <tr class="cat">
                <td colspan="2">Qualify:</td><td><select name="qualify" class="text"><option value="yes" selected>Yes</option><option value="no">No</option></select></td>
            </tr>
            <tr class="cat">
                <td colspan="2">Can reinvite:</td><td><select name="canreinvite"class="text"><option value="no" selected>No</option><option value="yes">Yes</option></select></td>
            </tr>
            <tr class="cat">
                <td colspan="2">disallow:</td><td><input name="disallow" type="text" class="text" value="all"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">allow:</td><td><input name="allow" type="text" class="text" value="all"/></td>
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

</body> 
</html> 




