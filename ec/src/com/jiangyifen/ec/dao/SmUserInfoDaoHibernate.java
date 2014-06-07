package com.jiangyifen.ec.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SmUserInfoDaoHibernate extends HibernateDaoSupport implements
		SmUserInfoDao {

	public SmUserInfo get(String accountId) {
		return (SmUserInfo) getHibernateTemplate()
				.get(SmUserInfo.class, accountId);
	}

	public boolean save(SmUserInfo smUserInfo) {
		getHibernateTemplate().save(smUserInfo);
		return true;
	}

	public void update(SmUserInfo smUserInfo) {
		getHibernateTemplate().saveOrUpdate(smUserInfo);
	}

	public void delete(String accountId) {
		getHibernateTemplate().delete(
				getHibernateTemplate().get(SmUserInfo.class, accountId));
	}

	public void delete(SmUserInfo smUserInfo) {
		getHibernateTemplate().delete(smUserInfo);
	}

	@SuppressWarnings("unchecked")
	public long getSmUserInfoCount() {
		String hql = "select count(*) from SmUserInfo";
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<SmUserInfo> findAll() {
		List<SmUserInfo> result = (List<SmUserInfo>) getHibernateTemplate()
				.find("from SmUserInfo order by accountId");
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<SmUserInfo> findByPage(int PAGESIZE, int pageIndex) {
		String sql = "from SmUserInfo order by accountId";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * PAGESIZE);
		q.setMaxResults(PAGESIZE);
		List<SmUserInfo> result = (List<SmUserInfo>) q.list();
		return result;
	}
}
