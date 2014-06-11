package com.jiangyifen.ec.customer.olay.report;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.report.internal.AbstractReport;
import com.jiangyifen.ec.util.Config;

public class ReportOperationalData extends AbstractReport {

	private String beginTime;
	private String endTime;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Connection connection = null;

	// //////////////
	// 呼入正常线路的电话总数
	private Map<String, Long> inboundCalls = new HashMap<String, Long>();

	// 呼入“示忙”线路的电话总数
	private Map<String, Long> forceDisconnectedCalls = new HashMap<String, Long>();

	// 呼入电话总量
	// incomingCalls = inboundCalls + forceDisconnectedCalls
	private Map<String, Long> incomingCalls = new HashMap<String, Long>();

	// 进入IVR的次数
	// 此数据统计自主兑换服务的请求次数。如果同一通电话在兑换成功后再次进行自主兑换，就算两次
	private Map<String, Long> ivrRedemptionCount = new HashMap<String, Long>();

	// 队列请求次数
	// 统计调用Queue Application的次数
	private Map<String, Long> queueCount = new HashMap<String, Long>();

	// 队列接听的次数
	private Map<String, Long> queueAnsweredCount = new HashMap<String, Long>();

	// 队列接听率
	// queueAnsweredRate = queueAnsweredCount / queueCount
	private Map<String, Double> queueAnsweredRate = new HashMap<String, Double>();

	// ActualSL，客户定义数据
	// ActualSL=queueAnsweredRate * inboundCalls/incomingCalls
	private Map<String, Double> actualSL = new HashMap<String, Double>();

	// 30秒内接听数
	// 30秒内被人工接起的数量
	private Map<String, Long> queueAnsweredCount_in30s = new HashMap<String, Long>();

	// 30秒内人工接起总时长
	private Map<String, Long> queueAnsweredTotalTimelength_in30s = new HashMap<String, Long>();

	// 30秒内人工接起平均时长
	private Map<String, Long> queueAnsweredAverageTimelength_in30s = new HashMap<String, Long>();

	// 30秒外接听数
	// 30秒外被人工接起的数量
	private Map<String, Long> queueAnsweredCount_out30s = new HashMap<String, Long>();

	// 30秒外人工接起总时长
	private Map<String, Long> queueAnsweredTotalTimelength_out30s = new HashMap<String, Long>();

	// 30秒外人工接起平均时长
	private Map<String, Long> queueAnsweredAverageTimelength_out30s = new HashMap<String, Long>();

	// 30秒内接听率
	// queueAnsweredRate_in30s = queueAnsweredCount_in30s / queueCount
	private Map<String, Double> queueAnsweredRate_in30s = new HashMap<String, Double>();

	// 30秒外接听率
	// queueAnsweredRate_out30s = queueAnsweredCount_out30s / queueCount
	private Map<String, Double> queueAnsweredRate_out30s = new HashMap<String, Double>();

	// 30秒内挂断数
	private Map<String, Long> queueAbandonCount_in30s = new HashMap<String, Long>();

	// 30秒内挂断总时长
	private Map<String, Long> queueAbandonTotalTimelength_in30s = new HashMap<String, Long>();

	// 30秒内挂断平均时长
	private Map<String, Long> queueAbandonAverageTimelength_in30s = new HashMap<String, Long>();

	// 总通话时长（秒）
	// totalTalkingTime
	private Map<String, Long> totalQueueAnswerTime = new HashMap<String, Long>();

	// 平均通话时长
	// averageTalkingTime = 总人工接听时间 / 总人工接听次数queueAnsweredCount
	private Map<String, Long> averageQueueAnswerTime = new HashMap<String, Long>();

	// 平均处理时长
	// averageHandleTime = （总人工接听时间 + 总事后处理时间） / 总人工接听次数queueAnsweredCount
	private Map<String, Long> averageHandleTime = new HashMap<String, Long>();

	// /////////////////////

