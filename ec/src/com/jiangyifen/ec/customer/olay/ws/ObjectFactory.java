
package com.jiangyifen.ec.customer.olay.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import com.rmi.RemoteException;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.jiangyifen.ec.customer.olay.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RemoteException_QNAME = new QName("http://IVR.myolay.www.accentiv.cn", "RemoteException");
    private final static QName _SMSMemberPointAccountCategory_QNAME = new QName("http://bean.ws.myolay.accentiv", "accountCategory");
    private final static QName _SMSMemberPointName_QNAME = new QName("http://bean.ws.myolay.accentiv", "name");
    private final static QName _SMSMemberPointPlaceCode_QNAME = new QName("http://bean.ws.myolay.accentiv", "placeCode");
    private final static QName _SMSMemberPointCity_QNAME = new QName("http://bean.ws.myolay.accentiv", "city");
    private final static QName _SMSMemberPointMemberID_QNAME = new QName("http://bean.ws.myolay.accentiv", "memberID");
    private final static QName _PointReturnObjectDesc_QNAME = new QName("http://bean.ws.myolay.accentiv", "desc");
    private final static QName _PointReturnObjectResult_QNAME = new QName("http://bean.ws.myolay.accentiv", "result");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.jiangyifen.ec.customer.olay.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IVRPointQueryResponse }
     * 
     */
    public IVRPointQueryResponse createIVRPointQueryResponse() {
        return new IVRPointQueryResponse();
    }

    /**
     * Create an instance of {@link PointReturnObject }
     * 
     */
    public PointReturnObject createPointReturnObject() {
        return new PointReturnObject();
    }

    /**
     * Create an instance of {@link EndCall }
     * 
     */
    public EndCall createEndCall() {
        return new EndCall();
    }

    /**
     * Create an instance of {@link SendSMS }
     * 
     */
    public SendSMS createSendSMS() {
        return new SendSMS();
    }

    /**
     * Create an instance of {@link StartCall }
     * 
     */
    public StartCall createStartCall() {
        return new StartCall();
    }

    /**
     * Create an instance of {@link ValidateAccountResponse }
     * 
     */
    public ValidateAccountResponse createValidateAccountResponse() {
        return new ValidateAccountResponse();
    }

    /**
     * Create an instance of {@link EndCallResponse }
     * 
     */
    public EndCallResponse createEndCallResponse() {
        return new EndCallResponse();
    }

    /**
     * Create an instance of {@link EnterVipChannel }
     * 
     */
    public EnterVipChannel createEnterVipChannel() {
        return new EnterVipChannel();
    }

    /**
     * Create an instance of {@link SendSMSResponse }
     * 
     */
    public SendSMSResponse createSendSMSResponse() {
        return new SendSMSResponse();
    }

    /**
     * Create an instance of {@link ValidateAccount }
     * 
     */
    public ValidateAccount createValidateAccount() {
        return new ValidateAccount();
    }

    /**
     * Create an instance of {@link CreateRedemption }
     * 
     */
    public CreateRedemption createCreateRedemption() {
        return new CreateRedemption();
    }

    /**
     * Create an instance of {@link EnterVipChannelResponse }
     * 
     */
    public EnterVipChannelResponse createEnterVipChannelResponse() {
        return new EnterVipChannelResponse();
    }

    /**
     * Create an instance of {@link StartCallResponse }
     * 
     */
    public StartCallResponse createStartCallResponse() {
        return new StartCallResponse();
    }

    /**
     * Create an instance of {@link IVRPointQuery }
     * 
     */
    public IVRPointQuery createIVRPointQuery() {
        return new IVRPointQuery();
    }

    /**
     * Create an instance of {@link CreateRedemptionResponse }
     * 
     */
    public CreateRedemptionResponse createCreateRedemptionResponse() {
        return new CreateRedemptionResponse();
    }

    /**
     * Create an instance of {@link SMSMemberPoint }
     * 
     */
    public SMSMemberPoint createSMSMemberPoint() {
        return new SMSMemberPoint();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://IVR.myolay.www.accentiv.cn", name = "RemoteException")
    public JAXBElement<RemoteException> createRemoteException(RemoteException value) {
        return new JAXBElement<RemoteException>(_RemoteException_QNAME, RemoteException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.ws.myolay.accentiv", name = "accountCategory", scope = SMSMemberPoint.class)
    public JAXBElement<String> createSMSMemberPointAccountCategory(String value) {
        return new JAXBElement<String>(_SMSMemberPointAccountCategory_QNAME, String.class, SMSMemberPoint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.ws.myolay.accentiv", name = "name", scope = SMSMemberPoint.class)
    public JAXBElement<String> createSMSMemberPointName(String value) {
        return new JAXBElement<String>(_SMSMemberPointName_QNAME, String.class, SMSMemberPoint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.ws.myolay.accentiv", name = "placeCode", scope = SMSMemberPoint.class)
    public JAXBElement<String> createSMSMemberPointPlaceCode(String value) {
        return new JAXBElement<String>(_SMSMemberPointPlaceCode_QNAME, String.class, SMSMemberPoint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.ws.myolay.accentiv", name = "city", scope = SMSMemberPoint.class)
    public JAXBElement<String> createSMSMemberPointCity(String value) {
        return new JAXBElement<String>(_SMSMemberPointCity_QNAME, String.class, SMSMemberPoint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.ws.myolay.accentiv", name = "memberID", scope = SMSMemberPoint.class)
    public JAXBElement<String> createSMSMemberPointMemberID(String value) {
        return new JAXBElement<String>(_SMSMemberPointMemberID_QNAME, String.class, SMSMemberPoint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.ws.myolay.accentiv", name = "desc", scope = PointReturnObject.class)
    public JAXBElement<String> createPointReturnObjectDesc(String value) {
        return new JAXBElement<String>(_PointReturnObjectDesc_QNAME, String.class, PointReturnObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SMSMemberPoint }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.ws.myolay.accentiv", name = "result", scope = PointReturnObject.class)
    public JAXBElement<SMSMemberPoint> createPointReturnObjectResult(SMSMemberPoint value) {
        return new JAXBElement<SMSMemberPoint>(_PointReturnObjectResult_QNAME, SMSMemberPoint.class, PointReturnObject.class, value);
    }

}
