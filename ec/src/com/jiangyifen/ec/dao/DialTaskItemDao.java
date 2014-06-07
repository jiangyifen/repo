package com.jiangyifen.ec.dao;

import java.util.List;
import java.util.Set;

public interface DialTaskItemDao {
	DialTaskItem get(Long id);
	boolean save(DialTaskItem o);
	void update(DialTaskItem o);
	void delete(Long id);
	void delete(DialTaskItem o);
	
	void deleteAllByTaskId(long id);
	
	long getDialTaskItemCount();
	long getDialTaskItemCount(long taskid);
	long getDialTaskItemCount(Set<User> users, String status);
	long getDialTaskItemCount(long taskid, String status);
	
	
	List<DialTaskItem> findAll();

	List<DialTaskItem> findByPage(int pageSize,int pageIndex);
	List<DialTaskItem> findByTaskId(int pageSize,int pageIndex,Long taskId);
	List<DialTaskItem> findByStatus(int pageSize,int pageIndex, String status);
	List<DialTaskItem> findByTaskIdAndStatus(int pageSize,int pageIndex,Long taskId, String status);
	List<DialTaskItem> findByTaskIdAndStatusHasSmReply(int pageSize,int pageIndex,Long taskId, String status);
	List<DialTaskItem> findByHasSmReplyAndStatus(int pageSize,int pageIndex,boolean hasSmReply, String status);
	List<DialTaskItem> findByPhoneNumberAndStatus(int pageSize,int pageIndex,String phoneNumber, String status);
	void updateSmReplyByPhoneNumber(String phoneNumber);
}
