package com.jiangyifen.ec.actions;


public class DialTaskItemDeleteAction extends BaseAction {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3426636152807710971L;
	private String[] u;
	
	private String id;

	public String execute() throws Exception {
		if (u != null) {
			for (String id : u){
				dialTaskItemManager.delete(new Long(id));
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

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
