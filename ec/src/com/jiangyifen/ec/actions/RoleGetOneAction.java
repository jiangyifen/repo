package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.Role;
import com.jiangyifen.ec.dao.RoleAction;

public class RoleGetOneAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8337702750703247995L;
	private String rolename;
	private List<Role> roles = new ArrayList<Role>();

	private List<Department> allDepartments = new ArrayList<Department>();
	private List<String> dpmts = new ArrayList<String>();

	private List<RoleAction> allRoleActions = new ArrayList<RoleAction>();
	private List<String> ras = new ArrayList<String>();

	public String execute() throws Exception {
		setAllDepartments(departmentManager.getDepartments());
		setAllRoleActions(roleActionManager.getRoleActions());
		Collections.sort(ras);

//		rolename = new String(rolename.getBytes("ISO-8859-1"));

		logger.info("get role " + rolename);
		Role r = roleManager.getRole(rolename);
		if (r != null) {
			for (Department d : r.getDepartments()) {
				dpmts.add(d.getDepartmentname());
			}
			for (RoleAction ra : r.getRoleactions()) {
				ras.add(ra.getRoleactionname());
			}

			roles.add(roleManager.getRole(rolename));
		}
		return SUCCESS;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setAllDepartments(List<Department> allDepartments) {
		this.allDepartments = allDepartments;
	}

	public List<Department> getAllDepartments() {
		return allDepartments;
	}

	public void setDpmts(List<String> dpmts) {
		this.dpmts = dpmts;
	}

	public List<String> getDpmts() {
		return dpmts;
	}

	public void setAllRoleActions(List<RoleAction> allRoleActions) {
		this.allRoleActions = allRoleActions;
	}

	public List<RoleAction> getAllRoleActions() {
		return allRoleActions;
	}

	public void setRas(List<String> ras) {
		this.ras = ras;
	}

	public List<String> getRas() {
		return ras;
	}

}
