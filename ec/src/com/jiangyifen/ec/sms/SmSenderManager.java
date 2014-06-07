package com.jiangyifen.ec.sms;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.copyright.ECSMKeygen;
import com.jiangyifen.copyright.Scrambler;
import com.jiangyifen.ec.biz.DialTaskItemManager;
import com.jiangyifen.ec.biz.SmTaskManager;
import com.jiangyifen.ec.biz.SmUserInfoManager;
import com.jiangyifen.ec.dao.SmTask;
import com.jiangyifen.ec.dao.SmUserInfo;

public class SmSenderManager extends Thread {

	private int PAGE_SIZE = 100;
	private int MAX_TASK_QUEUE_SIZE = 200;

	private final Log logger = LogFactory.getLog(getClass());

	protected SmTaskManager smTaskManager;
	protected SmUserInfoManager smUserInfoManager;
	protected DialTaskItemManager dialTaskItemManager;

	private HashMap<String, SmSenderWorkThread> workThreadPool = null;

	private Properties keys = null;

	public SmSenderManager() {

		FileInputStream fis = null;
		try {

			keys = new Properties();
			String path = getClass().getClassLoader().getResource("").getPath()
					+ "license.properties";
			logger.info(path);
			fis = new FileInputStream(path);
			keys.load(fis);

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		workThreadPool = new HashMap<String, SmSenderWorkThread>();

		this.setDaemon(true);
		this.setName("SmSenderManager");
		this.start();
	}

	public void setSmTaskManager(SmTaskManager smTaskManager) {
		this.smTaskManager = smTaskManager;
	}

	public void setSmUserInfoManager(SmUserInfoManager smUserInfoManager) {
		this.smUserInfoManager = smUserInfoManager;
	}

	public void setDialTaskItemManager(DialTaskItemManager dialTaskItemManager) {
		this.dialTaskItemManager = dialTaskItemManager;
	}

	private boolean usernameIsValid(String username) {

		String key;
		try {
			key = ECSMKeygen.calculateMD5(username, Scrambler.ECSM);

			if (keys.containsValue(key)) {
				return true;
			} else {
				return false;
			}
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		}
		return false;

	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(10000);
				if (smUserInfoManager == null || smTaskManager == null
						|| dialTaskItemManager == null) {
					continue;
				}
				// 实时加载短信通道帐号
				List<SmUserInfo> allSmUserInfo = smUserInfoManager
						.getSmUserInfos();

				// 为每一个帐号开启一个工作线程
				for (SmUserInfo sui : allSmUserInfo) {

					String accountid = sui.getAccountId();
					String username = sui.getUsername();

					if (usernameIsValid(username)) {

						// 看看这个帐号是不是新加载的，如果是新的，就开启新线程
						SmSenderWorkThread t = workThreadPool.get(accountid);
						if (t == null) {
							t = new SmSenderWorkThread(sui, smTaskManager,
									dialTaskItemManager);
							workThreadPool.put(accountid, t);
						}

						if (t.getTaskQueueSize() <= MAX_TASK_QUEUE_SIZE / 5) {
							List<SmTask> tl = smTaskManager.findByAccountId(
									PAGE_SIZE, 1, accountid);
							for (SmTask task : tl) {
								task.setStatus(SmTask.WAITING);
								smTaskManager.updateSmTask(task);
							}

							if (tl.size() > 0) {
								t.addTasks(tl);
							}
						}

					} else {
						logger.warn("您的企业帐号:[" + username
								+ "]未注册。您将无法使用该企业帐号发送短信。请联系我们获得注册码。");
					}

					Thread.sleep(100);

				}

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

}
