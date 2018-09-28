package com.king.tooth.sys.entity.other;

import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.util.StrUtils;


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
	/**
	 * 汉字描述名
	 */
	private String descName;
	
	public ResourceMetadataInfo() {
	}
	public ResourceMetadataInfo(String name) {
		this.name = name.equalsIgnoreCase("id")?ResourcePropNameConstants.ID:name;
	}
	public ResourceMetadataInfo(String name, String dataType, Integer length, Integer precision, Integer isUnique, Integer isNullabled, String descName) {
		this.name = name.equalsIgnoreCase("id")?ResourcePropNameConstants.ID:name;
		this.dataType = dataType;
		this.length = length;
		this.precision = precision==null?0:precision;
		this.isUnique = isUnique==null?0:isUnique;
		this.isNullabled = isNullabled==null?1:isNullabled;
		this.descName = StrUtils.isEmpty(descName)?name:descName;
	}
	
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
	public String getDescName() {
		return descName;
	}
	public void setDescName(String descName) {
		this.descName = descName;
	}
}
