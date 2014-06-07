package com.jiangyifen.ec.actions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.RoleAction;
import com.jiangyifen.ec.dao.User;
import com.opensymphony.xwork2.ActionContext;

public class LoginAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7342879615801856882L;

	private String password = "";
	private String username = "";

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String execute() throws Exception {
		Map<String, Object> session = ActionContext.getContext().getSession();

		if (userManager.loginValid(username, password)) {
			User u = userManager.getUser(username);
			session.put("username", username);

			Set<Department> dpmts = u.getRole().getDepartments();
			if (dpmts == null) {
				dpmts = new HashSet<Department>();
				logger.warn("This user does not belong to any department.");
			}
			session.put("departments", dpmts);

			Set<RoleAction> ras = u.getRole().getRoleactions();
			Set<String> raNames = new HashSet<String>();
			if (ras != null) {
				session.put("ras", ras);
				for (RoleAction ra : ras) {
					raNames.add(ra.getRoleactionname());
				}
				session.put("raNames", raNames);
			} else {
				logger.warn("This user's role have no roleactions.");
			}

			session.put("login", "true");
			logger.info("USER " + username + " login!");

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			Date d = new Date();
			String today = df.format(d);
			session.put("today", today);
			
			Date d2 = new Date(d.getTime() - 24 * 3600000);
			String yesterday = df.format(d2);
			session.put("yesterday", yesterday);
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)-30);
			String oneMonthAgo = df.format(calendar.getTime());
			session.put("oneMonthAgo", oneMonthAgo);

			HttpServletRequest request = ServletActionContext.getRequest();
			String sessID = request.getSession().getId();
			session.put("sessionID", sessID);

			return SUCCESS;
		} else {
			session.put("login", "false");
			logger.info("USER[" + username + "] login failed!");
			return LOGIN;
		}
	}

	public static void main(String[] agrs) throws Exception {

		// String sample = "Hello World,我是中国人,それは本です。";
		//
		// BASE64Encoder base64encoder = new BASE64Encoder();
		// String encode = base64encoder.encode(sample.getBytes("UTF-8"));
		// System.out.println(encode);
		//
		// byte[] bytes = new BASE64Decoder().decodeBuffer(encode);
		// String decode = new String(bytes, "UTF-8");
		// System.out.println(decode);
	}
}
