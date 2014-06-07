package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.dao.Notice;
import com.opensymphony.xwork2.ActionContext;

public class NoticeGetAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2391860460779157875L;

	private final Log logger = LogFactory.getLog(getClass());

	private final int PAGESIZE = 25;

	private List<Notice> noticeList;

	private String beginTime;
	private String endTime;
	private String title;
	private String content;

	private int noticeCount;

	private int pageIndex;
	private int maxPageIndex = 0;
	private List<Integer> pages = new ArrayList<Integer>();

	public String execute() throws Exception {

		
		Map<String, Object> session = ActionContext.getContext().getSession();
		if(beginTime==null)
			beginTime = (String)session.get("oneMonthAgo");
		
		if(endTime==null)
			endTime = (String)session.get("today");
		
		if(title==null)
			title="";
		if(content==null)
			content="";

		if (pageIndex < 1)
			pageIndex = 1;

		noticeCount = (int) noticeManager.getCount(beginTime, endTime, title,
				content);
		if ((noticeCount % PAGESIZE) == 0) {
			maxPageIndex = (noticeCount / PAGESIZE);
		} else {
			maxPageIndex = (noticeCount / PAGESIZE) + 1;
		}

		for (int i = 0; i < maxPageIndex; i++) {
			pages.add(i + 1);
		}
		if (pageIndex > maxPageIndex)
			pageIndex = maxPageIndex;


		
		List<Notice> list = noticeManager.find(PAGESIZE, maxPageIndex,
				beginTime, endTime, title, content);

		setNoticeList(list);
		logger.info("GetNoticeAction: succeed");
		return SUCCESS;

	}

	public List<Notice> getNoticeList() {
		return noticeList;
	}

	public void setNoticeList(List<Notice> noticeList) {
		this.noticeList = noticeList;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getNoticeCount() {
		return noticeCount;
	}

	public void setNoticeCount(int noticeCount) {
		this.noticeCount = noticeCount;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getMaxPageIndex() {
		return maxPageIndex;
	}

	public void setMaxPageIndex(int maxPageIndex) {
		this.maxPageIndex = maxPageIndex;
	}

	public List<Integer> getPages() {
		return pages;
	}

	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}

	public Log getLogger() {
		return logger;
	}

	public int getPAGESIZE() {
		return PAGESIZE;
	}

}
