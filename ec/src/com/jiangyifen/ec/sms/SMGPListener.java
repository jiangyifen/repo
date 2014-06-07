package com.jiangyifen.ec.sms;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.biz.DialTaskItemManager;
import com.jiangyifen.ec.biz.SmTaskManager;
import com.jiangyifen.ec.dao.SmTask;
import com.wondertek.ctmp.protocol.smgp.SMGPActiveTestMessage;
import com.wondertek.ctmp.protocol.smgp.SMGPActiveTestRespMessage;
import com.wondertek.ctmp.protocol.smgp.SMGPApi;
import com.wondertek.ctmp.protocol.smgp.SMGPBaseMessage;
import com.wondertek.ctmp.protocol.smgp.SMGPDeliverMessage;
import com.wondertek.ctmp.protocol.smgp.SMGPDeliverRespMessage;
import com.wondertek.ctmp.protocol.smgp.SMGPReportData;
import com.wondertek.ctmp.protocol.smgp.SMGPSubmitRespMessage;

public class SMGPListener implements Runnable {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private SMGPApi api;
	private SmTaskManager smTaskManager;
	private DialTaskItemManager dialTaskItemManager;

	public SMGPListener(SMGPApi api, SmTaskManager smTaskManager,DialTaskItemManager dialTaskItemManager) {
		this.api = api;
		this.smTaskManager = smTaskManager;
		this.dialTaskItemManager = dialTaskItemManager;
	}

	public void run() {

		while (true) {
			try {
//				if (!api.isConnected()) {
//					Thread.sleep(1000);
//					// new add
//					logger.info(api.getClientId() + " reconnecting...");
//					api.disconnect();
//					SMGPLoginRespMessage resp = api.connect();
//					if (resp != null && resp.getStatus() == 0) {
//						logger.info(api.getClientId() + " reconnected");
//					} else {
//						logger.error(api.getClientId() + " reconnect fail, status="
//								+ resp.getStatus());
//					}
//
//				}
				SMGPBaseMessage baseMsg = api.receiveMsg();
				if (baseMsg == null) {
					api.disconnect();
					continue;
				}

				if (baseMsg instanceof SMGPActiveTestMessage) {
					SMGPActiveTestRespMessage resp = new SMGPActiveTestRespMessage();
					resp.setSequenceNumber(baseMsg.getSequenceNumber());
					api.sendMsg(resp);
				} else if (baseMsg instanceof SMGPActiveTestRespMessage) {
//					logger.info("SMGPActiveTestRespMessage:" + baseMsg);
				}

				if (baseMsg instanceof SMGPSubmitRespMessage) {
					// 收到短信提交返回消息
					SMGPSubmitRespMessage resp = (SMGPSubmitRespMessage) baseMsg;

					int seq = resp.getSequenceNumber();
					SmTask st = smTaskManager.getSmTask(seq + "");

					String msgid = resp.msgIdString();
					st.setMsgid(msgid);

					// 根据结果update任务的状态
					int status = resp.getStatus();
					st.setStatus(status + "");

					smTaskManager.updateSmTask(st);
				}

				if (baseMsg instanceof SMGPDeliverMessage) {
					SMGPDeliverMessage deliver = (SMGPDeliverMessage) baseMsg;
					SMGPDeliverRespMessage deliverResp = new SMGPDeliverRespMessage();
					deliverResp.setMsgId(deliver.getMsgId());
					deliverResp.setSequenceNumber(deliver.getSequenceNumber());
					deliverResp.setStatus(0);
					api.sendMsg(deliverResp);

					if (deliver.getIsReport() == 0) {
						// 上行短信（手机回复过来的短信）

						String content = deliver.getMsgContent();
						String mobile = deliver.getSrcTermId();
						String accountId = deliver.getDestTermId().substring(0,
								13);
						String srcTermId = deliver.getDestTermId();
						String status = SmTask.RECEIVE;
						String msgId = deliver.msgIdString();
						Date timestamp = new Date();

						logger.info("Receive SM from [" + mobile + "] to ["
								+ srcTermId + "], msgId=" + msgId
								+ ", accountId=" + accountId);
						logger.info("Message：" + content);
						
						SmTask est = new SmTask();
						est.setContent(content);
						est.setMobile(mobile);
						est.setAccountId(accountId);
						est.setSrcTermId(srcTermId);
						est.setStatus(status);
						est.setMsgid(msgId);
						est.setTimestamp(timestamp);

						smTaskManager.addSmTask(est);

						logger.info("Message saved.");
						
						// for ec_dial_task_item
						dialTaskItemManager.updateSmReplyByPhoneNumber(mobile);
						// for ec_dial_task_item
					} else {
						// 状态报告
						SMGPReportData report = deliver.getReport();

						String msgid = report.msgIdString();
						SmTask st = null;
						Thread.sleep(100);
						for (int i = 0; i <= 3; i++) {
							logger.info("get sm task by msgid[" + msgid + "], retry=" + i);
							st = smTaskManager.getSmTask(msgid);
							if (st != null) {
								break;
							}
							Thread.sleep(100);
						}
						if (st != null) {
							st.setStatus(report.getStat());
							logger.info(report.getStat());
							st.setTaskReceiveReportTime(new Date());
							smTaskManager.updateSmTask(st);
						} else {
							logger.error("get sm task by msgid failed!");
						}
					}
				}

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				api.disconnect();
				break;
			}
		}

	}

}
