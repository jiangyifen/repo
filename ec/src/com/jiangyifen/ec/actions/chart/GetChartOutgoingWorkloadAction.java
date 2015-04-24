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
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.MyStringUtils;
import com.jiangyifen.ec.util.ShareData;
import com.opensymphony.xwork2.ActionContext;

public class GetChartOutgoingWorkloadAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8430226916697155730L;
	private String title = "座席呼出话务量统计";

	private String beginTime;
	private String endTime;
	private String groupByName;
	private String groupByTeam;
	private String secOrMin;

	private String orderByWorkload;

	private String excel;

	private List<String> excelResult = new ArrayList<String>();

	private Set<Department> dpmts = null;

	private double maxValue1 = 0;
	private double maxValue2 = 0;

	DefaultCategoryDataset dataset1;
	DefaultCategoryDataset dataset2;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public String execute() throws Exception {
		logger.info("----");
		
		
		logger.info("groupByName="+groupByName+" groupByName==null? "+(groupByName==null));

		logger.info("secOrMin="+secOrMin+" secOrMin==null? "+(secOrMin==null));
		
		logger.info("----");
		
		
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

			dpmts = (Set<Department>) ActionContext.getContext().getSession()
					.get("departments");

			if (groupByTeam == null || groupByTeam.equals("false")) {
				if (groupByName != null && groupByName.equals("true")) {
					p1 = "name";
				} else {
					p1 = "t.src";
				}
			} else {
				p1 = "dpmt";
			}

			if (orderByWorkload != null && !orderByWorkload.equals("false")) {
				p3 = "workload desc";
			} else {
				p3 = p1 + " desc";
			}

			int t = 60;
			if(secOrMin==null){
				t = 1;
			}
			
			String whereSql = "";
			if (dpmts != null && dpmts.size() > 0) {
				String s = "";
				for (Department d : dpmts) {
					s = s + "'" + d.getDepartmentname() + "',";
				}
				s = s.substring(0, s.length() - 1);

				whereSql = whereSql + "dpmt in (" + s + ") and ";

			}
			String sip = "";
			for (String sipname : ShareData.allSipName) {
				sip = sip + "'" + sipname + "',";
			}
			sip = sip.substring(0, sip.length() - 1);
			whereSql = whereSql + "src in (" + sip + ") and ";

			String sql1 = "select "
					+ p1                                          
					+ " , count(*) as workload, round(sum(t.duration)/"+t+",2) from cdr t where  " //name<>'0' and username<>'0' and
					+ "disposition='ANSWER' and dcontext='outgoing' and "
					+ whereSql + "t.calldate >= '" + beginTime
					+ " 00:00:00' and " + "t.calldate <= '" + endTime
					+ " 23:59:59' " + "group by " + p1 + " order by " + p3;

			logger.info(sql1);
			statement = con.prepareStatement(sql1);
			rs = statement.executeQuery();

			while (rs.next()) {
				String name = rs.getString(1);
				if (name == null) {
					name = "0";
				}
				dataset1.addValue(rs.getDouble(2), "接通次数", name);
				maxValue1 = Math.max(maxValue1, new Double(rs.getDouble(2)));
				dataset2.addValue(rs.getDouble(3), "接通时长", name);
				maxValue2 = Math.max(maxValue2, new Double(rs.getDouble(3)));
			}

			String sql2 = "select "
					+ p1
					+ " , count(*) as workload, round(sum(t.duration)/"+t+",2) from cdr t where  " //name<>'0' and username<>'0' and
					+ "disposition<>'ANSWER' and dcontext='outgoing' and "
					+ whereSql + "t.calldate >= '" + beginTime
					+ " 00:00:00' and " + "t.calldate <= '" + endTime
					+ " 23:59:59' " + "group by " + p1 + " order by " + p3;

			logger.info(sql2);
			statement = con.prepareStatement(sql2);
			rs = statement.executeQuery();
			while (rs.next()) {
				String name = rs.getString(1);
				if (name == null) {
					name = "0";
				}
				dataset1.addValue(rs.getDouble(2), "未接通次数", name);
				maxValue1 = Math.max(maxValue1, new Double(rs.getDouble(2)));
				dataset2.addValue(rs.getDouble(3), "未接通时长", name);
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

		final NumberAxis rangeAxis2 = new NumberAxis("呼叫时长（分钟）");
		rangeAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis2.setUpperBound(maxValue2 * 1.2);
		final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
		renderer2
				.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		renderer2.setBaseItemLabelsVisible(true);
		renderer2
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer2.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
		final CategoryPlot subplot2 = new CategoryPlot(dataset2, null,
				rangeAxis2, renderer2);
		subplot2.setDomainGridlinesVisible(true);

		final CategoryAxis domainAxis = new CategoryAxis("Category");
//      domainAxis.setLabelFont(new Font("宋体", Font.BOLD, 18));
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
//        domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));
		final CombinedDomainCategoryPlot plot = new CombinedDomainCategoryPlot(
				domainAxis);
		plot.add(subplot1, 2);
		plot.add(subplot2, 1);

		final JFreeChart result = new JFreeChart("", new Font("黑体",
				Font.ITALIC, 22), plot, true);
		result.setTitle(new TextTitle(beginTime + "~" + endTime + title,
				new Font("黑体", Font.ITALIC, 22)));

		return result;

	}

	@SuppressWarnings("unchecked")
	private void getExcelDataSet() {

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		HashMap<String,String> result1 = new HashMap<String, String>();
		HashMap<String,String> result2 = new HashMap<String, String>();

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
				if (groupByName != null ) {
					p1 = "u.name";
				} else {
					p1 = "t.src";
				}
			} else {
				p1 = "dpmt";
			}
//			p1="u.name";//强制按姓名

			if (orderByWorkload != null && !orderByWorkload.equals("false")) {
				p3 = "workload desc";
			} else {
				p3 = p1 + " desc";
			}
			
			int t = 60;
			if(secOrMin==null){
				t = 1;
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
			String sip = "";
			for (String sipname : ShareData.allSipName) {
				sip = sip + "'" + sipname + "',";
			}
			sip = sip.substring(0, sip.length() - 1);
			whereSql = whereSql + " src in (" + sip + ") and ";

			String sql1 = "select src,"
					+ p1
					+ " , count(*) as workload, round(sum(t.duration)/"+t+",2) from cdr t where "// name<>'0' and username<>'0' and
					+ " disposition='ANSWER' and dcontext='outgoing' and "
					+ whereSql + "t.calldate >= '" + beginTime
					+ " 00:00:00' and " + "t.calldate <= '" + endTime
					+ " 23:59:59' " + "group by src order by src";

			
			
			logger.info(sql1);
			statement = con.prepareStatement(sql1);
			rs1 = statement.executeQuery();

			while (rs1.next()) {
				String l_username = rs1.getString(1);
				if (l_username == null) {
					l_username = "0";
				}
				result1.put(l_username,rs1.getString(2) + ","+ rs1.getString(3)+ ","+ rs1.getString(4));
			}
			
			String sql2 = "select src,"
					+ p1
					+ " , count(*) as workload, round(sum(t.duration)/"+t+",2) from cdr t where "//name<>'0' and username<>'0' and 
					+ " disposition<>'ANSWER' and dcontext='outgoing' and  "
					+ whereSql + "t.calldate >= '" + beginTime
					+ " 00:00:00' and " + "t.calldate <= '" + endTime
					+ " 23:59:59' " + "group by src order by src";

			logger.info(sql2);
			statement = con.prepareStatement(sql2);
			rs2 = statement.executeQuery();

			while (rs2.next()) {
				String l_username = rs2.getString(1);
				if (l_username == null) {
					l_username = "0";
				}
				result2.put(l_username,  rs2.getString(3)+ ","+ rs2.getString(4));
			}
			
			HashMap<String,String> temp = new HashMap<String,String>();
			for(String key:result1.keySet()){
				temp.put(key, null);
			}
			for(String key:result2.keySet()){
				temp.put(key, null);
			}
			for(String key:temp.keySet()){
				String answer = result1.get(key);
				String noanswer = result2.get(key);
				if(answer==null)
					answer = "0,0";
				if(noanswer==null)
					noanswer="0,0";
				
				excelResult.add(key+","+answer+","+noanswer);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

		} finally {
			try {
				rs1.close();
				rs2.close();
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
			cell.setCellValue(beginTime + "~" + endTime + title);

			row = sheet.createRow(1);
			
			cell = row.createCell(0);
			cell.setCellValue("用户名");
			
			cell = row.createCell(1);
			cell.setCellValue("部门/分机/姓名");
			
			cell = row.createCell(2);
			cell.setCellValue("接通次数");
			
			cell = row.createCell(3);
			if(secOrMin==null){
				cell.setCellValue("接通时长（秒）");
			}else{
			    cell.setCellValue("接通时长（分钟）");
			}
			
			cell = row.createCell(4);
			cell.setCellValue("未接通次数");
			
			cell = row.createCell(5);
			if(secOrMin==null){
				cell.setCellValue("未接通时长（秒）");
			}else{
				cell.setCellValue("未接通时长（分钟）");
			}
			

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

			//
			// pw = new PrintWriter(file,"UnicodeLittle");
			//
			// getExcelDataSet();
			// setAxisTitles();
			// pw.println("部门/分机,次数,时长（分钟）");
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

	public void setGroupByName(String groupByName) {
		this.groupByName = groupByName;
	}

	public String getGroupByName() {
		return groupByName;
	}

	public String getSecOrMin() {
		return secOrMin;
	}

	public void setSecOrMin(String secOrMin) {
		this.secOrMin = secOrMin;
	}

}
