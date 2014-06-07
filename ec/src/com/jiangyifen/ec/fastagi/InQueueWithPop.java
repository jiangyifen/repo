package com.jiangyifen.ec.fastagi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.ShareData;

public class InQueueWithPop extends BaseAgiScript {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		String queueMember = this.getVariable("MEMBERINTERFACE");
		String callerid = this.getVariable("CALLERID(num)");

		this.exec(
				"Set",
				"CDR(userfield)=ANSWER,"
						+ queueMember.substring(queueMember.indexOf('/') + 1));
		String recFileName = this.getVariable("recFileName");

		logger.info("queue member " + queueMember + " answed the call from "
				+ callerid);

		// POP
		if (Config.props.getProperty("crm_pop_in_queueagi").equals("true")) {
			String uniqueid = this.getVariable("CDR(uniqueid)");
			String p1 = this.getVariable("p1");
			String p2 = this.getVariable("p2");

			String url = Config.props.getProperty("crm_server_url");
			url = url + "?callerId=" + callerid + "&exten="
					+ queueMember.substring(queueMember.indexOf('/') + 1)
					+ "&uniqueId=" + uniqueid + "&p1=" + p1 + "&p2=" + p2;
			logger.info("POP: "+url);

			try {
				new URL(url).openStream();
			} catch (MalformedURLException e) {
				logger.error(e.getMessage(), e);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}

		// 播放"XXXX为您服务"
		try {
			// String queueSayDigits = request.getParameter("saydigits");
			String queueSayDigits = Config.props
					.getProperty("queue_say_digits");
			if (queueSayDigits == null) {
				// do nothing
			} else if (queueSayDigits.equals("fenji")) {
				this.exec("wait", "1");
				String fenji = queueMember
						.substring(queueMember.indexOf('/') + 1);

				this.streamFile("customer/gonghao");
				this.sayDigits(fenji);
				this.streamFile("customer/foryou");
			} else if (queueSayDigits.equals("gonghao")) {
				this.exec("wait", "1");
				String fenji = queueMember
						.substring(queueMember.indexOf('/') + 1);
				String loginUsername = null;
				String gonghao = null;

				loginUsername = ShareData.sipAndLoginusername.get(fenji);
				if (loginUsername != null) {
					gonghao = ShareData.usernameAndUser.get(loginUsername)
							.getHid();
				}
				if (gonghao != null) {
					this.streamFile("customer/gonghao");
					this.sayDigits(gonghao);
					this.streamFile("customer/foryou");
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		// 获取录音文件存储路径
		String recLocalMemPath = Config.props.getProperty("rec_local_mem_path");

		logger.info(recLocalMemPath);
		
		// 开始录音
		// 在这里启动录音，而不是在dialplan中启动录音，是因为如果在dialplan中启动录音，而这里又播放工号的话，两个channel的录音文件mix后声音会不同步，因为开始录音的时间不一致。
		String format = Config.props.getProperty("rec_format");
		this.exec("MixMonitor", recLocalMemPath + recFileName + "." + format
				+ "|bw(4)");
		logger.info("exec MixMonitor " + recLocalMemPath + recFileName + "." + format
				+ "|bw(4)");

	}
}
