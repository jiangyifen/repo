drop table extensions_conf;

CREATE TABLE extensions_conf (
id serial NOT NULL,
context character varying(20) DEFAULT '' NOT NULL,
exten character varying(20) DEFAULT '' NOT NULL,
priority smallint DEFAULT 0 NOT NULL,
app character varying(20) DEFAULT '' NOT NULL,
appdata character varying(128)
);

drop table cdr;
CREATE TABLE cdr (
calldate timestamp with time zone DEFAULT now() NOT NULL,
clid character varying(80) DEFAULT '' NOT NULL,
src character varying(80) DEFAULT '' NOT NULL,
dst character varying(80) DEFAULT '' NOT NULL,
dcontext character varying(80) DEFAULT '' NOT NULL,
channel character varying(80) DEFAULT '' NOT NULL,
dstchannel character varying(80) DEFAULT '' NOT NULL,
lastapp character varying(80) DEFAULT '' NOT NULL,
lastdata character varying(80) DEFAULT '' NOT NULL,
duration bigint DEFAULT 0::bigint NOT NULL,
billsec bigint DEFAULT 0::bigint NOT NULL,
disposition character varying(45) DEFAULT '' NOT NULL,
amaflags bigint DEFAULT 0::bigint NOT NULL,
accountcode character varying(20) DEFAULT '' NOT NULL,
uniqueid character varying(32) DEFAULT '' NOT NULL,
userfield character varying(255) DEFAULT '' NOT NULL,
username character varying(32) DEFAULT '0',
name character varying(32) DEFAULT '0',
sync character varying(1) DEFAULT '0'
);

drop table sip_conf;
CREATE TABLE "public"."sip_conf" (
  "id" SERIAL, 
  "name" VARCHAR(80) DEFAULT ''::character varying NOT NULL, 
  "host" VARCHAR(31) DEFAULT ''::character varying NOT NULL, 
  "nat" VARCHAR(5) DEFAULT 'no'::character varying NOT NULL, 
  "type" VARCHAR DEFAULT 'friend'::character varying NOT NULL, 
  "accountcode" VARCHAR(20), 
  "amaflags" VARCHAR(7), 
  "call-limit" SMALLINT, 
  "callgroup" VARCHAR(10), 
  "callerid" VARCHAR(80), 
  "cancallforward" VARCHAR(3) DEFAULT 'yes'::character varying, 
  "canreinvite" VARCHAR(3) DEFAULT 'yes'::character varying, 
  "context" VARCHAR(80), 
  "defaultip" VARCHAR(15), 
  "dtmfmode" VARCHAR(7), 
  "fromuser" VARCHAR(80), 
  "fromdomain" VARCHAR(80), 
  "insecure" VARCHAR(4), 
  "language" VARCHAR(2), 
  "mailbox" VARCHAR(50), 
  "md5secret" VARCHAR(80), 
  "permit" VARCHAR(95), 
  "deny" VARCHAR(95), 
  "mask" VARCHAR(95), 
  "musiconhold" VARCHAR(100), 
  "pickupgroup" VARCHAR(10), 
  "qualify" VARCHAR(3), 
  "regexten" VARCHAR(80) DEFAULT ''::character varying NOT NULL, 
  "restrictcid" VARCHAR(1), 
  "rtptimeout" VARCHAR(3), 
  "rtpholdtimeout" VARCHAR(3), 
  "secret" VARCHAR(80), 
  "setvar" VARCHAR(100), 
  "disallow" VARCHAR(100) DEFAULT 'all'::character varying, 
  "allow" VARCHAR(100) DEFAULT 'all'::character varying, 
  "fullcontact" VARCHAR(80) DEFAULT ''::character varying NOT NULL, 
  "ipaddr" VARCHAR(15) DEFAULT ''::character varying NOT NULL, 
  "port" VARCHAR(5) DEFAULT ''::character varying NOT NULL, 
  "regserver" VARCHAR(100), 
  "regseconds" BIGINT DEFAULT (0)::bigint NOT NULL, 
  "lastms" INTEGER DEFAULT (-1) NOT NULL, 
  "username" VARCHAR(80) DEFAULT ''::character varying NOT NULL, 
  "defaultuser" VARCHAR(80) DEFAULT ''::character varying NOT NULL, 
  "subscribecontext" VARCHAR(80), 
  "dpmt" VARCHAR(100), 
  "loginusername" VARCHAR(16) DEFAULT '0'::character varying NOT NULL,
  CONSTRAINT "sip_conf_pkey" PRIMARY KEY("id"), 
  CONSTRAINT "sip_conf_type_check" CHECK (((("type")::text = 'user'::text) OR (("type")::text = 'peer'::text)) OR (("type")::text = 'friend'::text))
) WITHOUT OIDS;

