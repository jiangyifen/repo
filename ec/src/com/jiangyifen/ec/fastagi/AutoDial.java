package com.jiangyifen.ec.fastagi;

import java.util.List;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.jiangyifen.ec.dao.DialTaskItem;

public class AutoDial extends BaseAgiScript {

	private final Log logger = LogFactory.getLog(getClass());

	private static Configuration config = null;
	private static SessionFactory sessionFactory = null;

	public AutoDial() {
		initHibernate();
	}

	private void initHibernate() {
		config = new Configuration().configure("Hibernate.cfg.xml");
		sessionFactory = config.buildSessionFactory();
		logger.info("initHibernate()");
	}

	@SuppressWarnings("unchecked")
	private String getPhoneNumber() {

		String phoneNumber = "";

		Session session = null;
		Transaction tran = null;
		String hql = null;

		try {

			session = sessionFactory.openSession();
			tran = session.beginTransaction();

			hql = "from DialTaskItem where status='"
					+ DialTaskItem.STATUS_READY + "' order by id";
			logger.info("Get phone number : sql = " + hql);
			Query q = session.createQuery(hql);
			q.setFirstResult(0);
			q.setMaxResults(1);
			List<DialTaskItem> result = (List<DialTaskItem>) q.list();

			if (result.size() >= 1) {
				DialTaskItem dti = result.get(0);

				phoneNumber = dti.getPhoneNumber();

				dti.setStatus(DialTaskItem.STATUS_FINISH);

				session.update(dti);
				session.flush();
				tran.commit();
			}

		} catch (HibernateException e) {
			logger.error(e.getMessage(),e);
			initHibernate();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} finally {
			session.close();
		}

		return phoneNumber;
	}

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		String phoneNumber = getPhoneNumber();
		this.setVariable("phoneNumber", phoneNumber);
		logger.info("Get phone number: " + phoneNumber);

	}
}
