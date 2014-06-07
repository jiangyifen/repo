package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.ShiftConfig;

public interface ShiftConfigManager {
	ShiftConfig get(Long id);
	boolean save(ShiftConfig o);
	void update(ShiftConfig o);
	void delete(Long id);
	void delete(ShiftConfig o);
	
	List<ShiftConfig> getAll();
	
}
