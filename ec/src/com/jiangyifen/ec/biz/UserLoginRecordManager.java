package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.UserLoginRecord;

public interface UserLoginRecordManager {
	
	void save(UserLoginRecord alr);

	UserLoginRecord findLastULRByUsername(String username);
	
}
