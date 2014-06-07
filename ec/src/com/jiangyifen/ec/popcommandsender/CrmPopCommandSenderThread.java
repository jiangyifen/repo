package com.jiangyifen.ec.popcommandsender;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jiangyifen.ec.util.Config;

public class CrmPopCommandSenderThread extends Thread {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static String crmCommandSenderEnable = null;
	private static String host = null;
	private static int port = -1;
	private static String commandSenderID = null;

	private Socket conn = null;
	private PrintWriter out = null;
	// private BufferedReader in;

	private String commandLine = null;

	public CrmPopCommandSenderThread() {
		try {
			if (host == null || port == -1 || commandSenderID == null
					|| crmCommandSenderEnable == null) {


				crmCommandSenderEnable = Config.props.getProperty("crm_command_sender_enable");
				host = Config.props.getProperty("crm_server_ip");
				port = Integer.valueOf(Config.props.getProperty("crm_server_port"))
						.intValue();
				commandSenderID = Config.props.getProperty("crm_command_sender_id");

				this.setName(commandSenderID);
				this.setDaemon(true);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	private void connect() {
		try {
			conn = new Socket(host, port);
			out = new PrintWriter(
					new OutputStreamWriter(conn.getOutputStream()));

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	private void disconnect() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	}

	private void send() {

		if (out != null) {
			out.println(commandLine);
			out.flush();
		}

	}

	public void run() {
		connect();
		send();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(),e);
		}

		disconnect();
	}

	public void sendCommand(String command, String[] parameters) {
		if (crmCommandSenderEnable.equals("true")) {
			commandLine = System.currentTimeMillis() + "," + commandSenderID
					+ "," + command;
			for (String para : parameters) {
				commandLine = commandLine + "," + para;
			}
			if(commandLine.charAt(commandLine.length()-1)==','){
				commandLine = commandLine.substring(0,commandLine.length()-1);
			}
			logger.info(commandLine);
			this.start();
		}
	}

}
