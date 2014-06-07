package com.jiangyifen.ec.customer.olay;

public class OlayGlobalData {
	// 生产环境 vpn
	public static String wdsl = "http://172.31.238.110/Olay/services/MyOlayIVRSrv?wsdl";
	public static String ip = "172.31.238.110";
	
	// 测试环境
	public static String test_wdsl = "http://112.64.163.151/Olay/services/MyOlayIVRSrv?wsdl";
	public static String test_ip = "112.64.163.151";

	
	//IVRPointQuery
	public static String testIVRPointQueryWSDL = "http://112.64.163.151:8080/MyOlay_WS/xfire/MyOlayIVRSrv?wsdl";
	
	public static String IVRPointQueryWSDL = "http://172.31.238.110:8080/MyOlay_WS/xfire/MyOlayIVRSrv?wsdl";
}
