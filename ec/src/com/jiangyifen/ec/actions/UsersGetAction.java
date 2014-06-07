package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.List;

import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.Role;
import com.jiangyifen.ec.dao.User;

public class UsersGetAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4421866192055179720L;

	private List<User> users;
	private int pageIndex = 1;
	private final int PAGESIZE = 10;
	private int maxPageIndex = 0;
	private List<Integer> pages = new ArrayList<Integer>();

	private List<Role> roles = new ArrayList<Role>();
	private List<Department> Departments = new ArrayList<Department>();

	private String condition;

	public String execute() throws Exception {

		// 获取已存在的角色
		setRoles(roleManager.getRoles());
		setDepartments(departmentManager.getDepartments());

		// 计算页数
		int userCount;
		if (condition == null || condition.equals("")) {
			userCount = userManager.getUserCount();
		} else {
			userCount = userManager.getUserCountByCondition(condition);
		}
		
		
		if ((userCount % PAGESIZE) == 0) {
			maxPageIndex = (userCount / PAGESIZE);
		} else {
			maxPageIndex = (userCount / PAGESIZE) + 1;
		}

		for (int i = 0; i < maxPageIndex; i++) {
			pages.add(i + 1);
		}

		// 避免传入的页数超出范围
		if (pageIndex > maxPageIndex)
			pageIndex = maxPageIndex;
		if (pageIndex < 1)
			pageIndex = 1;
		
		// 获取指定页数的用户
		List<User> list;
		if (condition == null || condition.equals("")) {
			list = userManager.getUsersByPage(PAGESIZE, pageIndex);
		} else {
			list = userManager.getUsersByCondition(PAGESIZE, pageIndex,
					condition);
		}
		setUsers(list);

		logger.info("UsersGetAction  " + SUCCESS);
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

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setDepartments(List<Department> departments) {
		Departments = departments;
	}

	public List<Department> getDepartments() {
		return Departments;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getCondition() {
		return condition;
	}

}
