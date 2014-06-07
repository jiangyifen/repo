package com.jiangyifen.ec.backgroundthreads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.beans.SipStatus;
import com.jiangyifen.ec.fastagi.UserLogout;
import com.jiangyifen.ec.util.ShareData;

public class QueueMemberUpdater extends Thread {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public void run() {


		while (true) {


			try {

				for(SipStatus sipStatus:ShareData.sipStatusList){
					if(!sipStatus.isLogin()){
						UserLogout.dynamicQueueMemberDelete(sipStatus.getSipName());
						logger.info("QueueMemberUpdater: " + sipStatus.getSipName());
					}
				}

				Thread.sleep(5000);
			} catch (Exception e) {

			} finally {


			}
		}

	}

}
