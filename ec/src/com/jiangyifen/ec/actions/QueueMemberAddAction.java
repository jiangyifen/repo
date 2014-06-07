package com.jiangyifen.ec.actions;

public class QueueMemberAddAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 473378483919917905L;
	private String queuename;
	private String iface;
	private Long penalty;

	public String execute() throws Exception {
		logger.info("Queue name: " + queuename);
		logger.info("Interface: " + iface);
		logger.info("Penalty: " + penalty);
		// logger.info("" + (queueMemberManager == null));
		if (queueMemberManager.addQueueMember(queuename, iface, penalty)) {
			logger.debug("Add queue member success.");
			return SUCCESS;
		}
		logger.error("Queue member add failed.");
		return INPUT;
	}

	public void setQueuename(String queuename) {
		this.queuename = queuename;
	}

	public String getQueuename() {
		return queuename;
	}

	public void setIface(String iface) {
		this.iface = iface;
	}

	public String getIface() {
		return iface;
	}

	public void setPenalty(Long penalty) {
		this.penalty = penalty;
	}

	public Long getPenalty() {
		return penalty;
	}

}