drop table meetme;
CREATE TABLE "public"."meetme" (
  "confno" VARCHAR(80) DEFAULT 0 NOT NULL, 
  "pin" VARCHAR(20), 
  "adminpin" VARCHAR(20), 
  "members" INTEGER DEFAULT 0 NOT NULL, 
  CONSTRAINT "meetme_pkey" PRIMARY KEY("confno")
) WITHOUT OIDS;

drop table voicemail_users;
CREATE TABLE voicemail_users (
id serial NOT NULL,
customer_id bigint DEFAULT (0)::bigint NOT NULL,
context character varying(50) DEFAULT '' NOT NULL,
mailbox bigint DEFAULT (0)::bigint NOT NULL,
"password" character varying(4) DEFAULT '0' NOT NULL,
fullname character varying(50) DEFAULT '' NOT NULL,
email character varying(50) DEFAULT '' NOT NULL,
pager character varying(50) DEFAULT '' NOT NULL,
stamp timestamp(6) without time zone NOT NULL
);

drop table queue_table;
CREATE TABLE queue_table (
name varchar(128),
musiconhold varchar(128),
announce varchar(128),
context varchar(128),
timeout int8,
monitor_join bool,
monitor_format varchar(128),
queue_youarenext varchar(128),
queue_thereare varchar(128),
queue_callswaiting varchar(128),
queue_holdtime varchar(128),
queue_minutes varchar(128),
queue_seconds varchar(128),
queue_lessthan varchar(128),
queue_thankyou varchar(128),
queue_reporthold varchar(128),
announce_frequency int8,
announce_round_seconds int8,
announce_holdtime varchar(128),
retry int8,
wrapuptime int8,
maxlen int8,
servicelevel int8,
strategy varchar(128),
joinempty varchar(128),
leavewhenempty varchar(128),
eventmemberstatus bool,
eventwhencalled bool,
reportholdtime bool,
memberdelay int8,
weight int8,
timeoutrestart bool,
setinterfacevar bool,
autopause VARCHAR(128),
description VARCHAR(128),
periodic_announce_frequency int8, 
PRIMARY KEY (name)
) WITHOUT OIDS;

drop table queue_member_table;
CREATE TABLE queue_member_table
(
queue_name varchar(128),
interface varchar(128),
penalty int8,
PRIMARY KEY (queue_name, interface)
) WITHOUT OIDS;

GRANT ALL ON TABLE cdr TO asterisk;
GRANT ALL ON TABLE extensions_conf TO asterisk;
GRANT ALL ON TABLE sip_conf TO asterisk;
GRANT ALL ON TABLE voicemail_users TO asterisk;
GRANT ALL ON TABLE queue_member_table TO asterisk;
GRANT ALL ON TABLE queue_table TO asterisk;

CREATE INDEX "cdr_idx_uniqueid" ON "public"."cdr"
  USING btree ("uniqueid");

CREATE INDEX "cdr_idx_calldate" ON "public"."cdr"
  USING btree ("calldate");
  
CREATE INDEX "cdr_idx_username" ON "public"."cdr"
  USING btree ("username");
  
CREATE INDEX "sip_conf_idx" ON "public"."sip_conf"
  USING btree ("name");

  
  
