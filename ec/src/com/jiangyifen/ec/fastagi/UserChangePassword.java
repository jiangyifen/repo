package com.jiangyifen.ec.fastagi;

import java.util.List;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.jiangyifen.ec.dao.User;

public class UserChangePassword extends BaseAgiScript {

	private final Log logger = LogFactory.getLog(getClass());

	private static Configuration config = null;
	private static SessionFactory sessionFactory = null;

	public UserChangePassword() {
		initHibernate();
	}

	private void initHibernate() {
		config = new Configuration().configure("Hibernate.cfg.xml");
		sessionFactory = config.buildSessionFactory();
		logger.info("initHibernate()");
	}

	@SuppressWarnings("unchecked")
	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		
		this.setVariable("ISPWDCHANGE", "false");

		String username = "";
		String pwd = "";
		String newpwd = "";

		Session session = null;
		Transaction tran = null;
		String hql = null;

		try {
			session = sessionFactory.openSession();
			
			username = this.getVariable("USERNAME");
			pwd = this.getVariable("PWD");
			newpwd = this.getVariable("NEWPWD");
			
			hql = " from User where username='" + username + "'";
			List<User> userList = session.createQuery(hql).list();

			if (userList != null && userList.size() > 0) {

				User u = userList.get(0);

				if (pwd.equals(u.getPassword())) {
				
					u.setPassword(newpwd);

					tran = session.beginTransaction();
					session.merge(u);
					session.flush();
					tran.commit();
					
					this.setVariable("ISPWDCHANGE", "true");
					
					logger.info("User "+ username + " change password succeed.");
				}
			}
			
		} catch (Exception e) {
			initHibernate();
			logger.error("", e);
		} finally {
			session.close();
		}
	}
}
