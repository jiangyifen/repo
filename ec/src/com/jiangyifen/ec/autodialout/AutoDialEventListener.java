package com.jiangyifen.ec.autodialout;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.asteriskjava.manager.AbstractManagerEventListener;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerConnectionState;
import org.asteriskjava.manager.action.RedirectAction;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.LinkEvent;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.util.Config;

public class AutoDialEventListener extends AbstractManagerEventListener implements Runnable {

	private static final Log logger = LogFactory.getLog(AutoDialEventListener.class);
	private String ip;
	private String username;
	private String password;
	
	private static ManagerConnectionFactory factory;
	private static ManagerConnection managerConnection;
	
	private ConcurrentLinkedQueue<RedirectAction> redirectActionQueue = new ConcurrentLinkedQueue<RedirectAction>();

	public AutoDialEventListener() {
		ip = Config.props.getProperty("manager_ip");
		username = Config.props.getProperty("manager_username");
		password = Config.props.getProperty("manager_password");
		
		factory = new ManagerConnectionFactory(ip, username, password);
		
		Thread t = new Thread(this);
		t.setName("AutoDialEventListener");
		t.setDaemon(true);
		t.start();

	}

	private static void initManagerConnection(){
		if (managerConnection != null) {
			if (managerConnection.getState() == ManagerConnectionState.CONNECTED
					|| managerConnection.getState() == ManagerConnectionState.RECONNECTING) {
				managerConnection.logoff();
			}
		}
		managerConnection = factory.createManagerConnection();
		try {
			managerConnection.login();
			Thread.sleep(1000);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} 
	}
	
	private boolean isAutoDialoutChannel(String channel) {
		if (channel.startsWith("Local/99")
				&& channel.indexOf("channelpool") != -1) {
			return true;
		}
		return false;
	}

	protected void handleEvent(LinkEvent event) {

		String channel1 = event.getChannel1();
		String channel2 = event.getChannel2();

		// 实现自动外呼，外线接通后转给座席
		// 如果是通过Local channel临时转接的channel，则在接通后将外线转给座席，localchannel挂断
		if (isAutoDialoutChannel(channel1)) {
			String redirectToExten = channel1.substring("Local/".length(),
					channel1.indexOf("@"));

			RedirectAction redirectAction = new RedirectAction(channel2,
					"incoming", redirectToExten, 1);

			redirectActionQueue.add(redirectAction);

		}
	}
	
	protected void handleEvent(HangupEvent event) {

		String channel = event.getChannel();
		if (isAutoDialoutChannel(channel)) {
			String queueName = channel.substring("Local/999".length(), channel
					.indexOf("@"));
			DialTaskWorkThreadManager.dialingOutTaskCountDecrease(queueName, 1);
		}
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(500);

				if(managerConnection == null || managerConnection.getState()!=ManagerConnectionState.CONNECTED){
					initManagerConnection();
				}
				
				while(true){
					if(!redirectActionQueue.isEmpty()){
						RedirectAction ra = redirectActionQueue.poll();
//						if(managerConnection == null || managerConnection.getState()!=ManagerConnectionState.CONNECTED){
//							initManagerConnection();
//						}
						managerConnection.sendAction(ra);
						Thread.sleep(50);
					}else{
						Thread.sleep(500);
					}
				}
				
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				logger.error("managerConnection.getState()="
						+ managerConnection.getState());
				initManagerConnection();
				
			} 
		}
	}
}
