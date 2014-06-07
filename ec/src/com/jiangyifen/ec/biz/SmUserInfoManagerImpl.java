package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.SmUserInfo;
import com.jiangyifen.ec.dao.SmUserInfoDao;

public class SmUserInfoManagerImpl implements SmUserInfoManager {
	private SmUserInfoDao smUserInfoDao;

	public void setSmUserInfoDao(SmUserInfoDao smUserInfoDao) {
		this.smUserInfoDao = smUserInfoDao;
	}

	public SmUserInfoDao getSmUserInfoDao() {
		return smUserInfoDao;
	}

	public boolean addSmUserInfo(SmUserInfo smUserInfo) throws Exception {

		try {
			return smUserInfoDao.save(smUserInfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("新增EmppUserInfo时出现异常");
		}
	}

	public void deleteSmUserInfos(String[] accountIds) throws Exception {
		for (String accountId : accountIds) {
			smUserInfoDao.delete(accountId);
		}
	}

	public void deleteSmUserInfo(String accountId) throws Exception {
		smUserInfoDao.delete(accountId);
	}

	public void updateSmUserInfo(SmUserInfo emppUserInfo) throws Exception {
		smUserInfoDao.update(emppUserInfo);
	}
	
	public int getSmUserInfoCount() {
		return (int) smUserInfoDao.getSmUserInfoCount();
	}

	public SmUserInfo getSmUserInfo(String accountId) {
		return smUserInfoDao.get(accountId);
	}



	public List<SmUserInfo> getSmUserInfos() throws Exception {
		return smUserInfoDao.findAll();
	}

	public List<SmUserInfo> getSmUserInfosByPage(int pageSize, int pageIndex)
			throws Exception {
		return smUserInfoDao.findByPage(pageSize, pageIndex);
	}

}
