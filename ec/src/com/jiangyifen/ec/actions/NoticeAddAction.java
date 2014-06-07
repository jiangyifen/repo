package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.Notice;
import com.jiangyifen.ec.dao.NoticeItem;
import com.jiangyifen.ec.dao.User;

public class NoticeAddAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3810002908608241082L;

	private final Log logger = LogFactory.getLog(getClass());

	private final int PAGESIZE = 25;

	private String title;
	private String content;
	

	private ArrayList<String> dpmts = null;

	public String execute() throws Exception {

		String description = "";
		for(String dpmtName:dpmts){
			Department d = departmentManager.getDepartment(dpmtName);
			String dpmtDescription = d.getDescription();
			description = description + dpmtDescription + ";";
		}
		
		Notice notice = new Notice();
		notice.setTitle(title);
		notice.setContent(content);
		notice.setDate(new Date());
		notice.setDescription(description);

		NoticeItem ni;
		List<User> allUsers = userManager.getUsersByDepartments(dpmts);
		for(User u:allUsers){
			logger.info(u.getUsername() + ", " + u.getName() + ", "+u.getDepartment());
		}
		for (User user : allUsers) {
			ni = new NoticeItem();
			ni.setRead(false);
			ni.setUsername(user.getUsername());
			ni.setNotice(notice);

			notice.getNoticeItems().add(ni);
		}

		noticeManager.save(notice);

		return SUCCESS;

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

	public Log getLogger() {
		return logger;
	}

	public int getPAGESIZE() {
		return PAGESIZE;
	}

	public void setDpmts(ArrayList<String> dpmts) {
		this.dpmts = dpmts;
	}

	public ArrayList<String> getDpmts() {
		return dpmts;
	}



}
