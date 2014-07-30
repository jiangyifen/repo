package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.HoldEventLog;;

public interface HoldEventLogManager {
	
	void save(HoldEventLog log);
	void update(HoldEventLog log);

	HoldEventLog findLastLogByUniqueid(String uniqueid);
	
}
