package com.jiangyifen.ec.report.internal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ReportGenerator extends Thread {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private List<String> reports;

	public ReportGenerator() {
		this.setName("ReportGenerator");
		this.setDaemon(true);
		this.start();
	}

	public void run() {

		while (true) {
			try {
				while(reports==null){
					sleep(1000);
				}

				for (String s : reports) {
					Report report = (Report) Class.forName(s).newInstance();
					report.execute();
				}

				Thread.sleep(30 * 60 * 1000);
//				Thread.sleep(5 * 60 * 1000);

			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			} catch (ClassNotFoundException e) {
				logger.error(e.getMessage(), e);
			} catch (InstantiationException e) {
				logger.error(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			}
		}

	}

	public List<String> getReports() {
		return reports;
	}

	public void setReports(List<String> reports) {
		this.reports = reports;
	}



}
