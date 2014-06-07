package com.jiangyifen.ec.dao;


public interface CustomerManagerDao {
	
	void update(CustomerManager customerManager);
	CustomerManager get(String customerManagerLoginName);

}
