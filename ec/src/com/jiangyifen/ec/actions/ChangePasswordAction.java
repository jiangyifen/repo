package com.jiangyifen.ec.actions;

import com.jiangyifen.ec.dao.User;
import com.opensymphony.xwork2.ActionContext;

public class ChangePasswordAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2145873290487353212L;

	private User user;

	public String execute() throws Exception {

		String loginedUsername = (String) ActionContext.getContext()
				.getSession().get("username");

		User user = null;
		if (loginedUsername != null) {
			user = userManager.getUser(loginedUsername);
			if (user != null) {
				setUser(user);
				return SUCCESS;
			}else {
				logger.info("No user login. ");
				return LOGIN;
			}
		} else {
			logger.info("No user login. ");
			return LOGIN;
		}
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

}
