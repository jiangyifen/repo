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

public class GetChartWorkloadTimelineAction extends BaseAction {



	/**
	 * 
	 */
	private static final long serialVersionUID = -4792276850155631158L;

	private String title = "工作量时间线";
	
	private String beginTime;
	private String endTime;

	private String excel;

	private List<String> excelResult = new ArrayList<String>();

	private double maxValue1 = 0;
	private double maxValue2 = 0;

	DefaultCategoryDataset dataset1;
	DefaultCategoryDataset dataset2;
	
	public String execute() throws Exception {

		if (excel != null && excel.equals("true")) {
			return "excel";
		} else {
			return SUCCESS;
		}
	}

	
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

				String sql = "select substring(t.calldate from 0 for 11), count(*), round(sum(t.billsec)/60,2)"
					+ "from cdr t where t.calldate >= '"
					+ beginTime
					+ " 00:00:00' and t.calldate <= '"
					+ endTime
					+ " 23:59:59' group by substring(t.calldate from 0 for 11) order by substring(t.calldate from 0 for 11) ";
				
				logger.info(sql);
				statement = con.prepareStatement(sql);
				rs = statement.executeQuery();

				while (rs.next()) {
					dataset1.addValue(rs.getInt(2), "呼叫次数", rs.getString(1));
					maxValue1 = Math.max(maxValue1, new Double(rs.getInt(2)));
					dataset2.addValue(rs.getInt(3), "呼叫时长", rs.getString(1));
					maxValue2 = Math.max(maxValue2, new Double(rs.getInt(3)));
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
        rangeAxis1.setUpperBound(maxValue1*1.2);
        final LineAndShapeRenderer renderer1 = new LineAndShapeRenderer();
        renderer1.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		renderer1.setBaseItemLabelsVisible(true);
		renderer1.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer1.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
        final CategoryPlot subplot1 = new CategoryPlot(dataset1, null, rangeAxis1, renderer1);
        subplot1.setDomainGridlinesVisible(true);
		
        final NumberAxis rangeAxis2 = new NumberAxis("呼叫时长（秒）");
        rangeAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis2.setUpperBound(maxValue2*1.2);
        final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        renderer2.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		renderer2.setBaseItemLabelsVisible(true);
		renderer2.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer2.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
        final CategoryPlot subplot2 = new CategoryPlot(dataset2, null, rangeAxis2, renderer2);
        subplot2.setDomainGridlinesVisible(true);

        
        final CategoryAxis domainAxis = new CategoryAxis("Category");
//        domainAxis.setLabelFont(new Font("宋体", Font.BOLD, 18));
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
//        domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));
        final CombinedDomainCategoryPlot plot = new CombinedDomainCategoryPlot(domainAxis);
        plot.add(subplot1, 2);
        plot.add(subplot2, 1);
        
        final JFreeChart result = new JFreeChart(
            "",
            new Font("黑体", Font.ITALIC, 22),
            plot,
            true
        );
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

			String sql = "select substring(t.calldate from 0 for 11), count(*), round(sum(t.billsec)/60,2)"
				+ "from cdr t where t.calldate >= '"
				+ beginTime
				+ " 00:00:00' and t.calldate <= '"
				+ endTime
				+ " 23:59:59' group by substring(t.calldate from 0 for 11) order by substring(t.calldate from 0 for 11) ";
			
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			while (rs.next()) {
				excelResult.add(rs.getString(1) + "," + rs.getString(2)+ "," + rs.getString(3));
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
			cell = row.createCell(0);cell.setCellValue(title);
			
			row = sheet.createRow(1);
			cell = row.createCell(0);cell.setCellValue("日期");
			cell = row.createCell(1);cell.setCellValue("呼叫次数");
			cell = row.createCell(2);cell.setCellValue("呼叫时长");
			
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

}
