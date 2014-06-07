package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jiangyifen.ec.dao.Department;

public class DepartmentGetOneAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5300259224923121841L;
	private String departmentname;
	private List<Department> departments = new ArrayList<Department>();
	
	private List<Department> pdpmts = new ArrayList<Department>();

	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}

	public String getDepartmentname() {
		return departmentname;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public String execute() throws Exception {
		Department d = departmentManager.getDepartment(departmentname);
		List<Department> allDpmts = departmentManager.getDepartments();
		
		//获取当前部门的所有子部门和自己
		//在修改部门时，不允许把上级部门设置成自己或者自己的子部门
		Set<Department> allChildren = new HashSet<Department>();
		Department.getAllChildren(d, allChildren);	
		allChildren.add(d);
		
		ArrayList<String> allChildrenDpmtName = new ArrayList<String>();
		for(Department dpmt: allChildren){
			allChildrenDpmtName.add(dpmt.getDepartmentname());
		}

		for(Department dpmt: allDpmts){
			String name = dpmt.getDepartmentname();
			if(!allChildrenDpmtName.contains(name)){
				pdpmts.add(dpmt);
			}
		}

		departments.add(d);
		return SUCCESS;
	}

	public void setPdpmts(List<Department> pdpmts) {
		this.pdpmts = pdpmts;
	}

	public List<Department> getPdpmts() {
		return pdpmts;
	}

}
