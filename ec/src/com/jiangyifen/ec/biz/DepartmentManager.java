package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.Department;

public interface DepartmentManager {
	boolean addDepartment(String departmentname) throws Exception;
	
	boolean addDepartment(Department department) throws Exception;

	void deleteDepartments(String[] departmentnames) throws Exception;

	void deleteDepartment(String departmentname) throws Exception;
	
	void deleteDepartment(Department department) throws Exception;

	void updateDepartment(Department department) throws Exception;

	int getDepartmentCount() throws Exception;

	Department getDepartment(String departmentname) throws Exception;

	List<Department> getDepartments() throws Exception;

	List<Department> getDepartmentsByPage(int pageSize, int pageIndex)
			throws Exception;

}
