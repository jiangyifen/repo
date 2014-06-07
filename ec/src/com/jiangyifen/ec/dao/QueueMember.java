package com.jiangyifen.ec.dao;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class QueueMember implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8445154718461305108L;
	private String queueName;
	private String iface;// interface
	private Long penalty;

	public void setQueueName(String queue_name) {
		this.queueName = queue_name;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setIface(String iface) {
		this.iface = iface;
	}

	public String getIface() {
		return iface;
	}

	public void setPenalty(Long penalty) {
		this.penalty = penalty;
	}

	public Long getPenalty() {
		return penalty;
	}

	// Hibernate要求複合主鍵類別要實作Serializable介面，並定義equals()與hashCode()方法
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof QueueMember)) {
			return false;
		}

		QueueMember qm = (QueueMember) obj;
		return new EqualsBuilder().append(this.queueName, qm.getQueueName())
				.append(this.iface, qm.getIface()).isEquals();

	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.queueName).append(this.iface)
				.toHashCode();
	}
}
