package com.jiangyifen.ec.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface UserDao {
	User get(String username);
	boolean save(User user);
	void update(User user);
	void delete(String username);
	void delete(User user);
	
	long getUserCount();
	long getUserCountByCondition(String condition);
	
	List<User> findAll();
	List<User> findByPage(int pageSize,int pageIndex);
	List<User> findByCondition(String condition);
	List<User> findByCondition(int pageSize,int pageIndex,String condition);
	List<User> findByDepartments(Set<Department> dpmts);
	List<User> findByDepartments(ArrayList<String> departmentNames);
	User findUserByNameAndPass(String username, String password);
	User finaUserByName(String username);

}
