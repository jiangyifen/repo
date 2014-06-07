package com.jiangyifen.ec.dao;

import java.io.Serializable;
import java.util.Date;

public class SmTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8492887549619999878L;
	
	public static final String RECEIVE = "RECEIVE";
	public static final String READY = "READY";
	public static final String WAITING = "WAITING";
	public static final String DELIVRD = "DELIVRD";

	private Long id;
	private String content;
	private String mobile;
	private String accountId;
	private String srcTermId;
	private String status;            //ready waiting, receive, Deliver返回的状态
	private int penalty = 5;
	private Date timestamp;
	private String msgid;                 //此字段由程序回写
	private Date taskSubmitTime;          //此字段由程序回写
	private Date taskReceiveReportTime;   //此字段由程序回写
	private String senderId = "";
	private String department = "";
	private String batchNumber = "";
	private String userfield = "";
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return mobile;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setSrcTermId(String srcTermId) {
		this.srcTermId = srcTermId;
	}

	public String getSrcTermId() {
		return srcTermId;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setTimestamp(java.util.Date timestamp) {
		this.timestamp = timestamp;
	}

	public java.util.Date getTimestamp() {
		return timestamp;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public String getMsgid() {
		return msgid;
	}

	public void setTaskSubmitTime(Date taskSubmitTime) {
		this.taskSubmitTime = taskSubmitTime;
	}

	public Date getTaskSubmitTime() {
		return taskSubmitTime;
	}

	public void setTaskReceiveReportTime(Date taskReceiveReportTime) {
		this.taskReceiveReportTime = taskReceiveReportTime;
	}

	public Date getTaskReceiveReportTime() {
		return taskReceiveReportTime;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setUserfield(String userfield) {
		this.userfield = userfield;
	}

	public String getUserfield() {
		return userfield;
	}

	public void setPenalty(int penalty) {
		this.penalty = penalty;
	}

	public int getPenalty() {
		return penalty;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDepartment() {
		return department;
	}



}
