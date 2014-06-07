package com.jiangyifen.ec.dao;

import java.util.List;

public interface RoleDao {
	Role get(String rolename);
	boolean save(Role role);
	void update(Role role);
	void delete(String rolename);
	void delete(Role role);
	
	long getRoleCount();
	
	List<Role> findAll();
	List<Role> findByPage(int pageSize,int pageIndex);
	Role finaRoleByName(String rolename);
	
	void test();
}
