package com.jiangyifen.ec.dao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6092755925375224642L;

	private String username;
	private String password;
	private String email;
	private String name;
	private String hid;
	
	private Role role;
	private Department department;
	
	private Set<Queue> queues = new HashSet<Queue>();
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Role getRole() {
		return role;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}

	public String getHid() {
		return hid;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Department getDepartment() {
		return department;
	}

	public void setQueues(Set<Queue> queues) {
		this.queues = queues;
	}

	public Set<Queue> getQueues() {
		return queues;
	}

}
