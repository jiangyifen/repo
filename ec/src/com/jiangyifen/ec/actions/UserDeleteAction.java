package com.jiangyifen.ec.actions;

public class UserDeleteAction extends BaseAction {

	private static final long serialVersionUID = 570045581700715712L;

	private String[] u;

	public String execute() throws Exception {
		if (u != null) {
			userManager.deleteUsers(u);
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
