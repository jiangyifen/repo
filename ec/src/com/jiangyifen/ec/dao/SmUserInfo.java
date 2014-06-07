package com.jiangyifen.ec.dao;

import java.io.Serializable;

public class SmUserInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 392924536272236950L;
	
	public static final String CT = "CT";
	public static final String CMCC = "CMCC";
	
	private String accountId;
	private String username;
	private String password;
	private String host;
	private int port;
	private String operator;  //CT, CMCC

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getHost() {
		return host;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getPort() {
		return port;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOperator() {
		return operator;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	

}
