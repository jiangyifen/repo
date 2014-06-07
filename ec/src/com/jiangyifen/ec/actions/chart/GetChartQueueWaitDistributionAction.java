package com.jiangyifen.ec.actions.chart;

import java.awt.Font;
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
import java.util.Date;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.util.Config;
import com.opensymphony.xwork2.ActionContext;

public class GetChartQueueWaitDistributionAction extends BaseAction {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3791684495649607778L;

	private String title = "队列等待时长分布";

	private String beginTime;
	private String endTime;
	private String queuename;

	private String excel;

	private String yAxisTitle;
	private int[] wait1 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0 };
	private int[] wait2 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0 };
	private int[] wait3 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0 };

	public String execute() throws Exception {
		logger.info(beginTime);
		logger.info(endTime);
		if (excel != null && excel.equals("true")) {
			return "excel";
		} else {
			return SUCCESS;
		}
	}

	private void setAxisTitles() {

		yAxisTitle = "等待次数（单位：次）";

	}

	private CategoryDataset getDataSet() {
		DefaultCategoryDataset dataset = null;

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;

		try {

			String url = Config.props.getProperty("pg_url");
			String username = Config.props.getProperty("pg_username");
			String password = Config.props.getProperty("pg_password");

			Class.forName("org.postgresql.Driver");

			con = DriverManager.getConnection(url, username, password);

			String sql;
			sql = "select substring(t.datereceived::text from 12 for 2), count(*) from ec_queue_entry_event_log t where t.datereceived >= '"
					+ beginTime
					+ " 00:00:00' and t.datereceived <= '"
					+ endTime
					+ " 23:59:59' and wait<=5 group by substring(t.datereceived::text from 12 for 2) order by substring(t.datereceived::text from 12 for 2)";
			//and queue='"+queuename+"'
			statement = con.prepareStatement(sql);
			rs1 = statement.executeQuery();

			sql = "select substring(t.datereceived::text from 12 for 2), count(*) from ec_queue_entry_event_log t where t.datereceived >= '"
				+ beginTime
				+ " 00:00:00' and t.datereceived <= '"
				+ endTime
				+ " 23:59:59' and wait>5 and wait<=20 group by substring(t.datereceived::text from 12 for 2) order by substring(t.datereceived::text from 12 for 2)";
			//and queue='"+queuename+"'
			statement = con.prepareStatement(sql);
			rs2 = statement.executeQuery();

			sql = "select substring(t.datereceived::text from 12 for 2), count(*) from ec_queue_entry_event_log t where t.datereceived >= '"
				+ beginTime
				+ " 00:00:00' and t.datereceived <= '"
				+ endTime
				+ " 23:59:59' and wait>20 group by substring(t.datereceived::text from 12 for 2) order by substring(t.datereceived::text from 12 for 2)";
			//and queue='"+queuename+"'
			statement = con.prepareStatement(sql);
			rs3 = statement.executeQuery();

			dataset = new DefaultCategoryDataset();

			while (rs1.next()) {
				Integer i = new Integer(rs1.getString(1));
				wait1[i.intValue()] = Integer.valueOf(rs1.getInt(2)).intValue();
			}
			while (rs2.next()) {
				Integer i = new Integer(rs2.getString(1));
				wait2[i.intValue()] = Integer.valueOf(rs2.getInt(2)).intValue();
			}
			while (rs3.next()) {
				Integer i = new Integer(rs3.getString(1));
				wait3[i.intValue()] = Integer.valueOf(rs3.getInt(2)).intValue();
			}

			for (int i = 0; i < wait1.length; i++) {
				dataset.addValue(wait1[i], "小于5秒", i + "");
				dataset.addValue(wait2[i], "5～20秒", i + "");
				dataset.addValue(wait3[i], "20秒以上", i + "");
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				rs1.close();
				rs2.close();
				rs3.close();
				statement.close();
				con.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}

		}

		return dataset;

	}

	private void getExcelDataSet() {

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;

		try {

			String url = Config.props.getProperty("pg_url");
			String username = Config.props.getProperty("pg_username");
			String password = Config.props.getProperty("pg_password");

			Class.forName("org.postgresql.Driver");

			con = DriverManager.getConnection(url, username, password);



			String sql;
			sql = "select substring(t.datereceived::text from 12 for 2), count(*) from ec_queue_entry_event_log t where t.datereceived >= '"
					+ beginTime
					+ " 00:00:00' and t.datereceived <= '"
					+ endTime
					+ " 23:59:59' and wait<=5 group by substring(t.datereceived::text from 12 for 2) order by substring(t.datereceived::text from 12 for 2)";

			statement = con.prepareStatement(sql);
			rs1 = statement.executeQuery();

			sql = "select substring(t.datereceived::text from 12 for 2), count(*) from ec_queue_entry_event_log t where t.datereceived >= '"
				+ beginTime
				+ " 00:00:00' and t.datereceived <= '"
				+ endTime
				+ " 23:59:59' and wait>5 and wait<=20 group by substring(t.datereceived::text from 12 for 2) order by substring(t.datereceived::text from 12 for 2)";
			statement = con.prepareStatement(sql);
			rs2 = statement.executeQuery();

			sql = "select substring(t.datereceived::text from 12 for 2), count(*) from ec_queue_entry_event_log t where t.datereceived >= '"
				+ beginTime
				+ " 00:00:00' and t.datereceived <= '"
				+ endTime
				+ " 23:59:59' and wait>20 group by substring(t.datereceived::text from 12 for 2) order by substring(t.datereceived::text from 12 for 2)";
			statement = con.prepareStatement(sql);
			rs3 = statement.executeQuery();

			while (rs1.next()) {
				Integer i = new Integer(rs1.getString(1));
				wait1[i.intValue()] = Integer.valueOf(rs1.getInt(2)).intValue();

			}
			while (rs2.next()) {
				Integer i = new Integer(rs2.getString(1));
				wait2[i.intValue()] = Integer.valueOf(rs2.getInt(2)).intValue();

			}
			while (rs3.next()) {
				Integer i = new Integer(rs3.getString(1));
				wait3[i.intValue()] = Integer.valueOf(rs3.getInt(2)).intValue();

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				rs1.close();
				rs2.close();
				rs3.close();
				statement.close();
				con.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}

		}

	}

	public JFreeChart getChart() throws IOException {

		setAxisTitles();

		JFreeChart chart = ChartFactory.createBarChart3D("标题", "时间段",
				yAxisTitle, getDataSet(), PlotOrientation.VERTICAL, true,
				false, false);
		chart.setTitle(new TextTitle(beginTime + " 00:00:00 ～" + endTime
				+ " 23:59:59 " + title, new Font("黑体", Font.ITALIC, 22)));

		StackedBarRenderer3D renderer = new StackedBarRenderer3D();

		renderer.setBaseItemLabelsVisible(true);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setRenderer(renderer);

		CategoryAxis categoryAxis = plot.getDomainAxis();
		categoryAxis.setLabelFont(new Font("宋体", Font.BOLD, 18));
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		categoryAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));

		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		numberAxis.setLabelFont(new Font("宋体", Font.BOLD, 18));

		LegendTitle legend = chart.getLegend();
		legend.setItemFont(new Font("宋体", Font.BOLD, 12));

		return chart;

	}

	public InputStream getExcelFile() {

		// PrintWriter pw = null;
		String recLocalDiskPath = null;
		Properties props = null;
		FileInputStream fis = null;
		File file = null;
		try {
			props = new Properties();
			String path = getClass().getClassLoader().getResource("").getPath()
					+ "config.properties";
			fis = new FileInputStream(path);
			props.load(fis);
			recLocalDiskPath = props.getProperty("rec_local_disk_path");

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
			cell.setCellValue(title);

			row = sheet.createRow(1);
			cell = row.createCell(0);
			cell.setCellValue("时段");
			cell = row.createCell(1);
			cell.setCellValue("次数（小于5秒）");
			cell = row.createCell(2);
			cell.setCellValue("次数（5至20秒）");
			cell = row.createCell(3);
			cell.setCellValue("次数（20秒以上）");

			int i = 0;
			for (i = 0; i < 24; i++) {
				row = sheet.createRow(i + 2);
				cell = row.createCell(0);
				cell.setCellValue(i);
				cell = row.createCell(1);
				cell.setCellValue(wait1[i]);
				cell = row.createCell(2);
				cell.setCellValue(wait2[i]);
				cell = row.createCell(3);
				cell.setCellValue(wait3[i]);

			}
			row = sheet.createRow(i + 2);
			cell = row.createCell(0);
			cell.setCellValue("合计");
			cell = row.createCell(1);
			cell.setCellValue(sum(wait1));
			cell = row.createCell(2);
			cell.setCellValue(sum(wait2));
			cell = row.createCell(3);
			cell.setCellValue(sum(wait3));

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

	private int sum(int[] data) {
		int sum = 0;
		for (int d : data) {
			sum = sum + d;
		}
		return sum;
	}

	public String getQueuename() {
		return queuename;
	}

	public void setQueuename(String queuename) {
		this.queuename = queuename;
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
