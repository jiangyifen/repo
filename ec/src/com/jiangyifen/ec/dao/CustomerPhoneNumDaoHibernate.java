package com.jiangyifen.ec.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CustomerPhoneNumDaoHibernate extends HibernateDaoSupport implements CustomerPhoneNumDao {

	public CustomerPhoneNum get(String customerPhoneNumber) {
		return (CustomerPhoneNum) getHibernateTemplate().get(CustomerPhoneNum.class, customerPhoneNumber);
	}

}