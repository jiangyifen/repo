package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.DialTaskLog;

public interface DialTaskLogManager {
	
	void save(DialTaskLog dtl);
	void update(DialTaskLog dtl);
	
	DialTaskLog getByDialTaskId(long id);
	Long getDialTaskLogCount();
	
	List<DialTaskLog> getDialTaskLogs(int pageSize, int pageIndex);
	List<DialTaskLog> getDialTaskLogs(String dialTaskName, String beginTime, String endTime, int pageSize, int pageIndex);
}
