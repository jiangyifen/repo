package com.jiangyifen.ec.util;

import java.util.concurrent.ConcurrentHashMap;

import com.jiangyifen.ec.beans.PopStatus;

public class PopData {
	
	//这两个map的内容，在answer()时通过 outlineStorage.agi加入，在exten=>h时通过outlineRemove.agi移除
	public static ConcurrentHashMap<String, PopStatus> channelAndPop = new ConcurrentHashMap<String, PopStatus>();
	public static ConcurrentHashMap<String, PopStatus> calleridnumAndPop = new ConcurrentHashMap<String, PopStatus>();
	
	//这个容器用于转接（8001将一通呼叫转接给8002）的情况下临时存放popStatus对象
	//以便8002分机在new state event ringing时没有外线号码没有外线channel名的情况下也能找到popStatus对象
	public static ConcurrentHashMap<String, PopStatus> fenjiAndPop = new ConcurrentHashMap<String, PopStatus>();
	
}
