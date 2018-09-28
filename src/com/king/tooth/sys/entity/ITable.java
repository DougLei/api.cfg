package com.king.tooth.sys.entity;

import java.util.List;

import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 表接口
 * @author DougLei
 */
public interface ITable {
	
	/**
	 * 获取表名
	 * @return
	 */
	String toGetTableName();
	
	/**
	 * 获取create表的对象
	 * <p>包括列</p>
	 * @return
	 */
	ComTabledata toCreateTable();
	
	/**
	 * 获取列信息集合
	 * @return
	 */
	List<ComColumndata> getColumnList();
}
