package com.jiangyifen.ec.actions.chart;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class GetChartQueueEnrtyAndAnswerIVRLogAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8582368052784704406L;

	private String title = "队列话务报表";

	private String beginTime;
	private String endTime;

	private List<String> excelResult = new ArrayList<String>();

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Map<String, Map<String, Integer>> entryResult = new HashMap<String, Map<String, Integer>>();
	private Map<String, Map<String, Integer>> answerResult = new HashMap<String, Map<String, Integer>>();

	public String execute() throws Exception {

		return "excel";

	}

	private Map<String, Integer> getInitedMap() {
		// 第一个Integer是24小时时段 0~23
		// 第二个Integer是该时段的队列接听/放弃数据
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i <= 23; i++) {
			String t = "";
			if (i >= 0 && i <= 9) {
				t = "0" + i;
			} else {
				t = "" + i;
			}
			map.put(t, 0);
		}
		return map;
	}

	private void getExcelDataSet() {

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;

		try {

			String url = Config.props.getProperty("pg_url");
			String username = Config.props.getProperty("pg_username");
			String password = Config.props.getProperty("pg_password");
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(url, username, password);

			// 队列接听次数
			String sql1 = "select exten,SUBSTRING(date::text,12,2) ,count(*)  from ec_ivr_log where date>='"
					+ beginTime
					+ " 00:00:00' and date<='"
					+ endTime
					+ " 23:59:59' and node like '%queue_%_answer%' group by exten,SUBSTRING(date::text,12,2);";

			logger.info(sql1);
			statement = con.prepareStatement(sql1);
			rs1 = statement.executeQuery();
			while (rs1.next()) {
				String queue = rs1.getString(1);
				String hour = rs1.getString(2);
				Integer count = rs1.getInt(3);

				Map<String, Integer> answer = answerResult.get(queue);
				if (answer == null) {
					answer = this.getInitedMap();
					answerResult.put(queue, answer);
				}

				answer.put(hour, count);

			}

			// 队列进入次数
			String sql2 = "select exten,SUBSTRING(date::text,12,2) ,count(*)  from ec_ivr_log where date>='"
					+ beginTime
					+ " 00:00:00' and date<='"
					+ endTime
					+ " 23:59:59' and node like '%queue_%_enter%' group by exten,SUBSTRING(date::text,12,2);";
			logger.info(sql2);
			statement = con.prepareStatement(sql2);
			rs2 = statement.executeQuery();
			while (rs2.next()) {
				String queue = rs2.getString(1);
				String hour = rs2.getString(2);
				Integer count = rs2.getInt(3);

				Map<String, Integer> entry = entryResult.get(queue);
				if (entry == null) {
					entry = this.getInitedMap();
					entryResult.put(queue, entry);
				}

				entry.put(hour, count);
			}

			// 写入excelResult
			for (String queue : entryResult.keySet()) {
				// entryResult中的queue一定大等于answerResult中的queue
				// 执行这步，确保answerResult的keySet可entryResult的一样
				if (answerResult.get(queue) == null) {
					answerResult.put(queue, getInitedMap());
				}

				Map<String, Integer> answer = answerResult.get(queue);
				Map<String, Integer> entry = entryResult.get(queue);

				for (int i = 0; i <= 23; i++) {
					String t = "";
					if (i >= 0 && i <= 9) {
						t = "0" + i;
					} else {
						t = "" + i;
					}

					String time = i + ":00 ~ " + (i + 1) + ":00";
					String queueName = queue;
					Integer allCount = entry.get(t);
					Integer answerCount = answer.get(t);
					// if (answerCount > allCount) {
					// answerCount = allCount;
					// }
					// Integer abandonCount = allCount - answerCount;
					Integer answerRate = 0;
					if (allCount != 0) {
						answerRate = answerCount * 100 / allCount;
					}
					String result = time + "," + queueName + "," + allCount
							+ "," + answerCount + "," + answerRate + "%";
					logger.info(result);
					excelResult.add(result);
				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

		} finally {
			try {
				rs1.close();
				rs2.close();
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
			cell.setCellValue("队列名");

			cell = row.createCell(2);
			cell.setCellValue("队列进入次数");

			cell = row.createCell(3);
			cell.setCellValue("队列接起次数");

			cell = row.createCell(4);
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
