package com.jiangyifen.ec.actions;

import com.jiangyifen.ec.dao.BlackListItem;
import com.jiangyifen.ec.util.BlackListData;

public class BlackListItemDeleteAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8334310463177097456L;

	private String id;

	public String execute() throws Exception {

		BlackListItem bli = blackListItemManager.get(new Long(id));
		if (bli != null) {
			String type = bli.getType();
			String phoneNumber = bli.getPhoneNumber();
			if (type != null && phoneNumber != null) {
				if (type.equalsIgnoreCase(BlackListData.TYPE_BOTH)) {
					BlackListData.removeIncomingBlackListItem(phoneNumber);
					BlackListData.removeOutgoingBlackListItem(phoneNumber);
				} else if (type.equalsIgnoreCase(BlackListData.TYPE_INCOMING)) {
					BlackListData.removeIncomingBlackListItem(phoneNumber);
				} else if (type.equalsIgnoreCase(BlackListData.TYPE_OUTGOING)) {
					BlackListData.removeOutgoingBlackListItem(phoneNumber);
				}
			}
		}

		if (id != null && !id.equals("")) {
			Long idlong = new Long(id);
			blackListItemManager.delete(idlong);
		}
		return SUCCESS;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
