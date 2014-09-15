
package com.jiangyifen.ec.customer.olay.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="memberInfo" type="{http://web.myolay.www.accentiv.cn}MemberInfo"/>
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
    "memberInfo"
})
@XmlRootElement(name = "setmemberinfo")
public class Setmemberinfo {

    @XmlElement(required = true, nillable = true)
    protected MemberInfo memberInfo;

    /**
     * 获取memberInfo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link MemberInfo }
     *     
     */
    public MemberInfo getMemberInfo() {
        return memberInfo;
    }

    /**
     * 设置memberInfo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link MemberInfo }
     *     
     */
    public void setMemberInfo(MemberInfo value) {
        this.memberInfo = value;
    }

}
