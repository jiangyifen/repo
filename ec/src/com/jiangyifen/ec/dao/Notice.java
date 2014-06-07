package com.jiangyifen.ec.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Notice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1358458422265778683L;
	
	private long id;
	private String title;
	private String content;
	private Date date = new Date();
	private String description;
	
	private Set<NoticeItem> noticeItems = new HashSet<NoticeItem>();
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setNoticeItems(Set<NoticeItem> noticeItems) {
		this.noticeItems = noticeItems;
	}
	public Set<NoticeItem> getNoticeItems() {
		return noticeItems;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}

}
