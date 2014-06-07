
package com.jiangyifen.ec.customer.olay.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>PointReturnObject complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="PointReturnObject">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="desc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="exitCode" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="result" type="{http://bean.ws.myolay.accentiv}SMSMemberPoint" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PointReturnObject", namespace = "http://bean.ws.myolay.accentiv", propOrder = {
    "desc",
    "exitCode",
    "result"
})
public class PointReturnObject {

    @XmlElementRef(name = "desc", namespace = "http://bean.ws.myolay.accentiv", type = JAXBElement.class, required = false)
    protected JAXBElement<String> desc;
    protected Integer exitCode;
    @XmlElementRef(name = "result", namespace = "http://bean.ws.myolay.accentiv", type = JAXBElement.class, required = false)
    protected JAXBElement<SMSMemberPoint> result;

    /**
     * 获取desc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDesc() {
        return desc;
    }

    /**
     * 设置desc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDesc(JAXBElement<String> value) {
        this.desc = value;
    }

    /**
     * 获取exitCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getExitCode() {
        return exitCode;
    }

    /**
     * 设置exitCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setExitCode(Integer value) {
        this.exitCode = value;
    }

    /**
     * 获取result属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link SMSMemberPoint }{@code >}
     *     
     */
    public JAXBElement<SMSMemberPoint> getResult() {
        return result;
    }

    /**
     * 设置result属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link SMSMemberPoint }{@code >}
     *     
     */
    public void setResult(JAXBElement<SMSMemberPoint> value) {
        this.result = value;
    }

}
