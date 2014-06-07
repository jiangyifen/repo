package com.jiangyifen.ec.fastagi;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiHangupException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.jiangyifen.ec.dao.Sip;

public class WorkloadQuery extends BaseAgiScript {

	private final Log logger = LogFactory.getLog(getClass());

	private static Configuration config = null;
	private static SessionFactory sessionFactory = null;

	public WorkloadQuery() {
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

		String sipName;
		Session session = null;
		String hql = null;
		try {
			// 根据工号播报工作量
			session = sessionFactory.openSession();

			String username = "0";

			sipName = this.getVariable("CDR(src)");
			hql = "from Sip where name='" + sipName + "'";
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
			logger.info("WorkloadAGI " + username);

			if (username.equals("0")) {
				this.streamFile("51value/you_are_not_login");
			} else {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				String year = calendar.get(Calendar.YEAR) + "";
				String month = (calendar.get(Calendar.MONTH) + 1) + "";
				String date = calendar.get(Calendar.DATE) + "";
				String today = year + "-" + month + "-" + date;

				hql = "select count(*) from Cdr where calldate>='" + today
						+ "' and dcontext='incoming' and username='" + username
						+ "'";
				Integer incomingCount = new Integer(""+session.createQuery(hql).list().get(0));

				hql = "select count(*) from Cdr where calldate>='" + today
						+ "' and dcontext='outgoing' and username='" + username
						+ "'";
				Integer outgoingCount = new Integer(""+session.createQuery(hql).list().get(0));

				hql = "select count(*) from MyCustomerLog c,User u where c.hid=u.hid and c.date>='"
						+ today + "'  and u.username='" + username + "'";
				Integer myCustomerCount = new Integer(""+session.createQuery(hql).list().get(0));

				// logger.info("WORKLOAD QUERY outgoing="+outgoingCount);
				// logger.info("WORKLOAD QUERY outgoing="+outgoingCount.toString());

				this.streamFile("51value/gonghao");
				this.sayDigits(username);
				this.streamFile("51value/incomingCount");
				this.sayDigits(incomingCount.toString());
				this.streamFile("51value/outgoingCount");
				this.sayDigits(outgoingCount.toString());
				this.streamFile("51value/myCustomerCount");
				this.sayDigits(myCustomerCount.toString());

				if ((incomingCount+outgoingCount)>220){
					this.streamFile("51value/goodjob");
				}else if ((incomingCount+outgoingCount)<160){
					this.streamFile("51value/jiayou");
				}
				
			}

		} catch (AgiHangupException e) {

		} catch (HibernateException e) {
			logger.error("", e);
			initHibernate();
		} catch (AgiException e) {
			logger.error("", e);
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			session.close();
		}
	}
}
