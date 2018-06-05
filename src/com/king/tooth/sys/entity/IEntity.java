package com.king.tooth.sys.entity;

/**
 * 实体接口
 * @author DougLei
 */
public interface IEntity {
	
	/**
	 * 获取实体名
	 * @return
	 */
	public String getEntityName();
	
	/**
	 * 转换为实体(json)字符串
	 * @return
	 */
	public String toEntity();
}
