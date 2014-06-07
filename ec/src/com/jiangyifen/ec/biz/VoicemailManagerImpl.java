package com.jiangyifen.ec.biz;

import java.util.List;
import com.jiangyifen.ec.dao.Voicemail;
import com.jiangyifen.ec.dao.VoicemailDao;

public class VoicemailManagerImpl implements VoicemailManager {
	private VoicemailDao vmDao;

	public int getVmCount(String beginTime, String endTime, String callerid,String mailbox) {
		return vmDao.getVmCount(beginTime, endTime, callerid,mailbox);
	}

	public List<Voicemail> findVoicemail(String beginTime, String endTime, String callerid,String mailbox, int pageSize, int pageIndex) {
		return vmDao.findVoicemail(beginTime, endTime, callerid, mailbox,pageSize, pageIndex);
	}

	public void setVmDao(VoicemailDao vmDao) {
		this.vmDao = vmDao;
	}

	public VoicemailDao getVmDao() {
		return vmDao;
	}
	
	public void update(Voicemail vm){
		vmDao.update(vm);
	}

}
