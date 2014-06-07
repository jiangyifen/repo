package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.MyCustomerLog;
import com.jiangyifen.ec.dao.MyCustomerLogDao;

public class MyCustomerLogManagerImpl implements MyCustomerLogManager {
	private MyCustomerLogDao myCustomerLogDao;

	public void setMyCustomerLogDao(MyCustomerLogDao myCustomerLogDao) {
		this.myCustomerLogDao = myCustomerLogDao;
	}

	public void save(MyCustomerLog o) {
		myCustomerLogDao.save(o);
	}

	public long getMyCustomerLogCount(long taskid) {
		return myCustomerLogDao.getMyCustomerLogCount(taskid);
	}

	public long getMyCustomerLogCountByCustomerPhoneNumber(
			String customerPhoneNumber) {
		return myCustomerLogDao.getMyCustomerLogCountByCustomerPhoneNumber(customerPhoneNumber);
	}


}
