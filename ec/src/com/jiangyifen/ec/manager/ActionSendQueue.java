package com.jiangyifen.ec.manager;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerConnectionState;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.util.Config;

public class ActionSendQueue extends Thread {

	private final Log logger = LogFactory.getLog(getClass());

	String ip = Config.props.getProperty("manager_ip");
	String username = Config.props.getProperty("manager_username");
	String password = Config.props.getProperty("manager_password");
	ManagerConnectionFactory factory = new ManagerConnectionFactory(ip,
			username, password);
	ManagerConnection managerConnection = null;

	public static ConcurrentLinkedQueue<ManagerAction> actionQueue = new ConcurrentLinkedQueue<ManagerAction>();

	public ActionSendQueue() {

		this.setName("ActionSendQueue");
		this.setDaemon(true);
		this.start();
	}

	private void initManagerConnection() {
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
					if (actionQueue.isEmpty()) {
						sleep(100);
					} else {
						ManagerAction action = actionQueue.poll();
						managerConnection.sendAction(action, null);
						logger.info("ActionSendQueue: "+action);
					}
				}

			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			} catch (IllegalStateException e) {
				logger.error(e.getMessage(), e);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				initManagerConnection();
			}
		}
	}

}
