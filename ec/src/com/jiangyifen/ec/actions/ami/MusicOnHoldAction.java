package com.jiangyifen.ec.actions.ami;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.RedirectAction;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.manager.MyManager;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.MusicOnHoldData;

public class MusicOnHoldAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 705831626399517518L;
	private String u;
	private String p;

	private String sipName;
	private boolean moh;

	public String execute() throws Exception {

		String iface_u = Config.props.getProperty("3rd_username");
		String iface_p = Config.props.getProperty("3rd_password");

		if (!(iface_u.equals(u) && iface_p.equals(p))) {
			return LOGIN;
		}

		if (sipName == null) {
			return INPUT;
		}

		if (moh) {
			turnToMoh(sipName);
		} else {
			backFromMoh(sipName);
		}

		return SUCCESS;

	}

	private void turnToMoh(String sipName) {

		String channel1 = "";
		String channel2 = "";
		String value = "";
		try {
			value = MyManager.getActiveChannel(sipName);

			if (value == null) {
				logger.info("MusicOnHoldAction: " + sipName
						+ " has no active channels");
				return;
			} else {
				String[] channels = value.split(";");
				if (channels != null && channels.length == 2) {
					channel1 = value.split(";")[0];
					channel2 = value.split(";")[1];
				} else {
					logger.error("MusicOnHoldAction: channels==null or channels.length!=2");
					return;
				}
			}
			if (channel1 != null && channel2 != null) {
				MusicOnHoldData.putMohChannel(sipName, channel1 + ";"
						+ channel2);

				RedirectAction action = new RedirectAction();
				action.setChannel(channel1);
				action.setExtraChannel(channel2);
				action.setContext("mohpool");
				action.setExtraContext("mohpool");
				action.setExten("1");
				action.setExtraExten("1");
				action.setPriority(1);
				action.setExtraPriority(1);

				String ip = Config.props.getProperty("manager_ip");
				String username = Config.props.getProperty("manager_username");

				String password = Config.props.getProperty("manager_password");

				ManagerConnectionFactory factory = new ManagerConnectionFactory(
						ip, username, password);

				ManagerConnection managerConnection = factory
						.createManagerConnection();

				managerConnection.login();
				managerConnection.sendAction(action, null);
				managerConnection.logoff();
				logger.info("MusicOnHoldAction: " + channel1 + " and " + channel2
						+ " to mohpool ");
			}
		} catch (TimeoutException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void backFromMoh(String sipName) {

		String channel1 = "";
		String channel2 = "";
		String value = "";
		try {
			value = MusicOnHoldData.getMohChannel(sipName);

			if (value == null) {
				logger.info("MusicOnHoldAction: " + sipName
						+ " has no moh channels");
				return;
			} else {
				String[] channels = value.split(";");
				if (channels != null && channels.length == 2) {
					channel1 = value.split(";")[0];
					channel2 = value.split(";")[1];
				} else {
					logger.error("MusicOnHoldAction: channels==null or channels.length!=2");
					return;
				}
			}
			if (channel1 != null && channel2 != null) {
//				MusicOnHoldData.removeMohChannel(sipName);

				RedirectAction action = new RedirectAction();
				action.setChannel(channel1);
				action.setExtraChannel(channel2);
				action.setContext("incoming");
				action.setExtraContext("incoming");
				action.setExten("998"+sipName);
				action.setExtraExten("998"+sipName);
				action.setPriority(1);
				action.setExtraPriority(1);

				String ip = Config.props.getProperty("manager_ip");
				String username = Config.props.getProperty("manager_username");

				String password = Config.props.getProperty("manager_password");

				ManagerConnectionFactory factory = new ManagerConnectionFactory(
						ip, username, password);

				ManagerConnection managerConnection = factory
						.createManagerConnection();

				managerConnection.login();
				managerConnection.sendAction(action, null);
				managerConnection.logoff();
				logger.info("MusicOnHoldAction: " + channel1 + " and " + channel2
						+ " from mohpool to MeetMe");
			}
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

	public void setSipName(String sipName) {
		this.sipName = sipName;
	}

	public String getSipName() {
		return sipName;
	}

	public void setMoh(boolean moh) {
		this.moh = moh;
	}

	public boolean isMoh() {
		return moh;
	}

}
