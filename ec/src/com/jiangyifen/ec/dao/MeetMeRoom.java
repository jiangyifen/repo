package com.jiangyifen.ec.dao;

import java.io.Serializable;

public class MeetMeRoom implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6252960125055151394L;
	
	private String confno;
	private String pin;
	private String adminpin;
	private int members=0;
	
	public String getConfno() {
		return confno;
	}
	public void setConfno(String confno) {
		this.confno = confno;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getAdminpin() {
		return adminpin;
	}
	public void setAdminpin(String adminpin) {
		this.adminpin = adminpin;
	}
	public int getMembers() {
		return members;
	}
	public void setMembers(int members) {
		this.members = members;
	}
	
	
}
