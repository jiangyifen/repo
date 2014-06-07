package com.jiangyifen.ec.dao;

public interface IVRLogDao {

	void save(IVRLog o);
	
	long getCountByNode(String customerPhoneNumber);
	

}
