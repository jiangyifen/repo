<%@page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>登录页面</title>
</head>

<body>
error
username is: ${sessionScope.username}
password is: ${sessionScope.password}

<s:iterator value="errStrings">
    <s:property />
    <p />
</s:iterator>

</body>
</html>