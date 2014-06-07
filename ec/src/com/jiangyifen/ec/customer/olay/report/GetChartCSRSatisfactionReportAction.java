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

public class GetChartCSRSatisfactionReportAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3137191966040984407L;

	private String title = "CSR个工作现报表-个人";

	private String beginTime;
	private String endTime;

	private List<String> excelResult = new ArrayList<String>();

	private final Logger logger = LoggerFactory.getLogger(getClass());

	// 接听数
	private Map<String, Integer> answerCount = new HashMap<String, Integer>();

	// 总评价数（满意+一般+不满意）
	private Map<String, Integer> totalSatisfactionCount = new HashMap<String, Integer>();
	// 1分
	private Map<String, Integer> n1Count = new HashMap<String, Integer>();
	// 2分
	private Map<String, Integer> n2Count = new HashMap<String, Integer>();
	// 3分
	private Map<String, Integer> n3Count = new HashMap<String, Integer>();
	// 4分
	private Map<String, Integer> n4Count = new HashMap<String, Integer>();
	// 5分
	private Map<String, Integer> n5Count = new HashMap<String, Integer>();

	// 未评价（接听数-总评价数）
	private Map<String, Integer> nnCount = new HashMap<String, Integer>();

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

			// 总接听量
			sql = "select username,count(*) from cdr where calldate>='"
					+ beginTime
					+ " 00:00:00' and calldate<='"
					+ endTime
					+ " 23:59:59' and dcontext='incoming' and disposition='ANSWER' and lastapp='Queue' and username<>'0' group by username;";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				String l_username = rs.getString(1);
				int l_count = rs.getInt(2);
				answerCount.put(l_username, l_count);

				// 以总接听量中的username为准，初始化所有其他collection
				totalSatisfactionCount.put(l_username, 0);
				n1Count.put(l_username, 0);
				n2Count.put(l_username, 0);
				n3Count.put(l_username, 0);
				n4Count.put(l_username, 0);
				n5Count.put(l_username, 0);
				nnCount.put(l_username, 0);
			}

			// 总评价数量
			sql = "select c.username,count(*) from cdr c,ec_customer_satisfaction_investigation_log s where c.uniqueid=s.uniqueid and c.username<>'0' and date>='"
					+ beginTime
					+ " 00:00:00' and date<='"
					+ endTime
					+ " 23:59:59' and (s.p1='1' or s.p1='2' or s.p1='3' or s.p1='4' or s.p1='5') group by c.username order by c.username;";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				String l_username = rs.getString(1);
				int l_count = rs.getInt(2);
				totalSatisfactionCount.put(l_username, l_count);
			}

			// 各分值的数量
			sql = "select c.username,s.p1,count(*) from cdr c,ec_customer_satisfaction_investigation_log s where c.uniqueid=s.uniqueid and c.username<>'0' and date>='"
					+ beginTime
					+ " 00:00:00' and date<='"
					+ endTime
					+ " 23:59:59' and (s.p1='1' or s.p1='2' or s.p1='3' or s.p1='4' or s.p1='5') group by c.username,s.p1 order by c.username,s.p1;";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				String l_username = rs.getString(1);
				int l_p1 = rs.getInt(2);
				int l_count = rs.getInt(3);
				logger.info("TTT=== "+l_username +"," +l_p1+","+l_count);
				switch (l_p1) {
				case 1:
					n1Count.put(l_username, l_count);
					logger.info("TTT+++ "+l_username +"," +l_p1+","+l_count);
					break;
				case 2:
					n2Count.put(l_username, l_count);
					logger.info("TTT+++ "+l_username +"," +l_p1+","+l_count);
					break;
				case 3:
					n3Count.put(l_username, l_count);
					logger.info("TTT+++ "+l_username +"," +l_p1+","+l_count);
					break;
				case 4:
					n4Count.put(l_username, l_count);
					logger.info("TTT+++ "+l_username +"," +l_p1+","+l_count);
					break;
				case 5:
					n5Count.put(l_username, l_count);
					logger.info("TTT+++ "+l_username +"," +l_p1+","+l_count);
					break;
				}

			}

			// 未评价数量
			for (String l_username : answerCount.keySet()) {
				int l_answerCount = answerCount.get(l_username);
				int l_totalSatisfactionCount = totalSatisfactionCount
						.get(l_username);
				int l_nnCount = l_answerCount - l_totalSatisfactionCount;
				nnCount.put(l_username, l_nnCount);
			}

			// 写入excelResult
			for (String l_username : answerCount.keySet()) {

				int l_answerCount = answerCount.get(l_username);

				int l_totalSatisfactionCount = totalSatisfactionCount
						.get(l_username);
				int l_totalSatisfactionRate = 0;
				if (l_answerCount != 0) {
					l_totalSatisfactionRate = l_totalSatisfactionCount * 100
							/ l_answerCount;
				}

				int l_n1Count = n1Count.get(l_username);
				int l_n1Rate = 0;
				if (l_totalSatisfactionCount != 0) {
					l_n1Rate = l_n1Count * 100 / l_totalSatisfactionCount;
				}

				int l_n2Count = n2Count.get(l_username);
				int l_n2Rate = 0;
				if (l_totalSatisfactionCount != 0) {
					l_n2Rate = l_n2Count * 100 / l_totalSatisfactionCount;
				}

				int l_n3Count = n3Count.get(l_username);
				int l_n3Rate = 0;
				if (l_totalSatisfactionCount != 0) {
					l_n3Rate = l_n3Count * 100 / l_totalSatisfactionCount;
				}

				int l_n4Count = n4Count.get(l_username);
				int l_n4Rate = 0;
				if (l_totalSatisfactionCount != 0) {
					l_n4Rate = l_n4Count * 100 / l_totalSatisfactionCount;
				}

				int l_n5Count = n5Count.get(l_username);
				int l_n5Rate = 0;
				if (l_totalSatisfactionCount != 0) {
					l_n5Rate = l_n5Count * 100 / l_totalSatisfactionCount;
				}

				int l_nnCount = nnCount.get(l_username);
				int l_nnRate = 0;
				if (l_answerCount != 0) {
					l_nnRate = l_nnCount * 100 / l_answerCount;
				}

				String result = l_username + "," + l_answerCount + ","
						+ l_totalSatisfactionCount + ","
						+ l_totalSatisfactionRate + "%," + l_n1Count + ","
						+ l_n1Rate + "%," + l_n2Count + "," + l_n2Rate + "%,"
						+ l_n3Count + "," + l_n3Rate + "%," + l_n4Count + ","
						+ l_n4Rate + "%," + l_n5Count + "," + l_n5Rate + "%,"
						+ l_nnCount + "," + l_nnRate + "%";

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
			cell.setCellValue("工号");

			cell = row.createCell(1);
			cell.setCellValue("接听总量");

			cell = row.createCell(2);
			cell.setCellValue("获得评价总量");

			cell = row.createCell(3);
			cell.setCellValue("获得评价率");

			cell = row.createCell(4);
			cell.setCellValue("1分数量");

			cell = row.createCell(5);
			cell.setCellValue("1分率");

			cell = row.createCell(6);
			cell.setCellValue("2分数量");

			cell = row.createCell(7);
			cell.setCellValue("2分率");

			cell = row.createCell(8);
			cell.setCellValue("3分数量");

			cell = row.createCell(9);
			cell.setCellValue("3分率");

			cell = row.createCell(10);
			cell.setCellValue("4分数量");

			cell = row.createCell(11);
			cell.setCellValue("4分率");
			
			cell = row.createCell(12);
			cell.setCellValue("5分数量");

			cell = row.createCell(13);
			cell.setCellValue("5分率");
			
			cell = row.createCell(14);
			cell.setCellValue("没有评价数量");

			cell = row.createCell(15);
			cell.setCellValue("没有评价率");

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
