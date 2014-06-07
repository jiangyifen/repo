package com.jiangyifen.ec.dao;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Voicemail {

	private int id;
	private String origmailbox;
	private String callerid;
	private String origdate;
	private String duration;
	private String filename;

	private String vmUrl;
	private String vmUrlPerfix;

	public Voicemail() {
		Properties props = null;
		FileInputStream fis = null;
		try {
			props = new Properties();
			String path = getClass().getClassLoader().getResource("").getPath()
					+ "config.properties";
			fis = new FileInputStream(path);
			props.load(fis);
			vmUrlPerfix = props.getProperty("vm_url_perfix");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setCallerid(String callerid) {
		this.callerid = callerid;
	}

	public String getCallerid() {
		return callerid;
	}

	public void setOrigdate(String origdate) {
		this.origdate = origdate;
	}

	public String getOrigdate() {
		return origdate;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public void setVmUrl(String vmUrl) {
		this.vmUrl = vmUrl;
	}

	public String getVmUrl() {
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");
		Date d = null;
		String s = null;
		try {
			d = df1.parse(this.origdate);
			s = df2.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		vmUrl = vmUrlPerfix + s + "/" + this.getFilename();
		return vmUrl;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getOrigmailbox() {
		return origmailbox;
	}

	public void setOrigmailbox(String origmailbox) {
		this.origmailbox = origmailbox;
	}

}
