package com.jiangyifen.ec.actions;

import com.jiangyifen.ec.dao.Department;


public class DepartmentUpdateAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7899990161508815364L;
	private String departmentname;
	private String description;
	private String pdname;

	public String execute() throws Exception {
		Department u = new Department();
		u.setDepartmentname(departmentname);
		u.setDescription(description);

		Department parent = departmentManager.getDepartment(pdname);
		u.setParent(parent);
		
		departmentManager.updateDepartment(u);
		return SUCCESS;
	}
	
	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}

	public String getDepartmentname() {
		return departmentname;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setPdname(String pdname) {
		this.pdname = pdname;
	}

	public String getPdname() {
		return pdname;
	}

}
