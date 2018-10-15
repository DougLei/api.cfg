package com.king.tooth.sys.entity.tools.resource;

import java.io.Serializable;

import com.king.tooth.constants.ResourcePropNameConstants;

/**
 * 资源元数据信息对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ResourceMetadataInfo implements Serializable{
	/**
	 * 列名
	 */
	protected String columnName;
	/**
	 * 属性名
	 */
	protected String propName;
	/**
	 * 数据类型
	 */
	protected String dataType;
	/**
	 * 长度
	 */
	protected Integer length;
	/**
	 * 数据精度
	 */
	protected Integer precision;
	/**
	 * 是否唯一
	 */
	protected Integer isUnique;
	/**
	 * 是否可为空
	 */
	protected Integer isNullabled;
	/**
	 * 汉字描述名
	 */
	protected String descName;
	
	public ResourceMetadataInfo() {
	}
	public ResourceMetadataInfo(String propName) {
		this.propName = propName.equalsIgnoreCase("id")?ResourcePropNameConstants.ID:propName;
	}
	public ResourceMetadataInfo(String columnName, String dataType, Integer length, Integer precision, Integer isUnique, Integer isNullabled) {
		this.columnName = columnName;
		this.dataType = dataType;
		this.length = length;
		this.precision = precision==null?0:precision;
		this.isUnique = isUnique==null?0:isUnique;
		this.isNullabled = isNullabled==null?1:isNullabled;
	}
	
	public String getPropName() {
		return propName;
	}
	public void setPropName(String propName) {
		this.propName = propName;
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
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	/**
	 * 验证并解析数据
	 * <p>如果返回的字符串以error:开头，表示验证失败</p>
	 * @param val
	 * @return
	 */
	public Object analyzeData(Object val){
		return null;
	}
}
