package com.jiangyifen.ec.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.dao.SmTask;
import com.jiangyifen.ec.util.Config;
import com.opensymphony.xwork2.ActionContext;

public class GetSmAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3099507759460813966L;

	private final Log logger = LogFactory.getLog(getClass());

	private final int PAGESIZE = 25;

	private List<SmTask> smTasks;

	private String beginTime;
	private String endTime;
	private String content;
	private String mobile;
	private String status;
	private String senderId;
	private String batchNumber;

	private int smCount;

	private String excel;

	private int pageIndex;
	private int lastornext;
	private int maxPageIndex = 0;
	private List<Integer> pages = new ArrayList<Integer>();

	public String execute() throws Exception {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1,date2;
		try {
			date1 = df.parse(beginTime + " 00:00:00");
		} catch (ParseException e) {
			date1 = new Date();
		}

		try {
			date2 = df.parse(beginTime + " 00:00:00");
		} catch (ParseException e) {
			date2 = new Date();
		}
		long l = date2.getTime() - date1.getTime();
		long days = (l / (24 * 60 * 60 * 1000) + 1);

		if (days > 100) {
			return "to many results";
		}

		smCount = (int) smTaskManager.getSmTaskCount(beginTime, endTime,
				content, mobile, status, senderId, batchNumber);
		if ((smCount % PAGESIZE) == 0) {
			maxPageIndex = (smCount / PAGESIZE);
		} else {
			maxPageIndex = (smCount / PAGESIZE) + 1;
		}

		for (int i = 0; i < maxPageIndex; i++) {
			pages.add(i + 1);
		}

		pageIndex = pageIndex + lastornext;

		if (pageIndex < 1) {
			pageIndex = 1;
		}
		if (pageIndex > maxPageIndex) {
			pageIndex = maxPageIndex;
		}
		if (excel != null && excel.equals("true")) {
			return "excel";
		} else {
			List<SmTask> list = smTaskManager.findSmTask(PAGESIZE, pageIndex,
					beginTime, endTime, content, mobile, status, senderId,
					batchNumber);
			setSmTasks(list);
			logger.info("GetCdrAction: succeed");
			return SUCCESS;
		}
	}

	public InputStream getExcelFile() {

		PrintWriter pw = null;
		String recLocalDiskPath = null;
		FileInputStream fis = null;
		File file = null;
		try {

			recLocalDiskPath = Config.props.getProperty("rec_local_disk_path");

			String sessionID = (String) ActionContext.getContext().getSession()
					.get("sessionID");
			Date d = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
			String dir = recLocalDiskPath + sf.format(d) + "/";
			String cmd = "mkdir " + dir;
			logger.info(cmd);
			Runtime.getRuntime().exec(cmd);
			Thread.sleep(1000);

			file = new File(dir + sessionID);
			file.createNewFile();

			pw = new PrintWriter(file, "UnicodeLittle");
			pw.println("id,内容,手机号,短信通道帐号,短信通道号码,状态,收发时间,msgid,短信发送提交时间,短信发送状态报告时间,优先级,发送者id,批次,自定义字段");

			for (int i = 1; i <= maxPageIndex; i++) {
				List<SmTask> list = smTaskManager.findSmTask(PAGESIZE,
						pageIndex, beginTime, endTime, content, mobile, status,
						senderId, batchNumber);

				for (SmTask st : list) {
					String line = "\"" + st.getId() + "\"" + "," + "\""
							+ st.getContent() + "\"" + "," + "\""
							+ st.getMobile() + "\"" + "," + "\""
							+ st.getAccountId() + "\"" + "," + "\""
							+ st.getSrcTermId() + "\"" + "," + "\""
							+ st.getStatus() + "\"" + "," + "\""
							+ st.getTimestamp() + "\"" + "," + "\""
							+ st.getMsgid() + "\"" + "," + "\""
							+ st.getTaskSubmitTime() + "\"" + "," + "\""
							+ st.getTaskReceiveReportTime() + "\"" + "," + "\""
							+ st.getPenalty() + "\"" + "," + "\""
							+ st.getSenderId() + "\"" + "," + "\""
							+ st.getBatchNumber() + "\"" + "," + "\""
							+ st.getUserfield() + "\"";
					logger.debug(line);
					pw.println(line);
				}
				pw.flush();
			}

			fis = new FileInputStream(file);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}finally{
			pw.close();
		}
		return fis;
	}

	public List<SmTask> getSmTasks() {
		return smTasks;
	}

	public void setSmTasks(List<SmTask> smTasks) {
		this.smTasks = smTasks;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public int getSmCount() {
		return smCount;
	}

	public void setSmCount(int smCount) {
		this.smCount = smCount;
	}

	public String getExcel() {
		return excel;
	}

	public void setExcel(String excel) {
		this.excel = excel;
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

	public void setLastornext(int lastornext) {
		this.lastornext = lastornext;
	}

	public int getLastornext() {
		return lastornext;
	}

}
