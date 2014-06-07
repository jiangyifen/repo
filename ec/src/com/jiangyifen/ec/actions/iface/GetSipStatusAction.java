package com.jiangyifen.ec.actions.iface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.beans.SipStatus;
import com.jiangyifen.ec.util.ShareData;

@SuppressWarnings("unchecked")
public class GetSipStatusAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 66521009582713039L;

	public ArrayList<SipStatus> sipStatus = null;

	private HashMap<String, SipStatus> sipStatusMap = null;

	private int total = -1;
	private int longTimeFree = -1;
	private int free = -1;
	private int busy = -1;
	private int notLoginFree = -1;
	private int notLoginBusy = -1;
	private double workRate = -1;

	private int loginCount = 0;

	private String sipname;

	private int pagesize = 20;
	private String pageIndex;
	private int maxPageIndex = 0;
	private List<Integer> pages = new ArrayList<Integer>();

	public String execute() throws Exception {
		try {
			if (sipname != null && !sipname.toLowerCase().equals("all")) {
				// 通过接口获取单个分机状态
				sipStatusMap = (HashMap<String, SipStatus>) ShareData.sipStatusMap
						.clone();
				SipStatus s = sipStatusMap.get(sipname);
				sipStatus = new ArrayList<SipStatus>();
				sipStatus.add(s);
				return "single";
			} else if (sipname != null && sipname.toLowerCase().equals("all")) {
				// 通过接口获取所有分机状态
				sipStatus = (ArrayList<SipStatus>) ShareData.sipStatusList
						.clone();
				return "single";
			} else {
				// 网页上显示分机状态
				sipStatus = (ArrayList<SipStatus>) ShareData.sipStatusList
						.clone();
				// total = ShareData.allSipName.size();
				total = sipStatus.size();
				//
				// System.out.println(sipStatus.size());
				// System.out.println(total);
				//
				longTimeFree = ShareData.sipLongTimeFree;
				free = ShareData.sipFree;
				busy = ShareData.sipBusy;
				notLoginFree = ShareData.sipNotLoginFree;
				notLoginBusy = ShareData.sipNotLoginBusy;

				loginCount = longTimeFree + free + busy;

				double a = (double) busy;
				double b = (double) free;
				double c = (double) longTimeFree;
				double d = (double) notLoginBusy;
				setWorkRate((double) Math.round((a + d) * 10000
						/ (a + b + c + d)) / 100);

				// 分页显示
				int pageIdx = 1;
				try {
					pageIdx = new Integer(pageIndex);
				} catch (NumberFormatException e) {
					pageIdx = 1;
				}

				if (pageIdx < 1)
					pageIdx = 1;
				
				if ((total % pagesize) == 0) {
					maxPageIndex = (total / pagesize);
				} else {
					maxPageIndex = (total / pagesize) + 1;
				}
				if(maxPageIndex==0){
					maxPageIndex=1;
				}
				for (int i = 0; i < maxPageIndex; i++) {
					pages.add(i + 1);
				}
				if (pageIdx > maxPageIndex)
					pageIdx = maxPageIndex;
				
				int fromIndex = (pageIdx - 1) * pagesize;
				int toIndex = pageIdx * pagesize;
				if (toIndex > total) {
					toIndex = total;
				}

				ArrayList<SipStatus> result = new ArrayList<SipStatus>();
//				System.out.println("pageIdx="+pageIdx);
//				System.out.println("fromIndex="+fromIndex);
//				System.out.println("toIndex="+toIndex);
//				System.out.println("sipStatus.size()="+sipStatus.size());
				for (int i = fromIndex; i < toIndex; i++) {
//					System.out.println("i="+i);
					if (i < sipStatus.size())
						result.add(sipStatus.get(i));
				}
				sipStatus = result;

				// 分页显示
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return SUCCESS;

	}

	public void setFree(int free) {
		this.free = free;
	}

	public int getFree() {
		return free;
	}

	public void setBusy(int busy) {
		this.busy = busy;
	}

	public int getBusy() {
		return busy;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotal() {
		return total;
	}

	public void setNotLoginFree(int notLoginFree) {
		this.notLoginFree = notLoginFree;
	}

	public int getNotLoginFree() {
		return notLoginFree;
	}

	public void setNotLoginBusy(int notLoginBusy) {
		this.notLoginBusy = notLoginBusy;
	}

	public int getNotLoginBusy() {
		return notLoginBusy;
	}

	public void setSipname(String sipname) {
		this.sipname = sipname;
	}

	public String getSipname() {
		return sipname;
	}

	public void setSipStatusMap(HashMap<String, SipStatus> sipStatusMap) {
		this.sipStatusMap = sipStatusMap;
	}

	public HashMap<String, SipStatus> getSipStatusMap() {
		return sipStatusMap;
	}

	public void setWorkRate(double workRate) {
		this.workRate = workRate;
	}

	public double getWorkRate() {
		return workRate;
	}

	public void setLongTimeFree(int longTimeFree) {
		this.longTimeFree = longTimeFree;
	}

	public int getLongTimeFree() {
		return longTimeFree;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	public int getLoginCount() {
		return loginCount;
	}

	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}

	public List<Integer> getPages() {
		return pages;
	}

	public void setMaxPageIndex(int maxPageIndex) {
		this.maxPageIndex = maxPageIndex;
	}

	public int getMaxPageIndex() {
		return maxPageIndex;
	}

	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getPageIndex() {
		return pageIndex;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getPagesize() {
		return pagesize;
	}
}
