package com.jiangyifen.ec.biz;

import java.util.List;
import com.jiangyifen.ec.dao.Voicemail;

public interface VoicemailManager {
	void update(Voicemail vm);
	int getVmCount(String beginTime, String endTime, String callerid,String mailbox);
	List<Voicemail> findVoicemail(String beginTime, String endTime, String callerid, String mailbox, int pageSize, int pageIndex);

}
