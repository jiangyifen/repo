package com.jiangyifen.ec.dao;

import java.io.Serializable;

public class ExtenAndOutline  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1832699088707119476L;
	
	private String exten;
	private String outline;
	
	public String getExten() {
		return exten;
	}
	public void setExten(String exten) {
		this.exten = exten;
	}
	public String getOutline() {
		return outline;
	}
	public void setOutline(String outline) {
		this.outline = outline;
	}

}
