package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.List;

import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.Queue;
import com.jiangyifen.ec.dao.Role;
import com.jiangyifen.ec.dao.User;

public class UserGetOneAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4125694241654574652L;
	
	private String username;
	private List<User> users = new ArrayList<User>();
	
	private List<Role> rs = new ArrayList<Role>();
	private List<Department> departments = new ArrayList<Department>();
	private List<Queue> queues = new ArrayList<Queue>();
	
	public String execute() throws Exception {
		//获取已存在的角色
		setRs(roleManager.getRoles());
		setDepartments(departmentManager.getDepartments());
		setQueues(queueManager.getQueues());
		
//		username = new String(username.getBytes("ISO-8859-1"));

		users.add(userManager.getUser(username));
		return SUCCESS;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setRs(List<Role> rs) {
		this.rs = rs;
	}

	public List<Role> getRs() {
		return rs;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setQueues(List<Queue> queues) {
		this.queues = queues;
	}

	public List<Queue> getQueues() {
		return queues;
	}

}

