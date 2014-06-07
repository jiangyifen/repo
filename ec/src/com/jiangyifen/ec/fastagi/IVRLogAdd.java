package com.jiangyifen.ec.fastagi;

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

import com.jiangyifen.ec.dao.IVRLog;

public class IVRLogAdd extends BaseAgiScript {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static Configuration config = null;
	private static SessionFactory sessionFactory = null;

	public IVRLogAdd() {
		if (config == null) {
			config = new Configuration().configure("Hibernate.cfg.xml");
		}

		if (sessionFactory == null) {
			sessionFactory = config.buildSessionFactory();
		}
	}

	private void initHibernate() {
		config = new Configuration().configure("Hibernate.cfg.xml");
		sessionFactory = config.buildSessionFactory();
		logger.info("initHibernate()");
	}

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		Session session = null;
		Transaction tran = null;

		try {
			String context = this.getVariable("CONTEXT");
			String exten = this.getVariable("EXTEN");
			String priority = this.getVariable("PRIORITY");
			String calleridnum = this.getVariable("CALLERID(num)");
			String node = request.getParameter("node");
			String uniqueid = request.getUniqueId();

			IVRLog ivrlog = new IVRLog();
			ivrlog.setNode(node);
			ivrlog.setUniqueid(uniqueid);
			ivrlog.setContext(context);
			ivrlog.setExten(exten);
			ivrlog.setChannel(channel.getName());
			ivrlog.setCalleridnum(calleridnum);
			ivrlog.setPriority(priority);

			session = sessionFactory.openSession();
			tran = session.beginTransaction();
			session.save(ivrlog);
			tran.commit();
			logger.info("ivr log: " + ivrlog.getDate() + ", "
					+ ivrlog.getNode());
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			initHibernate();
		} finally {
			session.close();
		}
	}
}
