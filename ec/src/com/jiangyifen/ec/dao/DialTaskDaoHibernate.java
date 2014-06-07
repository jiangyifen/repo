package com.jiangyifen.ec.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class DialTaskDaoHibernate extends HibernateDaoSupport implements
		DialTaskDao {

	public DialTask get(long id) {
		return (DialTask) getHibernateTemplate().get(DialTask.class, id);
	}

	public boolean save(DialTask o) {
		getHibernateTemplate().save(o);
		return true;
	}

	public void update(DialTask o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	public void delete(long id) {
		getHibernateTemplate().delete(get(id));
	}

	public void delete(DialTask o) {
		getHibernateTemplate().delete(o);
	}

	@SuppressWarnings("unchecked")
	public long getDialTaskCount() {
		String hql = "select count(*) from DialTask";
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();

		long count = l.get(0);
		
		return count;
	}
	
//	@SuppressWarnings("unchecked")
//	public long getDialTaskItemCount(long taskid) {
//		String hql = "select count(*) from DialTaskItem where taskid="+taskid+"";
//		Query q = getSession().createQuery(hql);
//		List<Long> l = (List<Long>) q.list();
//
//		long count = l.get(0);
//		
//		return count;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public long getDialTaskItemCount(long taskid, String status) {
//		String hql = "select count(*) from DialTaskItem where taskid="+taskid+" and status='"+status+"'";
//		Query q = getSession().createQuery(hql);
//		List<Long> l = (List<Long>) q.list();
//
//		long count = l.get(0);
//		
//		return count;
//	}

	@SuppressWarnings("unchecked")
	public List<DialTask> findAll() {
		List<DialTask> result = (List<DialTask>) getHibernateTemplate().find(
				"from DialTask order by id");
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<DialTask> findByStatus(String status) {
		List<DialTask> result = (List<DialTask>) getHibernateTemplate().find(
				"from DialTask where status='"+status+"' order by id");
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<DialTask> findByAutoAssign(boolean autoAssign) {
		List<DialTask> result = (List<DialTask>) getHibernateTemplate().find(
				"from DialTask where autoassign="+autoAssign+" order by perority,id");
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<DialTask> findByQueueName(String queueName) {
		List<DialTask> result = (List<DialTask>) getHibernateTemplate().find(
				"from DialTask where queuename='"+queueName+"'");
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<DialTask> findByTaskName(String taskName) {
		List<DialTask> result = (List<DialTask>) getHibernateTemplate().find(
				"from DialTask where taskname='"+taskName+"' order by id");
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<DialTask> findByPage(int PAGESIZE, int pageIndex) {
		String sql = "from DialTask order by perority,queuename,id desc";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * PAGESIZE);
		q.setMaxResults(PAGESIZE);
		List<DialTask> result = (List<DialTask>) q.list();
		return result;
	}
}
