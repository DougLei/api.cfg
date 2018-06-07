package com.king.tooth.sys.entity;

import com.king.tooth.sys.entity.cfg.CfgTabledata;

/**
 * 表接口
 * @author DougLei
 */
public interface ITable {
	
	/**
	 * 获取表对象
	 * <p>包括列</p>
	 * @param dbType 数据库类型  <pre>目前主要是判断，如果是oracle数据库时，要判断表名长度不能超过30个字符</pre>
	 * @return
	 */
	CfgTabledata toCreateTable(String dbType);
	
	/**
	 * 删除表
	 * <p>返回表名</p>
	 * @return
	 */
	String toDropTable();
}
