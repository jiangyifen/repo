package com.jiangyifen.ec.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.dao.Cdr;
import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.util.Config;
import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings("unchecked")
public class GetCdrAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -635523577048381048L;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final int PAGESIZE = 25;

	private List<Cdr> cdr;
	private String src;
	private String dst;
	private String fenji;
	private String beginTime;
	private String endTime;
	private String moreThen;
	private String lessThen;
	private String moreThenUnit;
	private String lessThenUnit;
	private String moreThenValue;
	private String lessThenValue;
	private String andor;
	private String direction;
	private String hid;
	private String disposition;
	private int pageIndex;
	private int cdrCount;
	private String excel;
	private String xml;

	private int maxPageIndex = 0;
	private List<Integer> pages = new ArrayList<Integer>();

	private Set<Department> dpmts = null;

	public String execute() throws Exception {

		if (moreThen != null && lessThen != null && moreThenUnit != null
				&& lessThenUnit != null) {
			try {
				moreThenValue = Integer.valueOf(moreThen)
						* Integer.valueOf(moreThenUnit) + "";
				lessThenValue = Integer.valueOf(lessThen)
						* Integer.valueOf(lessThenUnit) + "";
			} catch (NumberFormatException e) {

			}
		}

		dpmts = (Set<Department>) ActionContext.getContext().getSession()
				.get("departments");

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1,date2;
		try {
			date1 = df.parse(beginTime + " 00:00:00");
		} catch (ParseException e) {
			date1 = new Date();
		}

		try {
			date2 = df.parse(beginTime + " 00:00:00");
		} catch (ParseException e) {
			date2 = new Date();
		}
		
		long l = date2.getTime() - date1.getTime();
		long days = (l / (24 * 60 * 60 * 1000) + 1);

		if (days > 100) {
			return "to many results";
		}

		if (pageIndex < 1)
			pageIndex = 1;

		cdrCount = cdrManager.getCdrCount(beginTime, endTime, src, dst, fenji,
				direction, hid, disposition, moreThenValue, lessThenValue,
				andor, null, dpmts);
		if ((cdrCount % PAGESIZE) == 0) {
			maxPageIndex = (cdrCount / PAGESIZE);
		} else {
			maxPageIndex = (cdrCount / PAGESIZE) + 1;
		}

		for (int i = 0; i < maxPageIndex; i++) {
			pages.add(i + 1);
		}
		if (pageIndex > maxPageIndex)
			pageIndex = maxPageIndex;

		if (excel != null && excel.equals("true")) {
			return "excel";
		} else {
			List<Cdr> list = cdrManager.findCdr(beginTime, endTime, src, dst,
					fenji, direction, hid, disposition, moreThenValue,
					lessThenValue, andor, null, dpmts, PAGESIZE, pageIndex);
			setCdr(list);
			logger.info("GetCdrAction: succeed");
			return SUCCESS;
		}
	}

	public InputStream getExcelFile() {

		PrintWriter pw = null;
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
			logger.info("GetCdrAction: "+cmd);
			Runtime.getRuntime().exec(cmd);
			Thread.sleep(1000);
			
			int l_PAGESIZE = 20000;
			int l_maxPageIndex = 0;
			

			cdrCount = cdrManager.getCdrCount(beginTime, endTime, src, dst, fenji,
					direction, hid, disposition, moreThenValue, lessThenValue,
					andor, null, dpmts);
			if ((cdrCount % l_PAGESIZE) == 0) {
				l_maxPageIndex = (cdrCount / l_PAGESIZE);
			} else {
				l_maxPageIndex = (cdrCount / l_PAGESIZE) + 1;
			}
			
			file = new File(dir + sessionID+System.currentTimeMillis());
			file.createNewFile();

			pw = new PrintWriter(file, "UnicodeLittle");
			pw.println("calldate,src,dst,fenji,context,duration,billsec,disposition,name,dpmt,url");

			logger.info("GetCdrAction: l_maxPageIndex="+l_maxPageIndex);
			for (int i = 1; i <= l_maxPageIndex; i++) {
				List<Cdr> list = cdrManager.findCdr(beginTime, endTime, src,
						dst, fenji, direction, hid, disposition, moreThenValue,
						lessThenValue, andor, null, dpmts, l_PAGESIZE, i);

				for (Cdr cdr : list) {
					String line = cdr.getCalldate() + "," + cdr.getSrc() + ","
							+ cdr.getDst() + "," + cdr.getFenji() + ","
							+ cdr.getDcontext() + "," + cdr.getDuration() + ","
							+ cdr.getBillsec() + "," + cdr.getDisposition()
							+ "," + cdr.getName() + "," + cdr.getDpmt()+","+cdr.getUrl();
//					logger.info("GetCdrAction: "+line);
					pw.println(line);
				}
				logger.info("GetCdrAction: "+i);
				pw.flush();
			}
			pw.close();
			fis = new FileInputStream(file);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}finally{
			
			
		}
		return fis;
	}

	public void setCdr(List<Cdr> cdr) {
		this.cdr = cdr;
	}

	public List<Cdr> getCdr() {
		return cdr;
	}

	public void setMaxPageIndex(int maxPageIndex) {
		this.maxPageIndex = maxPageIndex;
	}

	public long getMaxPageIndex() {
		return maxPageIndex;
	}

	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}

	public List<Integer> getPages() {
		return pages;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getSrc() {
		return src;
	}

	public void setDst(String dst) {
		this.dst = dst;
	}

	public String getDst() {
		return dst;
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

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setCdrCount(int cdrCount) {
		this.cdrCount = cdrCount;
	}

	public int getCdrCount() {
		return cdrCount;
	}

	public void setExcel(String excel) {
		this.excel = excel;
	}

	public String getExcel() {
		return excel;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDirection() {
		return direction;
	}

	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}

	public String getDisposition() {
		return disposition;
	}

	public void setDpmts(Set<Department> dpmts) {
		this.dpmts = dpmts;
	}

	public Set<Department> getDpmts() {
		return dpmts;
	}

	public void setMoreThen(String moreThen) {
		this.moreThen = moreThen;
	}

	public String getMoreThen() {
		return moreThen;
	}

	public void setMoreThenValue(String moreThenValue) {
		this.moreThenValue = moreThenValue;
	}

	public String getMoreThenValue() {
		return moreThenValue;
	}

	public void setLessThen(String lessThen) {
		this.lessThen = lessThen;
	}

	public String getLessThen() {
		return lessThen;
	}

	public void setLessThenValue(String lessThenValue) {
		this.lessThenValue = lessThenValue;
	}

	public String getLessThenValue() {
		return lessThenValue;
	}

	public void setAndor(String andor) {
		this.andor = andor;
	}

	public String getAndor() {
		return andor;
	}

	public void setMoreThenUnit(String moreThenUnit) {
		this.moreThenUnit = moreThenUnit;
	}

	public String getMoreThenUnit() {
		return moreThenUnit;
	}

	public void setLessThenUnit(String lessThenUnit) {
		this.lessThenUnit = lessThenUnit;
	}

	public String getLessThenUnit() {
		return lessThenUnit;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getXml() {
		return xml;
	}

	public void setFenji(String fenji) {
		this.fenji = fenji;
	}

	public String getFenji() {
		return fenji;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}

	public String getHid() {
		return hid;
	}

}
