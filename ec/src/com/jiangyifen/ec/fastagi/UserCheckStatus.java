package com.jiangyifen.ec.fastagi;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.util.ShareData;

public class UserCheckStatus extends BaseAgiScript {

	private final Log logger = LogFactory.getLog(getClass());

//	private static Configuration config = null;
//	private static SessionFactory sessionFactory = null;

//	public UserCheckStatus() {
//		initHibernate();
//	}

//	private void initHibernate() {
//		config = new Configuration().configure("Hibernate.cfg.xml");
//		sessionFactory = config.buildSessionFactory();
//		logger.info("initHibernate()");
//	}


	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		this.setVariable("LOGINUSERNAME", "0");

		String exten = "";

//		Session session = null;
//		String hql = null;

		try {
			
			
//			session = sessionFactory.openSession();
//
			exten = this.getVariable("CALLERID(num)");
//
//			hql = " from Sip where name='" + exten + "'";
//			List<Sip> result = session.createQuery(hql).list();
//
//			if (result != null && result.size() > 0) {
//
//				Sip s = result.get(0);
//
//				this.setVariable("LOGINUSERNAME", s.getLoginusername());
//
//			}
			

			this.setVariable("LOGINUSERNAME",ShareData.sipAndLoginusername.get(exten));

		} catch (Exception e) {
//			initHibernate();
			logger.error(e.getMessage(),e);
		} 
//		finally {
//			session.close();
//		}
	}
}
