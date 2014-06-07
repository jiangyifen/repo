package com.jiangyifen.ec.dao;

import java.util.Date;

public class QueueCallerAbandonEventLog {

	private Long id;
	private String queue;
	private String channel;
	private Integer count;
	private Date dateReceived;
	private Integer position;
	private Integer originalposition;
	private Integer holdtime;
	private String uniqueid;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getQueue() {
		return queue;
	}
	public void setQueue(String queue) {
		this.queue = queue;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Date getDateReceived() {
		return dateReceived;
	}
	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public Integer getOriginalposition() {
		return originalposition;
	}
	public void setOriginalposition(Integer originalposition) {
		this.originalposition = originalposition;
	}
	public Integer getHoldtime() {
		return holdtime;
	}
	public void setHoldtime(Integer holdtime) {
		this.holdtime = holdtime;
	}
	public String getUniqueid() {
		return uniqueid;
	}
	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}
	

}
