package com.jiangyifen.ec.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class DialTaskLogDaoHibernate extends HibernateDaoSupport implements
		DialTaskLogDao {

	public void save(DialTaskLog dtl) {
		getHibernateTemplate().saveOrUpdate(dtl);
	}

	public void update(DialTaskLog dtl) {
		getHibernateTemplate().saveOrUpdate(dtl);
	}
	
	@SuppressWarnings("unchecked")
	public DialTaskLog getByDialTaskId(long id) {
		List<DialTaskLog> list = (List<DialTaskLog>) getHibernateTemplate().find(
				"from DialTaskLog where dialtaskid=" + id + " order by date desc");
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Long getDialTaskLogCount() {
		String sql = "select count(*) from DialTaskLog";
		Query q = getSession().createQuery(sql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}
	
	@SuppressWarnings("unchecked")
	public List<DialTaskLog> getDialTaskLogs(int pageSize, int pageIndex){
		String sql = "from DialTaskLog order by date desc";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<DialTaskLog> result = (List<DialTaskLog>) q.list();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<DialTaskLog> getDialTaskLogs(String dialTaskName, String beginTime, String endTime, int pageSize, int pageIndex){
		String sql = "from DialTaskLog where 1=1 "+ buildWhereSql(dialTaskName, beginTime, endTime)+" order by date desc";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<DialTaskLog> result = (List<DialTaskLog>) q.list();
		
		return result;
	}

	private String buildWhereSql(String dialTaskName, String beginTime, String endTime) {
		String whereSql = "";
		if (beginTime != null && beginTime.length() >= 8) {
			whereSql = whereSql + " and date>='" + beginTime
					+ " 00:00:00'";
		}
		if (endTime != null && endTime.length() >= 8) {
			whereSql = whereSql + " and date<='" + endTime + " 23:59:59'";
		}
		if (dialTaskName != null && dialTaskName.length() > 0) {
			whereSql = whereSql + " and dialtaskname like '%" + dialTaskName + "%'";
		}

		return whereSql;
	}
}
