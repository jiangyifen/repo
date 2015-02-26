package com.jiangyifen.ec.customer.olay.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.MyStringUtils;
import com.opensymphony.xwork2.ActionContext;

public class GetChartOperationalData extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4342483481875886027L;

	private String title = "Operational Data";

	private String beginTime;
	private String endTime;

	private List<String> excelResult = new ArrayList<String>();

	private final Logger logger = LoggerFactory.getLogger(getClass());

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

	// 30秒内接听率
	// queueAnsweredRate_in30s = queueAnsweredCount_in30s / queueCount
	private Map<String, Double> queueAnsweredRate_in30s = new HashMap<String, Double>();

	// 总通话时长（秒）
	// totalTalkingTime
	private Map<String, Long> totalQueueAnswerTime = new HashMap<String, Long>();

	// 平均通话时长
	// averageTalkingTime = 总人工接听时间 / 总人工接听次数queueAnsweredCount
	private Map<String, Long> averageQueueAnswerTime = new HashMap<String, Long>();

	// 平均处理时长
	// averageHandleTime = （总人工接听时间 + 总事后处理时间） / 总人工接听次数queueAnsweredCount
	private Map<String, Long> averageHandleTime = new HashMap<String, Long>();

	// 800 转入
	private Map<String, Long> from800 = new HashMap<String, Long>();

	// 转出至 800
	private Map<String, Long> to800 = new HashMap<String, Long>();

	// /////////////////////

	public String execute() throws Exception {

		return "excel";

	}

	private void getExcelDataSet() {

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String sql = "";

		try {

			String url = Config.props.getProperty("pg_url");
			String username = Config.props.getProperty("pg_username");
			String password = Config.props.getProperty("pg_password");
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(url, username, password);
			// ////////////////////

			ArrayList<String> days = getDays(beginTime, endTime);

			for (String day : days) {

				// inboundCalls
				sql = "select count(*) from ec_ivr_log where date>='"
						+ day
						+ " 00:00:00' and date<='"
						+ day
						+ " 23:59:59' and (node='33251140' or node='33194910');";
				logger.info(sql);
				statement = con.prepareStatement(sql);
				rs = statement.executeQuery();
				rs.next();
				inboundCalls.put(day, rs.getLong(1));

				// forceDisconnectedCalls
				sql = "select count(*) from ec_ivr_log where date>='" + day
						+ " 00:00:00' and date<='" + day
						+ " 23:59:59' and node='33194901';";
				logger.info(sql);
				statement = con.prepareStatement(sql);
				rs = statement.executeQuery();
				rs.next();
				forceDisconnectedCalls.put(day, rs.getLong(1));

				// incomingCalls
				incomingCalls.put(day, inboundCalls.get(day)
						+ +forceDisconnectedCalls.get(day));

				// ivrRedemptionCount
				sql = "select count(*) from ec_ivr_log where date>='" + day
						+ " 00:00:00' and date<='" + day
						+ " 23:59:59' and node='1_zzdh';";
				logger.info(sql);
				statement = con.prepareStatement(sql);
				rs = statement.executeQuery();
				rs.next();
				ivrRedemptionCount.put(day, rs.getLong(1));

				// queueCount
				sql = "select count(*) from ec_ivr_log where date>='"
						+ day
						+ " 00:00:00' and date<='"
						+ day
						+ " 23:59:59' and (node='queue_vip_enter' or node='queue_normal_enter');";
				logger.info(sql);
				statement = con.prepareStatement(sql);
				rs = statement.executeQuery();
				rs.next();
				queueCount.put(day, rs.getLong(1));

				// queueAnsweredCount
				sql = "select count(*) from ec_ivr_log where date>='"
						+ day
						+ " 00:00:00' and date<='"
						+ day
						+ " 23:59:59' and (node='queue_vip_answer' or node='queue_normal_answer');";
				logger.info(sql);
				statement = con.prepareStatement(sql);
				rs = statement.executeQuery();
				rs.next();
				queueAnsweredCount.put(day, rs.getLong(1));

				// queueAnsweredRate
				double l_queueAnsweredRate = 0;
				if (queueCount.get(day) != 0) {
					l_queueAnsweredRate = 100
							* (double) queueAnsweredCount.get(day)
							/ (double) queueCount.get(day);
					BigDecimal b = new BigDecimal(l_queueAnsweredRate);
					l_queueAnsweredRate = b.setScale(2,
							BigDecimal.ROUND_HALF_UP).doubleValue();
					queueAnsweredRate.put(day, l_queueAnsweredRate);
				} else {
					queueAnsweredRate.put(day, (double) 0);
				}

				// actualSL
				// ActualSL=queueAnsweredRate * inboundCalls/incomingCalls
				double l_actualSL;
				if (incomingCalls.get(day) != 0) {
					l_actualSL = queueAnsweredRate.get(day)
							* inboundCalls.get(day) / incomingCalls.get(day);
					BigDecimal b = new BigDecimal(l_actualSL);
					l_actualSL = b.setScale(2, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					actualSL.put(day, l_actualSL);
				} else {
					actualSL.put(day, (double) 0);
				}

				// queueAnsweredCount_in30s
				// 该时段被接起的通话（uniqueid）
				Set<String> answered_uniqueids = new HashSet<String>();
				sql = "select distinct uniqueid from ec_ivr_log where date>='"
						+ day
						+ " 00:00:00' and date<='"
						+ day
						+ " 23:59:59' and (node='queue_vip_answer' or node='queue_normal_answer');";
				logger.info(sql);
				statement = con.prepareStatement(sql);
				rs = statement.executeQuery();
				while (rs.next()) {
					String uniqueid = rs.getString(1);
					answered_uniqueids.add(uniqueid);
				}

				// 把被接起的通话轮一遍，把应答时间-进入队列时间，小于30秒的做计次

				long l_queueAnsweredCount_in30s = 0;
				for (String uniqueid : answered_uniqueids) {

					try {
						sql = "select date from ec_ivr_log where date>='"
								+ day
								+ " 00:00:00' and date<='"
								+ day
								+ " 23:59:59' and (node='queue_vip_answer' or node='queue_normal_answer') and uniqueid='"
								+ uniqueid + "';";
						logger.info(sql);
						statement = con.prepareStatement(sql);
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
						statement = con.prepareStatement(sql);
						rs = statement.executeQuery();
						Date enterDate;
						if (rs.next())
							enterDate = rs.getTimestamp(1);
						else
							continue;

						if ((answerDate.getTime() - enterDate.getTime()) <= 30000)
							l_queueAnsweredCount_in30s++;

					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
				queueAnsweredCount_in30s.put(day, l_queueAnsweredCount_in30s);

				// queueAnsweredRate_in30s
				// queueAnsweredRate_in30s = queueAnsweredCount_in30s /
				// queueCount
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

				// totalQueueAnswerTime
				// 先从ivrlog里把当天所有有queue answer的uniqueid全找出来
				// 然后从linklog里，把bridgeuniqueid等于这些uniqueid的记录查出来，这些就是通过queue接通的电话
				// 将这些unlink 和 link 的时间差统计求和，就是呼叫时长
				// 注意选择某个uniqudid的unlink和link时，要选最后一个unlink和第一个link。虽然link和unlink一般情况下是各一条，但是某些情况asterisk会发出多条link
				// unlink event的。
				long l_totalQueueAnswerTime = 0;

				sql = "select distinct uniqueid from ec_ivr_log where date>='"
						+ day
						+ " 00:00:00' and date<='"
						+ day
						+ " 23:59:59' and (node='queue_vip_answer' or node='queue_normal_answer');";
				logger.info(sql);
				statement = con.prepareStatement(sql);
				rs = statement.executeQuery();
				ArrayList<String> uids = new ArrayList<String>();
				while (rs.next()) {
					uids.add(rs.getString(1));
				}

				for (String uniqueid : uids) {
					sql = "SELECT date from ec_link_log where action='link' and bridgeduniqueid='"
							+ uniqueid + "' order by date;";
					logger.info(sql);
					statement = con.prepareStatement(sql);
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
					statement = con.prepareStatement(sql);
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

				// averageQueueAnswerTime
				// averageQueueAnswerTime = totalQueueAnswerTime /
				// queueAnsweredCount
				if (queueAnsweredCount.get(day) != 0) {
					averageQueueAnswerTime.put(day, (totalQueueAnswerTime
							.get(day) / queueAnsweredCount.get(day)));
				} else {
					averageQueueAnswerTime.put(day, 0l);
				}

				// averageHandleTime
				averageHandleTime.put(day, 0l);

				// 800 转入
				sql = "select count(*) from ec_ivr_log where date>='" + day
						+ " 00:00:00' and date<='" + day
						+ " 23:59:59' and node='from_800';";

				logger.info(sql);
				statement = con.prepareStatement(sql);
				rs = statement.executeQuery();
				rs.next();
				from800.put(day, rs.getLong(1));

				// 转出至 800
				sql = "select count(*) from ec_ivr_log where date>='" + day
						+ " 00:00:00' and date<='" + day
						+ " 23:59:59' and node='to_800';";

				logger.info(sql);
				statement = con.prepareStatement(sql);
				rs = statement.executeQuery();
				rs.next();
				to800.put(day, rs.getLong(1));
			}

			// 写入excelResult
			for (String day : days) {

				String result = day
						+ ","
						+ incomingCalls.get(day)
						+ ","
						+ inboundCalls.get(day)
						+ ","
						+ forceDisconnectedCalls.get(day)
						+ ","
						+ ivrRedemptionCount.get(day)
						+ ","
						+ queueCount.get(day)
						+ ","
						+ queueAnsweredCount.get(day)
						+ ","
						+ queueAnsweredRate.get(day)
						+ "%,"
						+ actualSL.get(day)
						+ "%,"
						+ queueAnsweredCount_in30s.get(day)
						+ ","
						+ queueAnsweredRate_in30s.get(day)
						+ "%,"
						+ MyStringUtils.formatIntToHHmmss(new Integer(""
								+ totalQueueAnswerTime.get(day)))
						+ ","
						+ MyStringUtils.formatIntToHHmmss(new Integer(""
								+ averageQueueAnswerTime.get(day))) + ","
						+ averageHandleTime.get(day) + "," + from800.get(day)
						+ "," + to800.get(day);

				excelResult.add(result);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

		} finally {
			try {
				rs.close();
				statement.close();
				con.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}

		}

	}

	public InputStream getExcelFile() {

		String recLocalDiskPath = null;

		FileInputStream fis = null;
		File file = null;

		try {

			recLocalDiskPath = Config.props.getProperty("rec_local_disk_path");

			String sessionID = (String) ActionContext.getContext().getSession()
					.get("sessionID");
			Date d = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
			String dir = recLocalDiskPath + sf.format(d) + "/";
			String cmd = "mkdir " + dir;
			logger.info(cmd);
			Runtime.getRuntime().exec(cmd);
			Thread.sleep(1000);

			file = new File(dir + sessionID);
			file.createNewFile();

			getExcelDataSet();

			Workbook wb = new HSSFWorkbook();
			Sheet sheet = wb.createSheet();
			Row row = null;
			Cell cell = null;

			row = sheet.createRow(0);
			cell = row.createCell(0);
			cell.setCellValue(beginTime + "~" + endTime);

			cell = row.createCell(1);
			cell.setCellValue(title);

			row = sheet.createRow(1);

			cell = row.createCell(0);
			cell.setCellValue("Date");

			cell = row.createCell(1);
			cell.setCellValue("Income calls");

			cell = row.createCell(2);
			cell.setCellValue("Inbound calls");

			cell = row.createCell(3);
			cell.setCellValue("Force disconnect");

			cell = row.createCell(4);
			cell.setCellValue("IVR");

			cell = row.createCell(5);
			cell.setCellValue("Offerred call");

			cell = row.createCell(6);
			cell.setCellValue("ACD call");

			cell = row.createCell(7);
			cell.setCellValue("ACD answer rate");
			cell = row.createCell(8);
			cell.setCellValue("Actual S.L.");
			cell = row.createCell(9);
			cell.setCellValue("30S ACD");
			cell = row.createCell(10);
			cell.setCellValue("% 30S ACD");
			cell = row.createCell(11);
			cell.setCellValue("Total talking time");
			cell = row.createCell(12);
			cell.setCellValue("Avg. talking time");
			cell = row.createCell(13);
			cell.setCellValue("Avg. handle time");
			cell = row.createCell(14);
			cell.setCellValue("from 800");
			cell = row.createCell(15);
			cell.setCellValue("to 800");

			for (int rownum = 0; rownum < excelResult.size(); rownum++) {
				row = sheet.createRow(rownum + 2);
				String[] line = excelResult.get(rownum).split(",");
				for (int cellnum = 0; cellnum < line.length; cellnum++) {
					cell = row.createCell(cellnum);
					String value = line[cellnum];
					if (MyStringUtils.isNumeric(value))
						cell.setCellValue(new Double(value));
					else
						cell.setCellValue(value);
				}
			}

			FileOutputStream out = new FileOutputStream(file);
			wb.write(out);
			out.close();

			fis = new FileInputStream(file);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return fis;
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

}
