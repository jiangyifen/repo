import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskQueue;
import org.asteriskjava.live.MeetMeRoom;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.live.ManagerCommunicationException;

public class HelloLive {
	private AsteriskServer asteriskServer;

	public HelloLive() {
		asteriskServer = new DefaultAsteriskServer("192.168.6.118", "manager",
				"123456");
		
	}

	public void run() throws ManagerCommunicationException {
		System.out.println(asteriskServer.getVersion());
//		
		
		for (AsteriskChannel asteriskChannel : asteriskServer.getChannels()) {
			System.out.println(asteriskChannel);
		}
		
		for (AsteriskQueue asteriskQueue : asteriskServer.getQueues()) {
			System.out.println(asteriskQueue);
		}
		
		System.out.println(asteriskServer.getMeetMeRoom("8222"));
		for (MeetMeRoom meetMeRoom : asteriskServer.getMeetMeRooms()) {
			System.out.println("==="+meetMeRoom);
			System.out.println(asteriskServer.getMeetMeRoom(meetMeRoom.getRoomNumber()));
		}
		
	}

	public static void main(String[] args) throws Exception {
		HelloLive helloLive = new HelloLive();
		helloLive.run();
	}
}