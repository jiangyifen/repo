package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.SmTask;
import com.jiangyifen.ec.dao.SmTaskDao;

public class SmTaskManagerImpl implements SmTaskManager {
	private SmTaskDao smTaskDao;

	public void setSmTaskDao(SmTaskDao smTaskDao) {
		this.smTaskDao = smTaskDao;
	}

	public SmTaskDao getSmTaskDao() {
		return smTaskDao;
	}

	public boolean addSmTask(SmTask smTask) {

			return smTaskDao.save(smTask);

	}

	public void deleteSmTasks(Long[] ids) throws Exception {
		for (Long id : ids) {
			smTaskDao.delete(id);
		}
	}

	public void deleteSmTask(Long id) throws Exception {
		smTaskDao.delete(id);
	}

	public void deleteSmTask(SmTask smTask) throws Exception {
		smTaskDao.delete(smTask);
	}

	public long getSmTaskCount() {
		return smTaskDao.getSmTaskCount();
	}

	public SmTask getSmTask(Long id) {
		return smTaskDao.get(id);
	}

	public SmTask getSmTask(String msgid) throws Exception {
		return smTaskDao.get(msgid);
	}

	public void updateSmTask(SmTask smTask) throws Exception {
		smTaskDao.update(smTask);
	}

	public List<SmTask> getSmTasks() throws Exception {
		return smTaskDao.findAll();
	}

	public List<SmTask> getSmTasksByPage(int pageSize, int pageIndex)
			throws Exception {
		return smTaskDao.findByPage(pageSize, pageIndex);
	}

	public List<SmTask> getSmTasksByStatus(int pageSize, int pageIndex,
			String status) throws Exception {
		return smTaskDao.findByStatus(pageSize, pageIndex, status);
	}
	
	public List<SmTask> getSmTasksByStatusAndUserfield(int pageSize, int pageIndex,
			String status, String userfield) throws Exception {
		return smTaskDao.findByStatusAndUserfield(pageSize, pageIndex, status, userfield);
	}

	public List<SmTask> getSmTasksByDateAndStatus(int pageSize, int pageIndex,
			String beginTime, String endTime, String status) throws Exception {
		return smTaskDao.findByDateAndStatus(pageSize, pageIndex, beginTime,
				endTime, status);
	}

	public List<SmTask> findByAccountId(int pageSize, int pageIndex,
			String accountId) {
		return smTaskDao.findByAccountId(pageSize, pageIndex, accountId);
	}

	public long getSmTaskCount(String accountId) throws Exception {
		return smTaskDao.getSmTaskCount(accountId);
	}

	public long getSmTaskCount(String beginTime, String endTime,
			String content, String mobile, String status, String senderId,
			String batchNumber) throws Exception {
		return smTaskDao.getSmTaskCount(beginTime, endTime, content, mobile,
				status, senderId, batchNumber);
	}

	public List<SmTask> findSmTask(int pageSize, int pageIndex,
			String beginTime, String endTime, String content, String mobile,
			String status, String senderId, String batchNumber) {
		return smTaskDao.findSmTask(pageSize, pageIndex, beginTime, endTime,
				content, mobile, status, senderId, batchNumber);
	}

}
