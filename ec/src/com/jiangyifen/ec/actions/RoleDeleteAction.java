package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.Set;

import com.jiangyifen.ec.dao.Role;
import com.jiangyifen.ec.dao.User;

public class RoleDeleteAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2280098317784615239L;
	private String[] u;

	private ArrayList<String> errStrings = new ArrayList<String>();

	public String execute() throws Exception {
		if (u != null) {
			boolean err = false;
			for (String name : u) {
				Role r = roleManager.getRole(name);
				Set<User> users = r.getUsers();
				
				if (users != null && users.size() > 0) {
					err = true;
					errStrings.add("Role [" + name
							+ "] is still referenced by users: ");
					for (User u : users) {
						errStrings.add("User " + u.getUsername());
					}
				}
			}
			for (String s : errStrings) {
				logger.error(s);
			}

			if (err) {
				return "reference";
			} else {
				roleManager.deleteRoles(u);
				return SUCCESS;
			}
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
	
	public void setErrStrings(ArrayList<String> errStrings) {
		this.errStrings = errStrings;
	}

	public ArrayList<String> getErrStrings() {
		return errStrings;
	}

}
