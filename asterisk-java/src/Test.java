import java.io.IOException;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.action.ParkedCallsAction;



public class Test extends Thread {
	private ManagerConnection managerConnection;

	public Test() throws IOException {
		ManagerConnectionFactory factory = new ManagerConnectionFactory(
				"192.168.35.11", "manager", "123456");

		this.managerConnection = factory.createManagerConnection();
		managerConnection.addEventListener(new MyListener());
	}

	public void run() {

		try {
			managerConnection.login();
			while (true) {
				sleep(1000);
				System.out.println();
				System.out.println();
				ManagerAction action = new ParkedCallsAction();
				managerConnection.sendAction(action, null);
//				ManagerResponse r =  managerConnection.sendAction(action);
//				System.out.println("JYF:  "+r);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {
		Test helloManager;
		helloManager = new Test();
		helloManager.start();

	}
}