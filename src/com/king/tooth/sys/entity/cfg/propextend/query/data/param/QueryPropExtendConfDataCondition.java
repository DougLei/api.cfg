package com.king.tooth.sys.entity.cfg.propextend.query.data.param;

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
}
