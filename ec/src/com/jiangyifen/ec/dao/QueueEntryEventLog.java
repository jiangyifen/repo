package com.jiangyifen.ec.dao;

import java.util.Date;

public class QueueEntryEventLog {

	private Long id;
	private String queue;
	private Date dateReceived;
	private String callerid;
	private String calleridname;
	private String channel;
	private Integer position;
	private Long wait;
	
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public void setQueue(String queue) {
		this.queue = queue;
	}
	public String getQueue() {
		return queue;
	}
	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}
	public Date getDateReceived() {
		return dateReceived;
	}
	public void setCallerid(String callerid) {
		this.callerid = callerid;
	}
	public String getCallerid() {
		return callerid;
	}
	public void setCalleridname(String calleridname) {
		this.calleridname = calleridname;
	}
	public String getCalleridname() {
		return calleridname;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getChannel() {
		return channel;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public Integer getPosition() {
		return position;
	}
	public void setWait(Long wait) {
		this.wait = wait;
	}
	public Long getWait() {
		return wait;
	}

}
