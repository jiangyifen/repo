package com.jiangyifen.ec.dao;

import java.io.Serializable;
import java.util.Date;

public class HoldEventLog implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6742306176676252009L;
	
	private Long id;
	private String exten;
	private String channel;
	private String uniqueid;
	private Date holdDate;
	private Date unholdDate;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getUniqueid() {
		return uniqueid;
	}
	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}
	public String getExten() {
		return exten;
	}
	public void setExten(String exten) {
		this.exten = exten;
	}
	public Date getHoldDate() {
		return holdDate;
	}
	public void setHoldDate(Date holdDate) {
		this.holdDate = holdDate;
	}
	public Date getUnholdDate() {
		return unholdDate;
	}
	public void setUnholdDate(Date unholdDate) {
		this.unholdDate = unholdDate;
	}


}
