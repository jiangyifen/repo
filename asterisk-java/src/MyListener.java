import org.asteriskjava.manager.AbstractManagerEventListener;
import org.asteriskjava.manager.event.*;

;

public class MyListener extends AbstractManagerEventListener {


	protected void handleEvent(AgentCallbackLoginEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(AgentCallbackLogoffEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(AgentCalledEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(AgentLoginEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(AgentLogoffEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(AlarmClearEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(AlarmEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(CdrEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(ConnectEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(DialEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(DisconnectEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(DndStateEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(ExtensionStatusEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(HoldedCallEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(HoldEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(LogChannelEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(MessageWaitingEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(NewExtenEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(PeerStatusEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(ProtocolIdentifierReceivedEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(QueueEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(RegistryEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(ReloadEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(RenameEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(ShutdownEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(UnholdEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(UserEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(AgentCompleteEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(AgentConnectEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(AgentDumpEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(FaxReceivedEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(NewCallerIdEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(HangupEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(NewChannelEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(NewStateEvent event) {
		System.out.println(event.getState() + " " + event);
	}

	protected void handleEvent(MeetMeJoinEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(MeetMeLeaveEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(MeetMeMuteEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(MeetMeStopTalkingEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(MeetMeTalkingEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(ParkedCallGiveUpEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(ParkedCallTimeOutEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(UnparkedCallEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(QueueMemberAddedEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(QueueMemberPausedEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(QueueMemberRemovedEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(AgentsCompleteEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(AgentsEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(DbGetResponseEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(JoinEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(LeaveEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(LinkEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(OriginateResponseEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(ParkedCallEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(ParkedCallsCompleteEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(PeerEntryEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(PeerlistCompleteEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(QueueEntryEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(QueueMemberStatusEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(QueueParamsEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(QueueStatusCompleteEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(StatusCompleteEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(StatusEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(UnlinkEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(ZapShowChannelsCompleteEvent event) {
		System.out.println(event);
	}

	protected void handleEvent(ZapShowChannelsEvent event) {
		System.out.println(event);
	}
	
	protected void handleEvent(ManagerEvent event) {
		System.out.println("unknow event:"+event);
	}
	

}
