
package com.jiangyifen.ec.customer.olay.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import com.lang.Byte;
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

    private final static QName _SuveryInfoOther_QNAME = new QName("http://web.myolay.www.accentiv.cn", "other");
    private final static QName _RemoteException_QNAME = new QName("http://IVR.myolay.www.accentiv.cn", "RemoteException");
    private final static QName _SNSAccountInfoBindDate_QNAME = new QName("http://web.myolay.www.accentiv.cn", "bindDate");
    private final static QName _SNSAccountInfoSnsType_QNAME = new QName("http://web.myolay.www.accentiv.cn", "snsType");
    private final static QName _SNSAccountInfoSnsAccount_QNAME = new QName("http://web.myolay.www.accentiv.cn", "snsAccount");
    private final static QName _MemberInfoSuveryInfos_QNAME = new QName("http://web.myolay.www.accentiv.cn", "suveryInfos");
    private final static QName _MemberInfoCityCode_QNAME = new QName("http://web.myolay.www.accentiv.cn", "cityCode");
    private final static QName _MemberInfoMobile_QNAME = new QName("http://web.myolay.www.accentiv.cn", "mobile");
    private final static QName _MemberInfoLoginName_QNAME = new QName("http://web.myolay.www.accentiv.cn", "loginName");
    private final static QName _MemberInfoPostcode_QNAME = new QName("http://web.myolay.www.accentiv.cn", "postcode");
    private final static QName _MemberInfoAccountNumber_QNAME = new QName("http://web.myolay.www.accentiv.cn", "accountNumber");
    private final static QName _MemberInfoSnsAccountInfos_QNAME = new QName("http://web.myolay.www.accentiv.cn", "snsAccountInfos");
    private final static QName _MemberInfoBirthday_QNAME = new QName("http://web.myolay.www.accentiv.cn", "birthday");
    private final static QName _MemberInfoProvinceCode_QNAME = new QName("http://web.myolay.www.accentiv.cn", "provinceCode");
    private final static QName _MemberInfoName_QNAME = new QName("http://web.myolay.www.accentiv.cn", "name");
    private final static QName _MemberInfoEmail_QNAME = new QName("http://web.myolay.www.accentiv.cn", "email");
    private final static QName _MemberInfoAddress_QNAME = new QName("http://web.myolay.www.accentiv.cn", "address");
    private final static QName _PointReturnObjectDesc_QNAME = new QName("http://bean.ws.myolay.accentiv", "desc");
    private final static QName _PointReturnObjectResult_QNAME = new QName("http://bean.ws.myolay.accentiv", "result");
    private final static QName _SMSMemberPointAccountCategory_QNAME = new QName("http://bean.ws.myolay.accentiv", "accountCategory");
    private final static QName _SMSMemberPointName_QNAME = new QName("http://bean.ws.myolay.accentiv", "name");
    private final static QName _SMSMemberPointPlaceCode_QNAME = new QName("http://bean.ws.myolay.accentiv", "placeCode");
    private final static QName _SMSMemberPointCity_QNAME = new QName("http://bean.ws.myolay.accentiv", "city");
    private final static QName _SMSMemberPointMemberID_QNAME = new QName("http://bean.ws.myolay.accentiv", "memberID");
    private final static QName _MemberInfoReadOfflineJoinDate_QNAME = new QName("http://web.myolay.www.accentiv.cn", "offlineJoinDate");
    private final static QName _MemberInfoReadOnlineRecommendFriendName_QNAME = new QName("http://web.myolay.www.accentiv.cn", "onlineRecommendFriendName");
    private final static QName _MemberInfoReadLastUpdateTime_QNAME = new QName("http://web.myolay.www.accentiv.cn", "lastUpdateTime");
    private final static QName _MemberInfoReadBcCode_QNAME = new QName("http://web.myolay.www.accentiv.cn", "bcCode");
    private final static QName _MemberInfoReadLastLoginTime_QNAME = new QName("http://web.myolay.www.accentiv.cn", "lastLoginTime");
    private final static QName _MemberInfoReadMemberID_QNAME = new QName("http://web.myolay.www.accentiv.cn", "memberID");
    private final static QName _MemberInfoReadRecommendFriendAccount_QNAME = new QName("http://web.myolay.www.accentiv.cn", "recommendFriendAccount");
    private final static QName _MemberInfoReadMFCStartDate_QNAME = new QName("http://web.myolay.www.accentiv.cn", "MFC_start_date");
    private final static QName _MemberInfoReadRecommendFriendMobile_QNAME = new QName("http://web.myolay.www.accentiv.cn", "recommendFriendMobile");
    private final static QName _MemberInfoReadStatus_QNAME = new QName("http://web.myolay.www.accentiv.cn", "status");
    private final static QName _MemberInfoReadMFCExpireDate_QNAME = new QName("http://web.myolay.www.accentiv.cn", "MFC_expire_date");
    private final static QName _MemberInfoReadStoreCode_QNAME = new QName("http://web.myolay.www.accentiv.cn", "storeCode");
    private final static QName _MemberInfoReadSpecialCode_QNAME = new QName("http://web.myolay.www.accentiv.cn", "specialCode");
    private final static QName _MemberInfoReadOnlineJoinDate_QNAME = new QName("http://web.myolay.www.accentiv.cn", "onlineJoinDate");
    private final static QName _MemberInfoReturnObjectDesc_QNAME = new QName("http://web.myolay.www.accentiv.cn", "desc");
    private final static QName _MemberInfoReturnObjectResult_QNAME = new QName("http://web.myolay.www.accentiv.cn", "Result");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.jiangyifen.ec.customer.olay.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SNSAccountInfo }
     * 
     */
    public SNSAccountInfo createSNSAccountInfo() {
        return new SNSAccountInfo();
    }

    /**
     * Create an instance of {@link ArrayOfSNSAccountInfo }
     * 
     */
    public ArrayOfSNSAccountInfo createArrayOfSNSAccountInfo() {
        return new ArrayOfSNSAccountInfo();
    }

    /**
     * Create an instance of {@link MemberInfoRead }
     * 
     */
    public MemberInfoRead createMemberInfoRead() {
        return new MemberInfoRead();
    }

    /**
     * Create an instance of {@link ArrayOfSuveryInfo }
     * 
     */
    public ArrayOfSuveryInfo createArrayOfSuveryInfo() {
        return new ArrayOfSuveryInfo();
    }

    /**
     * Create an instance of {@link MemberInfoReturnObject }
     * 
     */
    public MemberInfoReturnObject createMemberInfoReturnObject() {
        return new MemberInfoReturnObject();
    }

    /**
     * Create an instance of {@link MemberInfo }
     * 
     */
    public MemberInfo createMemberInfo() {
        return new MemberInfo();
    }

    /**
     * Create an instance of {@link WebReturnObject }
     * 
     */
    public WebReturnObject createWebReturnObject() {
        return new WebReturnObject();
    }

    /**
     * Create an instance of {@link SuveryInfo }
     * 
     */
    public SuveryInfo createSuveryInfo() {
        return new SuveryInfo();
    }

    /**
     * Create an instance of {@link PointReturnObject }
     * 
     */
    public PointReturnObject createPointReturnObject() {
        return new PointReturnObject();
    }

    /**
     * Create an instance of {@link SMSMemberPoint }
     * 
     */
    public SMSMemberPoint createSMSMemberPoint() {
        return new SMSMemberPoint();
    }

    /**
     * Create an instance of {@link IVRPointQueryResponse }
     * 
     */
    public IVRPointQueryResponse createIVRPointQueryResponse() {
        return new IVRPointQueryResponse();
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
     * Create an instance of {@link ValidateAccountResponse }
     * 
     */
    public ValidateAccountResponse createValidateAccountResponse() {
        return new ValidateAccountResponse();
    }

    /**
     * Create an instance of {@link StartCall }
     * 
     */
    public StartCall createStartCall() {
        return new StartCall();
    }

    /**
     * Create an instance of {@link SetmemberinfoResponse }
     * 
     */
    public SetmemberinfoResponse createSetmemberinfoResponse() {
        return new SetmemberinfoResponse();
    }

    /**
     * Create an instance of {@link EnterVipChannel }
     * 
     */
    public EnterVipChannel createEnterVipChannel() {
        return new EnterVipChannel();
    }

    /**
     * Create an instance of {@link EndCallResponse }
     * 
     */
    public EndCallResponse createEndCallResponse() {
        return new EndCallResponse();
    }

    /**
     * Create an instance of {@link GetmemberinfoResponse }
     * 
     */
    public GetmemberinfoResponse createGetmemberinfoResponse() {
        return new GetmemberinfoResponse();
    }

    /**
     * Create an instance of {@link Setmemberinfo }
     * 
     */
    public Setmemberinfo createSetmemberinfo() {
        return new Setmemberinfo();
    }

    /**
     * Create an instance of {@link SendSMSResponse }
     * 
     */
    public SendSMSResponse createSendSMSResponse() {
        return new SendSMSResponse();
    }

    /**
     * Create an instance of {@link Getmemberinfo }
     * 
     */
    public Getmemberinfo createGetmemberinfo() {
        return new Getmemberinfo();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "other", scope = SuveryInfo.class)
    public JAXBElement<String> createSuveryInfoOther(String value) {
        return new JAXBElement<String>(_SuveryInfoOther_QNAME, String.class, SuveryInfo.class, value);
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
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "bindDate", scope = SNSAccountInfo.class)
    public JAXBElement<String> createSNSAccountInfoBindDate(String value) {
        return new JAXBElement<String>(_SNSAccountInfoBindDate_QNAME, String.class, SNSAccountInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "snsType", scope = SNSAccountInfo.class)
    public JAXBElement<Integer> createSNSAccountInfoSnsType(Integer value) {
        return new JAXBElement<Integer>(_SNSAccountInfoSnsType_QNAME, Integer.class, SNSAccountInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "snsAccount", scope = SNSAccountInfo.class)
    public JAXBElement<String> createSNSAccountInfoSnsAccount(String value) {
        return new JAXBElement<String>(_SNSAccountInfoSnsAccount_QNAME, String.class, SNSAccountInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSuveryInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "suveryInfos", scope = MemberInfo.class)
    public JAXBElement<ArrayOfSuveryInfo> createMemberInfoSuveryInfos(ArrayOfSuveryInfo value) {
        return new JAXBElement<ArrayOfSuveryInfo>(_MemberInfoSuveryInfos_QNAME, ArrayOfSuveryInfo.class, MemberInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "cityCode", scope = MemberInfo.class)
    public JAXBElement<String> createMemberInfoCityCode(String value) {
        return new JAXBElement<String>(_MemberInfoCityCode_QNAME, String.class, MemberInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "mobile", scope = MemberInfo.class)
    public JAXBElement<String> createMemberInfoMobile(String value) {
        return new JAXBElement<String>(_MemberInfoMobile_QNAME, String.class, MemberInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "loginName", scope = MemberInfo.class)
    public JAXBElement<String> createMemberInfoLoginName(String value) {
        return new JAXBElement<String>(_MemberInfoLoginName_QNAME, String.class, MemberInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "postcode", scope = MemberInfo.class)
    public JAXBElement<String> createMemberInfoPostcode(String value) {
        return new JAXBElement<String>(_MemberInfoPostcode_QNAME, String.class, MemberInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "accountNumber", scope = MemberInfo.class)
    public JAXBElement<String> createMemberInfoAccountNumber(String value) {
        return new JAXBElement<String>(_MemberInfoAccountNumber_QNAME, String.class, MemberInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSNSAccountInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "snsAccountInfos", scope = MemberInfo.class)
    public JAXBElement<ArrayOfSNSAccountInfo> createMemberInfoSnsAccountInfos(ArrayOfSNSAccountInfo value) {
        return new JAXBElement<ArrayOfSNSAccountInfo>(_MemberInfoSnsAccountInfos_QNAME, ArrayOfSNSAccountInfo.class, MemberInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "birthday", scope = MemberInfo.class)
    public JAXBElement<String> createMemberInfoBirthday(String value) {
        return new JAXBElement<String>(_MemberInfoBirthday_QNAME, String.class, MemberInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "provinceCode", scope = MemberInfo.class)
    public JAXBElement<String> createMemberInfoProvinceCode(String value) {
        return new JAXBElement<String>(_MemberInfoProvinceCode_QNAME, String.class, MemberInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "name", scope = MemberInfo.class)
    public JAXBElement<String> createMemberInfoName(String value) {
        return new JAXBElement<String>(_MemberInfoName_QNAME, String.class, MemberInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "email", scope = MemberInfo.class)
    public JAXBElement<String> createMemberInfoEmail(String value) {
        return new JAXBElement<String>(_MemberInfoEmail_QNAME, String.class, MemberInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "address", scope = MemberInfo.class)
    public JAXBElement<String> createMemberInfoAddress(String value) {
        return new JAXBElement<String>(_MemberInfoAddress_QNAME, String.class, MemberInfo.class, value);
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
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "cityCode", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadCityCode(String value) {
        return new JAXBElement<String>(_MemberInfoCityCode_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "offlineJoinDate", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadOfflineJoinDate(String value) {
        return new JAXBElement<String>(_MemberInfoReadOfflineJoinDate_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "onlineRecommendFriendName", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadOnlineRecommendFriendName(String value) {
        return new JAXBElement<String>(_MemberInfoReadOnlineRecommendFriendName_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "birthday", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadBirthday(String value) {
        return new JAXBElement<String>(_MemberInfoBirthday_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "provinceCode", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadProvinceCode(String value) {
        return new JAXBElement<String>(_MemberInfoProvinceCode_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "lastUpdateTime", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadLastUpdateTime(String value) {
        return new JAXBElement<String>(_MemberInfoReadLastUpdateTime_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "bcCode", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadBcCode(String value) {
        return new JAXBElement<String>(_MemberInfoReadBcCode_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "loginName", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadLoginName(String value) {
        return new JAXBElement<String>(_MemberInfoLoginName_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "lastLoginTime", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadLastLoginTime(String value) {
        return new JAXBElement<String>(_MemberInfoReadLastLoginTime_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "memberID", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadMemberID(String value) {
        return new JAXBElement<String>(_MemberInfoReadMemberID_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "accountNumber", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadAccountNumber(String value) {
        return new JAXBElement<String>(_MemberInfoAccountNumber_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "recommendFriendAccount", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadRecommendFriendAccount(String value) {
        return new JAXBElement<String>(_MemberInfoReadRecommendFriendAccount_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSNSAccountInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "snsAccountInfos", scope = MemberInfoRead.class)
    public JAXBElement<ArrayOfSNSAccountInfo> createMemberInfoReadSnsAccountInfos(ArrayOfSNSAccountInfo value) {
        return new JAXBElement<ArrayOfSNSAccountInfo>(_MemberInfoSnsAccountInfos_QNAME, ArrayOfSNSAccountInfo.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "name", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadName(String value) {
        return new JAXBElement<String>(_MemberInfoName_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "MFC_start_date", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadMFCStartDate(String value) {
        return new JAXBElement<String>(_MemberInfoReadMFCStartDate_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSuveryInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "suveryInfos", scope = MemberInfoRead.class)
    public JAXBElement<ArrayOfSuveryInfo> createMemberInfoReadSuveryInfos(ArrayOfSuveryInfo value) {
        return new JAXBElement<ArrayOfSuveryInfo>(_MemberInfoSuveryInfos_QNAME, ArrayOfSuveryInfo.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "recommendFriendMobile", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadRecommendFriendMobile(String value) {
        return new JAXBElement<String>(_MemberInfoReadRecommendFriendMobile_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "postcode", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadPostcode(String value) {
        return new JAXBElement<String>(_MemberInfoPostcode_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "status", scope = MemberInfoRead.class)
    public JAXBElement<Byte> createMemberInfoReadStatus(Byte value) {
        return new JAXBElement<Byte>(_MemberInfoReadStatus_QNAME, Byte.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "MFC_expire_date", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadMFCExpireDate(String value) {
        return new JAXBElement<String>(_MemberInfoReadMFCExpireDate_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "mobile", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadMobile(String value) {
        return new JAXBElement<String>(_MemberInfoMobile_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "storeCode", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadStoreCode(String value) {
        return new JAXBElement<String>(_MemberInfoReadStoreCode_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "specialCode", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadSpecialCode(String value) {
        return new JAXBElement<String>(_MemberInfoReadSpecialCode_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "email", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadEmail(String value) {
        return new JAXBElement<String>(_MemberInfoEmail_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "address", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadAddress(String value) {
        return new JAXBElement<String>(_MemberInfoAddress_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "onlineJoinDate", scope = MemberInfoRead.class)
    public JAXBElement<String> createMemberInfoReadOnlineJoinDate(String value) {
        return new JAXBElement<String>(_MemberInfoReadOnlineJoinDate_QNAME, String.class, MemberInfoRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "desc", scope = MemberInfoReturnObject.class)
    public JAXBElement<String> createMemberInfoReturnObjectDesc(String value) {
        return new JAXBElement<String>(_MemberInfoReturnObjectDesc_QNAME, String.class, MemberInfoReturnObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MemberInfoRead }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "Result", scope = MemberInfoReturnObject.class)
    public JAXBElement<MemberInfoRead> createMemberInfoReturnObjectResult(MemberInfoRead value) {
        return new JAXBElement<MemberInfoRead>(_MemberInfoReturnObjectResult_QNAME, MemberInfoRead.class, MemberInfoReturnObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "desc", scope = WebReturnObject.class)
    public JAXBElement<String> createWebReturnObjectDesc(String value) {
        return new JAXBElement<String>(_MemberInfoReturnObjectDesc_QNAME, String.class, WebReturnObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.myolay.www.accentiv.cn", name = "Result", scope = WebReturnObject.class)
    public JAXBElement<String> createWebReturnObjectResult(String value) {
        return new JAXBElement<String>(_MemberInfoReturnObjectResult_QNAME, String.class, WebReturnObject.class, value);
    }

}
