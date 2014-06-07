package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.UserLoginRecord;
import com.jiangyifen.ec.dao.UserLoginRecordDao;

public class UserLoginRecordManagerImpl implements UserLoginRecordManager {
	private UserLoginRecordDao userLoginRecordDao;

	public void setUserLoginRecordDao(UserLoginRecordDao userLoginRecordDao) {
		this.userLoginRecordDao = userLoginRecordDao;
	}

	public UserLoginRecordDao getAgentLoginRecordDao() {
		return userLoginRecordDao;
	}
	
	public UserLoginRecord findLastULRByUsername(String username) {
		return userLoginRecordDao.findLastULRByUsername(username);
	}

	public void save(UserLoginRecord alr) {
		userLoginRecordDao.save(alr);
	}


}
