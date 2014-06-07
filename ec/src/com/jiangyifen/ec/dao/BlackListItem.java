package com.jiangyifen.ec.dao;

import java.io.Serializable;

public class BlackListItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5561353775011292147L;
	
	public static final String TYPE_INCOMING = "incoming";
	public static final String TYPE_OUTGOING = "outgoing";
	
	// 数据库字段
	private Long id;
	private String phoneNumber;
	private String type;
	private String remark;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return remark;
	}
	

}
