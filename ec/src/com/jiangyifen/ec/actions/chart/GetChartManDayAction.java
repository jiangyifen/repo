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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainCategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.MyStringUtils;
import com.opensymphony.xwork2.ActionContext;

public class GetChartManDayAction extends BaseAction {



	/**
	 * 
	 */
	private static final long serialVersionUID = -6214785907130192523L;

	private String title = "人天数统计";

	private String beginTime;
	private String endTime;

	private String excel;

	private List<String> excelResult = new ArrayList<String>();

	private double maxValue1 = 0;

	DefaultCategoryDataset dataset1;

	public String execute() throws Exception {

		if (excel != null && excel.equals("true")) {
			return "excel";
		} else {
			return SUCCESS;
		}
	}

	private void createDataset() {
		dataset1 = new DefaultCategoryDataset();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {

			String url = Config.props.getProperty("pg_url");
			String username = Config.props.getProperty("pg_username");
			String password = Config.props.getProperty("pg_password");

			Class.forName("org.postgresql.Driver");

			con = DriverManager.getConnection(url, username, password);

			String sql = "select date,count(*) from (select substring(calldate,1,10) as date,username from cdr where calldate>='"
					+ beginTime
					+ " 00:00:00' and calldate<='"
					+ endTime
					+ " 23:59:59' group by date,username order by date) as foo group by foo.date order by foo.date";

			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			while (rs.next()) {
				dataset1.addValue(rs.getInt(2), "人天数", rs.getString(1));
				maxValue1 = Math.max(maxValue1, new Double(rs.getInt(3)));

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

	public JFreeChart getChart() throws IOException {

		createDataset();

		final NumberAxis rangeAxis1 = new NumberAxis("人天");
		rangeAxis1.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis1.setUpperBound(maxValue1 * 1.2);
		final LineAndShapeRenderer renderer1 = new LineAndShapeRenderer();
		renderer1
				.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		renderer1.setBaseItemLabelsVisible(true);
		renderer1
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer1.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
		final CategoryPlot subplot1 = new CategoryPlot(dataset1, null,
				rangeAxis1, renderer1);
		subplot1.setDomainGridlinesVisible(true);

		final CategoryAxis domainAxis = new CategoryAxis("Category");
		// domainAxis.setLabelFont(new Font("宋体", Font.BOLD, 18));
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		// domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));
		final CombinedDomainCategoryPlot plot = new CombinedDomainCategoryPlot(
				domainAxis);
		plot.add(subplot1, 2);

		final JFreeChart result = new JFreeChart("", new Font("黑体",
				Font.ITALIC, 22), plot, true);
		result.setTitle(new TextTitle(title, new Font("黑体", Font.ITALIC, 22)));

		return result;

	}

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

			String sql = "select date,dpmt,count(*) from (select substring(calldate,1,10) as date,dpmt,username from cdr where calldate>='"
					+ beginTime
					+ " 00:00:00' and calldate<='"
					+ endTime
					+ " 23:59:59' group by date,dpmt,username order by date) as foo group by foo.date,foo.dpmt order by foo.date";
			
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			while (rs.next()) {
				excelResult.add(rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3));
			}

		}

		catch (Exception e) {
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
			cell.setCellValue(title);

			row = sheet.createRow(1);
			cell = row.createCell(0);
			cell.setCellValue("日期");
			cell = row.createCell(1);
			cell.setCellValue("部门");
			cell = row.createCell(2);
			cell.setCellValue("人天数");


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

	public void setExcel(String excel) {
		this.excel = excel;
	}

	public String getExcel() {
		return excel;
	}

}
