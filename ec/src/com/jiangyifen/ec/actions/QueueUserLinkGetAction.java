package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.jiangyifen.ec.dao.Queue;
import com.jiangyifen.ec.dao.User;
import com.jiangyifen.ec.util.UserComparator;
import com.jiangyifen.ec.util.ShareData;

public class QueueUserLinkGetAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9168601304965106481L;

	private List<Queue> queues;

	private String queueName;

	private List<String> fromBox;
	private List<String> toBox = new ArrayList<String>();

	private List<User> fromBoxUser = new ArrayList<User>();
	private List<User> toBoxUser = new ArrayList<User>();

	public String execute() throws Exception {
		// 获取所有的queue，用于显示list
		setQueues(ShareData.allQueues);
		// 如果没有传入queueName参数，则指定一个默认值
		if (queueName == null && !queues.isEmpty()) {
			queueName = queues.get(0).getName();
		}

		// 根据queueName获取已经与该queue对应的username，赋值给toBoxUsers，用于页面显示
		// 获取所有的username，并减去toBox里的username，赋值给fromBoxUsers，用于页面显示
		@SuppressWarnings("unchecked")
		HashMap<String, User> allUsers = (HashMap<String, User>) ShareData.usernameAndUser
				.clone();

		Queue queue = queueManager.getQueue(queueName);
		Set<User> users = queue.getUsers();
		if (users != null) {
			for (User user : users) {
				toBoxUser.add(user);
				allUsers.remove(user.getUsername());
			}
		}

		for (User user : allUsers.values()) {
			fromBoxUser.add(user);
		}

		Collections.sort(toBoxUser, new UserComparator());

		Collections.sort(fromBoxUser, new UserComparator());

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

	public void setFromBox(List<String> fromBox) {
		this.fromBox = fromBox;
	}

	public List<String> getFromBox() {
		return fromBox;
	}

	public void setFromBoxUser(List<User> fromBoxUser) {
		this.fromBoxUser = fromBoxUser;
	}

	public List<User> getFromBoxUser() {
		return fromBoxUser;
	}

	public void setToBoxUser(List<User> toBoxUser) {
		this.toBoxUser = toBoxUser;
	}

	public List<User> getToBoxUser() {
		return toBoxUser;
	}

}

