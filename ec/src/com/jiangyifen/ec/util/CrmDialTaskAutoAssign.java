package com.jiangyifen.ec.util;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.biz.DialTaskItemManager;
import com.jiangyifen.ec.biz.DialTaskManager;
import com.jiangyifen.ec.biz.SmTaskManager;
import com.jiangyifen.ec.dao.DialTask;
import com.jiangyifen.ec.dao.DialTaskItem;
import com.jiangyifen.ec.dao.SmTask;

public class CrmDialTaskAutoAssign extends Thread {

	private static final int period = 3600000;

	private final Log logger = LogFactory.getLog(getClass());

	protected DialTaskManager dialTaskManager;
	protected DialTaskItemManager dialTaskItemManager;
	protected SmTaskManager smTaskManager;

	public CrmDialTaskAutoAssign() {
		this.setDaemon(true);
		this.setName("CrmDialTaskAutoAssign");
		this.start();
	}

	public void run() {
		logger.info("CrmDialTaskAutoAssign: start");
		try {
			logger.info("CrmDialTaskAutoAssign: 1");
			while (true) {
				logger.info("CrmDialTaskAutoAssign: 2");
				Date current = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("HH");
				String h = sdf.format(current);
				Integer hour = Integer.valueOf(h);

				//if中的条件应该确保if每天只执行一次，执行多次的话就会影响dialtask“当日分配”的标记
				if (hour >= 22 && hour <= 23) {
					logger.info("CrmDialTaskAutoAssign: 3");
					//查看收到的短信，将这些手机号码对应的dialtaskitem做上标记
					if(smTaskManager==null){
						sleep(500);
					}
					List<SmTask> smTasks = smTaskManager.getSmTasksByStatusAndUserfield(100, 1, SmTask.RECEIVE,"");
					//在while外先取一次，接下来在while里面再取，取完为止
					logger.info("CrmDialTaskAutoAssign: 4");
					while(smTasks.size()>0){
						logger.info("CrmDialTaskAutoAssign: 5");
						for(SmTask smTask:smTasks){
							logger.info("CrmDialTaskAutoAssign: 61");
							String phoneNumber = smTask.getMobile();
							
							List<DialTaskItem> dialTaskItemsManual =dialTaskItemManager.findByPhoneNumberAndStatus(100, 1, phoneNumber, DialTaskItem.STATUS_MANUAL);
							while(dialTaskItemsManual.size()>0){
								for(DialTaskItem dialTaskItem:dialTaskItemsManual){
									dialTaskItem.setHasSmReply(true);
									dialTaskItemManager.update(dialTaskItem);
								}
								dialTaskItemsManual =dialTaskItemManager.findByPhoneNumberAndStatus(100, 1, phoneNumber, DialTaskItem.STATUS_MANUAL);
							}
							
							List<DialTaskItem> dialTaskItemsReady = dialTaskItemManager.findByPhoneNumberAndStatus(100, 1, phoneNumber, DialTaskItem.STATUS_READY);
							while(dialTaskItemsReady.size()>0){
								for(DialTaskItem dialTaskItem:dialTaskItemsReady){
									dialTaskItem.setHasSmReply(true);
									dialTaskItemManager.update(dialTaskItem);
								}
								dialTaskItemsReady = dialTaskItemManager.findByPhoneNumberAndStatus(100, 1, phoneNumber, DialTaskItem.STATUS_READY);
							}
							
							//将该条短信标记为已处理
							smTask.setUserfield("assigned");
							smTaskManager.updateSmTask(smTask);
							logger.info("CrmDialTaskAutoAssign: 62");
						}
						logger.info("CrmDialTaskAutoAssign: 7");
						smTasks = smTaskManager.getSmTasksByStatusAndUserfield(100, 1, SmTask.RECEIVE,"");
					}

					//先将所有dialtask标记为当日未分配
					List<DialTask> list1 = dialTaskManager.findAll();
					for (DialTask dialTask : list1) {
						dialTask.setHasAssignedToday(false);
						dialTaskManager.update(dialTask);
					}
					
					//分派dial task item给每个人
					logger.info("CrmDialTaskAutoAssign: 8");
					List<DialTask> list2 = dialTaskManager.findByAutoAssign(true);
					logger.info("CrmDialTaskAutoAssign: 9");
					for (DialTask dialTask : list2) {
						logger.info("CrmDialTaskAutoAssign: 91");
						long id = dialTask.getId();
						URL u = new URL("http://localhost:8080/ec/crmDialTaskAssign?dialTaskId="+id);
						logger.info(u);
						u.openStream();
						logger.info("CrmDialTaskAutoAssign: 92");
					}
				}
				logger.info("CrmDialTaskAutoAssign: 10");
				Thread.sleep(period);

			}

		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		logger.info("CrmDialTaskAutoAssign: end");
	}
	

	public DialTaskManager getDialTaskManager() {
		return dialTaskManager;
	}

	public void setDialTaskManager(DialTaskManager dialTaskManager) {
		this.dialTaskManager = dialTaskManager;
	}

	
	public DialTaskItemManager getDialTaskItemManager() {
		return dialTaskItemManager;
	}

	public void setDialTaskItemManager(DialTaskItemManager dialTaskItemManager) {
		this.dialTaskItemManager = dialTaskItemManager;
	}

	public SmTaskManager getSmTaskManager() {
		return smTaskManager;
	}

	public void setSmTaskManager(SmTaskManager smTaskManager) {
		this.smTaskManager = smTaskManager;
	}
	
}
