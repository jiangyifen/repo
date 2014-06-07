
import java.io.IOException;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerConnectionState;

public class MyManager extends Thread {

	private static String ip;
	private static String username;
	private static String password;

	private static ManagerConnectionFactory factory;
	private static ManagerConnection managerConnection;

	public MyManager() throws IOException {
		ip = "192.168.1.241";
		username = "manager";
		password = "123456";

		factory = new ManagerConnectionFactory(ip, username, password);

		// this.setDaemon(true);
		this.start();
	}

	private static void initManagerConnection() {
		System.out.println("init manager connection!");

		try {
			if (managerConnection != null) {
				if (managerConnection.getState() == ManagerConnectionState.CONNECTED
						|| managerConnection.getState() == ManagerConnectionState.RECONNECTING) {
					managerConnection.logoff();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			managerConnection = factory.createManagerConnection();
			managerConnection.addEventListener(new MyEventListener());
			managerConnection.login();
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {

		try {
			initManagerConnection();
			while (true) {
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		try {
			new MyManager();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
