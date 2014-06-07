package com.jiangyifen.ec.customer.olay;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OlayWSEnterVipChannel extends BaseAgiScript {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public static JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory
			.newInstance();
	public static Client client = null;

	private Client initClient() {
		if (client == null) {
			client = dcf.createClient(OlayGlobalData.wdsl);
		}
		return client;
	}

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		try {

			initClient();

			String userId = request.getParameter("userId");

			Object[] objects = client.invoke("enterVipChannel", userId);

			String resultString = objects[0].toString();

			logger.info("enterVipChannel(" + userId + ")");
			logger.info("enterVipChannel return: " + resultString);

			String[] resultFields = resultString.split("\\|");

			setVariable("result", resultFields[0]);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

}