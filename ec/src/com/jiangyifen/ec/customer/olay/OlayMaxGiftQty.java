package com.jiangyifen.ec.customer.olay;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OlayMaxGiftQty extends BaseAgiScript {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		try {

			String giftPoints = channel.getVariable("giftPoints");

			String availablePoints = channel.getVariable("availablePoints");

			int maxqty = Integer.valueOf(availablePoints)
					/ Integer.valueOf(giftPoints);

			setVariable("maxqty", maxqty + "");
			logger.info("max gift qty=" + maxqty);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
