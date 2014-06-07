package com.jiangyifen.ec.actions;

public class DialTaskDeleteAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -463224741049433223L;
	private String[] u;

	public String execute() throws Exception {
		if (u != null) {
			for (String id : u){
				dialTaskManager.delete(new Long(id));
				dialTaskItemManager.deleteAllByTaskId(new Long(id));
			}
			return SUCCESS;
		} else {
			return INPUT;
		}
	}

	public void setU(String[] u) {
		this.u = u;
	}

	public String[] getU() {
		return u;
	}

}
