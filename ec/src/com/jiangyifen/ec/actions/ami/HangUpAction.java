package com.jiangyifen.ec.actions.ami;

import org.asteriskjava.manager.action.HangupAction;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.manager.ActionSendQueue;
import com.jiangyifen.ec.manager.MyManager;
import com.jiangyifen.ec.util.Config;

public class HangUpAction extends BaseAction {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6227357506223607521L;
	
	private String u;
	private String p;
	
	private String sipName;


	public String execute(){
		String iface_u = Config.props.getProperty("3rd_username");
		String iface_p = Config.props.getProperty("3rd_password");
		
		if(!(iface_u.equals(u) && iface_p.equals(p))){
			logger.info("DialAction: iface u and p is incorrect!");
			return LOGIN;
		}
		
		String temp = MyManager.getActiveChannel(sipName);
		String dstChannel = "";
		
		if (temp != null) {
			dstChannel = temp.split(";")[1];
		}
		System.out.println(temp+"/"+temp.split(";")[0]+"/"+temp.split(";")[1]);
		System.out.println(dstChannel);
		
		HangupAction action = new HangupAction(dstChannel);

		ActionSendQueue.actionQueue.offer(action);
		
//		String ip = Config.props.getProperty("manager_ip");
//		String username = Config.props.getProperty("manager_username");;
//		String password = Config.props.getProperty("manager_password");;
//		ManagerConnectionFactory factory = new ManagerConnectionFactory(ip, username, password);;
//		ManagerConnection managerConnection = factory.createManagerConnection();;
//
//		try {
//			managerConnection.login();
//			managerConnection.sendAction(action, null);
//			managerConnection.logoff();
//		} catch (IllegalStateException e) {
//			logger.error(e.getMessage(), e);
//		} catch (IOException e) {
//			logger.error(e.getMessage(), e);
//		} catch (AuthenticationFailedException e) {
//			logger.error(e.getMessage(), e);
//		} catch (TimeoutException e) {
//			logger.error(e.getMessage());
//		}catch(Exception e){
//			logger.error(e.getMessage(), e);
//		}

		return SUCCESS;

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



	public void setSipName(String sipName) {
		this.sipName = sipName;
	}



	public String getSipName() {
		return sipName;
	}

}
