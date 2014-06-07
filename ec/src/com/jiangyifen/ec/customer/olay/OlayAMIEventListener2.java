package com.jiangyifen.ec.customer.olay;

import java.net.URL;
import java.util.regex.Pattern;

import org.asteriskjava.manager.AbstractManagerEventListener;
import org.asteriskjava.manager.event.LinkEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.UnlinkEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.beans.PopStatus;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.PopData;
import com.jiangyifen.ec.util.ShareData;

public class OlayAMIEventListener2 extends AbstractManagerEventListener {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	// 实现弹屏
	protected void handleEvent(NewStateEvent event) {

		if (event.getState().equals("Ringing")
				&& Config.props.getProperty("crm_pop_in_newstatusevent")
						.equals("true")) {

			String to = event.getChannel().substring(
					event.getChannel().indexOf('/') + 1,
					event.getChannel().indexOf('-'));

			String from = event.getCallerIdNum();

			logger.info(event.toString());
			logger.info("to=" + to);
			logger.info("from=" + from);

			// 外线打给分机的情况（呼入）
			if (!ShareData.allSipName.contains(from)
					&& ShareData.allSipName.contains(to)
					&& Config.props.getProperty("crm_pop_in_newstatusevent_in")
							.equals("true")) {
				logger.info("外线打给分机的情况（呼入）");
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

				try {

					PopStatus ps = PopData.calleridnumAndPop.get(from);

					 String channel = ps.getChannel();
					 String userId = OlayShareDate.userChannelAndUserInput
					 .get(channel);

					String uniqueId = ps.getUniqueid();

					String loginusername = com.jiangyifen.ec.util.ShareData.sipAndLoginusername
							.get(to);

					URL url = new URL(
							Config.props.getProperty("crm_server_url")
									+ "?method=AgentStatus_Connecting&loginName="
									+ userId + "&callSessionId=" + uniqueId
									+ "&jobNo=" + loginusername + "");
					url.openStream();

					logger.info("startCall: " + url);
					
					
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}

			}
			// 分机打给外线的情况（呼出）
			else if (ShareData.allSipName.contains(from)
					&& !ShareData.allSipName.contains(to)
					&& Config.props
							.getProperty("crm_pop_in_newstatusevent_out")
							.equals("true")) {
				logger.info("分机打给外线的情况（呼出）");

			}
			// 转接以及分机互打的情况（内部呼叫）
			else if (ShareData.allSipName.contains(from)
					&& ShareData.allSipName.contains(to)) {

				logger.info("转接以及分机互打的情况（内部呼叫）");

			} else {
				logger.info(event.toString());
				logger.info("to=" + to + ", from=" + from);
			}

		}
	}

	protected void handleEvent(LinkEvent event) {

	}

	protected void handleEvent(UnlinkEvent event) {

		logger.info("-------------------------------------");
		logger.info("UnlinkEvent: ");
		logger.info("-------------------------------------");
		logger.info("event.getChannel1() = " + event.getChannel1());
		logger.info("event.getUniqueId1() = " + event.getUniqueId1());
		logger.info("event.getCallerId1() = " + event.getCallerId1());
		logger.info("-------------------------------------");
		logger.info("event.getChannel2() = " + event.getChannel2());
		logger.info("event.getUniqueId2() = " + event.getUniqueId2());
		logger.info("event.getCallerId2() = " + event.getCallerId2());
		logger.info("-------------------------------------");

		
		//调用webservice接口，通知通话结束
		try {

			String uniqueId = event.getUniqueId1();

			URL url = new URL(Config.props.getProperty("crm_server_url")
					+ "?method=AgentStatus_Disconnected&callSessionId="
					+ uniqueId);
			url.openStream();

			logger.info("endCall" + url);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

}
