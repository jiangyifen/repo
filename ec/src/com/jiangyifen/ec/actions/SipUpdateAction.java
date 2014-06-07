package com.jiangyifen.ec.actions;

import com.jiangyifen.ec.dao.Sip;

public class SipUpdateAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4903319711517002883L;
	private Long id;
	private String name;
	private String host = "dynamic";
	private String nat = "no";
	private String type = "friend";
	private String accountcode;
	private String amaflags;
	private Integer calllimit = 9999;
	private String callgroup;
	private String callerid;
	private String cancallforward = "yes";
	private String canreinvite = "no";
	private String context;
	private String defaultip;
	private String dtmfmode;
	private String fromuser;
	private String fromdomain;
	private String language;
	private String mailbox;
	private String md5secret;
	private String permit;
	private String deny;
	private String mask;
	private String musiconhold;
	private String pickupgroup;
	private String qualify = "yes";
	private String regexten = "";
	private String restrictcid;
	private String rtptimeout;
	private String rtpholdtimeout;
	private String secret;
	private String setvar;
	private String disallow = "all";
	private String allow = "all";
	private String fullcontact = "";
	private String ipaddr = "";
	private String port = "";
	private String regserver;
	private Long regseconds = 0L;
	private Integer lastms = -1;
	private String username = "";
	private String defaultuser = "";
	private String subscribecontext;
	private String dpmt;

	public String execute() throws Exception {
		Sip sip = new Sip();
		sip.setId(id);
		sip.setName(name);
		sip.setHost(host);
		sip.setNat(nat);
		sip.setType(type);
		sip.setAccountcode(accountcode);
		sip.setAmaflags(amaflags);
		sip.setCalllimit(calllimit);
		sip.setCallgroup(callgroup);
		sip.setCallerid(callerid);
		sip.setCancallforward(cancallforward);
		sip.setCanreinvite(canreinvite);
		sip.setContext(context);
		sip.setDefaultip(defaultip);
		sip.setDtmfmode(dtmfmode);
		sip.setFromuser(fromuser);
		sip.setFromdomain(fromdomain);
		sip.setLanguage(language);
		sip.setMailbox(mailbox);
		sip.setMd5secret(md5secret);
		sip.setPermit(permit);
		sip.setDeny(deny);
		sip.setMask(mask);
		sip.setMusiconhold(musiconhold);
		sip.setPickupgroup(pickupgroup);
		sip.setQualify(qualify);
		sip.setRegexten(regexten);
		sip.setRestrictcid(restrictcid);
		sip.setRtptimeout(rtptimeout);
		sip.setRtpholdtimeout(rtpholdtimeout);
		sip.setSecret(secret);
		sip.setSetvar(setvar);
		sip.setDisallow(disallow);
		sip.setAllow(allow);
		sip.setFullcontact(fullcontact);
		sip.setIpaddr(ipaddr);
		sip.setPort(port);
		sip.setRegserver(regserver);
		sip.setRegseconds(regseconds);
		sip.setLastms(lastms);
		sip.setUsername(username);
		sip.setDefaultip(defaultip);
		sip.setSubscribecontext(defaultuser);
		sip.setDpmt(dpmt);

		sipManager.updateSip(sip);
		return SUCCESS;

	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}

	public void setNat(String nat) {
		this.nat = nat;
	}

	public String getNat() {
		return nat;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setAccountcode(String accountcode) {
		this.accountcode = accountcode;
	}

	public String getAccountcode() {
		return accountcode;
	}

	public void setAmaflags(String amaflags) {
		this.amaflags = amaflags;
	}

	public String getAmaflags() {
		return amaflags;
	}

	public void setCalllimit(Integer calllimit) {
		this.calllimit = calllimit;
	}

	public Integer getCalllimit() {
		return calllimit;
	}

	public void setCallgroup(String callgroup) {
		this.callgroup = callgroup;
	}

	public String getCallgroup() {
		return callgroup;
	}

	public void setCallerid(String callerid) {
		this.callerid = callerid;
	}

	public String getCallerid() {
		return callerid;
	}

	public void setCancallforward(String cancallforward) {
		this.cancallforward = cancallforward;
	}

	public String getCancallforward() {
		return cancallforward;
	}

	public void setCanreinvite(String canreinvite) {
		this.canreinvite = canreinvite;
	}

	public String getCanreinvite() {
		return canreinvite;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getContext() {
		return context;
	}

	public void setDefaultip(String defaultip) {
		this.defaultip = defaultip;
	}

	public String getDefaultip() {
		return defaultip;
	}

	public void setDtmfmode(String dtmfmode) {
		this.dtmfmode = dtmfmode;
	}

	public String getDtmfmode() {
		return dtmfmode;
	}

	public void setFromuser(String fromuser) {
		this.fromuser = fromuser;
	}

	public String getFromuser() {
		return fromuser;
	}

	public void setFromdomain(String fromdomain) {
		this.fromdomain = fromdomain;
	}

	public String getFromdomain() {
		return fromdomain;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMd5secret(String md5secret) {
		this.md5secret = md5secret;
	}

	public String getMd5secret() {
		return md5secret;
	}

	public void setPermit(String permit) {
		this.permit = permit;
	}

	public String getPermit() {
		return permit;
	}

	public void setDeny(String deny) {
		this.deny = deny;
	}

	public String getDeny() {
		return deny;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getMask() {
		return mask;
	}

	public void setMusiconhold(String musiconhold) {
		this.musiconhold = musiconhold;
	}

	public String getMusiconhold() {
		return musiconhold;
	}

	public void setPickupgroup(String pickupgroup) {
		this.pickupgroup = pickupgroup;
	}

	public String getPickupgroup() {
		return pickupgroup;
	}

	public void setQualify(String qualify) {
		this.qualify = qualify;
	}

	public String getQualify() {
		return qualify;
	}

	public void setRegexten(String regexten) {
		this.regexten = regexten;
	}

	public String getRegexten() {
		return regexten;
	}

	public void setRestrictcid(String restrictcid) {
		this.restrictcid = restrictcid;
	}

	public String getRestrictcid() {
		return restrictcid;
	}

	public void setRtptimeout(String rtptimeout) {
		this.rtptimeout = rtptimeout;
	}

	public String getRtptimeout() {
		return rtptimeout;
	}

	public void setRtpholdtimeout(String rtpholdtimeout) {
		this.rtpholdtimeout = rtpholdtimeout;
	}

	public String getRtpholdtimeout() {
		return rtpholdtimeout;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getSecret() {
		return secret;
	}

	public void setSetvar(String setvar) {
		this.setvar = setvar;
	}

	public String getSetvar() {
		return setvar;
	}

	public void setDisallow(String disallow) {
		this.disallow = disallow;
	}

	public String getDisallow() {
		return disallow;
	}

	public void setAllow(String allow) {
		this.allow = allow;
	}

	public String getAllow() {
		return allow;
	}

	public void setFullcontact(String fullcontact) {
		this.fullcontact = fullcontact;
	}

	public String getFullcontact() {
		return fullcontact;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}

	public String getIpaddr() {
		return ipaddr;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPort() {
		return port;
	}

	public void setRegserver(String regserver) {
		this.regserver = regserver;
	}

	public String getRegserver() {
		return regserver;
	}

	public void setRegseconds(Long regseconds) {
		this.regseconds = regseconds;
	}

	public Long getRegseconds() {
		return regseconds;
	}

	public void setLastms(int lastms) {
		this.lastms = lastms;
	}

	public int getLastms() {
		return lastms;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setDefaultuser(String defaultuser) {
		this.defaultuser = defaultuser;
	}

	public String getDefaultuser() {
		return defaultuser;
	}

	public void setSubscribecontext(String subscribecontext) {
		this.subscribecontext = subscribecontext;
	}

	public String getSubscribecontext() {
		return subscribecontext;
	}

	public void setDpmt(String dpmt) {
		this.dpmt = dpmt;
	}

	public String getDpmt() {
		return dpmt;
	}
}
