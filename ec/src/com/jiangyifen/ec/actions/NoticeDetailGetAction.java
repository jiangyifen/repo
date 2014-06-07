package com.jiangyifen.ec.actions;

import com.jiangyifen.ec.dao.Notice;

public class NoticeDetailGetAction extends BaseAction {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1568170766994294841L;

	private String id;
	private Notice notice;
	
	public String execute() throws Exception {
		setNotice(noticeManager.get(Long.valueOf(id)));
		return SUCCESS;

	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setNotice(Notice notice) {
		this.notice = notice;
	}
	public Notice getNotice() {
		return notice;
	}


}
