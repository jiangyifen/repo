package com.jiangyifen.ec.dao;

import java.util.Date;

public class LinkLog { //NOTE changed 09-02

	private Long id;
	private Date date = new Date();
	private String channel = "";
	private String bridgedChannel = "";
	private String uniqueid = "";
	private String bridgedUniqueid = "";
	private String exten = "";
	private String callerid = "";
	private String name = "";
	private String username = "";
	private String action = "";// link,unlink

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

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getUniqueid() {
		return uniqueid;
	}

	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}

	public String getExten() {
		return exten;
	}

	public void setExten(String exten) {
		this.exten = exten;
	}

	public String getCallerid() {
		return callerid;
	}

	public void setCallerid(String callerid) {
		this.callerid = callerid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getBridgedChannel() {
		return bridgedChannel;
	}

	public void setBridgedChannel(String bridgedChannel) {
		this.bridgedChannel = bridgedChannel;
	}

	public String getBridgedUniqueid() {
		return bridgedUniqueid;
	}

	public void setBridgedUniqueid(String bridgedUniqueid) {
		this.bridgedUniqueid = bridgedUniqueid;
	}

	@Override
	public String toString() {
		return "LinkLog:action=" + action + ", date=" + date + ", channel="
				+ channel + ", uniqueid=" + uniqueid + ", exten=" + exten
				+ ", callerid=" + callerid + ", username=" + username
				+ ", name=" + name;
	}
}
