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
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.MyStringUtils;
import com.opensymphony.xwork2.ActionContext;

public class GetChartWorkloadAverageTimelineAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8448183275234487730L;
	private String title = "平均工作量统计";
	
	private String beginTime;
	private String endTime;
	private String excel;

	private String yAxisTitle;
	private String xAxisTitle;
	private List<String> excelResult = new ArrayList<String>();

	private Set<Department> dpmts = null;

	private double maxValue = 0;
	
	public String execute() throws Exception {

		if (excel != null && excel.equals("true")) {
			return "excel";
		} else {
			return SUCCESS;
		}
	}

	private void setAxisTitles() {
		yAxisTitle = "人均工作量";

		xAxisTitle = "日期";

	}

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

			String sql = "select substring(t.calldate from 0 for 11) , round(sum(t.billsec)/60,2), count(*) "
					+ "from cdr t where t.calldate >= '"
					+ beginTime
					+ " 00:00:00' and t.calldate <= '"
					+ endTime
					+ " 23:59:59' group by substring(t.calldate from 0 for 11) order by substring(t.calldate from 0 for 11) ";

			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			dataset = new DefaultCategoryDataset();

			ResultSet countRS = null;
			int count = 0;
			while (rs.next()) {
				sql = "select count(*) from (select distinct username from cdr where cdr.calldate>='"
						+ rs.getString(1)
						+ " 00:00:00' and cdr.calldate<='"
						+ rs.getString(1) + " 23:59:59') as t";
				statement = con.prepareStatement(sql);
				countRS = statement.executeQuery();
				countRS.next();
				count = countRS.getInt(1);
				if (count == 0)
					count = 999999;
				dataset.addValue(rs.getInt(3) / count, "Call Times",
						rs.getString(1));
				dataset.addValue(rs.getInt(2) / count, "Time Length",
						rs.getString(1));
				maxValue = Math.max(maxValue, Math.max(rs.getInt(3) / count, rs.getInt(2) / count));
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

		return dataset;

	}

	public JFreeChart getChart() throws IOException {

		setAxisTitles();
		JFreeChart chart = ChartFactory.createLineChart("标题", xAxisTitle,
				yAxisTitle, getDataSet(), PlotOrientation.VERTICAL, true,
				false, false);
		chart.setTitle(new TextTitle(beginTime + " 00:00:00 ～" + endTime
				+ " 23:59:59 " + title, new Font("黑体", Font.ITALIC, 22)));

		CategoryPlot plot = (CategoryPlot) chart.getPlot();

		CategoryAxis categoryAxis = plot.getDomainAxis();
		categoryAxis.setLabelFont(new Font("宋体", Font.BOLD, 18));
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		categoryAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));
		
		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setUpperBound(maxValue*1.2);
		
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		numberAxis.setLabelFont(new Font("宋体", Font.BOLD, 18));

		return chart;

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

			String sql = "select substring(t.calldate from 0 for 11) , round(sum(t.billsec)/60,2), count(*) "
					+ "from cdr t where t.calldate >= '"
					+ beginTime
					+ " 00:00:00' and t.calldate <= '"
					+ endTime
					+ " 23:59:59' group by substring(t.calldate from 0 for 11) order by substring(t.calldate from 0 for 11) ";

			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			ResultSet countRS = null;
			int count = 0;
			while (rs.next()) {
				sql = "select count(*) from (select distinct username from cdr where cdr.calldate>='"
						+ rs.getString(1)
						+ " 00:00:00' and cdr.calldate<='"
						+ rs.getString(1) + " 23:59:59') as t";
				statement = con.prepareStatement(sql);
				countRS = statement.executeQuery();
				countRS.next();
				count = countRS.getInt(1);
				if (count == 0)
					count = 1;

				excelResult.add(rs.getString(1) + "," + rs.getInt(2) / count
						+ "," + rs.getInt(3) / count);
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

//		PrintWriter pw = null;
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
			cell = row.createCell(0);cell.setCellValue(title);
			
			row = sheet.createRow(1);
			cell = row.createCell(0);cell.setCellValue("日期");
			cell = row.createCell(1);cell.setCellValue("人均时长（分钟）");
			cell = row.createCell(2);cell.setCellValue("人均次数");
			
			for(int rownum = 0;rownum<excelResult.size();rownum++){
				row = sheet.createRow(rownum + 2);
				String[] line = excelResult.get(rownum).split(",");
				for(int cellnum=0;cellnum<line.length;cellnum++){
					cell = row.createCell(cellnum);
					String value = line[cellnum];
					if(MyStringUtils.isNumeric(value))
						cell.setCellValue(new Double(value));
					else
						cell.setCellValue(value);
				}
			}
			
			FileOutputStream out = new FileOutputStream(file);
			wb.write(out);
			out.close();
			
			

//			pw = new PrintWriter(file,"UnicodeLittle");
//
//			getExcelDataSet();
//			setAxisTitles();
//			pw.println("日期,人均时长（分钟）,人均次数");
//			for (String line : excelResult) {
//				logger.debug(line);
//				pw.println(line);
//			}
//			pw.flush();
			
			fis = new FileInputStream(file);
			
			
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}catch (Exception e) {
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

	public void setDpmts(Set<Department> dpmts) {
		this.dpmts = dpmts;
	}

	public Set<Department> getDpmts() {
		return dpmts;
	}
}
