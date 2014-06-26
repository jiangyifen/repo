import org.asteriskjava.manager.SendActionCallback;
import org.asteriskjava.manager.response.ManagerResponse;


public class MySendActionCallback implements SendActionCallback {

	public void onResponse(ManagerResponse response) {
		System.out.println("==========    "+response);

	}

}
