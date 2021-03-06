
package org.opcfoundation.webservices.xmlda._1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>limitBits的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;simpleType name="limitBits"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="none"/&gt;
 *     &lt;enumeration value="low"/&gt;
 *     &lt;enumeration value="high"/&gt;
 *     &lt;enumeration value="constant"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "limitBits")
@XmlEnum
public enum LimitBits {

    @XmlEnumValue("none")
    NONE("none"),
    @XmlEnumValue("low")
    LOW("low"),
    @XmlEnumValue("high")
    HIGH("high"),
    @XmlEnumValue("constant")
    CONSTANT("constant");
    private final String value;

    LimitBits(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LimitBits fromValue(String v) {
        for (LimitBits c: LimitBits.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
