package com.jiangyifen.ec.beans;

public class AgentPauseDetail {
	
	private int id;
	private String username;
	private String name;
	private String queue;
	private String membername;
	private String pausedate;
	private String unpausedate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getQueue() {
		return queue;
	}
	public void setQueue(String queue) {
		this.queue = queue;
	}
	public String getMembername() {
		return membername;
	}
	public void setMembername(String membername) {
		this.membername = membername;
	}
	public String getPausedate() {
		return pausedate;
	}
	public void setPausedate(String pausedate) {
		this.pausedate = pausedate;
	}
	public String getUnpausedate() {
		return unpausedate;
	}
	public void setUnpausedate(String unpausedate) {
		this.unpausedate = unpausedate;
	}
	
}
