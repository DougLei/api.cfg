package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * hibernate的hbm内容表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysHibernateHbm extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 关联的数据库主键
	 */
	private String refDatabaseId;
	/**
	 * 关联的表主键
	 */
	private String refTableId;
	/**
	 * hbm内容
	 */
	private String content;

	//-------------------------------------------------------------------------
	
	public SysHibernateHbm() {
	}
	public SysHibernateHbm(ComTabledata table) {
		this.setRefTableId(table.getId());
	}
	
	public String getRefTableId() {
		return refTableId;
	}
	public void setRefTableId(String refTableId) {
		this.refTableId = refTableId;
	}
	public String getRefDatabaseId() {
		return refDatabaseId;
	}
	public void setRefDatabaseId(String refDatabaseId) {
		this.refDatabaseId = refDatabaseId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@JSONField(serialize = false)
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(18);
		
		ComColumndata refDatabaseIdColumn = new ComColumndata("ref_database_id", BuiltinDataType.STRING, 32);
		refDatabaseIdColumn.setName("关联的数据库主键");
		refDatabaseIdColumn.setComments("关联的数据库主键");
		columns.add(refDatabaseIdColumn);
		
		ComColumndata refTableIdColumn = new ComColumndata("ref_table_id", BuiltinDataType.STRING, 32);
		refTableIdColumn.setName("关联的表主键");
		refTableIdColumn.setComments("关联的表主键");
		columns.add(refTableIdColumn);
		
		ComColumndata hbmContentColumn = new ComColumndata("hbm_content", BuiltinDataType.CLOB, 0);
		hbmContentColumn.setName("hbm内容");
		hbmContentColumn.setComments("hbm内容");
		columns.add(hbmContentColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toGetTableName());
		table.setName("hibernate的hbm内容表");
		table.setComments("hibernate的hbm内容表");
		
		table.setColumns(getColumnList());
		return table;
	}
	
	public String toGetTableName() {
		return "SYS_HIBERNATE_HBM";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysHibernateHbm";
	}
}
