package com.jiangyifen.ec.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wondertek.ctmp.protocol.smgp.SMGPActiveTestMessage;
import com.wondertek.ctmp.protocol.smgp.SMGPApi;

public class SMGPActiveTestThread implements Runnable {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private SMGPApi api;

	public SMGPActiveTestThread(SMGPApi api) {
		this.api = api;
	}

	public void run() {
		while (true) {
			SMGPActiveTestMessage active = new SMGPActiveTestMessage();
			try {
				Thread.sleep(5000);
				api.sendMsg(active);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				api.disconnect();
				break;
			}

		}

	}

}
