package com.jiangyifen.ec.dao;

import java.util.List;

public interface DialTaskDao {
	DialTask get(long id);
	boolean save(DialTask o);
	void update(DialTask o);
	void delete(long id);
	void delete(DialTask o);
	
	long getDialTaskCount();
//	
//	long getDialTaskItemCount(long taskid);
//	long getDialTaskItemCount(long taskid, String status);
	
	List<DialTask> findAll();
	List<DialTask> findByStatus(String status);
	List<DialTask> findByAutoAssign(boolean autoAssign);
	List<DialTask> findByQueueName(String queueName);
	List<DialTask> findByTaskName(String taskName);
	List<DialTask> findByPage(int pageSize,int pageIndex);
}
