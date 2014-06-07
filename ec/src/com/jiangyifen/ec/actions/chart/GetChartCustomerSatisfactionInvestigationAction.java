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
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.beans.CustomerSatisfactionInvestigationReport;
import com.jiangyifen.ec.util.Config;
import com.opensymphony.xwork2.ActionContext;

public class GetChartCustomerSatisfactionInvestigationAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -177279826922053679L;

	private String title = "客户满意度";

	private String beginTime;
	private String endTime;

	private String excel;

	private List<CustomerSatisfactionInvestigationReport> resultData = new ArrayList<CustomerSatisfactionInvestigationReport>();

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

			String sql = "select t1.username,t2.name,date,calleridnum,exten as sipname,p1,p2 from ec_customer_satisfaction_investigation_log t1,ec_user t2 where t1.username=t2.username and date>='"
					+ beginTime
					+ " 00:00:00' and date<='"
					+ endTime
					+ " 23:59:59'";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			while (rs.next()) {
				try {
					String username = rs.getString(1);
					String name = rs.getString(2);
					String date = rs.getString(3);
					String calleridnum = rs.getString(4);
					String sipname = rs.getString(5);
					int p1 = rs.getInt(6);
					int p2 = rs.getInt(7);

					CustomerSatisfactionInvestigationReport aa = new CustomerSatisfactionInvestigationReport();
					aa.setUsername(username);
					aa.setName(name);
					aa.setDate(date);
					aa.setCalleridnum(calleridnum);
					aa.setSipname(sipname);
					aa.setP1(p1);
					aa.setP2(p2);

					resultData.add(aa);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
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

		FileOutputStream out = null;
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
			cell.setCellValue("用户名/工号");
			cell = row.createCell(1);
			cell.setCellValue("姓名");
			cell = row.createCell(2);
			cell.setCellValue("日期");
			cell = row.createCell(3);
			cell.setCellValue("电话号码");
			cell = row.createCell(4);
			cell.setCellValue("分机号码");
			cell = row.createCell(5);
			cell.setCellValue("p1");
			cell = row.createCell(6);
			cell.setCellValue("p2");

			for (int rownum = 0; rownum < resultData.size(); rownum++) {
				row = sheet.createRow(rownum + 2);
				CustomerSatisfactionInvestigationReport aa = resultData
						.get(rownum);

				cell = row.createCell(0);
				cell.setCellValue(aa.getUsername());

				cell = row.createCell(1);
				cell.setCellValue(aa.getName());

				cell = row.createCell(2);
				cell.setCellValue(aa.getDate());

				cell = row.createCell(3);
				cell.setCellValue(aa.getCalleridnum());

				cell = row.createCell(4);
				cell.setCellValue(aa.getSipname());

				cell = row.createCell(5);
				cell.setCellValue(aa.getP1());

				cell = row.createCell(6);
				cell.setCellValue(aa.getP2());
			}

			out = new FileOutputStream(file);
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
