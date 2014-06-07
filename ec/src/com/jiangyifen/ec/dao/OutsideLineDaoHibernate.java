package com.jiangyifen.ec.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class OutsideLineDaoHibernate extends HibernateDaoSupport implements
		OutsideLineDao {

	public OutsideLine get(String phoneNumber) {
		return (OutsideLine) getHibernateTemplate().get(OutsideLine.class,
				phoneNumber);
	}

	public void save(OutsideLine o) {
		getHibernateTemplate().save(o);
	}

	public void update(OutsideLine o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	public void delete(OutsideLine o) {
		delete(o);
	}

	public void delete(String phoneNumber) {
		getHibernateTemplate().delete(	(OutsideLine) getHibernateTemplate().get(OutsideLine.class, phoneNumber));
	}

	@SuppressWarnings("unchecked")
	public long getOutsideLineCount() {
		String hql = "select count(*) from OutsideLine";
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<OutsideLine> findAll() {
		List<OutsideLine> list = (List<OutsideLine>) getHibernateTemplate().find("from OutsideLine order by phonenumber");
		return list;
	}

	public List<OutsideLine> findByPage(int PAGESIZE, int pageIndex) {
		if (pageIndex > 0) {
			int a = PAGESIZE * (pageIndex - 1);
			int b = PAGESIZE * pageIndex;
			int count = (int) getOutsideLineCount();
			if (b > count)
				b = count;

			return findAll().subList(a, b);
		} else {
			return findAll().subList(0, PAGESIZE);
		}

	}

}
