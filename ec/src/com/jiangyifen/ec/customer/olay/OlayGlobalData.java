package com.jiangyifen.ec.customer.olay;

public class OlayGlobalData {
	// 生产环境 vpn
	public static String wdsl = "http://172.31.238.110/Olay/services/MyOlayIVRSrv?wsdl";
	
	// 测试环境
	public static String test_wdsl = "http://olayuat.crmyymd.com/MyOlay_WS/services/MyOlayIVRSrv?wsdl";

	//IVRPointQuery
	public static String testIVRPointQueryWSDL = "http://olayuat.crmyymd.com/MyOlay_WS/xfire/MyOlayIVRSrv?wsdl";
	
	public static String IVRPointQueryWSDL = "http://172.31.238.110:8080/MyOlay_WS/xfire/MyOlayIVRSrv?wsdl";
}
