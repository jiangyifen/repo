package com.jiangyifen.ec.customer.swarovski.report;

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

public class GetChartOverallReportAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4342483481875886027L;

	private String title = "呼叫中心整体话务报表";

	private String beginTime;
	private String endTime;

	private List<String> excelResult = new ArrayList<String>();

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private int[] totalIncomingCount = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	private int[] totalRengongCount = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	private int[] totalHangupCount = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	private int[] totalRengongAnswerCount = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	private int[] totalRengongMissCount = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

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

			for (int hour = 0; hour <= 23; hour++) {

				String uniqueids = "";

				// 该时段呼入的通话（uniqueid）
				sql = "select distinct uniqueid from ec_ivr_log where date>='"
						+ beginTime + " " + hour + ":00:00' and date<='"
						+ beginTime + " " + hour
						+ ":59:59' and node='ivrroot';";
				logger.info(sql);
				statement = con.prepareStatement(sql);
				rs = statement.executeQuery();
				while (rs.next()) {
					String uniqueid = rs.getString(1);
					uniqueids = uniqueids + "'" + uniqueid + "',";
				}
				uniqueids = uniqueids + "'tail'";

				// 总呼入量
				// totalIncomingCount
				sql = "select count(*) from ec_ivr_log where node='ivrroot' and uniqueid in ("
						+ uniqueids + ");";
				logger.info(sql);
				statement = con.prepareStatement(sql);
				rs = statement.executeQuery();
				rs.next();
				totalIncomingCount[hour] = rs.getInt(1);

				// 人工服务量
				// totalRengongCount
				sql = "select count(*) from ec_ivr_log where node='rengong' and uniqueid in ("
						+ uniqueids + ");";
				logger.info(sql);
				statement = con.prepareStatement(sql);
				rs = statement.executeQuery();
				rs.next();
				totalRengongCount[hour] = rs.getInt(1);

				// 接听量
				// totalRengongAnswerCount
				sql = "select count(*) from ec_ivr_log where node='queue_normal_answer' and uniqueid in ("
						+ uniqueids + ");";
				logger.info(sql);
				statement = con.prepareStatement(sql);
				rs = statement.executeQuery();
				rs.next();
				totalRengongAnswerCount[hour] = rs.getInt(1);

			}
			// /////////////////////

			// 挂断量
			// 这里粗定义为：总进线量-人工服务量
			// 但是这种定义有前提：“人工服务量”应该理解为人工服务消化掉的进线量（也就是说，一通电话进来，无论请求了几次队列，只算一次人工进线，不算多次），而不能算作人工服务请求次数。
			// 否则人工服务量会大于总进线量
			for (int i = 0; i <= 23; i++) {
				totalHangupCount[i] = totalIncomingCount[i]
						- totalRengongCount[i];
			}

			// 弃呼数=人工服务量-接听量
			for (int i = 0; i <= 23; i++) {
				totalRengongMissCount[i] = totalRengongCount[i]
						- totalRengongAnswerCount[i];
			}

			// 写入excelResult
			for (int i = 0; i <= 23; i++) {
				String time = i + ":00 ~ " + (i + 1) + ":00";
				int l_totalIncomingCount = totalIncomingCount[i];
				int l_totalHangupCount = totalHangupCount[i];
				int l_totalRengongCount = totalRengongCount[i];
				int l_totalRengongAnswerCount = totalRengongAnswerCount[i];
				int l_totalRengongMissCount = totalRengongMissCount[i];

				int l_answerRate = 0;
				if (l_totalRengongCount != 0) {
					l_answerRate = 100 * l_totalRengongAnswerCount
							/ l_totalRengongCount;
				}

				String result = time + "," + l_totalIncomingCount + ","
						+ l_totalHangupCount + "," + l_totalRengongCount + ","
						+ l_totalRengongAnswerCount + ","
						+ l_totalRengongMissCount + "," + l_answerRate;

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
			cell.setCellValue("总进线量");

			cell = row.createCell(2);
			cell.setCellValue("挂断量");

			cell = row.createCell(3);
			cell.setCellValue("人工进线量");

			cell = row.createCell(4);
			cell.setCellValue("接起量");

			cell = row.createCell(5);
			cell.setCellValue("弃呼量");

			cell = row.createCell(6);
			cell.setCellValue("接起率");

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
