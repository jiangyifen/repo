package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.DialTask;

public interface DialTaskManager {
	
	DialTask get(long id);
	boolean save(DialTask o);
	void update(DialTask o);
	void delete(long id);
	void delete(DialTask o);
	
	int getDialTaskCount();
	
	void setAutoAssign(long id, boolean autoAssign);
	void setPerorityToZero(long id);
	
	List<DialTask> findAll();
	List<DialTask> findByStatus(String status);
	List<DialTask> findByAutoAssign(boolean autoAssign);
	List<DialTask> findByTaskName(String taskName);
	List<DialTask> findByQueueName(String queueName);
	List<DialTask> findByPage(int pageSize,int pageIndex);

}
