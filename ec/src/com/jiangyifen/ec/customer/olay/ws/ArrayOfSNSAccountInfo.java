
package com.jiangyifen.ec.customer.olay.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfSNSAccountInfo complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfSNSAccountInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SNSAccountInfo" type="{http://web.myolay.www.accentiv.cn}SNSAccountInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfSNSAccountInfo", namespace = "http://web.myolay.www.accentiv.cn", propOrder = {
    "snsAccountInfo"
})
public class ArrayOfSNSAccountInfo {

    @XmlElement(name = "SNSAccountInfo", nillable = true)
    protected List<SNSAccountInfo> snsAccountInfo;

    /**
     * Gets the value of the snsAccountInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the snsAccountInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSNSAccountInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SNSAccountInfo }
     * 
     * 
     */
    public List<SNSAccountInfo> getSNSAccountInfo() {
        if (snsAccountInfo == null) {
            snsAccountInfo = new ArrayList<SNSAccountInfo>();
        }
        return this.snsAccountInfo;
    }

}
