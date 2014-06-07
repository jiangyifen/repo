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
import java.util.Properties;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
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

public class GetChartWeightedWorkloadAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4512536656495984565L;

	private String title = "加权工作量统计";
	
	private String beginTime;
	private String endTime;
	private String excel;

	private String yAxisTitle;
	private String xAxisTitle;
	private List<String> excelResult = new ArrayList<String>();

	private Set<Department> dpmts = null;

	private double maxValue = 0;
	
	private final Log logger = LogFactory.getLog(getClass());

	public String execute() throws Exception {
		if (excel != null && excel.equals("true")) {
			return "excel";
		} else {
			return SUCCESS;
		}
	}

	private void setAxisTitles() {

		yAxisTitle = "加权工作量";

		xAxisTitle = "座席";

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
			String sql = "select name, (count(*) + round(sum(t.billsec)/60,2))/2 as workload from cdr t where name<>'0' and username<>'0' and " + whereSql
					+ "t.calldate >= '" + beginTime + " 00:00:00' and "
					+ "t.calldate <= '" + endTime + " 23:59:59' "
					+ "group by name order by workload desc";

			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			dataset = new DefaultCategoryDataset();

			while (rs.next()) {
				String name = rs.getString(1);
				if (name == null) {
					name = "0";
				}
				int value = rs.getInt(2);
				dataset.addValue(value, "", name);
				maxValue = Math.max(maxValue, value);
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
	
		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setUpperBound(maxValue*1.2);
		
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
			String sql = "select name, (count(*) + round(sum(t.billsec)/60,2))/2 as workload from cdr t where name<>'0' and username<>'0' and " + whereSql
			+ "t.calldate >= '" + beginTime + " 00:00:00' and "
			+ "t.calldate <= '" + endTime + " 23:59:59' "
			+ "group by name order by workload desc";

			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			while (rs.next()) {
				
				String name = rs.getString(1);
				if (name == null) {
					name = "0";
				}
				
				excelResult.add(name + "," + rs.getString(2));
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

//		PrintWriter pw = null;
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
			cell = row.createCell(0);cell.setCellValue(title);
			
			row = sheet.createRow(1);
			cell = row.createCell(0);cell.setCellValue("部门/分机");
			cell = row.createCell(1);cell.setCellValue("加权工作量（（呼叫次数+呼叫时长分钟）/2）");
			
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
//			pw.println("部门/分机,次数,时长（分钟）,平均时长");
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
