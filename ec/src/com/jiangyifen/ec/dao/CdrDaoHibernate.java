package com.jiangyifen.ec.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CdrDaoHibernate extends HibernateDaoSupport implements CdrDao {

	public Cdr getCdrByUniqueid(String uniqueid) {
		return (Cdr) getHibernateTemplate().get(Cdr.class, uniqueid);
	}

	public void updateCdr(Cdr cdr) {
		getHibernateTemplate().merge(cdr);
	}

	@SuppressWarnings("unchecked")
	public List<Cdr> findCdr(String dst, int pageSize) {
		String hql = "from Cdr c where length(split_part(userfield,',',2))>0 and substring(c.dst,0,4)='"
				+ dst + "' order by calldate desc";
		Query q = getSession().createQuery(hql);
		q.setFirstResult(0);
		q.setMaxResults(pageSize);
		List<Cdr> cdrList = (List<Cdr>) q.list();
		return cdrList;
	}

	@SuppressWarnings("unchecked")
	public List<Cdr> findCdr(String beginTime, String endTime, String src,
			String dst, String fenji, String direction, String hid, String status,
			String moreThen, String lessThen, String andor, String username,
			Set<Department> dpmts, int pageSize, int pageIndex) {
		String sql = "from Cdr c where 1=1"
				+ buildWhereSql(beginTime, endTime, src, dst, fenji, direction, hid, 
						status, moreThen, lessThen, andor, username, dpmts)
				+ " order by c.calldate desc";
		logger.info(sql);
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<Cdr> cdrList = (List<Cdr>) q.list();
		return cdrList;
	}

	@SuppressWarnings("unchecked")
	public List<Cdr> findCdrByUsername(String username, int pageSize,
			int pageIndex) {
		String sql = "from Cdr c where username='" + username
				+ "' order by c.calldate desc";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<Cdr> cdrList = (List<Cdr>) q.list();
		return cdrList;
	}

	@SuppressWarnings("unchecked")
	public List<Cdr> findCdrByDst(String dst, int pageSize, int pageIndex) {
		String sql = "from Cdr c where dst='" + dst
				+ "' order by c.calldate desc";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<Cdr> cdrList = (List<Cdr>) q.list();
		return cdrList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Cdr> findCdrByHIDSrcOrDst(String phoneNumber, String hid, int pageSize, int pageIndex) {
		String sql = "from Cdr c where hid='"+hid+"' and src='"+phoneNumber+"' or dst='" + phoneNumber
				+ "' order by c.calldate desc";
		Query q = getSession().createQuery(sql);
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageSize);
		List<Cdr> cdrList = (List<Cdr>) q.list();
		return cdrList;
	}

	@SuppressWarnings("unchecked")
	public int getCdrCount(String beginTime, String endTime, String src,
			String dst, String fenji, String direction, String hid, String status,
			String moreThen, String lessThen, String andor, String username,
			Set<Department> dpmts) {
		String sql = "select count(*) from Cdr c where 1=1"
				+ buildWhereSql(beginTime, endTime, src, dst, fenji, direction, hid, 
						status, moreThen, lessThen, andor, username, dpmts);
		Query q = getSession().createQuery(sql);
		List<Long> l = (List<Long>) q.list();
		int count = l.get(0).intValue();
		return count;
	}

	private String buildWhereSql(String beginTime, String endTime, String src,
			String dst, String fenji, String direction, String hid, String disposition,
			String moreThen, String lessThen, String andor, String username,
			Set<Department> dpmts) {
		String whereSql = "";
		if (beginTime != null && beginTime.length() >= 8) {
			whereSql = whereSql + " and c.calldate>='" + beginTime
					+ " 00:00:00'";
		}
		if (endTime != null && endTime.length() >= 8) {
			whereSql = whereSql + " and c.calldate<='" + endTime + " 23:59:59'";
		}
		if (src != null && src.length() > 0) {
			whereSql = whereSql + " and c.src like '%" + src + "%'";
		}
		if (dst != null && dst.length() > 0) {
			whereSql = whereSql + " and c.dst like '%" + dst + "%'";
		}
		if (fenji != null && fenji.length() > 0) {
			whereSql = whereSql + " and c.fenji like '%" + fenji + "%'";
		}
		if (hid != null && hid.length() > 0) {
			whereSql = whereSql + " and c.hid like '%" + hid + "%'";
		}
		if (direction != null && direction.length() > 0
				&& !direction.toLowerCase().equals("both")) {
			whereSql = whereSql + " and c.dcontext='" + direction + "'";
		}
		if (disposition != null && disposition.length() > 0
				&& !disposition.toLowerCase().equals("all")) {
			if (disposition.equals("TRANSFER"))
				disposition = "";
			if (disposition.toLowerCase().equals("answer")) {
				whereSql = whereSql + " and disposition='ANSWER'";
			} else {
				whereSql = whereSql + " and disposition<>'ANSWER'";
			}
		}
		if (moreThen != null && lessThen != null && andor != null) {
			try {
				Integer m = new Integer(moreThen);
				Integer l = new Integer(lessThen);
				whereSql = whereSql + " and (c.billsec>=" + m + " " + andor
						+ " " + "c.billsec <=" + l + ")";
			} catch (NumberFormatException e) {

			}
		}
		if (username != null && username.length() > 0) {
			whereSql = whereSql + " and c.username='" + username + "'";
		}
		if (dpmts != null && dpmts.size() > 0) {
			String s = "";
			for (Department d : dpmts) {
				s = s + "'" + d.getDepartmentname() + "',";
			}
			s = s.substring(0, s.length() - 1);
			whereSql = whereSql + " and  dpmt in (" + s + ")";
		}

		return whereSql;
	}

}
