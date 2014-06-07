package com.jiangyifen.ec.sms;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.biz.DialTaskItemManager;
import com.jiangyifen.ec.biz.SmTaskManager;
import com.jiangyifen.ec.dao.SmTask;
import com.jiangyifen.ec.dao.SmUserInfo;
import com.wondertek.ctmp.protocol.smgp.SMGPApi;
import com.wondertek.ctmp.protocol.smgp.SMGPLoginRespMessage;
import com.wondertek.ctmp.protocol.smgp.SMGPSubmitMessage;
import com.wondertek.ctmp.protocol.util.SequenceGenerator;
import com.wondertek.esmp.esms.empp.EMPPConnectResp;
import com.wondertek.esmp.esms.empp.EMPPData;
import com.wondertek.esmp.esms.empp.EMPPObject;
import com.wondertek.esmp.esms.empp.EMPPShortMsg;
import com.wondertek.esmp.esms.empp.EMPPSubmitSM;
import com.wondertek.esmp.esms.empp.EMPPSubmitSMResp;
import com.wondertek.esmp.esms.empp.EmppApi;

public class SmSenderWorkThread extends Thread {

	private final Log logger = LogFactory.getLog(getClass());
	private SmTaskManager smTaskManager;
	private DialTaskItemManager dialTaskItemManager;

	private SmUserInfo sui = null;

	private ConcurrentLinkedQueue<SmTask> tasks = null;

	// CMCC EMPP API
	private EmppApi emppApi = null;
	private EMPPListener listener = null;

	// CT SMGP API
	private SMGPApi smgpApi = null;
	private Thread smgpListener = null;
	private Thread smgpActiveTest = null;
	
	public SmSenderWorkThread(SmUserInfo sui, SmTaskManager smTaskManager,DialTaskItemManager dialTaskItemManager) {
		this.sui = sui;
		this.smTaskManager = smTaskManager;
		this.dialTaskItemManager = dialTaskItemManager;

		this.connect();

		// 初始化任务队列
		tasks = new ConcurrentLinkedQueue<SmTask>();

		this.setDaemon(true);
		this.setName("SmSenderWorkThread: [" + sui.getOperator() + "]"
				+ sui.getAccountId());
		this.start();
		logger.info(this.getName() + " started.");
	}

	public int getTaskQueueSize() {

		return tasks.size();
	}

	public void addTasks(List<SmTask> tasks) {
		this.tasks.addAll(tasks);
		logger.info(getName() + " add [" + tasks.size() + "] tasks");
		logger.info(getName() + " task queue size：" + tasks.size());
	}

