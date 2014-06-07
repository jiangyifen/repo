package com.jiangyifen.ec.beans;

public class AgentMinLoginTimeAndMaxLogoutTime {
	
	private String date;
	private String username;
	private String name;
	private String minLoginTime;
	private String maxLogoutTime;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setMinLoginTime(String minLoginTime) {
		this.minLoginTime = minLoginTime;
	}
	public String getMinLoginTime() {
		return minLoginTime;
	}
	public void setMaxLogoutTime(String maxLogoutTime) {
		this.maxLogoutTime = maxLogoutTime;
	}
	public String getMaxLogoutTime() {
		return maxLogoutTime;
	}

}
