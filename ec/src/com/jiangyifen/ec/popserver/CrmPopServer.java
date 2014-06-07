package com.jiangyifen.ec.popserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.util.Config;

public class CrmPopServer extends Thread {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private int port = 18080;
	private ServerSocket server;
	private Socket socket;
	private String secret;
	public static final long CLIENT_TIMEOUT = 30000;

	public static ConcurrentHashMap<String, CrmClientThread> crmFenjiClients =new ConcurrentHashMap<String, CrmClientThread>();
	public static ConcurrentHashMap<String, CrmClientThread> crmUsernameClients = new ConcurrentHashMap<String, CrmClientThread>();

	private Thread watchDog;
	
	public CrmPopServer() {

		this.setName("CrmPopServer");
		this.setDaemon(true);
		this.port = Integer.valueOf(Config.props
				.getProperty("crm_pop_server_port"));
		this.secret = Config.props.getProperty("crm_pop_server_secret");

		this.start();
		
		watchDog = new Thread() {
			
			public void run() {
				
				
				Log logger = LogFactory.getLog(this.getClass());
				while (true) {
					
					try {
						sleep(1000);
						logger.debug(crmFenjiClients.size());
						logger.debug(crmUsernameClients.size());
						for (String key : crmFenjiClients.keySet()) {
							CrmClientThread clientThread = crmFenjiClients.get(key);
							if(clientThread!=null){
								long time = System.currentTimeMillis() - clientThread.getLastPackageReceiveTime();
								if(time>CLIENT_TIMEOUT){
									crmFenjiClients.get(key).stop = true;
									crmFenjiClients.remove(key);
									logger.info(this.getName() + clientThread.getName() + " time out.");
								}
							}
						}

						for (String key : crmUsernameClients.keySet()) {
							CrmClientThread clientThread = crmUsernameClients.get(key);
							if(clientThread!=null){
								long time = System.currentTimeMillis() - clientThread.getLastPackageReceiveTime();
								if(time>CLIENT_TIMEOUT){
									crmUsernameClients.get(key).stop = true;
									crmUsernameClients.remove(key);
									logger.info(this.getName() + clientThread.getName() + " time out.");
								}
							}
						}

					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		};
		watchDog.setName("CRM client heartbeat watch dog");
		watchDog.setDaemon(true);
		watchDog.start();
	}

	public void run() {

		BufferedReader in = null;
		PrintWriter out = null;

		while (true) {
			try {
				server = new ServerSocket(port);
				
				logger.info("crm popserver liston on " + port);
				while (true) {
					try {
						socket = server.accept();

						logger.info("-----------Accept client:----------");
						logger.info("getRemoteSocketAddress: "+ socket.getRemoteSocketAddress());
						logger.info("getLocalSocketAddress: " + socket.getLocalSocketAddress());
						logger.info("-----------------------------------");

						InputStream is = this.socket.getInputStream();
						InputStreamReader isr = new InputStreamReader(is);
						in = new BufferedReader(isr);

						OutputStream os = socket.getOutputStream();
						OutputStreamWriter osw = new OutputStreamWriter(os);
						out = new PrintWriter(osw);

						String crmFenji = "";
						String crmUsername = "";
						String crmVersion = "";
						String firstLine = in.readLine();
						if (firstLine != null) {
							logger.info(firstLine);
							// 解析firstLine
							
							JSONObject firstLineJSONObj = new JSONObject(
									firstLine);
							String clientSecret = (String) firstLineJSONObj
									.get("secret");
							if (clientSecret.equals(secret)) {
								crmFenji = (String) firstLineJSONObj
										.get("crmfenji");
								crmUsername = (String) firstLineJSONObj
										.get("crmusername");
								crmVersion = (String) firstLineJSONObj
										.get("crmversion");

								CrmClientThread clientThread = new CrmClientThread(
										crmFenji, crmUsername, crmVersion,
										socket, in, out);

								crmFenjiClients.put(crmFenji, clientThread);
								crmUsernameClients.put(crmUsername,
										clientThread);
								
								
//								logger.info("===============================");
//								for(String key: crmFenjiClients.keySet()){
//									logger.info(key + ":" + crmFenjiClients.get(key));
//								}
//								logger.info("-------------------------------");
//								for(String key: crmUsernameClients.keySet()){
//									logger.info(key + ":" + crmUsernameClients.get(key));
//								}
//								logger.info("===============================");
								
								
							} else {
								logger.warn("Wrong secret["+clientSecret+"]!");
								if (in != null)
									in.close();
								if (out != null)
									out.close();
								if (socket != null) {
									socket.close();
								}
							}
						} else {
							logger.warn("firstLine==null");
							socket.close();
						}

					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						socket.close();
						try {
							if (in != null)
								in.close();
							if (out != null)
								out.close();
							if (socket != null) {
								socket.close();
							}
						} catch (IOException ee) {
							logger.error(e.getMessage(), ee);
						}
					}
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			} finally {
				try {
					server.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}

			}
		}
	}
}
