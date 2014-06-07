package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.DialTaskLog;
import com.jiangyifen.ec.dao.DialTaskLogDao;

public class DialTaskLogManagerImpl implements DialTaskLogManager {
	private DialTaskLogDao dialTaskLogDao;

	public void setDialTaskLogDao(DialTaskLogDao dialTaskLogDao) {
		this.dialTaskLogDao = dialTaskLogDao;
	}

	public void save(DialTaskLog alr) {
		dialTaskLogDao.save(alr);
	}

	public DialTaskLog getByDialTaskId(long id) {
		return dialTaskLogDao.getByDialTaskId(id);
	}

	public void update(DialTaskLog dtl) {
		dialTaskLogDao.update(dtl);
	}

	public List<DialTaskLog> getDialTaskLogs(int pageSize, int pageIndex) {
		return dialTaskLogDao.getDialTaskLogs(pageSize, pageIndex);
	}

	public List<DialTaskLog> getDialTaskLogs(String dialTaskName,
			String beginTime, String endTime, int pageSize, int pageIndex) {
		return dialTaskLogDao.getDialTaskLogs(dialTaskName, beginTime, endTime,
				pageSize, pageIndex);
	}

	public Long getDialTaskLogCount() {
		return dialTaskLogDao.getDialTaskLogCount();
	}

}
