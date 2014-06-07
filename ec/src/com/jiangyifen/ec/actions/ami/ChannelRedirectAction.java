package com.jiangyifen.ec.actions.ami;

import org.asteriskjava.manager.action.RedirectAction;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.beans.PopStatus;
import com.jiangyifen.ec.manager.ActionSendQueue;
import com.jiangyifen.ec.manager.MyManager;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.PopData;

public class ChannelRedirectAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2135571552207982371L;
	private String u;
	private String p;

	private String sipName;
	private String channel = null;
	private String context = "outgoing";
	private String exten;
	private Integer priority = 1;

	public String execute() {

		String iface_u = Config.props.getProperty("3rd_username");
		String iface_p = Config.props.getProperty("3rd_password");

		if (!(iface_u.equals(u) && iface_p.equals(p))) {
			return LOGIN;
		}

		if (sipName == null) {
			return INPUT;
		}

		String temp = MyManager.getActiveChannel(sipName);
		if (temp != null) {
			channel = temp.split(";")[1];
		}
		if (channel != null) {
			// 以channel名查询出对应的popStatus，并放入 PopData.fenjiAndPop，以供转接情况的new
			// status event ringing时用分机号查询pop数据
			PopStatus popStatus = PopData.channelAndPop.get(channel);
			if (popStatus != null) {
				PopData.fenjiAndPop.put(exten, popStatus);

				new Thread() {
					public void run() {
						try {
							sleep(30000);
							PopData.fenjiAndPop.remove(exten);
							logger.info("remove " + exten + " from PopData.fenjiAndPop");
						} catch (InterruptedException e) {
							logger.error(e.getMessage(), e);
						}
					}
				}.start();

			}

			// 发送redirectAction
			RedirectAction action = new RedirectAction(channel, context, exten,
					priority);

			
			ActionSendQueue.actionQueue.offer(action);
			
//			String ip = Config.props.getProperty("manager_ip");
//			String username = Config.props.getProperty("manager_username");
//
//			String password = Config.props.getProperty("manager_password");
//
//			ManagerConnectionFactory factory = new ManagerConnectionFactory(ip,
//					username, password);
//
//			ManagerConnection managerConnection = factory
//					.createManagerConnection();
//
//			try {
//				managerConnection.login();
//				managerConnection.sendAction(action, null);
//				managerConnection.logoff();
//			} catch (IllegalStateException e) {
//				logger.error(e.getMessage(), e);
//			} catch (IOException e) {
//				logger.error(e.getMessage(), e);
//			} catch (AuthenticationFailedException e) {
//				logger.error(e.getMessage(), e);
//			} catch (TimeoutException e) {
//				logger.error(e.getMessage());
//			} catch (Exception e) {
//				logger.error(e.getMessage(), e);
//			}

			logger.info("ChannelRedirect: " + channel + " " + context + " "
					+ exten + " " + priority);
		}

		return SUCCESS;

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

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setExten(String exten) {
		this.exten = exten;
	}

	public String getExten() {
		return exten;
	}

	public void setSipName(String sipName) {
		this.sipName = sipName;
	}

	public String getSipName() {
		return sipName;
	}

}
