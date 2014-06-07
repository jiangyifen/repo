package com.jiangyifen.ec.biz;

import java.util.List;
import java.util.Set;

import com.jiangyifen.ec.dao.DialTaskItem;
import com.jiangyifen.ec.dao.User;

public interface DialTaskItemManager {
	DialTaskItem get(Long id);
	boolean save(DialTaskItem o);
	void update(DialTaskItem o);
	void delete(Long id);
	void delete(DialTaskItem o);
	
	void deleteAllByTaskId(long id);
	
	long getDialTaskItemCount();
	long getDialTaskItemCount(long taskId);
	long getDialTaskItemCount(Set<User> users, String status);
	long getDialTaskItemCount(long taskId, String status);
	
	
	List<DialTaskItem> findAll();
	List<DialTaskItem> findByPage(int pageSize,int pageIndex);
	List<DialTaskItem> findByTaskId(int pageSize, int pageIndex, Long taskId);
	List<DialTaskItem> findByTaskIdAndStatus(int pageSize, int pageIndex, Long taskId, String status);
	List<DialTaskItem> findByTaskIdAndStatusHasSmReply(int pageSize, int pageIndex, Long taskId, String status);
	List<DialTaskItem> findByPhoneNumberAndStatus(int pageSize, int pageIndex, String phoneNumber, String status);

	void updateSmReplyByPhoneNumber(String phoneNumber);
}
