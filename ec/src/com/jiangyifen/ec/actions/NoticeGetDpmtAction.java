package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.dao.Department;
import com.opensymphony.xwork2.ActionContext;

public class NoticeGetDpmtAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1802102690751326753L;

	private final Log logger = LogFactory.getLog(getClass());

	private List<Department> dpmts = null;

	@SuppressWarnings("unchecked")
	public String execute() throws Exception {

		Set<Department> departments = (Set<Department>) ActionContext
				.getContext().getSession().get("departments");
		if (departments == null || departments.size() == 0) {
			dpmts = departmentManager.getDepartments();
		} else {
			dpmts = new ArrayList<Department>();
			for (Department d : departments) {
				dpmts.add(d);
				logger.info(d.getDepartmentname());
			}
		}

		return SUCCESS;

	}

	public void setDpmts(List<Department> dpmts) {
		this.dpmts = dpmts;
	}

	public List<Department> getDpmts() {
		return dpmts;
	}

}
