package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.List;

import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.Sip;

public class SipGetOneAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1398704764009832412L;
	private Long id;
	private List<Sip> sips = new ArrayList<Sip>();
	
	private List<Department> dpmts = new ArrayList<Department>();
	

	public String execute() throws Exception {
		setDpmts(departmentManager.getDepartments());
		
		sips.add(sipManager.getSip(id));
		return SUCCESS;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setSips(List<Sip> sips) {
		this.sips = sips;
	}

	public List<Sip> getSips() {
		return sips;
	}

	public void setDpmts(List<Department> dpmts) {
		this.dpmts = dpmts;
	}

	public List<Department> getDpmts() {
		return dpmts;
	}

}
