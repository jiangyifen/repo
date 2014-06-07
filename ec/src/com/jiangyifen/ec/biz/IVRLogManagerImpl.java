package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.IVRLog;
import com.jiangyifen.ec.dao.IVRLogDao;

public class IVRLogManagerImpl implements IVRLogManager {
	private IVRLogDao ivrLogDao;

	public void setIvrLogDao(IVRLogDao ivrLogDao) {
		this.ivrLogDao = ivrLogDao;
	}

	@Override
	public void save(IVRLog o) {
		ivrLogDao.save(o);
	}

	@Override
	public long getCountByNode(String node) {
		return ivrLogDao.getCountByNode(node);
	}

}
