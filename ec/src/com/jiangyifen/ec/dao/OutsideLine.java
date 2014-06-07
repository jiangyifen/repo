package com.jiangyifen.ec.dao;

import java.io.Serializable;

public class OutsideLine  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -831657246207832616L;
	private String phoneNumber;
	private String description;
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
