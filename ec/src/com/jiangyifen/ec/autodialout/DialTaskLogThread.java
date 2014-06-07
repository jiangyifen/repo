package com.jiangyifen.ec.autodialout;

import java.util.Date;
import java.util.List;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.biz.DialTaskItemManager;
import com.jiangyifen.ec.biz.DialTaskLogManager;
import com.jiangyifen.ec.biz.DialTaskManager;
import com.jiangyifen.ec.biz.MyCustomerLogManager;
import com.jiangyifen.ec.dao.DialTask;
import com.jiangyifen.ec.dao.DialTaskItem;
import com.jiangyifen.ec.dao.DialTaskLog;

public class DialTaskLogThread extends Thread {

	private final Log logger = LogFactory.getLog(getClass());

	protected static DialTaskManager dialTaskManager;
	protected static DialTaskItemManager dialTaskItemManager;
	protected static DialTaskLogManager dialTaskLogManager;
	protected static MyCustomerLogManager myCustomerLogManager;

	public DialTaskLogThread() {
		this.setDaemon(true);
		this.setName("DialTaskLogThread");
		this.start();
	}

	public void setDialTaskManager(DialTaskManager dialTaskManager) {
		DialTaskLogThread.dialTaskManager = dialTaskManager;
	}

	public void setDialTaskItemManager(DialTaskItemManager dialTaskItemManager) {
		DialTaskLogThread.dialTaskItemManager = dialTaskItemManager;
	}

	public void setDialTaskLogManager(DialTaskLogManager dialTaskLogManager) {
		DialTaskLogThread.dialTaskLogManager = dialTaskLogManager;
	}

	public void setMyCustomerLogManager(
			MyCustomerLogManager myCustomerLogManager) {
		DialTaskLogThread.myCustomerLogManager = myCustomerLogManager;
	}

	private void saveDialTaskLog(DialTask dialTask){

		long dialTaskId = dialTask.getId();
		String dialTaskName = dialTask.getTaskName();
		long customerCount = myCustomerLogManager
				.getMyCustomerLogCount(dialTaskId);
		long dialTaskItemFinishedCount = dialTaskItemManager
				.getDialTaskItemCount(dialTask.getId(),
						DialTaskItem.STATUS_FINISH);
		long dialTaskItemTotalCount = dialTaskItemManager
				.getDialTaskItemCount(dialTask.getId());

		DialTaskLog dtl = dialTaskLogManager
				.getByDialTaskId(dialTaskId);

		if (dtl == null) {
			dtl = new DialTaskLog();
			dtl.setDialTaskId(dialTaskId);
			dtl.setDialTaskName(dialTaskName);
		}
		dtl.setCustomerCount(customerCount);
		dtl.setDialTaskItemFinishedCount(dialTaskItemFinishedCount);
		dtl.setDialTaskItemTotalCount(dialTaskItemTotalCount);
		dtl.setDate(new Date());

		dialTaskLogManager.save(dtl);
		logger.info("SAVE DialTaskLog: " + dtl.getDialTaskId());
	}
	public void run() {
		while (true) {
			try {

				if (dialTaskManager == null || dialTaskLogManager == null) {
					Thread.sleep(500);
					continue;
				}
				
//				List<DialTask> allRunningDialTasks = dialTaskManager.findByStatus(DialTask.STATUS_RUNNING);
				List<DialTask> allRunningDialTasks = dialTaskManager.findAll();
				for (DialTask dialTask : allRunningDialTasks) {
					saveDialTaskLog(dialTask);
				}

//				List<DialTask> allFinishedDialTasks = dialTaskManager.findByStatus(DialTask.STATUS_FINISH);
//				for(DialTask dialTask: allFinishedDialTasks){
//					dialTaskManager.delete(dialTask);
//				}

				Thread.sleep(10 * 60 * 1000);

			} catch (InterruptedException e) {
				logger.error("", e);
			}
		}
	}
}
