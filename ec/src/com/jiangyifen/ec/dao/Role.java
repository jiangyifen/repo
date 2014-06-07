package com.jiangyifen.ec.dao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Role implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7116947397996096892L;
	private String rolename;
	private String description;
	private Set<User> users = new HashSet<User>();
	private Set<Department> departments = new HashSet<Department>();
	private Set<RoleAction> roleactions = new HashSet<RoleAction>();

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getRolename() {
		return rolename;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}

	public Set<Department> getDepartments() {
		return departments;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setRoleactions(Set<RoleAction> roleactions) {
		this.roleactions = roleactions;
	}

	public Set<RoleAction> getRoleactions() {
		return roleactions;
	}

}
