package com.jiangyifen.ec.beans;

import java.util.ArrayList;

import org.asteriskjava.manager.event.QueueEntryEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.asteriskjava.manager.event.QueueParamsEvent;

public class QueueStatus {
	private String queueName;
	private String description;
	
	private QueueParamsEvent queueParamsEvent;
	private ArrayList<QueueMemberEvent> queueMemberList = new ArrayList<QueueMemberEvent>();
	private ArrayList<QueueEntryEvent> queueEntryList = new ArrayList<QueueEntryEvent>();

	private int free = 0;
	private int ringing = 0;
	private int busy = 0;
	private int unavailable = 0;

	private int queueMemberCount = 0;
	private int pausedMemberCount = 0;
	private int unpausedMemberCount = 0;

	private int loginMemberCount = 0;
	private int loginPausedMemberCount = 0;
	private int loginUnpausedMemberCount = 0;
	
	public QueueParamsEvent getQueueParamsEvent() {
		return queueParamsEvent;
	}
	public void setQueueParamsEvent(QueueParamsEvent queueParamsEvent) {
		this.queueParamsEvent = queueParamsEvent;
	}
	public ArrayList<QueueMemberEvent> getQueueMemberList() {
		return queueMemberList;
	}
	public void setQueueMemberList(ArrayList<QueueMemberEvent> queueMemberList) {
		this.queueMemberList = queueMemberList;
	}
	public ArrayList<QueueEntryEvent> getQueueEntryList() {
		return queueEntryList;
	}
	public void setQueueEntryList(ArrayList<QueueEntryEvent> queueEntryList) {
		this.queueEntryList = queueEntryList;
	}
	public int getFree() {
		return free;
	}
	public void setFree(int free) {
		this.free = free;
	}
	public int getRinging() {
		return ringing;
	}
	public void setRinging(int ringing) {
		this.ringing = ringing;
	}
	public int getBusy() {
		return busy;
	}
	public void setBusy(int busy) {
		this.busy = busy;
	}
	public int getUnavailable() {
		return unavailable;
	}
	public void setUnavailable(int unavailable) {
		this.unavailable = unavailable;
	}
	public int getQueueMemberCount() {
		return queueMemberCount;
	}
	public void setQueueMemberCount(int queueMemberCount) {
		this.queueMemberCount = queueMemberCount;
	}
	public int getPausedMemberCount() {
		return pausedMemberCount;
	}
	public void setPausedMemberCount(int pausedMemberCount) {
		this.pausedMemberCount = pausedMemberCount;
	}
	public int getUnpausedMemberCount() {
		return unpausedMemberCount;
	}
	public void setUnpausedMemberCount(int unpausedMemberCount) {
		this.unpausedMemberCount = unpausedMemberCount;
	}
	public int getLoginMemberCount() {
		return loginMemberCount;
	}
	public void setLoginMemberCount(int loginMemberCount) {
		this.loginMemberCount = loginMemberCount;
	}
	public int getLoginPausedMemberCount() {
		return loginPausedMemberCount;
	}
	public void setLoginPausedMemberCount(int loginPausedMemberCount) {
		this.loginPausedMemberCount = loginPausedMemberCount;
	}
	public int getLoginUnpausedMemberCount() {
		return loginUnpausedMemberCount;
	}
	public void setLoginUnpausedMemberCount(int loginUnpausedMemberCount) {
		this.loginUnpausedMemberCount = loginUnpausedMemberCount;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	public String getQueueName() {
		return queueName;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}

}