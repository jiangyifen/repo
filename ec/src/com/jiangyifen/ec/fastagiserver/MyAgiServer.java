package com.jiangyifen.ec.fastagiserver;

import org.asteriskjava.fastagi.AgiServer;
import org.asteriskjava.fastagi.AgiServerThread;
import org.asteriskjava.fastagi.DefaultAgiServer;

public class MyAgiServer {
	
	public MyAgiServer() {
		AgiServerThread t = new AgiServerThread();
		AgiServer s = new DefaultAgiServer();
		t.setAgiServer(s);
		t.setDaemon(true);
		t.startup();
	}
	
	public static void main(String[] args){
		new MyAgiServer();
	}

}
