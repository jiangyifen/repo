package com.jiangyifen.ec.customer.olay;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.customer.olay.ws.MemberInfo;
import com.jiangyifen.ec.customer.olay.ws.MyOlayIVRSrv;
import com.jiangyifen.ec.customer.olay.ws.MyOlayIVRSrvPortType;
import com.jiangyifen.ec.customer.olay.ws.RemoteException;
import com.jiangyifen.ec.customer.olay.ws.WebReturnObject;

public class OlayWSSetMemberInfo extends BaseAgiScript {

	private static Logger logger = LoggerFactory
			.getLogger(OlayWSSetMemberInfo.class);

	private static MyOlayIVRSrv srv = null;

	private static MyOlayIVRSrvPortType pt = null;

	private static void initClient() {
		try {
			URL url = new URL(OlayGlobalData.IVRPointQueryWSDL);
			srv = new MyOlayIVRSrv(url);
			pt = srv.getMyOlayIVRSrvHttpPort();
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		try {

			String getMemberInfo_accountNumber = getVariable("getMemberInfo_accountNumber");
			String setMemberInfo_new_mobile = getVariable("setMemberInfo_new_mobile");

			logger.info("set member info: getMemberInfo_accountNumber = "
					+ getMemberInfo_accountNumber);
			logger.info("set member info: setMemberInfo_new_mobile = "
					+ setMemberInfo_new_mobile);

			Integer exitCode = setMemberInfo(getMemberInfo_accountNumber,
					setMemberInfo_new_mobile);

			logger.info("set member info: exitCode=" + exitCode);

			setVariable("setMemberInfo_exitCode", exitCode.toString());

		} catch (RemoteException e) {
			initClient();
			logger.error(e.getMessage(), e);
		}
	}

	private static Integer setMemberInfo(String accountNumber, String mobile)
			throws RemoteException {
		if (srv == null || pt == null) {
			initClient();
		}

		MemberInfo memberInfo = new MemberInfo();

		memberInfo.setAccountNumber(new JAXBElement<String>(new QName(
				"http://web.myolay.www.accentiv.cn", "accountNumber"),
				String.class, accountNumber));

		memberInfo.setMobile(new JAXBElement<String>(new QName(
				"http://web.myolay.www.accentiv.cn", "mobile"), String.class,
				mobile));

		WebReturnObject returnObject = pt.setmemberinfo(memberInfo);

		Integer exitCode = returnObject.getExitCode();

		return exitCode;
	}
	
	
	public static void main(String[] args) {
		try {

			// 01140010987
			// 01140011925
			// 07130386553
			// 09130003411
			String getMemberInfo_accountNumber = "01140010987";
			String setMemberInfo_new_mobile = "13761488223";

			logger.info("set member info: getMemberInfo_accountNumber = "
					+ getMemberInfo_accountNumber);
			logger.info("set member info: setMemberInfo_new_mobile = "
					+ setMemberInfo_new_mobile);

			Integer exitCode = setMemberInfo(getMemberInfo_accountNumber,
					setMemberInfo_new_mobile);

			logger.info("set member info: exitCode=" + exitCode);

		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
