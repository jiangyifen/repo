package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.MeetMeRoom;

public interface MeetMeRoomManager {
	
	MeetMeRoom get(String confno) throws Exception;

	void add(MeetMeRoom o) throws Exception;

	void delete(String confno) throws Exception;

	void update(MeetMeRoom o) throws Exception;

	long getCount() throws Exception;

	

	List<MeetMeRoom> findAll() throws Exception;

	List<MeetMeRoom> findByPage(int pageSize, int pageIndex) throws Exception;

	MeetMeRoom findByConfno(String confno) throws Exception;

}
