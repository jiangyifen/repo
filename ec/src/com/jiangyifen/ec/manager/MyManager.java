package com.jiangyifen.ec.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerConnectionState;
import org.asteriskjava.manager.SendActionCallback;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.action.EventGeneratingAction;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.action.QueueStatusAction;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.QueueEntryEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.asteriskjava.manager.event.QueueParamsEvent;
import org.asteriskjava.manager.event.StatusEvent;
import org.asteriskjava.manager.response.ManagerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.biz.CdrManager;
import com.jiangyifen.ec.biz.HoldEventLogManager;
import com.jiangyifen.ec.biz.QueueCallerAbandonEventLogManager;
import com.jiangyifen.ec.biz.QueueEntryEventLogManager;
import com.jiangyifen.ec.biz.QueueMemberPauseLogManager;
import com.jiangyifen.ec.biz.SipManager;
import com.jiangyifen.ec.biz.UserManager;
import com.jiangyifen.ec.dao.QueueEntryEventLog;
import com.jiangyifen.ec.util.Config;

public class MyManager extends Thread {

	private static final Logger logger = LoggerFactory
			.getLogger(MyManager.class);

	private static String ip;
	private static String username;
	private static String password;

	private static ManagerConnectionFactory factory;
	private static ManagerConnection managerConnection;

	public static UserManager userManager;
	public static SipManager sipManager;
	public static CdrManager cdrManager;
	protected static QueueEntryEventLogManager queueEntryEventLogManager;
	protected static QueueCallerAbandonEventLogManager queueCallerAbandonEventLogManager;
	protected static QueueMemberPauseLogManager queueMemberPauseLogManager;

	protected static HoldEventLogManager holdEventLogManager;

	// 保存系统状态的一些容器
	private static ConcurrentHashMap<String, String> activeChannels = new ConcurrentHashMap<String, String>();

	public static boolean statusEventsIsReady = true;
	private static ArrayList<StatusEvent> statusEvents = new ArrayList<StatusEvent>();

	private static ConcurrentHashMap<String, QueueParamsEvent> queueParamsEvents = new ConcurrentHashMap<String, QueueParamsEvent>();
	private static ConcurrentHashMap<String, ArrayList<QueueMemberEvent>> queueMemberEvents = new ConcurrentHashMap<String, ArrayList<QueueMemberEvent>>();
	private static ConcurrentHashMap<String, ArrayList<QueueEntryEvent>> queueEntryEvents = new ConcurrentHashMap<String, ArrayList<QueueEntryEvent>>();

	public MyManager() throws IOException {
		ip = Config.props.getProperty("manager_ip");
		username = Config.props.getProperty("manager_username");
		password = Config.props.getProperty("manager_password");

		factory = new ManagerConnectionFactory(ip, username, password);

		this.setDaemon(true);
		this.start();
	}

	public void setUserManager(UserManager userManager) {
		MyManager.userManager = userManager;
	}

	public void setCdrManager(CdrManager cdrManager) {
		MyManager.cdrManager = cdrManager;
	}

	public void setSipManager(SipManager sipManager) {
		MyManager.sipManager = sipManager;
	}

	public void setQueueEntryEventLogManager(
			QueueEntryEventLogManager queueEntryEventLogManager) {
		MyManager.queueEntryEventLogManager = queueEntryEventLogManager;
	}

	public void setQueueCallerAbandonEventLogManager(
			QueueCallerAbandonEventLogManager queueCallerAbandonEventLogManager) {
		MyManager.queueCallerAbandonEventLogManager = queueCallerAbandonEventLogManager;
	}

	public void setQueueMemberPauseLogManager(
			QueueMemberPauseLogManager queueMemberPauseLogManager) {
		MyManager.queueMemberPauseLogManager = queueMemberPauseLogManager;
	}

	public void setHoldEventLogManager(HoldEventLogManager holdEventLogManager) {
		MyManager.holdEventLogManager = holdEventLogManager;
	}

	public static void sendEventGeneratingAction(EventGeneratingAction action) {
		sendEventGeneratingAction(action, 2000);
	}

	public static void sendEventGeneratingAction(EventGeneratingAction action,
			long timeout) {
		try {
			managerConnection.sendEventGeneratingAction(action, timeout);
		} catch (Exception e) {
			logger.error("managerConnection.getState()="
					+ managerConnection.getState());
			initManagerConnection();
			logger.error(e.getMessage(), e);
		}
	}

	public static ManagerResponse sendAction(ManagerAction action) {
		return sendAction(action, 2000);
	}

