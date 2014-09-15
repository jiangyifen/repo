
package com.jiangyifen.ec.customer.olay.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>MemberInfo complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="MemberInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SMS_flag" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="accountID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="accountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="birthday" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cityCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="email_flag" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="gender" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="loginName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mail_flag" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="mobile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="optBrand" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="optCompany" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="postcode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="provinceCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="snsAccountInfos" type="{http://web.myolay.www.accentiv.cn}ArrayOfSNSAccountInfo" minOccurs="0"/>
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
@XmlType(name = "MemberInfo", namespace = "http://web.myolay.www.accentiv.cn", propOrder = {
    "smsFlag",
    "accountID",
    "accountNumber",
    "address",
    "birthday",
    "cityCode",
    "email",
    "emailFlag",
    "gender",
    "loginName",
    "mailFlag",
    "mobile",
    "name",
    "optBrand",
    "optCompany",
    "postcode",
    "provinceCode",
    "snsAccountInfos",
    "suveryInfos",
    "telFlag"
})
public class MemberInfo {

    @XmlElement(name = "SMS_flag")
    protected Integer smsFlag;
    protected Integer accountID;
    @XmlElementRef(name = "accountNumber", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> accountNumber;
    @XmlElementRef(name = "address", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> address;
    @XmlElementRef(name = "birthday", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> birthday;
    @XmlElementRef(name = "cityCode", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> cityCode;
    @XmlElementRef(name = "email", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> email;
    @XmlElement(name = "email_flag")
    protected Integer emailFlag;
    protected Integer gender;
    @XmlElementRef(name = "loginName", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> loginName;
    @XmlElement(name = "mail_flag")
    protected Integer mailFlag;
    @XmlElementRef(name = "mobile", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> mobile;
    @XmlElementRef(name = "name", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> name;
    protected Integer optBrand;
    protected Integer optCompany;
    @XmlElementRef(name = "postcode", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> postcode;
    @XmlElementRef(name = "provinceCode", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> provinceCode;
    @XmlElementRef(name = "snsAccountInfos", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<ArrayOfSNSAccountInfo> snsAccountInfos;
    @XmlElementRef(name = "suveryInfos", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<ArrayOfSuveryInfo> suveryInfos;
    @XmlElement(name = "tel_flag")
    protected Integer telFlag;

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
