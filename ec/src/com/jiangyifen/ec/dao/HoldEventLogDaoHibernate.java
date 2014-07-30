package com.jiangyifen.ec.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HoldEventLogDaoHibernate extends HibernateDaoSupport
		implements HoldEventLogDao {

	public void save(HoldEventLog alr) {
		getHibernateTemplate().saveOrUpdate(alr);
	}
	
	public void update(HoldEventLog alr) {
		getHibernateTemplate().update(alr);
	}


	public HoldEventLog findLastLogByUniqueid(String uniqueid) {
		String hql = "from HoldEventLog t where t.uniqueid ='"+uniqueid+"' order by id desc";
		Query q = getSession().createQuery(hql);
		q.setFirstResult(0);
		q.setMaxResults(1);
		
		@SuppressWarnings("unchecked")
		List<HoldEventLog> result = (List<HoldEventLog>)q.list();
		
		if (result != null && result.size() >= 1) {
			return result.get(0);
		}

		return null;
	}

}
