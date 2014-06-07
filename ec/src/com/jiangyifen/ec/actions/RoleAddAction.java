package com.jiangyifen.ec.actions;

import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.Role;
import com.jiangyifen.ec.dao.RoleAction;

public class RoleAddAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6518369380324628762L;
	private String rolename;
	private String description;

	private String[] dpmts;
	private String[] ras;

	public String execute() throws Exception {

		Role r = new Role();
		r.setRolename(rolename);
		r.setDescription(description);

		if (dpmts != null) {
			for (String departmentname : dpmts) {
				Department d = departmentManager.getDepartment(departmentname);
				r.getDepartments().add(d);
			}
		} else {
			logger.warn("departments is null");
		}
		
		if (ras != null) {
			for (String roleactionname : ras) {
				RoleAction ra = roleActionManager.getRoleAction(roleactionname);
				r.getRoleactions().add(ra);
			}
		} else {
			logger.warn("roleAction is null");
		}

		if (roleManager.addRole(r)) {
			logger.info("Role " + r.getRolename() + " with : ");
			for (Department d : r.getDepartments()) {
				logger.info(d.getDepartmentname());
			}
			for (RoleAction ra : r.getRoleactions()) {
				logger.info(ra.getRoleactionname());
			}
			logger.info("added successful!");
			return SUCCESS;
		} else {
			logger.error("role name duplicate");
			//return INPUT;
			return INPUT;
		}
	}

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

	public void setDpmts(String[] departments) {
		this.dpmts = departments;
	}

	public String[] getDpmts() {
		return dpmts;
	}

	public void setRas(String[] ras) {
		this.ras = ras;
	}

	public String[] getRas() {
		return ras;
	}

}
