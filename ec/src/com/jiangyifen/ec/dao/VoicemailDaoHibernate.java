package com.jiangyifen.ec.dao;

import java.util.List;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class VoicemailDaoHibernate extends HibernateDaoSupport implements VoicemailDao {

	@SuppressWarnings("unchecked")
	public List<Voicemail> findVoicemail(String beginTime, String endTime, String callerid, String mailbox, int pageSize, int pageIndex) {
		String sql = "from Voicemail c where 1=1" + buildWhereSql(beginTime, endTime, callerid, mailbox) + " order by c.origdate desc";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<Voicemail> vmList = (List<Voicemail>) q.list();
		return vmList;
	}

	@SuppressWarnings("unchecked")
	public int getVmCount(String beginTime, String endTime, String callerid, String mailbox) {
		String sql = "select count(*) from Voicemail c where 1=1" + buildWhereSql(beginTime, endTime, callerid,mailbox);
		Query q = getSession().createQuery(sql);
		List<Long> l = (List<Long>) q.list();
		int count = l.get(0).intValue();
		return count;
	}
	
	private String buildWhereSql(String beginTime, String endTime, String callerid, String mailbox){
		String whereSql = "";
		if (beginTime != null && beginTime.length() >= 8) {
			whereSql = whereSql + " and c.origdate>='" + beginTime + " 00:00:00'";
		}
		if (endTime != null && endTime.length() >= 8) {
			whereSql = whereSql + " and c.origdate<='" + endTime + " 23:59:59'";
		}
		if (callerid != null && callerid.length() > 0) {
			whereSql = whereSql + " and c.callerid like '%" + callerid + "%'";
		}
		if (mailbox != null && mailbox.length() > 0) {
			whereSql = whereSql + " and c.origmailbox like '%" + mailbox + "%'";
		}

		return whereSql;
	}

	public void update(Voicemail vm) {
		getHibernateTemplate().saveOrUpdate(vm);
	}
}
