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
import com.jiangyifen.ec.beans.AgentAssessment;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.MyStringUtils;
import com.opensymphony.xwork2.ActionContext;

public class GetChartAgentAssessmentAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2180982628627894792L;

	private String title = "座席人员考核数据";

	private String beginTime;
	private String endTime;
	private String average;

	private String excel;

	private List<AgentAssessment> resultData = new ArrayList<AgentAssessment>();

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

		String sql = "select t.date as date,ec_user.username as username,ec_user.name as name,to_char(logintimelength, 'HH24:MI:SS') as logintimelength,to_char(pausetimelength, 'HH24:MI:SS') as pausetimelength,round((cast (EXTRACT(EPOCH from pausetimelength)/EXTRACT(EPOCH from logintimelength) as numeric))*100,2) as pauserate,incoming_workload_count,incoming_workload_billsec, (incoming_workload_billsec/incoming_workload_count) as incoming_workload_avg,outgoing_workload_count,outgoing_workload_billsec,(outgoing_workload_billsec/outgoing_workload_count) as outgoing_workload_avg,incoming_workload_count+outgoing_workload_count as total_workload_count,incoming_workload_billsec+outgoing_workload_billsec as total_workload_billsec,(incoming_workload_billsec+outgoing_workload_billsec)/(incoming_workload_count+outgoing_workload_count) as total_workload_avg, logintimelength as hidden_login_time_length, pausetimelength as hidden_pause_time_length from ec_user full join(select CASE WHEN (t1.username is not null) THEN t1.username ELSE t2.username END,CASE WHEN (t1.date is not null) THEN t1.date ELSE t2.date END,logintimelength,pausetimelength,incoming_workload_count,incoming_workload_billsec,outgoing_workload_count,outgoing_workload_billsec from (select username,date,logintimelength,pausetimelength from (select CASE WHEN(t_login.username is not null) THEN t_login.username ELSE t_pause.username END,CASE WHEN (t_login.date is not null) THEN t_login.date ELSE t_pause.date END ,t_login.timelength as logintimelength,t_pause.timelength as pausetimelength from ec_report_user_login_timelength t_login full join ec_report_queue_user_pause t_pause on t_login.username=t_pause.username and t_login.date=t_pause.date where (t_login.date>='"
				+ beginTime
				+ " 00:00:00' and t_login.date<='"
				+ endTime
				+ " 23:59:59') or (t_pause.date>='"
				+ beginTime
				+ " 00:00:00' and t_pause.date<='"
				+ endTime
				+ " 23:59:59')) as onlinetime) as t1 FULL JOIN (select username,date,incoming_workload_count,incoming_workload_billsec,outgoing_workload_count,outgoing_workload_billsec from (select CASE WHEN (t_li.username is not null) THEN t_li.username ELSE t_lo.username END,CASE WHEN (t_li.date is not null) THEN t_li.date ELSE t_lo.date END ,t_li.count as incoming_workload_count,t_li.billsec as incoming_workload_billsec,t_lo.count as outgoing_workload_count,t_lo.billsec as outgoing_workload_billsec from ec_report_user_incoming_workload t_li full join ec_report_user_outgoing_workload t_lo on t_li.username=t_lo.username and t_li.date=t_lo.date and t_li.hour=t_lo.hour where (t_li.date>='"
				+ beginTime
				+ " 00:00:00' and t_li.date<='"
				+ endTime
				+ " 23:59:59') or (t_lo.date>='"
				+ beginTime
				+ " 00:00:00' and t_lo.date<='"
				+ endTime
				+ " 23:59:59')) as workload) as t2 on t1.username=t2.username and t1.date=t2.date ) as t on ec_user.username=t.username where ec_user.username is not null and t.date is not null order by date,username";

		try {

			String pg_url = Config.props.getProperty("pg_url");
			String pg_username = Config.props.getProperty("pg_username");
			String pg_password = Config.props.getProperty("pg_password");

			Class.forName("org.postgresql.Driver");

			con = DriverManager.getConnection(pg_url, pg_username, pg_password);

			if (average != null) {
				sql = "SELECT '"
						+ beginTime
						+ "~"
						+ endTime
						+ "',username,name, to_char(avg(hidden_login_time_length),'HH24:MI:SS') as avg_login_time_length,to_char(avg(hidden_pause_time_length),'HH24:MI:SS') as avg_pause_time_length,round((cast (EXTRACT(EPOCH from avg(hidden_pause_time_length))/EXTRACT(EPOCH from avg(hidden_login_time_length)) as numeric))*100,2) as pauserate,round(avg(incoming_workload_count),2) as avg_incoming_count,round(avg(incoming_workload_billsec),0) as avg_incoming_billsec,(sum(incoming_workload_billsec)/sum(incoming_workload_count)) as avg_incoming_billsec_single,round(avg(outgoing_workload_count),2) as avg_outgoing_count,round(avg(outgoing_workload_billsec),0) as avg_outgoing_billsec,(sum(outgoing_workload_billsec)/sum(outgoing_workload_count)) as avg_outgoing_billsec_single,round(avg(total_workload_count),2) as avg_total_count,round(avg(total_workload_billsec),0) as avg_total_billsec,(sum(total_workload_billsec)/sum(total_workload_count)) as avg_total_billsec_single from ("
						+ sql
						+ ") as total group by username,name order by username,name";
			}
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			while (rs.next()) {

				String date = rs.getString(1);
				String username = rs.getString(2);
				String name = rs.getString(3);
				String logintimelength = rs.getString(4);
				String pausetimelength = rs.getString(5);
				String pauserate = rs.getString(6) + "%";
				String incomingWorkloadCount = rs.getString(7);
				String incomingWorkloadBillsec = MyStringUtils
						.formatIntToHHmmss(rs.getInt(8));
				String incomingWorkloadAvg = MyStringUtils.formatIntToHHmmss(rs
						.getInt(9));
				String outgoingWorkloadCount = rs.getString(10);
				String outgoingWorkloadBillsec = MyStringUtils
						.formatIntToHHmmss(rs.getInt(11));
				String outgoingWorkloadAvg = MyStringUtils.formatIntToHHmmss(rs
						.getInt(12));
				String totalWorkloadCount = rs.getString(13);
				String totalWorkloadBillsec = MyStringUtils
						.formatIntToHHmmss(rs.getInt(14));
				String totalWorkloadAvg = MyStringUtils.formatIntToHHmmss(rs
						.getInt(15));

				AgentAssessment aa = new AgentAssessment();
				aa.setDate(date);
				aa.setUsername(username);
				aa.setName(name);
				aa.setLogintimelength(logintimelength);
				aa.setPausetimelength(pausetimelength);
				aa.setPauserate(pauserate);
				aa.setIncomingWorkloadCount(incomingWorkloadCount);
				aa.setIncomingWorkloadBillsec(incomingWorkloadBillsec);
				aa.setIncomingWorkloadAvg(incomingWorkloadAvg);
				aa.setOutgoingWorkloadCount(outgoingWorkloadCount);
				aa.setOutgoingWorkloadBillsec(outgoingWorkloadBillsec);
				aa.setOutgoingWorkloadAvg(outgoingWorkloadAvg);
				aa.setTotalWorkloadCount(totalWorkloadCount);
				aa.setTotalWorkloadBillsec(totalWorkloadBillsec);
				aa.setTotalWorkloadAvg(totalWorkloadAvg);

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
			String cmd = "mkdir " + dir + " -p";
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
			cell.setCellValue("用户名/工号");
			cell = row.createCell(2);
			cell.setCellValue("姓名");
			cell = row.createCell(3);
			cell.setCellValue("在线时长");
			cell = row.createCell(4);
			cell.setCellValue("置忙时长");
			cell = row.createCell(5);
			cell.setCellValue("置忙率");
			cell = row.createCell(6);
			cell.setCellValue("呼入次数");
			cell = row.createCell(7);
			cell.setCellValue("呼入时长");
			cell = row.createCell(8);
			cell.setCellValue("平均单次呼入时长");
			cell = row.createCell(9);
			cell.setCellValue("呼出次数");
			cell = row.createCell(10);
			cell.setCellValue("呼出时长");
			cell = row.createCell(11);
			cell.setCellValue("平均单次呼出时长");
			cell = row.createCell(12);
			cell.setCellValue("总次数");
			cell = row.createCell(13);
			cell.setCellValue("总时长");
			cell = row.createCell(14);
			cell.setCellValue("平均单次时长");

			for (int rownum = 0; rownum < resultData.size(); rownum++) {
				row = sheet.createRow(rownum + 2);
				AgentAssessment aa = resultData.get(rownum);

				cell = row.createCell(0);
				cell.setCellValue(aa.getDate());
				cell = row.createCell(1);
				cell.setCellValue(aa.getUsername());
				cell = row.createCell(2);
				cell.setCellValue(aa.getName());
				cell = row.createCell(3);
				cell.setCellValue(aa.getLogintimelength());
				cell = row.createCell(4);
				cell.setCellValue(aa.getPausetimelength());
				cell = row.createCell(5);
				cell.setCellValue(aa.getPauserate());
				cell = row.createCell(6);
				cell.setCellValue(aa.getIncomingWorkloadCount());
				cell = row.createCell(7);
				cell.setCellValue(aa.getIncomingWorkloadBillsec());
				cell = row.createCell(8);
				cell.setCellValue(aa.getIncomingWorkloadAvg());
				cell = row.createCell(9);
				cell.setCellValue(aa.getOutgoingWorkloadCount());
				cell = row.createCell(10);
				cell.setCellValue(aa.getOutgoingWorkloadBillsec());
				cell = row.createCell(11);
				cell.setCellValue(aa.getOutgoingWorkloadAvg());
				cell = row.createCell(12);
				cell.setCellValue(aa.getTotalWorkloadCount());
				cell = row.createCell(13);
				cell.setCellValue(aa.getTotalWorkloadBillsec());
				cell = row.createCell(14);
				cell.setCellValue(aa.getTotalWorkloadAvg());
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

	public void setAverage(String average) {
		this.average = average;
	}

	public String getAverage() {
		return average;
	}

}
