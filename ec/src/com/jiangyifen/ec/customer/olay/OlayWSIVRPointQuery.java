package com.jiangyifen.ec.customer.olay;

import java.net.MalformedURLException;
import java.net.URL;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.customer.olay.ws.MyOlayIVRSrv;
import com.jiangyifen.ec.customer.olay.ws.MyOlayIVRSrvPortType;
import com.jiangyifen.ec.customer.olay.ws.PointReturnObject;
import com.jiangyifen.ec.customer.olay.ws.RemoteException;

public class OlayWSIVRPointQuery extends BaseAgiScript {

	private static Logger logger = LoggerFactory
			.getLogger(OlayWSIVRPointQuery.class);

	private static MyOlayIVRSrv srv = null;

	private static MyOlayIVRSrvPortType pt = null;

	private void initClient() {
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
			
			if(srv==null || pt==null){
				initClient();
			}

			String userId = request.getParameter("userId");

			logger.info("JJJ: userId = " + userId);

			PointReturnObject rt = pt.ivrPointQuery(userId);

			Integer exitCode = rt.getExitCode();

			String city = "";
			String accountCategory = "";
			String availablePoint = "0";
			String totalPoint = "0";
			String memberID = "";
			String name = "";

			if (exitCode == 0) {
				city = rt.getResult().getValue().getCity().getValue();
				accountCategory = rt.getResult().getValue()
						.getAccountCategory().getValue();
				availablePoint = rt.getResult().getValue().getAvailablePoint()
						.toString();
				totalPoint = rt.getResult().getValue().getTotalPoint()
						.toString();
				memberID = rt.getResult().getValue().getMemberID().getValue();
				name = rt.getResult().getValue().getName().getValue();
			}

			logger.info("JJJ: exitCode=" + exitCode);
			logger.info("JJJ: memberID=" + memberID);
			logger.info("JJJ: name=" + name);

			logger.info("JJJ: city=" + city);
			logger.info("JJJ: accountCategory=" + accountCategory);

			logger.info("JJJ: totalPoint=" + totalPoint);
			logger.info("JJJ: availablePoint=" + availablePoint);

			setVariable("exitCode", exitCode.toString());
			setVariable("totalPoint", totalPoint);
			setVariable("availablePoint", availablePoint);
			setVariable("accountCategory",accountCategory);

		} catch (RemoteException e) {
			initClient();
			logger.error(e.getMessage(), e);
		}
	}

	public static void main(String[] args) {
		try {

			String userId = "13031226136";
			
			URL url = new URL(OlayGlobalData.testIVRPointQueryWSDL);

			MyOlayIVRSrv srv = new MyOlayIVRSrv(url);

			MyOlayIVRSrvPortType pt = srv.getMyOlayIVRSrvHttpPort();

			logger.info("JJJ: userId = " + userId);

			PointReturnObject rt = pt.ivrPointQuery(userId);

			Integer exitCode = rt.getExitCode();

			String city = "";
			String accountCategory = "";
			String availablePoint = "0";
			String totalPoint = "0";
			String memberID = "";
			String name = "";

			if (exitCode == 0) {
				city = rt.getResult().getValue().getCity().getValue();
				accountCategory = rt.getResult().getValue()
						.getAccountCategory().getValue();
				availablePoint = rt.getResult().getValue().getAvailablePoint()
						.toString();
				totalPoint = rt.getResult().getValue().getTotalPoint()
						.toString();
				memberID = rt.getResult().getValue().getMemberID().getValue();
				name = rt.getResult().getValue().getName().getValue();
			}

			logger.info("JJJ: exitCode=" + exitCode);
			logger.info("JJJ: memberID=" + memberID);
			logger.info("JJJ: name=" + name);

			logger.info("JJJ: city=" + city);
			logger.info("JJJ: accountCategory=" + accountCategory);

			logger.info("JJJ: totalPoint=" + totalPoint);
			logger.info("JJJ: availablePoint=" + availablePoint);

		} catch (RemoteException e) {

			logger.error(e.getMessage(), e);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
