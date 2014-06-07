package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.CustomerPhoneNum;
import com.jiangyifen.ec.dao.CustomerPhoneNumDao;

public class CustomerPhoneNumManagerImpl implements CustomerPhoneNumManager {
	private CustomerPhoneNumDao customerPhoneNumDao;

	public void setCustomerPhoneNumDao(CustomerPhoneNumDao customerPhoneNumDao) {
		this.customerPhoneNumDao = customerPhoneNumDao;
	}

	public CustomerPhoneNumDao getCustomerPhoneNumDao() {
		return customerPhoneNumDao;
	}

	public CustomerPhoneNum get(String customerPhoneNumber) throws Exception {
		return customerPhoneNumDao.get(customerPhoneNumber);
	}
}
