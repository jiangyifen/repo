package com.jiangyifen.ec.actions.iface;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.dao.CustomerManager;

public class CrmExtenUpdateAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3915484470605104529L;
	
	
	private String customerManagerLoginName;
	private String exten;

	public String execute() throws Exception {

		if (customerManagerLoginName != null && exten != null) {
			customerManagerLoginName = new String(customerManagerLoginName.getBytes("ISO-8859-1"));
			CustomerManager customerManager = new CustomerManager();
			customerManager
					.setCustomerManagerLoginName(customerManagerLoginName);
			customerManager.setExten(exten);

			customerManagerManager.update(customerManager);
			logger.info("Customer Manager " + customerManagerLoginName + " logged in on exten " + exten + ".");
		}
		
		return SUCCESS;

	}

	public void setCustomerManagerLoginName(String customerManagerLoginName) {
		this.customerManagerLoginName = customerManagerLoginName;
	}

	public String getCustomerManagerLoginName() {
		return customerManagerLoginName;
	}

	public void setExten(String exten) {
		this.exten = exten;
	}

	public String getExten() {
		return exten;
	}
	
}
