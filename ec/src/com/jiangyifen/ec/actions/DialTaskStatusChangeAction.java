package com.jiangyifen.ec.actions;

import java.util.Date;

import com.jiangyifen.ec.dao.DialTask;

public class DialTaskStatusChangeAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6461944034433702941L;
	private String id;
	private String currentstatus;

	public String execute() throws Exception {

		DialTask dt = dialTaskManager.get(new Long(id));
		
		if (currentstatus.equals(DialTask.STATUS_RUNNING)) {
			dt.setStatus(DialTask.STATUS_STOP);
		} else if (currentstatus.equals(DialTask.STATUS_STOP)) {
			dt.setStatus(DialTask.STATUS_RUNNING);
			if(dt.getStartDate()==null){
				dt.setStartDate(new Date());
			}
		}
		dialTaskManager.update(dt);
		return SUCCESS;

	}

	public void setCurrentstatus(String currentstatus) {
		this.currentstatus = currentstatus;
	}

	public String getCurrentstatus() {
		return currentstatus;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
