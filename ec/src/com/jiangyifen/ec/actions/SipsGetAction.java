package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.List;

import com.jiangyifen.ec.dao.Sip;

public class SipsGetAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4421866192055179720L;

	private List<Sip> sips;
	private int pageIndex = 1;
	private final int PAGESIZE = 10;
	private int maxPageIndex = 0;
	private List<Integer> pages = new ArrayList<Integer>();

	public String execute() throws Exception {
		if ((sipManager.getSipCount() % PAGESIZE) == 0) {
			maxPageIndex = (sipManager.getSipCount() / PAGESIZE);
		} else {
			maxPageIndex = (sipManager.getSipCount() / PAGESIZE) + 1;
		}

		for (int i = 0; i < maxPageIndex; i++) {
			pages.add(i + 1);
		}
		if (pageIndex > maxPageIndex)
			pageIndex = maxPageIndex;
		if (pageIndex < 1)
			pageIndex = 1;

		List<Sip> list = sipManager.getSipsByPage(PAGESIZE, pageIndex);
		setSips(list);
		return SUCCESS;
	}

	public void setSips(List<Sip> sips) {
		this.sips = sips;
	}

	public List<Sip> getSips() {
		return sips;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public int getPAGESIZE() {
		return PAGESIZE;
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

}
