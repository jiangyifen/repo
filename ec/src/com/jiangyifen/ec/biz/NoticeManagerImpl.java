package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.Notice;
import com.jiangyifen.ec.dao.NoticeDao;


public class NoticeManagerImpl implements NoticeManager {
	private NoticeDao noticeDao;

	public void setNoticeDao(NoticeDao noticeDao) {
		this.noticeDao = noticeDao;
	}

	public NoticeDao getNoticeDao() {
		return noticeDao;
	}

	public Notice get(Long id) {
		return noticeDao.get(id);
	}

	public boolean save(Notice o) {
		return noticeDao.save(o);
	}

	public void update(Notice o) {
		noticeDao.update(o);
	}

	public void delete(Long id) {
		noticeDao.delete(id);
	}

	public void delete(Notice o) {
		noticeDao.delete(o);
	}

	public long getCount() {
		return noticeDao.getCount();
	}

	public long getCount(String beginTime, String endTime, String title,
			String content) {
		return noticeDao.getCount(beginTime, endTime, title, content);
	}

	public List<Notice> findAll() {
		return noticeDao.findAll();
	}

	public List<Notice> find(int pageSize, int pageIndex) {
		return noticeDao.find(pageSize, pageIndex);
	}

	public List<Notice> find(int pageSize, int pageIndex, String beginTime,
			String endTime, String title, String content) {
		return noticeDao.find(pageSize, pageIndex, beginTime, endTime, title, content);
	}



}
