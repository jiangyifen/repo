package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.ShiftConfig;
import com.jiangyifen.ec.dao.ShiftConfigDao;

public class ShiftConfigManagerImpl implements ShiftConfigManager {
	private ShiftConfigDao shiftConfigDao;

	public void setShiftConfigDao(ShiftConfigDao shiftConfigDao) {
		this.shiftConfigDao = shiftConfigDao;
	}

	public ShiftConfigDao getShiftConfigDao() {
		return shiftConfigDao;
	}

	public void delete(Long id) {
		shiftConfigDao.delete(id);
	}

	public void delete(ShiftConfig o) {
		shiftConfigDao.delete(o);
	}

	public ShiftConfig get(Long id) {
		return shiftConfigDao.get(id);
	}

	public boolean save(ShiftConfig o) {
		return shiftConfigDao.save(o);
	}

	public void update(ShiftConfig o) {
		shiftConfigDao.update(o);
	}

	@Override
	public List<ShiftConfig> getAll() {
		return shiftConfigDao.findAll();
	}

}
