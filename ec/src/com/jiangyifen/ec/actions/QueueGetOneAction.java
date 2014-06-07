package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.List;

import com.jiangyifen.ec.dao.Queue;

public class QueueGetOneAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4681086391687751015L;
	
	private String queuename;
	private List<Queue> queues = new ArrayList<Queue>();
	

	public String execute() throws Exception {
		queues.add(queueManager.getQueue(queuename));
		return SUCCESS;
	}

	public void setQueuename(String queuename) {
		this.queuename = queuename;
	}

	public String getQueuename() {
		return queuename;
	}

	public void setQueues(List<Queue> queues) {
		this.queues = queues;
	}

	public List<Queue> getQueues() {
		return queues;
	}


}

