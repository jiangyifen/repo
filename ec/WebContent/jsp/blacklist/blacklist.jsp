<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html> 
<head> 
<title>黑名单</title>
<script src="src/init.js"></script>
<script src="src/xuaLib.js"></script>
<script src="src/util.js"></script>
<script src="src/vmCtrlBar.js"></script>
<script src="src/vmCtxMenu.js"></script>
<script src="src/sxMonitor.js"></script>
<script language="javascript" type="text/javascript" src="src/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" href="css/usage.css" type="text/css" />
<link rel="stylesheet" href="css/page.css" type="text/css" />
<link rel="stylesheet" href="css/calendar.css" type="text/css" />


<script language="JavaScript"> 
	function addBlackListItem() {
		path = "blackListItemAdd.action?phoneNumber="+document.getElementById("phoneNumber").value+"&type="
				+ document.getElementById("type").value+"&remark="+document.getElementById("remark").value;
		document.getElementById("phoneNumber").value = "";
		document.getElementById("type").value = "both";
		window.location=path;
	}

</script> 


</head> 
 
<body>
<!--
	<form name="blacklistitemAdd" action="blackListItemAdd.action">
	    <div id="std">
	        <table border="0" cellpadding="0" cellspacing="0" style="border-top: 0px solid #E6E6E6; margin-top: 24px">
                <tr>
                    <td width="100%">
						电话号码:&nbsp;&nbsp;&nbsp;&nbsp;<input name="phoneNumber" value="<s:property value="phoneNumber"/>" type="text" class="text"/>&nbsp;&nbsp;&nbsp;&nbsp;
						方向:&nbsp;&nbsp;&nbsp;&nbsp;<select name="type">
                                                            <option value="incoming" <%if (request.getAttribute("type")!=null && request.getAttribute("type").equals("incoming")){%>selected<%} %>>呼入</option>
                                                            <option value="outgoing" <%if (request.getAttribute("type")!=null && request.getAttribute("type").equals("outgoing")){%>selected<%} %>>呼出</option>
                                                     </select>
					</td>
                    <td class="btnColLft">
                        <div class="btn" onclick="document.blacklistitemAdd.submit()">
                            <table>
                                <tr>
                                    <td class="lbl" nowrap>新增</td>
                                </tr>
                            </table>
                        </div>
                    </td>
                </tr>
	        </table>
	    </div>
	</form>
-->
	<form name="blacklistitem" action="blackListItemGet.action">
	    <div id="std">
	        <table border="0" cellpadding="0" cellspacing="0" style="border-top: 0px solid #E6E6E6; margin-top: 24px">
                <tr>
                    <td width="100%">
						电话号码:&nbsp;&nbsp;&nbsp;&nbsp;<input name="phoneNumber" value="<s:property value="phoneNumber"/>" type="text" class="text"/>&nbsp;&nbsp;&nbsp;&nbsp;
						方向:&nbsp;&nbsp;&nbsp;&nbsp;<select name="type">
                                                            <option value="both" <%if (request.getAttribute("type")!=null && request.getAttribute("type").equals("both")){%>selected<%} %>>全部</option>
                                                            <option value="incoming" <%if (request.getAttribute("type")!=null && request.getAttribute("type").equals("incoming")){%>selected<%} %>>呼入</option>
                                                            <option value="outgoing" <%if (request.getAttribute("type")!=null && request.getAttribute("type").equals("outgoing")){%>selected<%} %>>呼出</option>
                                                     </select>
						备注:&nbsp;&nbsp;&nbsp;&nbsp;<input name="remark" value="<s:property value="remark"/>" type="text" class="text"/>&nbsp;&nbsp;&nbsp;&nbsp;
						导出Excel<input type="checkbox" value="true" name="excel"/>
					</td>
                    <td class="btnColLft">
                        <div class="btn" onclick="document.blacklistitem.submit()">
                            <table>
                                <tr>
                                    <td class="lbl" nowrap>查询</td>
                                </tr>
                            </table>
                        </div>
                    </td>
                	<td>&nbsp;</td>
                    <td class="btnColLft">
                        <div class="btn" onclick="addBlackListItem();">
                            <table>
                                <tr>
                                    <td class="lbl" nowrap>新增</td>
                                </tr>
                            </table>
                        </div>
                    </td>
                </tr>
	        </table>
	    </div>
	    
		<div class="lst">
		    <table border="0" cellpadding="0" cellspacing="0" id="list">
		        <tr class="hdr">
	                <td nowrap>id</td>
	                <td nowrap>电话号码</td>
	                <td nowrap>方向</td>
	                <td nowrap>备注</td>
	                <td nowrap>删除</td>
		        </tr> 
			    <s:iterator value="blackListItem">
				    <tr class="cdr" >
	                    <td class="dn" nowrap><s:property value="id"/></td>
	                    <td class="dn" nowrap><s:property value="phoneNumber"/></td>
	                    <td class="dn" nowrap><s:property value="type"/></td>
	                    <td class="dn" nowrap><s:property value="remark"/></td>
	                    <td class="dn" nowrap><a href="blackListItemDelete.action?id=<s:property value="id"/>">删除</a></td>
	                    
				    </tr>
			    </s:iterator>
	        </table>   
		</div>
    	
		<div class="std" align="right">
		共<s:property value="blackListCount"/>条结果&nbsp;&nbsp;&nbsp;&nbsp;第<s:property value="pageIndex"/>页，共 <s:property value="maxPageIndex"/>页&nbsp;&nbsp;&nbsp;&nbsp;跳转到第<s:select name="pageIndex" list="pages" onChange="document.blacklistitem.submit()"></s:select>页
		</div>

	</form>

<br />

<jsp:include page="/jsp/about.jsp" flush="true"/>

</body> 
</html> 




