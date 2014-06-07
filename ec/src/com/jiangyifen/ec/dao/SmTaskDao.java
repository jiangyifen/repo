package com.jiangyifen.ec.dao;

import java.util.List;


public interface SmTaskDao {
	SmTask get(Long id);
	SmTask get(String msgid);
	boolean save(SmTask smTask);
	void update(SmTask smTask);
	void delete(Long id);
	void delete(SmTask smTask);
	
	long getSmTaskCount();
	long getSmTaskCount(String accountId);
	long getSmTaskCount(String beginTime, String endTime, String content,String mobile,String status,String senderId,String batchNumber);

	
	List<SmTask> findAll();
	List<SmTask> findByPage(int pageSize,int pageIndex);
	List<SmTask> findByStatus(int pageSize,int pageIndex,String status);
	List<SmTask> findByStatusAndUserfield(int pageSize,int pageIndex,String status, String userfield);
	List<SmTask> findByAccountId(int pageSize,int pageIndex,String accountId);
	List<SmTask> findByDateAndStatus(int pageSize,int pageIndex,String beginTime, String endTime, String status);
	
	List<SmTask> findSmTask(int pageSize, int pageIndex,String beginTime, String endTime, String content,String mobile,String status,String senderId,String batchNumber);
}
