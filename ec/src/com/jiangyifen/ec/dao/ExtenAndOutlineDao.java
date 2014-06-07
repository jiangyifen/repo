package com.jiangyifen.ec.dao;

import java.util.List;

public interface ExtenAndOutlineDao {
	ExtenAndOutline get(String exten);
	void save(ExtenAndOutline o);
	void update(ExtenAndOutline o);
	void delete(ExtenAndOutline o);
	void delete(String exten);
	
	long getExtenAndOutlineCount();
	
	List<ExtenAndOutline> findAll();
	List<ExtenAndOutline> findByPage(int pageSize,int pageIndex);

}
