package com.jiangyifen.ec.actions.iface;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.beans.Workload;
import com.jiangyifen.ec.util.ShareData;

public class GetUserTodayWorkloadAction extends BaseAction {



	/**
	 * 
	 */
	private static final long serialVersionUID = 3001631509876988272L;
	private String u;
	private String p;

	private String username;
	private String detail = "";
	private Workload workload = new Workload();

	public String execute() throws Exception {

		if (username != null && !username.equals("")) {
			Workload w = ShareData.usernameAndTodayWorkload.get(username);
			if(w!=null)
				workload = w;
		}
		
		if(detail.equalsIgnoreCase("true")){
			return "detail";
		}

		return SUCCESS;

	}

	public String getU() {
		return u;
	}

	public void setU(String u) {
		this.u = u;
	}

	public String getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setWorkload(Workload workload) {
		this.workload = workload;
	}

	public Workload getWorkload() {
		return workload;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getDetail() {
		return detail;
	}

}
