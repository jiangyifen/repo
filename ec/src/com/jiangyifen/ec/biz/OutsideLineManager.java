package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.OutsideLine;

public interface OutsideLineManager {
	
	void save(String phoneNumber) throws Exception;
	
	void save(OutsideLine o) throws Exception;

	void delete(String phoneNumber) throws Exception;

	void delete(String[] phoneNumber) throws Exception;

	void update(OutsideLine o) throws Exception;
	
	OutsideLine get(String phoneNumber) throws Exception;
	
	int getCount() throws Exception;

	List<OutsideLine> getAll() throws Exception;

	List<OutsideLine> getByPage(int pageSize, int pageIndex) throws Exception;

}
