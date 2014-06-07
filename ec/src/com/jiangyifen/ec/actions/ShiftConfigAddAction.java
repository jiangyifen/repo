package com.jiangyifen.ec.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.dao.ShiftConfig;

public class ShiftConfigAddAction extends BaseAction {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4580503552355191918L;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private String name;
	private String shiftGroup;
	private String phoneNumber;

	public String execute() throws Exception {
		logger.info("ShiftConfigAddAction: "+name);
		logger.info("ShiftConfigAddAction: "+phoneNumber);
		if(phoneNumber==null || phoneNumber.equals("")){
			return INPUT;
		}
		if(shiftGroup==null || shiftGroup.equals("")){
			return INPUT;
		}
		
		
		ShiftConfig sc = new ShiftConfig();
		sc.setName(name);
		sc.setShiftGroup(shiftGroup);
		sc.setPhoneNumber(phoneNumber);
		
		shiftConfigManager.save(sc);
		
		logger.info("Add ShiftConfig succeed! " + name + "/" + phoneNumber+"/"+shiftGroup);

		return SUCCESS;

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


	public String getShiftGroup() {
		return shiftGroup;
	}


	public void setShiftGroup(String shiftGroup) {
		this.shiftGroup = shiftGroup;
	}

}
