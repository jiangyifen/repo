package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.Sip;
import com.jiangyifen.ec.dao.SipDao;

public class SipManagerImpl implements SipManager {
	private SipDao sipDao;

	public void setSipDao(SipDao sipDao) {
		this.sipDao = sipDao;
	}

	public SipDao getSipDao() {
		return sipDao;
	}
	
	public boolean addSip(Sip sip) throws Exception {
		try {
			return sipDao.save(sip);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("新增Sip时出现异常");
		}
	}
	
	public void deleteSips(Long[] id) throws Exception {
		for (Long i : id) {
			sipDao.delete(i);
		}
	}

	public void deleteSip(Long id) throws Exception {
		sipDao.delete(id);
	}

	public void updateSip(Sip sip) throws Exception {
		sipDao.update(sip);
	}

	public int getSipCount() {
		return (int) sipDao.getSipCount();
	}

	public Sip getSip(Long id) {
		return sipDao.get(id);
	}
	
	public List<Sip> getSips() throws Exception {
		return sipDao.findAll();
	}
	
	public List<Sip> getSipsDesc() throws Exception {
		return sipDao.findAllDesc();
	}

	public List<Sip> getSipsByPage(int pageSize, int pageIndex)
			throws Exception {
		return sipDao.findByPage(pageSize, pageIndex);
	}

	public Sip findSipByName(String name) {
		return sipDao.fineByName(name);
	}

}
