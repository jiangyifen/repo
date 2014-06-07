package com.jiangyifen.ec.actions;

import java.util.Map;
import com.opensymphony.xwork2.ActionContext;

public class LogoutAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4152162722599800560L;

	public String execute() throws Exception {
		Map<String, Object> session = ActionContext.getContext().getSession();

		session.put("login", "logout");
		return LOGIN;
	}
}