	public void run() {
		String url = Config.props.getProperty("pg_url");
		String username = Config.props.getProperty("pg_username");
		String password = Config.props.getProperty("pg_password");
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, username, password);

		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}

	}

	// inboundCalls
	private void getInboundCalls(final String day) {
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			String sql = "select count(*) from ec_ivr_log where date>='" + day
					+ " 00:00:00' and date<='" + day
					+ " 23:59:59' and (node='33251140' or node='33194910');";
			logger.info(sql);
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			rs.next();
			inboundCalls.put(day, rs.getLong(1));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				rs.close();
				statement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	// forceDisconnectedCalls
	private void getForceDisconnectedCalls(final String day) {
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			String sql = "select count(*) from ec_ivr_log where date>='" + day
					+ " 00:00:00' and date<='" + day
					+ " 23:59:59' and node='33194901';";
			logger.info(sql);
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			rs.next();
			forceDisconnectedCalls.put(day, rs.getLong(1));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				rs.close();
				statement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	// incomingCalls
	private void getIncomingCalls(final String day) {
		incomingCalls.put(day,
				inboundCalls.get(day) + forceDisconnectedCalls.get(day));
	}

	// ivrRedemptionCount
	private void getIvrRedemptionCount(final String day) {
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			String sql = "select count(*) from ec_ivr_log where date>='" + day
					+ " 00:00:00' and date<='" + day
					+ " 23:59:59' and node='1_jfdh';";
			logger.info(sql);
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			rs.next();
			ivrRedemptionCount.put(day, rs.getLong(1));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				rs.close();
				statement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	// queueCount
	private void getQueueCount(final String day) {
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			String sql = "select count(*) from ec_ivr_log where date>='"
					+ day
					+ " 00:00:00' and date<='"
					+ day
					+ " 23:59:59' and (node='queue_vip_enter' or node='queue_normal_enter');";
			logger.info(sql);
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			rs.next();
			queueCount.put(day, rs.getLong(1));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				rs.close();
				statement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	// queueAnsweredCount
	private void getQueueAnsweredCount(final String day) {
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			String sql = "select count(*) from ec_ivr_log where date>='"
					+ day
					+ " 00:00:00' and date<='"
					+ day
					+ " 23:59:59' and (node='queue_vip_answer' or node='queue_normal_answer');";
			logger.info(sql);
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			rs.next();
			queueAnsweredCount.put(day, rs.getLong(1));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				rs.close();
				statement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	// queueAnsweredRate
	private void getQueueAnsweredRate(final String day) {
		double l_queueAnsweredRate = 0;
		if (queueCount.get(day) != 0) {
			l_queueAnsweredRate = 100 * (double) queueAnsweredCount.get(day)
					/ (double) queueCount.get(day);
			BigDecimal b = new BigDecimal(l_queueAnsweredRate);
			l_queueAnsweredRate = b.setScale(2, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
			queueAnsweredRate.put(day, l_queueAnsweredRate);
		} else {
			queueAnsweredRate.put(day, (double) 0);
		}
	}

	// actualSL
	// ActualSL=queueAnsweredRate * inboundCalls/incomingCalls
	private void getActualSL(final String day) {
		double l_actualSL;
		if (incomingCalls.get(day) != 0) {
			l_actualSL = queueAnsweredRate.get(day) * inboundCalls.get(day)
					/ incomingCalls.get(day);
			BigDecimal b = new BigDecimal(l_actualSL);
			l_actualSL = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			actualSL.put(day, l_actualSL);
		} else {
			actualSL.put(day, (double) 0);
		}
	}

	private Set<String> getEnteredUniqueids(final String day) {
		PreparedStatement statement = null;
		ResultSet rs = null;
		Set<String> result = new HashSet<String>();
		try {
			String sql = "select distinct uniqueid from ec_ivr_log where date>='"
					+ day
					+ " 00:00:00' and date<='"
					+ day
					+ " 23:59:59' and (node='queue_vip_enter' or node='queue_normal_enter');";
			logger.info(sql);
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				String uniqueid = rs.getString(1);
				result.add(uniqueid);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				rs.close();
				statement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return result;
	}

	private Set<String> getAnsweredUniqueids(final String day) {
		PreparedStatement statement = null;
		ResultSet rs = null;
		Set<String> result = new HashSet<String>();
		try {
			String sql = "select distinct uniqueid from ec_ivr_log where date>='"
					+ day
					+ " 00:00:00' and date<='"
					+ day
					+ " 23:59:59' and (node='queue_vip_answer' or node='queue_normal_answer');";
			logger.info(sql);
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				String uniqueid = rs.getString(1);
				result.add(uniqueid);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				rs.close();
				statement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return result;
	}

	// queueAnsweredCount_in30s
	// queueAnsweredCount_out30s
	// queueAnsweredTotalTimelength_in30s
	// queueAnsweredTotalTimelength_out30s
	private void get30sDetail(final String day) {

		// 该天所有进入队列的通话（uniqueid）
		Set<String> enteredUniqueids = getEnteredUniqueids(day);

		// 该时段被接起的通话（uniqueid）
		Set<String> answeredUniqueids = getAnsweredUniqueids(day);

		// 该时段进入队列且未被接起的通话（uniqueid）
		Set<String> abandonedUniqueids = new HashSet<String>();
		for (String uniqueid : enteredUniqueids) {
			if (!answeredUniqueids.contains(uniqueid))
				abandonedUniqueids.add(uniqueid);
		}

		PreparedStatement statement = null;
		ResultSet rs = null;
		String sql = null;

		try {

			// queueAnsweredCount_in30s
			// queueAnsweredCount_out30s
			// queueAnsweredTotalTimelength_in30s
			// queueAnsweredTotalTimelength_out30s
			//
			// queueAnsweredRate_in30s
			// queueAnsweredRate_out30s
			// queueAnsweredAverageTimelength_in30s
			// queueAbandonCount_in30s
			// queueAbandonAverageTimelength_in30s
			// 把被接起的通话轮一遍，把应答时间-进入队列时间，以30秒为界做分类统计

			long l_queueAnsweredCount_in30s = 0;
			long l_queueAnsweredTotalTimelength_in30s = 0;

			long l_queueAnsweredCount_out30s = 0;
			long l_queueAnsweredTotalTimelength_out30s = 0;

			for (String uniqueid : answeredUniqueids) {

				try {
					sql = "select date from ec_ivr_log where date>='"
							+ day
							+ " 00:00:00' and date<='"
							+ day
							+ " 23:59:59' and (node='queue_vip_answer' or node='queue_normal_answer') and uniqueid='"
							+ uniqueid + "';";
					logger.info(sql);
					statement = connection.prepareStatement(sql);
					rs = statement.executeQuery();
					Date answerDate;
					if (rs.next())
						answerDate = rs.getTimestamp(1);
					else
						continue;

					sql = "select date from ec_ivr_log where date>='"
							+ day
							+ " 00:00:00' and date<='"
							+ day
							+ " 23:59:59' and (node='queue_vip_enter' or node='queue_normal_enter') and uniqueid='"
							+ uniqueid + "';";
					logger.info(sql);
					statement = connection.prepareStatement(sql);
					rs = statement.executeQuery();
					Date enterDate;
					if (rs.next())
						enterDate = rs.getTimestamp(1);
					else
						continue;

					// 接听速度
					long timeLength = answerDate.getTime()
							- enterDate.getTime();
					if (timeLength <= 30000) {
						l_queueAnsweredCount_in30s++;
						l_queueAnsweredTotalTimelength_in30s = l_queueAnsweredTotalTimelength_in30s
								+ (answerDate.getTime() - enterDate.getTime());
					} else {
						l_queueAnsweredCount_out30s++;
						l_queueAnsweredTotalTimelength_out30s = l_queueAnsweredTotalTimelength_out30s
								+ (answerDate.getTime() - enterDate.getTime());
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}

			queueAnsweredCount_in30s.put(day, l_queueAnsweredCount_in30s);
			queueAnsweredCount_out30s.put(day, l_queueAnsweredCount_out30s);
			queueAnsweredTotalTimelength_in30s.put(day,
					l_queueAnsweredTotalTimelength_in30s / 1000);
			queueAnsweredTotalTimelength_out30s.put(day,
					l_queueAnsweredTotalTimelength_out30s / 1000);

			// queueAnsweredRate_in30s
			// queueAnsweredRate_in30s = queueAnsweredCount_in30s / queueCount
			double l_queueAnsweredRate_in30s;
			if (queueCount.get(day) != 0) {
				l_queueAnsweredRate_in30s = 100
						* (double) queueAnsweredCount_in30s.get(day)
						/ (double) queueCount.get(day);
				BigDecimal b = new BigDecimal(l_queueAnsweredRate_in30s);
				l_queueAnsweredRate_in30s = b.setScale(2,
						BigDecimal.ROUND_HALF_UP).doubleValue();
				queueAnsweredRate_in30s.put(day, l_queueAnsweredRate_in30s);
			} else {
				queueAnsweredRate_in30s.put(day, (double) 0);
			}

			// queueAnsweredRate_out30s
			// queueAnsweredRate_out30s = queueAnsweredCount_out30s / queueCount
			double l_queueAnsweredRate_out30s;
			if (queueCount.get(day) != 0) {
				l_queueAnsweredRate_out30s = 100
						* (double) queueAnsweredCount_in30s.get(day)
						/ (double) queueCount.get(day);
				BigDecimal b = new BigDecimal(l_queueAnsweredRate_out30s);
				l_queueAnsweredRate_out30s = b.setScale(2,
						BigDecimal.ROUND_HALF_UP).doubleValue();
				queueAnsweredRate_out30s.put(day, l_queueAnsweredRate_out30s);
			} else {
				queueAnsweredRate_out30s.put(day, (double) 0);
			}

			// queueAnsweredAverageTimelength_in30s
			// queueAnsweredAverageTimelength_out30s
			if (queueAnsweredCount_in30s.get(day) != 0) {
				queueAnsweredAverageTimelength_in30s.put(day,
						queueAnsweredTotalTimelength_in30s.get(day)
								/ queueAnsweredCount_in30s.get(day));
			} else {
				queueAnsweredAverageTimelength_in30s.put(day, 0l);
			}

			if (queueAnsweredCount_out30s.get(day) != 0) {
				queueAnsweredAverageTimelength_out30s.put(day,
						queueAnsweredTotalTimelength_out30s.get(day)
								/ queueAnsweredCount_out30s.get(day));
			} else {
				queueAnsweredAverageTimelength_out30s.put(day, 0l);
			}

			// queueAbandonCount_in30s
			// queueAbandonTotalTimelength_in30s
			// 把进入队列未被接起直接挂断的通话轮一遍，把应答时间-进入队列时间，以30秒为界做分类统计（这里只需要30秒内的）
			long l_queueAbandonCount_in30s = 0;
			long l_queueAbandonTotalTimelength_in30s = 0;

			for (String uniqueid : abandonedUniqueids) {

				try {
					sql = "select date from ec_ivr_log where date>='" + day
							+ " 00:00:00' and date<='" + day
							+ " 23:59:59' and node='hangup' and uniqueid='"
							+ uniqueid + "';";
					logger.info(sql);
					statement = connection.prepareStatement(sql);
					rs = statement.executeQuery();
					Date hangupDate;
					if (rs.next())
						hangupDate = rs.getTimestamp(1);
					else
						continue;

					sql = "select date from ec_ivr_log where date>='"
							+ day
							+ " 00:00:00' and date<='"
							+ day
							+ " 23:59:59' and (node='queue_vip_enter' or node='queue_normal_enter') and uniqueid='"
							+ uniqueid + "';";
					logger.info(sql);
					statement = connection.prepareStatement(sql);
					rs = statement.executeQuery();
					Date enterDate;
					if (rs.next())
						enterDate = rs.getTimestamp(1);
					else
						continue;

					// 挂断速度
					long timeLength = hangupDate.getTime()
							- enterDate.getTime();
					if (timeLength <= 30000) {
						l_queueAbandonCount_in30s++;
						l_queueAbandonTotalTimelength_in30s = l_queueAbandonTotalTimelength_in30s
								+ (hangupDate.getTime() - enterDate.getTime());
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}

			queueAbandonCount_in30s.put(day, l_queueAbandonCount_in30s);
			queueAbandonTotalTimelength_in30s.put(day,
					l_queueAbandonTotalTimelength_in30s / 1000);

			// queueAbandonAverageTimelength_in30s
			if (queueAbandonCount_in30s.get(day) != 0) {
				queueAbandonAverageTimelength_in30s.put(day,
						queueAbandonTotalTimelength_in30s.get(day)
								/ queueAbandonCount_in30s.get(day));
			} else {
				queueAbandonAverageTimelength_in30s.put(day, 0l);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				rs.close();
				statement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	// totalQueueAnswerTime
	private void getTotalQueueAnswerTime(String day) {

		PreparedStatement statement = null;
		ResultSet rs = null;
		String sql = null;

		try {
			// 先从ivrlog里把当天所有有queue answer的uniqueid全找出来
			// 然后从linklog里，把bridgeuniqueid等于这些uniqueid的记录查出来，这些就是通过queue接通的电话
			// 将这些unlink 和 link 的时间差统计求和，就是呼叫时长
			// 注意选择某个uniqudid的unlink和link时，要选最后一个unlink和第一个link。
			// 虽然link和unlink一般情况下是各一条，但是某些情况asterisk会发出多条link
			// unlink event的。
			long l_totalQueueAnswerTime = 0;

			sql = "select distinct uniqueid from ec_ivr_log where date>='"
					+ day
					+ " 00:00:00' and date<='"
					+ day
					+ " 23:59:59' and (node='queue_vip_answer' or node='queue_normal_answer');";
			logger.info(sql);
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			ArrayList<String> uids = new ArrayList<String>();
			while (rs.next()) {
				uids.add(rs.getString(1));
			}

			for (String uniqueid : uids) {
				sql = "SELECT date from ec_link_log where action='link' and bridgeduniqueid='"
						+ uniqueid + "' order by date;";
				logger.info(sql);
				statement = connection.prepareStatement(sql);
				rs = statement.executeQuery();
				Date linkDate;
				if (rs.next()) {
					linkDate = rs.getTimestamp(1);
				} else {
					continue;
				}

				sql = "SELECT date from ec_link_log where action='unlink' and bridgeduniqueid='"
						+ uniqueid + "' order by date desc;";
				logger.info(sql);
				statement = connection.prepareStatement(sql);
				rs = statement.executeQuery();
				Date unlinkDate;
				if (rs.next()) {
					unlinkDate = rs.getTimestamp(1);
				} else {
					continue;
				}

				l_totalQueueAnswerTime = l_totalQueueAnswerTime
						+ (unlinkDate.getTime() - linkDate.getTime());

			}
			totalQueueAnswerTime.put(day, l_totalQueueAnswerTime / 1000);// 换算成秒

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				rs.close();
				statement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	// averageQueueAnswerTime
	// averageQueueAnswerTime = totalQueueAnswerTime /
	// queueAnsweredCount
	private void getAverageQueueAnswerTime(String day) {
		if (queueAnsweredCount.get(day) != 0) {
			averageQueueAnswerTime.put(day,
					(totalQueueAnswerTime.get(day) / queueAnsweredCount
							.get(day)));
		} else {
			averageQueueAnswerTime.put(day, 0l);
		}
	}

	@Override
	protected void genData(String dateString) {
		// 没写完，
		// 请仔细核对Report框架，这里只要生成一天的数据，不需要for循环每天的数据
		for (String day : getDays(beginTime, endTime)) {

			getInboundCalls(day);

			getForceDisconnectedCalls(day);

			getIncomingCalls(day);

			getIvrRedemptionCount(day);

			getQueueCount(day);

			getQueueAnsweredCount(day);

			getQueueAnsweredRate(day);

			getActualSL(day);

			get30sDetail(day);

			getTotalQueueAnswerTime(day);

			getAverageQueueAnswerTime(day);

			// averageHandleTime
			averageHandleTime.put(day, 0l);

		}

	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEndTime() {
		return endTime;
	}

	private ArrayList<String> getDays(String beginTime, String endTime) {
		ArrayList<String> days = new ArrayList<String>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {

			long beginTimeLong = sdf.parse(beginTime).getTime();
			long endTimeLong = sdf.parse(endTime).getTime();

			for (long t = beginTimeLong; t <= endTimeLong; t = t + 3600 * 1000 * 24) {
				Date day = new Date(t);
				days.add(sdf.format(day));
			}

		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return days;
	}

	@Override
	protected String getLastDateInDB() {
		// TODO Auto-generated method stub
		return null;
	}

}
