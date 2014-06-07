package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.QueueMember;

public interface QueueMemberManager {
	
	boolean addQueueMember(String queueName, String iface, Long penalty)
			throws Exception;

	void deleteQueueMember(String queueName, String iface) throws Exception;


	List<QueueMember> findAll();
	List<QueueMember> findAllByQueue(String queueName);
	
}
