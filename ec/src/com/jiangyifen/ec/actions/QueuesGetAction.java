package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.List;

import com.jiangyifen.ec.dao.Queue;

public class QueuesGetAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 765263402653530273L;

	private List<Queue> queues;
	private int pageIndex = 1;
	private final int PAGESIZE = 10;
	private int maxPageIndex = 0;
	private List<Integer> pages = new ArrayList<Integer>();

	public String execute() throws Exception {
		if ((queueManager.getQueueCount() % PAGESIZE) == 0) {
			maxPageIndex = (queueManager.getQueueCount() / PAGESIZE);
		} else {
			maxPageIndex = (queueManager.getQueueCount() / PAGESIZE) + 1;
		}

		for (int i = 0; i < maxPageIndex; i++) {
			pages.add(i + 1);
		}
		if (pageIndex > maxPageIndex)
			pageIndex = maxPageIndex;
		if (pageIndex < 1)
			pageIndex = 1;

		List<Queue> list = queueManager.getQueuesByPage(PAGESIZE, pageIndex);

		setQueues(list);
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



	public void setQueues(List<Queue> queues) {
		this.queues = queues;
	}



	public List<Queue> getQueues() {
		return queues;
	}


}
