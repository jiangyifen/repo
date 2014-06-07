package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.QueueMemberPauseLog;

public interface QueueMemberPauseLogManager {
	
	void save(QueueMemberPauseLog log);
	void update(QueueMemberPauseLog log);

	QueueMemberPauseLog findLastLogByMemberNameAndQueueName(String memberName, String queueName);
	
}
