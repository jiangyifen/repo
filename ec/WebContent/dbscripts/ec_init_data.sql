
insert into ec_role values ('admin','管理员');
insert into ec_role values ('agent','座席');

insert into ec_roleaction values ('login','登录');
insert into ec_roleaction values ('logout','注销');
insert into ec_roleaction values ('changePassword','修改密码');
insert into ec_roleaction values ('dialTaskAdd','添加外拨任务');
insert into ec_roleaction values ('dialTaskUpdate','修改外拨任务');
insert into ec_roleaction values ('dialTaskGetOne','查看外拨任务信息');
insert into ec_roleaction values ('dialTasksGet','列出所有外拨任务');
insert into ec_roleaction values ('dialTaskDelete','删除外拨任务');
insert into ec_roleaction values ('dialTaskStatusChange','外拨任务状态更改');
insert into ec_roleaction values ('dialTaskAutoAssignChange','自动派单状态更改');
insert into ec_roleaction values ('dialTaskItemImport','外拨任务项导入');
insert into ec_roleaction values ('dialTaskItemDelete','外拨任务项删除');
insert into ec_roleaction values ('crmDialTaskAssign','CRM外拨任务分派');
insert into ec_roleaction values ('crmDialTaskRetrieve','CRM外拨任务回收');
insert into ec_roleaction values ('dialTaskSmSend','外拨任务短信群发');
insert into ec_roleaction values ('batchSmTaskStatusGet','短信群发任务状态');
insert into ec_roleaction values ('userAdd','添加用户');
insert into ec_roleaction values ('userUpdate','修改用户');
insert into ec_roleaction values ('userGetOne','查看用户信息');
insert into ec_roleaction values ('usersGet','列出所有用户');
insert into ec_roleaction values ('userDelete','删除用户');
insert into ec_roleaction values ('roleAdd','添加角色');
insert into ec_roleaction values ('roleUpdate','修改角色');
insert into ec_roleaction values ('roleGetOne','查看角色信息');
insert into ec_roleaction values ('rolesGet','列出所有角色');
insert into ec_roleaction values ('roleDelete','删除角色');
insert into ec_roleaction values ('departmentAdd','添加部门');
insert into ec_roleaction values ('departmentUpdate','修改部门');
insert into ec_roleaction values ('departmentGetOne','查看部门信息');
insert into ec_roleaction values ('departmentsGet','列出所有部门');
insert into ec_roleaction values ('departmentDelete','删除部门');
insert into ec_roleaction values ('sipAdd','添加分机');
insert into ec_roleaction values ('sipUpdate','修改分机');
insert into ec_roleaction values ('sipGetOne','查看分机信息');
insert into ec_roleaction values ('sipsGet','列出所有分机');
insert into ec_roleaction values ('sipDelete','删除分机');
insert into ec_roleaction values ('queueAdd','添加队列');
insert into ec_roleaction values ('queueUpdate','修改队列');
insert into ec_roleaction values ('queueGetOne','查看队列信息');
insert into ec_roleaction values ('queuesGet','列出所有队列');
insert into ec_roleaction values ('queueDelete','删除队列');
insert into ec_roleaction values ('queueMemberAdd','添加队列成员');
insert into ec_roleaction values ('queueMemberDelete','删除队列成员');
insert into ec_roleaction values ('queueUserLinkGet','获取队列和用户的对应关系');
insert into ec_roleaction values ('queueUserLinkUpdate','更新队列和用户的对应关系');
insert into ec_roleaction values ('systemMonitor','查看系统状态');
insert into ec_roleaction values ('getCdr','查询呼叫记录');
insert into ec_roleaction values ('getSm','查询短信记录');
insert into ec_roleaction values ('noticeGetDpmt','获取公告部门');
insert into ec_roleaction values ('noticeGet','查询公告');
insert into ec_roleaction values ('noticeDetailGet','获取公告详细内容');
insert into ec_roleaction values ('noticeUpdate','更新公告');
insert into ec_roleaction values ('noticeAdd','新增公告');
insert into ec_roleaction values ('noticeDelete','删除公告');
insert into ec_roleaction values ('getVoicemail','查询语音留言');
insert into ec_roleaction values ('blackListItemGet','获取黑名单');
insert into ec_roleaction values ('blackListItemDelete','删除黑名单');
insert into ec_roleaction values ('blackListItemAdd','添加黑名单');
insert into ec_roleaction values ('getChartOutgoingWorkload','【报表】呼出工作量');
insert into ec_roleaction values ('getChartOutgoingWorkloadDistinctDst','【报表】呼出工作量（唯一号码）');
insert into ec_roleaction values ('getChartQueueIncomingWorkload','【报表】呼入工作量');
insert into ec_roleaction values ('getChartIncomingWorkload','【报表】外线呼入工作量');
insert into ec_roleaction values ('getChartWorkloadTimeline','【报表】工作量时间线');
insert into ec_roleaction values ('getChartWorkloadAverageTimeline','【报表】人均工作量时间线');
insert into ec_roleaction values ('getChartMyCustomerTimeline','【报表】意向客户时间线');
insert into ec_roleaction values ('getChartWorkEfficiencyDistribution','【报表】工作效率');
insert into ec_roleaction values ('getChartMyCustomer','【报表】意向客户统计');
insert into ec_roleaction values ('getChartWorkload','【报表】综合工作量');
insert into ec_roleaction values ('getChartWeightedWorkload','【报表】加权工作量');
insert into ec_roleaction values ('getChartAbandon','【报表】队列放弃次数');
insert into ec_roleaction values ('getChartQueueEntryTimes','【报表】队列进入次数');
insert into ec_roleaction values ('getChartWait','【报表】队列平均等待时长');
insert into ec_roleaction values ('getChartQueueWaitAndEntryTimes','【报表】队列进入次数和平均等待时长');
insert into ec_roleaction values ('getChartSMSendQuantity','【报表】短信发送量');
insert into ec_roleaction values ('getChartQueueWaitDistribution','【报表】队列等待时长分布');
insert into ec_roleaction values ('getChartAgentAssessment','【报表】座席人员考核报表');
insert into ec_roleaction values ('getChartAgentMinLoginTimeAndMaxLogoutTime','【报表】座席人员登陆登出时间');
insert into ec_roleaction values ('getChartCustomerSatisfactionInvestigation','【报表】客户满意度调查');
insert into ec_roleaction values ('getChartManDay','【报表】人天数统计');
insert into ec_roleaction values ('getChartAgentLoginLogoutDetail','【报表】座席人员登入登出详单');
insert into ec_roleaction values ('getChartAgentPauseDetail','【报表】座席人员置忙置闲详单');
insert into ec_roleaction values ('getChartQueueEnrtyAndAbandonDistinct','【报表】队列话务报表');
insert into ec_roleaction values ('getChartQueueEnrtyAndAnswerIVRLog','【报表】队列话务报表');
insert into ec_roleaction values ('getChartOverallReport','【报表】整体话务报表');
insert into ec_roleaction values ('getChartCSRSatisfactionReport','【报表】CSR个人工作表现');
insert into ec_roleaction values ('getChartQueueAbandonDetail','【报表】队列放弃详单');
insert into ec_roleaction values ('getChartConcurrent','【报表】总体并发量');
insert into ec_roleaction values ('getChartIVRQuit','【报表】IVR节点退出统计');
insert into ec_roleaction values ('getChartOperationalData','【报表】operation data');
insert into ec_roleaction values ('getChart30sDetail','【报表】30 seconds detail');



