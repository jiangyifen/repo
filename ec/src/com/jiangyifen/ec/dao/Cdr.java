package com.jiangyifen.ec.dao;

import java.io.FileInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class Cdr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7443319952661324496L;
	private final Log logger = LogFactory.getLog(getClass());
	

	// asterisk realtime字段
	private Date calldate;
	private String clid;
	private String src;
	private String dst;
	private String dcontext;
	private String channel;
	private String dstchannel;
	private String lastapp;
	private String lastdata;
	private long duration;
	private long billsec;
	private String disposition;
	private long amaflags;
	private String accountcode;
	private String uniqueid;
	private String userfield;
	//自定义字段
	private String fenji;
	private String username;
	private String name;
	private String hid;
	private String dpmt;
	private String sync;
	private String url;
	private Boolean intention=false;

	// 非数据库字段
	private String status;
	private String recUrl;
	private String recUrlPerfix;

	public Cdr() {

		Properties props = null;
		FileInputStream fis = null;

		try {
			props = new Properties();
			String path = getClass().getClassLoader().getResource("").getPath()
					+ "config.properties";
			fis = new FileInputStream(path);
			props.load(fis);
			recUrlPerfix = props.getProperty("rec_url_perfix");

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}

	}

	public void setCalldate(Date calldate) {
		this.calldate = calldate;
	}

	public Date getCalldate() {
		return calldate;
	}

	public void setClid(String clid) {
		this.clid = clid;
	}

	public String getClid() {
		return clid;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getSrc() {
		return src;
	}

	public void setDst(String dst) {
		this.dst = dst;
	}

	public String getDst() {
		return dst;
	}

	public void setDcontext(String dcontext) {
		this.dcontext = dcontext;
	}

	public String getDcontext() {
		return dcontext;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChannel() {
		return channel;
	}

	public void setDstchannel(String dstchannel) {
		this.dstchannel = dstchannel;
	}

	public String getDstchannel() {
		return dstchannel;
	}

	public void setLastapp(String lastapp) {
		this.lastapp = lastapp;
	}

	public String getLastapp() {
		return lastapp;
	}

	public void setLastdata(String lastdata) {
		this.lastdata = lastdata;
	}

	public String getLastdata() {
		return lastdata;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getDuration() {
		return duration;
	}

	public void setBillsec(long billsec) {
		this.billsec = billsec;
	}

	public long getBillsec() {
		return billsec;
	}

	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}

	public String getDisposition() {
		return disposition;
	}

	public void setAmaflags(long amaflags) {
		this.amaflags = amaflags;
	}

	public long getAmaflags() {
		return amaflags;
	}

	public void setAccountcode(String accountcode) {
		this.accountcode = accountcode;
	}

	public String getAccountcode() {
		return accountcode;
	}

	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}

	public String getUniqueid() {
		return uniqueid;
	}

	public void setUserfield(String userfield) {
		this.userfield = userfield;
	}

	public String getUserfield() {
		return userfield;
	}
	

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		if (userfield != null && userfield.length() == 0)
			status = "TRANSFER";
		else if (userfield != null) {
			status = userfield.split(",")[0];
		}
		return status;
	}

	public void setRecUrl(String recUrl) {
		this.recUrl = recUrl;
	}

	public String getRecUrl() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String d = df.format(this.calldate);
		recUrl = recUrlPerfix + d + "/" + d + "-" + this.uniqueid + ".wav";
		return recUrl;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setSync(String sync) {
		this.sync = sync;
	}

	public String getSync() {
		return sync;
	}


	public void setDpmt(String dpmt) {
		this.dpmt = dpmt;
	}

	public String getDpmt() {
		return dpmt;
	}

	public void setFenji(String fenji) {
		this.fenji = fenji;
	}

	public String getFenji() {
		return fenji;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}

	public String getHid() {
		return hid;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setIntention(Boolean intention) {
		this.intention = intention;
	}

	public Boolean getIntention() {
		return intention;
	}

}
