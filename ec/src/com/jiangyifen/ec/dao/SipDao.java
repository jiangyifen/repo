package com.jiangyifen.ec.dao;

import java.util.List;

public interface SipDao {
	Sip get(Long id);
	boolean save(Sip sip);
	void update(Sip sip);
	void delete(Long id);
	void delete(Sip sip);
	
	long getSipCount();
	
	List<Sip> findAll();
	List<Sip> findAllDesc();
	List<Sip> findByPage(int pageSize,int pageIndex);
	Sip fineByName(String name);
}
