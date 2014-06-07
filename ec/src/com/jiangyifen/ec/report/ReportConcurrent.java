package com.jiangyifen.ec.report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.report.internal.AbstractReport;
import com.jiangyifen.ec.util.Config;

public class ReportConcurrent extends AbstractReport {

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
			String sql = "select max(begintime) from ec_report_concurrent";
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
				sql = "select min(calldate) from cdr";
				logger.info(sql);
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();

				resultSet.next();
				lastDate = resultSet.getDate(1);
				if (lastDate != null) {
					result = dateSdf.format(lastDate);
				} else {
					// 如果原始表是空的，直接返回空
					logger.warn("No data in table cdr. Cannot generate report.");
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
			sql = "delete from ec_report_concurrent where begintime>='"
					+ dateString + " 00:00:00' and begintime<='" + dateString
					+ " 23:59:59'";

			logger.info(sql);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.execute();

			// 从原始表中提取数据，记录到目标表中

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(dateString + " 00:00:00");
			Long epoch = date.getTime();

			int min = Integer.MAX_VALUE;
			int max = Integer.MIN_VALUE;
			String beginTime = "";
			String endTime = "";
			for (long i = 0; i < 3600 * 24; i++) {
				long currentTime = epoch + i * 1000;
				String currentTimeString = sdf.format(currentTime);

				// xx:00:00,xx:30:00
				if (currentTime % 1800000 == 0) {
					beginTime = currentTimeString;
					min = Integer.MAX_VALUE;
					max = Integer.MIN_VALUE;;
				}

				sql = "select count(*) from cdr where calldate >= '"
						+ dateString
						+ " 00:00:00' and calldate<= '"
						+ dateString
						+ " 23:59:59' and ('"
						+ currentTimeString
						+ "'-(calldate +  cast(duration||' s' as interval)))<='00:00:00' and ('"
						+ currentTimeString + "'-calldate)>='00:00:00';";

//				logger.info(sql);
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();
				resultSet.next();
				int count = resultSet.getInt(1);
				max = Math.max(max, count);
				min = Math.min(min, count);

				// xx:29:59,xx:59:59
				if (currentTime % 1800000 == 1799000) {
					endTime = currentTimeString;

					sql = "insert into ec_report_concurrent (beginTime,endTime,min,max) values ('"
							+ beginTime
							+ "', "
							+ "'"
							+ endTime
							+ "',"
							+ min
							+ "," + max + ");";
					logger.info(sql);
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.execute();
				}

			}

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

	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date date = sdf.parse("2012-08-09 00:00:00");
		Long epoch = date.getTime();

		for (long i = 0; i < 3600 * 24; i++) {
			long currentTime = epoch + i * 1000;
			String currentTimeString = sdf.format(currentTime);
			if (currentTime % 1800000 == 0)
				System.out.println(currentTimeString);
		}

	}

}
