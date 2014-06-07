package com.jiangyifen.ec.actions;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2156813812150290239L;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	private List<String> listCheck;

	public String execute() throws Exception {

		for (String s : listCheck) {
			logger.info(s);
		}
		return SUCCESS;

	}

	public List<String> getListCheck() {
		return listCheck;
	}

	public void setListCheck(List<String> listCheck) {
		this.listCheck = listCheck;
	}

}
