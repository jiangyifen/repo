package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.HoldEventLog;
import com.jiangyifen.ec.dao.HoldEventLogDao;;

public class HoldEventLogManagerImpl implements HoldEventLogManager {
	private HoldEventLogDao holdEventLogDao;

	public void setHoldEventLogDao(HoldEventLogDao dao) {
		this.holdEventLogDao = dao;
	}

	public HoldEventLogDao getQueueMemberPauseLogDao() {
		return holdEventLogDao;
	}
	
	public HoldEventLog findLastLogByUniqueid(String uniqueid) {
		return holdEventLogDao.findLastLogByUniqueid(uniqueid);
	}

	public void save(HoldEventLog log) {
		holdEventLogDao.save(log);
	}
	
	public void update(HoldEventLog log) {
		holdEventLogDao.update(log);
	}


}
