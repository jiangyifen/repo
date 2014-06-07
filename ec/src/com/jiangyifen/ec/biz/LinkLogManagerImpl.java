package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.LinkLog;
import com.jiangyifen.ec.dao.LinkLogDao;

public class LinkLogManagerImpl implements LinkLogManager {
	private LinkLogDao linkLogDao;

	public void setLinkLogDao(LinkLogDao linkLogDao) {
		this.linkLogDao = linkLogDao;
	}

	@Override
	public void save(LinkLog o) {
		linkLogDao.save(o);
	}

}
