import java.io.IOException;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.action.ParkedCallsAction;



public class Test extends Thread {
	private ManagerConnection managerConnection;

	public Test() throws IOException {
		ManagerConnectionFactory factory = new ManagerConnectionFactory(
				"10.0.1.22", "manager", "123456");

		this.managerConnection = factory.createManagerConnection();
		managerConnection.addEventListener(new MyListener());
	}

	public void run() {

		try {
			managerConnection.login();
			while (true) {
				sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {
		Test helloManager = new Test();
		helloManager.start();

	}
}