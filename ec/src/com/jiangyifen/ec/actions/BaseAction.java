package com.jiangyifen.ec.actions;

import com.jiangyifen.ec.biz.BlackListItemManager;
import com.jiangyifen.ec.biz.CdrManager;
import com.jiangyifen.ec.biz.ConfigManager;
import com.jiangyifen.ec.biz.CustomerManagerManager;
import com.jiangyifen.ec.biz.CustomerPhoneNumManager;
import com.jiangyifen.ec.biz.CustomerSatisfactionInvestigationLogManager;
import com.jiangyifen.ec.biz.DepartmentManager;
import com.jiangyifen.ec.biz.DialTaskItemManager;
import com.jiangyifen.ec.biz.DialTaskLogManager;
import com.jiangyifen.ec.biz.DialTaskManager;
import com.jiangyifen.ec.biz.MeetMeRoomManager;
import com.jiangyifen.ec.biz.MyCustomerLogManager;
import com.jiangyifen.ec.biz.NoticeItemManager;
import com.jiangyifen.ec.biz.NoticeManager;
import com.jiangyifen.ec.biz.QueueCallerAbandonEventLogManager;
import com.jiangyifen.ec.biz.QueueEntryEventLogManager;
import com.jiangyifen.ec.biz.QueueManager;
import com.jiangyifen.ec.biz.QueueMemberManager;
import com.jiangyifen.ec.biz.QueueMemberPauseLogManager;
import com.jiangyifen.ec.biz.RoleActionManager;
import com.jiangyifen.ec.biz.RoleManager;
import com.jiangyifen.ec.biz.ShiftConfigManager;
import com.jiangyifen.ec.biz.SipManager;
import com.jiangyifen.ec.biz.SmTaskManager;
import com.jiangyifen.ec.biz.SmUserInfoManager;
import com.jiangyifen.ec.biz.UserLoginRecordManager;
import com.jiangyifen.ec.biz.UserManager;
import com.jiangyifen.ec.biz.VoicemailManager;
import com.opensymphony.xwork2.ActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2948895753179022723L;
	protected Logger logger = LoggerFactory.getLogger(BaseAction.class);

	protected UserManager userManager;
	protected RoleManager roleManager;
	protected RoleActionManager roleActionManager;
	protected DepartmentManager departmentManager;
	protected CdrManager cdrManager;
	protected VoicemailManager vmManager;
	protected QueueManager queueManager;
	protected QueueMemberManager queueMemberManager;
	protected SipManager sipManager;
	protected CustomerPhoneNumManager customerPhoneNumManager;
	protected CustomerManagerManager customerManagerManager;
	protected UserLoginRecordManager userLoginRecordManager;
	protected DialTaskManager dialTaskManager;
	protected DialTaskItemManager dialTaskItemManager;
	protected DialTaskLogManager dialTaskLogManager;
	protected MyCustomerLogManager myCustomerLogManager;
	protected QueueEntryEventLogManager queueEntryEventLogManager;
	protected QueueCallerAbandonEventLogManager queueCallerAbandonEventLogManager;

	protected QueueMemberPauseLogManager queueMemberPauseLogManager;
	protected CustomerSatisfactionInvestigationLogManager customerSatisfactionInvestigationLogManager;

	protected BlackListItemManager blackListItemManager;

	protected MeetMeRoomManager meetMeRoomManager;
	protected SmUserInfoManager smUserInfoManager;
	protected SmTaskManager smTaskManager;

	protected NoticeManager noticeManager;
	protected NoticeItemManager noticeItemManager;

	protected ConfigManager configManager;
	
	protected ShiftConfigManager shiftConfigManager;

	public void setQueueMemberPauseLogManager(
			QueueMemberPauseLogManager queueMemberPauseLogManager) {
		this.queueMemberPauseLogManager = queueMemberPauseLogManager;
	}

	public void setCustomerSatisfactionInvestigationLogManager(
			CustomerSatisfactionInvestigationLogManager customerSatisfactionInvestigationLogManager) {
		this.customerSatisfactionInvestigationLogManager = customerSatisfactionInvestigationLogManager;
	}

	public void setMeetMeRoomManager(MeetMeRoomManager meetMeRoomManager) {
		this.meetMeRoomManager = meetMeRoomManager;
	}

	public void setSmUserInfoManager(SmUserInfoManager smUserInfoManager) {
		this.smUserInfoManager = smUserInfoManager;
	}

	public void setSmTaskManager(SmTaskManager smTaskManager) {
		this.smTaskManager = smTaskManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public void setDepartmentManager(DepartmentManager departmentManager) {
		this.departmentManager = departmentManager;
	}

	public void setRoleActionManager(RoleActionManager roleActionManager) {
		this.roleActionManager = roleActionManager;
	}

	public void setCdrManager(CdrManager cdrManager) {
		this.cdrManager = cdrManager;
	}

	public void setVmManager(VoicemailManager vmManager) {
		this.vmManager = vmManager;
	}

	public void setQueueManager(QueueManager queueManager) {
		this.queueManager = queueManager;
	}

	public void setQueueMemberManager(QueueMemberManager queueMemberManager) {
		this.queueMemberManager = queueMemberManager;
	}

	public void setSipManager(SipManager sipManager) {
		this.sipManager = sipManager;
	}

	public void setCustomerPhoneNumManager(
			CustomerPhoneNumManager customerPhoneNumManager) {
		this.customerPhoneNumManager = customerPhoneNumManager;
	}

	public void setCustomerManagerManager(
			CustomerManagerManager customerManagerManager) {
		this.customerManagerManager = customerManagerManager;
	}

	public void setUserLoginRecordManager(
			UserLoginRecordManager userLoginRecordManager) {
		this.userLoginRecordManager = userLoginRecordManager;
	}

	public void setDialTaskManager(DialTaskManager dialTaskManager) {
		this.dialTaskManager = dialTaskManager;
	}

	public void setDialTaskItemManager(DialTaskItemManager dialTaskItemManager) {
		this.dialTaskItemManager = dialTaskItemManager;
	}

	public void setDialTaskLogManager(DialTaskLogManager dialTaskLogManager) {
		this.dialTaskLogManager = dialTaskLogManager;
	}

	public void setMyCustomerLogManager(
			MyCustomerLogManager myCustomerLogManager) {
		this.myCustomerLogManager = myCustomerLogManager;
	}

	public void setQueueEntryEventLogManager(
			QueueEntryEventLogManager queueEntryEventLogManager) {
		this.queueEntryEventLogManager = queueEntryEventLogManager;
	}

	public void setQueueCallerAbandonEventLogManager(
			QueueCallerAbandonEventLogManager queueCallerAbandonEventLogManager) {
		this.queueCallerAbandonEventLogManager = queueCallerAbandonEventLogManager;
	}

	public void setBlackListItemManager(
			BlackListItemManager blackListItemManager) {
		this.blackListItemManager = blackListItemManager;
	}

	public void setNoticeManager(NoticeManager noticeManager) {
		this.noticeManager = noticeManager;
	}

	public void setNoticeItemManager(NoticeItemManager noticeItemManager) {
		this.noticeItemManager = noticeItemManager;
	}

	public void setConfigManager(ConfigManager configManager) {
		this.configManager = configManager;
	}

	public void setShiftConfigManager(ShiftConfigManager shiftConfigManager) {
		this.shiftConfigManager = shiftConfigManager;
	}
	
}