	private boolean connect() {

		try {
			if (sui.getOperator().equals(SmUserInfo.CMCC)) {
				// 建立同服务器的连接
				emppApi = new EmppApi();
				listener = new EMPPListener(emppApi, smTaskManager,dialTaskItemManager);

				EMPPConnectResp resp = emppApi.connect(sui.getHost(), sui
						.getPort(), sui.getUsername(), sui.getPassword(),
						listener);
				logger.info(resp);

				if (resp != null && emppApi.isConnected()) {
					logger.info(sui.getAccountId() + " connected");
					return true;
				} else if (resp != null) {
					logger.error(sui.getAccountId() + " connect fail, status="
							+ resp.getStatus());
					return false;
				} else if (resp == null) {
					logger.error(sui.getAccountId() + " connect fail.");
					return false;
				}
			} else if (sui.getOperator().equals(SmUserInfo.CT)) {
				if(smgpApi!=null){
					smgpApi.disconnect();
					smgpApi.close();
				}
				smgpApi = new SMGPApi();

				smgpApi.setHost(sui.getHost());
				smgpApi.setPort(sui.getPort());
				smgpApi.setClientId(sui.getUsername());
				smgpApi.setPassword(sui.getPassword());
				smgpApi.setVersion((byte) 0);

				SMGPLoginRespMessage resp = smgpApi.connect();

				smgpListener = new Thread(new SMGPListener(smgpApi,
						smTaskManager,dialTaskItemManager));
				smgpListener.setName("SMGPListener");
				smgpListener.setDaemon(true);
				smgpListener.start();

				smgpActiveTest = new Thread(
						new SMGPActiveTestThread(smgpApi));
				smgpActiveTest.setName("SMGP ActiveTestThread");
				smgpActiveTest.setDaemon(true);
				smgpActiveTest.start();

				if (resp != null && resp.getStatus() == 0) {
					logger.info(sui.getAccountId() + " connected");
					return true;
				} else {
					logger.error(sui.getAccountId() + " connect fail, status="
							+ resp.getStatus());
					return false;
				}

			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}

		return true;
	}

	private EMPPSubmitSMResp submitEMPPSmTask(SmTask st) {

		if (emppApi != null && emppApi.isSubmitable()) {

			try {
				EMPPSubmitSM msg = (EMPPSubmitSM) EMPPObject
						.createEMPP(EMPPData.EMPP_SUBMIT);

				List<String> dstId = new ArrayList<String>();
				dstId.add(st.getMobile());
				msg.setDstTermId(dstId);

				msg.setSrcTermId(st.getSrcTermId());

				msg.setServiceId("0");

				EMPPShortMsg msgContent = new EMPPShortMsg(
						EMPPShortMsg.EMPP_MSG_CONTENT_MAXLEN);

				String message = "" + st.getContent();
				if (message.length() > 70) {
					message = message.substring(0, 70);
				}
				msgContent.setMessage(message.getBytes("GBK"));
				msg.setShortMessage(msgContent);

				msg.assignSequenceNumber();

				EMPPSubmitSMResp resp = emppApi.submitMsg(msg);
				return resp;

			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}

		return null;
	}

	private void submitSMGPSmTask(SmTask st) {

		if (smgpApi != null && smgpApi.isConnected()) {

			try {
				byte msgFmt = 8;
				byte needReport = 1;
				int seq = SequenceGenerator.nextSequence();

				st.setMsgid(seq + "");
				st.setTaskSubmitTime(new Date());
				smTaskManager.updateSmTask(st);

				SMGPSubmitMessage msg = new SMGPSubmitMessage();

				String[] mobiles = new String[] { st.getMobile() };
				msg.setDestTermIdArray(mobiles);
				msg.setSrcTermId(st.getSrcTermId());
				msg.setServiceId("0");
				msg.setMsgFmt(msgFmt);
				String message = "" + st.getContent();
				if (message.length() > 70) {
					message = message.substring(0, 70);
				}
				msg.setBMsgContent(message.getBytes("iso-10646-ucs-2"));
				msg.setSequenceNumber(seq);
				msg.setNeedReport(needReport);

				smgpApi.sendMsg(msg);

			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}

		}

	}

	public void run() {

		while (true) {

			try {
				
				// 对于电信帐号
				// 如果连接断开则不断尝试重连
				while (smgpApi != null && !smgpApi.isConnected()) {
					this.connect();
					Thread.sleep(5000);
				}

				// 从任务列表获取一个任务，以同步方式发送该任务，并获取结果
				SmTask st = tasks.poll();

				// 如果是移动的帐号，则用移动的API发送
				if (sui.getOperator().equals("CMCC") && st != null) {
					// 如果连接断开则不断尝试重连
					while (emppApi != null && !emppApi.isConnected()) {
						this.connect();
						Thread.sleep(5000);
					}

					EMPPSubmitSMResp resp = submitEMPPSmTask(st);
					st.setTaskSubmitTime(new Date());

					if (resp != null) {

						BigInteger msgid = new BigInteger(
								EMPPListener.fiterBinaryZero(resp.getMsgId()));
						st.setMsgid(msgid + "");

						// 根据结果update任务的状态
						int result = resp.getResult();
						st.setStatus(result + "");

						smTaskManager.updateSmTask(st);

					}
				}
				// 如果是电信的帐号，则用电信的API发送
				else if (sui.getOperator().equals("CT") && st != null) {

					submitSMGPSmTask(st);

				}
				// sleep
				Thread.sleep(300);

			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}

		}
	}
}
