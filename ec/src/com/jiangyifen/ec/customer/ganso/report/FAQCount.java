package com.jiangyifen.ec.customer.ganso.report;

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

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.util.Config;
import com.opensymphony.xwork2.ActionContext;

public class FAQCount extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2285501429198686386L;

	private String title = "FAQ统计";

	private String beginTime;
	private String endTime;

	private String excel;

	private List<String> resultData = new ArrayList<String>();

	public String execute() throws Exception {
		getData();

		if (excel != null && excel.equals("true")) {
			return "excel";
		} else {
			return SUCCESS;
		}
	}

	private void getData() {

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {

			String pg_url = Config.props.getProperty("pg_url");
			String pg_username = Config.props.getProperty("pg_username");
			String pg_password = Config.props.getProperty("pg_password");

			Class.forName("org.postgresql.Driver");

			con = DriverManager.getConnection(pg_url, pg_username, pg_password);

			String sql = "select substring(calldate from 0 for 11),count(*) from cdr where calldate>='"
					+ beginTime
					+ " 00:00:00' and calldate<='"
					+ endTime
					+ " 23:59:59' and dcontext='incoming' and disposition='NO ANSWER' and lastapp='Playback' and lastdata like '%welcome%' group by substring(calldate from 0 for 11) order by substring(calldate from 0 for 11);";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			while (rs.next()) {
				String result = rs.getString(1) + "," + rs.getString(2);
				resultData.add(result);
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

			Workbook wb = new HSSFWorkbook();
			Sheet sheet = wb.createSheet();
			Row row = null;
			Cell cell = null;

			row = sheet.createRow(0);
			cell = row.createCell(0);
			cell.setCellValue(title);

			row = sheet.createRow(1);

			cell = row.createCell(0);
			cell.setCellValue("日期");
			cell = row.createCell(1);
			cell.setCellValue("次数");

			for (int rownum = 0; rownum < resultData.size(); rownum++) {
				try {
					row = sheet.createRow(rownum + 2);
					String resultLine = resultData.get(rownum);
					String[] result = resultLine.split(",");

					cell = row.createCell(0);
					cell.setCellValue(result[0]);

					cell = row.createCell(1);
					cell.setCellValue(result[1]);

				} catch (Exception e) {
					logger.error(e.getMessage(), e);
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

	public void setExcel(String excel) {
		this.excel = excel;
	}

	public String getExcel() {
		return excel;
	}

}
