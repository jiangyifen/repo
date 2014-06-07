package com.jiangyifen.ec.actions;

import com.jiangyifen.ec.dao.DialTask;

public class DialTaskUpdateAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6461944034433702941L;
	private String id;
	private String taskName;
	private String queueName;
	private String status;
	private String rate = "1";
	private Integer perority;

	public String execute() throws Exception {

		if (id != null && taskName != null && queueName != null && rate != null) {
			DialTask dt = new DialTask();
			dt.setId(new Long(id));
			dt.setTaskName(taskName);
			dt.setQueueName(queueName);
			dt.setStatus(DialTask.STATUS_STOP);
			dt.setRate(Double.valueOf(rate));
			dt.setPerority(perority);
			dialTaskManager.update(dt);
			
			if(perority.intValue()==0){
				dialTaskManager.setPerorityToZero(Long.valueOf(id));
			}
			
		}
		return SUCCESS;

	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getRate() {
		return rate;
	}

	public void setPerority(Integer perority) {
		this.perority = perority;
	}

	public Integer getPerority() {
		return perority;
	}

}
