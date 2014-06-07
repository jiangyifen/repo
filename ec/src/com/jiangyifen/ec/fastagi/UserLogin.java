package com.jiangyifen.ec.fastagi;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
import com.jiangyifen.ec.dao.QueueMember;
import com.jiangyifen.ec.dao.Sip;
import com.jiangyifen.ec.dao.User;
import com.jiangyifen.ec.dao.UserLoginRecord;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.ShareData;

public class UserLogin extends BaseAgiScript {

	private static final Logger logger = LoggerFactory
			.getLogger(UserLogin.class);

	private static Configuration config = null;
	private static SessionFactory sessionFactory = null;

	public UserLogin() {
		initHibernate();
	}

	private static void initHibernate() {
		config = new Configuration().configure("Hibernate.cfg.xml");
		sessionFactory = config.buildSessionFactory();
		logger.info("initHibernate()");
	}

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		this.setVariable("ISLOGIN", "false");

		String username = this.getVariable("USERNAME");
		String pwd = this.getVariable("PWD");
		String exten = this.getVariable("CALLERID(num)");

		String result = login(username, pwd, exten);

		setVariable("ISLOGIN", result);

		logger.info("Username " + username + " login " + exten + ".");

	}

	@SuppressWarnings({ "unchecked" })
	public static String login(String username, String agentpwd, String exten) {

		String adp = Config.props.getProperty("agent_default_password");
		if (!agentpwd.equals(adp)) {
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

					User user = userList.get(0);

					if (agentpwd.equals(user.getPassword())) {

						// 清除原有动态队列成员信息
						// 先检查改user之前登录的分机绑定的用户名
						// 若是当前正在做登录的用户名，则需要将之前的分机从相关队列中删除
						// 若不是（是其他，或是0）则不进行此步
						for (String sip : ShareData.sipAndLoginusername
								.keySet()) {
							if (ShareData.sipAndLoginusername.get(sip).equals(
									username)) {
								UserLogout.dynamicQueueMemberDelete(sip);
							}
						}
						
						tran = session.beginTransaction();

						// 把原先username和sip的绑定信息清除
						// 内存（ShareData）。
						// 这步可以不做，因为可以接受sharedata在稍后更新sipAndLoginusername。
						for (String sip : ShareData.sipAndLoginusername
								.keySet()) {
							if (ShareData.sipAndLoginusername.get(sip).equals(
									username)) {
								ShareData.sipAndLoginusername.put(sip, "0");
							}
						}
						// 数据库
						hql = " from Sip where loginusername='" + username
								+ "'";
						List<Sip> sipList = session.createQuery(hql).list();
						for (Sip s : sipList) {
							s.setLoginusername("0");
							session.update(s);
						}

						session.flush();
						session.clear();
						tran.commit();


						// 更新username和sip的绑定信息
						// 内存（ShareData）这步必须做，因为需要立刻将新用户名绑定到sipname
						ShareData.sipAndLoginusername.put(exten, username);
						// 数据库
						tran = session.beginTransaction();
						Sip s = null;
						hql = " from Sip where name='" + exten + "'";
						sipList = session.createQuery(hql).list();
						if (sipList != null && sipList.size() > 0) {
							s = sipList.get(0);
							s.setLoginusername(username);
							session.update(s);
							logger.info("SIP " + exten + " login by "
									+ username);
						}

						session.flush();
						session.clear();

						// 记录login日志
						UserLoginRecord ulr = new UserLoginRecord();
						ulr.setUsername(username);
						ulr.setExten(exten);
						ulr.setLoginDate(new Date());
						session.save(ulr);

						session.flush();
						session.clear();
						tran.commit();

						// 动态队列成员
						dynamicQueueMemberAdd(user, exten);

						return "true";
					} else {
						logger.error("UserLogin: invalid password");
					}
				}

			} catch (Exception e) {
				initHibernate();
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			} finally {
				session.close();
			}
		}
		return "false";

	}

	private static void dynamicQueueMemberAdd(User user, String exten) {

		// 按人员动态添加删除队列成员

		String sql;
		Session session = null;
		Transaction tran = null;

		try {
			if (sessionFactory == null) {
				initHibernate();
			}
			session = sessionFactory.openSession();

			// 用户登录时，首先将interface是当前exten的queuemember记录全部删除（仅dynamic队列）
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

				tran = session.beginTransaction();
				session.createQuery(sql).executeUpdate();
				session.flush();
				session.clear();
				tran.commit();
			}

			// 然后获取user所属queue的queuename
			// 将用户的exten作为interface来添加到queuemember表中

			tran = session.beginTransaction();
			Set<Queue> queues = user.getQueues();
			if (queues != null) {
				for (Queue queue : queues) {
					String queueName = queue.getName();
					String iface = "SIP/" + exten;
					Long penalty = new Long(5);

					QueueMember qm = new QueueMember();
					qm.setQueueName(queueName);
					qm.setIface(iface);
					qm.setPenalty(penalty);

					session.saveOrUpdate(qm);
				}
			}

			session.flush();
			session.clear();
			tran.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

	}
}