	public static ManagerResponse sendAction(ManagerAction action, long timeout) {
		try {
			return managerConnection.sendAction(action, timeout);
		} catch (Exception e) {
			logger.error("managerConnection.getState()="
					+ managerConnection.getState());
			initManagerConnection();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static void sendActionCallback(ManagerAction action,
			SendActionCallback callback) {
		try {
			managerConnection.sendAction(action, callback);
		} catch (Exception e) {
			logger.error("managerConnection.getState()="
					+ managerConnection.getState());
			initManagerConnection();
			logger.error(e.getMessage(), e);
		}
	}

	public static ConcurrentHashMap<String, String> getActiveChannels() {
		return activeChannels;
	}

	public static String getActiveChannel(String key) {
		synchronized (activeChannels) {
			return activeChannels.get(key);
		}
	}

	public static String putActiveChannel(String key, String value) {
		synchronized (activeChannels) {
			return activeChannels.put(key, value);
		}
	}

	public static String removeActiveChannel(String key) {
		synchronized (activeChannels) {
			return activeChannels.remove(key);
		}
	}

	public static void addStatusEvent(StatusEvent e) {
		synchronized (statusEvents) {
			statusEvents.add(e);
		}
	}

	public static ArrayList<StatusEvent> getStatusEvents() {
		synchronized (statusEvents) {
			return statusEvents;
		}
	}

	//
	public static QueueParamsEvent getQueueParams(String queueName) {
		synchronized (queueParamsEvents) {
			return queueParamsEvents.get(queueName);
		}
	}

	public static QueueParamsEvent putQueueParams(String queueName,
			QueueParamsEvent value) {
		synchronized (queueParamsEvents) {
			return queueParamsEvents.put(queueName, value);
		}
	}

	public static QueueParamsEvent removeQueueParams(String queueName) {
		synchronized (queueParamsEvents) {
			return queueParamsEvents.remove(queueName);
		}
	}

	//
	public static ArrayList<QueueMemberEvent> getQueueMember(String queueName) {
		synchronized (queueMemberEvents) {
			return queueMemberEvents.get(queueName);
		}
	}

	public static boolean addQueueMember(String queueName,
			QueueMemberEvent value) {
		synchronized (queueMemberEvents) {
			if (queueMemberEvents.get(queueName) == null) {
				ArrayList<QueueMemberEvent> queueMemberEventList = new ArrayList<QueueMemberEvent>();
				queueMemberEventList.add(value);
				queueMemberEvents.put(queueName, queueMemberEventList);
			}

			return queueMemberEvents.get(queueName).add(value);
		}
	}

	public static ArrayList<QueueMemberEvent> removeQueueMember(String queueName) {
		synchronized (queueMemberEvents) {
			return queueMemberEvents.remove(queueName);
		}
	}

	//
	public static ArrayList<QueueEntryEvent> getQueueEntry(String queueName) {
		synchronized (queueEntryEvents) {
			return queueEntryEvents.get(queueName);
		}
	}

	public static boolean addQueueEntry(String queueName, QueueEntryEvent value) {
		synchronized (queueEntryEvents) {
			if (queueEntryEvents.get(queueName) == null) {
				ArrayList<QueueEntryEvent> queueEventryEventList = new ArrayList<QueueEntryEvent>();
				queueEntryEvents.put(queueName, queueEventryEventList);
				return queueEventryEventList.add(value);
			} else {
				return queueEntryEvents.get(queueName).add(value);
			}
		}
	}

	public static ArrayList<QueueEntryEvent> removeQueueEntry(String queueName) {
		synchronized (queueEntryEvents) {
			return queueEntryEvents.remove(queueName);
		}
	}

	private void saveFinishedQueueEntryEvents(
			HashMap<String, QueueEntryEvent> lastTimeQueueEntryEvents,
			HashMap<String, QueueEntryEvent> thisTimeQueueEntryEvents) {

		for (String callid : lastTimeQueueEntryEvents.keySet()) {
			if (!thisTimeQueueEntryEvents.containsKey(callid)) {
				QueueEntryEvent e = lastTimeQueueEntryEvents.get(callid);
				// logger.info("SAVE " + e);
				QueueEntryEventLog qeel = new QueueEntryEventLog();
				qeel.setCallerid(e.getCallerId());
				qeel.setCalleridname(e.getCallerIdName());
				qeel.setChannel(e.getChannel());
				qeel.setDateReceived(e.getDateReceived());
				qeel.setPosition(e.getPosition());
				qeel.setQueue(e.getQueue());
				qeel.setWait(e.getWait());
				queueEntryEventLogManager.save(qeel);
			}
		}

	}

	private static void initManagerConnection() {
		logger.info("init manager connection!");

		try {
			if (managerConnection != null) {
				if (managerConnection.getState() == ManagerConnectionState.CONNECTED
						|| managerConnection.getState() == ManagerConnectionState.RECONNECTING) {
					managerConnection.logoff();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		try {
			managerConnection = factory.createManagerConnection();
			managerConnection.addEventListener(new MyEventListener());
			managerConnection.addEventListener(new HoldListener());
			managerConnection.login();
			Thread.sleep(1000);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public void run() {
		while (true) {

			try {

				initManagerConnection();

				HashMap<String, QueueEntryEvent> lastTimeQueueEntryEvents = new HashMap<String, QueueEntryEvent>();
				HashMap<String, QueueEntryEvent> thisTimeQueueEntryEvents = new HashMap<String, QueueEntryEvent>();

				while (true) {

					try {
						Thread.sleep(1000);

						for (ArrayList<QueueEntryEvent> al : queueEntryEvents
								.values()) {
							for (QueueEntryEvent qee : al) {
								thisTimeQueueEntryEvents.put(qee.getCallerId(),
										qee);
							}
						}

						saveFinishedQueueEntryEvents(lastTimeQueueEntryEvents,
								thisTimeQueueEntryEvents);

						lastTimeQueueEntryEvents = (HashMap<String, QueueEntryEvent>) thisTimeQueueEntryEvents
								.clone();
						thisTimeQueueEntryEvents.clear();

						// 这条命令不是多余的。asterisk启动后，直接用queueStatusAction无法获取queue状态，必须queue
						// show之后，asterisk才会将queue状态加载，才能通过queueStatusAction获取。
						// 命令需要在queue成员变化后执行，使得queueMemberEvents可以获取最新的成员
						sendActionCallback(new CommandAction("queue show"),
								null);

						queueParamsEvents.clear();
						queueMemberEvents.clear();
						queueEntryEvents.clear();
						sendActionCallback(new QueueStatusAction(), null);

						// 在这里置为false，在收到 StatusCompleteEvent时再设置成true
						statusEventsIsReady = false;
						statusEvents.clear();
						sendActionCallback(new StatusAction(), null);

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
