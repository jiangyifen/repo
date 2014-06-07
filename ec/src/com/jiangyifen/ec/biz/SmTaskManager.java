package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.SmTask;

public interface SmTaskManager {

	boolean addSmTask(SmTask smTask) throws Exception;

	void deleteSmTasks(Long[] ids) throws Exception;

	void deleteSmTask(Long id) throws Exception;
	
	void deleteSmTask(SmTask smTask) throws Exception;

	void updateSmTask(SmTask smTask) throws Exception;



	long getSmTaskCount() throws Exception;
	long getSmTaskCount(String accountId) throws Exception;
	long getSmTaskCount(String beginTime, String endTime, String content,String mobile,String status,String senderId,String batchNumber) throws Exception;

	
	SmTask getSmTask(Long id) throws Exception;
	
	SmTask getSmTask(String msgid) throws Exception;

	List<SmTask> getSmTasks() throws Exception;

	List<SmTask> getSmTasksByPage(int pageSize, int pageIndex) throws Exception;
	
	List<SmTask> getSmTasksByStatus(int pageSize, int pageIndex, String status) throws Exception;
	
	List<SmTask> getSmTasksByStatusAndUserfield(int pageSize, int pageIndex, String status, String userfield) throws Exception;
	
	List<SmTask> findByAccountId(int pageSize,int pageIndex,String accountId);
	
	List<SmTask> getSmTasksByDateAndStatus(int pageSize, int pageIndex, String beginTime, String endTime, String status) throws Exception;

	List<SmTask> findSmTask(int pageSize, int pageIndex,String beginTime, String endTime, String content,String mobile,String status,String senderId,String batchNumber);

}
