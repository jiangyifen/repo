package com.jiangyifen.ec.actions;

import java.util.List;

import com.jiangyifen.ec.dao.DialTask;

public class DialTaskAddAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2669331349366647145L;
	private String taskName;
	private String queueName;
	private String status;
	private String rate = "1";
	private Integer perority;

	public String execute() throws Exception {

		//确保分派给同一队列的dialtask只有一个优先级为0
		if (perority.intValue() == 0) {
			List<DialTask> dialTaskList = dialTaskManager.findByQueueName(queueName);
			for(DialTask dt:dialTaskList){
				if(dt.getPerority().intValue()==0){
					dt.setPerority(1);
					dialTaskManager.update(dt);
				}
			}
		}

		//新增dialtask
		DialTask dt = new DialTask();
		dt.setTaskName(taskName);
		dt.setQueueName(queueName);
		dt.setStatus(DialTask.STATUS_STOP);
		dt.setRate(Double.valueOf(rate));
		dt.setPerority(perority);
		dialTaskManager.save(dt);

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
