package com.jiangyifen.ec.actions;

import com.jiangyifen.ec.dao.Department;

public class DepartmentAddAction extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6439517471120742630L;
	private String departmentname;
	private String description;
	private String pdname;
	

	public String execute() throws Exception {
		Department d = new Department();
		d.setDepartmentname(departmentname);
		d.setDescription(description);
		
		Department parent = departmentManager.getDepartment(pdname);
		d.setParent(parent);

		if (departmentManager.addDepartment(d)) {
			return SUCCESS;
		} else {
			logger.error("department name duplicate");
			return INPUT;
		}
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
