package com.king.tooth.web.entity.request;

/**
 * 资源元数据信息对象
 * @author DougLei
 */
public class ResourceMetadataInfo {
	/**
	 * 属性名
	 */
	private String name;
	/**
	 * 数据类型
	 */
	private String dataType;
	/**
	 * 长度
	 */
	private int length;
	/**
	 * 数据精度
	 */
	private int precision;
	/**
	 * 是否可为空
	 */
	private int isNullabled;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public int getIsNullabled() {
		return isNullabled;
	}
	public void setIsNullabled(int isNullabled) {
		this.isNullabled = isNullabled;
	}
}
