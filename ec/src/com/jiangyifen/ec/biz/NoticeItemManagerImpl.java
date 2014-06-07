package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.NoticeItem;
import com.jiangyifen.ec.dao.NoticeItemDao;

public class NoticeItemManagerImpl implements NoticeItemManager {
	private NoticeItemDao noticeItemDao;

	public void setNoticeItemDao(NoticeItemDao noticeItemDao) {
		this.noticeItemDao = noticeItemDao;
	}

	public NoticeItemDao getNoticeItemDao() {
		return noticeItemDao;
	}

	public NoticeItem get(Long id) {
		return noticeItemDao.get(id);
	}

	public boolean save(NoticeItem o) {
		
		return noticeItemDao.save(o);
	}

	public void update(NoticeItem o) {
		noticeItemDao.update(o);
	}

	public void delete(Long id) {
		noticeItemDao.delete(id);
		
	}

	public void delete(NoticeItem o) {
		noticeItemDao.delete(o);
	}

	public void deleteAllByNoticeid(long id) {
		noticeItemDao.deleteAllByNoticeid(id);
		
	}

	public long getNoticeItemCount() {
		return noticeItemDao.getNoticeItemCount();
	}

	public long getNoticeItemCount(long noticeid) {
		return noticeItemDao.getNoticeItemCount(noticeid);
	}

	public long getNoticeItemCount(boolean read) {
		return noticeItemDao.getNoticeItemCount(read);
	}

	public long getNoticeItemCount(long noticeid, boolean read) {
		return noticeItemDao.getNoticeItemCount(noticeid, read);
	}

	public List<NoticeItem> findAll() {
		return noticeItemDao.findAll();
	}

	public List<NoticeItem> findByPage(int pageSize, int pageIndex) {
		return noticeItemDao.findByPage(pageSize, pageIndex);
	}

	public List<NoticeItem> findByNoticeid(int pageSize, int pageIndex,
			Long noticeid) {
		return noticeItemDao.findByNoticeid(pageSize, pageIndex, noticeid);
	}

	public List<NoticeItem> findByRead(int pageSize, int pageIndex, boolean read) {
		return noticeItemDao.findByRead(pageSize, pageIndex, read);
	}

	public List<NoticeItem> findByNoticeidAndRead(int pageSize, int pageIndex,
			Long noticeid, boolean read) {
		return noticeItemDao.findByNoticeidAndRead(pageSize, pageIndex, noticeid, read);
	}


}
