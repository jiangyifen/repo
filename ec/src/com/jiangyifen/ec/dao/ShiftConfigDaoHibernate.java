package com.jiangyifen.ec.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ShiftConfigDaoHibernate extends HibernateDaoSupport implements
ShiftConfigDao {

	public ShiftConfig get(Long id) {
		return (ShiftConfig) getHibernateTemplate()
				.get(ShiftConfig.class, id);
	}

	public boolean save(ShiftConfig o) {
		getHibernateTemplate().save(o);
		return true;
	}

	public void update(ShiftConfig o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	public void delete(Long id) {
		getHibernateTemplate().delete(get(id));
	}

	public void delete(ShiftConfig o) {
		getHibernateTemplate().delete(o);
	}

	@SuppressWarnings("unchecked")
	public List<ShiftConfig> findAll() {
		List<ShiftConfig> result = (List<ShiftConfig>) getHibernateTemplate()
				.find("from ShiftConfig order by id");
		return result;
	}
}
