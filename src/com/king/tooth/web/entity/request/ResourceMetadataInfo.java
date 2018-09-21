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
	private Integer length;
	/**
	 * 数据精度
	 */
	private Integer precision;
	/**
	 * 是否唯一
	 */
	private Integer isUnique;
	/**
	 * 是否可为空
	 */
	private Integer isNullabled;
	
	
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
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Integer getPrecision() {
		return precision;
	}
	public void setPrecision(Integer precision) {
		this.precision = precision;
	}
	public Integer getIsUnique() {
		return isUnique;
	}
	public void setIsUnique(Integer isUnique) {
		this.isUnique = isUnique;
	}
	public Integer getIsNullabled() {
		return isNullabled;
	}
	public void setIsNullabled(Integer isNullabled) {
		this.isNullabled = isNullabled;
	}
}
