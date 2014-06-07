package com.jiangyifen.ec.dao;

import java.io.Serializable;
import java.util.Date;

public class DialTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 82700415393640344L;
	public static final String STATUS_RUNNING = "RUNNING";
	public static final String STATUS_STOP = "STOP";
	public static final String STATUS_FINISH = "FINISH";

	private Long id;
	private String taskName;
	private String queueName;
	private String status;
	private double rate = 1l;
	private Date startDate;
	private Date endDate;
	private boolean autoAssign = true;
	private Integer perority = 1;
	private Boolean hasAssignedToday = false;

	private Long myCustomerCount;
	private Long finishedDialTaskItemCount;
	private Long manualDialTaskItemCount;
	private Long readyDialTaskItemCount;
	private Long dialTaskItemCount;
	
	private Long smTaskCount;
	private Long smTaskDelivrdCount;
	
	private double customerRate;
	
	private String queueDescription;
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setDialTaskItemCount(Long dialTaskItemCount) {
		this.dialTaskItemCount = dialTaskItemCount;
	}

	public Long getDialTaskItemCount() {
		return dialTaskItemCount;
	}

	public void setFinishedDialTaskItemCount(Long finishedDialTaskItemCount) {
		this.finishedDialTaskItemCount = finishedDialTaskItemCount;
	}

	public Long getFinishedDialTaskItemCount() {
		return finishedDialTaskItemCount;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getRate() {
		return rate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setMyCustomerCount(Long myCustomerCount) {
		this.myCustomerCount = myCustomerCount;
	}

	public Long getMyCustomerCount() {
		return myCustomerCount;
	}

	public void setCustomerRate(double customerRate) {
		this.customerRate = customerRate;
	}

	public double getCustomerRate() {
		return customerRate;
	}

	public void setManualDialTaskItemCount(Long manualDialTaskItemCount) {
		this.manualDialTaskItemCount = manualDialTaskItemCount;
	}

	public Long getManualDialTaskItemCount() {
		return manualDialTaskItemCount;
	}

	public void setReadyDialTaskItemCount(Long readyDialTaskItemCount) {
		this.readyDialTaskItemCount = readyDialTaskItemCount;
	}

	public Long getReadyDialTaskItemCount() {
		return readyDialTaskItemCount;
	}

	public void setSmTaskCount(Long smTaskCount) {
		this.smTaskCount = smTaskCount;
	}

	public Long getSmTaskCount() {
		return smTaskCount;
	}

	public void setSmTaskDelivrdCount(Long smTaskDelivrdCount) {
		this.smTaskDelivrdCount = smTaskDelivrdCount;
	}

	public Long getSmTaskDelivrdCount() {
		return smTaskDelivrdCount;
	}

	public void setAutoAssign(boolean autoAssign) {
		this.autoAssign = autoAssign;
	}

	public boolean isAutoAssign() {
		return autoAssign;
	}

	public void setQueueDescription(String queueDescription) {
		this.queueDescription = queueDescription;
	}

	public String getQueueDescription() {
		return queueDescription;
	}

	public void setPerority(Integer perority) {
		this.perority = perority;
	}

	public Integer getPerority() {
		return perority;
	}

	public void setHasAssignedToday(Boolean hasAssignedToday) {
		this.hasAssignedToday = hasAssignedToday;
	}

	public Boolean getHasAssignedToday() {
		return hasAssignedToday;
	}

}
