package com.jiangyifen.ec.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class NoticeItemDaoHibernate extends HibernateDaoSupport implements
		NoticeItemDao {

	public NoticeItem get(Long id) {
		return (NoticeItem) getHibernateTemplate().get(NoticeItem.class, id);
	}

	public boolean save(NoticeItem o) {
		getHibernateTemplate().save(o);
		return true;
	}

	public void update(NoticeItem o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	public void delete(Long id) {
		getHibernateTemplate().delete(get(id));
	}

	public void delete(NoticeItem o) {
		getHibernateTemplate().delete(o);
	}

	//TODO:test this
	public void deleteAllByNoticeid(long id) {
		Session session = getSession();
//		Transaction tran = session.beginTransaction();
		session.createQuery("delete from NoticeItem where noticeid="+ id).executeUpdate();
//		tran.commit();
//		session.close(); 
	}

	@SuppressWarnings("unchecked")
	public long getNoticeItemCount() {
		String hql = "select count(*) from NoticeItem";
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public long getNoticeItemCount(long noticeid) {
		String hql = "select count(*) from NoticeItem where noticeid=" + noticeid;
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();

		long count = l.get(0);

		return count;
	}

	@SuppressWarnings("unchecked")
	public long getNoticeItemCount(boolean read) {
		String hql = "select count(*) from NoticeItem where read=" + read;
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();

		long count = l.get(0);

		return count;
	}
	
	@SuppressWarnings("unchecked")
	public long getNoticeItemCount(long noticeid, boolean read) {
		String hql = "select count(*) from NoticeItem where noticeid=" + noticeid
				+ " and read=" + read ;
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<NoticeItem> findAll() {
		List<NoticeItem> result = (List<NoticeItem>) getHibernateTemplate()
				.find("from NoticeItem order by id");
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<NoticeItem> findByPage(int pageSize, int pageIndex) {
		String sql = "from NoticeItem order by id";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<NoticeItem> result = (List<NoticeItem>) q.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<NoticeItem> findByNoticeid(int pageSize, int pageIndex,
			Long noticeid) {

		String sql = "from NoticeItem t where t.noticeid=" + noticeid
				+ " order by id";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<NoticeItem> result = (List<NoticeItem>) q.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<NoticeItem> findByRead(int pageSize, int pageIndex,
			boolean read) {

		String sql = "from NoticeItem t where t.read=" + read
				+ " order by id";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<NoticeItem> result = (List<NoticeItem>) q.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<NoticeItem> findByNoticeidAndRead(int pageSize,
			int pageIndex, Long noticeid, boolean read) {

		String sql = "from NoticeItem t where t.noticeid=" + noticeid
				+ " and t.read=" + read + " order by id";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<NoticeItem> result = (List<NoticeItem>) q.list();
		return result;
	}




}
