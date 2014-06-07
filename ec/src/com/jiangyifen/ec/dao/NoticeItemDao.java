package com.jiangyifen.ec.dao;

import java.util.List;

public interface NoticeItemDao {
	NoticeItem get(Long id);
	boolean save(NoticeItem o);
	void update(NoticeItem o);
	void delete(Long id);
	void delete(NoticeItem o);
	
	void deleteAllByNoticeid(long id);
	
	long getNoticeItemCount();
	long getNoticeItemCount(long noticeid);
	long getNoticeItemCount(boolean read);
	long getNoticeItemCount(long noticeid, boolean read);
	
	
	List<NoticeItem> findAll();

	List<NoticeItem> findByPage(int pageSize,int pageIndex);
	List<NoticeItem> findByNoticeid(int pageSize,int pageIndex,Long noticeid);
	List<NoticeItem> findByRead(int pageSize,int pageIndex, boolean read);
	List<NoticeItem> findByNoticeidAndRead(int pageSize,int pageIndex, Long noticeid, boolean read);

}