----- 号码归属地表 ---------
CREATE TABLE "public"."ec_location" (
  "id" BIGSERIAL, 
  "num" VARCHAR(32), 
  "code" VARCHAR(32), 
  "location" VARCHAR(32), 
  "type" VARCHAR(32), 
  CONSTRAINT "ec_location_pkey" PRIMARY KEY("id")
) WITHOUT OIDS;

CREATE UNIQUE INDEX "ec_location_idx" ON "public"."ec_location"
  USING btree ("num");
----- 号码归属地表 ---------

  
  
  
--------------------------eccrm----------------------------------

  CREATE TABLE "public"."tb_ctiserver" (
  "id" BIGSERIAL, 
  "ctiserver_hostname" VARCHAR(20), 
  "ctiserver_port" VARCHAR(4), 
  "ctiserver_sn" VARCHAR(50), 
  "ctiserver_en" INTEGER DEFAULT 0, 
  "loginurl" TEXT, 
  "pgsqlhost" VARCHAR(20), 
  "pgsqluser" VARCHAR(20), 
  "pgsqlpwd" VARCHAR(20), 
  "pgsqldb" VARCHAR(20), 
  "ftphost" VARCHAR(20), 
  "ftpuser" VARCHAR(20), 
  "ftppwd" VARCHAR(20), 
  "echost" VARCHAR(20), 
  "ecport" VARCHAR(10), 
  "ecsecret" VARCHAR(20), 
  "popurl" VARCHAR(250)
) WITHOUT OIDS;

INSERT INTO "public"."tb_ctiserver" ("id", "ctiserver_hostname", "ctiserver_port", "ctiserver_sn", "ctiserver_en", "loginurl", "pgsqlhost", "pgsqluser", "pgsqlpwd", "pgsqldb", "ftphost", "ftpuser", "ftppwd", "echost", "ecport", "ecsecret", "popurl")
VALUES (1, '192.168.1.252', '9100', NULL, 0, 'http://192.168.1.252:8080/', '192.168.1.252', 'asterisk', 'asterisk', 'asterisk', '127.0.0.1', NULL, NULL, '192.168.1.252', '18080', 'pa55w0rd', 'http://192.168.1.252:8080/');




CREATE TABLE "public"."tb_file" (
  "nfile_id" BIGSERIAL, 
  "nfile_srcfname" VARCHAR(128), 
  "nfile_dstfpath" VARCHAR(10), 
  "nfile_dstfname" VARCHAR(128), 
  "nfile_memo" TEXT, 
  CONSTRAINT "tb_file_pkey" PRIMARY KEY("nfile_id")
) WITHOUT OIDS;

CREATE TABLE "public"."tb_notice" (
  "notice_id" VARCHAR(20) NOT NULL, 
  "notice_title" VARCHAR(128), 
  "notice_type" VARCHAR(10), 
  "notice_sendtime" TIMESTAMP WITH TIME ZONE, 
  "notice_context" TEXT, 
  "notice_outtime" TIMESTAMP WITH TIME ZONE, 
  "notice_memo" TEXT, 
  CONSTRAINT "tb_notice_pkey" PRIMARY KEY("notice_id")
) WITHOUT OIDS;

CREATE TABLE "public"."tb_noticefile" (
  "nf_id" BIGSERIAL, 
  "nf_noticeid" VARCHAR(20), 
  "nf_fileid" INTEGER, 
  CONSTRAINT "tb_noticefile_pkey" PRIMARY KEY("nf_id")
) WITHOUT OIDS;

