package com.jiangyifen.ec.actions.iface;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.util.ShareData;

public class GetDpmtTopWorkloadAction extends BaseAction {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1196268093341438141L;
	
	private String u;
	private String p;

	private String dpmtname;
	private Double topCount=-1d;
	private Double topBillsec=-1d;
	private Double topCustomerCount=-1d;
	private Double topWorkload=-1d;
	

	public String execute() throws Exception {
//		logger.info("dpmtname="+dpmtname);
		if (dpmtname != null && !dpmtname.equals("")) {
			topCount = ShareData.dpmtTopCount.get(dpmtname);
			topBillsec = ShareData.dpmtTopBillsec.get(dpmtname);
			topCustomerCount = ShareData.dpmtTopCustomerCount.get(dpmtname);
			topWorkload = ShareData.dpmtTopWorkload.get(dpmtname);
//			logger.info("topCount="+topCount);
//			logger.info("topBillsec="+topBillsec);
//			logger.info("topCustomerCount="+topCustomerCount);
//			logger.info("topWorkload="+topWorkload);
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

	public void setDpmtname(String dpmtname) {
		this.dpmtname = dpmtname;
	}

	public String getDpmtname() {
		return dpmtname;
	}

	public void setTopCount(Double topCount) {
		this.topCount = topCount;
	}

	public Double getTopCount() {
		return topCount;
	}

	public void setTopBillsec(Double topBillsec) {
		this.topBillsec = topBillsec;
	}

	public Double getTopBillsec() {
		return topBillsec;
	}

	public void setTopWorkload(Double topWorkload) {
		this.topWorkload = topWorkload;
	}

	public Double getTopWorkload() {
		return topWorkload;
	}

	public void setTopCustomerCount(Double topCustomerCount) {
		this.topCustomerCount = topCustomerCount;
	}

	public Double getTopCustomerCount() {
		return topCustomerCount;
	}

}
