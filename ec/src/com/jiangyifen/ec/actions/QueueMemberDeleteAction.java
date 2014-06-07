package com.jiangyifen.ec.actions;

public class QueueMemberDeleteAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6354492468412794437L;
	private String queuename;
	private String iface;

	public String execute() throws Exception {
		queueMemberManager.deleteQueueMember(queuename, iface);
		return SUCCESS;
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

}
