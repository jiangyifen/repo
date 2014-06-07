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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.MyStringUtils;
import com.opensymphony.xwork2.ActionContext;

public class GetChartSMSendQuantityAction extends BaseAction {


	/**
	 * 
	 */
	private static final long serialVersionUID = 8666814561420418727L;

	private String title = "短信发送量";

	private String beginTime;
	private String endTime;
	private String groupByTeam;
	private String orderByWorkload;
	private String excel;

	private String yAxisTitle;
	private String xAxisTitle;
	private List<String> excelResult = new ArrayList<String>();

	private Set<Department> dpmts = null;

	public String execute() throws Exception {
		if (excel != null && excel.equals("true")) {
			return "excel";
		} else {
			return SUCCESS;
		}
	}

	private void setAxisTitles() {

		yAxisTitle = "短信发送量（单位：次）";
		xAxisTitle = "发送者";

	}

	@SuppressWarnings("unchecked")
	private CategoryDataset getDataSet() {
		DefaultCategoryDataset dataset = null;

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {

			String url = Config.props.getProperty("pg_url");
			String username = Config.props.getProperty("pg_username");
			String password = Config.props.getProperty("pg_password");

			Class.forName("org.postgresql.Driver");

			con = DriverManager.getConnection(url, username, password);

			dpmts = (Set<Department>) ActionContext.getContext().getSession()
					.get("departments");

			String whereSql = "";
			if (dpmts != null && dpmts.size() > 0) {
				String s = "";
				for (Department d : dpmts) {
					s = s + "'" + d.getDepartmentname() + "',";
				}
				s = s.substring(0, s.length() - 1);

				whereSql = whereSql + " dpmt in (" + s + ") and ";
			}

			String sql = "select department, count(*) as c from ec_sm_task where "
					+ whereSql
					+ " department is not null and timestamp>='"
					+ beginTime
					+ "' and timestamp<='"
					+ endTime
					+ "' and status in ('DELIVRD','0') group by department;";

			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			dataset = new DefaultCategoryDataset();

			while (rs.next()) {
				String dpmtName = rs.getString(1);
				if (dpmtName == null) {
					dpmtName = "0";
				}
				dataset.addValue(rs.getInt(2), "", dpmtName);
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

		return dataset;

	}

	public JFreeChart getChart() throws IOException {

		setAxisTitles();
		JFreeChart chart = ChartFactory.createBarChart3D("标题", xAxisTitle,
				yAxisTitle, getDataSet(), PlotOrientation.VERTICAL, false,
				false, false);
		chart.setTitle(new TextTitle(beginTime + " 00:00:00 ～" + endTime
				+ " 23:59:59 " + title, new Font("黑体", Font.ITALIC, 22)));

		BarRenderer3D renderer = new BarRenderer3D();
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

		return chart;

	}

	@SuppressWarnings("unchecked")
	private void getExcelDataSet() {

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {

			String url = Config.props.getProperty("pg_url");
			String username = Config.props.getProperty("pg_username");
			String password = Config.props.getProperty("pg_password");

			Class.forName("org.postgresql.Driver");

			con = DriverManager.getConnection(url, username, password);

			dpmts = (Set<Department>) ActionContext.getContext().getSession()
					.get("departments");
			
			String whereSql = "";
			if (dpmts != null && dpmts.size() > 0) {
				String s = "";
				for (Department d : dpmts) {
					s = s + "'" + d.getDepartmentname() + "',";
				}
				s = s.substring(0, s.length() - 1);

				whereSql = whereSql + " dpmt in (" + s + ") and ";
			}

			String sql = "select department, count(*) as c from ec_sm_task where "
				+ whereSql
				+ " department is not null and timestamp>='"
				+ beginTime
				+ "' and timestamp<='"
				+ endTime
				+ "' and status in ('DELIVRD','0') group by department;";

			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			while (rs.next()) {
				String dpmtName = rs.getString(1);
				if (dpmtName == null) {
					dpmtName = "0";
				}
				excelResult.add(dpmtName + "," + rs.getString(2));
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
			logger.info("QueueIncoming:getExcelDataSet()");
			getExcelDataSet();
			logger.info("QueueIncoming:new xls");
			Workbook wb = new HSSFWorkbook();
			Sheet sheet = wb.createSheet();
			Row row = null;
			Cell cell = null;

			row = sheet.createRow(0);
			cell = row.createCell(0);
			cell.setCellValue(title);

			row = sheet.createRow(1);
			cell = row.createCell(0);
			cell.setCellValue("部门");
			cell = row.createCell(1);
			cell.setCellValue("次数");

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

	public void setGroupByTeam(String groupByTeam) {
		this.groupByTeam = groupByTeam;
	}

	public String getGroupByTeam() {
		return groupByTeam;
	}


	public void setOrderByWorkload(String orderByWorkload) {
		this.orderByWorkload = orderByWorkload;
	}

	public String getOrderByWorkload() {
		return orderByWorkload;
	}


	public void setExcel(String excel) {
		this.excel = excel;
	}

	public String getExcel() {
		return excel;
	}

	public void setDpmts(Set<Department> dpmts) {
		this.dpmts = dpmts;
	}

	public Set<Department> getDpmts() {
		return dpmts;
	}


}
