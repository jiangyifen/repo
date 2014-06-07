package com.jiangyifen.ec.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class QueueMemberDaoHibernate extends HibernateDaoSupport implements
		QueueMemberDao {

	public boolean save(QueueMember qm) {
		getHibernateTemplate().saveOrUpdate(qm);
		return true;
	}

	public void delete(QueueMember qm) {
		getHibernateTemplate().delete(qm);
	}

	@SuppressWarnings("unchecked")
	public List<QueueMember> findAll() {
		List<QueueMember> list = (List<QueueMember>) getHibernateTemplate().find(
				"from QueueMember order by queue_name, penalty, interface");
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<QueueMember> findAllByQueue(String queueName) {
		List<QueueMember> list = (List<QueueMember>) getHibernateTemplate().find(
				"from QueueMember where queue_name='"+queueName+"' order by penalty, interface");
		return list;
	}
	
}
