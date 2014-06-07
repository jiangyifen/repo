package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.jiangyifen.ec.dao.Queue;
import com.jiangyifen.ec.dao.User;
import com.jiangyifen.ec.util.ShareData;

public class QueueUserLinkUpdateAction extends BaseAction {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1996575565751780676L;

	private List<Queue> queues;

	private String queueName;

	private List<String> toBox = new ArrayList<String>();


	public String execute() throws Exception {

		Queue queue = queueManager.getQueue(queueName);
		Set<User> users = queue.getUsers();
		if(users!=null){
			users.clear();
		}
		
		@SuppressWarnings("unchecked")
		HashMap<String,User> allUsers = (HashMap<String,User>)ShareData.usernameAndUser.clone();
		for(String username: toBox){
			User user = allUsers.get(username);
			if(user!=null){
				users.add(user);
			}
		}
		
		queueManager.updateQueue(queue);
		
		
		return SUCCESS;

	}

	public void setQueues(List<Queue> queues) {
		this.queues = queues;
	}

	public List<Queue> getQueues() {
		return queues;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setToBox(List<String> toBox) {
		this.toBox = toBox;
	}

	public List<String> getToBox() {
		return toBox;
	}

}
