package com.jiangyifen.ec.actions;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.dao.ShiftConfig;

public class ShiftConfigGetAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6885795402937414416L;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private List<ShiftConfig> shiftConfigList;

	public String execute() throws Exception {
		
		logger.info("-------------ShiftConfigGetAction");
		shiftConfigList = shiftConfigManager.getAll();

//		for (ShiftConfig o : shiftConfigList) {
//
//			logger.info("Load ShiftConfig succeed! " + o.getName() + "/"
//					+ o.getPhoneNumber() + " isSun "+ o.isSun()+ " "+ " ");
//		}

		return SUCCESS;

	}

	public List<ShiftConfig> getShiftConfigList() {
		return shiftConfigList;
	}

	public void setShiftConfigList(List<ShiftConfig> shiftConfigList) {
		this.shiftConfigList = shiftConfigList;
	}

}
