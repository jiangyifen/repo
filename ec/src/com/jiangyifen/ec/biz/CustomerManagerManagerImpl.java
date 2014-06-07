package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.CustomerManager;
import com.jiangyifen.ec.dao.CustomerManagerDao;

public class CustomerManagerManagerImpl implements CustomerManagerManager {
	private CustomerManagerDao customerManagerDao;

	public void setCustomerManagerDao(CustomerManagerDao customerManagerDao) {
		this.customerManagerDao = customerManagerDao;
	}

	public CustomerManagerDao getCustomerManagerDao() {
		return customerManagerDao;
	}

	public CustomerManager get(String customerManagerLoginName) {
		return customerManagerDao.get(customerManagerLoginName);
	}

	public void update(CustomerManager customerManager){
		customerManagerDao.update(customerManager);
	}


}
