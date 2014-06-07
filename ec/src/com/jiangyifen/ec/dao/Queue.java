package com.jiangyifen.ec.dao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Queue  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3332867075485019841L;
	
	private String name;
	private String musiconhold;
	private String announce;
	private String context;
	private Long timeout;
	private Boolean monitor_join;
	private String monitor_format;
	private String queue_youarenext;
	private String queue_thereare;
	private String queue_callswaiting;
	private String queue_holdtime;
	private String queue_minutes;
	private String queue_seconds;
	private String queue_lessthan;
	private String queue_thankyou;
	private String queue_reporthold;
	private Long announce_frequency;
	private Long queue_periodic_announce;
	private Long announce_round_seconds;
	private String announce_holdtime;
	private Long retry;
	private Long wrapuptime;
	private Long maxlen;
	private Long servicelevel;
	private String strategy;
	private String joinempty;
	private String leavewhenempty;
	private Boolean eventmemberstatus;
	private Boolean eventwhencalled;
	private Boolean reportholdtime;
	private Long memberdelay;
	private Long weight;
	private Boolean timeoutrestart;
	private Boolean setinterfacevar;
	private String autopause;
	
	//none asterisk fields
	private String description;
	private Boolean dynamicMember=false;
	
	private Set<QueueMember> members = new HashSet<QueueMember>();
	private Set<User> users = new HashSet<User>();

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setMusiconhold(String musiconhold) {
		this.musiconhold = musiconhold;
	}

	public String getMusiconhold() {
		return musiconhold;
	}

	public void setAnnounce(String announce) {
		this.announce = announce;
	}

	public String getAnnounce() {
		return announce;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getContext() {
		return context;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

	public Long getTimeout() {
		return timeout;
	}

	public void setMonitor_join(Boolean monitor_join) {
		this.monitor_join = monitor_join;
	}

	public Boolean isMonitor_join() {
		return monitor_join;
	}

	public void setMonitor_format(String monitor_format) {
		this.monitor_format = monitor_format;
	}

	public String getMonitor_format() {
		return monitor_format;
	}

	public void setQueue_youarenext(String queue_youarenext) {
		this.queue_youarenext = queue_youarenext;
	}

	public String getQueue_youarenext() {
		return queue_youarenext;
	}

	public void setQueue_thereare(String queue_thereare) {
		this.queue_thereare = queue_thereare;
	}

	public String getQueue_thereare() {
		return queue_thereare;
	}

	public void setQueue_callswaiting(String queue_callswaiting) {
		this.queue_callswaiting = queue_callswaiting;
	}

	public String getQueue_callswaiting() {
		return queue_callswaiting;
	}

	public void setQueue_holdtime(String queue_holdtime) {
		this.queue_holdtime = queue_holdtime;
	}

	public String getQueue_holdtime() {
		return queue_holdtime;
	}

	public void setQueue_minutes(String queue_minutes) {
		this.queue_minutes = queue_minutes;
	}

	public String getQueue_minutes() {
		return queue_minutes;
	}

	public void setQueue_seconds(String queue_seconds) {
		this.queue_seconds = queue_seconds;
	}

	public String getQueue_seconds() {
		return queue_seconds;
	}

	public void setQueue_lessthan(String queue_lessthan) {
		this.queue_lessthan = queue_lessthan;
	}

	public String getQueue_lessthan() {
		return queue_lessthan;
	}

	public void setQueue_thankyou(String queue_thankyou) {
		this.queue_thankyou = queue_thankyou;
	}

	public String getQueue_thankyou() {
		return queue_thankyou;
	}

	public void setQueue_reporthold(String queue_reporthold) {
		this.queue_reporthold = queue_reporthold;
	}

	public String getQueue_reporthold() {
		return queue_reporthold;
	}

	public void setAnnounce_frequency(Long announce_frequency) {
		this.announce_frequency = announce_frequency;
	}

	public Long getAnnounce_frequency() {
		return announce_frequency;
	}

	public void setAnnounce_round_seconds(Long announce_round_seconds) {
		this.announce_round_seconds = announce_round_seconds;
	}

	public Long getAnnounce_round_seconds() {
		return announce_round_seconds;
	}

	public void setAnnounce_holdtime(String announce_holdtime) {
		this.announce_holdtime = announce_holdtime;
	}

	public String getAnnounce_holdtime() {
		return announce_holdtime;
	}

	public void setRetry(Long retry) {
		this.retry = retry;
	}

	public Long getRetry() {
		return retry;
	}

	public void setWrapuptime(Long wrapuptime) {
		this.wrapuptime = wrapuptime;
	}

	public Long getWrapuptime() {
		return wrapuptime;
	}

	public void setMaxlen(Long maxlen) {
		this.maxlen = maxlen;
	}

	public Long getMaxlen() {
		return maxlen;
	}

	public void setServicelevel(Long servicelevel) {
		this.servicelevel = servicelevel;
	}

	public Long getServicelevel() {
		return servicelevel;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setJoinempty(String joinempty) {
		this.joinempty = joinempty;
	}

	public String getJoinempty() {
		return joinempty;
	}

	public void setLeavewhenempty(String leavewhenempty) {
		this.leavewhenempty = leavewhenempty;
	}

	public String getLeavewhenempty() {
		return leavewhenempty;
	}

	public void setEventmemberstatus(Boolean eventmemberstatus) {
		this.eventmemberstatus = eventmemberstatus;
	}

	public Boolean isEventmemberstatus() {
		return eventmemberstatus;
	}

	public void setEventwhencalled(Boolean eventwhencalled) {
		this.eventwhencalled = eventwhencalled;
	}

	public Boolean isEventwhencalled() {
		return eventwhencalled;
	}

	public void setReportholdtime(Boolean reportholdtime) {
		this.reportholdtime = reportholdtime;
	}

	public Boolean isReportholdtime() {
		return reportholdtime;
	}

	public void setMemberdelay(Long memberdelay) {
		this.memberdelay = memberdelay;
	}

	public Long getMemberdelay() {
		return memberdelay;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

	public Long getWeight() {
		return weight;
	}

	public void setTimeoutrestart(Boolean timeoutrestart) {
		this.timeoutrestart = timeoutrestart;
	}

	public Boolean isTimeoutrestart() {
		return timeoutrestart;
	}

	public void setSetinterfacevar(Boolean setinterfacevar) {
		this.setinterfacevar = setinterfacevar;
	}

	public Boolean isSetinterfacevar() {
		return setinterfacevar;
	}

	public void setMembers(Set<QueueMember> members) {
		this.members = members;
	}

	public Set<QueueMember> getMembers() {
		return members;
	}

	public void setAutopause(String autopause) {
		this.autopause = autopause;
	}

	public String getAutopause() {
		return autopause;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setQueue_periodic_announce(Long queue_periodic_announce) {
		this.queue_periodic_announce = queue_periodic_announce;
	}

	public Long getQueue_periodic_announce() {
		return queue_periodic_announce;
	}

	public void setDynamicMember(Boolean dynamicMember) {
		this.dynamicMember = dynamicMember;
	}

	public Boolean getDynamicMember() {
		return dynamicMember;
	}

}
