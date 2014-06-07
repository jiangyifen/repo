package com.jiangyifen.ec.actions;

import java.util.ArrayList;

import org.asteriskjava.manager.event.StatusEvent;

import com.jiangyifen.ec.manager.MyManager;

@SuppressWarnings("unchecked")
public class SystemMonitorAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9056868544312154449L;


	private ArrayList<StatusEvent> statusEvents = null;

	public String execute() throws Exception {
		int i = 0;
		while (!MyManager.statusEventsIsReady && i < 5) {
			i++;
			Thread.sleep(100);
		}
		statusEvents = (ArrayList<StatusEvent>) MyManager.getStatusEvents()
				.clone();

		return SUCCESS;
	}

	public void setStatusEvents(ArrayList<StatusEvent> statusEvents) {
		this.statusEvents = statusEvents;
	}

	public ArrayList<StatusEvent> getStatusEvents() {
		return statusEvents;
	}

}
