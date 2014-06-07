package com.jiangyifen.ec.dao;

public interface QueueMemberPauseLogDao {

	void save(QueueMemberPauseLog log);
	void update(QueueMemberPauseLog log);

	QueueMemberPauseLog findLastLogByMemberNameAndQueueName(String memberName, String queueName);

}
