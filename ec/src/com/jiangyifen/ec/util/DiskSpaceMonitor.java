package com.jiangyifen.ec.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class DiskSpaceMonitor extends Thread {
	private static final int HI_LINE = 90;
	private static final int LO_LINE = 70;
	private static final int period = 3600000;

	private final Log logger = LogFactory.getLog(getClass());

	private String recLocalDiskPath;

	public DiskSpaceMonitor() {
		Properties props = null;
		FileInputStream fis = null;

		try {

			props = new Properties();
			String path = getClass().getClassLoader().getResource("").getPath()
					+ "config.properties";
			logger.info(path);
			fis = new FileInputStream(path);
			props.load(fis);
			recLocalDiskPath = props.getProperty("rec_local_disk_path");

		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		this.setDaemon(true);
		this.setName("DiskSpaceMonitorThread");
		this.start();
	}

	private int getFreeDiskSpace() {
		int freeSpace = 0;
		try {
			Process p = Runtime.getRuntime().exec("df");
			BufferedReader br = new BufferedReader(new InputStreamReader(p
					.getInputStream()));
			br.readLine();
			br.readLine();
			String line = br.readLine();
			while (line.indexOf("  ") != -1)
				line = line.replace("  ", " ");
			String s = line.split(" ")[4];
			freeSpace = Integer.valueOf(s.substring(0, s.length() - 1))
					.intValue();

		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}

		return freeSpace;
	}

	private File[] getFiles(String dir) {
		File d = new File(dir);
		return d.listFiles();
	}

	public void run() {
		try {
			while (true) {

				Date current = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("HH");
				String h = sdf.format(current);
				Integer hour = Integer.valueOf(h);

				if (hour >= 3 && hour <= 6) {

					int usedSpace = getFreeDiskSpace();
					if (usedSpace >= HI_LINE) {
						logger.warn("Disk space not enough: " + usedSpace
								+ "% used.");
						File[] recDirs = getFiles(recLocalDiskPath);
						Arrays.sort(recDirs);
						String cmd;
						String cmd2;
						String cmd3;
						for (int i = 0; i < recDirs.length; i++) {
							cmd = "rm " + recDirs[i].toString() + " -rf";
							cmd2 = "rm /var/spool/asteriek/monitor -rf";
							cmd3 = "mkdir /var/spool/asterisk/monitor";
							logger.info(cmd);
							try {
								Runtime.getRuntime().exec(cmd);
								Runtime.getRuntime().exec(cmd2);
								Runtime.getRuntime().exec(cmd3);
								Thread.sleep(300000);
							} catch (IOException e) {
								logger.error(e.getMessage(),e);
							}
							usedSpace = getFreeDiskSpace();
							logger.info("Disk used " + usedSpace + "%");
							if (usedSpace <= LO_LINE) {
								logger.info("Disk used " + usedSpace + "%, < "
										+ LO_LINE
										+ "%.Stop deleteing record files");
								break;
							}
						}
					}

				}

				Thread.sleep(period);

			}
		} catch (InterruptedException e) {
			logger.error(e.getMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
}
