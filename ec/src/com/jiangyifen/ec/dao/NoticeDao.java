package com.jiangyifen.ec.dao;

import java.util.List;


public interface NoticeDao {
	Notice get(Long id);

	boolean save(Notice o);
	void update(Notice o);
	void delete(Long id);
	void delete(Notice o);
	
	long getCount();
	long getCount(String beginTime, String endTime, String title,String content);

	
	List<Notice> findAll();
	List<Notice> find(int pageSize,int pageIndex);
	List<Notice> find(int pageSize,int pageIndex,String beginTime, String endTime, String title,String content);
}
