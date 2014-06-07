package com.jiangyifen.ec.beans;

public class Workload {

	private int incount = 0;
	private double induration = 0;
	private int outcount=0;
	private double outduration = 0;

	private int count = 0;
	private double duration = 0;
	private double customerCount = 0;
	private int workload = 0;
	
	public void setCount(int count) {
		this.count = count;
	}
	public int getCount() {
		return incount+outcount;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
	public double getDuration() {
		return induration+outduration;
	}
	public void setWorkload(int workload) {
		this.workload = workload;
	}
	public int getWorkload() {
		workload = (int)(customerCount*50*0.4+count*0.3+duration*0.3);
		return workload;
	}
	public void setCustomerCount(double customerCount) {
		this.customerCount = customerCount;
	}
	public double getCustomerCount() {
		return customerCount;
	}
	public int getIncount() {
		return incount;
	}
	public void setIncount(int incount) {
		this.incount = incount;
	}
	public double getInduration() {
		return induration;
	}
	public void setInduration(double induration) {
		this.induration = induration;
	}
	public int getOutcount() {
		return outcount;
	}
	public void setOutcount(int outcount) {
		this.outcount = outcount;
	}
	public double getOutduration() {
		return outduration;
	}
	public void setOutduration(double outduration) {
		this.outduration = outduration;
	}
}
