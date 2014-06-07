package com.jiangyifen.ec.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class QueueCallerAbandonEventLogDaoHibernate extends HibernateDaoSupport
		implements QueueCallerAbandonEventLogDao {

	public void save(QueueCallerAbandonEventLog o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

}
