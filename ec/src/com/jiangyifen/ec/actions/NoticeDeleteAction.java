package com.jiangyifen.ec.actions;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class NoticeDeleteAction extends BaseAction {




	/**
	 * 
	 */
	private static final long serialVersionUID = 4122222466661753955L;

	private final Log logger = LogFactory.getLog(getClass());

	private final int PAGESIZE = 25;

	private Long id;

	public String execute() throws Exception {

		noticeManager.delete(id);
		return SUCCESS;

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
