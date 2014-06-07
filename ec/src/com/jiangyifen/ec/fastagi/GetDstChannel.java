package com.jiangyifen.ec.fastagi;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.manager.MyManager;

public class GetDstChannel extends BaseAgiScript {
	private final Log logger = LogFactory.getLog(getClass());

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		String exten = this.getVariable("EXTEN").substring(3);
		String temp = MyManager.getActiveChannel(exten);
		String dstChannel = "";
		if (temp != null) {
			dstChannel = temp.split(";")[1];
		}
		this.setVariable("dstChannel", dstChannel);
		logger.info("dstChannel = " + dstChannel);
	}
}