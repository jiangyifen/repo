package com.jiangyifen.ec.dao;

import java.util.List;

public interface VoicemailDao {
	void update(Voicemail vm);
	int getVmCount(String beginTime, String endTime, String callerid, String mailbox);
	List<Voicemail> findVoicemail(String beginTime, String endTime, String callerid,String mailbox, int pageSize, int pageIndex);
}