package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.DialTask;
import com.jiangyifen.ec.dao.DialTaskDao;

public class DialTaskManagerImpl implements DialTaskManager {
	private DialTaskDao dialTaskDao;

	public void setDialTaskDao(DialTaskDao dialTaskDao) {
		this.dialTaskDao = dialTaskDao;
	}

	public DialTaskDao getDialTaskDao() {
		return dialTaskDao;
	}

	public void delete(long id) {
		dialTaskDao.delete(id);
	}

	public void delete(DialTask o) {
		dialTaskDao.delete(o);
	}

	public void setAutoAssign(long id, boolean autoAssign) {
		DialTask dt = dialTaskDao.get(id);
		if (dt != null) {
			dt.setAutoAssign(autoAssign);
			dialTaskDao.update(dt);
		}
	}

	public List<DialTask> findAll() {
		return dialTaskDao.findAll();
	}

	public List<DialTask> findByStatus(String status) {
		return dialTaskDao.findByStatus(status);
	}

	public List<DialTask> findByAutoAssign(boolean autoAssign) {
		return dialTaskDao.findByAutoAssign(autoAssign);
	}

	public List<DialTask> findByTaskName(String taskName) {
		return dialTaskDao.findByTaskName(taskName);
	}

	public List<DialTask> findByPage(int pageSize, int pageIndex) {
		return dialTaskDao.findByPage(pageSize, pageIndex);
	}

	public DialTask get(long id) {
		return dialTaskDao.get(id);
	}

	public int getDialTaskCount() {
		return (int) dialTaskDao.getDialTaskCount();
	}

	// public long getDialTaskItemCount(long taskid) {
	// return dialTaskDao.getDialTaskItemCount(taskid);
	// }
	//
	// public long getDialTaskItemCount(long taskid, String status) {
	// return dialTaskDao.getDialTaskItemCount(taskid, status);
	// }

	public boolean save(DialTask o) {
		return dialTaskDao.save(o);
	}

	public void update(DialTask o) {
		dialTaskDao.update(o);
	}

	public void setPerorityToZero(long id) {
		DialTask dialTask = dialTaskDao.get(id);
		if (dialTask == null) {
			return;
		}

		String queueName = dialTask.getQueueName();
		List<DialTask> dialTaskList = dialTaskDao.findByQueueName(queueName);
		for (DialTask dt : dialTaskList) {
			if (dt.getId() == id) {
				dt.setPerority(0);
			} else {
				dt.setPerority(1);
			}
			dialTaskDao.update(dt);
		}
	}

	public List<DialTask> findByQueueName(String queueName) {
		return dialTaskDao.findByQueueName(queueName);
	}

}
