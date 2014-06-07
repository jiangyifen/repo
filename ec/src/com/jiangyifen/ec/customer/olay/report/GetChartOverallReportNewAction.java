package com.jiangyifen.ec.customer.olay.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

public class GetChartOverallReportNewAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8014045013644336934L;

	private String title = "呼叫中心整体话务报表";

	private String beginTime;
	private String endTime;

	private List<String> excelResult = new ArrayList<String>();

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private int[] totalIncomingCount = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	private int[] totalRengongCount = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	private int[] totalQueueEntryCount = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	private int[] totalIVRCount = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private int[] totalVoicemailCount = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	private int[] totalSelfHangupCount = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	private int[] totalQueueEntryAnswerCount = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	private int[] totalQueueEntryAnswerRate = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

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

			// 总呼入量
			sql = "select substring(date::text,12,2),count(*) from ec_ivr_log where date>='"
					+ beginTime
					+ " 00:00:00' and date<='"
					+ endTime
					+ " 23:59:59' and (node='33251140' or node='33194910') group by substring(date::text,12,2) order by substring(date::text,12,2);";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				int hour = rs.getInt(1);
				int count = rs.getInt(2);
				totalIncomingCount[hour] = count;
			}

			// 人工服务请求量
			sql = "select substring(date::text,12,2),count(*) from ec_ivr_log where date>='"
					+ beginTime
					+ " 00:00:00' and date<='"
					+ endTime
					+ " 23:59:59' and (node='3' or node='2' or node='normal_ivr_1' or node='normal_ivr_2') group by substring(date::text,12,2) order by substring(date::text,12,2);";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				int hour = rs.getInt(1);
				int count = rs.getInt(2);
				totalRengongCount[hour] = count;
			}

			// 队列进入量(ivr log agi统计的量)
			sql = "select substring(date::text,12,2),count(*) from ec_ivr_log where date>='"
					+ beginTime
					+ " 00:00:00' and date<='"
					+ endTime
					+ " 23:59:59' and (node='queue_vip_enter' or node='queue_normal_enter') group by substring(date::text,12,2) order by substring(date::text,12,2);";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				int hour = rs.getInt(1);
				int count = rs.getInt(2);
				totalQueueEntryCount[hour] = count;
			}

			//队列接起量(ivr log agi统计的量)
			sql = "select substring(date::text,12,2),count(*) from ec_ivr_log where date>='"
					+ beginTime
					+ " 00:00:00' and date<='"
					+ endTime
					+ " 23:59:59' and (node='queue_vip_answer' or node='queue_normal_answer') group by substring(date::text,12,2) order by substring(date::text,12,2);";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				int hour = rs.getInt(1);
				int count = rs.getInt(2);
				totalQueueEntryAnswerCount[hour] = count;
			}

			// 接起率
			for (int i = 0; i <= 23; i++) {
				int rate = 0;
				if (totalQueueEntryCount[i] != 0) {
					rate = totalQueueEntryAnswerCount[i] * 100
							/ totalQueueEntryCount[i];
				}
				totalQueueEntryAnswerRate[i] = rate;
			}

			// 自助语音量
			sql = "select SUBSTRING(date::text,12,2),node,count(*) from ec_ivr_log where (node='1' or node='faq') and date>='"
					+ beginTime
					+ " 00:00:00' and date<='"
					+ endTime
					+ " 23:59:59' group by SUBSTRING(date::text,12,2),node order by SUBSTRING(date::text,12,2);";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				int hour = rs.getInt(1);
				int count = rs.getInt(3);
				totalIVRCount[hour] = count;
			}

			// 自主挂断
			sql = "select SUBSTRING(calldate::text,12,2),SUBSTRING(dst::text,1,7),count(*) from cdr where calldate>='"
					+ beginTime
					+ " 00:00:00' and calldate<='"
					+ endTime
					+ " 23:59:59' and SUBSTRING(dst::text,1,10)='ivrroot' group by SUBSTRING(calldate::text,12,2),SUBSTRING(dst::text,1,7) order by SUBSTRING(calldate::text,12,2);";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				int hour = rs.getInt(1);
				int count = rs.getInt(3);
				totalSelfHangupCount[hour] = count;
			}

			// 留言量
			sql = "select SUBSTRING(origdate::text,12,2) ,count(*) from ec_voicemail where origdate>='"
					+ beginTime
					+ " 00:00:00' and origdate<='"
					+ endTime
					+ " 23:59:59' group by SUBSTRING(origdate::text,12,2) order by SUBSTRING(origdate::text,12,2);";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				int hour = rs.getInt(1);
				int count = rs.getInt(2);
				totalVoicemailCount[hour] = count;
			}

			// 写入excelResult
			for (int i = 0; i <= 23; i++) {
				String time = i + ":00 ~ " + (i + 1) + ":00";
				int l_totalIncomingCount = totalIncomingCount[i];
				int l_totalRengongCount = totalRengongCount[i];
				int l_totalQueueEntryCount = totalQueueEntryCount[i];
//				int l_totalQueueEntryAbandonCount = totalQueueEntryAbandonCount[i];
				int l_totalIVRCount = totalIVRCount[i];
				int l_totalSelfHangupCount = totalSelfHangupCount[i];
				int l_totalQueueEntryAnswerCount = totalQueueEntryAnswerCount[i];
				int l_totalQueueEntryAnswerRate = totalQueueEntryAnswerRate[i];
				int l_totalVoicemailCount = totalVoicemailCount[i];

				String result = time + "," 
						+ l_totalIncomingCount + ","
						+ l_totalRengongCount + "," 
						+ l_totalQueueEntryCount + "," 
//						+ l_totalQueueEntryAbandonCount + ","
						+ l_totalQueueEntryAnswerCount + ","
						+ l_totalQueueEntryAnswerRate + "%" + ","
						+ l_totalIVRCount + "," 
						+ l_totalSelfHangupCount + ","
						+ l_totalVoicemailCount;

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
			cell.setCellValue("时段");

			cell = row.createCell(1);
			cell.setCellValue("呼入总量");

			cell = row.createCell(2);
			cell.setCellValue("人工服务量");

			cell = row.createCell(3);
			cell.setCellValue("队列进入量");

			cell = row.createCell(4);
			cell.setCellValue("接起量");

			cell = row.createCell(5);
			cell.setCellValue("接起率");

			cell = row.createCell(6);
			cell.setCellValue("自助语音量");

			cell = row.createCell(7);
			cell.setCellValue("自主挂断量");

			cell = row.createCell(8);
			cell.setCellValue("留言量");

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

}
