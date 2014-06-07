<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html> 
<head> 
<title>修改分机</title>
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
    if (document.sipupdate.secret.value != document.sipupdate.secret2.value) {
        alert("两次密码输入不一致，请重新输入！");
        document.sipupdate.secret.focus();
        return false;
    }
    document.sipupdate.submit();
    return true;
}
</script>
</head> 
 
<body id="bodyObj"> 


<form method="POST" action="sipUpdate.action"  name="sipupdate">

    <div id="sxStatHdr" class="sctHdr">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="100%">修改分机</td>
            </tr>
        </table>
    </div>
    
<s:iterator value="sips">    
    <div class="cfg">
        <input name="id" type="hidden" class="text" value="<s:property value="id"/>"/>
        <table border="0" cellpadding="0" cellspacing="0">
            <tr class="cat">
                <td colspan="2">分&nbsp;机&nbsp;号:</td><td><s:property value="name"/><input name="name" type="hidden" class="text" value="<s:property value="name"/>"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;门:</td>
                <td>
                <s:select theme="simple" list="dpmts" name="dpmt" listKey="departmentname" listValue="departmentname"/>
                </td>
            </tr>
            <tr class="cat">
                <td colspan="2">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:</td><td><input name="secret" type="password" class="text" value="<s:property value="secret"/>"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">确认密码:</td><td><input name="secret2" type="password" class="text" value="<s:property value="secret"/>"/></td>
            </tr>            
            <tr class="cat">
                <td colspan="2">Context:</td><td><input name="context" type="text" class="text" value="<s:property value="context"/>"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">Type:</td>
                <td>
                    <select name="type">
                        <%if(request.getAttribute("type")!=null && request.getAttribute("type").equals("friend")){ %><option value="friend" selected>Friend</option><%}else{ %><option value="ringall">Friend</option><%} %>
                        <%if(request.getAttribute("type")!=null && request.getAttribute("type").equals("user")){ %><option value="user" selected>User</option><%}else{ %><option value="user">User</option><%} %>
                        <%if(request.getAttribute("type")!=null && request.getAttribute("type").equals("peer")){ %><option value="peer" selected>Peer</option><%}else{ %><option value="peer">Peer</option><%} %>
                    </select>
                </td>
            </tr>
            <tr class="cat">
                <td colspan="2">Qualify:</td>
                <td>
                    <select name="qualify">
                        <%if(request.getAttribute("qualify")!=null && request.getAttribute("qualify").equals("yes")){ %><option value="yes" selected>Yes</option><%}else{ %><option value="yes">Yes</option><%} %>
                        <%if(request.getAttribute("qualify")!=null && request.getAttribute("qualify").equals("no")){ %><option value="no" selected>No</option><%}else{ %><option value="no">No</option><%} %>
                    </select>
                </td>
            </tr>
            <tr class="cat">
                <td colspan="2">Can reinvite:</td>
                <td>
                    <select name="canreinvite">
                        <%if(request.getAttribute("canreinvite")!=null && request.getAttribute("canreinvite").equals("yes")){ %><option value="yes" selected>Yes</option><%}else{ %><option value="yes">Yes</option><%} %>
                        <%if(request.getAttribute("canreinvite")!=null && request.getAttribute("canreinvite").equals("no")){ %><option value="no" selected>No</option><%}else{ %><option value="no">No</option><%} %>
                    </select>
                </td>
            </tr>
            <tr class="cat">
                <td colspan="2">disallow:</td><td><input name="disallow" type="text" class="text" value="<s:property value="disallow"/>"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">allow:</td><td><input name="allow" type="text" class="text" value="<s:property value="allow"/>"/></td>
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




