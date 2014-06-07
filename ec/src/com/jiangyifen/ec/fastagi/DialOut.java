package com.jiangyifen.ec.fastagi;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DialOut extends BaseAgiScript {

	private final Logger logger = LoggerFactory.getLogger(getClass());

//	public static ConcurrentHashMap<String, String> outline = new ConcurrentHashMap<String, String>();
	
	
	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		
		String dst = request.getParameter("dst");
		
		String dialStatus = "jiangyifen";
		
		dialStatus = this.getVariable("DIALSTATUS");		
		logger.info("before: "+ dialStatus);
		
		this.exec("Dial", "SIP/"+dst+"@33190910");
		
		dialStatus = this.getVariable("DIALSTATUS");		
		logger.info("after: " + dialStatus);

	}

}
