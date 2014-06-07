package com.jiangyifen.ec.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.dao.BlackListItem;
import com.jiangyifen.ec.util.Config;
import com.opensymphony.xwork2.ActionContext;

public class BlackListItemGetAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6088399216066337959L;

	private final Log logger = LogFactory.getLog(getClass());

	private final int PAGESIZE = 25;

	private List<BlackListItem> blackListItem;
	private String type;
	private String phoneNumber;

	private int pageIndex;
	private int blackListCount;
	
	private String excel;

	private int maxPageIndex = 0;
	private List<Integer> pages = new ArrayList<Integer>();

	public String execute() throws Exception {
		
		blackListCount = blackListItemManager.getBlackListItemCount(phoneNumber, type);
		if ((blackListCount % PAGESIZE) == 0) {
			maxPageIndex = (blackListCount / PAGESIZE);
		} else {
			maxPageIndex = (blackListCount / PAGESIZE) + 1;
		}

		for (int i = 0; i < maxPageIndex; i++) {
			pages.add(i + 1);
		}
		
		if (pageIndex < 1)
			pageIndex = 1;
		if (pageIndex > maxPageIndex)
			pageIndex = maxPageIndex;

		if (excel != null && excel.equals("true")) {
			return "excel";
		} else {
			List<BlackListItem> list = blackListItemManager.findByTypeAndPhoneNumber(PAGESIZE, maxPageIndex, type, phoneNumber);
			setBlackListItem(list);
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
			logger.info(cmd);
			Runtime.getRuntime().exec(cmd);
			Thread.sleep(1000);

			file = new File(dir + sessionID+"BlackListItem");
			file.createNewFile();

			pw = new PrintWriter(file,"UnicodeLittle");
			pw.println("PhoneNumber,Type");

			for (int i = 1; i <= maxPageIndex; i++) {
				List<BlackListItem> list = blackListItemManager.findByTypeAndPhoneNumber(PAGESIZE, i, type, phoneNumber);

				for (BlackListItem o : list) {
					String line = "\"" + o.getPhoneNumber() + "\"" + "," + "\""
							+ o.getType() + "\"";
					logger.debug(line);
					pw.println(line);
				}
				pw.flush();
			}

			fis = new FileInputStream(file);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(),e);
		}finally{
			pw.close();
		}
		return fis;
	}

	public List<BlackListItem> getBlackListItem() {
		return blackListItem;
	}

	public void setBlackListItem(List<BlackListItem> blackListItem) {
		this.blackListItem = blackListItem;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getBlackListCount() {
		return blackListCount;
	}

	public void setBlackListCount(int blackListCount) {
		this.blackListCount = blackListCount;
	}

	public String getExcel() {
		return excel;
	}

	public void setExcel(String excel) {
		this.excel = excel;
	}

	public int getMaxPageIndex() {
		return maxPageIndex;
	}

	public void setMaxPageIndex(int maxPageIndex) {
		this.maxPageIndex = maxPageIndex;
	}

	public List<Integer> getPages() {
		return pages;
	}

	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}

	public Log getLogger() {
		return logger;
	}

	public int getPAGESIZE() {
		return PAGESIZE;
	}



}
