package com.jiangyifen.ec.dao;

import java.io.Serializable;

public class ShiftConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1552433609430779898L;
	private Long id;
	private String name;
	private String phoneNumber;
	//group用来将ShiftConfig分组
	private String shiftGroup;

	private boolean sun = false;
	private boolean mon = false;
	private boolean tue = false;
	private boolean wed = false;
	private boolean thu = false;
	private boolean fri = false;
	private boolean sat = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isSun() {
		return sun;
	}

	public void setSun(boolean sun) {
		this.sun = sun;
	}

	public boolean isMon() {
		return mon;
	}

	public void setMon(boolean mon) {
		this.mon = mon;
	}

	public boolean isTue() {
		return tue;
	}

	public void setTue(boolean tue) {
		this.tue = tue;
	}

	public boolean isWed() {
		return wed;
	}

	public void setWed(boolean wed) {
		this.wed = wed;
	}

	public boolean isThu() {
		return thu;
	}

	public void setThu(boolean thu) {
		this.thu = thu;
	}

	public boolean isFri() {
		return fri;
	}

	public void setFri(boolean fri) {
		this.fri = fri;
	}

	public boolean isSat() {
		return sat;
	}

	public void setSat(boolean sat) {
		this.sat = sat;
	}


	public String getShiftGroup() {
		return shiftGroup;
	}

	public void setShiftGroup(String shiftGroup) {
		this.shiftGroup = shiftGroup;
	}

}
