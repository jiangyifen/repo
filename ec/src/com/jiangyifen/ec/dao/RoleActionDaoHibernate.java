package com.jiangyifen.ec.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class RoleActionDaoHibernate extends HibernateDaoSupport implements RoleActionDao {

	public RoleAction get(String roleactionname) {
		return (RoleAction) getHibernateTemplate().get(RoleAction.class, roleactionname);
	}

	public boolean save(RoleAction roleAction) {
		RoleAction u = get(roleAction.getRoleactionname());
		if (u == null) {
			getHibernateTemplate().save(roleAction);
			return true;
		} else {
			return false;
		}
	}

	public void update(RoleAction roleAction) {
		getHibernateTemplate().saveOrUpdate(roleAction);
	}

	public void delete(String roleactionname) {
		getHibernateTemplate().delete(
				getHibernateTemplate().get(RoleAction.class, roleactionname));
	}
	
	public void delete(RoleAction roleAction) {
		getHibernateTemplate().delete(roleAction);
	}

	@SuppressWarnings("unchecked")
	public long getRoleActionCount() {
		String sql = "select count(*) from RoleAction";
		Query q = getSession().createQuery(sql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<RoleAction> findAll() {
		List<RoleAction> list = (List<RoleAction>) getHibernateTemplate().find("from RoleAction order by roleactionname");
		return list;
	}


	public List<RoleAction> findByPage(int PAGESIZE, int pageIndex) {
		if (pageIndex > 0) {
			int a = PAGESIZE * (pageIndex - 1);
			int b = PAGESIZE * pageIndex;
			int count = (int) getRoleActionCount();
			if (b > count)
				b = count;

			return findAll().subList(a, b);
		} else {
			return findAll().subList(0, PAGESIZE);
		}

	}
	
	@SuppressWarnings("rawtypes")
	public RoleAction finaRoleActionByName(String roleactionname) {
		List ul = getHibernateTemplate().find(
				"from RoleAction au where au.roleactionname = ?", roleactionname);
		if (ul != null && ul.size() == 1) {
			return (RoleAction) ul.get(0);
		} else if (ul != null && ul.size() > 1) {
			System.out
					.println("more than 1 RoleAction have same name!");
		}
		return null;
	}
	
	public void test() {

	}

}
