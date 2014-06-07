package com.jiangyifen.ec.fastagi;

import java.util.Date;
import java.util.List;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiHangupException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.dao.CustomerSatisfactionInvestigationLog;
import com.jiangyifen.ec.dao.Sip;

public class CustomerSatisfactionInvestigation extends BaseAgiScript {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static Configuration config = null;
	private static SessionFactory sessionFactory = null;

	public CustomerSatisfactionInvestigation() {
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
		
		Session session = null;
		Transaction tran = null;
		String hql = null;
		try {
			String exten = this.getVariable("EXTEN").substring(3);
			String calleridNum = this.getVariable("CALLERID(num)");
			String p1 = this.getVariable("p1");
			String p2 = this.getVariable("p2");
			
			logger.info("CustomerSatisfactionInvestigation: exten="+exten);
			logger.info("CustomerSatisfactionInvestigation: CALLERID(num)="+calleridNum);
			logger.info("CustomerSatisfactionInvestigation: p1="+p1);
			logger.info("CustomerSatisfactionInvestigation: p2="+p2);
			
			session = sessionFactory.openSession();
			tran = session.beginTransaction();
			
			hql = "from Sip where name='" + exten + "'";
			String username = "0";
			Query q1 = session.createQuery(hql);
			q1.setFirstResult(0);
			q1.setMaxResults(1);
			List<Sip> sipList = q1.list();
			if (sipList != null && sipList.size() > 0) {
				String s = sipList.get(0).getLoginusername();
				if (s != null) {
					username = s;
				}
			}
			logger.info("CustomerSatisfactionInvestigation: hql="+hql);
			logger.info("CustomerSatisfactionInvestigation: username="+username);

			CustomerSatisfactionInvestigationLog log = new CustomerSatisfactionInvestigationLog();
			log.setDate(new Date());
			log.setCalleridNum(calleridNum);
			log.setExten(exten);
			log.setUsername(username);
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
