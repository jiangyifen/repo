package com.jiangyifen.ec.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShiftConfigDeleteAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4580503552355191918L;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private Long id;

	public String execute() throws Exception {

		shiftConfigManager.delete(id);
		logger.info("delete shiftConfig: " + id);

		return SUCCESS;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
