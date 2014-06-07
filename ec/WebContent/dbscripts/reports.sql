--用户每日登录在线时长
CREATE TABLE "public"."ec_report_user_login_timelength" (
  "id" BIGSERIAL, 
  "username" VARCHAR(32) NOT NULL, 
  "date" DATE NOT NULL, 
  "timelength" INTERVAL(6) NOT NULL, 
  CONSTRAINT "ec_report_user_login_timelength_pkey" PRIMARY KEY("id")
) WITHOUT OIDS;

CREATE INDEX "ec_report_user_login_timelength_idx" ON "public"."ec_report_user_login_timelength"
  USING btree ("date");
  
--用户每日置忙时长
CREATE TABLE "public"."ec_report_queue_user_pause" (
  "id" BIGSERIAL, 
  "username" VARCHAR(32) NOT NULL, 
  "date" DATE NOT NULL, 
  "timelength" INTERVAL(6) NOT NULL, 
  CONSTRAINT "ec_report_queue_user_pause_pkey" PRIMARY KEY("id")
) WITHOUT OIDS;

CREATE INDEX "ec_report_queue_user_pause_idx" ON "public"."ec_report_queue_user_pause"
  USING btree ("date");
  
--用户每日呼出工作量
CREATE TABLE "public"."ec_report_user_outgoing_workload" (
  "id" BIGSERIAL, 
  "username" VARCHAR(32) NOT NULL, 
  "date" DATE NOT NULL, 
  "hour" VARCHAR(16) NOT NULL, 
  "count" INTEGER NOT NULL, 
  "billsec" INTEGER NOT NULL, 
  CONSTRAINT "ec_report_user_outgoing_workload_pkey" PRIMARY KEY("id")
) WITHOUT OIDS;

CREATE INDEX "ec_report_user_outgoing_workload_idx" ON "public"."ec_report_user_outgoing_workload"
  USING btree ("date");

--用户每日呼入工作量
CREATE TABLE "public"."ec_report_user_incoming_workload" (
  "id" BIGSERIAL, 
  "username" VARCHAR(32) NOT NULL, 
  "date" DATE NOT NULL, 
  "hour" VARCHAR(16) NOT NULL,
  "count" INTEGER NOT NULL, 
  "billsec" INTEGER NOT NULL, 
  CONSTRAINT "ec_report_user_incoming_workload_pkey" PRIMARY KEY("id")
) WITHOUT OIDS;

CREATE INDEX "ec_report_user_incoming_workload_idx" ON "public"."ec_report_user_incoming_workload"
  USING btree ("date");


--Queue Entry Event 去重（同一个callerid，同一个channel反复进入queue算一次）
  CREATE TABLE "public"."ec_report_queue_entry_event_log_distinct" (
  "id" BIGSERIAL, 
  "queue" VARCHAR(255) NOT NULL, 
  "datereceived" TIMESTAMP WITHOUT TIME ZONE NOT NULL, 
  "callerid" VARCHAR(255) NOT NULL, 
  "calleridname" VARCHAR(255) NOT NULL, 
  "channel" VARCHAR(255) NOT NULL, 
  "position" INTEGER NOT NULL, 
  "wait" BIGINT NOT NULL, 
  CONSTRAINT "ec_report_queue_entry_event_log_distinct_pkey" PRIMARY KEY("id")
) WITHOUT OIDS;

--Queue Caller Abandon Event 去重（同一个uniqueid反复abandon算一次abandon）
CREATE TABLE "public"."ec_report_queue_caller_abandon_event_log_distinct" (
  "id" BIGSERIAL, 
  "queue" VARCHAR(255), 
  "channel" VARCHAR(255), 
  "count" INTEGER, 
  "datereceived" TIMESTAMP WITHOUT TIME ZONE, 
  "position" INTEGER, 
  "originalposition" INTEGER, 
  "holdtime" INTEGER, 
  "uniqueid" VARCHAR(255), 
  CONSTRAINT "ec_report_queue_caller_abandon_event_log_distinct_pkey" PRIMARY KEY("id")
) WITHOUT OIDS;

--系统并发数
CREATE TABLE "public"."ec_report_concurrent" (
  "id" BIGSERIAL, 
  "begintime" TIMESTAMP WITHOUT TIME ZONE,
  "endtime" TIMESTAMP WITHOUT TIME ZONE,
  "min" INTEGER, 
  "max" INTEGER, 
  CONSTRAINT "ec_report_concurrent_pkey" PRIMARY KEY("id")
) WITHOUT OIDS;

CREATE INDEX "ec_report_concurrent_idx_begintime" ON "public"."ec_report_concurrent"
  USING btree ("begintime");
  
  CREATE INDEX "ec_report_concurrent_idx_endtime" ON "public"."ec_report_concurrent"
  USING btree ("endtime");


--link unlink 日志
CREATE INDEX "ec_link_log_idx_date" ON "public"."ec_link_log"
  USING btree ("date");
CREATE INDEX "ec_link_log_idx_uniqueid" ON "public"."ec_link_log"
  USING btree ("uniqueid");
  

