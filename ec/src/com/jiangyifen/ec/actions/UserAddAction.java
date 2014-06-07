package com.jiangyifen.ec.actions;

import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.Role;
import com.jiangyifen.ec.dao.User;

public class UserAddAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1090897494239800465L;

	private String username;
	private String password;
	private String email;
	private String name;
	private String hid;

	private String rolename;
	private String departmentname;

	public String execute() throws Exception {

		User u = new User();
		u.setUsername(username);
		u.setPassword(password);
		u.setEmail(email);
		u.setName(name);
		u.setHid(hid);

		Role role = new Role();
		role.setRolename(rolename);
		u.setRole(role);

		Department department = new Department();
		department.setDepartmentname(departmentname);
		u.setDepartment(department);

		if (userManager.addUser(u)) {
			logger.info("UserAddAction " + SUCCESS);
			return SUCCESS;
		}

		logger.error("username duplicate");
		logger.info("UserAddAction " + INPUT);
		return INPUT;
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

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getRolename() {
		return rolename;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}

	public String getDepartmentname() {
		return departmentname;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}

	public String getHid() {
		return hid;
	}

}
