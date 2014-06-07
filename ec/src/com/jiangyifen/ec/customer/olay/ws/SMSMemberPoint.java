
package com.jiangyifen.ec.customer.olay.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SMSMemberPoint complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="SMSMemberPoint">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accountCategory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="availablePoint" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="placeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="totalPoint" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SMSMemberPoint", namespace = "http://bean.ws.myolay.accentiv", propOrder = {
    "accountCategory",
    "availablePoint",
    "city",
    "memberID",
    "name",
    "placeCode",
    "totalPoint"
})
public class SMSMemberPoint {

    @XmlElementRef(name = "accountCategory", namespace = "http://bean.ws.myolay.accentiv", type = JAXBElement.class)
    protected JAXBElement<String> accountCategory;
    protected Integer availablePoint;
    @XmlElementRef(name = "city", namespace = "http://bean.ws.myolay.accentiv", type = JAXBElement.class)
    protected JAXBElement<String> city;
    @XmlElementRef(name = "memberID", namespace = "http://bean.ws.myolay.accentiv", type = JAXBElement.class)
    protected JAXBElement<String> memberID;
    @XmlElementRef(name = "name", namespace = "http://bean.ws.myolay.accentiv", type = JAXBElement.class)
    protected JAXBElement<String> name;
    @XmlElementRef(name = "placeCode", namespace = "http://bean.ws.myolay.accentiv", type = JAXBElement.class)
    protected JAXBElement<String> placeCode;
    protected Integer totalPoint;

    /**
     * ��ȡaccountCategory���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAccountCategory() {
        return accountCategory;
    }

    /**
     * ����accountCategory���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAccountCategory(JAXBElement<String> value) {
        this.accountCategory = value;
    }

    /**
     * ��ȡavailablePoint���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAvailablePoint() {
        return availablePoint;
    }

    /**
     * ����availablePoint���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAvailablePoint(Integer value) {
        this.availablePoint = value;
    }

    /**
     * ��ȡcity���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCity() {
        return city;
    }

    /**
     * ����city���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCity(JAXBElement<String> value) {
        this.city = value;
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
     * ��ȡplaceCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPlaceCode() {
        return placeCode;
    }

    /**
     * ����placeCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPlaceCode(JAXBElement<String> value) {
        this.placeCode = value;
    }

    /**
     * ��ȡtotalPoint���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTotalPoint() {
        return totalPoint;
    }

    /**
     * ����totalPoint���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTotalPoint(Integer value) {
        this.totalPoint = value;
    }

}
