<%@page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title>Efficient Call CTI Server Login</title>
<script src="src/init.js"></script>
<script src="src/xuaLib.js"></script>
<script src="src/base64.js"></script>
<script src="src/login.js"></script>
<script>
function checkErr(){        
    if('${sessionScope.login}' == 'false')
    	document.getElementById("err").style.display="block";
}
</script>
<link rel="stylesheet" href="css/page.css" type="text/css" />


</head>
<body onload="checkErr()">
<br/>
<br/>

<center>
	<form name="v">
		<table border="0" cellpadding="0" cellspacing="0" style="width:301px;">
			<tr>
			    <td style="padding:0px;width:100%;">
			        <div class="btn" style="margin-bottom: 0px; width:100%;">
			            <table style="background: url(imx/tile-0003.png);border-color: #666666; width:100%;">
			                <tr>
			                    <td class="icon" nowrap><img src="imx/logo_16x16.png" /></td>
			                    <td width="100%" class="ilbl" nowrap style="color: #000000; text-align: center;">EC 呼叫中心管理系统</td>
			                </tr>
			            </table>
			        </div>
			         
			        <table border="0" cellpadding="0" cellspacing="0" style="background: url(imx/tile-0004.png);border: solid 1px #666666;border-top: none;margin-bottom: 0px; width:100%;">
                        <tr>
                            <td align="right" style="padding-top: 12px;padding-left:12px;width:20%">用户名</td>
                            <td align="right" style="padding-top: 12px;padding-right:12px; width:80%;">
			                <input name="u" type="text" class="text" value="" style="width: 100%;"  onkeypress="javascript:if(event.keyCode==13){login();}"/></td>
                        </tr>
			    
                        <tr>
			                <td align="right" style="padding-bottom: 6px;padding-left:12px;width:20%;">密码</td>
			                <td align="right" style="padding-bottom: 6px;padding-right:12px; width:80%;">
			                <input name="p" type="password" class="text" value="" style="width: 100%;" onkeypress="javascript:if(event.keyCode==13){login();}"/></td>
                        </tr>
                        <tr>
			                <td colspan="2" align="right" style="padding-bottom: 12px;padding-right:12px;">
                                <div class="btn">
                                    <table onclick="javascript:login();">
				                        <tr>
				                            <td class="lbl" nowrap>登录</td>
				                        </tr>
			                        </table>
			                    </div>
			                </td>
                        </tr>
			        </table>
			    </td>
			</tr>
		</table>
	</form>    
	<form name="k" method="post" action="login.action">
		<input name="username" type="hidden" value=""/>
		<input name="password" type="hidden" value=""/>
	</form>
</center>


<div id="err" style="display:none; margin-left:25%; margin-right:25%;">
  <br />
  <center>
    <table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top" nowrap><img src="imx/error-12x12.png" width="12" height="12" vspace="2"/></td>
        <td><p id="str"></p>登录失败</td>
      </tr>
    </table>
  </center>
</div>

<div id="cookieless" style="display:none; margin-left:25%; margin-right:25%;">
  <br />
  <center>
    <table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top"><img src="imx/error-12x12.png" width="12" height="12" vspace="2"/></td>
        <td><p>The EasyCall CTI Server Management Interface depends on HTTP cookies. Please ensure that you are using a compatible browser and that HTTP cookies are enabled.</p></td>
      </tr>
    </table>
  </center>
</div>

<br />
<br />

<jsp:include page="/jsp/about.jsp" flush="true"/>



</body>
</html>
