package com.jiangyifen.ec.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class IVRLogDaoHibernate extends HibernateDaoSupport implements
		IVRLogDao {

	public void save(IVRLog o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public long getCountByNode(String node) {
		String hql = "select count(*) from IVRLog where node='" + node + "'";
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

}
