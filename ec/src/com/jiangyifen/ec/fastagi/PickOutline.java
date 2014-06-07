package com.jiangyifen.ec.fastagi;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;

import com.jiangyifen.ec.util.ShareData;

public class PickOutline extends BaseAgiScript {

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		String calleridNum = this.getVariable("CALLERID(num)");
		String outline = getOutline(calleridNum);
		System.out.println("pickoutline: "+calleridNum + " + " + outline);
		this.setVariable("outline", outline);
	}
	
	private String getOutline(String calleridNum){
		String outline = ShareData.extenAndOutline.get(calleridNum);
		if(outline==null){
			outline=ShareData.extenAndOutline.get("default");
			
		}
		if(outline==null){
			outline="100";
		}
		return outline;
	}
}