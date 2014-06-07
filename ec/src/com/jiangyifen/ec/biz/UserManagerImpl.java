package com.jiangyifen.ec.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.User;
import com.jiangyifen.ec.dao.UserDao;

public class UserManagerImpl implements UserManager {
	private UserDao userDao;

	public boolean addUser(User user) throws Exception {

		try {

			return userDao.save(user);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public boolean loginValid(String username, String password)
			throws Exception {
		try {
			User u = userDao.findUserByNameAndPass(username, password);
			if (u != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("验证用户登录时出现异常");
		}
		return false;
	}

	public boolean validateName(String username) throws Exception {
		try {
			if (userDao.finaUserByName(username) == null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("验证用户名是否有效出错");
		}
		return false;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public List<User> getUsers() throws Exception {
		return userDao.findAll();
	}

	public List<User> getUsersByPage(int pageSize, int pageIndex)
			throws Exception {
		return userDao.findByPage(pageSize, pageIndex);
	}

	public List<User> getUsersByCondition(int pageSize, int pageIndex,
			String condition) throws Exception {
		return userDao.findByCondition(pageSize, pageIndex,condition);
	}

	public void deleteUsers(String[] users) throws Exception {
		for (String u : users) {
			userDao.delete(u);
		}
	}

	public void deleteUser(String user) throws Exception {
		userDao.delete(user);
	}

	public int getUserCount() {
		return (int) userDao.getUserCount();
	}

	public User getUser(String username) {
		return userDao.get(username);
	}

	public void updateUser(User user) throws Exception {
		userDao.update(user);
	}

	public int getUserCountByCondition(String condition) {
		return (int)userDao.getUserCountByCondition(condition);
	}

	public List<User> getUsersByDepartments(Set<Department> dpmts) {
		return userDao.findByDepartments(dpmts);
	}

	public List<User> getUsersByDepartments(ArrayList<String> departmentNames) {
		return userDao.findByDepartments(departmentNames);
	}

}
