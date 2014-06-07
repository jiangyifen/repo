package com.jiangyifen.ec.actions;

public class QueueDeleteAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6354492468412794437L;
	private String[] q;

	public String execute() throws Exception {

		if (q != null) {
			
			for (int i = 0; i < q.length; i++){
				queueManager.deleteQueue(q[i]);
				logger.info("Queue " + q[i] + " deleted.");
			}
			return SUCCESS;
		} else {
			return INPUT;
		}
	}

	public void setQ(String[] q) {
		this.q = q;
	}

	public String[] getQ() {
		return q;
	}

}
