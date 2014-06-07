package com.jiangyifen.ec.customer.olay;

import java.io.IOException;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerConnectionState;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.util.Config;

public class OlayAMI extends Thread {

	private static final Log logger = LogFactory.getLog(OlayAMI.class);

	private static String ip;
	private static String username;
	private static String password;

	private static ManagerConnectionFactory factory;
	private static ManagerConnection managerConnection;

	public OlayAMI() throws IOException {
		ip = Config.props.getProperty("manager_ip");
		username = Config.props.getProperty("manager_username");
		password = Config.props.getProperty("manager_password");

		factory = new ManagerConnectionFactory(ip, username, password);

		this.setDaemon(true);
		this.start();
	}

	private static void initManagerConnection() {
		logger.info("init manager connection!");

		try {
			if (managerConnection != null) {
				if (managerConnection.getState() == ManagerConnectionState.CONNECTED
						|| managerConnection.getState() == ManagerConnectionState.RECONNECTING) {
					managerConnection.logoff();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		try {
			managerConnection = factory.createManagerConnection();
			managerConnection.addEventListener(new OlayAMIEventListener2());
			managerConnection.addEventListener(new OlayAMIEventListener());
			managerConnection.login();
			Thread.sleep(1000);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void run() {
		while (true) {

			try {

				initManagerConnection();

				while (true) {

					try {
						Thread.sleep(10000);

					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
