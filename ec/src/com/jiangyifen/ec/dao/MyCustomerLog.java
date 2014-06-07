package com.jiangyifen.ec.dao;

import java.util.Date;

public class MyCustomerLog {
	
	private Long id;
	private Long dialTaskId;
	private String dialTaskName ;
	private String customerPhoneNumber;
	private String username;
	private String department;
	private String hid ;
	private Date date = new Date();
	private String sync = "0";
	
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public void setDialTaskId(Long dialTaskId) {
		this.dialTaskId = dialTaskId;
	}
	public Long getDialTaskId() {
		return dialTaskId;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getDate() {
		return date;
	}
	public void setDialTaskName(String dialTaskName) {
		this.dialTaskName = dialTaskName;
	}
	public String getDialTaskName() {
		return dialTaskName;
	}
	public void setCustomerPhoneNumber(String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}
	public String getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}
	public void setHid(String hid) {
		this.hid = hid;
	}
	public String getHid() {
		return hid;
	}
	public void setSync(String sync) {
		this.sync = sync;
	}
	public String getSync() {
		return sync;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDepartment() {
		return department;
	}


}
