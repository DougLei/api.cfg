package com.king.tooth.web.builtin.method.common.util.querycondfunc.entity;

/**
 * 查询函数参数实体接口
 * @author DougLei
 */
public interface IQueryCondFuncEntity {
	/**
	 * 方法名
	 * @return
	 */
	public String getMethodName();
	
	/**
	 * 值数组
	 * @return
	 */
	public Object[] getValues();
	
	/**
	 * 是否取反
	 * @return
	 */
	public boolean isInversion();
	
	/**
	 * 请求的属性名
	 * @return
	 */
	public String getPropName();
}
