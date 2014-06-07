package com.jiangyifen.ec.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LicenseWatchDog extends Thread {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private SimpleDateFormat sdf = null;
	private Date stopDate = null;

	public LicenseWatchDog() {
		try {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			stopDate = sdf.parse("2012-10-07");
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void run() {

		while (true) {
			try {

				// 5 分钟
				sleep(1000 * 5);

				Date now = new Date();
				if (now.after(stopDate)) {
					
					Runtime.getRuntime().exec("service asterisk stop");

					Runtime.getRuntime().exec("service postgresql stop");
					
				}

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

}
