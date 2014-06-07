package com.jiangyifen.ec.actions.ami;

import java.io.IOException;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.manager.ActionSendQueue;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.ShareData;

public class MeetMeInviteAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9120197263360690984L;

	private String u;
	private String p;

	private String number;
	private String context = "incoming";
	private String exten;
	private Integer priority = 1;

	private String confno;

	public String execute() {

		String iface_u = Config.props.getProperty("3rd_username");
		String iface_p = Config.props.getProperty("3rd_password");

		if (!(iface_u.equals(u) && iface_p.equals(p))) {
			logger.warn("MeetMeInviteAction: iface u and p is incorrect!");
			return LOGIN;
		}

		if (number == null || number.equals("")) {
			logger.warn("MeetMeInviteAction: number=" + number);
			return INPUT;
		}

		if (confno == null || confno.equals("")) {
			logger.warn("MeetMeInviteAction: confno=" + confno);
			return INPUT;
		}

		if (ShareData.allSipName.contains(number)) {
			inviteInline(number);
		} else {
			inviteOutline(number);
		}

		return SUCCESS;

	}

	private void inviteOutline(String number) {
		// 呼叫
		OriginateAction action = new OriginateAction();
		action.setChannel("Local/998" + confno + "@channelpool");
		action.setContext("outgoing");
		action.setExten(number);
		action.setPriority(new Integer(1));

		ActionSendQueue.actionQueue.offer(action);

//		String ip = Config.props.getProperty("manager_ip");
//		String username = Config.props.getProperty("manager_username");
//		String password = Config.props.getProperty("manager_password");
//		ManagerConnectionFactory factory = new ManagerConnectionFactory(ip,
//				username, password);
//		ManagerConnection managerConnection = factory.createManagerConnection();
//
//		try {
//			managerConnection.login();
//			managerConnection.sendAction(action, null);
//			managerConnection.logoff();
//		} catch (IllegalStateException e) {
//			logger.error(e.getMessage(), e);
//		} catch (IOException e) {
//			logger.error(e.getMessage(), e);
//		} catch (AuthenticationFailedException e) {
//			logger.error(e.getMessage(), e);
//		} catch (TimeoutException e) {
//			logger.error(e.getMessage());
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//		}
	}

	private void inviteInline(String number) {

		exten = "998" + confno;
		OriginateAction action = new OriginateAction();
		action.setChannel("SIP/" + number);
		action.setCallerId(number);
		action.setContext(context);
		action.setExten(exten);
		action.setPriority(priority);

		logger.info("MeetMeInviteAction: channel=" + action.getChannel()
				+ ", callerid=" + number + ", context=" + context + ", exten="
				+ exten);

		String ip = Config.props.getProperty("manager_ip");
		String username = Config.props.getProperty("manager_username");
		String password = Config.props.getProperty("manager_password");
		ManagerConnectionFactory factory = new ManagerConnectionFactory(ip,
				username, password);
		ManagerConnection managerConnection = factory.createManagerConnection();

		try {
			managerConnection.login();
			managerConnection.sendAction(action, null);
			managerConnection.logoff();
		} catch (IllegalStateException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (AuthenticationFailedException e) {
			logger.error(e.getMessage(), e);
		} catch (TimeoutException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getExten() {
		return exten;
	}

	public void setExten(String exten) {
		this.exten = exten;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public void setConfno(String confno) {
		this.confno = confno;
	}

	public String getConfno() {
		return confno;
	}

}
