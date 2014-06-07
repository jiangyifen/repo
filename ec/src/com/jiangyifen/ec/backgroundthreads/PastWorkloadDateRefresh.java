package com.jiangyifen.ec.backgroundthreads;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.beans.Workload;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.ShareData;

public class PastWorkloadDateRefresh extends Thread {

	private static final int period = 3600000 * 24; // 24 hours

	private final Log logger = LogFactory.getLog(getClass());

	public PastWorkloadDateRefresh() {

		this.setDaemon(true);
		this.setName("PastWorkloadDateRefresh");
		this.start();
	}

	private void refreshWorkload() {
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			String url = Config.props.getProperty("pg_url");
			String db_username = Config.props.getProperty("pg_username");
			String db_password = Config.props.getProperty("pg_password");

			Class.forName("org.postgresql.Driver");

			con = DriverManager.getConnection(url, db_username, db_password);

			String sql = "";
			
			//本人前30天平均工作量：
			//意向客户			
			sql = "select username,round(sum(count)/26,2) as c from (select username,to_date(date,'YYYY-MM-DD') as d, count(*) as count from ec_my_customer_log where date>=CURRENT_DATE-INTERVAL '30 days' and username<>'0' and department<>'UNKNOW' and department<>'' group by username, d) as t group by t.username";
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			HashMap<String, Double> temp0 = new HashMap<String, Double>();
			while (rs.next()) {
				String username = rs.getString(1);
				Double customerCount = rs.getDouble(2);
				temp0.put(username, customerCount);
			}
			//呼叫次数和时长
			sql = "select username,round(sum(count)/26,2) as c,round(avg(billsec),2) as b from (select username,to_date(calldate,'YYYY-MM-DD') as date, count(*) as count, round(sum(billsec)/60,2) as billsec from cdr where disposition='ANSWER' and calldate>=CURRENT_DATE-INTERVAL '30 days' and username<>'0' and dpmt<>'UNKNOW' and dpmt<>'' group by username, date order by billsec desc ) as t group by t.username";
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			HashMap<String, Workload> temp1 = new HashMap<String, Workload>();
			while (rs.next()) {
				String username = rs.getString(1);
								
				Workload workload = new Workload();
				workload.setCount(rs.getInt(2));
				workload.setDuration(rs.getDouble(3));
				Double customerCount = temp0.get(username);
				if(customerCount!=null){
					workload.setCustomerCount(customerCount);
				}
				temp1.put(username, workload);
			}
			ShareData.usernameAndAvgWorkload = temp1;
			
			//部门前30天最高呼叫次数
			
			sql = "select dpmt,max(count)   from (select dpmt,username,to_date(calldate,'YYYY-MM-DD') as date, count(*)                              as count     from cdr where disposition='ANSWER' and calldate>=CURRENT_DATE-INTERVAL '30 days' and username<>'0' and dpmt<>'UNKNOW' and dpmt<>'' group by dpmt,date,username order by dpmt,  count desc ) as t group by t.dpmt";
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			HashMap<String,Double> temp2 = new HashMap<String,Double>();
			while(rs.next()){
				String departmentName = rs.getString(1);
				double count = rs.getDouble(2);
				temp2.put(departmentName, count);
			}
			ShareData.dpmtTopCount=temp2;
			
			//部门前30天最高呼叫时长
			sql = "select dpmt,max(billsec) from (select dpmt,username,to_date(calldate,'YYYY-MM-DD') as date, round(sum(billsec)/60,2)              as billsec   from cdr where disposition='ANSWER' and calldate>=CURRENT_DATE-INTERVAL '30 days' and username<>'0' and dpmt<>'UNKNOW' and dpmt<>'' group by dpmt,date,username order by dpmt,billsec desc ) as t group by t.dpmt";
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			HashMap<String,Double> temp3 = new HashMap<String,Double>();
			while(rs.next()){
				String departmentName = rs.getString(1);
				double billsec = rs.getDouble(2);
				temp3.put(departmentName, billsec);
			}
			ShareData.dpmtTopBillsec=temp3;
			
			//部门前30天最高加权工作量
			sql="select dpmt,max(workload) from (select dpmt,username,to_date(calldate,'YYYY-MM-DD') as date, round((count(*) + sum(billsec)/60)/2,2) as workload from cdr where disposition='ANSWER' and calldate>=CURRENT_DATE-INTERVAL '30 days' and username<>'0' and dpmt<>'UNKNOW' and dpmt<>'' group by dpmt,date,username order by dpmt,workload desc ) as t group by t.dpmt";
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			HashMap<String,Double> temp4 = new HashMap<String,Double>();
			while(rs.next()){
				String departmentName = rs.getString(1);
				double workload = rs.getDouble(2);
				temp4.put(departmentName, workload);
			}
			ShareData.dpmtTopWorkload=temp4;
			
			//部门前30天最高意向客户数量
			sql="select department,max(workload) from (select department,username,to_date(date,'YYYY-MM-DD') as d, count(*) as workload from ec_my_customer_log where date>=CURRENT_DATE-INTERVAL '30 days' and username<>'0' and department<>'UNKNOW' and department<>'' group by department,d,username order by department,workload desc) as t group by t.department";
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			HashMap<String,Double> temp5 = new HashMap<String,Double>();
			while(rs.next()){
				String departmentName = rs.getString(1);
				double workload = rs.getDouble(2);
				temp5.put(departmentName, workload);
			}
			ShareData.dpmtTopCustomerCount=temp5;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}finally {
			try {
				rs.close();
				statement.close();
				con.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}

		}
	}

	public void run() {
		
		refreshWorkload();
		
		while (true) {
			try {
				
				Date current = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("HH");
				String h = sdf.format(current);
				Integer hour = Integer.valueOf(h);
				if (hour == 5 || hour == 6) {

					refreshWorkload();
				}

				Thread.sleep(period);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}

	}
}
