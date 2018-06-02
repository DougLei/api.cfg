package org.hibernate.internal;

/**
 * hibernate 配置的映射文件的属性元数据
 * @author DougLei
 */
public class HbmConfPropMetadata {
	/**
	 * 属性名
	 */
	private String propName;
	/**
	 * 属性的数据类型
	 * 目前只有string和date
	 */
	private String propDataType;
	
	
	public HbmConfPropMetadata(String propName, String propDataType) {
		this.propName = propName;
		this.propDataType = propDataType;
	}
	public HbmConfPropMetadata() {
	}
	public String getPropName() {
		return propName;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}
	public String getPropDataType() {
		return propDataType;
	}
	public void setPropDataType(String propDataType) {
		this.propDataType = propDataType;
	}
}