insert into ec_roleaction values ('getQueueStatus','查看队列状态');
insert into ec_roleaction values ('getSipStatus','查看分机状态');
insert into ec_roleaction values ('dialTaskLogsGet','查看外呼任务统计');
insert into ec_roleaction values ('smSend','短信发送');
insert into ec_roleaction values ('shiftConfigGet','排班信息获取');
insert into ec_roleaction values ('shiftConfigAdd','排班信息添加');
insert into ec_roleaction values ('shiftConfigUpdate','排班信息更新');
insert into ec_roleaction values ('shiftConfigDelete','排班信息删除');

insert into ec_roleaction values ('faqCount','【报表】FAQ统计');
insert into ec_roleaction values ('faqDetail','【报表】FAQ详单');
insert into ec_roleaction values ('fenduanHour','【报表】座席按分段报表（小时）');
insert into ec_roleaction values ('fenduanDay','【报表】座席分段报表（天）');
insert into ec_roleaction values ('fenduanMonth','【报表】座席分段报表（月）');


insert into ec_role_roleaction_link values ('admin','login');
insert into ec_role_roleaction_link values ('admin','logout');
insert into ec_role_roleaction_link values ('admin','changePassword');
insert into ec_role_roleaction_link values ('admin','dialTaskAdd');
insert into ec_role_roleaction_link values ('admin','dialTaskUpdate');
insert into ec_role_roleaction_link values ('admin','dialTaskGetOne');
insert into ec_role_roleaction_link values ('admin','dialTasksGet');
insert into ec_role_roleaction_link values ('admin','dialTaskDelete');
insert into ec_role_roleaction_link values ('admin','dialTaskStatusChange');
insert into ec_role_roleaction_link values ('admin','dialTaskAutoAssignChange');
insert into ec_role_roleaction_link values ('admin','dialTaskItemImport');
insert into ec_role_roleaction_link values ('admin','dialTaskItemDelete');
insert into ec_role_roleaction_link values ('admin','crmDialTaskAssign');
insert into ec_role_roleaction_link values ('admin','crmDialTaskRetrieve');
insert into ec_role_roleaction_link values ('admin','dialTaskSmSend');
insert into ec_role_roleaction_link values ('admin','batchSmTaskStatusGet');
insert into ec_role_roleaction_link values ('admin','userAdd');
insert into ec_role_roleaction_link values ('admin','userUpdate');
insert into ec_role_roleaction_link values ('admin','userGetOne');
insert into ec_role_roleaction_link values ('admin','usersGet');
insert into ec_role_roleaction_link values ('admin','userDelete');
insert into ec_role_roleaction_link values ('admin','roleAdd');
insert into ec_role_roleaction_link values ('admin','roleUpdate');
insert into ec_role_roleaction_link values ('admin','roleGetOne');
insert into ec_role_roleaction_link values ('admin','rolesGet');
insert into ec_role_roleaction_link values ('admin','roleDelete');
insert into ec_role_roleaction_link values ('admin','departmentAdd');
insert into ec_role_roleaction_link values ('admin','departmentUpdate');
insert into ec_role_roleaction_link values ('admin','departmentGetOne');
insert into ec_role_roleaction_link values ('admin','departmentsGet');
insert into ec_role_roleaction_link values ('admin','departmentDelete');
insert into ec_role_roleaction_link values ('admin','sipAdd');
insert into ec_role_roleaction_link values ('admin','sipUpdate');
insert into ec_role_roleaction_link values ('admin','sipGetOne');
insert into ec_role_roleaction_link values ('admin','sipsGet');
insert into ec_role_roleaction_link values ('admin','sipDelete');
insert into ec_role_roleaction_link values ('admin','queueAdd');
insert into ec_role_roleaction_link values ('admin','queueUpdate');
insert into ec_role_roleaction_link values ('admin','queueGetOne');
insert into ec_role_roleaction_link values ('admin','queuesGet');
insert into ec_role_roleaction_link values ('admin','queueDelete');
insert into ec_role_roleaction_link values ('admin','queueMemberAdd');
insert into ec_role_roleaction_link values ('admin','queueMemberDelete');
insert into ec_role_roleaction_link values ('admin','queueUserLinkGet');
insert into ec_role_roleaction_link values ('admin','queueUserLinkUpdate');
insert into ec_role_roleaction_link values ('admin','systemMonitor');
insert into ec_role_roleaction_link values ('admin','getCdr');
insert into ec_role_roleaction_link values ('admin','getSm');
insert into ec_role_roleaction_link values ('admin','noticeGetDpmt');
insert into ec_role_roleaction_link values ('admin','noticeGet');
insert into ec_role_roleaction_link values ('admin','noticeDetailGet');
insert into ec_role_roleaction_link values ('admin','noticeUpdate');
insert into ec_role_roleaction_link values ('admin','noticeAdd');
insert into ec_role_roleaction_link values ('admin','noticeDelete');
insert into ec_role_roleaction_link values ('admin','getVoicemail');
insert into ec_role_roleaction_link values ('admin','blackListItemGet');
insert into ec_role_roleaction_link values ('admin','blackListItemDelete');
insert into ec_role_roleaction_link values ('admin','blackListItemAdd');
insert into ec_role_roleaction_link values ('admin','getChartOutgoingWorkload');
insert into ec_role_roleaction_link values ('admin','getChartOutgoingWorkloadDistinctDst');
insert into ec_role_roleaction_link values ('admin','getChartQueueIncomingWorkload');
insert into ec_role_roleaction_link values ('admin','getChartIncomingWorkload');
insert into ec_role_roleaction_link values ('admin','getChartWorkloadTimeline');
insert into ec_role_roleaction_link values ('admin','getChartWorkloadAverageTimeline');
insert into ec_role_roleaction_link values ('admin','getChartMyCustomerTimeline');
insert into ec_role_roleaction_link values ('admin','getChartWorkEfficiencyDistribution');
insert into ec_role_roleaction_link values ('admin','getChartMyCustomer');
insert into ec_role_roleaction_link values ('admin','getChartWorkload');
insert into ec_role_roleaction_link values ('admin','getChartWeightedWorkload');
insert into ec_role_roleaction_link values ('admin','getChartAbandon');
insert into ec_role_roleaction_link values ('admin','getChartQueueEntryTimes');
insert into ec_role_roleaction_link values ('admin','getChartWait');
insert into ec_role_roleaction_link values ('admin','getChartQueueWaitAndEntryTimes');
insert into ec_role_roleaction_link values ('admin','getChartSMSendQuantity');
insert into ec_role_roleaction_link values ('admin','getChartQueueWaitDistribution');
insert into ec_role_roleaction_link values ('admin','getChartAgentAssessment');
insert into ec_role_roleaction_link values ('admin','getChartAgentMinLoginTimeAndMaxLogoutTime');
insert into ec_role_roleaction_link values ('admin','getChartCustomerSatisfactionInvestigation');
insert into ec_role_roleaction_link values ('admin','getChartManDay');
insert into ec_role_roleaction_link values ('admin','getChartAgentLoginLogoutDetail');
insert into ec_role_roleaction_link values ('admin','getChartAgentPauseDetail');
insert into ec_role_roleaction_link values ('admin','getChartQueueEnrtyAndAbandonDistinct');
insert into ec_role_roleaction_link values ('admin','getChartQueueEnrtyAndAnswerIVRLog');
insert into ec_role_roleaction_link values ('admin','getChartOverallReport');
insert into ec_role_roleaction_link values ('admin','getChartCSRSatisfactionReport');
insert into ec_role_roleaction_link values ('admin','getChartQueueAbandonDetail');
insert into ec_role_roleaction_link values ('admin','getChartConcurrent');
insert into ec_role_roleaction_link values ('admin','getChartIVRQuit');
insert into ec_role_roleaction_link values ('admin','getChartOperationalData');
insert into ec_role_roleaction_link values ('admin','getChart30sDetail');

