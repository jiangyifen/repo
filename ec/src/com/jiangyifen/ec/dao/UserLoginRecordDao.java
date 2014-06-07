package com.jiangyifen.ec.dao;

public interface UserLoginRecordDao {

	void save(UserLoginRecord alr);

	UserLoginRecord findLastULRByUsername(String username);

}
