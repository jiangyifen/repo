package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.RoleAction;

public interface RoleActionManager {
	boolean addRoleAction(String roleactionname) throws Exception;
	
	boolean addRoleAction(RoleAction roleAction) throws Exception;

	void deleteRoleActions(String[] roleactionnames) throws Exception;

	void deleteRoleAction(String roleactionname) throws Exception;
	
	void deleteRoleAction(RoleAction roleAction) throws Exception;

	void updateRoleAction(RoleAction roleAction) throws Exception;

	int getRoleActionCount() throws Exception;

	RoleAction getRoleAction(String roleactionname) throws Exception;

	List<RoleAction> getRoleActions() throws Exception;

	List<RoleAction> getRoleActionsByPage(int pageSize, int pageIndex)
			throws Exception;

}
