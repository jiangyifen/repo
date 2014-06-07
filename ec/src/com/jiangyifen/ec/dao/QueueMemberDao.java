package com.jiangyifen.ec.dao;

import java.util.List;

public interface QueueMemberDao {

	boolean save(QueueMember qm);
	void delete(QueueMember qm);
	
	List<QueueMember> findAll();
	List<QueueMember> findAllByQueue(String queueName);

}
