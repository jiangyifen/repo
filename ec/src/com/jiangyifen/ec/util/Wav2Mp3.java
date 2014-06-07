package com.jiangyifen.ec.util;

import java.io.IOException;
import java.util.ArrayList;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class Wav2Mp3 extends Thread {
	private final Log logger = LogFactory.getLog(getClass());
	private static ArrayList<String> tasks = new ArrayList<String>();

	public Wav2Mp3() {
		this.setName("Wav2Mp3Thread");
		this.setDaemon(true);
		this.start();
	}

	public synchronized static void addTask(String cmd) {
		tasks.add(cmd);
	}

	public synchronized static void removeTask(int index) {
		tasks.remove(index);
	}

	public synchronized static String getTask(int index) {
		return tasks.get(index);
	}

	public void run() {
		try {
			while (true) {
				if (tasks != null) {
					int size = tasks.size();
					if (size > 0) {

						for (int i = 0; i < size; i++) {
							String cmd = getTask(i);
							logger.info(cmd);
							try {
								Process p = Runtime.getRuntime().exec(cmd);
								p.waitFor();
							} catch (IOException e) {
								logger.error(e.getMessage(), e);
							} catch (InterruptedException e) {
								logger.error(e.getMessage(), e);
							}
						}

						for (int i = 0; i < size; i++) {
							logger.info("Task finished! " + getTask(0));
							removeTask(0);
						}

					} else {

						Thread.sleep(1000);

					}
				}
			}
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
