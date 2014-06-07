package com.jiangyifen.ec.fastagi;

import java.util.Date;
import java.util.List;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.dao.Queue;
import com.jiangyifen.ec.dao.Sip;
import com.jiangyifen.ec.dao.User;
import com.jiangyifen.ec.dao.UserLoginRecord;
import com.jiangyifen.ec.util.ShareData;

public class UserLogout extends BaseAgiScript {

	private static final Logger logger = LoggerFactory
			.getLogger(UserLogout.class);

	private static Configuration config = null;
	private static SessionFactory sessionFactory = null;

	public UserLogout() {
		initHibernate();
	}

	private static void initHibernate() {
		config = new Configuration().configure("Hibernate.cfg.xml");
		sessionFactory = config.buildSessionFactory();
		logger.info("initHibernate()");
	}

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		this.setVariable("ISLOGOUT", "false");

		String username = this.getVariable("USERNAME");
		String pwd = this.getVariable("PWD");
		String exten = this.getVariable("CALLERID(num)");

		String result = logout(username, pwd, exten);

		setVariable("ISLOGOUT", result);

		logger.info("Username " + username + " logout " + exten + ".");

	}

	@SuppressWarnings({ "unchecked" })
	public static String logout(String username, String agentpwd, String exten) {

		Session session = null;
		Transaction tran = null;
		String hql = null;

		try {
			if (sessionFactory == null) {
				initHibernate();
			}

			session = sessionFactory.openSession();

			hql = " from User where username='" + username + "'";
			List<User> userList = session.createQuery(hql).list();

			if (userList != null && userList.size() > 0) {

				User u = userList.get(0);

				if (agentpwd.equals(u.getPassword())
						|| agentpwd.equals("autologout")) {

					// 动态队列成员
					for (String sip : ShareData.sipAndLoginusername
							.keySet()) {
						if (ShareData.sipAndLoginusername.get(sip).equals(
								username)) {
							UserLogout.dynamicQueueMemberDelete(sip);
						}
					}					
					
					tran = session.beginTransaction();

					// 把原先username和sip的绑定信息清除
					hql = " from Sip where loginusername='" + username + "'";
					List<Sip> sipList = session.createQuery(hql).list();
					for (Sip s : sipList) {
						s.setLoginusername("0");
						session.update(s);
					}

					session.flush();

					// 记录logout日志
					// 获取该用户的上一条日志，看看有没有登出信息。
					UserLoginRecord lastRecord = null;
					hql = "from UserLoginRecord where username='" + username
							+ "' order by id desc";
					List<UserLoginRecord> recordList = session.createQuery(hql)
							.list();
					if (recordList != null && recordList.size() > 0) {
						lastRecord = recordList.get(0);
					}
					// 如有记录且没有登出信息，便把此次登出信息写入。
					if (lastRecord != null
							&& lastRecord.getLogoutDate() == null) {
						lastRecord.setLogoutDate(new Date());
						session.update(lastRecord);
					}
					// 若无记录，或已有记录且有登出信息，则新增一条记录，仅有登出信息没有登录信息
					else {
						UserLoginRecord ulr = new UserLoginRecord();
						ulr.setUsername(username);
						ulr.setExten(exten);
						ulr.setLogoutDate(new Date());
						session.save(ulr);
					}

					session.flush();
					session.clear();
					tran.commit();


					return "true";
				}
			}

		} catch (Exception e) {
			initHibernate();
			logger.error("", e);
		} finally {
			session.close();
		}

		return "false";

	}

	public static void dynamicQueueMemberDelete(String exten) {

		// 按人员动态添加删除队列成员

		Session session = null;
		Transaction tran = null;
		String sql = null;

		try {
			if (sessionFactory == null) {
				initHibernate();
			}

			// 用户登出时，首先将interface是当前exten的queuemember记录全部删除（仅dynamic队列）
			String dynamicQueues = "";
			for (Queue queue : ShareData.allQueues) {
				if (queue.getDynamicMember()) {
					dynamicQueues = dynamicQueues + "'" + queue.getName()
							+ "',";
				}
			}

			if (!dynamicQueues.equals("")) {
				dynamicQueues = dynamicQueues.substring(0,
						dynamicQueues.length() - 1);
				sql = "delete from QueueMember where interface='SIP/" + exten
						+ "' and queue_name in (" + dynamicQueues + ")";
				logger.info(sql);

				session = sessionFactory.openSession();
				tran = session.beginTransaction();
				session.createQuery(sql).executeUpdate();
				session.flush();
				session.clear();
				tran.commit();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		// /////////////////////////////////////////////////////////////////////////
	}

}
