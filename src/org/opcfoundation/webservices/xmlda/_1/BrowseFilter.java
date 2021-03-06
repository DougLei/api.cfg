
package org.opcfoundation.webservices.xmlda._1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>browseFilter的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;simpleType name="browseFilter"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="all"/&gt;
 *     &lt;enumeration value="branch"/&gt;
 *     &lt;enumeration value="item"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "browseFilter")
@XmlEnum
public enum BrowseFilter {

    @XmlEnumValue("all")
    ALL("all"),
    @XmlEnumValue("branch")
    BRANCH("branch"),
    @XmlEnumValue("item")
    ITEM("item");
    private final String value;

    BrowseFilter(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BrowseFilter fromValue(String v) {
        for (BrowseFilter c: BrowseFilter.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
