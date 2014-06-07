package com.jiangyifen.ec.actions;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.DialTask;
import com.jiangyifen.ec.dao.DialTaskItem;
import com.jiangyifen.ec.dao.SmTask;
import com.jiangyifen.ec.util.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DialTaskSmSendAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 164164641854127706L;
	
	private static Logger logger = LoggerFactory.getLogger(DialTaskSmSendAction.class);

	private int PAGESIZE = 5000;
	private static String accountId = Config.props.getProperty("sm_accountid");

	private String smcontent;
	private Long dialTaskId;
	private String dialTaskName;

	private String departmentname;
	private String date;
	private String hour;
	private String minute;
	private String second;

	private String send;
	private List<Department> dpmts;

	private String phonenumbers;

	public String execute() throws Exception {
		if (send != null && send.equals("false")) {
			dpmts = departmentManager.getDepartments();
			return "page";
		}

		addSmTasks(smcontent, dialTaskId, DialTaskItem.STATUS_MANUAL);
		addSmTasks(smcontent, dialTaskId, DialTaskItem.STATUS_READY);

		if (phonenumbers != null) {
			String regEx = "[^0-9;]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(phonenumbers);
			phonenumbers = m.replaceAll("").trim();

			String[] phoneNumberArray = phonenumbers.split(";");

			addSmTasks(smcontent, dialTaskId, phoneNumberArray);
		}

		return SUCCESS;

	}

	private void addSmTasks(String smcontent, Long dialTaskId,
			String dialTaskItemStatus) {
		Long count;
		Long pages;
		try {
			count = dialTaskItemManager.getDialTaskItemCount(dialTaskId,
					dialTaskItemStatus);
			pages = (count / PAGESIZE) + 1;
			for (int i = 1; i <= pages; i++) {
				List<DialTaskItem> dialTaskItems = dialTaskItemManager
						.findByTaskIdAndStatus(PAGESIZE, i, dialTaskId,
								dialTaskItemStatus);
				for (DialTaskItem dti : dialTaskItems) {
					DialTask dt = dialTaskManager.get(dialTaskId);
					String taskName = "";
					if (dt != null) {
						taskName = dt.getTaskName();
					}

					// 只允数字
					String mobile = dti.getPhoneNumber();
					if (mobile == null || mobile.length() < 11
							|| mobile.length() > 12) {
						continue;
					}
					String regEx = "[^0-9]";
					Pattern p = Pattern.compile(regEx);
					Matcher m = p.matcher(mobile);
					mobile = m.replaceAll("").trim();
					if (mobile.startsWith("0")) {
						mobile = mobile.substring(1, mobile.length());
					}

					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String dateString = date + " " + hour + ":" + minute + ":"
							+ second;
					SmTask smTask = new SmTask();
					smTask.setBatchNumber(dti.getTaskid() + "_" + taskName
							+ "_" + dateString);
					smTask.setAccountId(accountId);
					smTask.setSrcTermId(accountId);

					smTask.setMobile(mobile);
					smTask.setSenderId("DialTaskSmSendAction");
					smTask.setUserfield(departmentname);
					smTask.setStatus(SmTask.READY);
					smTask.setTimestamp(format.parse(dateString));
					smTask.setPenalty(5);

					String tempContent = smcontent;
					tempContent = tempContent.replaceAll("\\(","（");
					tempContent = tempContent.replaceAll("\\)","）");
					tempContent = tempContent.replaceAll("\\[","【");
					tempContent = tempContent.replaceAll("\\]","】");
					tempContent = tempContent.replaceAll("\\{","『");
					tempContent = tempContent.replaceAll("\\}","』");

					if (tempContent.length() <= 70) {
						
						smTask.setContent(tempContent);
						smTaskManager.addSmTask(smTask);
						
					} else {
						int part = 1;
						while (tempContent.length() > 65) {
							String content = tempContent.substring(0, 65);
							
							smTask.setContent("(" + part + ")" + content);
							System.out.println("(" + part + ")" + content);
							smTaskManager.addSmTask(smTask);
							
							tempContent = tempContent.substring(content.length(), tempContent.length());
							System.out.println(tempContent);
							part++;
						}

						smTask.setContent("(" + part + ")" + tempContent);
						smTaskManager.addSmTask(smTask);

					}
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	private void addSmTasks(String smcontent, Long dialTaskId,
			String[] phoneNumberArray) {
		DialTask dt = dialTaskManager.get(dialTaskId);

		try {

			for (String phoneNumber : phoneNumberArray) {

				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String dateString = date + " " + hour + ":" + minute + ":"
						+ second;

				SmTask smTask = new SmTask();
				smTask.setBatchNumber(dialTaskId + "_" + dt.getTaskName() + "_"
						+ dateString);
				smTask.setAccountId(accountId);
				smTask.setSrcTermId(accountId);

				smTask.setMobile(phoneNumber);
				smTask.setSenderId("DialTaskSmSendAction");
				smTask.setUserfield(departmentname);
				smTask.setStatus(SmTask.READY);
				smTask.setTimestamp(format.parse(dateString));
				smTask.setPenalty(3);

				String tempContent = smcontent;
				tempContent = tempContent.replaceAll("\\(","（");
				tempContent = tempContent.replaceAll("\\)","）");
				tempContent = tempContent.replaceAll("\\[","【");
				tempContent = tempContent.replaceAll("\\]","】");
				tempContent = tempContent.replaceAll("\\{","『");
				tempContent = tempContent.replaceAll("\\}","』");
				logger.info("+++++++++ "+tempContent);
				if (tempContent.length() <= 70) {
					
					smTask.setContent(tempContent);
					smTaskManager.addSmTask(smTask);
					logger.info(phoneNumber, tempContent);
					
				} else {
					int part = 1;
					while (tempContent.length() > 65) {
						String content = tempContent.substring(0, 65);
						
						smTask.setContent("(" + part + ")" + content);
						System.out.println("(" + part + ")" + content);
						smTaskManager.addSmTask(smTask);
						
						tempContent = tempContent.substring(content.length(), tempContent.length());
						System.out.println(tempContent);
						part++;
					}

					smTask.setContent("(" + part + ")" + tempContent);
					smTaskManager.addSmTask(smTask);
					logger.info("(" + part + ")" + tempContent);
				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	public void setSmcontent(String smcontent) {
		this.smcontent = smcontent;
	}

	public String getSmcontent() {
		return smcontent;
	}

	public void setDialTaskId(Long dialTaskId) {
		this.dialTaskId = dialTaskId;
	}

	public Long getDialTaskId() {
		return dialTaskId;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setSend(String send) {
		this.send = send;
	}

	public String getSend() {
		return send;
	}

	public void setDpmts(List<Department> dpmts) {
		this.dpmts = dpmts;
	}

	public List<Department> getDpmts() {
		return dpmts;
	}

	public void setDialTaskName(String dialTaskName) {
		this.dialTaskName = dialTaskName;
	}

	public String getDialTaskName() {
		return dialTaskName;
	}

	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}

	public String getDepartmentname() {
		return departmentname;
	}

	public void setPhonenumbers(String phonenumbers) {
		this.phonenumbers = phonenumbers;
	}

	public String getPhonenumbers() {
		return phonenumbers;
	}

}