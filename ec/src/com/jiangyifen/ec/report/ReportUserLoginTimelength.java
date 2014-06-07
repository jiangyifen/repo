package com.jiangyifen.ec.report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.report.internal.AbstractReport;
import com.jiangyifen.ec.util.Config;

public class ReportUserLoginTimelength extends AbstractReport {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected String getLastDateInDB() {

		String result = null;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			String url = Config.props.getProperty("pg_url");
			String username = Config.props.getProperty("pg_username");
			String password = Config.props.getProperty("pg_password");

			connection = DriverManager.getConnection(url, username, password);

			// 从中间表获取最新的日期
			String sql = "select max(date) from ec_report_user_login_timelength";
			logger.info(sql);
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();

			resultSet.next();
			SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
			Date lastDate = resultSet.getDate(1);
			if (lastDate != null) {
				result = dateSdf.format(lastDate);
			} else {
				// 如果中间表没有记录，就从原始表获取最早的日期
				sql = "select min(logindate) from ec_user_login_record where username is not null";
				logger.info(sql);
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();

				resultSet.next();
				lastDate = resultSet.getDate(1);
				if (lastDate != null) {
					result = dateSdf.format(lastDate);
				} else {
					// 如果原始表是空的，直接返回空
					logger.warn("No data in table ec_user_login_record. Cannot generate report.");
					return null;
				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}

		}
		return result;
	}

	@Override
	protected void genData(String dateString) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			String url = Config.props.getProperty("pg_url");
			String username = Config.props.getProperty("pg_username");
			String password = Config.props.getProperty("pg_password");

			connection = DriverManager.getConnection(url, username, password);

			String sql;

			// 清除这天的数据
			sql = "delete from ec_report_user_login_timelength where date='"
					+ dateString + "'";

			logger.info(sql);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.execute();

			// 从原始表中提取数据，记录到目标表中
			sql = "insert into ec_report_user_login_timelength (username,date,timelength) select username , to_date(date, 'YYYY-MM-DD') as date,timelength from (select username, to_char(logoutdate, 'YYYY-MM-DD') as date, sum(logoutdate-logindate) as timelength from ec_user_login_record where username is not null and logindate is not null and logoutdate is not null and logoutdate>='"
					+ dateString
					+ " 00:00:00' and logoutdate<='"
					+ dateString
					+ " 23:59:59' group by date, username order by date, username ) as foo ;";
			logger.info(sql);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.execute();

			// 处理完后将日期记录在lastGenDate中
			lastGenDate = dateString;
			logger.info("jyf lastGenDate: " + lastGenDate);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}

		}
	}

	public static void main(String[] args) {
		ReportUserLoginTimelength r = new ReportUserLoginTimelength();
		System.out.println(r.getLastDateInDB());
	}

}
