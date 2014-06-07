package com.jiangyifen.ec.actions.ami;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.action.OriginateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.util.Config;

public class DialAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5908959556259293018L;
	
	private Logger logger = LoggerFactory.getLogger(DialAction.class);
	
	
	private String u;
	private String p;
	
	private String channel;
	private String context = "outgoing";
	private String exten;
	private Integer priority = 1;

	public String execute(){
////		String iface_u = Config.props.getProperty("3rd_username");
////		String iface_p = Config.props.getProperty("3rd_password");
//		
////		if(!(iface_u.equals(u) && iface_p.equals(p))){
////			logger.info("DialAction: iface u and p is incorrect!");
////			return LOGIN;
////		}

		OriginateAction action = new OriginateAction();
		action.setChannel("SIP/"+channel);
		action.setCallerId(channel);
		action.setContext(context);
		action.setExten(exten);
		action.setPriority(priority);
		
		logger.info("DialAction: channel="+action.getChannel()+", callerid=" + channel + ", context="+context + ", exten="+exten);

//		ActionSendQueue.actionQueue.offer(action);
		
		String ip = Config.props.getProperty("manager_ip");
		String username = Config.props.getProperty("manager_username");
		String password = Config.props.getProperty("manager_password");
		ManagerConnectionFactory factory = new ManagerConnectionFactory(ip, username, password);
		ManagerConnection managerConnection = factory.createManagerConnection();

		try {
			managerConnection.login();
			managerConnection.sendAction(action, null);
			managerConnection.logoff();
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}

		return SUCCESS;

	}


	public void setChannel(String channel) {
		this.channel = channel;
	}


	public String getChannel() {
		return channel;
	}


	public void setContext(String context) {
		this.context = context;
	}


	public String getContext() {
		return context;
	}


	public void setExten(String exten) {
		this.exten = exten;
	}


	public String getExten() {
		return exten;
	}


	public void setPriority(Integer priority) {
		this.priority = priority;
	}


	public Integer getPriority() {
		return priority;
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
