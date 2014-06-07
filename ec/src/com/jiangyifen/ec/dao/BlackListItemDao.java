package com.jiangyifen.ec.dao;

import java.util.List;

public interface BlackListItemDao {
	BlackListItem get(Long id);
	boolean save(BlackListItem o);
	void update(BlackListItem o);
	void delete(Long id);
	void delete(BlackListItem o);
	
	int getBlackListItemCount(String phoneNumber, String type);
	
	List<BlackListItem> findAll();
	List<BlackListItem> findAllByType(String type1, String type2);
	List<BlackListItem> findByPage(int pageSize,int pageIndex);
	List<BlackListItem> findByType(int pageSize,int pageIndex, String type);
	List<BlackListItem> findByTypeAndPhoneNumber(int pageSize, int pageIndex, String type, String phoneNumber);
}
