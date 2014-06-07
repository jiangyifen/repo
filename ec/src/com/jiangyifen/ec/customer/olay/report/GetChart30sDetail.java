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

public class GetChart30sDetail extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6874623594246189159L;

	private String title = "30 Seconds Detail";

	private String beginTime;
	private String endTime;

	private List<String> excelResult = new ArrayList<String>();

	private final Logger logger = LoggerFactory.getLogger(getClass());

	// 呼入正常线路的电话总数
	private Map<String, Long> inboundCalls = new HashMap<String, Long>();

	// 呼入“示忙”线路的电话总数
	private Map<String, Long> forceDisconnectedCalls = new HashMap<String, Long>();

	// 呼入电话总量
	// incomingCalls = inboundCalls + forceDisconnectedCalls
	private Map<String, Long> incomingCalls = new HashMap<String, Long>();

	// 队列请求次数
	// 统计调用Queue Application的次数
	private Map<String, Long> queueCount = new HashMap<String, Long>();

	// 队列接听的次数
	private Map<String, Long> queueAnsweredCount = new HashMap<String, Long>();

	// 队列接听率
	// queueAnsweredRate = queueAnsweredCount / queueCount
	private Map<String, Double> queueAnsweredRate = new HashMap<String, Double>();

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

	// 30秒内挂断数
	private Map<String, Long> queueAbandonCount_in30s = new HashMap<String, Long>();

	// 30秒内挂断总时长
	private Map<String, Long> queueAbandonTotalTimelength_in30s = new HashMap<String, Long>();

	// 30秒内挂断平均时长
	private Map<String, Long> queueAbandonAverageTimelength_in30s = new HashMap<String, Long>();

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

				// 该时段所有进入队列的通话（uniqueid）
				Set<String> entered_uniqueids = new HashSet<String>();
				sql = "select distinct uniqueid from ec_ivr_log where date>='"
						+ day
						+ " 00:00:00' and date<='"
						+ day
						+ " 23:59:59' and (node='queue_vip_enter' or node='queue_normal_enter');";
				logger.info(sql);
				statement = con.prepareStatement(sql);
				rs = statement.executeQuery();
				while (rs.next()) {
					String uniqueid = rs.getString(1);
					entered_uniqueids.add(uniqueid);
				}

				// 该时段进入队列且被接起的通话（uniqueid）
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

				// 该时段进入队列且未被接起的通话（uniqueid）
				Set<String> abandoned_uniqueids = new HashSet<String>();
				for (String uniqueid : entered_uniqueids) {
					if (!answered_uniqueids.contains(uniqueid))
						abandoned_uniqueids.add(uniqueid);
				}

				// queueAnsweredCount_in30s
				// queueAnsweredCount_out30s
				// queueAnsweredTotalTimelength_in30s
				// queueAnsweredTotalTimelength_out30s
				// 把被接起的通话轮一遍，把应答时间-进入队列时间，以30秒为界做分类统计
				long l_queueAnsweredCount_in30s = 0;
				long l_queueAnsweredTotalTimelength_in30s = 0;

				long l_queueAnsweredCount_out30s = 0;
				long l_queueAnsweredTotalTimelength_out30s = 0;

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

						// 接听速度
						long timeLength = answerDate.getTime()
								- enterDate.getTime();
						if (timeLength <= 30000) {
							l_queueAnsweredCount_in30s++;
							l_queueAnsweredTotalTimelength_in30s = l_queueAnsweredTotalTimelength_in30s
									+ (answerDate.getTime() - 
											enterDate.getTime());
						} else {
							l_queueAnsweredCount_out30s++;
							l_queueAnsweredTotalTimelength_out30s = l_queueAnsweredTotalTimelength_out30s
									+ (answerDate.getTime() - 
											enterDate.getTime());
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}

				queueAnsweredCount_in30s.put(day, l_queueAnsweredCount_in30s);
				queueAnsweredCount_out30s.put(day, l_queueAnsweredCount_out30s);
				queueAnsweredTotalTimelength_in30s.put(day,
						l_queueAnsweredTotalTimelength_in30s/1000);
				queueAnsweredTotalTimelength_out30s.put(day,
						l_queueAnsweredTotalTimelength_out30s/1000);

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

				for (String uniqueid : abandoned_uniqueids) {

					try {
						sql = "select date from ec_ivr_log where date>='" + day
								+ " 00:00:00' and date<='" + day
								+ " 23:59:59' and node='hangup' and uniqueid='"
								+ uniqueid + "';";
						logger.info(sql);
						statement = con.prepareStatement(sql);
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
						statement = con.prepareStatement(sql);
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
									+ (hangupDate.getTime() - 
											enterDate.getTime());
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}

				queueAbandonCount_in30s.put(day, l_queueAbandonCount_in30s);
				queueAbandonTotalTimelength_in30s.put(day,
						l_queueAbandonTotalTimelength_in30s/1000);

				// queueAbandonAverageTimelength_in30s
				if (queueAbandonCount_in30s.get(day) != 0) {
					queueAbandonAverageTimelength_in30s.put(day,
							queueAbandonTotalTimelength_in30s.get(day)
									/ queueAbandonCount_in30s.get(day));
				} else {
					queueAbandonAverageTimelength_in30s.put(day, 0l);
				}

			}

			// 写入excelResult
			for (String day : days) {

				String result = day + "," + incomingCalls.get(day) + ","
						+ inboundCalls.get(day) + ","
						+ forceDisconnectedCalls.get(day) + ","
						+ queueCount.get(day) + ","
						+ queueAnsweredCount.get(day) + ","
						+ queueAnsweredRate.get(day) + "%,"
						+ queueAnsweredCount_in30s.get(day) + ","
						+ queueAnsweredAverageTimelength_in30s.get(day) + ","
						+ queueAnsweredCount_out30s.get(day) + ","
						+ queueAnsweredAverageTimelength_out30s.get(day) + ","
						+ queueAbandonCount_in30s.get(day) + "%,"
						+ queueAbandonAverageTimelength_in30s.get(day);

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
			cell.setCellValue("Offerred call");

			cell = row.createCell(5);
			cell.setCellValue("ACD call");

			cell = row.createCell(6);
			cell.setCellValue("ACD answer rate");

			cell = row.createCell(7);
			cell.setCellValue("answer in 30s, count");
			
			cell = row.createCell(8);
			cell.setCellValue("answer in 30s, Avg Timelength");
			
			cell = row.createCell(9);
			cell.setCellValue("answer after 30s, count");
			
			cell = row.createCell(10);
			cell.setCellValue("answer after 30s, Avg Timelength");
			
			cell = row.createCell(11);
			cell.setCellValue("hangup in 30s, count");

			cell = row.createCell(12);
			cell.setCellValue("hangup in 30s, Avg Timelength");

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
