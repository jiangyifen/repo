package com.jiangyifen.ec.autodialout;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerConnectionState;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.asteriskjava.manager.event.QueueMemberStatusEvent;
import org.asteriskjava.manager.response.ManagerResponse;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.biz.DialTaskItemManager;
import com.jiangyifen.ec.biz.DialTaskManager;
import com.jiangyifen.ec.dao.DialTask;
import com.jiangyifen.ec.dao.DialTaskItem;
import com.jiangyifen.ec.manager.MyManager;
import com.jiangyifen.ec.util.Config;

public class DialTaskWorkThreadManager extends Thread {

	private static final Log logger = LogFactory
			.getLog(DialTaskWorkThreadManager.class);

	protected DialTaskItemManager dialTaskItemManager;
	protected DialTaskManager dialTaskManager;

	private static ConcurrentHashMap<String, Integer> dialingOutChannelCount = null;
	private static ConcurrentHashMap<String, Integer> tempDialingOutChannelCount = null;

	private static String ip;
	private static String username;
	private static String password;

	private static ManagerConnectionFactory factory;
	private static ManagerConnection managerConnection;

	public DialTaskWorkThreadManager() {

		ip = Config.props.getProperty("manager_ip");
		username = Config.props.getProperty("manager_username");
		password = Config.props.getProperty("manager_password");

		factory = new ManagerConnectionFactory(ip, username, password);

		dialingOutChannelCount = new ConcurrentHashMap<String, Integer>();
		tempDialingOutChannelCount = new ConcurrentHashMap<String, Integer>();

		this.setDaemon(true);
		this.setName("DialTaskManager");
		this.start();
	}

	public void setDialTaskItemManager(DialTaskItemManager dialTaskItemManager) {
		this.dialTaskItemManager = dialTaskItemManager;
	}

	public void setDialTaskManager(DialTaskManager dialTaskManager) {
		this.dialTaskManager = dialTaskManager;
	}

