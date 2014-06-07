package com.jiangyifen.ec.actions.iface;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.beans.Workload;
import com.jiangyifen.ec.util.ShareData;

public class GetUserAvgWorkloadAction extends BaseAction {


	/**
	 * 
	 */
	private static final long serialVersionUID = -4605178718102580734L;
	private String u;
	private String p;

	private String username;
	private Workload workload = new Workload();

	public String execute() throws Exception {

		if (username != null && !username.equals("")) {
			Workload w = ShareData.usernameAndAvgWorkload.get(username);
			if(w!=null)
				workload = w;
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

}
