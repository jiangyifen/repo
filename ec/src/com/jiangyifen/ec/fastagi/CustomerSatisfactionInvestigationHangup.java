package com.jiangyifen.ec.fastagi;

import java.util.Date;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiHangupException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.dao.CustomerSatisfactionInvestigationLog;

public class CustomerSatisfactionInvestigationHangup extends BaseAgiScript {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static Configuration config = null;
	private static SessionFactory sessionFactory = null;

	public CustomerSatisfactionInvestigationHangup() {
		initHibernate();
	}

	private void initHibernate() {
		config = new Configuration().configure("Hibernate.cfg.xml");
		sessionFactory = config.buildSessionFactory();
		logger.info("initHibernate()");
	}

//	@SuppressWarnings("unchecked")
	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		
		Session session = null;
		Transaction tran = null;

		try {
			
			String uniqueid = channel.getUniqueId();
			String calleridNum = this.getVariable("CALLERID(num)");
			String p1 = this.getVariable("p1");
			String p2 = this.getVariable("p2");

			logger.info("CustomerSatisfactionInvestigation: uniqueid="+uniqueid);
			logger.info("CustomerSatisfactionInvestigation: CALLERID(num)="+calleridNum);
			logger.info("CustomerSatisfactionInvestigation: p1="+p1);
			logger.info("CustomerSatisfactionInvestigation: p2="+p2);
			
			session = sessionFactory.openSession();
			tran = session.beginTransaction();

			CustomerSatisfactionInvestigationLog log = new CustomerSatisfactionInvestigationLog();
			log.setDate(new Date());
			log.setCalleridNum(calleridNum);
			log.setExten(null);
			log.setUsername(null);
			log.setUniqueid(uniqueid);
			log.setP1(p1);
			log.setP2(p2);
			session.save(log);

			tran.commit();
			logger.info("CustomerSatisfactionInvestigation: succeed");
			
		} catch (AgiHangupException e) {

		} catch (HibernateException e) {
			logger.error(e.getMessage(), e);
			initHibernate();
		} catch (AgiException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			session.close();
		}
	}
}
