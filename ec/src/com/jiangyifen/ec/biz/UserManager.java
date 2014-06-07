package com.jiangyifen.ec.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.User;

public interface UserManager {
//	boolean addUser(String username, String password, String email)
//			throws Exception;

	boolean addUser(User user) throws Exception;

	void deleteUsers(String[] users) throws Exception;

	void deleteUser(String user) throws Exception;

	void updateUser(User user) throws Exception;

	int getUserCount() throws Exception;
	int getUserCountByCondition(String condition);
	
	User getUser(String username) throws Exception;

	List<User> getUsers() throws Exception;

	List<User> getUsersByPage(int pageSize, int pageIndex) throws Exception;
	
	List<User> getUsersByCondition(int pageSize, int pageIndex,String condition) throws Exception;
	
	List<User> getUsersByDepartments(Set<Department> dpmts);
	
	List<User> getUsersByDepartments(ArrayList<String> departmentNames);

	boolean loginValid(String username, String password) throws Exception;

	boolean validateName(String username) throws Exception;

}
