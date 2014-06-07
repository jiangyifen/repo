package com.jiangyifen.ec.dao;

import java.util.List;

public interface OutsideLineDao {
	OutsideLine get(String phoneNumber);
	void save(OutsideLine o);
	void update(OutsideLine o);
	void delete(OutsideLine o);
	void delete(String phoneNumber);
	
	long getOutsideLineCount();
	
	List<OutsideLine> findAll();
	List<OutsideLine> findByPage(int pageSize,int pageIndex);

}
