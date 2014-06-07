package com.jiangyifen.ec.manager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.regex.Pattern;

import org.asteriskjava.manager.AbstractManagerEventListener;
import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.LinkEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.QueueCallerAbandonEvent;
import org.asteriskjava.manager.event.QueueEntryEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.asteriskjava.manager.event.QueueMemberPausedEvent;
import org.asteriskjava.manager.event.QueueMemberStatusEvent;
import org.asteriskjava.manager.event.QueueParamsEvent;
import org.asteriskjava.manager.event.StatusCompleteEvent;
import org.asteriskjava.manager.event.StatusEvent;
import org.asteriskjava.manager.event.UnlinkEvent;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.backgroundthreads.CdrUpdateThread;
import com.jiangyifen.ec.beans.PopStatus;
import com.jiangyifen.ec.dao.QueueCallerAbandonEventLog;
import com.jiangyifen.ec.dao.QueueMemberPauseLog;
import com.jiangyifen.ec.popcommandsender.CrmPopCommandSenderThread;
import com.jiangyifen.ec.popserver.CrmClientThread;
import com.jiangyifen.ec.popserver.CrmPopServer;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.PopData;
import com.jiangyifen.ec.util.ShareData;

public class MyEventListener extends AbstractManagerEventListener {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final String crmServerUrl = Config.props
			.getProperty("crm_server_url");

	private final String crm_pop3rd_url = Config.props
			.getProperty("crm_pop3rd_url");

	class PopThread extends Thread {
		String callerNum;
		String fenji;
		String uniqueid;

		public PopThread(String callerNum, String fenji, String uniqueid) {
			this.callerNum = callerNum;
			this.fenji = fenji;
			this.uniqueid = uniqueid;
			start();
		}

