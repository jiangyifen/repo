package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.MyCustomerLog;

public interface MyCustomerLogManager {
	
	void save(MyCustomerLog o);

	long getMyCustomerLogCountByCustomerPhoneNumber(String customerPhoneNumber);
	
	long getMyCustomerLogCount(long taskid);
}
