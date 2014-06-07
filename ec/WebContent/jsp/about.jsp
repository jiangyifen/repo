<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.jiangyifen.ec.util.Version" %>

<%
java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy");
java.util.Date current = new java.util.Date();
String date = formatter.format(current);
%>

<center>
<div class="label"> 
<div class="c">&#169; 2007-<%=date %> 上海圣利信息科技有限公司 <a href="http://www.sl-soft.cn" target="_blank">http://www.sl-soft.cn</a></div> 
<div class="c">Version: <%= Version.VERSION %>;TAG: <%= Version.TAG %></div> 
</div> 
</center>
