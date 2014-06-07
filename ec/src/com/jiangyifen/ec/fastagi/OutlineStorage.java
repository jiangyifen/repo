package com.jiangyifen.ec.fastagi;

import java.util.regex.Pattern;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;

import com.jiangyifen.ec.beans.PopStatus;
import com.jiangyifen.ec.util.PopData;

public class OutlineStorage extends BaseAgiScript {

//	private final Log logger = LogFactory.getLog(getClass());

//	public static ConcurrentHashMap<String, String> outline = new ConcurrentHashMap<String, String>();
	
	
	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		
		//获取来电号码,来源外线号码,channel名,uniqueid
		String calleridNum = this.getVariable("CALLERID(num)");
		if (calleridNum == null) {
			calleridNum = " ";
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		if (!pattern.matcher(calleridNum).matches()) {
			calleridNum = " ";
		}
		if (calleridNum.length() >= 8) {
			if (!(calleridNum.startsWith("1") || calleridNum.startsWith("0"))) {
				calleridNum = "021" + calleridNum;
			}
		}
		String exten = this.getVariable("EXTEN");
		String channelName = channel.getName();
		String uniqueid = channel.getUniqueId();
		
		///////////////////
		//保存呼叫来源号码//
		///////////////////
//		outline.put(calleridNum, exten);
//		logger.info("put " + calleridNum + ":" + exten);
//
//		Thread t = new RemoveRecord(calleridNum);
//		t.setDaemon(true);
//		t.start();
		
		///////////////
		//保存弹屏数据//
		///////////////
		PopStatus popStatus = new PopStatus();
		popStatus.setCalleridNum(calleridNum);
		popStatus.setUniqueid(uniqueid);
		popStatus.setChannel(channelName);
		popStatus.setOutlineNum(exten);
		
		//这个fastagi在dialplan里调用
		//调用时机为Answer()一个外线呼入的电话后
		//将相关信息放入一个map
		//等到 exten => h时，再调用另一个AGI（OutlineRemove），将channelAndPop里的popstatus移除
		PopData.channelAndPop.put(channelName, popStatus);
		PopData.calleridnumAndPop.put(calleridNum, popStatus);
		
//		System.out.println("put popStatus: ");
//		System.out.println(popStatus.getCalleridNum());
//		System.out.println(popStatus.getChannel());
//		System.out.println(popStatus.getOutlineNum());
//		System.out.println(popStatus.getUniqueid());

	}

}

//class RemoveRecord extends Thread {
//	private final Log logger = LogFactory.getLog(getClass());
//	private String callerid;
//
//	public RemoveRecord(String callerid) {
//		this.callerid = callerid;
//	}
//
//	public void run() {
//		try {
//			sleep(30000);
//			OutlineStorage.outline.remove(callerid);
//			logger.info("remove " + callerid);
//		} catch (InterruptedException e) {
//			logger.error(e.getMessage(), e);
//		}
//	}
//}