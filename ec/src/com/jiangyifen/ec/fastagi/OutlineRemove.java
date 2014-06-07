package com.jiangyifen.ec.fastagi;

import java.util.concurrent.ConcurrentHashMap;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.beans.PopStatus;
import com.jiangyifen.ec.util.PopData;

public class OutlineRemove extends BaseAgiScript {

	private final Log logger = LogFactory.getLog(getClass());

	public static ConcurrentHashMap<String, String> outline = new ConcurrentHashMap<String, String>();
	
	
	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		String channelName = channel.getName();

		//outlineStorage/outlineRemove这一对fastagi在dialplan里调用
		//调用时机为Answer()一个外线呼入的电话后调用outlineStorage将相关信息放入一个map
		//等到 exten => h时，再调用outlineRemove，将channelAndPop里的popstatus移除
		PopStatus popStatus = PopData.channelAndPop.get(channelName);
		if(popStatus==null){
			logger.error("channel " + channelName + ":popstatus is null");
		}else{
			String calleridnum = popStatus.getCalleridNum();
			PopData.calleridnumAndPop.remove(calleridnum);
//			System.out.println("remove popStatus " + channelName);
//			System.out.println(popStatus.getCalleridNum());
//			System.out.println(popStatus.getChannel());
//			System.out.println(popStatus.getOutlineNum());
//			System.out.println(popStatus.getUniqueid());
		}
		
		PopData.channelAndPop.remove(channelName);
		
	}

}
