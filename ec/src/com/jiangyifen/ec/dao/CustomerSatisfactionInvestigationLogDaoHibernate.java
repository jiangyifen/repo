package com.jiangyifen.ec.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CustomerSatisfactionInvestigationLogDaoHibernate extends HibernateDaoSupport implements CustomerSatisfactionInvestigationLogDao{

	public void save(CustomerSatisfactionInvestigationLog o) {
		getHibernateTemplate().saveOrUpdate(o);
	}
	
}
