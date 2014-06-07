<%@ page language="java" contentType="text/html; charset=gb2312" pageEncoding="utf-8"%><%@ taglib prefix="s" uri="/struts-tags"%>
<s:iterator value="sipStatus" status="st">
sipname:<s:property value="sipName"/>;status:<s:property value="status"/>;loginusername:<s:property value="loginusername"/>;name:<s:property value="name"/>;countCallin:<s:property value="countCallin"/>;countCallout:<s:property value="countCallout"/></s:iterator>
