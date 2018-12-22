package com.king.tooth.sys.entity;

import java.util.List;

import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;

/**
 * 表接口
 * @author DougLei
 */
public interface ITable {
	
	/**
	 * 获取create表的对象
	 * <p>包括列</p>
	 * @return
	 */
	CfgTable toCreateTable();
	
	/**
	 * 获取删除表的信息
	 * <p>即返回表名</p>
	 * @return
	 */
	String toDropTable();
	
	/**
	 * 获取列信息集合
	 * @return
	 */
	List<CfgColumn> getColumnList();
	
	/**
	 * 获取表资源名
	 * @return
	 */
	String getTableResourceName();
}
