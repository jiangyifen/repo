package com.jiangyifen.ec.actions;

import com.jiangyifen.ec.dao.User;

public class ChangePasswordUpdateAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1070112640127764388L;

	private String username;
	private String password;
	private String email;

	public String execute() throws Exception {

		User u = userManager.getUser(username);
		if (u != null) {
			u.setPassword(password);
			u.setEmail(email);

			logger.info("ChangePasswordUpdateAction: update");
			userManager.updateUser(u);
			logger.info("ChangePasswordUpdateAction: end");
		}
		return SUCCESS;

	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

}
