package com.jiangyifen.ec.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class RoleDaoHibernate extends HibernateDaoSupport implements RoleDao {

	public Role get(String rolename) {
		return (Role) getHibernateTemplate().get(Role.class, rolename);
	}

	public boolean save(Role role) {
		Role u = get(role.getRolename());
		if (u == null) {
			getHibernateTemplate().merge(role);
			return true;
		} else {
			return false;
		}
	}

	public void update(Role role) {
		getHibernateTemplate().merge(role);
	}

	public void delete(String rolename) {
		Role role = (Role) getHibernateTemplate().get(Role.class, rolename);
		
		Set<Department> departments = role.getDepartments();
		for (Department d : departments) {
			d.getRoles().remove(role);
		}
		
		Set<RoleAction> ras = role.getRoleactions();
		for (RoleAction ra : ras) {
			ra.getRoles().remove(role);
		}

		delete(role);
	}

	public void delete(Role role) {
		Set<Department> Departments = role.getDepartments();
		for (Department d : Departments) {
			d.getRoles().remove(role);
		}
		getHibernateTemplate().delete(role);
	}

	@SuppressWarnings("unchecked")
	public long getRoleCount() {
		String sql = "select count(*) from Role";
		Query q = getSession().createQuery(sql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Role> findAll() {
		List<Role> list = (List<Role>) getHibernateTemplate().find(
				"from Role order by rolename");
		return list;
	}

	public List<Role> findByPage(int PAGESIZE, int pageIndex) {
		if (pageIndex > 0) {
			int a = PAGESIZE * (pageIndex - 1);
			int b = PAGESIZE * pageIndex;
			int count = (int) getRoleCount();
			if (b > count)
				b = count;

			return findAll().subList(a, b);
		} else {
			return findAll().subList(0, PAGESIZE);
		}

	}

	@SuppressWarnings("rawtypes")
	public Role finaRoleByName(String rolename) {
		List ul = getHibernateTemplate().find(
				"from Role au where au.rolename = ?", rolename);
		if (ul != null && ul.size() == 1) {
			return (Role) ul.get(0);
		} else if (ul != null && ul.size() > 1) {
			System.out
					.println("more than 1 Role have same username and password!!");
		}
		return null;
	}

	public void test() {

	}

}
