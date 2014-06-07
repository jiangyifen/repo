package com.jiangyifen.ec.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SipDaoHibernate extends HibernateDaoSupport implements SipDao {

	public Sip get(Long id) {
		return (Sip) getHibernateTemplate().get(Sip.class, id);
	}

	@SuppressWarnings("rawtypes")
	public boolean save(Sip sip) {
		List result = this.getSession().createQuery(
				" from Sip where name=" + sip.getName()).list();

		if (result.size() == 0) {
			getHibernateTemplate().save(sip);
			return true;
		} else {
			return false;
		}
	}

	// @SuppressWarnings("unchecked")
	public void update(Sip sip) {
		// List result =
		// this.getSession().createQuery(" from Sip where name="+sip.getName()).list();
		// Sip s = (Sip)result.get(0);
		// sip.setId(s.getId());
		getHibernateTemplate().saveOrUpdate(sip);
	}

	public void delete(Long id) {
		getHibernateTemplate().delete(get(id));
	}

	public void delete(Sip sip) {
		getHibernateTemplate().delete(sip);
	}

	@SuppressWarnings("unchecked")
	public long getSipCount() {
		String hql = "select count(*) from Sip";
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Sip> findAll() {
		List<Sip> list = (List<Sip>) getHibernateTemplate().find(
				"from Sip order by name");
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Sip> findAllDesc() {
		List<Sip> list = (List<Sip>) getHibernateTemplate().find(
				"from Sip order by name desc");
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Sip> findByPage(int PAGESIZE, int pageIndex) {
		String sql = "from Sip order by name";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * PAGESIZE);
		q.setMaxResults(PAGESIZE);
		List<Sip> sipList = (List<Sip>) q.list();
		return sipList;

	}

	@SuppressWarnings("unchecked")
	public Sip fineByName(String name) {
		List<Sip> list = (List<Sip>) getHibernateTemplate().find(
				"from Sip where name='" + name + "'");
		if (list != null && list.size() > 0) {
			return list.get(0);
		}

		return null;
	}
}
