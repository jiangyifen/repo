package com.jiangyifen.ec.customer.olay;

import java.net.MalformedURLException;
import java.net.URL;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.customer.olay.ws.MemberInfoReturnObject;
import com.jiangyifen.ec.customer.olay.ws.MyOlayIVRSrv;
import com.jiangyifen.ec.customer.olay.ws.MyOlayIVRSrvPortType;
import com.jiangyifen.ec.customer.olay.ws.RemoteException;

public class OlayWSGetMemberInfoTest extends BaseAgiScript {

	private static Logger logger = LoggerFactory
			.getLogger(OlayWSGetMemberInfoTest.class);

	private static MyOlayIVRSrv srv = null;

	private static MyOlayIVRSrvPortType pt = null;

	private void initClient() {
		try {
			URL url = new URL(OlayGlobalData.testIVRPointQueryWSDL);
			logger.info("getMemberInfo: ===000");
			srv = new MyOlayIVRSrv(url);
			logger.info("getMemberInfo: ===111");
			pt = srv.getMyOlayIVRSrvHttpPort();
			logger.info("getMemberInfo: ===222");
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(), e);
		}

	}

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		try {
			if (srv == null || pt == null) {
				logger.info("getMemberInfo: 111");
				initClient();
				logger.info("getMemberInfo: 222");
			}else{
				logger.info("getMemberInfo: 0000000000");
			}
			
			String getMemberInfo_accountNumber = getVariable("getMemberInfo_accountNumber");

			logger.info("getMemberInfo: getMemberInfo_accountNumber="
					+ getMemberInfo_accountNumber);



			logger.info("getMemberInfo: 11111111");
			
			MemberInfoReturnObject rt = pt
					.getmemberinfo(getMemberInfo_accountNumber);

			Integer exitCode = rt.getExitCode();
			logger.info("getMemberInfo: exitCode=" + exitCode);
			
			Integer accountID = 0;
			String birthday = "";
			String mobile = "";

			if (exitCode == 0) {
				accountID = rt.getResult().getValue().getAccountID();
				birthday = rt.getResult().getValue().getBirthday().getValue();
				mobile = rt.getResult().getValue().getMobile().getValue();
			}
			logger.info("getMemberInfo: 22222222");
			
			logger.info("getMemberInfo: accountID=" + accountID);
			logger.info("getMemberInfo: birthday=" + birthday);
			logger.info("getMemberInfo: mobile=" + mobile);

			setVariable("getMemberInfo_exitCode", exitCode.toString());
			setVariable("getMemberInfo_accountID", accountID.toString());
			setVariable("getMemberInfo_birthday", birthday);
			setVariable("getMemberInfo_mobile", mobile);

			logger.info("getMemberInfo: 33333333");
			
		} catch (RemoteException e) {
			initClient();
			logger.error(e.getMessage(), e);
		}
	}

	public static void main(String[] args) {
		try {

			// 01140010987
			// 01140011925
			// 07130386553
			// 09130003411

			String accountNumber = "01140010987";

			logger.info("getMemberInfo: accountNumber=" + accountNumber);

			URL url = new URL(OlayGlobalData.testIVRPointQueryWSDL);
			srv = new MyOlayIVRSrv(url);
			pt = srv.getMyOlayIVRSrvHttpPort();

			MemberInfoReturnObject rt = pt.getmemberinfo(accountNumber);

			Integer exitCode = rt.getExitCode();

			Integer accountID = 0;
			String birthday = "";
			String mobile = "";

			if (exitCode == 0) {
				accountID = rt.getResult().getValue().getAccountID();
				birthday = rt.getResult().getValue().getBirthday().getValue();
				mobile = rt.getResult().getValue().getMobile().getValue();
			}

			logger.info("getMemberInfo: exitCode=" + exitCode);
			logger.info("getMemberInfo: accountID=" + accountID);
			logger.info("getMemberInfo: birthday=" + birthday);
			logger.info("getMemberInfo: mobile=" + mobile);

		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
