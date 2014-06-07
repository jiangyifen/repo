package com.jiangyifen.ec.beans;

import java.util.ArrayList;

public class PopStatus {

	private String uniqueid = "";
	private String calleridNum = "";
	private String channel = "";
	private ArrayList<String> keyPressed = new ArrayList<String>();
	private String outlineNum;//来源号码

	public String getUniqueid() {
		return uniqueid;
	}

	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}


	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}


	public void setCalleridNum(String calleridNum) {
		this.calleridNum = calleridNum;
	}

	public String getCalleridNum() {
		return calleridNum;
	}

	public void setKeyPressed(ArrayList<String> keyPressed) {
		this.keyPressed = keyPressed;
	}

	public ArrayList<String> getKeyPressed() {
		return keyPressed;
	}

	public void setOutlineNum(String outlineNum) {
		this.outlineNum = outlineNum;
	}

	public String getOutlineNum() {
		return outlineNum;
	}
}
