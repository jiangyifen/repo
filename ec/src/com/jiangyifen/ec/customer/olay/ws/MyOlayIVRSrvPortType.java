package com.jiangyifen.ec.customer.olay.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.6.1
 * 2014-07-17T11:04:59.559+08:00
 * Generated source version: 2.6.1
 * 
 */
@WebService(targetNamespace = "http://IVR.myolay.www.accentiv.cn", name = "MyOlayIVRSrvPortType")
@XmlSeeAlso({com.lang.ObjectFactory.class, com.rmi.ObjectFactory.class, ObjectFactory.class})
public interface MyOlayIVRSrvPortType {

    @WebResult(name = "out", targetNamespace = "http://IVR.myolay.www.accentiv.cn")
    @RequestWrapper(localName = "IVRPointQuery", targetNamespace = "http://IVR.myolay.www.accentiv.cn", className = "com.jiangyifen.ec.customer.olay.ws.IVRPointQuery")
    @WebMethod(operationName = "IVRPointQuery")
    @ResponseWrapper(localName = "IVRPointQueryResponse", targetNamespace = "http://IVR.myolay.www.accentiv.cn", className = "com.jiangyifen.ec.customer.olay.ws.IVRPointQueryResponse")
    public com.jiangyifen.ec.customer.olay.ws.PointReturnObject ivrPointQuery(
        @WebParam(name = "memberID", targetNamespace = "http://IVR.myolay.www.accentiv.cn")
        java.lang.String memberID
    ) throws RemoteException;

    @WebResult(name = "out", targetNamespace = "http://IVR.myolay.www.accentiv.cn")
    @RequestWrapper(localName = "sendSMS", targetNamespace = "http://IVR.myolay.www.accentiv.cn", className = "com.jiangyifen.ec.customer.olay.ws.SendSMS")
    @WebMethod
    @ResponseWrapper(localName = "sendSMSResponse", targetNamespace = "http://IVR.myolay.www.accentiv.cn", className = "com.jiangyifen.ec.customer.olay.ws.SendSMSResponse")
    public java.lang.String sendSMS(
        @WebParam(name = "mobile", targetNamespace = "http://IVR.myolay.www.accentiv.cn")
        java.lang.String mobile,
        @WebParam(name = "content", targetNamespace = "http://IVR.myolay.www.accentiv.cn")
        java.lang.String content
    ) throws RemoteException;

    @WebResult(name = "out", targetNamespace = "http://IVR.myolay.www.accentiv.cn")
    @RequestWrapper(localName = "createRedemption", targetNamespace = "http://IVR.myolay.www.accentiv.cn", className = "com.jiangyifen.ec.customer.olay.ws.CreateRedemption")
    @WebMethod
    @ResponseWrapper(localName = "createRedemptionResponse", targetNamespace = "http://IVR.myolay.www.accentiv.cn", className = "com.jiangyifen.ec.customer.olay.ws.CreateRedemptionResponse")
    public java.lang.String createRedemption(
        @WebParam(name = "loginName", targetNamespace = "http://IVR.myolay.www.accentiv.cn")
        java.lang.String loginName,
        @WebParam(name = "counterCode", targetNamespace = "http://IVR.myolay.www.accentiv.cn")
        java.lang.String counterCode,
        @WebParam(name = "gifts", targetNamespace = "http://IVR.myolay.www.accentiv.cn")
        java.lang.String gifts
    ) throws RemoteException;

    @WebResult(name = "out", targetNamespace = "http://IVR.myolay.www.accentiv.cn")
    @RequestWrapper(localName = "endCall", targetNamespace = "http://IVR.myolay.www.accentiv.cn", className = "com.jiangyifen.ec.customer.olay.ws.EndCall")
    @WebMethod
    @ResponseWrapper(localName = "endCallResponse", targetNamespace = "http://IVR.myolay.www.accentiv.cn", className = "com.jiangyifen.ec.customer.olay.ws.EndCallResponse")
    public java.lang.String endCall(
        @WebParam(name = "callSessionId", targetNamespace = "http://IVR.myolay.www.accentiv.cn")
        java.lang.String callSessionId
    ) throws RemoteException;

    @WebResult(name = "out", targetNamespace = "http://IVR.myolay.www.accentiv.cn")
    @RequestWrapper(localName = "validateAccount", targetNamespace = "http://IVR.myolay.www.accentiv.cn", className = "com.jiangyifen.ec.customer.olay.ws.ValidateAccount")
    @WebMethod
    @ResponseWrapper(localName = "validateAccountResponse", targetNamespace = "http://IVR.myolay.www.accentiv.cn", className = "com.jiangyifen.ec.customer.olay.ws.ValidateAccountResponse")
    public java.lang.String validateAccount(
        @WebParam(name = "loginName", targetNamespace = "http://IVR.myolay.www.accentiv.cn")
        java.lang.String loginName
    ) throws RemoteException;

    @WebResult(name = "out", targetNamespace = "http://IVR.myolay.www.accentiv.cn")
    @RequestWrapper(localName = "enterVipChannel", targetNamespace = "http://IVR.myolay.www.accentiv.cn", className = "com.jiangyifen.ec.customer.olay.ws.EnterVipChannel")
    @WebMethod
    @ResponseWrapper(localName = "enterVipChannelResponse", targetNamespace = "http://IVR.myolay.www.accentiv.cn", className = "com.jiangyifen.ec.customer.olay.ws.EnterVipChannelResponse")
    public java.lang.String enterVipChannel(
        @WebParam(name = "loginName", targetNamespace = "http://IVR.myolay.www.accentiv.cn")
        java.lang.String loginName
    ) throws RemoteException;

    @WebResult(name = "out", targetNamespace = "http://IVR.myolay.www.accentiv.cn")
    @RequestWrapper(localName = "startCall", targetNamespace = "http://IVR.myolay.www.accentiv.cn", className = "com.jiangyifen.ec.customer.olay.ws.StartCall")
    @WebMethod
    @ResponseWrapper(localName = "startCallResponse", targetNamespace = "http://IVR.myolay.www.accentiv.cn", className = "com.jiangyifen.ec.customer.olay.ws.StartCallResponse")
    public java.lang.String startCall(
        @WebParam(name = "input", targetNamespace = "http://IVR.myolay.www.accentiv.cn")
        java.lang.String input
    ) throws RemoteException;
}
