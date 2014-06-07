<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html> 
<head> 
<title>修改部门</title>
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
    document.departmentupdate.submit();
    return true;
}
function test(){
    var dpmtname = document.getElementById("pdnameHidden");
    var slct2 = document.getElementById("pdname");
    slct2.value = dpmtname.value;
}
</script>
</head> 
 
<body id="bodyObj"  onload="test();"> 


<form method="POST" action="departmentUpdate.action"  name="departmentupdate">

    <div id="sxStatHdr" class="sctHdr">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="100%">修改部门</td>
            </tr>
        </table>
    </div>
    
<s:iterator value="departments">    
    <div class="cfg">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr class="cat">
                <td colspan="2">部&nbsp;门&nbsp;名:</td>
                <td><s:property value="departmentname"/><input name="departmentname" type="hidden" class="text" value="<s:property value="departmentname"/>"/></td>
            </tr>

            <tr class="cat">
                <td colspan="2">描&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;述：</td>
                <td><input name="description" type="text" class="text" value="<s:property value="description"/>"/></td>
            </tr>

			
            <tr class="cat">
                <td colspan="2">上级部门:</td>
                <td class="user">
                <input id="pdnameHidden" type="hidden" value="<s:property value="parent.departmentname"/>"/>
                <s:select theme="simple" list="pdpmts" name="pdname" listKey="departmentname" listValue="description"/>
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




