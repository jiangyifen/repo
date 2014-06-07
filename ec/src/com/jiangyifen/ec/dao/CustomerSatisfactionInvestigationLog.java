package com.jiangyifen.ec.dao;

import java.util.Date;

public class CustomerSatisfactionInvestigationLog {

	private Long id;
	private Date date = new Date();
	private String calleridNum;
	private String exten;
	private String username;
	private String uniqueid;
	private String p1;
	private String p2;
	private String sync="0";
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getExten() {
		return exten;
	}
	public void setExten(String exten) {
		this.exten = exten;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getP1() {
		return p1;
	}
	public void setP1(String p1) {
		this.p1 = p1;
	}
	public String getP2() {
		return p2;
	}
	public void setP2(String p2) {
		this.p2 = p2;
	}
	public void setCalleridNum(String calleridNum) {
		this.calleridNum = calleridNum;
	}
	public String getCalleridNum() {
		return calleridNum;
	}
	public void setSync(String sync) {
		this.sync = sync;
	}
	public String getSync() {
		return sync;
	}
	public String getUniqueid() {
		return uniqueid;
	}
	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}
}
