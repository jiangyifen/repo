package com.jiangyifen.ec.backgroundthreads;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.beans.SipStatus;
import com.jiangyifen.ec.beans.Workload;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.ShareData;

public class TodayWorkloadDateRefresh extends Thread {

	private static final int period = 1000 * 60 * 5;

	private final Log logger = LogFactory.getLog(getClass());

	public TodayWorkloadDateRefresh() {

		this.setDaemon(true);
		this.setName("TodayWorkloadDateRefresh");
		this.start();
	}

	private void refreshWorkload() {
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;

		try {
			String url = Config.props.getProperty("pg_url");
			String db_username = Config.props.getProperty("pg_username");
			String db_password = Config.props.getProperty("pg_password");

			Class.forName("org.postgresql.Driver");

			con = DriverManager.getConnection(url, db_username, db_password);

			// 统计每个分机当天的呼入呼出量
			String allSipNameString = "";
			for (String sipName : ShareData.allSipName) {
				allSipNameString = allSipNameString + "'" + sipName + "',";
			}
			allSipNameString = allSipNameString + "''";

			String sql1 = "";
			sql1 = "select src, count(*) from cdr where src in ("
					+ allSipNameString
					+ ") and calldate>CURRENT_DATE group by src";
			statement = con.prepareStatement(sql1);
			rs1 = statement.executeQuery();
			while (rs1.next()) {
				String src = rs1.getString(1);
				long countCallout = rs1.getLong(2);
				SipStatus s = ShareData.sipStatusMap.get(src);
				if (s != null) {
					s.setCountCallout(countCallout);
				}
			}
			String sql2 = "";
			sql2 = "select fenji, count(*) from cdr where fenji in ("
					+ allSipNameString
					+ ") and calldate>CURRENT_DATE group by fenji;";
			statement = con.prepareStatement(sql2);
			rs2 = statement.executeQuery();
			while (rs2.next()) {
				String dst = rs2.getString(1);
				long countCallin = rs2.getLong(2);
				SipStatus s = ShareData.sipStatusMap.get(dst);
				if (s != null) {
					s.setCountCallin(countCallin);
				}
			}

			// 统计每个用户当日的工作量
			String sql3 = "";
			// 意向客户
			HashMap<String, Double> temp0 = new HashMap<String, Double>();
			sql3 = "select username, count(*) from ec_my_customer_log where date>CURRENT_DATE group by username";
			statement = con.prepareStatement(sql3);
			rs3 = statement.executeQuery();
			while (rs3.next()) {
				String username = rs3.getString(1);
				Double customerCount = rs3.getDouble(2);
				temp0.put(username, customerCount);
			}
			// 呼叫次数和时长
			HashMap<String, Workload> temp1 = new HashMap<String, Workload>();

			sql3 = "select username, count(*), round(sum(billsec)/60,2) from cdr where dcontext='incoming' and disposition='ANSWER' and calldate>CURRENT_DATE group by username";
			statement = con.prepareStatement(sql3);
			rs3 = statement.executeQuery();
			while (rs3.next()) {
				String username = rs3.getString(1);
				if (username == null || username.equals("")) {
					continue;
				}

				Workload workload = new Workload();
				workload.setIncount(rs3.getInt(2));
				workload.setInduration(rs3.getDouble(3));
				Double customerCount = temp0.get(username);
				if (customerCount != null) {
					workload.setCustomerCount(customerCount);
				}
				temp1.put(username, workload);

			}

			sql3 = "select username, count(*), round(sum(billsec)/60,2) from cdr where dcontext='outgoing' and disposition='ANSWER' and calldate>CURRENT_DATE group by username";
			statement = con.prepareStatement(sql3);
			rs4 = statement.executeQuery();
			while (rs4.next()) {
				String username = rs4.getString(1);
				if (username == null || username.equals("")) {
					continue;
				}

				Workload workload = temp1.get(username);
				if (workload == null) {

					workload = new Workload();

					Double customerCount = temp0.get(username);
					if (customerCount != null) {
						workload.setCustomerCount(customerCount);
					}
					temp1.put(username, workload);
				}

				workload.setOutcount(rs4.getInt(2));
				workload.setOutduration(rs4.getDouble(3));
			}

			ShareData.usernameAndTodayWorkload = temp1;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				rs1.close();
				rs2.close();
				rs3.close();
				rs4.close();
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

				refreshWorkload();

				Thread.sleep(period);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}

	}
}
