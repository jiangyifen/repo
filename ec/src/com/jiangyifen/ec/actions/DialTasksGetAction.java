package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.List;

import com.jiangyifen.ec.dao.DialTask;
import com.jiangyifen.ec.dao.DialTaskItem;
import com.jiangyifen.ec.dao.Queue;

public class DialTasksGetAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4421866192055179720L;

	private List<DialTask> dialTasks;
	private int pageIndex = 1;
	private final int PAGESIZE = 20;
	private int maxPageIndex = 0;

	private List<Integer> pages = new ArrayList<Integer>();

	private List<Queue> queues = new ArrayList<Queue>();

	public String execute() throws Exception {

		logger.info("DialTasksGetAction:"+"queueManager.getQueues()");
		setQueues(queueManager.getQueues());

		if ((dialTaskManager.getDialTaskCount() % PAGESIZE) == 0) {
			maxPageIndex = (dialTaskManager.getDialTaskCount() / PAGESIZE);
		} else {
			maxPageIndex = (dialTaskManager.getDialTaskCount() / PAGESIZE) + 1;
		}

		for (int i = 0; i < maxPageIndex; i++) {
			pages.add(i + 1);
		}
		if (pageIndex > maxPageIndex)
			pageIndex = maxPageIndex;
		if (pageIndex < 1)
			pageIndex = 1;

		logger.info("DialTasksGetAction:"+"dialTaskManager.findByPage(PAGESIZE, pageIndex)");
		List<DialTask> list = dialTaskManager.findByPage(PAGESIZE, pageIndex);
		for (DialTask dt : list) {

			dt.setDialTaskItemCount(dialTaskItemManager.getDialTaskItemCount(dt.getId()));

			dt.setFinishedDialTaskItemCount(dialTaskItemManager.getDialTaskItemCount(dt.getId(), DialTaskItem.STATUS_FINISH));
			
			dt.setReadyDialTaskItemCount(dialTaskItemManager.getDialTaskItemCount(dt.getId(), DialTaskItem.STATUS_READY));
			
			dt.setManualDialTaskItemCount(dialTaskItemManager.getDialTaskItemCount(dt.getId(), DialTaskItem.STATUS_MANUAL));

			long myCustomerCount = myCustomerLogManager.getMyCustomerLogCount(dt.getId());

			dt.setMyCustomerCount(myCustomerCount);

			if (dt.getFinishedDialTaskItemCount() != 0) {
				double d = dt.getMyCustomerCount() * 10000
						/ dt.getFinishedDialTaskItemCount();
				dt.setCustomerRate(d / 100);
			} else {
				dt.setCustomerRate(0);
			}
			
			Queue q = queueManager.getQueue(dt.getQueueName());
			if (q != null) {
				String queueDescription = q.getDescription();
				dt.setQueueDescription(queueDescription);

			}

		}
		logger.info("DialTasksGetAction:"+"setDialTasks(list)");
		setDialTasks(list);
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

	public void setDialTasks(List<DialTask> dialTasks) {
		this.dialTasks = dialTasks;
	}

	public List<DialTask> getDialTasks() {
		return dialTasks;
	}

}
