package com.jiangyifen.ec.actions.ami;

import java.io.IOException;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.QueuePauseAction;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.util.Config;

public class QueueMemberPauseAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2553025150213581575L;
	private String u;
	private String p;
	
	private String queue;
	private String iface;
	private boolean paused;

	public String execute() throws Exception {
		String iface_u = Config.props.getProperty("3rd_username");
		String iface_p = Config.props.getProperty("3rd_password");
		
		if(!(iface_u.equals(u) && iface_p.equals(p))){
			return LOGIN;
		}
		

		QueuePauseAction action = new QueuePauseAction();
		if (queue != null && !queue.equals("")) {
			action.setQueue(queue);
		}
		action.setInterface(iface);
		action.setPaused(paused);

		String ip = Config.props.getProperty("manager_ip");
		String username = Config.props.getProperty("manager_username");;
		String password = Config.props.getProperty("manager_password");;
		ManagerConnectionFactory factory = new ManagerConnectionFactory(ip, username, password);;
		ManagerConnection managerConnection = factory.createManagerConnection();;

		logger.info("QueueMemberPauseAction: Interface " + iface + " in queue " + queue + " paused=" + paused);
		
		try {

			managerConnection.login();
			managerConnection.sendAction(action, null);
			managerConnection.logoff();
			
		} catch (IllegalStateException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (AuthenticationFailedException e) {
			logger.error(e.getMessage(), e);
		} catch (TimeoutException e) {
			logger.error(e.getMessage());
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		
		

		
		return SUCCESS;

	}
	
	public void setQueue(String queue) {
		this.queue = queue;
	}

	public String getQueue() {
		return queue;
	}

	public void setIface(String iface) {
		this.iface = iface;
	}

	public String getIface() {
		return iface;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setU(String u) {
		this.u = u;
	}

	public String getU() {
		return u;
	}

	public void setP(String p) {
		this.p = p;
	}

	public String getP() {
		return p;
	}

}
