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

public class FenduanMonth extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4073613492784250253L;

	private String title = "座席人员分段报表（月）";

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

			String sql = "select tt.username,name, to_char(date, 'YYYY') as year,to_char(date, 'MM') as month,sum(incoming_workload_count),sum(incoming_workload_billsec),sum(outgoing_workload_count),sum(outgoing_workload_billsec) from (select CASE WHEN (t_li.username is not null) THEN t_li.username ELSE t_lo.username END, CASE WHEN (t_li.date is not null) THEN t_li.date ELSE t_lo.date END , CASE WHEN (t_li.hour is not null) THEN t_li.hour ELSE t_lo.hour END , t_li.count as incoming_workload_count, t_li.billsec as incoming_workload_billsec, t_lo.count as outgoing_workload_count, t_lo.billsec as outgoing_workload_billsec from ec_report_user_incoming_workload t_li full join ec_report_user_outgoing_workload t_lo on t_li.username=t_lo.username and t_li.date=t_lo.date and t_li.hour=t_lo.hour where (t_li.date>='"
					+ beginTime
					+ " 00:00:00' and t_li.date<='"
					+ endTime
					+ " 23:59:59') or (t_lo.date>='"
					+ beginTime
					+ " 00:00:00' and t_lo.date<='"
					+ endTime
					+ " 23:59:59')) as tt full join ec_user on tt.username=ec_user.username where tt.username is not null group by tt.username,name,year,month";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			while (rs.next()) {
				String result = rs.getString(1) + "," + rs.getString(2) + ","
						+ rs.getString(3) + "," + rs.getString(4) + ","
						+ rs.getString(5) + "," + rs.getString(6) + ","
						+ rs.getString(7) + "," + rs.getString(8);
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
			cell.setCellValue("用户名");
			cell = row.createCell(1);
			cell.setCellValue("姓名");
			cell = row.createCell(2);
			cell.setCellValue("年");
			cell = row.createCell(3);
			cell.setCellValue("月");

			cell = row.createCell(4);
			cell.setCellValue("呼入次数");
			cell = row.createCell(5);
			cell.setCellValue("呼入时长");
			cell = row.createCell(6);
			cell.setCellValue("呼出次数");
			cell = row.createCell(7);
			cell.setCellValue("呼出时长");

			for (int rownum = 0; rownum < resultData.size(); rownum++) {
				try {
					row = sheet.createRow(rownum + 2);
					String resultLine = resultData.get(rownum);
					String[] result = resultLine.split(",");

					cell = row.createCell(0);
					cell.setCellValue(result[0]);

					cell = row.createCell(1);
					cell.setCellValue(result[1]);
					
					cell = row.createCell(2);
					cell.setCellValue(result[2]);
					
					cell = row.createCell(3);
					cell.setCellValue(result[3]);
					
					cell = row.createCell(4);
					cell.setCellValue(result[4]);
					
					cell = row.createCell(5);
					cell.setCellValue(result[5]);
					
					cell = row.createCell(6);
					cell.setCellValue(result[6]);

					cell = row.createCell(7);
					cell.setCellValue(result[7]);


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
