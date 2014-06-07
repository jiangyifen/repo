package com.jiangyifen.ec.dao;

import java.util.Date;

public class IVRLog {
	
	private Long id;
	private Date date = new Date();
	private String uniqueid = "";
	private String context = "";
	private String exten = "";
	private String priority = "";
	private String calleridnum = "";
	private String channel = "";
	private String node = "";
	
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getExten() {
		return exten;
	}
	public void setExten(String exten) {
		this.exten = exten;
	}
	
	
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
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getUniqueid() {
		return uniqueid;
	}
	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}
	public String getCalleridnum() {
		return calleridnum;
	}
	public void setCalleridnum(String calleridnum) {
		this.calleridnum = calleridnum;
	}
	

}
