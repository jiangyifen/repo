package com.jiangyifen.ec.actions.iface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.actions.BaseAction;
import com.jiangyifen.ec.dao.Cdr;
import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.DialTask;
import com.jiangyifen.ec.dao.DialTaskItem;
import com.jiangyifen.ec.dao.MyCustomerLog;
import com.jiangyifen.ec.dao.Sip;
import com.jiangyifen.ec.dao.User;
import com.jiangyifen.ec.util.Config;

public class MyCustomerLogAddAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6949105747316202235L;

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private String u;
	private String p;
	
	private String sipName;

	private String hid;
	private String customerPhoneNumber;
	
	private String username;
	

	public String execute() throws Exception {
		String iface_u = Config.props.getProperty("3rd_username");
		String iface_p = Config.props.getProperty("3rd_password");
		
		if(!(iface_u.equals(u) && iface_p.equals(p))){
			return LOGIN;
		}

		Long dialTaskId;
		String dialTaskName;

		if(sipName != null){
			// 根据分机号确定username
			Sip sip = sipManager.findSipByName(sipName);
			username = sip.getLoginusername();

			// 进而确定hid，以及找出cdr中的客户电话号码
			hid = getHidByUsername(username);

			if(username.equals("0")){
				logger.info("MyCustomer: getcustomerPhoneNumberByDst("+sipName+")");
				customerPhoneNumber = getCustomerPhoneNumberByDst(sipName);
			}else{
				logger.info("MyCustomer: getCustomerPhoneNumberByUsername("+username+")");
				customerPhoneNumber = getCustomerPhoneNumberByUsername(username);
			}

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			
			logger.info(df.format(new Date()) + " MyCustomer: " + "sip name=" + sipName + "; loginusername=" + username + "; customerPhoneNumber=" + customerPhoneNumber + "; HID=" + hid);
		}
		if (hid != null && customerPhoneNumber != null) {
			//如果该号码已经注册，则直接结束流程
			if(myCustomerLogManager.getMyCustomerLogCountByCustomerPhoneNumber(customerPhoneNumber)>0){
				logger.info("MyCustomer: " + "already have number " + customerPhoneNumber);
				return SUCCESS;
			}

			// 根据电话号码，尝试确定其所属的dialtask
			dialTaskId = getDialTaskId(customerPhoneNumber);
			dialTaskName = getDialTaskName(dialTaskId);
			logger.info("MyCustomer:" + "taskId=" + dialTaskId + "; taskName="+dialTaskName);

			// 保存
			Department d = userManager.getUser(username).getDepartment();
			String department = d.getDepartmentname();
			
			MyCustomerLog log = new MyCustomerLog();
			log.setUsername(username);
			log.setDepartment(department);
			log.setHid(hid);
			log.setCustomerPhoneNumber(customerPhoneNumber);
			log.setDialTaskId(dialTaskId);
			log.setDialTaskName(dialTaskName);
			myCustomerLogManager.save(log);
			
			//将customerPhoneNumber对应的最后一条cdr打上标记，表示这通电话打出了意向客户
			updateCdrIntention(customerPhoneNumber,hid);
			logger.info("MyCustomer: ok");

		}

		return SUCCESS;
	}
	
	private String getHidByUsername(String username){
		String hid=null;
		User user;
		try {
			user = userManager.getUser(username);
			if (user != null) {
				hid = user.getHid();
			}
			if (hid == null) {
				hid = "0";
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		return hid;
	}

	
	private String getCustomerPhoneNumberByUsername(String username){
		String customerPhoneNumber = null;
		
		List<Cdr> cdrList = cdrManager.findCdrByUsername(username, 1, 1);
		if (cdrList.size() >= 1) {
			Cdr cdr = cdrList.get(0);
			String src = cdr.getSrc();
			String dst = cdr.getDst();
			if (src.length() > 4) {
				customerPhoneNumber = src;
			} else if (dst.length() > 4) {
				customerPhoneNumber = dst;
			} else {
				customerPhoneNumber = "dst";
			}
		}
		
		return customerPhoneNumber;
	}
	
	private String getCustomerPhoneNumberByDst(String dst){
		String customerPhoneNumber = null;
		
		List<Cdr> cdrList = cdrManager.findCdrByDst(dst, 1, 1);
		if (cdrList.size() >= 1) {
			Cdr cdr = cdrList.get(0);
			String src = cdr.getSrc();
			if (src.length() > 4) {
				customerPhoneNumber = src;
			} else {
				customerPhoneNumber = "UNKNOW";
			}
		}
		
		return customerPhoneNumber;
	}
	
	private Long getDialTaskId(String phoneNumber) {
		Long dialTaskId;
		List<DialTaskItem> dtiList = dialTaskItemManager
				.findByPhoneNumberAndStatus(1, 1, customerPhoneNumber,
						DialTaskItem.STATUS_FINISH);
		if (dtiList.size() >= 1) {
			dialTaskId = dtiList.get(0).getTaskid();
		} else {
			dialTaskId = 0L;
		}
		return dialTaskId;
	}

	private String getDialTaskName(Long dialTaskId) {
		String dialTaskName;
		DialTask dt = dialTaskManager.get(dialTaskId);
		if (dt != null) {
			dialTaskName = dt.getTaskName();
		} else {
			dialTaskName = "UNKNOW TASK";
		}
		return dialTaskName;
	}


	private void updateCdrIntention(String phoneNumber,String hid){
		List<Cdr> cdr = cdrManager.findCdrByHIDSrcOrDst(phoneNumber,hid, 1, 1);
		if(cdr!=null && cdr.size()>0){
			Cdr c = cdr.get(0);
			c.setIntention(true);
			cdrManager.updateCdr(c);
		}
	}
	public void setSipName(String sipName) {
		this.sipName = sipName;
	}

	public String getSipName() {
		return sipName;
	}

	public void setCustomerPhoneNumber(String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}

	public String getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}

	public String getHid() {
		return hid;
	}

	public void setU(String u) {
		this.u = u;
	}

	public String getU() {
		return u;
	}

	public void setP(String p) {
		this.p = p;
	}

	public String getP() {
		return p;
	}

}
