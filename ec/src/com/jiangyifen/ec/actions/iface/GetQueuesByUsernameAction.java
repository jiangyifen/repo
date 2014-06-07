package com.jiangyifen.ec.actions.iface;

import java.util.Set;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.dao.Queue;
import com.jiangyifen.ec.dao.User;
import com.jiangyifen.ec.util.Config;

public class GetQueuesByUsernameAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2405992679036108497L;

	private String u;
	private String p;

	private String username;
	private String queues = "";

	private String errorMsg;

	public String execute() throws Exception {

		String trd_username = Config.props.getProperty("3rd_username");
		String trd_password = Config.props.getProperty("3rd_password");

		if (u == null || p == null || username == null) {
			setErrorMsg("GetQueuesByUsernameAction: u == null || p == null || username == null");
			logger.warn(errorMsg);
			return INPUT;
		} else if (!u.equals(trd_username) || !p.equals(trd_password)) {
			setErrorMsg("GetQueuesByUsernameAction:  invalid u & p!");
			logger.warn(errorMsg);
			return INPUT;
		} else {
			User user = userManager.getUser(username);
			if (user == null) {
				setErrorMsg("GetQueuesByUsernameAction: username not found");
				logger.warn(errorMsg);
				return INPUT;
			} else {
				Set<Queue> queueSet = user.getQueues();
				
				if (queueSet == null) {
					queues = "";
				} else {
					for (Queue queue : queueSet) {
						queues = queues + queue.getName()+":"+queue.getDescription() + ",";
					}
					
					if (queues.length() > 0)
						queues = queues.substring(0, queues.length() - 1);
				}
			}

			return "iface";
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

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setQueues(String queues) {
		this.queues = queues;
	}

	public String getQueues() {
		return queues;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
}