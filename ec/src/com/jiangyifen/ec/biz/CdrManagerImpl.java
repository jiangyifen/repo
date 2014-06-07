package com.jiangyifen.ec.biz;

import java.util.List;
import java.util.Set;

import com.jiangyifen.ec.dao.Cdr;
import com.jiangyifen.ec.dao.CdrDao;
import com.jiangyifen.ec.dao.Department;

public class CdrManagerImpl implements CdrManager {
	private CdrDao cdrDao;

	public void setCdrDao(CdrDao cdrDao) {
		this.cdrDao = cdrDao;
	}

	public CdrDao getCdrDao() {
		return cdrDao;
	}

	public Cdr getCdrByUniqueId(String uid) {
		return cdrDao.getCdrByUniqueid(uid);
	}

	public void updateCdr(Cdr cdr) {
		cdrDao.updateCdr(cdr);
	}

	public void rewriteCDR(String uniqueid, String username, String name, String hid, String dpmt, String dst,String fenji,String disposition, String url,String dcontext) {
		Cdr cdr = cdrDao.getCdrByUniqueid(uniqueid);
		if (cdr != null) {
			cdr.setUsername(username);
			cdr.setName(name);
			cdr.setHid(hid);
			cdr.setDst(dst);
			cdr.setDpmt(dpmt);
			cdr.setFenji(fenji);
			cdr.setDisposition(disposition);
			cdr.setUrl(url);
			cdr.setDcontext(dcontext);
			cdrDao.updateCdr(cdr);
		}
	}

	public int getCdrCount(String beginTime, String endTime, String src,
			String dst, String fenji, String direction, String hid, String status,
			String moreThen, String lessThen, String andor, String username,
			Set<Department> dpmts) {
		return cdrDao.getCdrCount(beginTime, endTime, src, dst, fenji,
				direction, hid, status, moreThen, lessThen, andor, username, dpmts);
	}

	public List<Cdr> findCdr(String beginTime, String endTime, String src,
			String dst,String fenji,String direction, String hid, String status, String moreThen,
			String lessThen, String andor, String username, Set<Department> dpmts, int pageSize,
			int pageIndex) {
		return cdrDao.findCdr(beginTime, endTime, src, dst, fenji, direction, hid, status,
				moreThen, lessThen, andor,username, dpmts, pageSize, pageIndex);
	}

	public List<Cdr> findCdr(String dst, int pageSize) {
		return cdrDao.findCdr(dst, pageSize);
	}
	
	public List<Cdr> findCdrByUsername(String username, int pageSize, int pageIndex){
		return cdrDao.findCdrByUsername(username, pageSize, pageIndex);
	}
	
	public List<Cdr> findCdrByDst(String dst, int pageSize, int pageIndex){
		return cdrDao.findCdrByDst(dst, pageSize, pageIndex);
	}

	public List<Cdr> findCdrByHIDSrcOrDst(String phoneNumber, String hid, int pageSize,
			int pageIndex) {
		return cdrDao.findCdrByHIDSrcOrDst(phoneNumber, hid, pageSize, pageIndex);
	}

}
