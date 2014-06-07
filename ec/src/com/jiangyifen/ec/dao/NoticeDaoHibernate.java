package com.jiangyifen.ec.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class NoticeDaoHibernate extends HibernateDaoSupport implements
		NoticeDao {

	public Notice get(Long id) {
		return (Notice) getHibernateTemplate().get(Notice.class, id);
	}

	public boolean save(Notice o) {
		getHibernateTemplate().save(o);
		return true;
	}

	public void update(Notice o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	public void delete(Long id) {
		getHibernateTemplate().delete(get(id));
	}

	public void delete(Notice o) {
		getHibernateTemplate().delete(o);
	}

	@SuppressWarnings("unchecked")
	public long getCount() {
		String hql = "select count(*) from Notice";
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public long getCount(String beginTime, String endTime, String title,
			String content) {
		String hql = "select count(*) from Notice where date>='" + beginTime
				+ "' and date<='" + endTime + "' and title like '%" + title
				+ "%' and content like '%" + content + "%'";
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Notice> findAll() {
		List<Notice> result = (List<Notice>) getHibernateTemplate().find(
				"from Notice order by id");
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Notice> find(int pageSize, int pageIndex) {
		String sql = "from Notice order by id desc";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<Notice> result = (List<Notice>) q.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Notice> find(int pageSize, int pageIndex, String beginTime,
			String endTime, String title, String content) {

		String sql = "from Notice where date>='" + beginTime
				+ " 00:00:00' and date<='" + endTime + " 23:59:59' and title like '%" + title
				+ "%' and content like '%" + content + "%' order by id desc";
		logger.info(sql);
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<Notice> result = (List<Notice>) q.list();
		return result;
	}

}
