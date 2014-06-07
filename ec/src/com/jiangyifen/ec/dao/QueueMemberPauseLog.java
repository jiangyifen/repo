package com.jiangyifen.ec.dao;

import java.io.Serializable;
import java.util.Date;

public class QueueMemberPauseLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1111151012410458704L;
	
	private Long id;
	private String username;
	private String queue;
	private String memberName;
	private Date pauseDate;
	private Date unpauseDate;
	
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
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public Date getPauseDate() {
		return pauseDate;
	}
	public void setPauseDate(Date pauseDate) {
		this.pauseDate = pauseDate;
	}
	public Date getUnpauseDate() {
		return unpauseDate;
	}
	public void setUnpauseDate(Date unpauseDate) {
		this.unpauseDate = unpauseDate;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}

}
