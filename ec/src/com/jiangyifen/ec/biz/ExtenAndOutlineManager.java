package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.ExtenAndOutline;

public interface ExtenAndOutlineManager {

	void save(ExtenAndOutline o) throws Exception;

	void delete(String exten) throws Exception;

	void delete(String[] exten) throws Exception;

	void update(ExtenAndOutline o) throws Exception;
	
	ExtenAndOutline get(String exten) throws Exception;
	
	int getCount() throws Exception;

	List<ExtenAndOutline> getAll() throws Exception;

	List<ExtenAndOutline> getByPage(int pageSize, int pageIndex) throws Exception;

}
