package com.jiangyifen.ec.dao;

import java.util.List;

public interface SmUserInfoDao {
	SmUserInfo get(String accountId);
	boolean save(SmUserInfo smUserInfo);
	void update(SmUserInfo smUserInfo);
	void delete(String accountId);
	void delete(SmUserInfo smUserInfo);
	
	long getSmUserInfoCount();
	
	List<SmUserInfo> findAll();
	List<SmUserInfo> findByPage(int pageSize,int pageIndex);
}
