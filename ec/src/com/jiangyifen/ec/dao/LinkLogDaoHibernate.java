package com.jiangyifen.ec.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class LinkLogDaoHibernate extends HibernateDaoSupport implements
		LinkLogDao {

	@Override
	public void save(LinkLog o) {
		getHibernateTemplate().saveOrUpdate(o);
	}


}
