package com.jiangyifen.ec.beans;

public class BatchSmTaskStatus {
	
	private String batchNumber;
	private long totalCount;
	private long readyCount;
	private long waitingCount;
	private long delivrdCount;
	private String status;
	
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public long getReadyCount() {
		return readyCount;
	}
	public void setReadyCount(long readyCount) {
		this.readyCount = readyCount;
	}
	public long getWaitingCount() {
		return waitingCount;
	}
	public void setWaitingCount(long waitingCount) {
		this.waitingCount = waitingCount;
	}
	public long getDelivrdCount() {
		return delivrdCount;
	}
	public void setDelivrdCount(long delivrdCount) {
		this.delivrdCount = delivrdCount;
	}
	public long getOtherCount() {
		return otherCount;
	}
	public void setOtherCount(long otherCount) {
		this.otherCount = otherCount;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
	private long otherCount;
	
}
