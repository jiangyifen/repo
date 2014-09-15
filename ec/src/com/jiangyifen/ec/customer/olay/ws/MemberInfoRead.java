
package com.jiangyifen.ec.customer.olay.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import com.lang.Byte;


/**
 * <p>MemberInfoRead complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="MemberInfoRead">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MFC_expire_date" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MFC_start_date" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SMS_flag" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="accountCategory" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="accountID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="accountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bcCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="birthday" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cityCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="email_flag" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="gender" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="isGroupPurchaserBefore" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="isOffline" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="lastLoginTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastUpdateTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="loginName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mail_flag" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="memberID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mobile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="offlineJoinDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="onlineJoinDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="onlineRecommendFriendName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="optBrand" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="optCompany" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="postcode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="provinceCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="purchased" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="recommendFriendAccount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recommendFriendMobile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="snsAccountInfos" type="{http://web.myolay.www.accentiv.cn}ArrayOfSNSAccountInfo" minOccurs="0"/>
 *         &lt;element name="source_type" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="specialCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="status" type="{http://lang.java}Byte" minOccurs="0"/>
 *         &lt;element name="storeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="suveryInfos" type="{http://web.myolay.www.accentiv.cn}ArrayOfSuveryInfo" minOccurs="0"/>
 *         &lt;element name="tel_flag" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MemberInfoRead", namespace = "http://web.myolay.www.accentiv.cn", propOrder = {
    "mfcExpireDate",
    "mfcStartDate",
    "smsFlag",
    "accountCategory",
    "accountID",
    "accountNumber",
    "address",
    "bcCode",
    "birthday",
    "cityCode",
    "email",
    "emailFlag",
    "gender",
    "isGroupPurchaserBefore",
    "isOffline",
    "lastLoginTime",
    "lastUpdateTime",
    "loginName",
    "mailFlag",
    "memberID",
    "mobile",
    "name",
    "offlineJoinDate",
    "onlineJoinDate",
    "onlineRecommendFriendName",
    "optBrand",
    "optCompany",
    "postcode",
    "provinceCode",
    "purchased",
    "recommendFriendAccount",
    "recommendFriendMobile",
    "snsAccountInfos",
    "sourceType",
    "specialCode",
    "status",
    "storeCode",
    "suveryInfos",
    "telFlag"
})
public class MemberInfoRead {

    @XmlElementRef(name = "MFC_expire_date", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> mfcExpireDate;
    @XmlElementRef(name = "MFC_start_date", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> mfcStartDate;
    @XmlElement(name = "SMS_flag")
    protected Integer smsFlag;
    protected Integer accountCategory;
    protected Integer accountID;
    @XmlElementRef(name = "accountNumber", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> accountNumber;
    @XmlElementRef(name = "address", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> address;
    @XmlElementRef(name = "bcCode", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> bcCode;
    @XmlElementRef(name = "birthday", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> birthday;
    @XmlElementRef(name = "cityCode", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> cityCode;
    @XmlElementRef(name = "email", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> email;
    @XmlElement(name = "email_flag")
    protected Integer emailFlag;
    protected Integer gender;
    protected Integer isGroupPurchaserBefore;
    protected Integer isOffline;
    @XmlElementRef(name = "lastLoginTime", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> lastLoginTime;
    @XmlElementRef(name = "lastUpdateTime", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> lastUpdateTime;
    @XmlElementRef(name = "loginName", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> loginName;
    @XmlElement(name = "mail_flag")
    protected Integer mailFlag;
    @XmlElementRef(name = "memberID", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> memberID;
    @XmlElementRef(name = "mobile", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> mobile;
    @XmlElementRef(name = "name", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> name;
    @XmlElementRef(name = "offlineJoinDate", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> offlineJoinDate;
    @XmlElementRef(name = "onlineJoinDate", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> onlineJoinDate;
    @XmlElementRef(name = "onlineRecommendFriendName", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> onlineRecommendFriendName;
    protected Integer optBrand;
    protected Integer optCompany;
    @XmlElementRef(name = "postcode", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> postcode;
    @XmlElementRef(name = "provinceCode", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> provinceCode;
    protected Integer purchased;
    @XmlElementRef(name = "recommendFriendAccount", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> recommendFriendAccount;
    @XmlElementRef(name = "recommendFriendMobile", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> recommendFriendMobile;
    @XmlElementRef(name = "snsAccountInfos", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<ArrayOfSNSAccountInfo> snsAccountInfos;
    @XmlElement(name = "source_type")
    protected Integer sourceType;
    @XmlElementRef(name = "specialCode", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> specialCode;
    @XmlElementRef(name = "status", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<Byte> status;
    @XmlElementRef(name = "storeCode", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> storeCode;
    @XmlElementRef(name = "suveryInfos", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<ArrayOfSuveryInfo> suveryInfos;
    @XmlElement(name = "tel_flag")
    protected Integer telFlag;

    /**
     * ��ȡmfcExpireDate���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMFCExpireDate() {
        return mfcExpireDate;
    }

    /**
     * ����mfcExpireDate���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMFCExpireDate(JAXBElement<String> value) {
        this.mfcExpireDate = value;
    }

    /**
     * ��ȡmfcStartDate���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMFCStartDate() {
        return mfcStartDate;
    }

    /**
     * ����mfcStartDate���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMFCStartDate(JAXBElement<String> value) {
        this.mfcStartDate = value;
    }

    /**
     * ��ȡsmsFlag���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSMSFlag() {
        return smsFlag;
    }

    /**
     * ����smsFlag���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSMSFlag(Integer value) {
        this.smsFlag = value;
    }

    /**
     * ��ȡaccountCategory���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAccountCategory() {
        return accountCategory;
    }

    /**
     * ����accountCategory���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAccountCategory(Integer value) {
        this.accountCategory = value;
    }

    /**
     * ��ȡaccountID���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAccountID() {
        return accountID;
    }

    /**
     * ����accountID���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAccountID(Integer value) {
        this.accountID = value;
    }

    /**
     * ��ȡaccountNumber���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAccountNumber() {
        return accountNumber;
    }

    /**
     * ����accountNumber���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAccountNumber(JAXBElement<String> value) {
        this.accountNumber = value;
    }

    /**
     * ��ȡaddress���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAddress() {
        return address;
    }

    /**
     * ����address���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAddress(JAXBElement<String> value) {
        this.address = value;
    }

    /**
     * ��ȡbcCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getBcCode() {
        return bcCode;
    }

    /**
     * ����bcCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setBcCode(JAXBElement<String> value) {
        this.bcCode = value;
    }

    /**
     * ��ȡbirthday���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getBirthday() {
        return birthday;
    }

    /**
     * ����birthday���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setBirthday(JAXBElement<String> value) {
        this.birthday = value;
    }

    /**
     * ��ȡcityCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCityCode() {
        return cityCode;
    }

    /**
     * ����cityCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCityCode(JAXBElement<String> value) {
        this.cityCode = value;
    }

    /**
     * ��ȡemail���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEmail() {
        return email;
    }

    /**
     * ����email���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEmail(JAXBElement<String> value) {
        this.email = value;
    }

    /**
     * ��ȡemailFlag���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEmailFlag() {
        return emailFlag;
    }

    /**
     * ����emailFlag���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEmailFlag(Integer value) {
        this.emailFlag = value;
    }

    /**
     * ��ȡgender���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getGender() {
        return gender;
    }

    /**
     * ����gender���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setGender(Integer value) {
        this.gender = value;
    }

    /**
     * ��ȡisGroupPurchaserBefore���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIsGroupPurchaserBefore() {
        return isGroupPurchaserBefore;
    }

    /**
     * ����isGroupPurchaserBefore���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIsGroupPurchaserBefore(Integer value) {
        this.isGroupPurchaserBefore = value;
    }

    /**
     * ��ȡisOffline���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIsOffline() {
        return isOffline;
    }

    /**
     * ����isOffline���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIsOffline(Integer value) {
        this.isOffline = value;
    }

    /**
     * ��ȡlastLoginTime���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * ����lastLoginTime���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLastLoginTime(JAXBElement<String> value) {
        this.lastLoginTime = value;
    }

    /**
     * ��ȡlastUpdateTime���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * ����lastUpdateTime���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLastUpdateTime(JAXBElement<String> value) {
        this.lastUpdateTime = value;
    }

    /**
     * ��ȡloginName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLoginName() {
        return loginName;
    }

    /**
     * ����loginName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLoginName(JAXBElement<String> value) {
        this.loginName = value;
    }

    /**
     * ��ȡmailFlag���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMailFlag() {
        return mailFlag;
    }

    /**
     * ����mailFlag���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMailFlag(Integer value) {
        this.mailFlag = value;
    }

    /**
     * ��ȡmemberID���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMemberID() {
        return memberID;
    }

    /**
     * ����memberID���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMemberID(JAXBElement<String> value) {
        this.memberID = value;
    }

    /**
     * ��ȡmobile���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMobile() {
        return mobile;
    }

    /**
     * ����mobile���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMobile(JAXBElement<String> value) {
        this.mobile = value;
    }

    /**
     * ��ȡname���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getName() {
        return name;
    }

    /**
     * ����name���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setName(JAXBElement<String> value) {
        this.name = value;
    }

    /**
     * ��ȡofflineJoinDate���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOfflineJoinDate() {
        return offlineJoinDate;
    }

    /**
     * ����offlineJoinDate���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOfflineJoinDate(JAXBElement<String> value) {
        this.offlineJoinDate = value;
    }

    /**
     * ��ȡonlineJoinDate���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOnlineJoinDate() {
        return onlineJoinDate;
    }

    /**
     * ����onlineJoinDate���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOnlineJoinDate(JAXBElement<String> value) {
        this.onlineJoinDate = value;
    }

    /**
     * ��ȡonlineRecommendFriendName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOnlineRecommendFriendName() {
        return onlineRecommendFriendName;
    }

    /**
     * ����onlineRecommendFriendName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOnlineRecommendFriendName(JAXBElement<String> value) {
        this.onlineRecommendFriendName = value;
    }

    /**
     * ��ȡoptBrand���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOptBrand() {
        return optBrand;
    }

    /**
     * ����optBrand���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOptBrand(Integer value) {
        this.optBrand = value;
    }

    /**
     * ��ȡoptCompany���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOptCompany() {
        return optCompany;
    }

    /**
     * ����optCompany���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOptCompany(Integer value) {
        this.optCompany = value;
    }

    /**
     * ��ȡpostcode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPostcode() {
        return postcode;
    }

    /**
     * ����postcode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPostcode(JAXBElement<String> value) {
        this.postcode = value;
    }

    /**
     * ��ȡprovinceCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getProvinceCode() {
        return provinceCode;
    }

    /**
     * ����provinceCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setProvinceCode(JAXBElement<String> value) {
        this.provinceCode = value;
    }

    /**
     * ��ȡpurchased���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPurchased() {
        return purchased;
    }

    /**
     * ����purchased���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPurchased(Integer value) {
        this.purchased = value;
    }

    /**
     * ��ȡrecommendFriendAccount���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRecommendFriendAccount() {
        return recommendFriendAccount;
    }

    /**
     * ����recommendFriendAccount���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRecommendFriendAccount(JAXBElement<String> value) {
        this.recommendFriendAccount = value;
    }

    /**
     * ��ȡrecommendFriendMobile���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRecommendFriendMobile() {
        return recommendFriendMobile;
    }

    /**
     * ����recommendFriendMobile���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRecommendFriendMobile(JAXBElement<String> value) {
        this.recommendFriendMobile = value;
    }

    /**
     * ��ȡsnsAccountInfos���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSNSAccountInfo }{@code >}
     *     
     */
    public JAXBElement<ArrayOfSNSAccountInfo> getSnsAccountInfos() {
        return snsAccountInfos;
    }

    /**
     * ����snsAccountInfos���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSNSAccountInfo }{@code >}
     *     
     */
    public void setSnsAccountInfos(JAXBElement<ArrayOfSNSAccountInfo> value) {
        this.snsAccountInfos = value;
    }

    /**
     * ��ȡsourceType���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSourceType() {
        return sourceType;
    }

    /**
     * ����sourceType���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSourceType(Integer value) {
        this.sourceType = value;
    }

    /**
     * ��ȡspecialCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSpecialCode() {
        return specialCode;
    }

    /**
     * ����specialCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSpecialCode(JAXBElement<String> value) {
        this.specialCode = value;
    }

    /**
     * ��ȡstatus���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Byte }{@code >}
     *     
     */
    public JAXBElement<Byte> getStatus() {
        return status;
    }

    /**
     * ����status���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Byte }{@code >}
     *     
     */
    public void setStatus(JAXBElement<Byte> value) {
        this.status = value;
    }

    /**
     * ��ȡstoreCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getStoreCode() {
        return storeCode;
    }

    /**
     * ����storeCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setStoreCode(JAXBElement<String> value) {
        this.storeCode = value;
    }

    /**
     * ��ȡsuveryInfos���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSuveryInfo }{@code >}
     *     
     */
    public JAXBElement<ArrayOfSuveryInfo> getSuveryInfos() {
        return suveryInfos;
    }

    /**
     * ����suveryInfos���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSuveryInfo }{@code >}
     *     
     */
    public void setSuveryInfos(JAXBElement<ArrayOfSuveryInfo> value) {
        this.suveryInfos = value;
    }

    /**
     * ��ȡtelFlag���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTelFlag() {
        return telFlag;
    }

    /**
     * ����telFlag���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTelFlag(Integer value) {
        this.telFlag = value;
    }

}
