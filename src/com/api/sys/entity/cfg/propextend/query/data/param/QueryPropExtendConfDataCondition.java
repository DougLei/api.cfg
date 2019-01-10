package com.api.sys.entity.cfg.propextend.query.data.param;

import com.api.constants.DataTypeConstants;
import com.api.util.StrUtils;

/**
 * 查询属性扩展配置的对应数据列表用条件
 * @author DougLei
 */
public class QueryPropExtendConfDataCondition {
	/**
	 * 属性名
	 */
	private String propName;
	/**
	 * 值
	 */
	private String value;
	/**
	 * 数据类型
	 */
	private String dataType;
	
	public String getPropName() {
		return propName;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDataType() {
		if(StrUtils.isEmpty(dataType)){
			dataType = DataTypeConstants.STRING;
		}
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
}
