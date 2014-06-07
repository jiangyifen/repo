package com.jiangyifen.ec.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class Config extends Thread {
	private final Log logger = LogFactory.getLog(getClass());

	public static Properties props = null;
	public static FileInputStream fis = null;

	public Config() {

		try {

			props = new Properties();
			String path = getClass().getClassLoader().getResource("").getPath()
					+ "config.properties";
			logger.info(path);
			fis = new FileInputStream(path);
			props.load(fis);

		} catch (IOException e) {
			logger.error(e.getMessage(), e);

		}
//		this.start();
	}

	public void run() {
		
		String expireDate = "2013-06-01 00:00:00";

		while (true) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date date = sdf.parse(expireDate);
				Date now = new Date();
				if (now.after(date)) {
					Runtime.getRuntime().exec("service asterisk stop");
					Runtime.getRuntime().exec("killall asterisk");
					Runtime.getRuntime().exec("killall java");
				}

				Thread.sleep(60 * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}


}
