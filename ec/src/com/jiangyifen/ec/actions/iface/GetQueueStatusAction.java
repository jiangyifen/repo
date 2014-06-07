package com.jiangyifen.ec.actions.iface;

import java.util.ArrayList;
import java.util.List;

import org.asteriskjava.manager.event.QueueEntryEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.asteriskjava.manager.event.QueueParamsEvent;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.beans.QueueStatus;
import com.jiangyifen.ec.dao.Queue;
import com.jiangyifen.ec.util.ShareData;

public class GetQueueStatusAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7627586900215696781L;

	private List<Queue> queues;
	private String queueName;
	private boolean iface = false;
	private boolean paramsAndEntry = false;
	private boolean allQueueStatus = false;

	private List<QueueParamsEvent> queueParamsEventList = new ArrayList<QueueParamsEvent>();
	private QueueParamsEvent queueParamsEvent = null;
	private ArrayList<QueueMemberEvent> queueMemberList = new ArrayList<QueueMemberEvent>();
	private ArrayList<QueueEntryEvent> queueEntryList = new ArrayList<QueueEntryEvent>();

	private int free = 0;
	private int ringing = 0;
	private int busy = 0;
	private int unavailable = 0;

	private int queueMemberCount = 0;
	private int pausedMemberCount = 0;
	private int unpausedMemberCount = 0;

	private int loginMemberCount = 0;
	private int loginPausedMemberCount = 0;
	private int loginUnpausedMemberCount = 0;

	private int pagesize = 25;
	private String pageIndex;
	private int maxPageIndex = 0;
	private List<Integer> pages = new ArrayList<Integer>();

	private List<QueueStatus> queueStatusList = new ArrayList<QueueStatus>();

	public String execute() throws Exception {
		if (allQueueStatus) {
			for (String key : ShareData.queueStatus.keySet()) {
				queueStatusList.add(ShareData.queueStatus.get(key));
			}
			return "all";
		}
		setQueues(ShareData.allQueues);

		if (queueName == null || queueName.equals("")) {
			Thread.sleep(500);
			return INPUT;
		}

		for (int i = 0; i < 10; i++) {
			if (!ShareData.queueStatusIsReady) {
				Thread.sleep(50);
			} else {
				break;
			}
		}

		QueueStatus queueStatus = ShareData.queueStatus.get(queueName);
		this.setQueueParamsEvent(queueStatus.getQueueParamsEvent());
		this.setQueueMemberList(queueStatus.getQueueMemberList());
		this.setQueueEntryList(queueStatus.getQueueEntryList());
		this.setFree(queueStatus.getFree());
		this.setRinging(queueStatus.getRinging());
		this.setBusy(queueStatus.getBusy());
		this.setUnavailable(queueStatus.getUnavailable());
		this.setQueueMemberCount(queueStatus.getQueueMemberCount());
		this.setPausedMemberCount(queueStatus.getPausedMemberCount());
		this.setUnpausedMemberCount(queueStatus.getUnpausedMemberCount());
		this.setLoginMemberCount(queueStatus.getLoginMemberCount());
		this.setLoginPausedMemberCount(queueStatus.getLoginPausedMemberCount());
		this.setLoginUnpausedMemberCount(queueStatus.getUnpausedMemberCount());

		if (iface) {
			if (paramsAndEntry)
				return "paramsAndEntry";
			else
				return "iface";
		}

		return SUCCESS;
	}
