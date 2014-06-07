package com.jiangyifen.ec.customer.olay;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OlayGiftQtyValidate extends BaseAgiScript {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		try {

			String qty = channel.getVariable("qty");
			String maxqty = channel.getVariable("maxqty");

			int i_qty = Integer.valueOf(qty);
			int i_maxqty = Integer.valueOf(maxqty);

			setVariable("isValid", "false");
			if (i_qty > 0 && i_qty <= i_maxqty) {
				setVariable("isValid", "true");
				
				String giftPoints = channel.getVariable("giftPoints");
				int i_giftPoints = Integer.valueOf(giftPoints);
				String availablePoints = channel.getVariable("availablePoints");
				int i_availablePoints = Integer.valueOf(availablePoints);
				
				int pointleft = i_availablePoints - (i_giftPoints*i_qty);
				setVariable("pointleft", pointleft+"");
			}

			logger.info("maxQty=" + maxqty + ", userInput qty=" + qty
					+ ", isValid=" + channel.getVariable("isValid"));

			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
