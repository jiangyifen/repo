package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.List;

import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.Role;
import com.jiangyifen.ec.dao.RoleAction;

public class RolesGetAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4421866192055179720L;

	private List<Role> roles;
	private int pageIndex = 1;
	private final int PAGESIZE = 10;
	private int maxPageIndex = 0;
	private List<Integer> pages = new ArrayList<Integer>();

	private List<Department> departments = new ArrayList<Department>();
	private List<RoleAction> roleactions = new ArrayList<RoleAction>();
	
	public String execute() throws Exception {
		//获取已存在的部门
		setDepartments(departmentManager.getDepartments());
		setRoleactions(roleActionManager.getRoleActions());
		
		//获取角色
		if ((roleManager.getRoleCount() % PAGESIZE) == 0) {
			maxPageIndex = (roleManager.getRoleCount() / PAGESIZE);
		} else {
			maxPageIndex = (roleManager.getRoleCount() / PAGESIZE) + 1;
		}

		for (int i = 0; i < maxPageIndex; i++) {
			pages.add(i + 1);
		}
		if (pageIndex > maxPageIndex)
			pageIndex = maxPageIndex;
		if (pageIndex < 1)
			pageIndex = 1;

		List<Role> list = roleManager.getRolesByPage(PAGESIZE, pageIndex);
		setRoles(list);
		
		return SUCCESS;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setMaxPageIndex(int maxPageIndex) {
		this.maxPageIndex = maxPageIndex;
	}

	public int getMaxPageIndex() {
		return maxPageIndex;
	}

	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}

	public List<Integer> getPages() {
		return pages;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setRoleactions(List<RoleAction> roleactions) {
		this.roleactions = roleactions;
	}

	public List<RoleAction> getRoleactions() {
		return roleactions;
	}


}
