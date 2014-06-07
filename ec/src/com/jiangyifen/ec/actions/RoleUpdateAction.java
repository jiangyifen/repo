package com.jiangyifen.ec.actions;

import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.Role;
import com.jiangyifen.ec.dao.RoleAction;

public class RoleUpdateAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1070112640127764388L;

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
		
		roleManager.updateRole(r);
		return SUCCESS;
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

	public void setDpmts(String[] dpmts) {
		this.dpmts = dpmts;
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
