package com.jiangyifen.ec.actions;

public class SipDeleteAction extends BaseAction {

	private static final long serialVersionUID = 570045581700715712L;

	private Long[] id;

	public void setId(Long[] id) {
		this.id = id;
	}

	public Long[] getId() {
		return id;
	}

	public String execute() throws Exception {
		if (id != null) {
				sipManager.deleteSips(id);
			return SUCCESS;
		} else {
			return INPUT;
		}
	}


}
