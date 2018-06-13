package com.king.tooth.sys.entity;

import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 表接口
 * @author DougLei
 */
public interface ITable {
	
	/**
	 * 获取表对象
	 * <p>包括列</p>
	 * @return
	 */
	ComTabledata toCreateTable();
	
	/**
	 * 删除表
	 * <p>返回表名</p>
	 * @return
	 */
	String toDropTable();
}
