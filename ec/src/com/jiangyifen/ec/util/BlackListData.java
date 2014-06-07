package com.jiangyifen.ec.util;

import java.util.HashSet;
import java.util.List;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.biz.BlackListItemManager;
import com.jiangyifen.ec.dao.BlackListItem;

public class BlackListData extends Thread {

	private static final Log logger = LogFactory.getLog(BlackListData.class);

	public final static String TYPE_INCOMING = "incoming";
	public final static String TYPE_OUTGOING = "outgoing";
	public final static String TYPE_BOTH = "both";

	public static HashSet<String> incomingBlackList = new HashSet<String>();
	public static HashSet<String> outgoingBlackList = new HashSet<String>();

	protected static BlackListItemManager blackListItemManager;

	public BlackListData() {
		this.setName("BlackListData");
		this.setDaemon(true);
		this.start();
	}

	public void run() {
		try {
			sleep(20000);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
		
		initBlackList();
		
		while (true) {

			try {
				sleep(60000);
//				for(String s:incomingBlackList)
//					System.out.println("=="+s);
//				for(String s:outgoingBlackList)
//					System.out.println("++"+s);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private void initBlackList() {
		try {

			List<BlackListItem> incomingBlackListItem = blackListItemManager
					.findAllByType(BlackListData.TYPE_INCOMING,
							BlackListData.TYPE_BOTH);
			List<BlackListItem> outgoingBlackListItem = blackListItemManager
					.findAllByType(BlackListData.TYPE_OUTGOING,
							BlackListData.TYPE_BOTH);

			for (BlackListItem bli : incomingBlackListItem) {
				BlackListData.incomingBlackList.add(bli.getPhoneNumber());
			}

			for (BlackListItem bli : outgoingBlackListItem) {
				BlackListData.outgoingBlackList.add(bli.getPhoneNumber());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void addIncomingBlackListItem(String phoneNumber) {
		BlackListData.incomingBlackList.add(phoneNumber);
		logger.info("BlackListData.incomingBlackList.add(" + phoneNumber + ")");
	}

	public static void addOutgoingBlackListItem(String phoneNumber) {
		BlackListData.outgoingBlackList.add(phoneNumber);
		logger.info("BlackListData.outgoingBlackList.add(" + phoneNumber + ")");
	}

	public static void removeIncomingBlackListItem(String phoneNumber) {
		BlackListData.incomingBlackList.remove(phoneNumber);
		logger.info("BlackListData.incomingBlackList.remove(" + phoneNumber
				+ ");");
	}

	public static void removeOutgoingBlackListItem(String phoneNumber) {
		BlackListData.outgoingBlackList.remove(phoneNumber);
		logger.info("BlackListData.outgoingBlackList.remove(" + phoneNumber
				+ ");");
	}

	public BlackListItemManager getBlackListItemManager() {
		return blackListItemManager;
	}

	public void setBlackListItemManager(
			BlackListItemManager blackListItemManager) {
		BlackListData.blackListItemManager = blackListItemManager;
	}

}
