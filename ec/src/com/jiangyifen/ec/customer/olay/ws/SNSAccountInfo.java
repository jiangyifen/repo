
package com.jiangyifen.ec.customer.olay.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SNSAccountInfo complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="SNSAccountInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bindDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="snsAccount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="snsType" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SNSAccountInfo", namespace = "http://web.myolay.www.accentiv.cn", propOrder = {
    "bindDate",
    "snsAccount",
    "snsType"
})
public class SNSAccountInfo {

    @XmlElementRef(name = "bindDate", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> bindDate;
    @XmlElementRef(name = "snsAccount", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<String> snsAccount;
    @XmlElementRef(name = "snsType", namespace = "http://web.myolay.www.accentiv.cn", type = JAXBElement.class)
    protected JAXBElement<Integer> snsType;

    /**
     * ��ȡbindDate���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getBindDate() {
        return bindDate;
    }

    /**
     * ����bindDate���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setBindDate(JAXBElement<String> value) {
        this.bindDate = value;
    }

    /**
     * ��ȡsnsAccount���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSnsAccount() {
        return snsAccount;
    }

    /**
     * ����snsAccount���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSnsAccount(JAXBElement<String> value) {
        this.snsAccount = value;
    }

    /**
     * ��ȡsnsType���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getSnsType() {
        return snsType;
    }

    /**
     * ����snsType���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setSnsType(JAXBElement<Integer> value) {
        this.snsType = value;
    }

}
