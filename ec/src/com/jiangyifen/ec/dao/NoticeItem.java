package com.jiangyifen.ec.dao;

import java.io.Serializable;

public class NoticeItem implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2364737790335530548L;
	
	private long id;
	private String username;
	private boolean read = false;
	
	private Notice notice;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	public void setNotice(Notice notice) {
		this.notice = notice;
	}
	public Notice getNotice() {
		return notice;
	}
	

}
