package com.jiangyifen.ec.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CustomerManagerDaoHibernate extends HibernateDaoSupport implements CustomerManagerDao {

	public CustomerManager get(String customerManagerLoginName) {
		return (CustomerManager) getHibernateTemplate().get(CustomerManager.class, customerManagerLoginName);
	}
	
	public void update(CustomerManager customerManager) {
		getHibernateTemplate().merge(customerManager);
	}
	
}
