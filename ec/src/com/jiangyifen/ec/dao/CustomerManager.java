package com.jiangyifen.ec.dao;

import java.io.Serializable;

public class CustomerManager  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7500490872472276103L;

	private String customerManagerLoginName;
	private String exten;

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
