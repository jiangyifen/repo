package com.jiangyifen.ec.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.asteriskjava.manager.event.StatusEvent;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.beans.QueueStatus;
import com.jiangyifen.ec.beans.SipStatus;
import com.jiangyifen.ec.beans.Workload;
import com.jiangyifen.ec.biz.QueueManager;
import com.jiangyifen.ec.biz.SipManager;
import com.jiangyifen.ec.biz.UserManager;
import com.jiangyifen.ec.dao.Queue;
import com.jiangyifen.ec.dao.Sip;
import com.jiangyifen.ec.dao.User;
import com.jiangyifen.ec.manager.MyManager;

@SuppressWarnings("unchecked")
public class ShareData extends Thread {

	private final Log logger = LogFactory.getLog(getClass());

	protected static UserManager userManager;
	protected static SipManager sipManager;
	protected static QueueManager queueManager;

	public static List<Queue> allQueues;
	public static List<Sip> allSips;
	public static List<User> allUsers;
	public static Map<String, String> sipAndLoginusername = new ConcurrentHashMap<String, String>();
	public static List<String> allSipName = new ArrayList<String>();
	public static Map<String, String> usernameAndName = new HashMap<String, String>();

	public static HashMap<String, User> usernameAndUser = new HashMap<String, User>();
	public static HashMap<String, SipStatus> sipStatusMap = new HashMap<String, SipStatus>();
	public static ArrayList<SipStatus> sipStatusList = new ArrayList<SipStatus>();
	public static List<StatusEvent> statusEvents = new ArrayList<StatusEvent>();
	public static ArrayList<String> activeSips = new ArrayList<String>();
	
	public static Map<String, QueueStatus> queueStatus = new ConcurrentHashMap<String, QueueStatus>();
	public static boolean queueStatusIsReady = false;
	
	public static Map<String, Workload> usernameAndTodayWorkload = new HashMap<String, Workload>();
	public static Map<String, Workload> usernameAndAvgWorkload = new HashMap<String, Workload>();
	public static Map<String, Double> dpmtTopCount = new HashMap<String, Double>();
	public static Map<String, Double> dpmtTopBillsec = new HashMap<String, Double>();
	public static Map<String, Double> dpmtTopWorkload = new HashMap<String, Double>();
	public static Map<String, Double> dpmtTopCustomerCount = new HashMap<String, Double>();
	
	public static Map<String,String> extenAndOutline = new HashMap<String, String>();

	public static int sipTotal = allSipName.size();
	public static int sipNotLoginFree = 0;
	public static int sipFree = 0;
	public static int sipLongTimeFree = 0;
	public static int sipNotLoginBusy = 0;
	public static int sipBusy = 0;

	public ShareData() {
		this.setName("ShareDate");
		this.setDaemon(true);
		this.start();
	}

