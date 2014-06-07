
package com.jiangyifen.ec.customer.olay.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="loginName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="counterCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="gifts" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "loginName",
    "counterCode",
    "gifts"
})
@XmlRootElement(name = "createRedemption")
public class CreateRedemption {

    @XmlElement(required = true, nillable = true)
    protected String loginName;
    @XmlElement(required = true, nillable = true)
    protected String counterCode;
    @XmlElement(required = true, nillable = true)
    protected String gifts;

    /**
     * ��ȡloginName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * ����loginName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoginName(String value) {
        this.loginName = value;
    }

    /**
     * ��ȡcounterCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCounterCode() {
        return counterCode;
    }

    /**
     * ����counterCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCounterCode(String value) {
        this.counterCode = value;
    }

    /**
     * ��ȡgifts���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGifts() {
        return gifts;
    }

    /**
     * ����gifts���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGifts(String value) {
        this.gifts = value;
    }

}
