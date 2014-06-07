package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.QueueCallerAbandonEventLog;
import com.jiangyifen.ec.dao.QueueCallerAbandonEventLogDao;

public class QueueCallerAbandonEventLogManagerImpl implements QueueCallerAbandonEventLogManager {
	private QueueCallerAbandonEventLogDao queueCallerAbandonEventLogDao;

	public void setQueueCallerAbandonEventLogDao(QueueCallerAbandonEventLogDao queueCallerAbandonEventLogDao) {
		this.queueCallerAbandonEventLogDao = queueCallerAbandonEventLogDao;
	}

	public void save(QueueCallerAbandonEventLog o) {
		queueCallerAbandonEventLogDao.save(o);
	}

}
