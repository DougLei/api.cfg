package com.king.tooth.sys.entity;

/**
 * 实体属性解析器接口
 * @author DougLei
 */
public interface IEntityPropAnalysis {
	
	/**
	 * 验证所有不能为空的属性
	 * <p>是否为空，如果空则抛出异常</p>
	 * <p>主要针对analysisResourceProp()用到的属性进行验证</p>
	 */
	public void validNotNullProps();
	
	/**
	 * 解析资源属性
	 * <p>例如:解析ComSqlScript，根据传入的sql脚本，获取sql脚本的类型，参数集合等</p>
	 */
	public void analysisResourceProp();
}
