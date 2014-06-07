package com.jiangyifen.ec.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class QueueEntryEventLogDaoHibernate extends HibernateDaoSupport
		implements QueueEntryEventLogDao {

	public void save(QueueEntryEventLog o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

}
