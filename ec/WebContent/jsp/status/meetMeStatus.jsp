<%@ page language="java" contentType="text/html; charset=gb2312" pageEncoding="utf-8"%><%@ taglib prefix="s" uri="/struts-tags"%>
<s:iterator value="meetMeStatusList" status="st">
position:<s:property value="position"/>;callerIdNum:<s:property value="callerIdNum"/>;callerIdName:<s:property value="callerIdName"/>;channel:<s:property value="channel"/>;role:<s:property value="role"/>;time:<s:property value="time"/>;name:<s:property value="name"/>;</s:iterator>
