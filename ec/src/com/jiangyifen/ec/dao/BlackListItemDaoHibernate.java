package com.jiangyifen.ec.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class BlackListItemDaoHibernate extends HibernateDaoSupport implements
		BlackListItemDao {

	public BlackListItem get(Long id) {
		return (BlackListItem) getHibernateTemplate().get(BlackListItem.class,
				id);
	}

	public boolean save(BlackListItem o) {
		getHibernateTemplate().save(o);
		return true;
	}

	public void update(BlackListItem o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	public void delete(Long id) {
		getHibernateTemplate().delete(get(id));
	}

	public void delete(BlackListItem o) {
		getHibernateTemplate().delete(o);
	}

	@SuppressWarnings("unchecked")
	public List<BlackListItem> findAll() {
		List<BlackListItem> result = (List<BlackListItem>) getHibernateTemplate()
				.find("from BlackListItem order by id");
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<BlackListItem> findAllByType(String type1, String type2) {
		List<BlackListItem> result = (List<BlackListItem>) getHibernateTemplate()
				.find("from BlackListItem where type='"+type1+"' or type='"+type2+"' order by id");
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<BlackListItem> findByPage(int pageSize, int pageIndex) {
		String sql = "from BlackListItem order by id";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<BlackListItem> result = (List<BlackListItem>) q.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<BlackListItem> findByType(int pageSize, int pageIndex,
			String type) {

		String sql = "from BlackListItem t where t.type=" + type
				+ " order by id";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<BlackListItem> result = (List<BlackListItem>) q.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<BlackListItem> findByTypeAndPhoneNumber(int pageSize,
			int pageIndex, String type, String phoneNumber) {
		String sql = "from BlackListItem where 1=1";
		if (type != null
				&& (type.equals(BlackListItem.TYPE_INCOMING) || type
						.equals(BlackListItem.TYPE_OUTGOING))) {
			sql = sql + " and type='" + type + "'";
		}
		if (phoneNumber != null && !phoneNumber.equals("")) {
			sql = sql + " and phoneNumber like '%" + phoneNumber + "%'";
		}
		
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<BlackListItem> result = (List<BlackListItem>) q.list();
		return result;
	}

	public int getBlackListItemCount(String phoneNumber, String type) {
		String sql = "select count(*) from BlackListItem where 1=1";
		if (type != null
				&& (type.equals(BlackListItem.TYPE_INCOMING) || type
						.equals(BlackListItem.TYPE_OUTGOING))) {
			sql = sql + " and type='" + type + "'";
		}
		if (phoneNumber != null && !phoneNumber.equals("")) {
			sql = sql + " and phoneNumber like '%" + phoneNumber + "%'";
		}

		Query q = getSession().createQuery(sql);
		@SuppressWarnings("unchecked")
		List<Long> l = (List<Long>) q.list();
		int count = l.get(0).intValue();
		return count;
	}

}
