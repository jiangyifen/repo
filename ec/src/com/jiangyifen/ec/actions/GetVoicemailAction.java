package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.List;
import com.jiangyifen.ec.dao.Voicemail;

public class GetVoicemailAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6537108250536246619L;

	private final int PAGESIZE = 25;
	
	private List<Voicemail> vm;
	private String mailbox;
	private String src;
	private String beginTime;
	private String endTime;
	private int pageIndex;
	private int vmCount;

	private int maxPageIndex = 0;
	private List<Integer> pages = new ArrayList<Integer>();

	public String execute() throws Exception {
		
		if (pageIndex < 1)
			pageIndex = 1;
		
		List<Voicemail> list = vmManager.findVoicemail(beginTime, endTime, src, mailbox, PAGESIZE, pageIndex);
		
		vmCount = vmManager.getVmCount(beginTime, endTime, src,mailbox);
		if ((vmCount % PAGESIZE) == 0) {
			maxPageIndex = (vmCount / PAGESIZE);
		} else {
			maxPageIndex = (vmCount / PAGESIZE) + 1;
		}

		for (int i = 0; i < maxPageIndex; i++) {
			pages.add(i + 1);
		}
		if (pageIndex > maxPageIndex)
			pageIndex = maxPageIndex;

		setVm(list);
		return SUCCESS;
	}

	public void setVm(List<Voicemail> vm) {
		this.vm = vm;
	}

	public List<Voicemail> getVm() {
		return vm;
	}


	public void setMaxPageIndex(int maxPageIndex) {
		this.maxPageIndex = maxPageIndex;
	}

	public long getMaxPageIndex() {
		return maxPageIndex;
	}

	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}

	public List<Integer> getPages() {
		return pages;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getSrc() {
		return src;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageIndex() {
		return pageIndex;
	}
	
	public void setVmCount(int cdrCount) {
		this.vmCount = cdrCount;
	}

	public int getVmCount() {
		return vmCount;
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

}