	public void run() {
		while (true) {

			try {

				while (true) {

					try {
						while (queueManager == null) {
							sleep(500);
						}
						allQueues = queueManager.getQueues();

						while (sipManager == null) {
							sleep(500);
						}
						allSips = sipManager.getSipsDesc();
						allSipName.clear();
						for (Sip sip : allSips) {
							allSipName.add(sip.getName());
							sipAndLoginusername.put(sip.getName(),
									sip.getLoginusername());
						}

						while (userManager == null) {
							sleep(500);
						}
						allUsers = userManager.getUsers();

						usernameAndName.clear();
						usernameAndUser.clear();
						for (User user : allUsers) {
							usernameAndName.put(user.getUsername(),
									user.getName());
							usernameAndUser.put(user.getUsername(), user);
						}

						// sip status

						while (MyManager.statusEventsIsReady) {
							ArrayList<StatusEvent> tempStatusEvent = (ArrayList<StatusEvent>) MyManager
									.getStatusEvents().clone();
							statusEvents = tempStatusEvent;
							Thread.sleep(50);
						}

						activeSips.clear();
						if (statusEvents.size() == 0) {
//							logger.warn("oh! statusEvents size = 0 !!!");
						}
						
						Map<String,String> tempSipNameAndLinkedCallerId = new HashMap<String,String>();
						Map<String,Integer> tempSipNameAndSeconds = new HashMap<String,Integer>();
						for (StatusEvent event : statusEvents) {
							
							//看channel（主叫）是否分机
							String channel = event.getChannel();
							if (channel == null) {
								continue;
							} else if (channel.contains("SIP")) {

								String sipName = channel.substring(
										channel.indexOf("/") + 1,
										channel.indexOf("-"));

								if (allSipName.contains(sipName)) {
									activeSips.add(sipName);
							////////////
									String linkedCallerId = event.getExtension();
									int seconds = event.getSeconds();
									tempSipNameAndLinkedCallerId.put(sipName, linkedCallerId);
									tempSipNameAndSeconds.put(sipName, seconds);
							////////////
								}
							}

							//看link（被叫）是否分机
							String link = event.getLink();
							if (link != null && link.contains("SIP")) {
								String sipName = link.substring(
										link.indexOf("/") + 1,
										link.indexOf("-"));

								if (allSipName.contains(sipName)) {
									activeSips.add(sipName);
							////////////
									String linkedCallerId = event.getCallerIdNum();
									int seconds = event.getSeconds();
									tempSipNameAndLinkedCallerId.put(sipName, linkedCallerId);
									tempSipNameAndSeconds.put(sipName, seconds);
							////////////

								}
							}

							//这段描述的情况，应该已经包含在前两段里了，可以去掉。
							String calleridnum = event.getCallerIdNum();
							if (allSipName.contains(calleridnum)) {
								activeSips.add(calleridnum);
							}
						}
						if (activeSips.size() == 0) {
//							logger.warn("oh! activeSips size = 0 !!!");
						}

						sipStatusList.clear();
						sipNotLoginBusy = 0;
						sipNotLoginFree = 0;
						sipBusy = 0;
						sipFree = 0;
						sipLongTimeFree = 0;
						for (String sipName : allSipName) {
							SipStatus sipStatus = sipStatusMap.get(sipName);
							if (sipStatus == null) {
								sipStatus = new SipStatus();
								sipStatus.setSipName(sipName);
							}

					////////////
							String linkedCallerId = tempSipNameAndLinkedCallerId.get(sipName);
							Integer seconds = tempSipNameAndSeconds.get(sipName);
							if(linkedCallerId!=null){
								sipStatus.setLinkedCallerId(linkedCallerId);
							}else{
								sipStatus.setLinkedCallerId("");
							}
							if(seconds!=null){
								sipStatus.setSeconds(seconds);
							}else{
								sipStatus.setSeconds(0);
							}
					////////////
							
							String username = sipAndLoginusername.get(sipName);

							if (username != null && !username.equals("0")) {
								sipStatus.setLogin(true);
								sipStatus.setLoginusername(username);
							} else {
								sipStatus.setLogin(false);
							}

							String name = "";
							String hid = "";
							if (username != null && !username.equals("0")) {
								name = usernameAndName.get(username);
								if (name == null) {
									name = "";
								}

								User u = usernameAndUser.get(username);
								if (u != null) {
									hid = u.getHid();
									if (hid == null) {
										hid = "";
									}
								}
							}
							sipStatus.setName(name);
							sipStatus.setHid(hid);

							if (activeSips.contains(sipName)) {
								// if sip is busy
								if (sipStatus.isLogin()) {
									sipStatus.setStatus(SipStatus.STATUS_BUSY);// busy
									sipBusy++;
								} else {
									sipStatus
											.setStatus(SipStatus.STATUS_NO_LOGIN_BUSY);
									sipNotLoginBusy++;
								}

							} else {
								// if sip is free
								if (sipStatus.isLogin()) {
									int lastStatus = sipStatus.getStatus();
									if (lastStatus == -1) {
										sipStatus
												.setStatus(SipStatus.STATUS_NOT_IN_USE);
										sipFree++;
									} else if (lastStatus == SipStatus.STATUS_BUSY) {
										// set free
										sipStatus
												.setStatus(SipStatus.STATUS_NOT_IN_USE);
										// set turn to free timestamp
										long t = System.currentTimeMillis();
										sipStatus.setTurnToFreeTime(t);
										sipFree++;
									} else if (lastStatus == SipStatus.STATUS_NOT_IN_USE) {
										long duration = System
												.currentTimeMillis()
												- sipStatus.getTurnToFreeTime();
										if (duration >= 300000) {
											sipStatus
													.setStatus(SipStatus.STATUS_LONG_TIME_NOT_IN_USE);
											sipLongTimeFree++;
										} else {
											sipStatus
													.setStatus(SipStatus.STATUS_NOT_IN_USE);
											sipFree++;
										}
									} else if (lastStatus == SipStatus.STATUS_LONG_TIME_NOT_IN_USE) {
										// do nothing
										sipLongTimeFree++;
									} else {
										// 针对之前是not login的情况
										sipStatus
												.setStatus(SipStatus.STATUS_NOT_IN_USE);
										sipStatus.setTurnToFreeTime(System
												.currentTimeMillis());
										sipFree++;
									}

								} else {

									sipStatus
											.setStatus(SipStatus.STATUS_NO_LOGIN_NOT_IN_USE);
									sipNotLoginFree++;

								}
							}

							sipStatusMap.put(sipName, sipStatus);
							sipStatusList.add(sipStatus);

						}

						Thread.sleep(5000);

					} catch (SQLException e) {
						logger.error(e.getMessage(), e);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}

				}

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {


			}

		}
	}

	public void setUserManager(UserManager userManager) {
		ShareData.userManager = userManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setSipManager(SipManager sipManager) {
		ShareData.sipManager = sipManager;
	}

	public SipManager getSipManager() {
		return sipManager;
	}

	public void setQueueManager(QueueManager queueManager) {
		ShareData.queueManager = queueManager;
	}

	public QueueManager getQueueManager() {
		return queueManager;
	}
}
