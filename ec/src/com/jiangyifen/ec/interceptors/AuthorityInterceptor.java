package com.jiangyifen.ec.interceptors;

import java.util.HashSet;
import java.util.Map;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class AuthorityInterceptor extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7119784734101444556L;

	@SuppressWarnings("unchecked")
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		invocation.addPreResultListener(new MyResultListener1());
		
		Map<String, Object> session = invocation.getInvocationContext()
				.getSession();
		String s = (String) session.get("login");
		if (s != null && s.equals("true")) {
			
			String actionName = invocation.getProxy().getActionName();
			HashSet<String> raNames = (HashSet<String>) session.get("raNames");
			if (raNames.contains(actionName)) {
				return invocation.invoke();
			} else {
				Thread.sleep(1000);
				return "noauthority";
			}
			
		} else {
			Thread.sleep(1000);
			return ActionSupport.LOGIN;
		}

	}

}
