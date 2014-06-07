package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.List;

import com.jiangyifen.ec.dao.DialTask;
import com.jiangyifen.ec.dao.DialTaskItem;
import com.jiangyifen.ec.dao.Queue;

public class DialTaskGetOneAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5650457433316760084L;
	private String id;
	private List<DialTask> dialTasks = new ArrayList<DialTask>();
	
	private List<Queue> queues = new ArrayList<Queue>();
	
	private List<DialTaskItem> dialTaskItems;
	private int dialTaskItemCount;
	private int pageIndex = 1;
	private final int PAGESIZE = 15;
	private int maxPageIndex = 0;

	private List<Integer> pages = new ArrayList<Integer>();
	
	
	
	public String execute() throws Exception {

		setQueues(queueManager.getQueues());

		dialTasks.add(dialTaskManager.get(new Long(id)));
		
		long dialTaskId = new Long(id);
			
		dialTaskItemCount = (int)dialTaskItemManager.getDialTaskItemCount(dialTaskId);
		if ((dialTaskItemCount % PAGESIZE) == 0) {
			maxPageIndex = dialTaskItemCount / PAGESIZE;
		} else {
			maxPageIndex = (dialTaskItemCount / PAGESIZE) + 1;
		}

		for (int i = 0; i < maxPageIndex; i++) {
			pages.add(i + 1);
		}
		if (pageIndex > maxPageIndex)
			pageIndex = maxPageIndex;
		if (pageIndex < 1)
			pageIndex = 1;

		List<DialTaskItem> list = dialTaskItemManager.findByTaskId(PAGESIZE, pageIndex, dialTaskId);

		setDialTaskItems(list);
		
		return SUCCESS;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setDialTasks(List<DialTask> dialTasks) {
		this.dialTasks = dialTasks;
	}

	public List<DialTask> getDialTasks() {
		return dialTasks;
	}

	public void setQueues(List<Queue> queues) {
		this.queues = queues;
	}

	public List<Queue> getQueues() {
		return queues;
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


	public void setDialTaskItems(List<DialTaskItem> dialTaskItems) {
		this.dialTaskItems = dialTaskItems;
	}

	public List<DialTaskItem> getDialTaskItems() {
		return dialTaskItems;
	}

	public void setDialTaskItemCount(int dialTaskItemCount) {
		this.dialTaskItemCount = dialTaskItemCount;
	}

	public int getDialTaskItemCount() {
		return dialTaskItemCount;
	}

	
}

