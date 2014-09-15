
package com.jiangyifen.ec.customer.olay.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfSuveryInfo complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfSuveryInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SuveryInfo" type="{http://web.myolay.www.accentiv.cn}SuveryInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfSuveryInfo", namespace = "http://web.myolay.www.accentiv.cn", propOrder = {
    "suveryInfo"
})
public class ArrayOfSuveryInfo {

    @XmlElement(name = "SuveryInfo", nillable = true)
    protected List<SuveryInfo> suveryInfo;

    /**
     * Gets the value of the suveryInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the suveryInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSuveryInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SuveryInfo }
     * 
     * 
     */
    public List<SuveryInfo> getSuveryInfo() {
        if (suveryInfo == null) {
            suveryInfo = new ArrayList<SuveryInfo>();
        }
        return this.suveryInfo;
    }

}
