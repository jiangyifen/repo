package com.jiangyifen.ec.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.biz.VoicemailManager;
import com.jiangyifen.ec.dao.Voicemail;

public class VoicemailTransfer extends Thread {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static String vmSrcDir;
	private static String vmDstDir;
	protected VoicemailManager vmManager;

	public VoicemailTransfer() {
		Properties props = null;
		FileInputStream fis = null;

		try {

			props = new Properties();
			String path = getClass().getClassLoader().getResource("").getPath()
					+ "config.properties";
			logger.info(path);
			fis = new FileInputStream(path);
			props.load(fis);
			vmSrcDir = props.getProperty("vm_src_dir");
			vmDstDir = props.getProperty("vm_dst_dir");

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		this.setDaemon(true);
		this.setName("VoicemailTransferThread");
		this.start();
	}

	public void setVmManager(VoicemailManager vmManager) {
		this.vmManager = vmManager;
	}

	private File[] getFiles(String dir, String ext) {
		File d = new File(dir);
		return d.listFiles(new OnlyExt(ext));
	}

	private Voicemail getVMBeanFromFile(File f) {
		Voicemail vm = new Voicemail();

		String callerid = null;
		String origdate = null;
		String duration = null;
		String origmailbox = null;
		String filename = null;

		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("callerid")) {
					if (line.indexOf("<") != -1 && line.indexOf(">") != -1) {
						callerid = line.substring(line.indexOf("<") + 1,
								line.indexOf(">"));
					} else {
						callerid = line.substring(9).replaceAll(" ", "")
								.replaceAll("\"", "");
					}
					if (callerid.length() == 0) {
						callerid = "unknowen";
					}

				} else if (line.startsWith("origdate")) {
					origdate = line.substring(9);
				} else if (line.startsWith("duration")) {
					duration = line.substring(9);
				} else if (line.startsWith("origmailbox")) {
					origmailbox = line.substring(13);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {

			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}

		if (origdate != null && callerid != null) {
			SimpleDateFormat sf1 = new SimpleDateFormat(
					"EEE MMM dd hh:mm:ss aa zzz yyyy", Locale.US);
			SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.US);
			Date d = null;
			try {
				d = sf1.parse(origdate);
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
			origdate = sf2.format(d);
			vm.setOrigdate(origdate);

			vm.setCallerid(callerid);

			vm.setDuration(duration);

			vm.setOrigmailbox(origmailbox);

			filename = origdate.replace("-", "").replace(" ", "-")
					.replace(":", "")
					+ "-" + callerid + ".wav";
			vm.setFilename(filename);

			return vm;
		}
		return null;
	}

	private void transferVmFiles(String mailboxDir) {
		File[] msgTxtFiles = null;
		if (mailboxDir != null) {
			msgTxtFiles = getFiles(mailboxDir, "txt");
		}
		if (msgTxtFiles != null) {
			for (int i = 0; i < msgTxtFiles.length; i++) {
				String srcTxtFilePath = msgTxtFiles[i].toString();
				String srcVMFilename = msgTxtFiles[i].toString()
						.substring(srcTxtFilePath.indexOf("msg"))
						.replace(".txt", ".wav");

						String srcVMFilePath = mailboxDir +"/"+ srcVMFilename;

				Voicemail vm = getVMBeanFromFile(msgTxtFiles[i]);
				if (vm != null) {
					logger.info("Find a new voicemail!");
					String dateDir = vm.getFilename().substring(0, 8) + "/";
					String dstVMFilePath = vmDstDir + dateDir
							+ vm.getFilename();

					// 把vm存入数据库

					vmManager.update(vm);
					logger.info("The new voicemail has been saved to the database!");
					
					// voicemail wav文件从src目录移动到dst目录
					try {
						String cmd;
						cmd = "mkdir -p " + vmDstDir + dateDir;
						logger.info(cmd);
						Runtime.getRuntime().exec(cmd);

						cmd = "mv " + srcVMFilePath + " " + dstVMFilePath;
						logger.info(cmd);
						Runtime.getRuntime().exec(cmd);

					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
				}

				// 删除msgXXX.txt
				try {

					String cmd;
					cmd = "rm " + srcTxtFilePath + " -rf";
					logger.info(cmd);
					Runtime.getRuntime().exec(cmd);

					cmd = "rm " + srcVMFilePath + " -rf";
					logger.info(cmd);
					Runtime.getRuntime().exec(cmd);

				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}

			}
		}
	}

	public void run() {
		while (true) {
logger.info("vmt: loop=loop");
			
			File d;
			File[] files;
			try {

				Thread.sleep(10000);

				d = new File(vmSrcDir);
				files = d.listFiles();
				if (files == null) {
					logger.warn("vmt: " + vmSrcDir + " files==null");
					continue;
				}else{
				}
				for (File file : files) {
					String mailboxDir = vmSrcDir + file.getName()
							+ "/INBOX";
logger.info("vmt: mailboxDir="+mailboxDir);
					transferVmFiles(mailboxDir);
				}

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}finally{
				
			}
			
		}
	}
}
