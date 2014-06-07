package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.Queue;
import com.jiangyifen.ec.dao.QueueDao;

public class QueueManagerImpl implements QueueManager {
	
	private QueueDao queueDao;

	public void setQueueDao(QueueDao queueDao) {
		this.queueDao = queueDao;
	}

	public QueueDao getQueueDao() {
		return queueDao;
	}

	public boolean addQueue(String queueName) throws Exception {

		try {
			Queue q = new Queue();
			q.setName(queueName);
			return queueDao.save(q);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("新增呼叫队列时出现异常");
		}
	}
	
	public boolean addQueue(Queue queue) throws Exception {

		try {
			return queueDao.save(queue);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("新增呼叫队列时出现异常");
		}
	}

	public void deleteQueue(String queueName) throws Exception {
		queueDao.delete(queueName);
	}

	public void deleteQueues(String[] queueNames) throws Exception {
		for (String queueName : queueNames) {
			queueDao.delete(queueName);
		}
	}

	public void updateQueue(Queue queue) throws Exception {
		queueDao.update(queue);
	}

	public int getQueueCount() {
		return (int) queueDao.getQueueCount();
	}

	public Queue getQueue(String queueName) {
		return queueDao.get(queueName);
	}

	public List<Queue> getQueues() throws Exception {
		return queueDao.findAll();
	}

	public List<Queue> getQueuesByPage(int pageSize, int pageIndex)
			throws Exception {
		return queueDao.findByPage(pageSize, pageIndex);
	}

	public void test() {
		queueDao.test();
	}
}
