package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.Role;
import com.jiangyifen.ec.dao.RoleDao;

public class RoleManagerImpl implements RoleManager {
	private RoleDao roleDao;

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public RoleDao getRoleDao() {
		return roleDao;
	}

	public boolean addRole(Role role) throws Exception {
		try {
			return roleDao.save(role);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("新增role时出现异常");
		}
	}

	public boolean addRole(String rolename) throws Exception {

		Role u = new Role();
		u.setRolename(rolename);
		return addRole(u);

	}

	public void deleteRoles(String[] rolenames) throws Exception {
		for (String rolename : rolenames) {
			roleDao.delete(rolename);
		}
	}

	public void deleteRole(String rolename) throws Exception {
		roleDao.delete(rolename);
	}

	public void updateRole(Role role) throws Exception {
		roleDao.update(role);
	}

	public int getRoleCount() {
		return (int) roleDao.getRoleCount();
	}

	public Role getRole(String rolename) {
		return roleDao.get(rolename);
	}

	public List<Role> getRoles() throws Exception {
		return roleDao.findAll();
	}

	public List<Role> getRolesByPage(int pageSize, int pageIndex)
			throws Exception {
		return roleDao.findByPage(pageSize, pageIndex);
	}

	public void test() {
		roleDao.test();
	}

}