		public void run() {
			try {
				String urlString = crmServerUrl + "?";

				urlString = urlString + "type=1&fenji=" + fenji + "&callernum="
						+ callerNum + "&uniqueid=" + uniqueid;
				logger.info(urlString);

				URL url = new URL(urlString);

				url.openStream();

			} catch (MalformedURLException e) {
				logger.error(e.getMessage(), e);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private void pop3rdUrl(String callerNum, String fenji, String uniqueid) {
		new PopThread(callerNum, fenji, uniqueid);
	}

	protected void handleEvent(QueueMemberPausedEvent event) {

		// QueueMemberPause发生时，需要记录日志，用来统计置忙时长
		if (event.getPaused()) {
			// pause

			// QueueMemberPauseLog
			// 生成一条新纪录，保存
			String memberName = event.getMemberName();
			String queueName = event.getQueue();
			String sipname = memberName.substring(memberName.indexOf("/") + 1);
			String username = ShareData.sipAndLoginusername.get(sipname);

			QueueMemberPauseLog log = new QueueMemberPauseLog();
			log.setMemberName(memberName);
			log.setQueue(queueName);
			log.setPauseDate(new Date());
			log.setUsername(username);

			MyManager.queueMemberPauseLogManager.save(log);

		} else {
			// unpause

			// QueueMemberPauseLog
			// 获取该queueMember的上一条日志，看看有没有unpause信息。
			QueueMemberPauseLog log = MyManager.queueMemberPauseLogManager
					.findLastLogByMemberNameAndQueueName(event.getMemberName(),
							event.getQueue());

			// 如有记录且没有unpause信息，便把此次unpause信息写入。
			if (log != null && log.getUnpauseDate() == null) {
				log.setUnpauseDate(new Date());
				MyManager.queueMemberPauseLogManager.update(log);
			}
			// 若无记录，或已有记录且有登出信息，则新增一条记录，仅有登出信息没有登录信息
			else {
				String memberName = event.getMemberName();
				String queueName = event.getQueue();
				String sipname = memberName
						.substring(memberName.indexOf("/") + 1);
				String username = ShareData.sipAndLoginusername.get(sipname);

				log = new QueueMemberPauseLog();
				log.setMemberName(memberName);
				log.setQueue(queueName);
				log.setUnpauseDate(new Date());
				log.setUsername(username);

				MyManager.queueMemberPauseLogManager.save(log);
			}

		}

	}

	protected void handleEvent(LinkEvent event) {
		// logger.info(event);
		// 监控正在通话的channel用（实现强插）
		// 把接通的channel放入容器
		String channel1 = event.getChannel1();
		String channel2 = event.getChannel2();

		String fenji = channel1.substring(channel1.indexOf("/") + 1,
				channel1.indexOf("-"));
		MyManager.putActiveChannel(fenji, channel1 + ";" + channel2);

		String fenji2 = channel2.substring(channel2.indexOf("/") + 1,
				channel2.indexOf("-"));
		MyManager.putActiveChannel(fenji2, channel2 + ";" + channel1);


		logger.info("-------------------------------------");
		logger.info("LinkEvent: "+event);
		logger.info("-------------------------------------");
		logger.info("event.getChannel1() = "+event.getChannel1());
		logger.info("event.getUniqueId1() = "+event.getUniqueId1());
		logger.info("event.getCallerId1() = "+event.getCallerId1());
		logger.info("-------------------------------------");
		logger.info("event.getChannel2() = "+event.getChannel2());
		logger.info("event.getUniqueId2() = "+event.getUniqueId2());
		logger.info("event.getCallerId2() = "+event.getCallerId2());
		logger.info("-------------------------------------");

		//通过socket发送弹屏消息
		JSONObject pop = new JSONObject();
		try {
			pop.put("command", "linkevent");
			pop.put("channel1", event.getChannel1());
			pop.put("channel2", event.getChannel2());
			pop.put("callerid1", event.getCallerId1());
			pop.put("callerid2", event.getCallerId2());
			pop.put("uniqueid1", event.getUniqueId1());
			pop.put("uniqueid2", event.getUniqueId2());

		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		}
		
		CrmClientThread clientThread = null;
		
		clientThread = CrmPopServer.crmFenjiClients
				.get(fenji2);
		if (clientThread != null) {
			clientThread.sendMessage(pop);
			logger.info(pop.toString());
		}else{
			logger.warn(fenji2+" have no client thread");
		}
		
		clientThread = CrmPopServer.crmFenjiClients
				.get(fenji);
		if (clientThread != null) {
			clientThread.sendMessage(pop);
			logger.info(pop.toString());
		}else{
			logger.warn(fenji+" have no client thread");
		}

	}

	protected void handleEvent(UnlinkEvent event) {
		// logger.info(event);
		// 监控正在通话的channel用（实现强插）
		// 把挂断的channel移出容器
		String channel1 = event.getChannel1();
		String channel2 = event.getChannel2();

		String fenji = channel1.substring(channel1.indexOf("/") + 1,
				channel1.indexOf("-"));
		MyManager.removeActiveChannel(fenji);

		String fenji2 = channel2.substring(channel2.indexOf("/") + 1,
				channel2.indexOf("-"));
		MyManager.removeActiveChannel(fenji2);

		logger.info("-------------------------------------");
		logger.info("UnlinkEvent: ");
		logger.info("-------------------------------------");
		logger.info("event.getChannel1() = "+event.getChannel1());
		logger.info("event.getUniqueId1() = "+event.getUniqueId1());
		logger.info("event.getCallerId1() = "+event.getCallerId1());
		logger.info("-------------------------------------");
		logger.info("event.getChannel2() = "+event.getChannel2());
		logger.info("event.getUniqueId2() = "+event.getUniqueId2());
		logger.info("event.getCallerId2() = "+event.getCallerId2());
		logger.info("-------------------------------------");
		

		//通过socket发送弹屏消息
		JSONObject pop = new JSONObject();
		try {
			pop.put("command", "unlinkevent");
			pop.put("channel1", event.getChannel1());
			pop.put("channel2", event.getChannel2());
			pop.put("callerid1", event.getCallerId1());
			pop.put("callerid2", event.getCallerId2());
			pop.put("uniqueid1", event.getUniqueId1());
			pop.put("uniqueid2", event.getUniqueId2());

		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		}

		CrmClientThread clientThread = null;
		
		clientThread = CrmPopServer.crmFenjiClients
				.get(fenji2);
		if (clientThread != null) {
			clientThread.sendMessage(pop);
			logger.info(pop.toString());
		}else{
			logger.warn(fenji2+" have no client thread");
		}
		
		clientThread = CrmPopServer.crmFenjiClients
				.get(fenji);
		if (clientThread != null) {
			clientThread.sendMessage(pop);
			logger.info(pop.toString());
		}else{
			logger.warn(fenji+" have no client thread");
		}
	}

	// 获取当前所有的状态用，实现监控面板
	protected void handleEvent(StatusEvent event) {
		// logger.info("jiangyifen: " + event);
		if (event.getExtension() != null) {
			MyManager.addStatusEvent(event);
		}
	}

	// 获取当前所有的状态用，实现监控面板
	protected void handleEvent(StatusCompleteEvent event) {
		MyManager.statusEventsIsReady = true;
	}

	// 实现弹屏
	protected void handleEvent(NewStateEvent event) {


		if (event.getState().equals("Ringing")
				&& Config.props.getProperty("crm_pop_in_newstatusevent")
						.equals("true")) {

			String to = event.getChannel().substring(
					event.getChannel().indexOf('/') + 1,
					event.getChannel().indexOf('-'));

			String from = event.getCallerIdNum();

			logger.info("NewStateEvent:"+event.toString());
			logger.info("NewStateEvent:"+"to=" + to);
			logger.info("NewStateEvent:"+"from=" + from);

			logger.info("NewStateEvent:"+"!ShareData.allSipName.contains(from) = "
					+ !ShareData.allSipName.contains(from));
			logger.info("NewStateEvent:"+"ShareData.allSipName.contains(to) = "
					+ ShareData.allSipName.contains(to));
			logger.info("NewStateEvent:"+"Config.props.getProperty(\"crm_pop_in_newstatusevent_in\").equals(\"true\") = "
					+ Config.props.getProperty("crm_pop_in_newstatusevent_in")
							.equals("true"));

			// 外线打给分机的情况（呼入）
			if (!ShareData.allSipName.contains(from)
					&& ShareData.allSipName.contains(to)
					&& Config.props.getProperty("crm_pop_in_newstatusevent_in")
							.equals("true")) {
				logger.info("NewStateEvent:"+"外线打给分机的情况（呼入）");
				if (from == null) {
					from = " ";
				}
				Pattern pattern = Pattern.compile("[0-9]*");
				if (!pattern.matcher(from).matches()) {
					from = " ";
				}
				if (from.length() >= 8) {
					if (!(from.startsWith("1") || from.startsWith("0"))) {
						from = "021" + from;
					}
				}

//				logger.info("===================================");
//				logger.info("from=" + from);
//				for (String key : PopData.calleridnumAndPop.keySet()) {
//					logger.info("+ " + key + " : "
//							+ PopData.calleridnumAndPop.get(key));
//				}
//
//				logger.info("===================================");

				PopStatus popStatus = PopData.calleridnumAndPop.get(from);

				String ly = null;
				String keypress = "";
				if (popStatus != null) {
					// 来源号码
					ly = popStatus.getOutlineNum();
					// 客户按键
					for (String key : popStatus.getKeyPressed()) {
						keypress = keypress + key;
					}
				}
				if (ly == null) {
					ly = "UNKNOW";
				}

				// 用老的中转程序弹屏
				String[] parameters = { from, to, ly, keypress };
				new CrmPopCommandSenderThread().sendCommand("POPWINDOW",
						parameters);

				// 用新的中转程序弹屏
				if (popStatus != null) {

					JSONObject pop = new JSONObject();
					try {
						pop.put("command", "pop");
						pop.put("fenji", to);
						pop.put("callernum", from);
						pop.put("ly", ly);
						pop.put("uniqueid", popStatus.getUniqueid());
						pop.put("keypress", popStatus.getKeyPressed());
					} catch (JSONException e) {
						logger.error(e.getMessage(), e);
					}

					CrmClientThread clientThread = CrmPopServer.crmFenjiClients
							.get(to);
					if (clientThread != null) {
						clientThread.sendMessage(pop);
						logger.info(pop.toString());
					}

				} else {
					logger.warn("PopStatus of " + from + " is null.");
				}

				// 调用第三方url弹屏
				if (crm_pop3rd_url.equals("true")) {
					pop3rdUrl(from, to, popStatus.getUniqueid());
				}

			}
			// 分机打给外线的情况（呼出）
			else if (ShareData.allSipName.contains(from)
					&& !ShareData.allSipName.contains(to)
					&& Config.props
							.getProperty("crm_pop_in_newstatusevent_out")
							.equals("true")) {
				logger.info("NewStateEvent:"+"分机打给外线的情况（呼出）");
				// 用老的中转程序弹屏
				String[] parameters = { to, from, "CALLOUT", "" };
				new CrmPopCommandSenderThread().sendCommand("POPWINDOW",
						parameters);

				// 用新的中转程序弹屏

				JSONObject pop = new JSONObject();
				try {
					pop.put("command", "pop");
					pop.put("fenji", from);
					pop.put("callernum", to);
					pop.put("ly", "CALLOUT");
					pop.put("uniqueid", event.getUniqueId());
					pop.put("keypress", "[]");
				} catch (JSONException e) {
					logger.error(e.getMessage(), e);
				}

				CrmClientThread clientThread = CrmPopServer.crmFenjiClients
						.get(from);
				if (clientThread != null) {
					clientThread.sendMessage(pop);
					logger.info(pop.toString());
				}

				// 调用第三方url弹屏
				if (crm_pop3rd_url.equals("true")) {
					pop3rdUrl(to, from, event.getUniqueId());
				}

			}
			// 转接以及分机互打的情况（内部呼叫）
			else if (ShareData.allSipName.contains(from)
					&& ShareData.allSipName.contains(to)) {

				logger.info("NewStateEvent:"+"转接以及分机互打的情况（内部呼叫）");

				// 要检查是分机叫分机，还是分机将一通外来呼叫转接给另一分机。
				// 如果是分机叫分机，就什么都不做。
				// 如果是转接的情况，就需要弹屏。
				// 如何检查？
				// 在channelRedirectActon里，除了发出redirectAction之外，还会以
				// 目标分机号为key，popStatus对象为value。
				// 目标分机号也就是event中的channel的号码部分，对应to变量。
				PopStatus popStatus = PopData.fenjiAndPop.get(to);
				if (popStatus != null) {

					String keypress = "";

					// 客户按键
					for (String key : popStatus.getKeyPressed()) {
						keypress = keypress + key;
					}

					// 用老的中转程序弹屏
					String[] parameters = { popStatus.getCalleridNum(), to,
							popStatus.getOutlineNum(), keypress };
					new CrmPopCommandSenderThread().sendCommand("POPWINDOW",
							parameters);

					// 用新的中转程序弹屏
					JSONObject pop = new JSONObject();
					try {
						pop.put("command", "pop");
						pop.put("fenji", to);
						pop.put("callernum", popStatus.getCalleridNum());
						pop.put("ly", popStatus.getOutlineNum());
						pop.put("uniqueid", popStatus.getUniqueid());
						pop.put("keypress", popStatus.getKeyPressed());
					} catch (JSONException e) {
						logger.error(e.getMessage(), e);
					}

					CrmClientThread clientThread = CrmPopServer.crmFenjiClients
							.get(to);
					if (clientThread != null) {
						clientThread.sendMessage(pop);
						logger.info(pop.toString());
					}

					// 调用第三方url弹屏
					if (crm_pop3rd_url.equals("true")) {
						pop3rdUrl(popStatus.getCalleridNum(), to,
								popStatus.getUniqueid());
					}
				} else {
					logger.warn("PopStatus of " + from + " is null.");
				}

			} else {
				logger.info("NewStateEvent: NOPOP "+event.toString());
				logger.info("NewStateEvent: NOPOP "+"to=" + to + ", from=" + from);
			}

		}
	}

	protected void handleEvent(CdrEvent event) {
		CdrUpdateThread.cdrEvents.add(event);
	}

	protected void handleEvent(QueueParamsEvent event) {
		MyManager.putQueueParams(event.getQueue(), event);
	}

	protected void handleEvent(QueueMemberEvent event) {
		if (event instanceof QueueMemberStatusEvent) {
			return;
		}
		MyManager.addQueueMember(event.getQueue(), event);
	}

	protected void handleEvent(QueueEntryEvent event) {
		MyManager.addQueueEntry(event.getQueue(), event);
	}

	protected void handleEvent(QueueCallerAbandonEvent event) {

		QueueCallerAbandonEventLog log = new QueueCallerAbandonEventLog();
		log.setChannel(event.getChannel());
		log.setCount(event.getCount());
		log.setDateReceived(event.getDateReceived());
		log.setHoldtime(event.getHoldTime());
		log.setOriginalposition(event.getOriginalPosition());
		log.setPosition(event.getPosition());
		log.setQueue(event.getQueue());
		log.setUniqueid(event.getUniqueId());

		MyManager.queueCallerAbandonEventLogManager.save(log);

	}

}
