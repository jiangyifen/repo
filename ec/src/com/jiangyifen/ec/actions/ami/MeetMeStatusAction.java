package com.jiangyifen.ec.actions.ami;

import java.util.ArrayList;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerConnectionState;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.response.CommandResponse;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.beans.MeetMeStatus;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.MyStringUtils;
import com.jiangyifen.ec.util.ShareData;

public class MeetMeStatusAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6263570341128656588L;

	private static final Log logger = LogFactory
			.getLog(MeetMeStatusAction.class);

	private String u;
	private String p;

	private String confno;

	private ArrayList<MeetMeStatus> meetMeStatusList = new ArrayList<MeetMeStatus>();

	private static String ip;
	private static String username;
	private static String password;

	private static ManagerConnectionFactory factory;
	private static ManagerConnection managerConnection;

	public MeetMeStatusAction() {
		ip = Config.props.getProperty("manager_ip");
		username = Config.props.getProperty("manager_username");
		password = Config.props.getProperty("manager_password");

		factory = new ManagerConnectionFactory(ip, username, password);
		if (managerConnection == null || managerConnection.getState() != ManagerConnectionState.CONNECTED) {
			initManagerConnection();
		} 
	}

	private static void initManagerConnection() {
		if (managerConnection != null) {
			if (managerConnection.getState() == ManagerConnectionState.CONNECTED
					|| managerConnection.getState() == ManagerConnectionState.RECONNECTING) {
				managerConnection.logoff();
			}
		}
		managerConnection = factory.createManagerConnection();
		try {
			managerConnection.login();
			Thread.sleep(1000);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public String execute() {

		String iface_u = Config.props.getProperty("3rd_username");
		String iface_p = Config.props.getProperty("3rd_password");

		if (!(iface_u.equals(u) && iface_p.equals(p))) {
			logger.warn("MeetMeStatusAction: iface u and p is incorrect!");
			return LOGIN;
		}

		if (confno == null || confno.equals("")) {
			logger.warn("MeetMeStatusAction: confno=" + confno);
			return INPUT;
		}

		getMeetMeStatus(confno);

		return SUCCESS;

	}

	private void getMeetMeStatus(String congno) {

		// 呼叫

		// String db_ip = Config.props.getProperty("manager_ip");
		// String db_username = Config.props.getProperty("manager_username");
		// String db_password = Config.props.getProperty("manager_password");

		CommandAction action = new CommandAction("meetme list " + confno
				+ " concise");

		// ManagerConnectionFactory factory = new
		// ManagerConnectionFactory(db_ip,
		// db_username, db_password);
		// ManagerConnection managerConnection =
		// factory.createManagerConnection();

		try {

			// managerConnection.login();
			CommandResponse r = (CommandResponse) managerConnection
					.sendAction(action);

			for (String line : r.getResult()) {
				String[] fields = line.split("!");
				int position = 0;
				if (MyStringUtils.isNumeric(fields[0])) {
					position = new Integer(fields[0]);
				}
				String callerIdNum = fields[1];
				String callerIdName = fields[2];
				String channel = fields[3];
				String role = fields[4];
				String time = fields[8];
				String name = "";
				String loginusername = ShareData.sipAndLoginusername
						.get(callerIdNum);

				if (loginusername != null) {
					name = ShareData.usernameAndName.get(loginusername);
				}

				MeetMeStatus mms = new MeetMeStatus();
				mms.setPosition(position);
				mms.setCallerIdNum(callerIdNum);
				mms.setCallerIdName(callerIdName);
				mms.setChannel(channel);
				mms.setRole(role);
				mms.setTime(time);
				mms.setName(name);

				this.meetMeStatusList.add(mms);

			}

			// managerConnection.logoff();

		} catch (TimeoutException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			initManagerConnection();
		}
	}

	public ArrayList<MeetMeStatus> getMeetMeStatusList() {
		return meetMeStatusList;
	}

	public void setMeetMeStatusList(ArrayList<MeetMeStatus> meetMeStatusList) {
		this.meetMeStatusList = meetMeStatusList;
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

	public void setConfno(String confno) {
		this.confno = confno;
	}

	public String getConfno() {
		return confno;
	}

}
