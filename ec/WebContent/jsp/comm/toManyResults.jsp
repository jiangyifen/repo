<%@page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>错误</title>
</head>

<body>
<b>您一次性查询的数据太多了！</b>
<p>为防止无效操作导致数据库大量占用系统资源，系统已对您的操作做了限制。</p>
<p>请将每次查询通话记录的范围控制在100天以内。</p>
</body>
</html>