package com.jiangyifen.ec.customer.olay;

import org.asteriskjava.manager.AbstractManagerEventListener;
import org.asteriskjava.manager.event.LinkEvent;
import org.asteriskjava.manager.event.UnlinkEvent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.dao.LinkLog;
import com.jiangyifen.ec.util.MyStringUtils;
import com.jiangyifen.ec.util.ShareData;

public class OlayAMIEventListener extends AbstractManagerEventListener {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static Configuration config = null;
	private static SessionFactory sessionFactory = null;

	public OlayAMIEventListener() {
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

	protected void handleEvent(LinkEvent event) {
		String sipPeernanme1 = MyStringUtils
				.getSipNameFromChannel(event.getChannel1());
		String sipPeername2 = MyStringUtils
				.getSipNameFromChannel(event.getChannel2());

		if (ShareData.allSipName.contains(sipPeernanme1)) {

			Session session = null;
			Transaction tran = null;

			try {
				
				String username = ShareData.sipAndLoginusername.get(sipPeernanme1);
				if(username==null){
					username="0";
				}
				
				String name = ShareData.usernameAndName.get(username);
				if(name==null){
					name="0";
				}
				
				LinkLog linkLog = new LinkLog();
				linkLog.setCallerid(event.getCallerId1());
				linkLog.setChannel(event.getChannel1());
				linkLog.setBridgedChannel(event.getChannel2());
				linkLog.setDate(event.getDateReceived());
				linkLog.setExten(sipPeernanme1);
				linkLog.setUniqueid(event.getUniqueId1());
				linkLog.setBridgedUniqueid(event.getUniqueId2());
				linkLog.setName(name);
				linkLog.setUsername(username);
				linkLog.setAction("link");
				
				session = sessionFactory.openSession();
				tran = session.beginTransaction();
				session.save(linkLog);
				tran.commit();
				session.close();
				
				logger.info("linkLog : " + linkLog);
				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				initHibernate();
			} finally {
				session.close();
			}
		}

		if (ShareData.allSipName.contains(sipPeername2)) {

			Session session = null;
			Transaction tran = null;

			try {
				
				String username = ShareData.sipAndLoginusername.get(sipPeername2);
				if(username==null){
					username="0";
				}
				
				String name = ShareData.usernameAndName.get(username);
				if(name==null){
					name="0";
				}
				
				LinkLog linkLog = new LinkLog();
				linkLog.setCallerid(event.getCallerId2());
				linkLog.setChannel(event.getChannel2());
				linkLog.setBridgedChannel(event.getChannel1());
				linkLog.setDate(event.getDateReceived());
				linkLog.setExten(sipPeername2);
				linkLog.setUniqueid(event.getUniqueId2());
				linkLog.setBridgedUniqueid(event.getUniqueId1());
				linkLog.setName(name);
				linkLog.setUsername(username);
				linkLog.setAction("link");
				
				session = sessionFactory.openSession();
				tran = session.beginTransaction();
				session.save(linkLog);
				tran.commit();
				session.close();
				
				logger.info("linkLog : " + linkLog);
				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				initHibernate();
			} finally {
				session.close();
			}
		}

	}

	protected void handleEvent(UnlinkEvent event) {
		String sipPeername1 = MyStringUtils
				.getSipNameFromChannel(event.getChannel1());
		String sipPeername2 = MyStringUtils
				.getSipNameFromChannel(event.getChannel2());

		if (ShareData.allSipName.contains(sipPeername1)) {

			Session session = null;
			Transaction tran = null;

			try {
				
				String username = ShareData.sipAndLoginusername.get(sipPeername1);
				if(username==null){
					username="0";
				}
				
				String name = ShareData.usernameAndName.get(username);
				if(name==null){
					name="0";
				}
				
				LinkLog linkLog = new LinkLog();
				linkLog.setCallerid(event.getCallerId1());
				linkLog.setChannel(event.getChannel1()); 
				linkLog.setBridgedChannel(event.getChannel2());
				linkLog.setDate(event.getDateReceived());
				linkLog.setExten(sipPeername1);
				linkLog.setUniqueid(event.getUniqueId1());
				linkLog.setBridgedUniqueid(event.getUniqueId2());
				linkLog.setName(name);
				linkLog.setUsername(username);
				linkLog.setAction("unlink");
				
				session = sessionFactory.openSession();
				tran = session.beginTransaction();
				session.save(linkLog);
				tran.commit();
				session.close();
				
				logger.info("unlinkLog : " + linkLog);
				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				initHibernate();
			} finally {
				session.close();
			}
		}

		if (ShareData.allSipName.contains(sipPeername2)) {

			Session session = null;
			Transaction tran = null;

			try {
				
				String username = ShareData.sipAndLoginusername.get(sipPeername2);
				if(username==null){
					username="0";
				}
				
				String name = ShareData.usernameAndName.get(username);
				if(name==null){
					name="0";
				}
				
				LinkLog linkLog = new LinkLog();
				linkLog.setCallerid(event.getCallerId2());
				linkLog.setChannel(event.getChannel2());
				linkLog.setBridgedChannel(event.getChannel1());
				linkLog.setDate(event.getDateReceived());
				linkLog.setExten(sipPeername2);
				linkLog.setUniqueid(event.getUniqueId2());
				linkLog.setBridgedUniqueid(event.getUniqueId1());
				linkLog.setName(name);
				linkLog.setUsername(username);
				linkLog.setAction("unlink");
				
				session = sessionFactory.openSession();
				tran = session.beginTransaction();
				session.save(linkLog);
				tran.commit();
				session.close();
				
				logger.info("unlinkLog : " + linkLog);
				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				initHibernate();
			} finally {
				session.close();
			}
		}
	}

}
