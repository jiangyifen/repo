package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.RoleAction;
import com.jiangyifen.ec.dao.RoleActionDao;

public class RoleActionManagerImpl implements RoleActionManager {
	private RoleActionDao roleActionDao;

	public void setRoleActionDao(RoleActionDao roleActionDao) {
		this.roleActionDao = roleActionDao;
	}

	public RoleActionDao getRoleActionDao() {
		return roleActionDao;
	}

	public boolean addRoleAction(RoleAction roleAction) throws Exception {
		try {
			return roleActionDao.save(roleAction);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("新增RoleAction时出现异常");
		}
	}
	
	public boolean addRoleAction(String roleactionname) throws Exception {
			RoleAction d = new RoleAction();
			d.setRoleactionname(roleactionname);
			return addRoleAction(d);
	}

	public void deleteRoleActions(String[] roleactionnames) throws Exception {
		for (String roleactionname : roleactionnames) {
			roleActionDao.delete(roleactionname);
		}
	}

	public void deleteRoleAction(String roleactionname) throws Exception {
		roleActionDao.delete(roleactionname);
	}
	
	public void deleteRoleAction(RoleAction roleAction) throws Exception {
		roleActionDao.delete(roleAction);
	}

	public void updateRoleAction(RoleAction roleAction) throws Exception {
		roleActionDao.update(roleAction);
	}

	public int getRoleActionCount() {
		return (int) roleActionDao.getRoleActionCount();
	}

	public RoleAction getRoleAction(String roleactionname) {
		return roleActionDao.get(roleactionname);
	}

	public List<RoleAction> getRoleActions() throws Exception {
		return roleActionDao.findAll();
	}

	public List<RoleAction> getRoleActionsByPage(int pageSize, int pageIndex)
			throws Exception {
		return roleActionDao.findByPage(pageSize, pageIndex);
	}
}