	private static void initManagerConnection() {
		if (managerConnection != null) {
			if (managerConnection.getState() == ManagerConnectionState.CONNECTED
					|| managerConnection.getState() == ManagerConnectionState.RECONNECTING) {
				managerConnection.logoff();
			}
		}
		managerConnection = factory.createManagerConnection();
		managerConnection.addEventListener(new AutoDialEventListener());
		try {
			managerConnection.login();
			Thread.sleep(1000);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	private int getTaskQuantity(DialTask dialTask) {

		long dialingOutTaskNumber = Math.round(((double) dialingOutChannelCount
				.get(dialTask.getQueueName())) / 2);
		logger.info("DialTaskWorkThread: queue " + dialTask.getQueueName() + " has [" + dialingOutTaskNumber + "] dialingOutTasks.");

		int freeQueueMember = 0;
		ArrayList<QueueMemberEvent> temp = MyManager.getQueueMember(dialTask
				.getQueueName());
		ArrayList<QueueMemberEvent> queueMemberEvents = null;
		if (temp != null) {
			queueMemberEvents = (ArrayList<QueueMemberEvent>) temp.clone();
		}

		if (queueMemberEvents != null) {
			// 去掉重复的queueMemberEvent
			HashMap<String, QueueMemberEvent> tmp = new HashMap<String, QueueMemberEvent>();
			for (QueueMemberEvent e : queueMemberEvents) {
				tmp.put(e.getName(), e);
			}
			for (QueueMemberEvent e : tmp.values()) {
				if (e instanceof QueueMemberStatusEvent) {
					continue;
				}
				if (!e.getPaused() && e.getStatus() == 1) {
					freeQueueMember++;
				}
			}
		} else {
			logger.warn("DialTaskWorkThread: queue " + dialTask.getQueueName()
					+ " queueMemberEvents is null");
		}
		logger.info("DialTaskWorkThread: queue " + dialTask.getQueueName() + " has [" + freeQueueMember + "] free members.");

		if (freeQueueMember == 0) {
			return 0;
		}

		int shouldAddTask = (int) ((freeQueueMember * dialTask.getRate() - dialingOutTaskNumber) + 0.5);// +1

		if (shouldAddTask <= 0) {
			shouldAddTask = 0;
		} 
		logger.info("DialTaskWorkThread: queue " + dialTask.getQueueName() + " should add [" + shouldAddTask	+ "] dialTasks.");
		
		return shouldAddTask;
	}

	private void runDialTaskItem(DialTask dialTask, DialTaskItem dti) {

		// 呼叫
		OriginateAction originateAction = null;
		ManagerResponse originateResponse = null;

		originateAction = new OriginateAction();
		originateAction.setChannel("Local/999" + dialTask.getQueueName()
				+ "@channelpool");
		originateAction.setContext("outgoing");
		originateAction.setExten(dti.getPhoneNumber());
		originateAction.setPriority(new Integer(1));

		try {
			originateResponse = managerConnection.sendAction(originateAction,
					5000);
		} catch (Exception e) {
			logger.error("DialTaskWorkThread: managerConnection.getState()="
					+ managerConnection.getState());
			initManagerConnection();
			logger.error(e.getMessage(), e);
		}

		// 根据结果update任务的状态
		dti.setCalldate(new Date());
		if (originateResponse != null
				&& originateResponse.getResponse().equals("Success")) {
			dti.setStatus(DialTaskItem.STATUS_FINISH);
			// logger.info("auto dial task to " + dti.getPhoneNumber() +
			// " finished");
		} else {
			dti.setStatus(DialTaskItem.STATUS_FAILED);
			logger.warn("DialTaskWorkThread: dial task to " + dti.getPhoneNumber() + " FAILED");
		}

		dialTaskItemManager.update(dti);

		// 计数
		dialingOutTaskCountIncrease(dialTask.getQueueName(), 2);

	}

	// 此函数在runDialTaskItem中调用
	public static void dialingOutTaskCountIncrease(String queueName, int amount) {
		Integer currentCount = dialingOutChannelCount.get(queueName);
		if (currentCount != null) {
			int count = currentCount + amount;
			// logger.info("dialing out Local/999 channel number in queue " +
			// queueName + " is [" + count + "]");
			dialingOutChannelCount.put(queueName, count);
		}
	}

	// 此函数在manager中挂机event中调用
	public static void dialingOutTaskCountDecrease(String queueName, int amount) {
		Integer currentCount = dialingOutChannelCount.get(queueName);
		if (currentCount != null) {
			int count = currentCount - amount;
			if (count < 0) {
				count = 0;
			}
			// logger.info("dialing out Local/999 channel number in queue " +
			// queueName + " is [" + count + "]");
			dialingOutChannelCount.put(queueName, count);
		}
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(5000);
				initManagerConnection();

				while (true) {
					try {
						// 这里sleep 5秒是因为ShareDate里也是sleep 5秒
//						Thread.sleep(5000);
						Thread.sleep(15000);

						// 如果数据库对象还没有被spring初始化，就等一下再试，直到spring将他们都注入完成
						if (dialTaskManager == null
								|| dialTaskItemManager == null) {
							logger.warn("DialTaskWorkThread: dialTaskItemManager is null");
							continue;
						}
						// 获取所有状态为RUNNING的dialTask
						List<DialTask> allRunningDialTasks = dialTaskManager
								.findByStatus(DialTask.STATUS_RUNNING);

						if (allRunningDialTasks == null) {
							logger.warn("DialTaskWorkThread: allRunningDialTasks is null");
							continue;
						}

						// 统计每个运行中的dialTask有多少个正在呼出的电话
						for (DialTask dialTask : allRunningDialTasks) {

							Integer count = dialingOutChannelCount.get(dialTask
									.getQueueName());
							if (count == null) {
								count = 0;
							}
							tempDialingOutChannelCount.put(
									dialTask.getQueueName(), count);

							logger.info("DialTaskWorkThreadManager: "
									+ dialTask.getTaskName() + " " + count);
						}

						dialingOutChannelCount.clear();
						dialingOutChannelCount = tempDialingOutChannelCount;
						tempDialingOutChannelCount = new ConcurrentHashMap<String, Integer>();

						for (DialTask dialTask : allRunningDialTasks) {
							logger.info("DialTaskWorkThread: queue " + dialTask.getQueueName() + " on dialTask " + dialTask.getTaskName());
							int taskQuantity = getTaskQuantity(dialTask);
							

							if (taskQuantity > 0) {
								List<DialTaskItem> dtiList = dialTaskItemManager
										.findByTaskIdAndStatus(taskQuantity, 1,
												dialTask.getId(),
												DialTaskItem.STATUS_READY);

								if (dtiList != null && dtiList.size() > 0) {
									Thread.sleep(100);
									for (DialTaskItem d : dtiList) {
										runDialTaskItem(dialTask, d);// null point exception??
										Thread.sleep(100);
									}
								} else {
									// 如果这段放在这里，那么在没有任务且没有队列成员时，已完成的任务不会切换至完成状态。因为判断了if(taskQuantity>0)
									logger.info("DialTaskWorkThread: Dial Task " + dialTask.getTaskName() + " finished");
									dialTask.setEndDate(new Date());
									dialTask.setStatus(DialTask.STATUS_FINISH);
									dialTaskManager.update(dialTask);
								}
							}

							Thread.sleep(100);
						}

					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			
		}
		
	}

}