//
//	@SuppressWarnings("unchecked")
//	public String execute1111() throws Exception {
//		// queues
//
//		setQueues(ShareData.allQueues);
//
//		if (queueName == null || queueName.equals("")) {
//			Thread.sleep(500);
//			return INPUT;
//		}
//
//		// queueParamsEvent
//		queueParamsEvent = MyManager.getQueueParams(queueName);
//
//		// queueMemberList
//		ArrayList<QueueMemberEvent> queueMemberEventsList = null;
//
//		if (queueMemberEventsList == null) {
//			ArrayList<QueueMemberEvent> tmp = null;
//			for (int retry = 0; retry <= 3; retry++) {
//				tmp = MyManager.getQueueMember(queueName);
//				if (tmp != null) {
//					queueMemberEventsList = (ArrayList<QueueMemberEvent>) tmp
//							.clone();
//				}
//
//				if (queueMemberEventsList != null) {
//					break;
//				}
//
//				Thread.sleep(100);
//			}
//		}
//		if (queueMemberEventsList == null) {
//			Thread.sleep(500);
//			if (iface) {
//				if (paramsAndEntry)
//					return "paramsAndEntry";
//				else
//					return "iface";
//			}
//			return SUCCESS;
//		}
//
//		HashMap<String, QueueMemberEvent> queueMemberEventsMap = new HashMap<String, QueueMemberEvent>();
//		for (QueueMemberEvent queueMemberEvent : queueMemberEventsList) {
//
//			// 利用QueueMemberEvent固有的name和memberName字段来保存座席人员的姓名和工号
//			String temp = queueMemberEvent.getLocation();
//			if (temp == null) {
//				logger.warn("GetQueueStatusAction: " + temp);
//				continue;
//			}
//			String[] fullSipName = temp.split("/");
//			String sipName = fullSipName[1];
//
//			String username = ShareData.sipAndLoginusername.get(sipName);
//			if (username == null) {
//				username = "0";
//			}
//			String name = ShareData.usernameAndName.get(username);
//			if (name == null) {
//				name = "";
//			}
//
//			String hid = "";
//			User user = ShareData.usernameAndUser.get(username);
//			if (user != null) {
//				hid = user.getHid();
//			}
//			if (hid == null) {
//				hid = "";
//			}
//			queueMemberEvent.setMemberName(hid);// 工号
//			queueMemberEvent.setName(name);// 姓名
//
//			// 将主动播出电话的SIP成员状态改为忙
//			if (Config.props.getProperty("set_queue_member_busy_by_sip_status")
//					.equals("true")) {
//
//				ArrayList<String> activeSips = (ArrayList<String>) ShareData.activeSips
//						.clone();
//				if (activeSips.contains(sipName)) {
//					queueMemberEvent.setStatus(3);
//				}
//			}
//
//			queueMemberEventsMap.put(queueMemberEvent.getLocation(),
//					queueMemberEvent);
//
//		}
//
//		Object[] keys = queueMemberEventsMap.keySet().toArray();
//		Arrays.sort(keys);
//
//		queueMemberList.clear();
//		for (Object key : keys) {
//			QueueMemberEvent e = queueMemberEventsMap.get(key);
//			queueMemberList.add(e);
//
//			// 队列总成员数
//			queueMemberCount++;
//
//			// 统计各member的状态
//			int status = e.getStatus();
//			if (status == 1) {
//				free++;
//			} else if (status == 6) {
//				ringing++;
//			} else if (status == 2 || status == 3) {
//				busy++;
//			} else {
//				unavailable++;
//			}
//
//			// 置忙置闲成员数
//			if (e.getPaused()) {
//				pausedMemberCount++;
//			} else {
//				unpausedMemberCount++;
//			}
//
//			// 登录成员数
//			// 将登陆状态和pause状态混合成一个结果，用action id字段保存
//
//			if (e.getMemberName() != null && e.getMemberName().length() > 0) {
//				loginMemberCount++;
//				if (e.getPaused()) {
//					loginPausedMemberCount++;
//					e.setActionId("login_paused");
//				} else {
//					loginUnpausedMemberCount++;
//					e.setActionId("login_unpaused");
//				}
//			} else {
//				if (e.getPaused()) {
//					e.setActionId("logout_paused");
//				} else {
//					e.setActionId("logout_unpaused");
//				}
//			}
//
//		}
//
//		// 分页显示
//		int pageIdx = 0;
//		try {
//			pageIdx = new Integer(pageIndex);
//		} catch (NumberFormatException e) {
//			pageIdx = 1;
//		}
//		if (pageIdx < 1)
//			pageIdx = 1;
//
//		if ((queueMemberCount % pagesize) == 0) {
//			maxPageIndex = (queueMemberCount / pagesize);
//		} else {
//			maxPageIndex = (queueMemberCount / pagesize) + 1;
//		}
//
//		for (int i = 0; i < maxPageIndex; i++) {
//			pages.add(i + 1);
//		}
//		if (pageIdx > maxPageIndex)
//			pageIdx = maxPageIndex;
//
//		int fromIndex = (pageIdx - 1) * pagesize;
//		int toIndex = pageIdx * pagesize;
//		if (toIndex > queueMemberCount) {
//			toIndex = queueMemberCount;
//		}
//
//		ArrayList<QueueMemberEvent> result = new ArrayList<QueueMemberEvent>();
//		for (int i = fromIndex; i < toIndex; i++) {
//			result.add(queueMemberList.get(i));
//		}
//		queueMemberList = result;
//
//		// 分页显示
//
//		// queueEntryList
//		ArrayList<QueueEntryEvent> b = null;
//		ArrayList<QueueEntryEvent> t = MyManager.getQueueEntry(queueName);
//		if (t != null) {
//			b = (ArrayList<QueueEntryEvent>) t.clone();
//		}
//		if (b != null) {
//			queueEntryList = (ArrayList<QueueEntryEvent>) b.clone();
//		}
//
//		if (iface) {
//			if (paramsAndEntry)
//				return "paramsAndEntry";
//			else
//				return "iface";
//		}
//
//		return SUCCESS;
//
//	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public boolean getIface() {
		return iface;
	}

	public void setIface(boolean iface) {
		this.iface = iface;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueMemberList(ArrayList<QueueMemberEvent> queueMemberList) {
		this.queueMemberList = queueMemberList;
	}

	public ArrayList<QueueMemberEvent> getQueueMemberList() {
		return queueMemberList;
	}

	public void setQueueEntryList(ArrayList<QueueEntryEvent> queueEntryList) {
		this.queueEntryList = queueEntryList;
	}

	public ArrayList<QueueEntryEvent> getQueueEntryList() {
		return queueEntryList;
	}

	public void setQueueParamsEvent(QueueParamsEvent queueParamsEvent) {
		this.queueParamsEvent = queueParamsEvent;
	}

	public QueueParamsEvent getQueueParamsEvent() {
		return queueParamsEvent;
	}

	public void setFree(int free) {
		this.free = free;
	}

	public int getFree() {
		return free;
	}

	public void setRinging(int ringing) {
		this.ringing = ringing;
	}

	public int getRinging() {
		return ringing;
	}

	public void setBusy(int busy) {
		this.busy = busy;
	}

	public int getBusy() {
		return busy;
	}

	public void setUnavailable(int unavailable) {
		this.unavailable = unavailable;
	}

	public int getUnavailable() {
		return unavailable;
	}

	public void setQueues(List<Queue> queues) {
		this.queues = queues;
	}

	public List<Queue> getQueues() {
		return queues;
	}

	public int getQueueMemberCount() {
		return queueMemberCount;
	}

	public void setQueueMemberCount(int queueMemberCount) {
		this.queueMemberCount = queueMemberCount;
	}

	public int getPausedMemberCount() {
		return pausedMemberCount;
	}

	public void setPausedMemberCount(int pausedMemberCount) {
		this.pausedMemberCount = pausedMemberCount;
	}

	public int getUnpausedMemberCount() {
		return unpausedMemberCount;
	}

	public void setUnpausedMemberCount(int unpausedMemberCount) {
		this.unpausedMemberCount = unpausedMemberCount;
	}

	public int getLoginMemberCount() {
		return loginMemberCount;
	}

	public void setLoginMemberCount(int loginMemberCount) {
		this.loginMemberCount = loginMemberCount;
	}

	public void setLoginPausedMemberCount(int loginPausedMemberCount) {
		this.loginPausedMemberCount = loginPausedMemberCount;
	}

	public int getLoginPausedMemberCount() {
		return loginPausedMemberCount;
	}

	public void setLoginUnpausedMemberCount(int loginUnpausedMemberCount) {
		this.loginUnpausedMemberCount = loginUnpausedMemberCount;
	}

	public int getLoginUnpausedMemberCount() {
		return loginUnpausedMemberCount;
	}

	public String getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getMaxPageIndex() {
		return maxPageIndex;
	}

	public void setMaxPageIndex(int maxPageIndex) {
		this.maxPageIndex = maxPageIndex;
	}

	public List<Integer> getPages() {
		return pages;
	}

	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setParamsAndEntry(boolean paramsAndEntry) {
		this.paramsAndEntry = paramsAndEntry;
	}

	public boolean isParamsAndEntry() {
		return paramsAndEntry;
	}

	public void setQueueParamsEventList(
			List<QueueParamsEvent> queueParamsEventList) {
		this.queueParamsEventList = queueParamsEventList;
	}

	public List<QueueParamsEvent> getQueueParamsEventList() {
		return queueParamsEventList;
	}

	public void setQueueStatusList(List<QueueStatus> queueStatusList) {
		this.queueStatusList = queueStatusList;
	}

	public List<QueueStatus> getQueueStatusList() {
		return queueStatusList;
	}

	public void setAllQueueStatus(boolean allQueueStatus) {
		this.allQueueStatus = allQueueStatus;
	}

	public boolean isAllQueueStatus() {
		return allQueueStatus;
	}

}
