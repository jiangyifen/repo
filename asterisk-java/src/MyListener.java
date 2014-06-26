import org.asteriskjava.manager.AbstractManagerEventListener;
import org.asteriskjava.manager.event.*;

;

public class MyListener extends AbstractManagerEventListener {

	protected void handleEvent(HoldEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(UnholdEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(ManagerEvent event) {
		System.out.println("unknow event:" + event);
	}

}
