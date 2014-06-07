package com.jiangyifen.ec.customer.olay;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OlayWSCreateRedemptionTest extends BaseAgiScript {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public static JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory
			.newInstance();
	public static Client client = null;

	private Client initClient() {
		if (client == null) {
			client = dcf.createClient(OlayGlobalData.test_wdsl);
		}
		return client;
	}

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		try {

			initClient();

			String userId = channel.getVariable("userId");
			String counterCode = channel.getVariable("counterCode");
			String giftCode = channel.getVariable("giftCode");
			String qty = channel.getVariable("qty");

			logger.info("OlayWSCreateRedemption(request.getCallerIdNumber()=" + request.getCallerIdNumber() + ")");
			logger.info("OlayWSCreateRedemption(request.getChannel()=" + request.getChannel() + ")");
			logger.info("OlayWSCreateRedemption(request.getUniqueId()=" + request.getUniqueId() + ")");
			logger.info("OlayWSCreateRedemption(channel.getUniqueId()=" + channel.getUniqueId() + ")");
			logger.info("OlayWSCreateRedemption(userId:" + userId+")");
			logger.info("OlayWSCreateRedemption(counterCode:" + counterCode+")");
			logger.info("OlayWSCreateRedemption(giftCode:" + giftCode+")");
			logger.info("OlayWSCreateRedemption(qty:" + qty+")" );

			Object[] objects = client.invoke("createRedemption", userId,
					counterCode, giftCode + "*" + qty);

			String resultString = objects[0].toString();

			logger.info("createRedemption return: " + resultString);

			String[] resultFields = resultString.split("\\|");

			setVariable("exitCode", resultFields[0]);
			setVariable("exitMsg", resultFields[1]);
			setVariable("availablePoints", resultFields[2]);

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}

	}

}
