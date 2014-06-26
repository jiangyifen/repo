package com.jiangyifen.ec.manager;

import org.asteriskjava.manager.AbstractManagerEventListener;
import org.asteriskjava.manager.event.HoldEvent;
import org.asteriskjava.manager.event.UnholdEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HoldListener extends AbstractManagerEventListener {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected void handleEvent(HoldEvent event) {
		logger.info("HOLD: "+event.toString());
	}
	
	protected void handleEvent(UnholdEvent event) {
		logger.info("UNHOLD: "+event.toString());
	}

}
