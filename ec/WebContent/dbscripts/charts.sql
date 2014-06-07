--综合工作量select name , count(*), round(sum(t.duration)/60,2) as workload from cdr t where name<>'0' and username<>'0' and disposition='ANSWER' and dcontext in ('incoming','outgoing') and t.calldate >= '2011-07-01 00:00:00' and t.calldate <= '2011-07-31 23:59:59' group by name order by name desc;--呼入话务量  QueueIncomingselect name , count(*) as workload from cdr t where fenji is not null and name<>'0' and username<>'0' and disposition='ANSWER' and dcontext='incoming' and t.calldate >= '2011-07-01 00:00:00' and t.calldate <= '2011-07-31 23:59:59' group by name order by name desc;--呼出话务量select t.src , count(*) as workload, round(sum(t.duration)/60,2) from cdr t where name<>'0' and username<>'0' and disposition='ANSWER' and dcontext='outgoing' and src in ('8888','8007','8006','8005','8004','8003','8002','8001') and t.calldate >= '2011-07-01 00:00:00' and t.calldate <= '2011-07-31 23:59:59' group by t.src order by t.src desc;--包含[进入队列后放弃的]和[尚未进入队列就放弃的]select substring(t.calldate from 0 for 11),count(*) from cdr t where disposition='NO ANSWER'  and t.calldate >= '2011-07-01 00:00:00' and t.calldate <= '2011-07-31 23:59:59' group by substring(t.calldate from 0 for 11) order by substring(t.calldate from 0 for 11);select * from cdr t where disposition='NO ANSWER' and t.calldate >= '2011-07-13 00:00:00' and t.calldate <= '2011-07-13 23:59:59';--包含[进入队列后放弃的]select substring(t.datereceived from 0 for 11),count(*) from ec_queue_caller_abandon_event_log t where t.datereceived>= '2011-07-01 00:00:00' and t.datereceived <= '2011-07-31 23:59:59' group by substring(t.datereceived from 0 for 11) order by substring(t.datereceived from 0 for 11);select * from ec_queue_caller_abandon_event_log t where t.datereceived>= '2011-07-13 00:00:00' and t.datereceived <= '2011-07-13 23:59:59';----------------------------------------------------用户登录时长、置忙时长、呼入呼出工作量--------------------------------------------------select t.date as date,ec_user.username as username,ec_user.name as name,to_char(logintimelength, 'HH24:MI:SS') as logintimelength,to_char(pausetimelength, 'HH24:MI:SS') as pausetimelength,round((cast (EXTRACT(EPOCH from pausetimelength)/EXTRACT(EPOCH from logintimelength) as numeric))*100,2) as pauserate,incoming_workload_count,incoming_workload_billsec, (incoming_workload_billsec/incoming_workload_count) as incoming_workload_avg,outgoing_workload_count,outgoing_workload_billsec,(outgoing_workload_billsec/outgoing_workload_count) as outgoing_workload_avg, incoming_workload_count+outgoing_workload_count as total_workload_count,incoming_workload_billsec+outgoing_workload_billsec as total_workload_billsec,(incoming_workload_billsec+outgoing_workload_billsec)/(incoming_workload_count+outgoing_workload_count) as total_workload_avgfrom ec_user full join(select CASE WHEN (t1.username is not null) THEN t1.username ELSE t2.username END,CASE WHEN (t1.date is not null) THEN t1.date ELSE t2.date END,logintimelength,pausetimelength,incoming_workload_count,incoming_workload_billsec,outgoing_workload_count,outgoing_workload_billsec from (-------yellow------select username,date,logintimelength,pausetimelength from(select CASE WHEN(t_login.username is not null) THEN t_login.username ELSE t_pause.username END,CASE WHEN (t_login.date is not null) THEN t_login.date ELSE t_pause.date END ,t_login.timelength as logintimelength,t_pause.timelength as pausetimelengthfrom ec_report_user_login_timelength t_login full join ec_report_queue_user_pause t_pause on t_login.username=t_pause.username and t_login.date=t_pause.date where (t_login.date>='2011-11-01 00:00:00' and t_login.date<='2011-11-30 23:59:59') or (t_pause.date>='2011-11-01 00:00:00' and t_pause.date<='2011-11-30 23:59:59')) as onlinetime-------yellow------) as t1FULL JOIN(-----  red -----select username,date,incoming_workload_count,incoming_workload_billsec,outgoing_workload_count,outgoing_workload_billsec from (select CASE WHEN (t_li.username is not null) THEN t_li.username ELSE t_lo.username END,CASE WHEN (t_li.date is not null) THEN t_li.date ELSE t_lo.date END ,t_li.count as incoming_workload_count,t_li.billsec as incoming_workload_billsec,t_lo.count as outgoing_workload_count,t_lo.billsec as outgoing_workload_billsec from ec_report_user_incoming_workload t_li full join ec_report_user_outgoing_workload t_lo on t_li.username=t_lo.username and t_li.date=t_lo.date where (t_li.date>='2011-11-01 00:00:00' and t_li.date<='2011-11-30 23:59:59') or (t_lo.date>='2011-11-01 00:00:00' and t_lo.date<='2011-11-30 23:59:59')) as workload -----  red -----) as t2on t1.username=t2.username and t1.date=t2.date ) as ton ec_user.username=t.usernamewhere ec_user.username is not null and t.date is not nullorder by date,username;-------------------------------------------------------------用户登录时长、置忙时长、呼入呼出工作量--------------------------------------------------