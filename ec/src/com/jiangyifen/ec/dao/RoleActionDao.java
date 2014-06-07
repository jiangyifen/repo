package com.jiangyifen.ec.dao;

import java.util.List;

public interface RoleActionDao {
	RoleAction get(String roleactionName);
	boolean save(RoleAction roleAction);
	void update(RoleAction roleAction);
	void delete(String roleactionName);
	void delete(RoleAction roleAction);
	
	long getRoleActionCount();
	
	List<RoleAction> findAll();
	List<RoleAction> findByPage(int pageSize,int pageIndex);
	RoleAction finaRoleActionByName(String roleactionName);

}
