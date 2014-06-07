package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.SmUserInfo;

public interface SmUserInfoManager {

	boolean addSmUserInfo(SmUserInfo smUserInfo) throws Exception;

	void deleteSmUserInfos(String[] accountIds) throws Exception;

	void deleteSmUserInfo(String accountId) throws Exception;

	void updateSmUserInfo(SmUserInfo smUserInfo) throws Exception;

	int getSmUserInfoCount() throws Exception;

	SmUserInfo getSmUserInfo(String accountId) throws Exception;

	List<SmUserInfo> getSmUserInfos() throws Exception;

	List<SmUserInfo> getSmUserInfosByPage(int pageSize, int pageIndex) throws Exception;


}
