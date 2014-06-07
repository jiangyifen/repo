package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.MeetMeRoom;
import com.jiangyifen.ec.dao.MeetMeRoomDao;

public class MeetMeRoomManagerImpl implements MeetMeRoomManager {
	private MeetMeRoomDao meetMeRoomDao;

	public void setMeetMeRoomDao(MeetMeRoomDao meetMeRoomDao) {
		this.meetMeRoomDao = meetMeRoomDao;
	}

	public MeetMeRoomDao getMeetMeRoomDao() {
		return meetMeRoomDao;
	}

	public MeetMeRoom get(String confno) throws Exception {
		return meetMeRoomDao.get(confno);
	}

	public void add(MeetMeRoom o) throws Exception {
		meetMeRoomDao.add(o);
		
	}

	public void delete(String confno) throws Exception {
		meetMeRoomDao.delete(confno);
		
	}

	public void update(MeetMeRoom o) throws Exception {
		meetMeRoomDao.update(o);
		
	}

	public long getCount() throws Exception {
		return meetMeRoomDao.getCount();
	}

	public List<MeetMeRoom> findAll() throws Exception {
		return meetMeRoomDao.findAll();
	}

	public List<MeetMeRoom> findByPage(int pageSize, int pageIndex)
			throws Exception {
		return meetMeRoomDao.findByPage(pageSize, pageIndex);
	}

	public MeetMeRoom findByConfno(String confno) throws Exception {
		return meetMeRoomDao.findByConfno(confno);
	}

}
