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
	 * <p>如果发布到项目中，这个字段必须有值</p>
	 */
	private String refDatabaseId;
	/**
	 * 关联的表主键
	 * <p>如果在被发布到项目中，这个字段有无值均可</p>
	 */
	private String refTableId;
	/**
	 * hbm资源名
	 * <p>即对应的表的资源名</p>
	 */
	private String resourceName;
	/**
	 * hbm内容
	 */
	private String content;

	//-------------------------------------------------------------------------
	
	public SysHibernateHbm() {
	}
	public SysHibernateHbm(ComTabledata table) {
		this.setRefTableId(table.getId());
		this.setResourceName(table.getResourceName());
	}
	
	public String getRefTableId() {
		return refTableId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
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
		refDatabaseIdColumn.setComments("关联的数据库主键：如果发布到项目中，这个字段必须有值");
		columns.add(refDatabaseIdColumn);
		
		ComColumndata refTableIdColumn = new ComColumndata("ref_table_id", BuiltinDataType.STRING, 32);
		refTableIdColumn.setName("关联的表主键");
		refTableIdColumn.setComments("关联的表主键：如果在被发布到项目中，这个字段有无值均可");
		columns.add(refTableIdColumn);
		
		ComColumndata hbmResourceNameColumn = new ComColumndata("hbm_resource_name", BuiltinDataType.STRING, 60);
		hbmResourceNameColumn.setName("hbm资源名");
		hbmResourceNameColumn.setComments("hbm资源名：即对应的表的资源名");
		columns.add(hbmResourceNameColumn);
		
		ComColumndata hbmContentColumn = new ComColumndata("hbm_content", BuiltinDataType.CLOB, 0);
		hbmContentColumn.setName("hbm内容");
		hbmContentColumn.setComments("hbm内容");
		columns.add(hbmContentColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toDropTable());
		table.setName("hibernate的hbm内容表");
		table.setComments("hibernate的hbm内容表");
		
		table.setColumns(getColumnList());
		return table;
	}
	
	public String toDropTable() {
		return "SYS_HIBERNATE_HBM";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysHibernateHbm";
	}
}
