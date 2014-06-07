package com.jiangyifen.ec.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class QueueDaoHibernate extends HibernateDaoSupport implements QueueDao {

	public Queue get(String queueName) {
		return (Queue) getHibernateTemplate().get(Queue.class, queueName);
	}

	public boolean save(Queue queue) {
		Queue q = get(queue.getName());
		if (q == null) {
			getHibernateTemplate().save(queue);
			return true;
		} else {
			return false;
		}
	}

	public void update(Queue queue) {
		getHibernateTemplate().saveOrUpdate(queue);
	}

	public void delete(Queue queue) {
		delete(queue);
	}

	public void delete(String queueName) {
		getHibernateTemplate().delete(get(queueName));
	}

	@SuppressWarnings("unchecked")
	public long getQueueCount() {
		String hql = "select count(*) from Queue";
		Query q = getSession().createQuery(hql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Queue> findAll() {
		List<Queue> list = (List<Queue>) getHibernateTemplate().find(
				"from Queue order by name");
		return list;
	}

	public List<Queue> findByPage(int PAGESIZE, int pageIndex) {
		if (pageIndex > 0) {
			int a = PAGESIZE * (pageIndex - 1);
			int b = PAGESIZE * pageIndex;
			int count = (int) getQueueCount();
			if (b > count)
				b = count;

			return findAll().subList(a, b);
		} else {
			return findAll().subList(0, PAGESIZE);
		}

	}

	public void test() {

	}

}
