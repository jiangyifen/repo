package com.jiangyifen.ec.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class MeetMeRoomDaoHibernate extends HibernateDaoSupport implements
		MeetMeRoomDao {

	public MeetMeRoom get(String confno) {
		return (MeetMeRoom) getHibernateTemplate()
				.get(MeetMeRoom.class, confno);
	}

	public void add(MeetMeRoom o) {
		getHibernateTemplate().save(o);
	}

	public void update(MeetMeRoom o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	public void delete(String confno) {
		getHibernateTemplate().delete(
				getHibernateTemplate().get(MeetMeRoom.class, confno));
	}

	public void delete(MeetMeRoom o) {
		getHibernateTemplate().delete(o);
	}

	@SuppressWarnings("unchecked")
	public long getCount() {
		String sql = "select count(*) from MeetMeRoom";
		Query q = getSession().createQuery(sql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<MeetMeRoom> findAll() {
		List<MeetMeRoom> list = (List<MeetMeRoom>) getHibernateTemplate().find(
				"from MeetMeRoom order by confno");
		return list;
	}

	public List<MeetMeRoom> findByPage(int PAGESIZE, int pageIndex) {
		if (pageIndex > 0) {
			int a = PAGESIZE * (pageIndex - 1);
			int b = PAGESIZE * pageIndex;
			int count = (int) getCount();
			if (b > count)
				b = count;

			return findAll().subList(a, b);
		} else {
			return findAll().subList(0, PAGESIZE);
		}

	}

	@SuppressWarnings("rawtypes")
	public MeetMeRoom findByConfno(String confno) {
		List ul = getHibernateTemplate().find(
				"from MeetMeRoom au where au.confno = ?", confno);
		if (ul != null && ul.size() > 0)
			return (MeetMeRoom) ul.get(0);
		else
			return null;

	}


}
