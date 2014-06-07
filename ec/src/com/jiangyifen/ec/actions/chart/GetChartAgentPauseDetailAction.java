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
import com.jiangyifen.ec.beans.AgentPauseDetail;
import com.jiangyifen.ec.util.Config;
import com.opensymphony.xwork2.ActionContext;

public class GetChartAgentPauseDetailAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2285501429198686386L;

	private String title = "座席人员置忙置闲详单";

	private String beginTime;
	private String endTime;

	private String excel;

	private List<AgentPauseDetail> resultData = new ArrayList<AgentPauseDetail>();

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

			String sql = "select id,ec_user.username,name,queue,membername,pausedate,unpausedate from ec_queue_member_pause_log,ec_user where ec_user.username=ec_queue_member_pause_log.username and pausedate>='"
					+ beginTime + " 00:00:00' and pausedate<='" + endTime + " 23:59:59'";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			while (rs.next()) {

				int id = rs.getInt(1);
				String username = rs.getString(2);
				String name = rs.getString(3);
				String queue = rs.getString(4);
				String membername = rs.getString(5);
				String pausedate = rs.getString(6);
				String unpausedate = rs.getString(7);

				AgentPauseDetail aa = new AgentPauseDetail();
				aa.setId(id);
				aa.setUsername(username);
				aa.setName(name);
				aa.setQueue(queue);
				aa.setMembername(membername);
				aa.setPausedate(pausedate);
				aa.setUnpausedate(unpausedate);
				
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
			cell.setCellValue("序号");
			cell = row.createCell(1);
			cell.setCellValue("用户名/工号");
			cell = row.createCell(2);
			cell.setCellValue("姓名");
			cell = row.createCell(3);
			cell.setCellValue("队列");
			cell = row.createCell(4);
			cell.setCellValue("队列成员");
			cell = row.createCell(5);
			cell.setCellValue("置忙时间");
			cell = row.createCell(6);
			cell.setCellValue("置闲时间");

			for (int rownum = 0; rownum < resultData.size(); rownum++) {
				row = sheet.createRow(rownum + 2);
				AgentPauseDetail aa = resultData.get(rownum);

				cell = row.createCell(0);
				cell.setCellValue(aa.getId());

				cell = row.createCell(1);
				cell.setCellValue(aa.getUsername());

				cell = row.createCell(2);
				cell.setCellValue(aa.getName());

				cell = row.createCell(3);
				cell.setCellValue(aa.getQueue());

				cell = row.createCell(4);
				cell.setCellValue(aa.getMembername());

				cell = row.createCell(5);
				cell.setCellValue(aa.getPausedate());
				
				cell = row.createCell(6);
				cell.setCellValue(aa.getUnpausedate());

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
