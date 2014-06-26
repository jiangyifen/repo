package com.jiangyifen.ec.manager;

import org.asteriskjava.manager.AbstractManagerEventListener;
import org.asteriskjava.manager.event.HoldEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HoldListener extends AbstractManagerEventListener {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected void handleEvent(HoldEvent event) {
		logger.info("holdhold: "+event.toString());
	}

}
