package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.DepartmentDao;

public class DepartmentManagerImpl implements DepartmentManager {
	private DepartmentDao departmentDao;

	public void setDepartmentDao(DepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
	}

	public DepartmentDao getDepartmentDao() {
		return departmentDao;
	}

	public boolean addDepartment(Department department) throws Exception {

		try {
			return departmentDao.save(department);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("新增department时出现异常");
		}
	}
	
	public boolean addDepartment(String departmentname) throws Exception {
			Department d = new Department();
			d.setDepartmentname(departmentname);
			return addDepartment(d);
	}

	public void deleteDepartments(String[] departmentnames) throws Exception {
		for (String departmentname : departmentnames) {
			departmentDao.delete(departmentname);
		}
	}

	public void deleteDepartment(String departmentname) throws Exception {
		departmentDao.delete(departmentname);
	}
	
	public void deleteDepartment(Department department) throws Exception {
		departmentDao.delete(department);
	}

	public void updateDepartment(Department department) throws Exception {
		departmentDao.update(department);
	}

	public int getDepartmentCount() {
		return (int) departmentDao.getDepartmentCount();
	}

	public Department getDepartment(String departmentname) {
		return departmentDao.get(departmentname);
	}

	public List<Department> getDepartments() throws Exception {
		return departmentDao.findAll();
	}

	public List<Department> getDepartmentsByPage(int pageSize, int pageIndex)
			throws Exception {
		return departmentDao.findByPage(pageSize, pageIndex);
	}
}
