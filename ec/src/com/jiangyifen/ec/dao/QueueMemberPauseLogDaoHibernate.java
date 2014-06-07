package com.jiangyifen.ec.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class QueueMemberPauseLogDaoHibernate extends HibernateDaoSupport
		implements QueueMemberPauseLogDao {

	public void save(QueueMemberPauseLog alr) {
		getHibernateTemplate().saveOrUpdate(alr);
	}
	
	public void update(QueueMemberPauseLog alr) {
		getHibernateTemplate().update(alr);
	}


	public QueueMemberPauseLog findLastLogByMemberNameAndQueueName(String memberName, String queueName) {
		String hql = "from QueueMemberPauseLog t where t.memberName ='"+memberName+"' and t.queue ='"+queueName+"' order by id desc";
		Query q = getSession().createQuery(hql);
		q.setFirstResult(0);
		q.setMaxResults(1);
		
		@SuppressWarnings("unchecked")
		List<QueueMemberPauseLog> result = (List<QueueMemberPauseLog>)q.list();
		
		if (result != null && result.size() >= 1) {
			return result.get(0);
		}

		return null;
	}

}
