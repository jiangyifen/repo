package com.jiangyifen.ec.dao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class RoleAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6882909166238670511L;
	
	private String roleactionname;
	private String description;
	
	private Set<Role> roles = new HashSet<Role>();

	public void setRoleactionname(String roleactionname) {
		this.roleactionname = roleactionname;
	}

	public String getRoleactionname() {
		return roleactionname;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Role> getRoles() {
		return roles;
	}

}
