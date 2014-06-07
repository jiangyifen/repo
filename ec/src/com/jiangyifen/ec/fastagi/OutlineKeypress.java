package com.jiangyifen.ec.fastagi;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;

import com.jiangyifen.ec.beans.PopStatus;
import com.jiangyifen.ec.util.PopData;

public class OutlineKeypress extends BaseAgiScript {

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		//获取用户按键
		String keypress = request.getParameter("keypress");
		String channelName = channel.getName();

		// /////////////
		// 保存按键数据//
		// /////////////
		PopStatus popStatus = PopData.channelAndPop.get(channelName);
		if (popStatus != null) {
			popStatus.getKeyPressed().add(keypress);
			String calleridnum = popStatus.getCalleridNum();
			PopStatus popStatus1 = PopData.channelAndPop.get(calleridnum);
			if (popStatus1 != null) {
				popStatus1.getKeyPressed().add(keypress);
			}
		}

	}

}
