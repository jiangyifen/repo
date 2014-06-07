package com.jiangyifen.ec.fastagi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.util.Config;

public class GetCustomerManagerFuWang extends BaseAgiScript {

	private final Log logger = LogFactory.getLog(getClass());
	
	private final String crmServerUrl = Config.props
			.getProperty("crm_server_url");
	

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		String customerPhoneNumber = "";
		String dstQueue = "default";
		
		InputStream in = null;
		InputStreamReader inReader = null;
		BufferedReader bufReader = null;
		try {

			String urlString = "";
			customerPhoneNumber = this.getVariable("CALLERID(num)");
			
			urlString = urlString + crmServerUrl + "?" + "type=3&callernum="+customerPhoneNumber;
			logger.info(urlString);

			URL url = new URL(urlString);
			in = url.openStream();
			inReader = new InputStreamReader(in);
			bufReader = new BufferedReader(inReader);
			
			dstQueue = bufReader.readLine();
			
			this.setVariable("dstQueue", dstQueue);
			logger.info("dstQueue=" + dstQueue);

			

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				inReader.close();
				in.close();
				bufReader.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
