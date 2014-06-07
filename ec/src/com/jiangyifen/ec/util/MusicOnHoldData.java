package com.jiangyifen.ec.util;

import java.util.HashMap;

public class MusicOnHoldData {
	public static HashMap<String, String> mohChannels = new HashMap<String, String>();

	
	
	public static String getMohChannel(String key) {
		synchronized (mohChannels) {
			return mohChannels.get(key);
		}
	}

	public static String putMohChannel(String key, String value) {
		synchronized (mohChannels) {
			return mohChannels.put(key, value);
		}
	}

	public static String removeMohChannel(String key) {
		synchronized (mohChannels) {
			return mohChannels.remove(key);
		}
	}

	
	
}
