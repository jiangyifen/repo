﻿<%@ page language="java" contentType="text/html; charset=gb2312" pageEncoding="utf-8"%><%@ taglib prefix="s" uri="/struts-tags"%>QUEUE PARAMS:
completed:<s:property value="queueParamsEvent.completed"/>;abandoned:<s:property value="queueParamsEvent.abandoned"/>;calls:<s:property value="queueParamsEvent.calls"/>;max:<s:property value="queueParamsEvent.max"/>;holdtime:<s:property value="queueParamsEvent.holdtime"/>;queue:<s:property value="queueParamsEvent.queue"/>;serviceLevel:<s:property value="queueParamsEvent.serviceLevel"/>;serviceLevelPerf:<s:property value="queueParamsEvent.serviceLevelPerf"/>;strategy:<s:property value="queueParamsEvent.strategy"/>;weight:<s:property value="queueParamsEvent.weight"/>;loginMemberCount:<s:property value="loginMemberCount"/>
QUEUE ENTRYS:<s:iterator value="queueEntryList">
queue:<s:property value="queue"/>;position:<s:property value="position"/>;channel:<s:property value="channel"/>;callerId:<s:property value="callerId"/>;wait:<s:property value="wait"/>;</s:iterator>