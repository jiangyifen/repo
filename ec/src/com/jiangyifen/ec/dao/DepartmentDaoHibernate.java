package com.jiangyifen.ec.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class DepartmentDaoHibernate extends HibernateDaoSupport implements DepartmentDao {

	public Department get(String departmentname) {
		return (Department) getHibernateTemplate().get(Department.class, departmentname);
	}

	public boolean save(Department department) {
		Department u = get(department.getDepartmentname());
		if (u == null) {
			getHibernateTemplate().save(department);
			return true;
		} else {
			return false;
		}
	}

	public void update(Department department) {
		getHibernateTemplate().saveOrUpdate(department);
	}

	public void delete(String departmentname) {
		getHibernateTemplate().delete(
				getHibernateTemplate().get(Department.class, departmentname));
	}
	
	public void delete(Department department) {
		getHibernateTemplate().delete(department);
	}

	@SuppressWarnings("unchecked")
	public long getDepartmentCount() {
		String sql = "select count(*) from Department";
		Query q = getSession().createQuery(sql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Department> findAll() {
		List<Department> list = (List<Department>) getHibernateTemplate().find("from Department order by departmentname");
		return list;
	}


	public List<Department> findByPage(int PAGESIZE, int pageIndex) {
		if (pageIndex > 0) {
			int a = PAGESIZE * (pageIndex - 1);
			int b = PAGESIZE * pageIndex;
			int count = (int) getDepartmentCount();
			if (b > count)
				b = count;

			return findAll().subList(a, b);
		} else {
			return findAll().subList(0, PAGESIZE);
		}

	}
	
	@SuppressWarnings("rawtypes")
	public Department finaDepartmentByName(String departmentname) {
		List ul = getHibernateTemplate().find(
				"from Department au where au.departmentname = ?", departmentname);
		if (ul != null && ul.size() == 1) {
			return (Department) ul.get(0);
		} else if (ul != null && ul.size() > 1) {
			System.out
					.println("more than 1 Department have same department name!");
		}
		return null;
	}
	
}
