package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.QueueEntryEventLog;
import com.jiangyifen.ec.dao.QueueEntryEventLogDao;

public class QueueEntryEventLogManagerImpl implements QueueEntryEventLogManager {
	private QueueEntryEventLogDao queueEntryEventLogDao;

	public void setQueueEntryEventLogDao(QueueEntryEventLogDao queueEntryEventLogDao) {
		this.queueEntryEventLogDao = queueEntryEventLogDao;
	}

	public void save(QueueEntryEventLog o) {
		queueEntryEventLogDao.save(o);
	}

}
