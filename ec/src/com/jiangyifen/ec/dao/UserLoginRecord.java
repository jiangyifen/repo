package com.jiangyifen.ec.dao;

import java.io.Serializable;
import java.util.Date;

public class UserLoginRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 467846692150953797L;
	
	private Long id;
	private String exten;
	private String username;
	private Date loginDate;
	private Date logoutDate;

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public Date getLogoutDate() {
		return logoutDate;
	}

	public void setLogoutDate(Date logoutDate) {
		this.logoutDate = logoutDate;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setExten(String exten) {
		this.exten = exten;
	}

	public String getExten() {
		return exten;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}


}
