package com.jiangyifen.ec.dao;

import java.util.List;

public interface ShiftConfigDao {

	ShiftConfig get(Long id);

	boolean save(ShiftConfig o);

	void update(ShiftConfig o);

	void delete(Long id);

	void delete(ShiftConfig o);

	List<ShiftConfig> findAll();
}
