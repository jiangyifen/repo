package com.jiangyifen.ec.fastagi;

import java.util.regex.Pattern;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.util.BlackListData;

public class BlackList extends BaseAgiScript {
	private static final Log logger = LogFactory.getLog(BlackList.class);

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		
		String direction = request.getParameter("direction");
		
		String callerIdNum = request.getCallerIdNumber();
		
		if (inBlaskList(callerIdNum, direction)) {
			exec("goto", "hangup|1|1");
		}

	}

	private static boolean inBlaskList(String number, String direction) {

		if (number == null) {
			return false;
		}

		// 过滤干扰产生的振铃，此种振铃来电号码是乱码
		Pattern pattern = Pattern.compile("[0-9]*");
		if (!pattern.matcher(number).matches()) {
			return true;
		}

		// 过滤数据库保存的黑名单
		if (direction.equals("in")) {
			if (BlackListData.incomingBlackList.contains(number)) {
				logger.info("BlackList: " + number
						+ " is in incoming black list.");
				return true;
			}
		}else if(direction.equals("out")){
			if (BlackListData.outgoingBlackList.contains(number)) {
				logger.info("BlackList: " + number
						+ " is in outgoing black list.");
				return true;
			}
		}
		return false;

	}

}