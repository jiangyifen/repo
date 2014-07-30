package com.jiangyifen.ec.manager;

import java.util.Date;

import org.asteriskjava.manager.AbstractManagerEventListener;
import org.asteriskjava.manager.event.HoldEvent;
import org.asteriskjava.manager.event.UnholdEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.dao.HoldEventLog;

public class HoldListener extends AbstractManagerEventListener {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected void handleEvent(HoldEvent event) {
		logger.info("HOLD: " + event.toString());

		String channel = event.getChannel();

		HoldEventLog log = new HoldEventLog();
		log.setChannel(channel);
		log.setExten(channel.substring(channel.indexOf("/") + 1,
				channel.indexOf("-")));
		log.setHoldDate(new Date());
		log.setUniqueid(event.getUniqueId());

		MyManager.holdEventLogManager.save(log);
		logger.info("HOLD: saved!");

	}

	protected void handleEvent(UnholdEvent event) {
		logger.info("UNHOLD: " + event.toString());

		HoldEventLog log = MyManager.holdEventLogManager
				.findLastLogByUniqueid(event.getUniqueId());

		// 如有记录且没有unhold信息，便把此次unhold信息写入。
		if (log != null && log.getUnholdDate() == null) {
			log.setUnholdDate(new Date());
			MyManager.holdEventLogManager.update(log);
			logger.info("UNHOLD: updated!");
		}
		// 若无记录，或已有记录且有登出信息，则新增一条记录，仅有登出信息没有登录信息
		else {
			String channel = event.getChannel();

			log = new HoldEventLog();
			log.setChannel(channel);
			log.setExten(channel.substring(channel.indexOf("/") + 1,
					channel.indexOf("-")));
			log.setUnholdDate(new Date());
			log.setUniqueid(event.getUniqueId());

			MyManager.holdEventLogManager.save(log);
			logger.info("UNHOLD: new unhold!");
		}
	}

}
