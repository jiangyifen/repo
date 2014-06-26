import java.io.IOException;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.SendActionCallback;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.action.HangupAction;
import org.asteriskjava.manager.action.QueueStatusAction;
import org.asteriskjava.manager.response.CommandResponse;
import org.asteriskjava.manager.response.ManagerResponse;

@SuppressWarnings("unused")
public class Test1 {
	private ManagerConnection managerConnection;

	public Test1() throws IOException {
		ManagerConnectionFactory factory = new ManagerConnectionFactory(
				"192.168.35.11", "manager", "123456");

		this.managerConnection = factory.createManagerConnection();
		managerConnection.addEventListener(new MyListener());

	}

	public void run() throws IOException, AuthenticationFailedException,
			TimeoutException, InterruptedException {


		// connect to Asterisk and log in

		managerConnection.login();

		HangupAction action = new HangupAction("SIP/8001-00000013");
		ManagerResponse r =  managerConnection.sendAction(action);
		System.out.println(r);

		managerConnection.logoff();
//
//		while (true) {
//
//			Thread.sleep(1000);
//		}
		

	}

	public static void main(String[] args) throws Exception {
		Test1 t1;

		t1 = new Test1();
		t1.run();

	}
}