package com.jiangyifen.ec.dao;

import java.io.Serializable;
import java.util.Date;

public class DialTaskItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -447487221461851477L;
	public static final String STATUS_READY = "READY";
	public static final String STATUS_MANUAL = "MANUAL";
	public static final String STATUS_FINISH = "FINISH";
	public static final String STATUS_FAILED = "FAILED";
	
	public static final String RESULT_GOOD = "GOOD";
	public static final String RESULT_NORMAL = "NORMAL";
	public static final String RESULT_BAD = "BAD";

	private Long id;
	private String phoneNumber;
	private Long taskid;
	private String status;
	private Date calldate;
	
	private String phoneNumber2;
	private String name;
	private String sex;
	private String province;
	private String city;
	private String address;
	private String company;

	private String info;   //导入的数据，对该条数据的描述
	private String result; //用户填写，非常有意向，有一点意向，没意向
	private String remark; //用户填写，文字备注

	private boolean hasSmReply = false;
	private Date timestamp;

	
	private String owner; //This field is FK. In most case, it should be a user id.
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setTaskid(Long taskid) {
		this.taskid = taskid;
	}
	public Long getTaskid() {
		return taskid;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
	public void setCalldate(Date calldate) {
		this.calldate = calldate;
	}
	public Date getCalldate() {
		return calldate;
	}
	public void setPhoneNumber2(String phoneNumber2) {
		this.phoneNumber2 = phoneNumber2;
	}
	public String getPhoneNumber2() {
		return phoneNumber2;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getResult() {
		return result;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getInfo() {
		return info;
	}
	public void setHasSmReply(boolean hasSmReply) {
		this.hasSmReply = hasSmReply;
	}
	public boolean isHasSmReply() {
		return hasSmReply;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompany() {
		return company;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public Date getTimestamp() {
		return timestamp;
	}

}
