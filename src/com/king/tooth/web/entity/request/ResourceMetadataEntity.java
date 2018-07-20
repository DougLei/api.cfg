package com.king.tooth.web.entity.request;

/**
 * 资源元数据
 * @author DougLei
 */
public class ResourceMetadataEntity {
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
	/**
	 * 描述
	 */
	private String description;
	
	public ResourceMetadataEntity(String name, String dataType, int length, int precision, int isNullabled, String description) {
		this.name = name;
		this.dataType = dataType;
		this.length = length;
		this.precision = precision;
		this.isNullabled = isNullabled;
		this.description = description;
	}
	public ResourceMetadataEntity() {
	}
	
	public String getName() {
		return name;
	}
	public String getDataType() {
		return dataType;
	}
	public int getLength() {
		return length;
	}
	public int getPrecision() {
		return precision;
	}
	public int getIsNullabled() {
		return isNullabled;
	}
	public String getDescription() {
		return description;
	}
}
