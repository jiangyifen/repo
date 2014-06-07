package com.jiangyifen.ec.actions.iface;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.Role;
import com.jiangyifen.ec.dao.User;
import com.jiangyifen.ec.fastagi.UserLogin;
import com.jiangyifen.ec.fastagi.UserLogout;
import com.jiangyifen.ec.util.Config;

public class UserLoginAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8337961371319652955L;

	private String u;
	private String p;
	private String username;
	private String password;
	private String name;
	private String hid = "0";

	private String exten;
	private String cmd;

	public String execute() throws Exception {

		String trd_username = Config.props.getProperty("3rd_username");
		String trd_password = Config.props.getProperty("3rd_password");

		if (u == null || p == null || username == null || password == null
				|| exten == null) {
			logger.warn("UserLogin: null u & p!");
			return INPUT;
		} else if (!u.equals(trd_username) || !p.equals(trd_password)) {
			logger.warn("UserLogin: invalid u & p!");
			return LOGIN;
		} else {
			
			if (cmd.toLowerCase().equals("login")) {
				
				
				User u = userManager.getUser(username);
				if (u == null) {
					u = new User();

					Role role = new Role();
					role.setRolename("agent");
					u.setRole(role);

					Department department = new Department();
					department.setDepartmentname("default");
					u.setDepartment(department);

					u.setUsername(username);
					u.setPassword(password);
					if (name != null)
						u.setName(name);
					u.setHid(hid);

					userManager.addUser(u);
				} else if (u.getRole().getRolename().equals("agent")) {
					u.setUsername(username);
					u.setPassword(password);
					if (name != null)
						u.setName(name);
					
					userManager.updateUser(u);

				} else {
					// do nothing
				}
				
				
				UserLogin.login(username, password, exten);
				logger.info("UserLogin: " + username + "/" + password + "/"
						+ exten + "/" + name + "/" + hid);
			} else if (cmd.toLowerCase().equals("logout")) {
				UserLogout.logout(username, password, exten);
				logger.info("UserLogout: " + username + "/" + password + "/"
						+ exten + "/" + name + "/" + hid);
			}


			return SUCCESS;
		}
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

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setExten(String exten) {
		this.exten = exten;
	}

	public String getExten() {
		return exten;
	}

	public String getHid() {
		return hid;
	}

	public void setHid(String hid) {
		this.hid = hid;
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

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getCmd() {
		return cmd;
	}

}
