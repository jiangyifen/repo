package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.BlackListItem;
import com.jiangyifen.ec.dao.BlackListItemDao;

public class BlackListItemManagerImpl implements BlackListItemManager {
	private BlackListItemDao blackListItemDao;

	public BlackListItemDao getBlackListItemDao() {
		return blackListItemDao;
	}

	public void setBlackListItemDao(BlackListItemDao blackListItemDao) {
		this.blackListItemDao = blackListItemDao;
	}

	public BlackListItem get(Long id) {
		return blackListItemDao.get(id);
	}

	public boolean save(BlackListItem o) {
		return blackListItemDao.save(o);
	}

	public void update(BlackListItem o) {
		blackListItemDao.update(o);
	}

	public void delete(Long id) {
		blackListItemDao.delete(id);
	}

	public void delete(BlackListItem o) {
		blackListItemDao.delete(o);
	}

	public List<BlackListItem> findAll() {
		return blackListItemDao.findAll();
	}

	public List<BlackListItem> findAllByType(String type1, String type2) {
		return blackListItemDao.findAllByType(type1, type2);
	}
	

	public List<BlackListItem> findByPage(int pageSize, int pageIndex) {
		return blackListItemDao.findByPage(pageSize, pageIndex);
	}

	public List<BlackListItem> findByType(int pageSize, int pageIndex,
			String type) {
		return blackListItemDao.findByType(pageSize, pageIndex, type);
	}

	public List<BlackListItem> findByTypeAndPhoneNumber(int pageSize,
			int pageIndex, String type, String phoneNumber) {
		return blackListItemDao.findByTypeAndPhoneNumber(pageSize, pageIndex, type, phoneNumber);
	}

	public int getBlackListItemCount(String phoneNumber, String type) {
		return blackListItemDao.getBlackListItemCount(phoneNumber, type);
	}

}