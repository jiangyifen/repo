package com.jiangyifen.ec.dao;

import java.util.Date;

public class DialTaskLog {
	
	private Long id;
	private Long dialTaskId;
	private String dialTaskName;
	private Long customerCount = 0l;
	private Long dialTaskItemFinishedCount = 0l;
	private Long dialTaskItemTotalCount = 0l;
	private Date date = new Date();
	
	private double customerRate = 0;
	private double finishRate = 0;
	
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
	public void setCustomerCount(Long customerCount) {
		this.customerCount = customerCount;
	}
	public Long getCustomerCount() {
		return customerCount;
	}
	public void setDialTaskItemFinishedCount(Long dialTaskItemFinishedCount) {
		this.dialTaskItemFinishedCount = dialTaskItemFinishedCount;
	}
	public Long getDialTaskItemFinishedCount() {
		return dialTaskItemFinishedCount;
	}
	public void setDialTaskItemTotalCount(Long dialTaskItemTotalCount) {
		this.dialTaskItemTotalCount = dialTaskItemTotalCount;
	}
	public Long getDialTaskItemTotalCount() {
		return dialTaskItemTotalCount;
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
	public void setCustomerRate(double customerRate) {
		this.customerRate = customerRate;
	}
	public double getCustomerRate() {
		return customerRate;
	}
	public void setFinishRate(double finishRate) {
		this.finishRate = finishRate;
	}
	public double getFinishRate() {
		return finishRate;
	}


}
