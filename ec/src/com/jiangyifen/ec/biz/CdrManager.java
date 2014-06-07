package com.jiangyifen.ec.biz;

import java.util.List;
import java.util.Set;

import com.jiangyifen.ec.dao.Cdr;
import com.jiangyifen.ec.dao.Department;

public interface CdrManager {
	Cdr getCdrByUniqueId(String uid);
	void updateCdr(Cdr cdr);
	void rewriteCDR(String uniqueid, String username, String name, String hid, String dpmt, String dst,String fenji,String disposition,String url,String dcontext);
	int getCdrCount(String beginTime, String endTime, String src,
			String dst, String fenji, String direction, String hid, String status, String moreThen,String lessThen,String andor,String username, Set<Department> dpmts);
	List<Cdr> findCdr(String beginTime, String endTime, String src, String dst, String fenji,String direction, String hid, String status,String moreThen,String lessThen,String andor,String username,  Set<Department> dpmts, int pageSize, int pageIndex);
	List<Cdr> findCdr(String dst, int pageSize);
	List<Cdr> findCdrByUsername(String username, int pageSize, int pageIndex);
	List<Cdr> findCdrByDst(String dst, int pageSize, int pageIndex);
	List<Cdr> findCdrByHIDSrcOrDst(String phoneNumber, String hid, int pageSize, int pageIndex);
}