CREATE TABLE "public"."tb_noticerelation" (
  "nr_id" SERIAL, 
  "nr_noticeid" VARCHAR(20) NOT NULL, 
  "nr_sender" VARCHAR(20) NOT NULL, 
  "nr_receiver" VARCHAR(20) NOT NULL, 
  "nr_state" VARCHAR(10) DEFAULT '未查看'::character varying NOT NULL, 
  CONSTRAINT "tb_noticerelation_pkey" PRIMARY KEY("nr_id")
) WITH OIDS;



  CREATE TABLE "public"."tb_repository" (
  "repository_id" BIGSERIAL, 
  "repository_categoryid" INTEGER, 
  "repository_title" VARCHAR(250), 
  "repository_content" TEXT, 
  CONSTRAINT "tb_repository_pkey" PRIMARY KEY("repository_id")
) WITHOUT OIDS;
  
   CREATE TABLE "public"."subtb_repository_category" (
  "id" BIGSERIAL, 
  "category_parentid" INTEGER, 
  "category_name" VARCHAR(250), 
  "category_flag" VARCHAR(1) DEFAULT '0'::character varying, 
  CONSTRAINT "subtb_repository_category_pkey" PRIMARY KEY("id")
) WITHOUT OIDS;


  CREATE TABLE "public"."eccrm_dialtask" (
  "id" BIGSERIAL, 
  "name" VARCHAR(128) NOT NULL, 
  "owner" VARCHAR(32), 
  "ownername" VARCHAR(32), 
  "importdate" TIMESTAMP WITHOUT TIME ZONE, 
  "begindate" TIMESTAMP WITHOUT TIME ZONE, 
  "enddate" TIMESTAMP WITHOUT TIME ZONE, 
  "count" BIGINT DEFAULT 0, 
  "state" INTEGER DEFAULT 0, 
  CONSTRAINT "eccrm_dialtask_pkey" PRIMARY KEY("id")
) WITHOUT OIDS;


CREATE TABLE "public"."eccrm_dialtask_item" (
  "id" BIGSERIAL, 
  "dialtaskid" BIGINT NOT NULL, 
  "phonenumber" VARCHAR(32) NOT NULL, 
  "phonenumber2" VARCHAR(32), 
  "name" VARCHAR(32), 
  "sex" VARCHAR(8), 
  "birthday" VARCHAR(32), 
  "company" VARCHAR(255), 
  "province" VARCHAR(32), 
  "city" VARCHAR(32), 
  "address" VARCHAR(255), 
  "remark" VARCHAR(1024), 
  "owner" VARCHAR(32), 
  "isfinished" BOOLEAN, 
  "isanswered" BOOLEAN, 
  "result" VARCHAR(32), 
  CONSTRAINT "eccrm_dialtask_item_pkey" PRIMARY KEY("id")
) WITHOUT OIDS;

CREATE INDEX "eccrm_dialtask_item_idx_owner" ON "public"."eccrm_dialtask_item"
  USING btree ("owner");

CREATE INDEX "eccrm_dialtask_item_idx_phonenumber" ON "public"."eccrm_dialtask_item"
  USING btree ("phonenumber");

CREATE INDEX "eccrm_dialtask_item_idx_result" ON "public"."eccrm_dialtask_item"
  USING btree ("result");
  
  
  CREATE TABLE "public"."eccrm_dialtask_resulttype" (
  "id" BIGSERIAL, 
  "resulttype" VARCHAR(255)
) WITHOUT OIDS;


CREATE TABLE "public"."eccrm_dialtask_service_record" (
  "id" BIGSERIAL, 
  "dialtaskid" BIGINT NOT NULL, 
  "dialtaskitemid" BIGINT NOT NULL, 
  "name" VARCHAR(32), 
  "tel" VARCHAR(32), 
  "date" TIMESTAMP WITHOUT TIME ZONE, 
  "isanswered" BOOLEAN, 
  "result" VARCHAR(32), 
  "owner" VARCHAR(32), 
  "remark" VARCHAR(1024), 
  CONSTRAINT "eccrm_dialtask_service_record_pkey" PRIMARY KEY("id")
) WITHOUT OIDS;

CREATE INDEX "eccrm_dialtask_service_record_idx" ON "public"."eccrm_dialtask_service_record"
  USING btree ("dialtaskitemid");

CREATE INDEX "eccrm_dialtask_service_record_idx1" ON "public"."eccrm_dialtask_service_record"
  USING btree ("dialtaskid");
--------------------------eccrm----------------------------------
  
  
  
  