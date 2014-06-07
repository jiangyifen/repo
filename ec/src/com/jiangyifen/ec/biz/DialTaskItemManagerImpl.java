package com.jiangyifen.ec.biz;

import java.util.List;
import java.util.Set;

import com.jiangyifen.ec.dao.DialTaskItem;
import com.jiangyifen.ec.dao.DialTaskItemDao;
import com.jiangyifen.ec.dao.User;

public class DialTaskItemManagerImpl implements DialTaskItemManager {
	private DialTaskItemDao dialTaskItemDao;

	public void setDialTaskItemDao(DialTaskItemDao dialTaskItemDao) {
		this.dialTaskItemDao = dialTaskItemDao;
	}

	public DialTaskItemDao getDialTaskItemDao() {
		return dialTaskItemDao;
	}
	
	public void delete(Long id) {
		dialTaskItemDao.delete(id);
	}

	public void delete(DialTaskItem o) {
		dialTaskItemDao.delete(o);
	}

	public void deleteAllByTaskId(long id){
		dialTaskItemDao.deleteAllByTaskId(id);
	}
	
	public List<DialTaskItem> findAll() {
		return dialTaskItemDao.findAll();
	}

	public List<DialTaskItem> findByPage(int pageSize, int pageIndex) {
		return dialTaskItemDao.findByPage(pageSize, pageIndex);
	}

	public List<DialTaskItem> findByTaskId(int pageSize,
			int pageIndex, Long taskId) {
		return dialTaskItemDao.findByTaskId(pageSize, pageIndex, taskId);
	}
	
	public List<DialTaskItem> findByTaskIdAndStatus(int pageSize,
			int pageIndex, Long taskId, String status) {
		return dialTaskItemDao.findByTaskIdAndStatus(pageSize, pageIndex, taskId, status);
	}
	
	public List<DialTaskItem> findByTaskIdAndStatusHasSmReply(int pageSize,
			int pageIndex, Long taskId, String status) {
		return dialTaskItemDao.findByTaskIdAndStatusHasSmReply(pageSize, pageIndex, taskId, status);
	}
	
	public List<DialTaskItem> findByPhoneNumberAndStatus(int pageSize,
			int pageIndex, String phonenumber, String status) {
		return dialTaskItemDao.findByPhoneNumberAndStatus(pageSize, pageIndex, phonenumber, status);
	}

	public DialTaskItem get(Long id) {
		return dialTaskItemDao.get(id);
	}

	public long getDialTaskItemCount() {
		return dialTaskItemDao.getDialTaskItemCount();
	}

	public long getDialTaskItemCount(long taskId) {
		return dialTaskItemDao.getDialTaskItemCount(taskId);
	}

	public long getDialTaskItemCount(Set<User> users, String status) {
		return dialTaskItemDao.getDialTaskItemCount(users, status);
	}
	
	public long getDialTaskItemCount(long taskId, String status) {
		return dialTaskItemDao.getDialTaskItemCount(taskId, status);
	}

	public boolean save(DialTaskItem o) {
		return dialTaskItemDao.save(o);
	}

	public void update(DialTaskItem o) {
		dialTaskItemDao.update(o);
	}

	public void updateSmReplyByPhoneNumber(String phoneNumber) {
		dialTaskItemDao.updateSmReplyByPhoneNumber(phoneNumber);
	}


}
