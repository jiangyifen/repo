package com.jiangyifen.ec.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.jiangyifen.ec.biz.SipManager;
import com.jiangyifen.ec.dao.Sip;

public class UserLoginStatusClean extends Thread {
	protected SipManager sipManager;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public UserLoginStatusClean() {
		this.setDaemon(true);
		this.setName("UserLoginStatusClean");
		this.start();
	}

	public void run() {
		while (true) {
			try {

				Date current = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("HH");
				String h = sdf.format(current);
				Integer hour = Integer.valueOf(h);

				if (hour >= 1 && hour <= 3) {

					// 清除所有动态的队列成员
					Connection con = null;
					PreparedStatement statement = null;

					try {

						String url = Config.props.getProperty("pg_url");
						String username = Config.props
								.getProperty("pg_username");
						String password = Config.props
								.getProperty("pg_password");

						Class.forName("org.postgresql.Driver");

						con = DriverManager.getConnection(url, username,
								password);

						String sql = "delete from queue_member_table where queue_name in (select name from queue_table where dynamicmember=true)";
						logger.info(sql);
						statement = con.prepareStatement(sql);

					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					} finally {
						try {
							statement.close();
							con.close();
						} catch (SQLException e) {
							logger.error(e.getMessage(), e);
						}

					}

					// 清除sipname和username的绑定
					if (sipManager == null) {
						Thread.sleep(500);
					}
					List<Sip> sipList = sipManager.getSips();
					if (sipList != null && sipList.size() > 0) {
						for (Sip sip : sipList) {
							sip.setLoginusername("0");
							sipManager.updateSip(sip);
							logger.info("clean " + sip.getName());
						}
					}
					logger.info("login info has been cleaned!");
				}

				Thread.sleep(3600000);

			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public void setSipManager(SipManager sipManager) {
		this.sipManager = sipManager;
	}

	public SipManager getSipManager() {
		return sipManager;
	}
}
