package com.jiangyifen.ec.customer.olay;

public class OlayGlobalData {
	
	
	// 老接口 生产环境（vpn）
	public static String wdsl = "http://172.31.238.110/Olay/services/MyOlayIVRSrv?wsdl";

	// 老接口 测试环境
	public static String test_wdsl = "http://olayuat.crmyymd.com/MyOlay_WS/services/MyOlayIVRSrv?wsdl";

	
	
	
	// 新接口 IVRPointQuery生产环境（vpn）
	public static String IVRPointQueryWSDL = "http://172.31.238.110:8080/MyOlay_WS/xfire/MyOlayIVRSrv?wsdl";

	// 新接口 IVRPointQuery测试环境
	public static String testIVRPointQueryWSDL = "http://olayuat.crmyymd.com/MyOlay_WS/xfire/MyOlayIVRSrv?wsdl";




}
