package com.jiangyifen.ec.dao;

import java.util.List;

public interface DepartmentDao {
	Department get(String departmentname);
	boolean save(Department department);
	void update(Department department);
	void delete(String departmentname);
	void delete(Department department);
	
	long getDepartmentCount();
	
	List<Department> findAll();
	List<Department> findByPage(int pageSize,int pageIndex);
	Department finaDepartmentByName(String departmentname);

}
