package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.QueueMember;
import com.jiangyifen.ec.dao.QueueMemberDao;

public class QueueMemberManagerImpl implements QueueMemberManager {
	
	private QueueMemberDao queueMemberDao;

	public void setQueueMemberDao(QueueMemberDao queueMemberDao) {
		this.queueMemberDao = queueMemberDao;
	}

	public QueueMemberDao getQueueMemberDao() {
		return queueMemberDao;
	}

	public boolean addQueueMember(String queueName, String iface, Long penalty) throws Exception {

		try {
			QueueMember qm = new QueueMember();
			qm.setQueueName(queueName);
			qm.setIface(iface);
			qm.setPenalty(penalty);
			return queueMemberDao.save(qm);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("新增queuemember时出现异常");
		}
	}

	public void deleteQueueMember(String queueName, String iface) throws Exception {
		QueueMember qm = new QueueMember();
		qm.setQueueName(queueName);
		qm.setIface(iface);
		queueMemberDao.delete(qm);
	}


	public 	List<QueueMember> findAll(){
		return queueMemberDao.findAll();
	}
	
	public List<QueueMember> findAllByQueue(String queueName){
		return queueMemberDao.findAllByQueue(queueName);
	}
	
}
