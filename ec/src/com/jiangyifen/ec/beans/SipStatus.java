package com.jiangyifen.ec.beans;

public class SipStatus {

	// same as asterisk queue member status
	public static final int STATUS_UNKNOW = 0;
	public static final int STATUS_NOT_IN_USE = 1;
	public static final int STATUS_IN_USE = 2;
	public static final int STATUS_BUSY = 3;
	public static final int STATUS_INVALID = 4;
	public static final int STATUS_UNAVAILABLE = 5;
	public static final int STATUS_RINGING = 6;
	// status added by jiangyifen
	public static final int STATUS_LONG_TIME_NOT_IN_USE = 7;
	public static final int STATUS_NO_LOGIN_BUSY = 8;
	public static final int STATUS_NO_LOGIN_NOT_IN_USE = 9;

	private String sipName;
	private int status = -1;
	private String linkedCallerId="";
	private int seconds=0;
	private String loginusername="0";
	private String name;
	private String hid;
	private boolean login = false;
	private long turnToFreeTime = System.currentTimeMillis();
	private boolean paused = false; 

	private long countCallin=0;
	private long countCallout=0;
	
	public void setSipName(String sipName) {
		this.sipName = sipName;
	}

	public String getSipName() {
		return sipName;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public boolean isLogin() {
		return login;
	}

	public void setTurnToFreeTime(long turnToFreeTime) {
		this.turnToFreeTime = turnToFreeTime;
	}

	public long getTurnToFreeTime() {
		return turnToFreeTime;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}

	public String getHid() {
		return hid;
	}

	public void setLoginusername(String loginusername) {
		this.loginusername = loginusername;
	}

	public String getLoginusername() {
		return loginusername;
	}

	public void setCountCallin(long countCallin) {
		this.countCallin = countCallin;
	}

	public long getCountCallin() {
		return countCallin;
	}

	public void setCountCallout(long countCallout) {
		this.countCallout = countCallout;
	}

	public long getCountCallout() {
		return countCallout;
	}

	public void setLinkedCallerId(String linkedCallerId) {
		this.linkedCallerId = linkedCallerId;
	}

	public String getLinkedCallerId() {
		return linkedCallerId;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public int getSeconds() {
		return seconds;
	}


}