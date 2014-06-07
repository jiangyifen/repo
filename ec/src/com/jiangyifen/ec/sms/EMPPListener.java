/*
 * 创建日期 2005-12-14
 *
 * 
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.jiangyifen.ec.sms;

import java.math.BigInteger;
import java.util.Date;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.biz.DialTaskItemManager;
import com.jiangyifen.ec.biz.SmTaskManager;
import com.jiangyifen.ec.dao.SmTask;
import com.wondertek.esmp.esms.empp.EMPPAnswer;
import com.wondertek.esmp.esms.empp.EMPPChangePassResp;
import com.wondertek.esmp.esms.empp.EMPPDeliver;
import com.wondertek.esmp.esms.empp.EMPPDeliverReport;
import com.wondertek.esmp.esms.empp.EMPPObject;
import com.wondertek.esmp.esms.empp.EMPPRecvListener;
import com.wondertek.esmp.esms.empp.EMPPReqNoticeResp;
import com.wondertek.esmp.esms.empp.EMPPSubmitSM;
import com.wondertek.esmp.esms.empp.EMPPSubmitSMResp;
import com.wondertek.esmp.esms.empp.EMPPSyncAddrBookResp;
import com.wondertek.esmp.esms.empp.EMPPTerminate;
import com.wondertek.esmp.esms.empp.EMPPUnAuthorization;
import com.wondertek.esmp.esms.empp.EmppApi;

public class EMPPListener implements EMPPRecvListener {

	private final Log logger = LogFactory.getLog(getClass());

	private static final long RECONNECT_TIME = 10 * 1000;

	private EmppApi emppApi = null;

	private SmTaskManager smTaskManager;
	private DialTaskItemManager dialTaskItemManager;

	private int closedCount = 0;

	public EMPPListener(EmppApi emppApi, SmTaskManager smTaskManager, DialTaskItemManager dialTaskItemManager) {
		this.emppApi = emppApi;
		this.smTaskManager = smTaskManager;
		this.dialTaskItemManager = dialTaskItemManager;
	}

	// 处理接收到的消息
	public void onMessage(EMPPObject message) {

		if (message instanceof EMPPUnAuthorization) {
			EMPPUnAuthorization unAuth = (EMPPUnAuthorization) message;
			logger.info("客户端无权执行此操作 commandId=" + unAuth.getUnAuthCommandId());
			return;
		}
		if (message instanceof EMPPSubmitSMResp) {
			EMPPSubmitSMResp resp = (EMPPSubmitSMResp) message;
			logger.debug("收到sumbitResp:");
			byte[] msgId = fiterBinaryZero(resp.getMsgId());

			logger.debug("msgId=" + new BigInteger(msgId));
			logger.debug("result=" + resp.getResult());
			return;
		}
		if (message instanceof EMPPDeliver) {

			EMPPDeliver deliver = (EMPPDeliver) message;
			if (deliver.getRegister() == EMPPSubmitSM.EMPP_STATUSREPORT_TRUE) {
				try {
					// 收到状态报告
					EMPPDeliverReport report = deliver.getDeliverReport();
					String msgId = new BigInteger(fiterBinaryZero(report
							.getMsgId()))
							+ "";
					String status = report.getStat();

					logger.info("收到状态报告:" + msgId + "[" + status + "]");

					SmTask smTask = null;

					for (int i = 0; i <= 3; i++) {
						logger.info("根据msgid[" + msgId + "]获取短信" + i);
						Thread.sleep(3000);
						smTask = smTaskManager.getSmTask(msgId);
						if (smTask != null) {
							break;
						}
					}
					if (smTask != null) {
						smTask.setTaskReceiveReportTime(new Date());
						smTask.setStatus(status);
						smTaskManager.updateSmTask(smTask);
					} else {
						logger.error("根据msgid获取短信失败！");
					}

					logger.info("msgId=" + msgId);
					logger.info("status=" + status);
					
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}

			} else {
				try {
					// 收到手机回复
					String content = deliver.getMsgContent().getMessage();
					String mobile = deliver.getSrcTermId();
					String accountId = deliver.getDstAddr().substring(0, 14);
					String srcTermId = deliver.getDstAddr();
					String status = SmTask.RECEIVE;
					String msgId = new BigInteger(fiterBinaryZero(deliver
							.getMsgId()))
							+ "";
					Date timestamp = new Date();

					SmTask st = new SmTask();
					st.setContent(content);
					st.setMobile(mobile);
					st.setAccountId(accountId);
					st.setSrcTermId(srcTermId);
					st.setStatus(status);
					st.setMsgid(msgId);
					st.setTimestamp(timestamp);

					smTaskManager.addSmTask(st);

					logger.info("Receive SM from [" + mobile + "] to ["
							+ srcTermId + "], msgId=" + msgId + ", accountId="
							+ accountId);
					logger.info("Message：" + content);
					
					// for ec_dial_task_item
					dialTaskItemManager.updateSmReplyByPhoneNumber(mobile);
					// for ec_dial_task_item
					
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}

			}
			return;
		}
		if (message instanceof EMPPSyncAddrBookResp) {
			EMPPSyncAddrBookResp resp = (EMPPSyncAddrBookResp) message;
			if (resp.getResult() != EMPPSyncAddrBookResp.RESULT_OK)
				logger.info("同步通讯录失败");
			else {
				logger.info("收到服务器发送的通讯录信息");
				logger.info("通讯录类型为：" + resp.getAddrBookType());
				logger.info(resp.getAddrBook());
			}
		}
		if (message instanceof EMPPChangePassResp) {
			EMPPChangePassResp resp = (EMPPChangePassResp) message;
			if (resp.getResult() == EMPPChangePassResp.RESULT_VALIDATE_ERROR)
				logger.info("更改密码：验证失败");
			if (resp.getResult() == EMPPChangePassResp.RESULT_OK) {
				logger.info("更改密码成功,新密码为：" + resp.getPassword());
				emppApi.setPassword(resp.getPassword());
			}
			return;

		}
		if (message instanceof EMPPReqNoticeResp) {
			EMPPReqNoticeResp response = (EMPPReqNoticeResp) message;
			if (response.getResult() != EMPPReqNoticeResp.RESULT_OK)
				logger.info("查询运营商发布信息失败");
			else {
				logger.info("收到运营商发布的信息");
				logger.info(response.getNotice());
			}
			return;
		}
		if (message instanceof EMPPAnswer) {
			logger.info("收到企业疑问解答");
			EMPPAnswer answer = (EMPPAnswer) message;
			logger.info(answer.getAnswer());

		}
		logger.info(message);

	}

	// 处理连接断掉事件
	public void OnClosed(Object object) {
		// 该连接是被服务器主动断掉，不需要重连
		if (object instanceof EMPPTerminate) {
			logger.info("收到服务器发送的Terminate消息，连接终止");
			return;
		}
		// 这里注意要将emppApi做为参数传入构造函数
		// RecvListener listener = new RecvListener(emppApi,smTaskManager);
		logger.info("连接断掉次数：" + (++closedCount));
		for (int i = 1; !emppApi.isConnected(); i++) {
			try {
				logger.info("重连次数:" + i);
				Thread.sleep(RECONNECT_TIME);
				emppApi.reConnect(this);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);

			}
		}
		logger.info("重连成功");
	}

	// 处理错误事件
	public void OnError(Exception e) {
		logger.error(e.getMessage(),e);
	}

	public static byte[] fiterBinaryZero(byte[] bytes) {
		byte[] returnBytes = new byte[8];
		for (int i = 0; i < 8; i++) {
			returnBytes[i] = bytes[i];
		}
		return returnBytes;
	}
}
