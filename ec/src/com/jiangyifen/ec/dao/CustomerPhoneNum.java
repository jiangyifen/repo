package com.jiangyifen.ec.dao;

import java.io.Serializable;

public class CustomerPhoneNum  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8397826928647091470L;

	private String customerPhoneNumber;
	private String customerManagerLoginName;

	public void setCustomerPhoneNumber(String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}

	public String getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}

	public void setCustomerManagerLoginName(String customerManagerLoginName) {
		this.customerManagerLoginName = customerManagerLoginName;
	}

	public String getCustomerManagerLoginName() {
		return customerManagerLoginName;
	}

}
