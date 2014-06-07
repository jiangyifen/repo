package com.jiangyifen.ec.actions;

import com.jiangyifen.ec.dao.BlackListItem;
import com.jiangyifen.ec.util.BlackListData;

public class BlackListItemAddAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2173974280751976246L;

	private String phoneNumber;
	private String type;
	private String remark;



	public String execute() throws Exception {

		if (type.equalsIgnoreCase(BlackListData.TYPE_BOTH)) {
			BlackListData.addIncomingBlackListItem(phoneNumber);
			BlackListData.addOutgoingBlackListItem(phoneNumber);
		} else if (type.equalsIgnoreCase(BlackListData.TYPE_INCOMING)) {
			BlackListData.addIncomingBlackListItem(phoneNumber);
		} else if (type.equalsIgnoreCase(BlackListData.TYPE_OUTGOING)) {
			BlackListData.addOutgoingBlackListItem(phoneNumber);
		}

		BlackListItem bli = new BlackListItem();
		bli.setPhoneNumber(phoneNumber);
		bli.setType(type);
		bli.setRemark(remark);
		blackListItemManager.update(bli);

		phoneNumber = "";
		return SUCCESS;

	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

}
