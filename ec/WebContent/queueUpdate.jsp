<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html> 
<head> 
<title>修改呼叫队列</title>
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
    if (!document.queueupdate.queuename.value) {
        alert("队列名不能为空");
        document.queueupdate.queuename.focus();
        return false;
    }
    document.queueupdate.submit();
    return true;
}

function insert_row(iface, penalty){
    var queuename = document.getElementById('queuename').value;
    R=tbl.insertRow();
    C=R.insertCell();
    C.innerHTML = iface + "";
    C=R.insertCell();
    C.innerHTML = penalty + "";
    C=R.insertCell();
    C.innerHTML="<a href=\"queueMemberDelete.action?queuename="+queuename+"&iface="+iface+"\">删除</a>";
} 
</script>
</head> 
 
<body id="bodyObj"> 

<s:iterator value="queues">
<form method="POST" action="queueUpdate.action"  name="queueupdate">

    <div id="sxStatHdr" class="sctHdr">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="100%">修改呼叫队列</td>
            </tr>
        </table>
    </div>
    
    
    <div class="cfg">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr class="cat">
                <td colspan="2">队列名（数字或字母）:</td><td colspan="2"><s:property value="name"/><input name="queuename" type="hidden" class="text" value="<s:property value="name"/>"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">队列描述（支持中文）:</td><td colspan="2"><input name="description" type="text" class="text" value="<s:property value="description"/>"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">等待音乐：</td><td colspan="2"><input name="musiconhold" type="text" class="text" value="<s:property value="musiconhold"/>"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">
                    	振铃策略：</td><td colspan="2">
	                <select name="strategy">
					    <%if(request.getAttribute("strategy")!=null && request.getAttribute("strategy").equals("ringall")){ %><option value="ringall" selected>群振</option><%}else{ %><option value="ringall">群振</option><%} %>
					    <%if(request.getAttribute("strategy")!=null && request.getAttribute("strategy").equals("roundrobin")){ %><option value="roundrobin" selected>轮询分配</option><%}else{ %><option value="roundrobin">轮询分配</option><%} %>
					    <%if(request.getAttribute("strategy")!=null && request.getAttribute("strategy").equals("rrmemory")){ %><option value="rrmemory" selected>记忆轮询分配</option><%}else{ %><option value="rrmemory">记忆轮询分配</option><%} %>
					    <%if(request.getAttribute("strategy")!=null && request.getAttribute("strategy").equals("leastrecent")){ %><option value="leastrecent" selected>按最后一次接通分配</option><%}else{ %><option value="leastrecent">按最后一次接通分配</option><%} %>
					    <%if(request.getAttribute("strategy")!=null && request.getAttribute("strategy").equals("fewestcalls")){ %><option value="fewestcalls" selected>按呼叫次数分配</option><%}else{ %><option value="fewestcalls">按呼叫次数分配</option><%} %>
					    <%if(request.getAttribute("strategy")!=null && request.getAttribute("strategy").equals("random")){ %><option value="random" selected>随机分配</option><%}else{ %><option value="random">随机分配</option><%} %>
	                </select>
	            </td>
            </tr>
            <tr class="cat">
                <td colspan="2">超时时长（秒）：</td><td colspan="2"><input name="timeout" type="text" class="text" value="<s:property value="timeout"/>"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">冷却时长（秒）：</td><td colspan="2"><input name="wrapuptime" type="text" class="text" value="<s:property value="wrapuptime"/>"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">最大排队数：</td><td colspan="2"><input name="maxlen" type="text" class="text" value="<s:property value="maxlen"/>"/></td>
            </tr>
            <tr class="cat">
                <td colspan="2">
                    	漏接置忙：</td><td colspan="2">
	                <select name="autopause">
					    <%if(request.getAttribute("autopause")!=null && request.getAttribute("autopause").equals("yes")){ %><option value="yes" selected>是</option><%}else{ %><option value="yes">是</option><%} %>
					    <%if(request.getAttribute("autopause")!=null && request.getAttribute("autopause").equals("no")){ %><option value="no" selected>否</option><%}else{ %><option value="no">否</option><%} %>
					</select>
	            </td>
          	</tr>
          	<tr class="cat">
                <td colspan="2">
                    	 动态成员：</td><td colspan="2">
	                <select name="dynamicMember">
					    <%if((Boolean)request.getAttribute("dynamicMember")){ %><option value="true" selected>是</option><%}else{ %><option value="true">是</option><%} %>
					    <%if(!(Boolean)request.getAttribute("dynamicMember")){ %><option value="false" selected>否</option><%}else{ %><option value="false">否</option><%} %>
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

            </tr>
        </table>
    </div>
</form>


    <form action="queueMemberAdd.action" method="post" name="qmupdate">
    <input type="hidden" name="queuename" value="<s:property value="name"/>"/>
        <div class="cfg">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr class="cat">
                <td colspan="2">
                    <div>队列成员:&nbsp;&nbsp;&nbsp;</div>
                    <table id="tbl" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td>interface</td>
                            <td>penalty</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td><input id="iface" name="iface" type="text" class="text"/></td>
                            <td><input id="penalty" name="penalty" type="text" class="text"/></td>
                            <td><a href="javascript:document.qmupdate.submit();">添加</a></td>
                    </tr>
                    
                        <s:iterator value="members" status="stat">
                        <script>insert_row("<s:property value="iface"/>","<s:property value="penalty"/>");</script>
                        </s:iterator>
                    </table> 
                </td>
            </tr>
        </table>
        </div>
    </form>

</s:iterator>


</body> 
</html> 




