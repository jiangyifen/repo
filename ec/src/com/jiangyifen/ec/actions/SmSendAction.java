package com.jiangyifen.ec.actions;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.dao.SmTask;
import com.jiangyifen.ec.util.Config;

public class SmSendAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 164164641854127706L;

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private static String accountId = Config.props.getProperty("sm_accountid");

	private String smcontent;
	private String phoneNumbers;

	private String date;
	private String hour;

	private String minute;
	private String second;

	public String execute() throws Exception {
		logger.info("SmSend: send " + phoneNumbers + ", content = "+smcontent);
		// 只允数字
		if (phoneNumbers != null) {
			String regEx = "[^0-9;]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(phoneNumbers);
			phoneNumbers = m.replaceAll("").trim();

			String[] phoneNumberArray = phoneNumbers.split(";");

			addSmTasks(smcontent, phoneNumberArray);
		}
		return SUCCESS;

	}

	private void addSmTasks(String smcontent, String[] phoneNumbers) {

		try {

			for (String phoneNumber : phoneNumbers) {

				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String dateString = date + " " + hour + ":" + minute + ":"
						+ second;

				SmTask smTask = new SmTask();
				smTask.setBatchNumber("");
				smTask.setAccountId(accountId);
				smTask.setSrcTermId(accountId);
				smTask.setMobile(phoneNumber);
				smTask.setSenderId("SmSendAction");
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

	public String getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(String phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

}