insert into ec_role_roleaction_link values ('admin','getQueueStatus');
insert into ec_role_roleaction_link values ('admin','getSipStatus');
insert into ec_role_roleaction_link values ('admin','dialTaskLogsGet');
insert into ec_role_roleaction_link values ('admin','smSend');
insert into ec_role_roleaction_link values ('admin','shiftConfigGet');
insert into ec_role_roleaction_link values ('admin','shiftConfigAdd');
insert into ec_role_roleaction_link values ('admin','shiftConfigUpdate');
insert into ec_role_roleaction_link values ('admin','shiftConfigDelete');

insert into ec_role_roleaction_link values ('admin','faqCount');
insert into ec_role_roleaction_link values ('admin','faqDetail');
insert into ec_role_roleaction_link values ('admin','fenduanHour');
insert into ec_role_roleaction_link values ('admin','fenduanDay');
insert into ec_role_roleaction_link values ('admin','fenduanMonth');



insert into ec_department values ('default','default');
insert into ec_department values ('root','root');

--insert into ec_role_department_link values ('admin','default');

insert into ec_user (username,password,email,name,rolename,departmentname) values ('admin','123456','admin@localhost','admin','admin','default');


CREATE INDEX "ec_sm_task_idx_of_status" ON "public"."ec_sm_task"
  USING btree ("status");

