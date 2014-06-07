package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.List;

import com.jiangyifen.ec.dao.Department;

public class DepartmentsGetAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6222040601140367978L;
	private List<Department> departments;
	private int pageIndex = 1;
	private final int PAGESIZE = 10;
	private int maxPageIndex = 0;
	private List<Integer> pages = new ArrayList<Integer>();
	private List<Department> pdpmts;

	public String execute() throws Exception {
		if ((departmentManager.getDepartmentCount() % PAGESIZE) == 0) {
			maxPageIndex = (departmentManager.getDepartmentCount() / PAGESIZE);
		} else {
			maxPageIndex = (departmentManager.getDepartmentCount() / PAGESIZE) + 1;
		}

		for (int i = 0; i < maxPageIndex; i++) {
			pages.add(i + 1);
		}
		if (pageIndex > maxPageIndex)
			pageIndex = maxPageIndex;
		if (pageIndex < 1)
			pageIndex = 1;

		List<Department> list = departmentManager.getDepartmentsByPage(PAGESIZE, pageIndex);
		setDepartments(list);
		
		pdpmts = departmentManager.getDepartments();
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

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setPdpmts(List<Department> pdpmts) {
		this.pdpmts = pdpmts;
	}

	public List<Department> getPdpmts() {
		return pdpmts;
	}

}
