package com.jiangyifen.ec.customer.olay;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OlayWSValidateAccountTest extends BaseAgiScript {

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

			String userId = request.getParameter("userId");
			String quanmingduihuan = request.getParameter("quanmingduihuan");

			Object[] objects = client.invoke("validateAccount", userId);

			String resultString = objects[0].toString();

			logger.info("validateAccount(request.getCallerIdNumber()="
					+ request.getCallerIdNumber() + ")");
			logger.info("validateAccount(request.getChannel()="
					+ request.getChannel() + ")");
			logger.info("validateAccount(request.getUniqueId()="
					+ request.getUniqueId() + ")");
			logger.info("validateAccount(channel.getUniqueId()="
					+ channel.getUniqueId() + ")");
			logger.info("validateAccount(userId=" + userId + ")");
			logger.info("validateAccount return: " + resultString);

			String[] resultFields = resultString.split("\\|");

			String exitCode = resultFields[0];
			String exitMsg = resultFields[1];
			String counterCode = resultFields[2];
			String availablePoints = resultFields[3];
			String accountCategory = resultFields[4];

			String level = "0";
			String lowpoint = "false";

			int points = Integer.valueOf(availablePoints).intValue();

			if (quanmingduihuan != null && quanmingduihuan.equals("true")) {
				if (points < 1500) {
					level = "0";
					lowpoint = "true";
				} else if (1500 <= points && points < 3000) {
					level = "1500";
				} else if (3000 <= points && points < 4500) {
					level = "3000";
				} else if (4500 <= points && points < 9000) {
					level = "4500";
				} else if (9000 <= points && points < 15000) {
					level = "9000";
				} else if (15000 <= points) {
					level = "15000";
				}
			}else{
				if (points < 4500) {
					level = "0";
					lowpoint = "true";
				} else if (4500 <= points && points < 9000) {
					level = "4500";
				} else if (9000 <= points && points < 15000) {
					level = "9000";
				} else if (15000 <= points) {
					level = "15000";
				}
			}
			// 普通会员
			if (accountCategory.equals("1")) {
				if (level.equals("9000") || level.equals("15000")) {
					level = "4500";
				}
			}
			// VIP会员（白金会员）
			else if (accountCategory.equals("3")) {
				if (level.equals("15000")) {
					level = "9000";
				}
			}
			setVariable("exitCode", exitCode);
			setVariable("exitMsg", exitMsg);
			setVariable("counterCode", counterCode);
			setVariable("availablePoints", availablePoints);
			setVariable("level", level);
			setVariable("lowpoint", lowpoint);
			setVariable("accountCategory", accountCategory);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

}
