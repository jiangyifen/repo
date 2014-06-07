package com.jiangyifen.ec.fastagi;

import java.net.URL;
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

import com.jiangyifen.ec.dao.Cdr;
import com.jiangyifen.ec.dao.Sip;
import com.jiangyifen.ec.util.Config;

public class MyCustomerLogAdd extends BaseAgiScript {

	private final Log logger = LogFactory.getLog(getClass());

	private static Configuration config = null;
	private static SessionFactory sessionFactory = null;

	public MyCustomerLogAdd() {
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
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}

		String sipName;
		Session session = null;
		String hql = null;
		try {
			String regist = request.getParameter("regist");

			// 根据分机号添加客户电话号码
			session = sessionFactory.openSession();

			sipName = this.getVariable("CDR(src)");
			// logger.info("MyCustomerAGI: " + "sipName=" + sipName);
			if (regist.equals("true")) {
				String u = Config.props.getProperty("3rd_username");
				String p = Config.props.getProperty("3rd_password");
				URL url = new URL(
						"http://localhost:8080/ec/myCustomerLogAdd?u=" + u
								+ "&p=" + p + "&sipName=" + sipName);
				logger.info("MyCustomerAGI: " + url);
				url.openStream();
			}
			// 播报所添加号码的相关信息
			String username = "0";
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

			String customerPhoneNumber = "";
			if (username.equals("0")) {
				logger.info("MyCustomerAGI: get customer phone by sipName");
				hql = "from Cdr where dst='" + sipName
						+ "' order by calldate desc";
				this.streamFile("51value/you_are_not_login");
			} else {
				logger.info("MyCustomerAGI: get customer phone by username");
				hql = "from Cdr where username='" + username
						+ "' order by calldate desc";
			}
			logger.info("MyCustomerAGI: hql="+hql);
			Query q3 = session.createQuery(hql);
			q3.setFirstResult(0);
			q3.setMaxResults(1);
			List<Cdr> cdrList = q3.list();
			if (cdrList != null && cdrList.size() > 0) {
				Cdr cdr = cdrList.get(0);
				String src = cdr.getSrc();
				String dst = cdr.getDst();
				Date calldate = cdr.getCalldate();
				long duration = cdr.getDuration();

				if (src.length() > 4) {
					customerPhoneNumber = src;
				} else if (dst.length() > 4) {
					customerPhoneNumber = dst;
				} else {
					customerPhoneNumber = dst;
				}

				logger.info("MyCustomerAGI: calldate="+calldate+" username=" + username
						+ ", customerPhoneNumber=" + customerPhoneNumber);


				this.streamFile("51value/phone_number_is");
				this.sayDigits(customerPhoneNumber);

				if (regist.equals("true")) {
					this.streamFile("51value/de_yong_hu");
					this.streamFile("51value/have_add_to_gonghao");
					this.sayDigits(username);
				}

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(calldate);
				calendar.add(Calendar.SECOND, (int) duration);
				String year = calendar.get(Calendar.YEAR) + "";
				String month = (calendar.get(Calendar.MONTH) + 1) + "";
				String date = calendar.get(Calendar.DATE) + "";
				String hour = calendar.get(Calendar.HOUR_OF_DAY) + "";
				String minute = calendar.get(Calendar.MINUTE) + "";
				String second = calendar.get(Calendar.SECOND) + "";

				this.streamFile("51value/calldate_end");
				this.sayDigits(year);
				this.streamFile("51value/year");
				this.sayDigits(month);
				this.streamFile("51value/month");
				this.sayDigits(date);
				this.streamFile("51value/day");
				this.sayDigits(hour);
				this.streamFile("51value/hour");
				this.sayDigits(minute);
				this.streamFile("51value/minute");
				this.sayDigits(second);
				this.streamFile("51value/second");

			}
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
