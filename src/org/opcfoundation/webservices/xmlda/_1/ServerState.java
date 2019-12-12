
package org.opcfoundation.webservices.xmlda._1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>serverState的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;simpleType name="serverState"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="running"/&gt;
 *     &lt;enumeration value="failed"/&gt;
 *     &lt;enumeration value="noConfig"/&gt;
 *     &lt;enumeration value="suspended"/&gt;
 *     &lt;enumeration value="test"/&gt;
 *     &lt;enumeration value="commFault"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "serverState")
@XmlEnum
public enum ServerState {

    @XmlEnumValue("running")
    RUNNING("running"),
    @XmlEnumValue("failed")
    FAILED("failed"),
    @XmlEnumValue("noConfig")
    NO_CONFIG("noConfig"),
    @XmlEnumValue("suspended")
    SUSPENDED("suspended"),
    @XmlEnumValue("test")
    TEST("test"),
    @XmlEnumValue("commFault")
    COMM_FAULT("commFault");
    private final String value;

    ServerState(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ServerState fromValue(String v) {
        for (ServerState c: ServerState.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
