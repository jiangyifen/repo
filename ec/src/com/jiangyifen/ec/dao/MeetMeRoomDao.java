package com.jiangyifen.ec.dao;

import java.util.List;

public interface MeetMeRoomDao {
	MeetMeRoom get(String confno);
	void add(MeetMeRoom o);
	void update(MeetMeRoom o);
	void delete(String confno);
	void delete(MeetMeRoom o);
	
	long getCount();
	
	List<MeetMeRoom> findAll();
	List<MeetMeRoom> findByPage(int pageSize,int pageIndex);
	MeetMeRoom findByConfno(String confno);

}
