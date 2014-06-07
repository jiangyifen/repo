package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.Queue;

public interface QueueManager {
	boolean addQueue(String queueName) throws Exception;
	
	boolean addQueue(Queue queue) throws Exception;

	void deleteQueue(String user) throws Exception;

	void deleteQueues(String[] queueNames) throws Exception;

	void updateQueue(Queue queue) throws Exception;

	int getQueueCount() throws Exception;

	Queue getQueue(String queueName) throws Exception;

	List<Queue> getQueues() throws Exception;

	List<Queue> getQueuesByPage(int pageSize, int pageIndex) throws Exception;

	void test() throws Exception;
}
