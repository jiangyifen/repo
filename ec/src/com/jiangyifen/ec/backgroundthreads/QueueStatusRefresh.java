package com.jiangyifen.ec.backgroundthreads;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.asteriskjava.manager.event.QueueEntryEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.asteriskjava.manager.event.QueueParamsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.beans.QueueStatus;
import com.jiangyifen.ec.beans.SipStatus;
import com.jiangyifen.ec.dao.Queue;
import com.jiangyifen.ec.dao.User;
import com.jiangyifen.ec.manager.MyManager;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.MyStringUtils;
import com.jiangyifen.ec.util.ShareData;

public class QueueStatusRefresh extends Thread {

	private static final int period = 5000; // 5 seconds

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public QueueStatusRefresh() {

		this.setDaemon(true);
		this.setName("QueueStatusRefresh");
		this.start();
	}

	@SuppressWarnings("unchecked")
	private void refresh() {
		try {
			if (ShareData.allQueues == null) {
				logger.info("ShareData.allQueues==null");
				return;
			}

			// test
//			long start = System.currentTimeMillis();
			// test
			for (Queue queue : ShareData.allQueues) {

				ShareData.queueStatusIsReady = false;

				try {
					// queueName
					QueueStatus queueStatus = new QueueStatus();
					String queueName = queue.getName();
					String description = queue.getDescription();
					queueStatus.setQueueName(queueName);
					queueStatus.setDescription(description);

					// queueParamsEvent
					QueueParamsEvent queueParamsEvent = MyManager
							.getQueueParams(queueName);
					queueStatus.setQueueParamsEvent(queueParamsEvent);

					// queueMemberList
					ArrayList<QueueMemberEvent> queueMemberEventsList = null;

					if (queueMemberEventsList == null) {
						ArrayList<QueueMemberEvent> tmp = null;
						for (int retry = 0; retry <= 10; retry++) {
							tmp = MyManager.getQueueMember(queueName);

							if (tmp != null) {

								queueMemberEventsList = (ArrayList<QueueMemberEvent>) tmp
										.clone();
							}

							if (queueMemberEventsList != null) {
								break;
							}

							Thread.sleep(100);
						}
					}
					// if (queueMemberEventsList == null) {
					// return;
					// }

					HashMap<String, QueueMemberEvent> queueMemberEventsMap = new HashMap<String, QueueMemberEvent>();
					if (queueMemberEventsList != null) {
						for (QueueMemberEvent queueMemberEvent : queueMemberEventsList) {

							String temp = queueMemberEvent.getLocation();
							if (temp == null) {
								continue;
							}
							String[] fullSipName = temp.split("/");
							String sipName = fullSipName[1];

							SipStatus sipStatus = ShareData.sipStatusMap
									.get(sipName);
							if (sipStatus != null) {

								// 利用QueueMemberEvent固有的internalActionId来保存linkedCallerId(通话的电话号码)
								String linkedCallerId = sipStatus
										.getLinkedCallerId();
								if (linkedCallerId != null) {
									queueMemberEvent
											.setInternalActionId(linkedCallerId);
								}

								// 利用QueueMemberEvent固有的membership来保存seconds(通话时长)
								Integer seconds = sipStatus.getSeconds();
								if (seconds != null) {
									String sec = MyStringUtils
											.formatIntToHHmmss(seconds);
									queueMemberEvent.setMembership(sec);
								}
							}

							// 利用QueueMemberEvent固有的name和memberName字段来保存座席人员的姓名和工号
							String username = ShareData.sipAndLoginusername
									.get(sipName);
							if (username == null) {
								username = "0";
							}
							String name = ShareData.usernameAndName
									.get(username);
							if (name == null) {
								name = "";
							}

							String hid = "";
							User user = ShareData.usernameAndUser.get(username);
							if (user != null) {
								hid = user.getHid();
							}
							if (hid == null) {
								hid = "";
							}
							queueMemberEvent.setMemberName(hid);// 工号
							queueMemberEvent.setName(name);// 姓名

							// 将主动播出电话的SIP成员状态改为忙
							if (Config.props.getProperty(
									"set_queue_member_busy_by_sip_status")
									.equals("true")) {

								ArrayList<String> activeSips = (ArrayList<String>) ShareData.activeSips
										.clone();
								if (activeSips.contains(sipName)) {
									queueMemberEvent.setStatus(3);
								}
							}

							queueMemberEventsMap.put(
									queueMemberEvent.getLocation(),
									queueMemberEvent);

						}
					}

					Object[] keys = queueMemberEventsMap.keySet().toArray();
					Arrays.sort(keys);

					ArrayList<QueueMemberEvent> queueMemberList = new ArrayList<QueueMemberEvent>();
					int free = 0;
					int ringing = 0;
					int busy = 0;
					int unavailable = 0;

					int queueMemberCount = 0;
					int pausedMemberCount = 0;
					int unpausedMemberCount = 0;

					int loginMemberCount = 0;
					int loginPausedMemberCount = 0;
					int loginUnpausedMemberCount = 0;

					queueMemberList.clear();
					for (Object key : keys) {
						QueueMemberEvent event = queueMemberEventsMap.get(key);
						queueMemberList.add(event);

						// 队列总成员数
						queueMemberCount++;

						// 统计各member的状态
						int status = event.getStatus();
						if (status == 1) {
							free++;
						} else if (status == 6) {
							ringing++;
						} else if (status == 2 || status == 3) {
							busy++;
						} else {
							unavailable++;
						}

						// 置忙置闲成员数
						if (event.getPaused()) {
							pausedMemberCount++;
						} else {
							unpausedMemberCount++;
						}

						// 登录成员数
						// 将登陆状态和pause状态混合成一个结果，用action id字段保存

						if (event.getMemberName() != null
								&& event.getMemberName().length() > 0) {
							loginMemberCount++;
							if (event.getPaused()) {
								loginPausedMemberCount++;
								event.setActionId("login_paused");
							} else {
								loginUnpausedMemberCount++;
								event.setActionId("login_unpaused");
							}
						} else {
							if (event.getPaused()) {
								event.setActionId("logout_paused");
							} else {
								event.setActionId("logout_unpaused");
							}
						}

					}

					if (queueMemberList.size() != 0) {
						queueStatus.setQueueMemberList(queueMemberList);
					}
					queueStatus.setFree(free);
					queueStatus.setRinging(ringing);
					queueStatus.setBusy(busy);
					queueStatus.setUnavailable(unavailable);
					queueStatus.setQueueMemberCount(queueMemberCount);
					queueStatus.setPausedMemberCount(pausedMemberCount);
					queueStatus.setUnpausedMemberCount(unpausedMemberCount);
					queueStatus.setLoginMemberCount(loginMemberCount);
					queueStatus
							.setLoginPausedMemberCount(loginPausedMemberCount);
					queueStatus
							.setLoginUnpausedMemberCount(loginUnpausedMemberCount);

					// queueEntryList
					ArrayList<QueueEntryEvent> queueEntryList = new ArrayList<QueueEntryEvent>();
					ArrayList<QueueEntryEvent> b = null;
					ArrayList<QueueEntryEvent> t = MyManager
							.getQueueEntry(queueName);
					if (t != null) {
						b = (ArrayList<QueueEntryEvent>) t.clone();
					}
					if (b != null) {
						queueEntryList = (ArrayList<QueueEntryEvent>) b.clone();
					}
					queueStatus.setQueueEntryList(queueEntryList);

					// put into ShareData
					ShareData.queueStatus.put(queueName, queueStatus);

				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}

			// test
//			long end = System.currentTimeMillis();

//			logger.info("refresh queueStatus, time= " + (end - start) + " ms");
			// test

			ShareData.queueStatusIsReady = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void run() {

		while (true) {
			try {
				refresh();

				Thread.sleep(period);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}

	}
}
