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
	 * @return 验证结果，如果为null，标识验证通过
	 */
	public String validNotNullProps();
	
	/**
	 * 解析资源属性
	 * <p>例如:解析ComSqlScript，根据传入的sql脚本，获取sql脚本的类型，参数集合等</p>
	 * @return 解析结果，如果为null，标识解析通过
	 */
	public String analysisResourceProp();
}
