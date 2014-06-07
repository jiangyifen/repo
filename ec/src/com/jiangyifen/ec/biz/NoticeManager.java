package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.Notice;


public interface NoticeManager {
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
