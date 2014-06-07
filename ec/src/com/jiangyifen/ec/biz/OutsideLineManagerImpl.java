package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.OutsideLine;
import com.jiangyifen.ec.dao.OutsideLineDao;

public class OutsideLineManagerImpl implements OutsideLineManager {

	private OutsideLineDao outsideLineDao;

	public void setOutsideLineDao(OutsideLineDao outsideLineDao) {
		this.outsideLineDao = outsideLineDao;
	}

	public void save(String phoneNumber) throws Exception {
		OutsideLine o = new OutsideLine();
		o.setPhoneNumber(phoneNumber);
		save(o);
	}

	public void save(OutsideLine o) throws Exception {
		outsideLineDao.save(o);
	}

	public void delete(String phoneNumber) throws Exception {
		outsideLineDao.delete(phoneNumber);

	}

	public void delete(String[] phoneNumber) throws Exception {
		for (String s : phoneNumber) {
			delete(s);
		}

	}

	public void update(OutsideLine o) throws Exception {
		outsideLineDao.update(o);
	}

	public OutsideLine get(String phoneNumber) throws Exception {
		return outsideLineDao.get(phoneNumber);
	}

	public int getCount() throws Exception {
		return (int) outsideLineDao.getOutsideLineCount();
	}

	public List<OutsideLine> getAll() throws Exception {
		return outsideLineDao.findAll();
	}

	public List<OutsideLine> getByPage(int pageSize, int pageIndex)
			throws Exception {
		return outsideLineDao.findByPage(pageSize, pageIndex);
	}
}
