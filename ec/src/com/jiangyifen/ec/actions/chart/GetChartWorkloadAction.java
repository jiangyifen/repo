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
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.MyStringUtils;
import com.opensymphony.xwork2.ActionContext;

public class GetChartWorkloadAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8919377575931942992L;
	private String title = "综合工作量统计";

	private String beginTime;
	private String endTime;
	private String groupByTeam;
	private String orderByWorkload;

	private String excel;

	private List<String> excelResult = new ArrayList<String>();

	private Set<Department> dpmts = null;

	private double maxValue1 = 0;
	private double maxValue2 = 0;

	DefaultCategoryDataset dataset1;
	DefaultCategoryDataset dataset2;

	private final Log logger = LogFactory.getLog(getClass());

	public String execute() throws Exception {
		if (excel != null && excel.equals("true")) {
			return "excel";
		} else {
			return SUCCESS;
		}
	}

	@SuppressWarnings("unchecked")
	private void createDataset() {
		dataset1 = new DefaultCategoryDataset();
		dataset2 = new DefaultCategoryDataset();

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

			if (groupByTeam == null || groupByTeam.equals("false")) {
				p1 = "name";
			} else {
				p1 = "dpmt";
			}

			if (orderByWorkload != null && !orderByWorkload.equals("false")) {
				p3 = "workload desc";
			} else {
				p3 = p1 + " desc";
			}
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
			String sql = "select "
					+ p1
					+ " , count(*), round(sum(t.duration)/60,0) as workload from cdr t where disposition='ANSWER' and dcontext in ('incoming','outgoing') and name<>'0' and username<>'0' and "
					+ whereSql + "t.calldate >= '" + beginTime
					+ " 00:00:00' and " + "t.calldate <= '" + endTime
					+ " 23:59:59' " + "group by " + p1 + " order by " + p3;

			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			while (rs.next()) {
				String name = rs.getString(1);
				if (name == null) {
					name = "0";
				}
				dataset1.addValue(rs.getDouble(2), "呼叫次数", name);
				maxValue1 = Math.max(maxValue1, new Double(rs.getDouble(2)));
				dataset2.addValue(rs.getDouble(3), "呼叫时长", name);
				maxValue2 = Math.max(maxValue2, new Double(rs.getDouble(3)));
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

		final NumberAxis rangeAxis1 = new NumberAxis("呼叫次数（次）");
		rangeAxis1.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis1.setUpperBound(maxValue1 * 1.2);
		final BarRenderer renderer1 = new BarRenderer();
		renderer1.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		renderer1.setBaseItemLabelsVisible(true);
		renderer1.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer1.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
		final CategoryPlot subplot1 = new CategoryPlot(dataset1, null, rangeAxis1, renderer1);
		subplot1.setDomainGridlinesVisible(true);

		final NumberAxis rangeAxis2 = new NumberAxis("呼叫时长（分钟）");
		rangeAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis2.setUpperBound(maxValue2 * 1.2);
		final BarRenderer renderer2 = new BarRenderer();
		renderer2.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		renderer2.setBaseItemLabelsVisible(true);
		renderer2.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer2.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
		final CategoryPlot subplot2 = new CategoryPlot(dataset2, null, rangeAxis2, renderer2);
		subplot2.setDomainGridlinesVisible(true);

		final CategoryAxis domainAxis = new CategoryAxis("Category");
//      domainAxis.setLabelFont(new Font("宋体", Font.BOLD, 18));
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
//        domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));
		final CombinedDomainCategoryPlot plot = new CombinedDomainCategoryPlot(domainAxis);
		plot.add(subplot1, 2);
		plot.add(subplot2, 1);

		final JFreeChart result = new JFreeChart("", new Font("黑体",
				Font.ITALIC, 22), plot, true);
		result.setTitle(new TextTitle(beginTime + "~" + endTime + title, new Font("黑体", Font.ITALIC, 22)));

		return result;

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

			if (groupByTeam == null || groupByTeam.equals("false")) {
				p1 = "name";
			} else {
				p1 = "dpmt";
			}

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
			String sql = "select "
					+ p1
					+ " , count(*), round(sum(t.duration)/60,2),round(sum(t.duration)/60/count(*),2) as workload from cdr t where disposition='ANSWER' and dcontext in ('incoming','outgoing') and name<>'0' and username<>'0' and "
					+ whereSql + "t.calldate >= '" + beginTime
					+ " 00:00:00' and " + "t.calldate <= '" + endTime
					+ " 23:59:59' " + "group by " + p1 + " order by " + p1
					+ " desc";

			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			while (rs.next()) {

				String name = rs.getString(1);
				if (name == null) {
					name = "0";
				}

				excelResult.add(name + "," + rs.getString(2) + ","
						+ rs.getString(3) + "," + rs.getString(4));
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
			cell.setCellValue("部门/分机");
			cell = row.createCell(1);
			cell.setCellValue("次数");
			cell = row.createCell(2);
			cell.setCellValue("时长（分钟）");
			cell = row.createCell(3);
			cell.setCellValue("平均时长");

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

			// pw = new PrintWriter(file,"UnicodeLittle");
			//
			// getExcelDataSet();
			// setAxisTitles();
			// pw.println("部门/分机,次数,时长（分钟）,平均时长");
			// for (String line : excelResult) {
			// logger.debug(line);
			// pw.println(line);
			// }
			// pw.flush();
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

	public void setGroupByTeam(String groupByTeam) {
		this.groupByTeam = groupByTeam;
	}

	public String getGroupByTeam() {
		return groupByTeam;
	}

	public void setMaxValue2(double maxValue2) {
		this.maxValue2 = maxValue2;
	}

	public double getMaxValue2() {
		return maxValue2;
	}

}
