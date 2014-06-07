package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.Sip;

public interface SipManager {
	boolean addSip(Sip sip) throws Exception;

	void deleteSips(Long[] id) throws Exception;

	void deleteSip(Long id) throws Exception;

	void updateSip(Sip sip) throws Exception;

	int getSipCount() throws Exception;

	Sip getSip(Long id) throws Exception;

	List<Sip> getSips() throws Exception;
	List<Sip> getSipsDesc() throws Exception;

	List<Sip> getSipsByPage(int pageSize, int pageIndex) throws Exception;
	
	Sip findSipByName(String exten);

}
