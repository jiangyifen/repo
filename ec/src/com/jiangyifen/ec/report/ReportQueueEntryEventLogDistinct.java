package com.jiangyifen.ec.report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.report.internal.AbstractReport;
import com.jiangyifen.ec.util.Config;

public class ReportQueueEntryEventLogDistinct extends AbstractReport {

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
			String sql = "select max(datereceived) from ec_report_queue_entry_event_log_distinct";
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
				sql = "select min(datereceived) from ec_queue_entry_event_log";
				logger.info(sql);
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();

				resultSet.next();
				lastDate = resultSet.getDate(1);
				if (lastDate != null) {
					result = dateSdf.format(lastDate);
				} else {
					// 如果原始表是空的，直接返回空
					logger.warn("No data in table ec_queue_entry_event_log. Cannot generate report.");
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
		ResultSet resultSet = null;

		try {
			String url = Config.props.getProperty("pg_url");
			String username = Config.props.getProperty("pg_username");
			String password = Config.props.getProperty("pg_password");

			connection = DriverManager.getConnection(url, username, password);

			String sql;

			// 清除这天的数据
			sql = "delete from ec_report_queue_entry_event_log_distinct where datereceived>='"
					+ dateString
					+ " 00:00:00' and datereceived<='"
					+ dateString + " 23:59:59'";

			logger.info(sql);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.execute();

			// 从原始表中提取数据，记录到目标表中

			// 先按照callerid和channel及queue判断这是一通电话。一通电话反复进入同一个queue，在这个报表中只算一次。
			ArrayList<String> calleridAndChannel = new ArrayList<String>();
			sql = "select callerid,channel,queue from ec_queue_entry_event_log where datereceived>='"
					+ dateString
					+ " 00:00:00' and datereceived<='"
					+ dateString
					+ " 23:59:59' group by callerid,channel,queue;";
			logger.info(sql);
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String callerid = resultSet.getString(1);
				String channel = resultSet.getString(2);
				String queueName = resultSet.getString(3);
				calleridAndChannel.add(callerid + ":" + channel + ":"
						+ queueName);
			}
			logger.info("calleridAndChannel.size()="
					+ calleridAndChannel.size());

			ArrayList<Long> idList = new ArrayList<Long>();
			for (String s : calleridAndChannel) {
				String[] cAc = s.split(":");
				String callerid = cAc[0];
				String channel = cAc[1];
				String queueName = cAc[2];
				sql = "select min(id) from ec_queue_entry_event_log where callerid='"
						+ callerid
						+ "' and channel='"
						+ channel
						+ "' and queue='" + queueName + "';";
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					Long id = resultSet.getLong(1);
					idList.add(id);
				}
			}
			logger.info("idList.size()=" + idList.size());

			int i = 0;
			for (Long id : idList) {
				sql = "insert into ec_report_queue_entry_event_log_distinct (queue,datereceived,callerid,calleridname,channel,position,wait) select queue,datereceived,callerid,calleridname,channel,position,wait from ec_queue_entry_event_log where id="
						+ id;
				// logger.info(sql);
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.execute();
				i++;
			}
			logger.info("i=" + i);

			// 处理完后将日期记录在lastGenDate中
			lastGenDate = dateString;
			logger.info("jyf lastGenDate: " + lastGenDate);

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
	}

	public static void main(String[] args) {
		ReportQueueEntryEventLogDistinct r = new ReportQueueEntryEventLogDistinct();
		System.out.println(r.getLastDateInDB());
	}

}
