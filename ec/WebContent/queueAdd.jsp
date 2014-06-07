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
<script language="javascript">
function validate() {
    if (!document.queueadd.queuename.value) {
        alert("请输入队列名");
        document.queueadd.queuename.focus();
        return false;
    }
    document.queueadd.submit();
    return true;
}

</script>
</head> 
 
<body id="bodyObj"> 


<form method="POST" action="queueAdd.action"  name="queueadd">

    <div id="sxStatHdr" class="sctHdr">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="100%">添加呼叫队列</td>
            </tr>
        </table>
    </div>
    
    <div class="cfg">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr class="cat">
                <td colspan="2">队列名（数字或字母）:</td><td colspan="2"><input id="queuename" name="queuename" type="text" class="text"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">队列描述（支持中文）:</td><td colspan="2"><input id="description" name="description" type="text" class="text"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">等待音乐：</td><td colspan="2"><input name="musiconhold" type="text" class="text" value="default"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">
                    	振铃策略：</td><td colspan="2">
                    <select name="strategy">
                        <option value="ringall">群振</option>
                        <option value="roundrobin">轮询分配</option>
                        <option value="rrmemory">记忆轮询分配</option>
                        <option value="leastrecent">按最后一次接通分配</option>
                        <option value="fewestcalls">按呼叫次数分配</option>
                        <option value="random">随机分配</option>
                    </select>
                    
                </td>
            </tr>
            <tr class="cat">
                <td colspan="2">超时时长（秒）：</td><td colspan="2"><input name="timeout" type="text" class="text"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">冷却时长（秒）：</td><td colspan="2"><input name="wrapuptime" type="text" class="text"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">最大排队数：</td><td colspan="2"><input name="maxlen" type="text" class="text"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">
                   	 漏接置忙：</td><td colspan="2">
                    <select name="autopause">
                        <option value="yes">是</option>
                        <option value="no" selected>否</option>
                    </select>
                    
                </td>
            </tr>
            <tr class="cat">
                <td colspan="2">
                   	 动态成员：</td><td colspan="2">
                    <select name="dynamicMember">
                        <option value="true">是</option>
                        <option value="false" selected>否</option>
                    </select>
                    
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
                    <div class="btn" onclick="document.forms[0].reset();">
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




