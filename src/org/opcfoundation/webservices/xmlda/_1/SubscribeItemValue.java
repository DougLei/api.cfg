
package org.opcfoundation.webservices.xmlda._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SubscribeItemValue complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="SubscribeItemValue"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ItemValue" type="{http://opcfoundation.org/webservices/XMLDA/1.0/}ItemValue" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="RevisedSamplingRate" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubscribeItemValue", propOrder = {
    "itemValue"
})
public class SubscribeItemValue {

    @XmlElement(name = "ItemValue")
    protected ItemValue itemValue;
    @XmlAttribute(name = "RevisedSamplingRate")
    protected Integer revisedSamplingRate;

    /**
     * 获取itemValue属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ItemValue }
     *     
     */
    public ItemValue getItemValue() {
        return itemValue;
    }

    /**
     * 设置itemValue属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ItemValue }
     *     
     */
    public void setItemValue(ItemValue value) {
        this.itemValue = value;
    }

    /**
     * 获取revisedSamplingRate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRevisedSamplingRate() {
        return revisedSamplingRate;
    }

    /**
     * 设置revisedSamplingRate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRevisedSamplingRate(Integer value) {
        this.revisedSamplingRate = value;
    }

}
