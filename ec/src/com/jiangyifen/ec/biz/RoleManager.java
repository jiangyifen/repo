package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.Role;

public interface RoleManager {
	boolean addRole(String rolename) throws Exception;

	boolean addRole(Role role) throws Exception;

	void deleteRoles(String[] rolenames) throws Exception;

	void deleteRole(String rolename) throws Exception;

	void updateRole(Role role) throws Exception;

	int getRoleCount() throws Exception;

	Role getRole(String rolename) throws Exception;

	List<Role> getRoles() throws Exception;

	List<Role> getRolesByPage(int pageSize, int pageIndex) throws Exception;

	void test() throws Exception;
}
