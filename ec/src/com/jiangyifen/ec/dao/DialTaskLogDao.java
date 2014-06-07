package com.jiangyifen.ec.dao;

import java.util.List;

public interface DialTaskLogDao {

	void save(DialTaskLog dtl);
	
	void update(DialTaskLog dtl);
	
	Long getDialTaskLogCount();
	
	DialTaskLog getByDialTaskId(long id);
	
	List<DialTaskLog> getDialTaskLogs(int pageSize, int pageIndex);
	List<DialTaskLog> getDialTaskLogs(String dialTaskName, String beginTime, String endTime, int pageSize, int pageIndex);

}
