package com.jiangyifen.ec.actions.ami;

import org.asteriskjava.manager.action.RedirectAction;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.manager.ActionSendQueue;
import com.jiangyifen.ec.manager.MyManager;
import com.jiangyifen.ec.util.Config;

public class MeetMeStartAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7314893745499059993L;
	private String u;
	private String p;

	private String sipName;
	private String confno;

	public String execute() throws Exception {

		String iface_u = Config.props.getProperty("3rd_username");
		String iface_p = Config.props.getProperty("3rd_password");

		if (!(iface_u.equals(u) && iface_p.equals(p))) {
			return LOGIN;
		}

		if (sipName == null || confno==null) {
			return INPUT;
		}

		turnToMeetMe(sipName,confno);

		return SUCCESS;

	}

	private void turnToMeetMe(String sipName,String confno) {

		String channel1 = "";
		String channel2 = "";
		String tempValue = "";
		try {
			tempValue = MyManager.getActiveChannel(sipName);

			if (tempValue == null) {
				logger.info("MeetMeStart: " + sipName
						+ " has no active channels");
				return;
			} else {
				String[] channels = tempValue.split(";");
				if (channels != null && channels.length == 2) {
					channel1 = tempValue.split(";")[0];
					channel2 = tempValue.split(";")[1];
				} else {
					logger.error("MeetMeStart: channels==null or channels.length!=2");
					return;
				}
			}
			if (channel1 != null && channel2 != null) {

				String exten = "998" + confno;

				RedirectAction action = new RedirectAction();
				action.setChannel(channel1);
				action.setExtraChannel(channel2);
				action.setContext("incoming");
				action.setExtraContext("incoming");
				action.setExten(exten);
				action.setExtraExten(exten);
				action.setPriority(1);
				action.setExtraPriority(1);

				ActionSendQueue.actionQueue.offer(action);
				
//				String ip = Config.props.getProperty("manager_ip");
//				String username = Config.props.getProperty("manager_username");
//
//				String password = Config.props.getProperty("manager_password");
//
//				ManagerConnectionFactory factory = new ManagerConnectionFactory(
//						ip, username, password);
//
//				ManagerConnection managerConnection = factory
//						.createManagerConnection();
//
//				managerConnection.login();
//				managerConnection.sendAction(action, null);
//				managerConnection.logoff();
				logger.info("MeetMeStart: redirect " + channel1 + " and "
						+ channel2 + " to meetme");
			}
		} 
//		catch (TimeoutException e) {
//			logger.error(e.getMessage());
//		} 
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public String getU() {
		return u;
	}

	public void setU(String u) {
		this.u = u;
	}

	public String getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}

	public void setSipName(String sipName) {
		this.sipName = sipName;
	}

	public String getSipName() {
		return sipName;
	}

	public void setConfno(String confno) {
		this.confno = confno;
	}

	public String getConfno() {
		return confno;
	}

}
