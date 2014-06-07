package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.List;

import com.jiangyifen.ec.dao.DialTaskLog;

public class DialTaskLogsGetAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 283779535261691717L;

	private List<DialTaskLog> dialTaskLogs;

	private String dialTaskName;
	private String beginTime;
	private String endTime;
	private int pageIndex = 1;
	private final int PAGESIZE = 100;
	private int maxPageIndex = 0;
	private List<Integer> pages = new ArrayList<Integer>();

	public String execute() throws Exception {

		// 获取dialtasklog
		if ((dialTaskLogManager.getDialTaskLogCount() % PAGESIZE) == 0) {
			maxPageIndex = (int) (dialTaskLogManager.getDialTaskLogCount() / PAGESIZE);
		} else {
			maxPageIndex = (int) (dialTaskLogManager.getDialTaskLogCount() / PAGESIZE) + 1;
		}

		for (int i = 0; i < maxPageIndex; i++) {
			pages.add(i + 1);
		}
		if (pageIndex > maxPageIndex)
			pageIndex = maxPageIndex;
		if (pageIndex < 1)
			pageIndex = 1;

		List<DialTaskLog> list = dialTaskLogManager.getDialTaskLogs(
				dialTaskName, beginTime, endTime, PAGESIZE, pageIndex);
		for (DialTaskLog log : list) {
			if (log.getDialTaskItemFinishedCount() != 0) {
				double d = log.getCustomerCount() * 10000
						/ log.getDialTaskItemFinishedCount();
				log.setCustomerRate(d / 100);
			} else {
				log.setCustomerRate(0);
			}
			if (log.getDialTaskItemTotalCount() != 0) {
				double d = log.getDialTaskItemFinishedCount() * 10000
						/ log.getDialTaskItemTotalCount();
				log.setFinishRate(d / 100);
			} else {
				log.setFinishRate(0);
			}
		}

		setDialTaskLogs(list);
		return SUCCESS;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setMaxPageIndex(int maxPageIndex) {
		this.maxPageIndex = maxPageIndex;
	}

	public int getMaxPageIndex() {
		return maxPageIndex;
	}

	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}

	public List<Integer> getPages() {
		return pages;
	}

	public void setDialTaskLogs(List<DialTaskLog> dialTaskLogs) {
		this.dialTaskLogs = dialTaskLogs;
	}

	public List<DialTaskLog> getDialTaskLogs() {
		return dialTaskLogs;
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

	public void setDialTaskName(String dialTaskName) {
		this.dialTaskName = dialTaskName;
	}

	public String getDialTaskName() {
		return dialTaskName;
	}

}
