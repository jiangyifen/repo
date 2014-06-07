package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.QueueMemberPauseLog;
import com.jiangyifen.ec.dao.QueueMemberPauseLogDao;

public class QueueMemberPauseLogManagerImpl implements QueueMemberPauseLogManager {
	private QueueMemberPauseLogDao queueMemberPauseLogDao;

	public void setQueueMemberPauseLogDao(QueueMemberPauseLogDao dao) {
		this.queueMemberPauseLogDao = dao;
	}

	public QueueMemberPauseLogDao getQueueMemberPauseLogDao() {
		return queueMemberPauseLogDao;
	}
	
	public QueueMemberPauseLog findLastLogByMemberNameAndQueueName(String memberName, String queueName) {
		return queueMemberPauseLogDao.findLastLogByMemberNameAndQueueName(memberName, queueName);
	}

	public void save(QueueMemberPauseLog log) {
		queueMemberPauseLogDao.save(log);
	}
	
	public void update(QueueMemberPauseLog log) {
		queueMemberPauseLogDao.update(log);
	}


}
