package com.jiangyifen.ec.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SmTaskDaoHibernate extends HibernateDaoSupport implements
		SmTaskDao {

	public SmTask get(Long id) {
		return (SmTask) getHibernateTemplate().get(SmTask.class, id);
	}

	@SuppressWarnings("unchecked")
	public SmTask get(String msgid) {
		String sql = "from SmTask where msgid='" + msgid + "'";
		Query q = getSession().createQuery(sql);
		q.setFirstResult(0);
		q.setMaxResults(1);
		List<SmTask> result = (List<SmTask>) q.list();
		if (result != null && result.size() > 0) {
			return result.get(0);
		}

		return null;
	}

	public boolean save(SmTask smTask) {
		getHibernateTemplate().save(smTask);
		return true;
	}

	public void update(SmTask smTask) {
		getHibernateTemplate().saveOrUpdate(smTask);
	}

	public void delete(Long id) {
		getHibernateTemplate().delete(get(id));
	}

	public void delete(SmTask smTask) {
		getHibernateTemplate().delete(smTask);
	}

	@SuppressWarnings("unchecked")
	public long getSmTaskCount() {
		String hql = "select count(*) from SmTask";
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public long getSmTaskCount(String accountId) {
		String hql = "select count(*) from SmTask where accountid='"
				+ accountId + "'";
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<SmTask> findAll() {
		List<SmTask> result = (List<SmTask>) getHibernateTemplate().find(
				"from SmTask order by id");
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<SmTask> findByPage(int pageSize, int pageIndex) {
		String sql = "from SmTask order by id";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<SmTask> result = (List<SmTask>) q.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<SmTask> findByStatus(int pageSize, int pageIndex, String status) {
		String sql = "from SmTask where status='" + status + "' order by id";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<SmTask> result = (List<SmTask>) q.list();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<SmTask> findByStatusAndUserfield(int pageSize, int pageIndex, String status,String userfield) {
		String sql = "from SmTask where status='" + status + "' and userfield='"+userfield+"' order by id";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<SmTask> result = (List<SmTask>) q.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<SmTask> findByDateAndStatus(int pageSize, int pageIndex,
			String beginTime, String endTime, String status) {
		String sql = "from SmTask where timestamp>='" + beginTime
				+ "' and timestamp<='" + endTime + "' status='" + status
				+ "' order by id";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<SmTask> result = (List<SmTask>) q.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<SmTask> findByAccountId(int pageSize, int pageIndex,
			String accountId) {

		Date current = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = sdf.format(current);

		String sql = "from SmTask t where t.timestamp<='" + d
				+ "' and t.status='" + SmTask.READY + "' and t.accountId='"
				+ accountId + "' order by penalty,timestamp";

		
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<SmTask> result = (List<SmTask>) q.list();
		
		return result;

	}

	@SuppressWarnings("unchecked")
	public long getSmTaskCount(String beginTime, String endTime,
			String content, String mobile, String status, String senderId,
			String batchNumber) {
		String sql = "select count(*) from SmTask c where 1=1"
				+ buildWhereSql(beginTime, endTime, content, mobile, status,
						senderId, batchNumber);
		Query q = getSession().createQuery(sql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0).longValue();
		return count;
	}

	private String buildWhereSql(String beginTime, String endTime,
			String content, String mobile, String status, String senderId,
			String batchNumber) {
		String whereSql = "";
		if (beginTime != null && beginTime.length() >= 8) {
			whereSql = whereSql + " and timestamp>='" + beginTime
					+ " 00:00:00'";
		}
		if (endTime != null && endTime.length() >= 8) {
			whereSql = whereSql + " and timestamp<='" + endTime + " 23:59:59'";
		}
		if (content != null && content.length() > 0) {
			whereSql = whereSql + " and content like '%" + content + "%'";
		}
		if (mobile != null && mobile.length() > 0) {
			whereSql = whereSql + " and mobile like '%" + mobile + "%'";
		}
		if (status != null && status.length() > 0) {
			whereSql = whereSql + " and status='" + status + "'";
		}
		if (senderId != null && senderId.length() > 0) {
			whereSql = whereSql + " and senderId like '%" + senderId + "%'";
		}
		if (batchNumber != null && batchNumber.length() > 0) {
			whereSql = whereSql + " and batchNumber like '%" + batchNumber
					+ "%'";
		}

		return whereSql;
	}

	@SuppressWarnings("unchecked")
	public List<SmTask> findSmTask(int pageSize, int pageIndex, String beginTime, String endTime,
			String content, String mobile, String status, String senderId,
			String batchNumber) {
		String sql = "from SmTask c where 1=1"
				+ buildWhereSql(beginTime, endTime, content, mobile, status,
						senderId, batchNumber) + " order by timestamp desc";
		logger.info(sql);
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<SmTask> result = (List<SmTask>) q.list();
		return result;
	}

}
