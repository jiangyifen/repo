import org.asteriskjava.manager.AbstractManagerEventListener;
import org.asteriskjava.manager.event.NewStateEvent;

import com.jiangyifen.ec.beans.PopStatus;
import com.jiangyifen.ec.util.PopData;
import com.jiangyifen.ec.util.ShareData;

public class MyEventListener extends AbstractManagerEventListener {

	// private final Logger logger = LoggerFactory.getLogger(getClass());

	// 实现弹屏
	protected void handleEvent(NewStateEvent event) {
		// System.out.println(event);
		if (event.getState().equals("Ringing")) {

			String to = event.getChannel().substring(
					event.getChannel().indexOf('/') + 1,
					event.getChannel().indexOf('-'));

			String from = event.getCallerIdNum();
			System.out.println("=========================");
			System.out.println(event.toString());
			System.out.println("to=" + to);
			System.out.println("from=" + from);
			System.out.println("=========================");

			// 外线打给分机的情况（呼入）
			if (!ShareData.allSipName.contains(from)
					&& ShareData.allSipName.contains(to)) {
				System.out.println("外线打给分机的情况（呼入）");

			}
			// 分机打给外线的情况（呼出）
			else if (ShareData.allSipName.contains(from)
					&& !ShareData.allSipName.contains(to)) {
				System.out.println("分机打给外线的情况（呼出）");

			}
			// 转接以及分机互打的情况（内部呼叫）
			else if (ShareData.allSipName.contains(from)
					&& ShareData.allSipName.contains(to)) {

				System.out.println("转接以及分机互打的情况（内部呼叫）");

				// 要检查是分机叫分机，还是分机将一通外来呼叫转接给另一分机。
				// 如果是分机叫分机，就什么都不做。
				// 如果是转接的情况，就需要弹屏。
				// 如何检查？
				// 在channelRedirectActon里，除了发出redirectAction之外，还会以
				// 目标分机号为key，popStatus对象为value。
				// 目标分机号也就是event中的channel的号码部分，对应to变量。
				PopStatus popStatus = PopData.fenjiAndPop.get(to);
				if (popStatus != null) {

				} else {
					System.out.println("PopStatus of " + from + " is null.");
				}

			} else {
				System.out.println(event.toString());
				System.out.println("to=" + to + ", from=" + from);
			}

		}
	}

}
