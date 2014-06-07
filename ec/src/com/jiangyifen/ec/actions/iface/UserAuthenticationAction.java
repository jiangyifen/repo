package com.jiangyifen.ec.actions.iface;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.dao.User;
import com.jiangyifen.ec.util.Config;

public class UserAuthenticationAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7305260726773914074L;
	
	private String u;
	private String p;
	private String username;
	private String password;
	
	private String result;


	public String execute() throws Exception {

		String trd_username = Config.props.getProperty("3rd_username");
		String trd_password = Config.props.getProperty("3rd_password");

		if (u == null || p == null || username == null || password == null) {
			result = "failed[null u, p, username, password]";
			logger.warn(result);
		} else if (!u.equals(trd_username) || !p.equals(trd_password)) {
			result = "failed[invalid u & p!]";
			logger.warn(result);
		} else {
			User u = userManager.getUser(username);
			if (u == null) {
				result = "failed[无此用户]";
				logger.warn(result);
			} else if(password.equals(u.getPassword())){
				result = "success";
				logger.info(result);
			} else{
				result = "failed[密码错误]";
				logger.warn(result);
			}
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

	public void setU(String u) {
		this.u = u;
	}

	public String getU() {
		return u;
	}

	public void setP(String p) {
		this.p = p;
	}

	public String getP() {
		return p;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

}
