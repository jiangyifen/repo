package com.jiangyifen.ec.dao;

import java.util.List;

public interface QueueDao {
	Queue get(String queueName);
	boolean save(Queue queue);
	void update(Queue queue);
	void delete(Queue queue);
	void delete(String queueName);
	
	long getQueueCount();
	
	List<Queue> findAll();
	List<Queue> findByPage(int pageSize,int pageIndex);

	void test();
}
