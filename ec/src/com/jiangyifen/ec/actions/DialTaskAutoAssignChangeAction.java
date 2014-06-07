package com.jiangyifen.ec.actions;

import com.jiangyifen.ec.dao.DialTask;

public class DialTaskAutoAssignChangeAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3242571728296919205L;
	private String id;
	private boolean currentautoassign;

	public String execute() throws Exception {

		DialTask dt = dialTaskManager.get(new Long(id));
		
		if (currentautoassign) {
			dt.setAutoAssign(false);
		} else {
			dt.setAutoAssign(true);
		}
		dialTaskManager.update(dt);
		return SUCCESS;

	}


	public boolean isCurrentautoassign() {
		return currentautoassign;
	}


	public void setCurrentautoassign(boolean currentautoassign) {
		this.currentautoassign = currentautoassign;
	}


	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