CREATE INDEX "ec_sm_task_idx_of_accountid" ON "public"."ec_sm_task"
  USING btree ("accountid");
  
CREATE INDEX "ec_sm_task_idx_of_msgid" ON "public"."ec_sm_task"
  USING btree ("msgid");
  
CREATE INDEX "ec_sm_task_idx_of_mobile" ON "public"."ec_sm_task"
  USING btree ("mobile");
  
CREATE INDEX "ec_sm_task_idx_of_timestamp" ON "public"."ec_sm_task"
  USING btree ("timestamp");

CREATE INDEX "ec_my_customer_log_idx" ON "public"."ec_my_customer_log"
  USING btree ("date");

CREATE INDEX "ec_my_customer_log_idx1" ON "public"."ec_my_customer_log"
  USING btree ("username");

CREATE INDEX "ec_dial_task_item_idx" ON "public"."ec_dial_task_item"
  USING btree ("owner");
  
CREATE INDEX "ec_dial_task_item_idx1" ON "public"."ec_dial_task_item"
  USING btree ("status");

CREATE INDEX "ec_ivr_log_idx" ON "public"."ec_ivr_log"
  USING btree ("date");
  
insert into ec_config  values ('crm_sms_enable','true');
insert into ec_config  values ('crm_pop_server_secret','pa55w0rd');

