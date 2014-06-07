package com.jiangyifen.ec.actions;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.dao.Notice;

public class NoticeUpdateAction extends BaseAction {



	/**
	 * 
	 */
	private static final long serialVersionUID = -1124074732775413022L;

	private final Log logger = LogFactory.getLog(getClass());

	private final int PAGESIZE = 25;

	private Long id;
	private String title;
	private String content;

	public String execute() throws Exception {
		Notice notice = noticeManager.get(id);
		notice.setTitle(title);
		notice.setContent(content);
		noticeManager.update(notice);
		return SUCCESS;

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


	public Log getLogger() {
		return logger;
	}

	public int getPAGESIZE() {
		return PAGESIZE;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
