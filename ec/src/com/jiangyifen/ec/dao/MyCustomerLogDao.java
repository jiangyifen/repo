package com.jiangyifen.ec.dao;

public interface MyCustomerLogDao {

	void save(MyCustomerLog o);
	
	long getMyCustomerLogCountByCustomerPhoneNumber(String customerPhoneNumber);
	
	long getMyCustomerLogCount(long taskid);

}
