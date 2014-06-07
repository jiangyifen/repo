package com.jiangyifen.ec.dao;

import java.util.List;
import java.util.Set;

public interface CdrDao {
	Cdr getCdrByUniqueid(String uniqueid);
	
	void updateCdr(Cdr cdr);

	int getCdrCount(String beginTime, String endTime, String src, String dst,String fenji,
			String direction, String hid, String status, String moreThen,String lessThen,String andor,String username,Set<Department> dpmts);

	List<Cdr> findCdr(String dst, int pageSize);
	
	List<Cdr> findCdr(String beginTime, String endTime, String src, String dst,String fenji,
			String direction, String hid, String status, String moreThen,String lessThen,String andor,String username,Set<Department> dpmts, int pageSize, int pageIndex);
	
	List<Cdr> findCdrByUsername(String username, int pageSize, int pageIndex);
	List<Cdr> findCdrByDst(String dst, int pageSize, int pageIndex);
	List<Cdr> findCdrByHIDSrcOrDst(String phoneNumber, String hid, int pageSize, int pageIndex);
}
