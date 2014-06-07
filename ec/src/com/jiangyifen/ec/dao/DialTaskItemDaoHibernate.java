package com.jiangyifen.ec.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class DialTaskItemDaoHibernate extends HibernateDaoSupport implements
		DialTaskItemDao {

	public DialTaskItem get(Long id) {
		return (DialTaskItem) getHibernateTemplate()
				.get(DialTaskItem.class, id);
	}

	public boolean save(DialTaskItem o) {
		getHibernateTemplate().save(o);
		return true;
	}

	public void update(DialTaskItem o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	public void delete(Long id) {
		getHibernateTemplate().delete(get(id));
	}

	public void delete(DialTaskItem o) {
		getHibernateTemplate().delete(o);
	}

	//TODO:test this
	public void deleteAllByTaskId(long id) {
		Session session = getSession();
//		Transaction tran = session.beginTransaction();
		session.createQuery("delete from DialTaskItem where taskid="+ id).executeUpdate();
//		tran.commit();
//		session.close(); 
	}

	@SuppressWarnings("unchecked")
	public long getDialTaskItemCount() {
		String hql = "select count(*) from DialTaskItem";
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public long getDialTaskItemCount(long taskId) {
		String hql = "select count(*) from DialTaskItem where taskid=" + taskId;
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();
		
		long count = l.get(0);
		
		return count;
	}

	@SuppressWarnings("unchecked")
	public long getDialTaskItemCount(Set<User> users, String status) {
		String usernames = "";
		for (User user : users) {
			usernames = usernames + "'" + user.getUsername() + "',";
		}
		usernames = usernames.substring(0, usernames.length() - 1);
		
		String hql = "select count(*) from DialTaskItem where owner in ("+usernames+") and status='" + status + "'";
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}
	
	@SuppressWarnings("unchecked")
	public long getDialTaskItemCount(long taskId, String status) {
		String hql = "select count(*) from DialTaskItem where taskid=" + taskId
				+ " and status='" + status + "'";
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}


	@SuppressWarnings("unchecked")
	public List<DialTaskItem> findAll() {
		List<DialTaskItem> result = (List<DialTaskItem>) getHibernateTemplate()
				.find("from DialTaskItem order by id");
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<DialTaskItem> findByPage(int pageSize, int pageIndex) {
		String sql = "from DialTaskItem order by id";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<DialTaskItem> result = (List<DialTaskItem>) q.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<DialTaskItem> findByTaskId(int pageSize, int pageIndex,
			Long taskid) {

		String sql = "from DialTaskItem t where t.taskid=" + taskid
				+ " order by id";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<DialTaskItem> result = (List<DialTaskItem>) q.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<DialTaskItem> findByStatus(int pageSize, int pageIndex,
			String status) {

		String sql = "from DialTaskItem t where t.status='" + status
				+ "' order by id";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<DialTaskItem> result = (List<DialTaskItem>) q.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<DialTaskItem> findByTaskIdAndStatus(int pageSize,
			int pageIndex, Long taskid, String status) {

		String sql = "from DialTaskItem t where t.status='" + status
				+ "' and t.taskid=" + taskid + " order by id";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<DialTaskItem> result = (List<DialTaskItem>) q.list();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<DialTaskItem> findByTaskIdAndStatusHasSmReply(int pageSize,
			int pageIndex, Long taskid, String status) {

		String sql = "from DialTaskItem t where hassmreply=true and t.status='" + status
				+ "' and t.taskid=" + taskid + " order by id";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<DialTaskItem> result = (List<DialTaskItem>) q.list();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<DialTaskItem> findByHasSmReplyAndStatus(int pageSize,
			int pageIndex, boolean hasSmReply, String status) {

		String sql = "from DialTaskItem t where t.status='" + status
				+ "' and t.hasSmReply=" + hasSmReply + " order by id";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<DialTaskItem> result = (List<DialTaskItem>) q.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<DialTaskItem> findByPhoneNumberAndStatus(int pageSize,
			int pageIndex, String phoneNumber, String status) {

		String sql = "from DialTaskItem where status='" + status
				+ "' and phonenumber='" + phoneNumber
				+ "' order by calldate desc";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<DialTaskItem> result = (List<DialTaskItem>) q.list();
		return result;
	}

	//TODO:test this
	public void updateSmReplyByPhoneNumber(String phoneNumber) {
		Session session = getSession();
//		Transaction tran = session.beginTransaction();
		session.createQuery("update DialTaskItem set hassmreply=true where phonenumber ='"+ phoneNumber + "'or phonenumber2='"+phoneNumber+"'").executeUpdate();
//		tran.commit();
//		session.close(); 
	}

}
