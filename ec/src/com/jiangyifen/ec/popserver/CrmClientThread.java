package com.jiangyifen.ec.popserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.jiangyifen.ec.util.Version;

public class CrmClientThread extends Thread {

	private final Log logger = LogFactory.getLog(this.getClass());

	public volatile boolean stop;

	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	private long lastPackageReceiveTime = System.currentTimeMillis();

	private String crmFenji;
	private String crmUsername;
	private String crmVersion;

	private ConcurrentLinkedQueue<String> commandQueue = new ConcurrentLinkedQueue<String>();

	private Thread sendThread;

	public CrmClientThread(String p_crmFenji, String p_crmUsername,
			String p_crmVersion, Socket p_socket, BufferedReader p_in,
			PrintWriter p_out) {
		this.crmFenji = p_crmFenji;
		this.crmUsername = p_crmUsername;
		this.crmVersion = p_crmVersion;
		this.socket = p_socket;
		this.in = p_in;
		this.out = p_out;

		sendThread = new Thread() {
			public void run() {
//				long timeout = System.currentTimeMillis();

				try {
					while (!stop) {
						
						try {
							if (commandQueue.isEmpty()) {
								sleep(500);
								if((System.currentTimeMillis()-lastPackageReceiveTime)>CrmPopServer.CLIENT_TIMEOUT){
									stop=true;
								}
							} else {
								String line = commandQueue.poll();
								out.println(line);
								out.flush();
//								logger.info(this.getName() + " send: " + line);//记得注销这一行
							}
						} catch (InterruptedException e) {
							logger.error(e.getMessage(), e);
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				} finally {
					logger.info(this.getName() + "stop");
				}
			}
		};
		sendThread.setName("crmOut:" + crmUsername + "@" + crmFenji
				+ "_version:" + crmVersion);
		sendThread.setDaemon(true);
		sendThread.start();

		this.setName("crmIn:" + this.crmUsername + "@" + this.crmFenji
				+ "_version:" + this.crmVersion);
		this.setDaemon(true);
		this.start();
	}

	public void sendMessage(Object msg) {
		this.commandQueue.offer(msg.toString());
	}

	private void dispatch(String line) throws JSONException {
		//记得注销这行
//		logger.info(this.getName() + " receive: " + line);

		JSONObject lineObj = new JSONObject(line);
		String command = (String) lineObj.get("command");

		// 如果是心跳
		if (command.equals("heartbeat")) {
			this.sendMessage(line);
		}
		// 如果是公文
		else if (command.equals("newdoc")) {
			String toUser = (String) lineObj.get("to");
			JSONObject newdocnotify = new JSONObject();
			newdocnotify.put("command", "newdocnotify");

			CrmClientThread clientThread = CrmPopServer.crmUsernameClients
					.get(toUser);
			if (clientThread != null) {
				clientThread.sendMessage(newdocnotify.toString());
			}
		}
	}

	public void run() {
		try {
			JSONObject loginsuccess = new JSONObject();

			loginsuccess.put("command", "loginsuccess").put("serverversion",
					Version.VERSION);
			sendMessage(loginsuccess.toString());

			String line;

			while (!stop && (line = in.readLine()) != null) {
				this.lastPackageReceiveTime = System.currentTimeMillis();
				dispatch(line);
			}
		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				in.close();
				out.close();
				socket.close();

				logger.info(this.getName() + "stop");
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public void setLastPackageReceiveTime(long lastPackageReceiveTime) {
		this.lastPackageReceiveTime = lastPackageReceiveTime;
	}

	public long getLastPackageReceiveTime() {
		return lastPackageReceiveTime;
	}

}
