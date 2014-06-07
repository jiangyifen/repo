package com.jiangyifen.ec.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class MyCustomerLogDaoHibernate extends HibernateDaoSupport implements
		MyCustomerLogDao {

	public void save(MyCustomerLog o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	@SuppressWarnings("unchecked")
	public long getMyCustomerLogCount(long taskid) {
		String hql = "select count(*) from MyCustomerLog where dialtaskid='"
				+ taskid + "'";
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public long getMyCustomerLogCountByCustomerPhoneNumber(String customerPhoneNumber) {
		String hql = "select count(*) from MyCustomerLog where customerphonenumber='"
			+ customerPhoneNumber + "'";
	Query q = getSession().createQuery(hql);
	List<Long> l = (List<Long>) q.list();
	long count = l.get(0);
	return count;
		
	}

}
