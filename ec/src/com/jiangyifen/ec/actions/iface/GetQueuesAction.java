package com.jiangyifen.ec.actions.iface;

import java.util.List;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.dao.Queue;
import com.jiangyifen.ec.util.Config;

public class GetQueuesAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8716540975120588705L;
	private String u;
	private String p;

	private String queues = "";

	private String errorMsg;

	public String execute() throws Exception {

		String trd_username = Config.props.getProperty("3rd_username");
		String trd_password = Config.props.getProperty("3rd_password");

		if (u == null || p == null) {
			setErrorMsg("GetQueuesAction: u == null || p == null || username == null");
			logger.warn(errorMsg);
			return INPUT;
		} else if (!u.equals(trd_username) || !p.equals(trd_password)) {
			setErrorMsg("GetQueuesAction:  invalid u & p!");
			logger.warn(errorMsg);
			return INPUT;
		} else {
				List<Queue> queueSet = queueManager.getQueues();
				
				if (queueSet == null) {
					queues = "";
				} else {
					for (Queue queue : queueSet) {
						queues = queues + queue.getName()+":"+queue.getDescription() + ",";
					}
					
					if (queues.length() > 0)
						queues = queues.substring(0, queues.length() - 1);
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