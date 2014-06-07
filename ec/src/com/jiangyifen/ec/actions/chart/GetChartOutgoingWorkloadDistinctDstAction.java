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

public class GetChartOutgoingWorkloadDistinctDstAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5478074310158056943L;
	/**
	 * 
	 */

	private String title = "座席呼出话务量统计(唯一号码)";
	private String beginTime;
	private String endTime;
	private String groupByName;
	private String groupByTeam;
	private String average;
	private String orderByWorkload;
	private String timelen;
	private String efficiency;
	private String excel;

	private String yAxisTitle;
	private String xAxisTitle;
	private List<String> excelResult = new ArrayList<String>();

	private Set<Department> dpmts = null;

	private final Log logger = LogFactory.getLog(getClass());

	public String execute() throws Exception {

		if (excel != null && excel.equals("true")) {
			return "excel";
		} else {
			return SUCCESS;
		}
	}

	private void setAxisTitles() {
		if (efficiency != null) {
			yAxisTitle = "单次通话时长（单位：秒/次）";
		} else if (average == null || average.equals("false")) {
			if (timelen == null) {
				yAxisTitle = "呼叫次数（单位：次）";
			} else {
				yAxisTitle = "呼叫时长（单位：分钟）";
			}
		} else {
			if (timelen == null) {
				yAxisTitle = "呼叫次数（单位：次/天）";
			} else {
				yAxisTitle = "呼叫时长（单位：分钟/天）";
			}
		}
		if (groupByTeam != null && groupByTeam.equals("true")) {
			xAxisTitle = "部门";
		} else {
			xAxisTitle = "座席";
		}
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



			String p1 = "";
			String p3 = "";


			dpmts = (Set<Department>) ActionContext.getContext().getSession()
					.get("departments");

			if (groupByTeam == null || groupByTeam.equals("false")) {
				if (groupByName != null && groupByName.equals("true")) {
					p1 = "name";
				} else {
					p1 = "src";
				}
			} else {
					p1 = "dpmt";
			}

			if (orderByWorkload != null && !orderByWorkload.equals("false")) {
				p3 = "workload desc";
			} else {
				p3 = p1 + " desc";
			}

			String whereSql = "";
			if (dpmts != null && dpmts.size() > 0) {
				String s = "";
				for (Department d : dpmts) {
					s = s + "'" + d.getDepartmentname() + "',";
				}
				s = s.substring(0, s.length() - 1);

				whereSql = whereSql + " dpmt in (" + s + ") and ";
			}

			String sql1 = "select " + p1 + " , count(*) as workload from (select distinct dst, "+p1+" from cdr t where name<>'0' and username<>'0' and"
					+ " disposition='ANSWER' and length(src)<=4 and dcontext='outgoing' and  "
					+ whereSql + "t.calldate >= '" + beginTime
					+ " 00:00:00' and " + "t.calldate <= '" + endTime
					+ " 23:59:59' ) as foo " + "group by " + p1 + " " + "order by " + p3
					+ "";

			logger.info(sql1);
			statement = con.prepareStatement(sql1);
			rs = statement.executeQuery();

			dataset = new DefaultCategoryDataset();

			while (rs.next()) {
				dataset.addValue(rs.getInt(2), "接通", rs.getString(1));
			}

			String sql2 = "select " + p1 + " , count(*) as workload from (select distinct dst, "+p1+" from cdr t where name<>'0' and username<>'0' and"
			+ " disposition<>'ANSWER' and length(src)<=4 and dcontext='outgoing' and  "
			+ whereSql + "t.calldate >= '" + beginTime
			+ " 00:00:00' and " + "t.calldate <= '" + endTime
			+ " 23:59:59' ) as foo " + "group by " + p1 + " " + "order by " + p3
			+ "";

			logger.info(sql2);
			statement = con.prepareStatement(sql2);
			rs = statement.executeQuery();
			while (rs.next()) {
				dataset.addValue(rs.getInt(2), "未接通", rs.getString(1));
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);

		} finally {
			try {
				rs.close();
				statement.close();
				con.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(),e);

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


			String p1 = "";

			String p3 = "";


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
			if (groupByTeam == null || groupByTeam.equals("false")) {
				if (groupByName != null && groupByName.equals("true")) {
					p1 = "name";
				} else {
					p1 = "t.src";
				}
			} else {
				p1 = "dpmt";
			}


			p3 = p1 + " desc";

			String sql = "select " + p1 + " , count(*) as workload from (select distinct dst, "+p1+" from cdr t where "
			+ " disposition='ANSWER' and length(src)<=4 and dcontext='outgoing' and  "
			+ whereSql + "t.calldate >= '" + beginTime
			+ " 00:00:00' and " + "t.calldate <= '" + endTime
			+ " 23:59:59' ) as foo " + "group by " + p1 + " " + "order by " + p3
			+ "";

			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			while (rs.next()) {
				excelResult.add(rs.getString(1) + "," + rs.getString(2));
			}

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} finally {
			try {
				rs.close();
				statement.close();
				con.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(),e);
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
			cell = row.createCell(0);cell.setCellValue("日期");
			cell = row.createCell(1);cell.setCellValue("次数");
			
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
			
			
//			
//			pw = new PrintWriter(file,"UnicodeLittle");
//
//			getExcelDataSet();
//			setAxisTitles();
//			pw.println("部门/分机,次数");
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

	public void setGroupByTeam(String groupByTeam) {
		this.groupByTeam = groupByTeam;
	}

	public String getGroupByTeam() {
		return groupByTeam;
	}

	public void setAverage(String average) {
		this.average = average;
	}

	public String getAverage() {
		return average;
	}

	public void setOrderByWorkload(String orderByWorkload) {
		this.orderByWorkload = orderByWorkload;
	}

	public String getOrderByWorkload() {
		return orderByWorkload;
	}

	public void setTimelen(String timelen) {
		this.timelen = timelen;
	}

	public String getTimelen() {
		return timelen;
	}

	public void setExcel(String excel) {
		this.excel = excel;
	}

	public String getExcel() {
		return excel;
	}

	public void setEfficiency(String efficiency) {
		this.efficiency = efficiency;
	}

	public String getEfficiency() {
		return efficiency;
	}

	public void setDpmts(Set<Department> dpmts) {
		this.dpmts = dpmts;
	}

	public Set<Department> getDpmts() {
		return dpmts;
	}

	public void setGroupByName(String groupByName) {
		this.groupByName = groupByName;
	}

	public String getGroupByName() {
		return groupByName;
	}

}
