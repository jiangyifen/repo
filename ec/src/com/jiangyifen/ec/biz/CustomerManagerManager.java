package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.CustomerManager;

public interface CustomerManagerManager {

	public CustomerManager get(String customerManagerLoginName) throws Exception;
	public void update(CustomerManager customerManager) throws Exception;

}
