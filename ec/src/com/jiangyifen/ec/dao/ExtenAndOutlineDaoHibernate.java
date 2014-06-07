package com.jiangyifen.ec.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ExtenAndOutlineDaoHibernate extends HibernateDaoSupport implements
ExtenAndOutlineDao {

	public ExtenAndOutline get(String exten) {
		return (ExtenAndOutline) getHibernateTemplate().get(ExtenAndOutline.class,
				exten);
	}

	public void save(ExtenAndOutline o) {
		getHibernateTemplate().save(o);
	}

	public void update(ExtenAndOutline o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	public void delete(ExtenAndOutline o) {
		delete(o);
	}

	public void delete(String exten) {
		getHibernateTemplate().delete((ExtenAndOutline) get(exten));
	}

	@SuppressWarnings("unchecked")
	public long getExtenAndOutlineCount() {
		String hql = "select count(*) from ExtenAndOutline";
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<ExtenAndOutline> findAll() {
		List<ExtenAndOutline> list = (List<ExtenAndOutline>) getHibernateTemplate().find("from ExtenAndOutline order by exten");
		return list;
	}

	public List<ExtenAndOutline> findByPage(int PAGESIZE, int pageIndex) {
		if (pageIndex > 0) {
			int a = PAGESIZE * (pageIndex - 1);
			int b = PAGESIZE * pageIndex;
			int count = (int) getExtenAndOutlineCount();
			if (b > count)
				b = count;

			return findAll().subList(a, b);
		} else {
			return findAll().subList(0, PAGESIZE);
		}

	}

}
