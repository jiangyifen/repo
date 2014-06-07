package com.jiangyifen.ec.fastagi;

import java.util.List;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.dao.CustomerManager;
import com.jiangyifen.ec.dao.CustomerPhoneNum;

public class GetCustomerManager extends BaseAgiScript {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static Configuration config = null;
	private static SessionFactory sessionFactory = null;

	public GetCustomerManager() {
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

		String customerPhoneNumber;
		String dstQueue = "default";
		Session session = null;
		String hql = null;
		try {
			session = sessionFactory.openSession();

			customerPhoneNumber = this.getVariable("CALLERID(num)");
			hql = " from CustomerPhoneNum where customer_phone_number='"
					+ customerPhoneNumber + "'";
			List<CustomerPhoneNum> cpnList = session.createQuery(hql).list();

			if (cpnList != null && cpnList.size() > 0) {
				CustomerPhoneNum cpn = cpnList.get(0);
				String customerManagerLoginName = cpn
						.getCustomerManagerLoginName();
				if (customerManagerLoginName != null) {
					hql = " from CustomerManager where customer_manager_login_name='"
							+ customerManagerLoginName + "'";
					List<CustomerManager> cmList = session.createQuery(hql)
							.list();
					if (cmList != null && cmList.size() > 0) {
						CustomerManager cm = cmList.get(0);
						dstQueue = cm.getExten();
					}
				}

				logger.info("dstQueue=" + dstQueue);
			}
			this.setVariable("dstQueue", dstQueue);

		} catch (HibernateException e) {
			logger.error(e.getMessage(),e);
			initHibernate();
		} catch (AgiException e) {
			logger.error(e.getMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} finally {
			session.close();
		}
	}
}
