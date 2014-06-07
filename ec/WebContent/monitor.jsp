<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html> 
<head> 
<title></title>
<meta http-equiv="refresh" content="60">
<script src="src/init.js"></script> 
<script src="src/xuaLib.js"></script> 
<script src="src/util.js"></script> 
<script src="src/vmCtrlBar.js"></script> 
<script src="src/vmCtxMenu.js"></script> 
<script src="src/sxMonitor.js"></script> 
<link rel="stylesheet" href="css/usage.css" type="text/css" /> 
<link rel="stylesheet" href="css/page.css" type="text/css" /> 
<script language="javascript">

</script>
</head> 
 
<body id="bodyObj">
 

<div class="sctHdr"> 
  <table border="0" cellpadding="0" cellspacing="0"> 
    <tr> 
      <td>呼叫信息(<s:property value="statusEvents.size"/>)</td> 
    </tr> 
  </table> 
</div> 

 <div class="lst">
    <table border="0" cellpadding="0" cellspacing="0">
                <tr class="hdr">
                    <td nowrap>callerIdNum</td>
                    <td nowrap>extension</td>
                    <td nowrap>channel</td>
                    <td nowrap>link</td>
                    <td nowrap>state</td>
                    <td nowrap>seconds</td>
                </tr>
                <s:iterator value="statusEvents">
                    <tr class="cdr">
                        <td class="dn" nowrap><s:property value="callerIdNum"/></td>
                        <td class="dn" nowrap><s:property value="extension"/></td>
                        <td class="dn" nowrap><s:property value="channel"/></td>
                        <td class="dn" nowrap><s:property value="link"/></td>
                        <td class="dn" nowrap><s:property value="state"/></td>
                        <td class="dn" nowrap><s:property value="seconds"/></td>
                    </tr>
                </s:iterator> 
    </table> 
 </div>
 
<br />


<jsp:include page="/jsp/about.jsp" flush="true"/>


</body> 
</html> 




