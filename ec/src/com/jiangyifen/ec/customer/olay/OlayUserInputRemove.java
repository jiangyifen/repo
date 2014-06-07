package com.jiangyifen.ec.customer.olay;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将用户的输入放置到一个Map中，供其他程序获取。 key为channel，value为用户输入
 * 
 * @author Bruce
 * 
 */
public class OlayUserInputRemove extends BaseAgiScript {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		String channelString = this.getVariable("CHANNEL");
		logger.info("channelString=" + channelString);

		logger.info("before remove: " + OlayShareDate.userChannelAndUserInput.size());
		OlayShareDate.userChannelAndUserInput.remove(channelString);
		logger.info("after remove: " + OlayShareDate.userChannelAndUserInput.size());
	}

}