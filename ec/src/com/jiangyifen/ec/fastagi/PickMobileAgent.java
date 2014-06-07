package com.jiangyifen.ec.fastagi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

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

import com.jiangyifen.ec.dao.ShiftConfig;

public class PickMobileAgent extends BaseAgiScript {

	private static String lastDay = "";

	private static Map<String,ConcurrentLinkedQueue<String>> todayAgents = new ConcurrentHashMap<String,ConcurrentLinkedQueue<String>>();

	private static final Logger logger = LoggerFactory
			.getLogger(PickMobileAgent.class);

	private static Configuration config = null;
	private static SessionFactory sessionFactory = null;

	public PickMobileAgent() {
		initHibernate();
	}

	private static void initHibernate() {
		config = new Configuration().configure("Hibernate.cfg.xml");
		sessionFactory = config.buildSessionFactory();
		logger.info("initHibernate()");
	}

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		
		String shiftgroup = request.getParameter("shiftgroup");
		
		
		SimpleDateFormat dateformat = new SimpleDateFormat("EEE", Locale.US);
		String today = dateformat.format(new Date()).toLowerCase();

		// 判断是否距离上一次执行是否已经跨天了
		// 如果跨天，需要重新加载当天的值班名单
		if (!today.equalsIgnoreCase(lastDay)) {
			reloadMobileAgent(today);
			lastDay = today;
		}

		String phoneNumber = getMobileAgent(shiftgroup);

		this.setVariable("phonenumber", phoneNumber);

	}

	private static String getMobileAgent(String shiftgroup) {
		ConcurrentLinkedQueue<String> todayAgentsOfshiftgroup = todayAgents.get(shiftgroup);
		String phoneNumber = todayAgentsOfshiftgroup.poll();
		todayAgentsOfshiftgroup.add(phoneNumber);
		for (String phonenumber : todayAgentsOfshiftgroup) {
			logger.info(phonenumber);
		}
		return phoneNumber;
	}

	public static void reloadMobileAgent(String day) {

		Session session = null;
		String hql = null;
		try {
			initHibernate();
			session = sessionFactory.openSession();

			hql = " from ShiftConfig where " + day + "= :bool";

			@SuppressWarnings("unchecked")
			List<ShiftConfig> result = session.createQuery(hql)
					.setBoolean("bool", true).list();
			logger.info(hql);
			
			todayAgents.clear();
			logger.info("todayAgents.clear();");
			if (result != null) {
				for (ShiftConfig sf : result) {
					String shiftGroup = sf.getShiftGroup();
					String phoneNumber = sf.getPhoneNumber();
					ConcurrentLinkedQueue<String> todayAgentsOfshiftgroup = todayAgents.get(shiftGroup);
					if(todayAgentsOfshiftgroup==null){
						todayAgentsOfshiftgroup = new ConcurrentLinkedQueue<String>();
						todayAgents.put(shiftGroup, todayAgentsOfshiftgroup);
					}
					todayAgentsOfshiftgroup.add(phoneNumber);
				}
			}

		} catch (HibernateException e) {
			logger.error(e.getMessage(), e);
			initHibernate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			session.close();
		}

	}

}