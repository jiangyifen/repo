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

public class ReportQueueUserPause extends AbstractReport {

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
			String sql = "select max(date) from ec_report_queue_user_pause";
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
				sql = "select min(pausedate) from ec_queue_member_pause_log where username is not null";
				logger.info(sql);
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();

				resultSet.next();
				lastDate = resultSet.getDate(1);
				if (lastDate != null) {
					result = dateSdf.format(lastDate);
				} else {
					// 如果原始表是空的，直接返回空
					logger.warn("No data in table ec_queue_member_pause_log. Cannot generate report.");
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
			sql = "delete from ec_report_queue_user_pause where date='"
					+ dateString + "'";

			logger.info(sql);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.execute();

			// 从原始表中提取数据，记录到目标表中
			sql = "insert into ec_report_queue_user_pause (username,date,timelength) select username , to_date(date, 'YYYY-MM-DD') as date,max(timelength) from (select username, queue, to_char(unpausedate, 'YYYY-MM-DD') as date, sum(unpausedate-pausedate) as timelength from ec_queue_member_pause_log where username is not null and pausedate is not null and unpausedate is not null and unpausedate>='"
					+ dateString
					+ " 00:00:00' and unpausedate<='"
					+ dateString
					+ " 23:59:59' group by username, queue, date order by username ) as foo group by username,date;";

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
		ReportQueueUserPause r = new ReportQueueUserPause();
		System.out.println(r.getLastDateInDB());
	}

}
