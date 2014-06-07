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
import com.jiangyifen.ec.beans.AgentMinLoginTimeAndMaxLogoutTime;
import com.jiangyifen.ec.util.Config;
import com.opensymphony.xwork2.ActionContext;

public class GetChartAgentMinLoginTimeAndMaxLogoutTimeAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5935287945075774293L;

	private String title = "座席人员上下线时间";

	private String beginTime;
	private String endTime;

	private String excel;

	private List<AgentMinLoginTimeAndMaxLogoutTime> resultData = new ArrayList<AgentMinLoginTimeAndMaxLogoutTime>();

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

			String sql = "select t1.username,name,substring(logindate from 1 for 10) as d,substring(min(logindate) from 12 for 8) as minlogin,substring(max(logoutdate) from 12 for 8) as maxlogout from ec_user_login_record t1,ec_user t2 WHERE t1.username=t2.username and substring(logindate from 1 for 10)>='"+beginTime+"' and substring(logindate from 1 for 10)<='"+endTime+"' group by t1.username,name,d order by d,t1.username;";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			while (rs.next()) {

				
				String username = rs.getString(1);
				String name = rs.getString(2);
				String date = rs.getString(3);
				String minLoginTime = rs.getString(4);
				String maxLogoutTime = rs.getString(5);

				AgentMinLoginTimeAndMaxLogoutTime aa = new AgentMinLoginTimeAndMaxLogoutTime();
				aa.setUsername(username);
				aa.setName(name);
				aa.setDate(date);
				aa.setMinLoginTime(minLoginTime);
				aa.setMaxLogoutTime(maxLogoutTime);

				resultData.add(aa);
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
			cell.setCellValue("用户名/工号");
			cell = row.createCell(1);
			cell.setCellValue("姓名");
			cell = row.createCell(2);
			cell.setCellValue("日期");
			cell = row.createCell(3);
			cell.setCellValue("首次登陆时间");
			cell = row.createCell(4);
			cell.setCellValue("末次登出时间");

			for (int rownum = 0; rownum < resultData.size(); rownum++) {
				row = sheet.createRow(rownum + 2);
				AgentMinLoginTimeAndMaxLogoutTime aa = resultData.get(rownum);

				cell = row.createCell(0);
				cell.setCellValue(aa.getUsername());
				
				cell = row.createCell(1);
				cell.setCellValue(aa.getName());
				
				cell = row.createCell(2);
				cell.setCellValue(aa.getDate());
				
				cell = row.createCell(3);
				cell.setCellValue(aa.getMinLoginTime());
				
				cell = row.createCell(4);
				cell.setCellValue(aa.getMaxLogoutTime());
				
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
