package com.api.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.api.annotation.Table;
import com.api.constants.DataTypeConstants;
import com.api.sys.entity.BasicEntity;
import com.api.sys.entity.IEntity;

/**
 * hibernate的hbm内容表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgHibernateHbm extends BasicEntity implements IEntity{
	
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
	/**
	 * 资源名
	 * <p>冗余，即表资源名</p>
	 */
	private String resourceName;

	//-------------------------------------------------------------------------
	
	public CfgHibernateHbm() {
	}
	public CfgHibernateHbm(CfgTable table) {
		this.setRefTableId(table.getId());
		this.resourceName = table.getResourceName();
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
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(4+7);
		
		CfgColumn refDatabaseIdColumn = new CfgColumn("ref_database_id", DataTypeConstants.STRING, 32);
		refDatabaseIdColumn.setName("关联的数据库主键");
		refDatabaseIdColumn.setComments("关联的数据库主键");
		columns.add(refDatabaseIdColumn);
		
		CfgColumn refTableIdColumn = new CfgColumn("ref_table_id", DataTypeConstants.STRING, 32);
		refTableIdColumn.setName("关联的表主键");
		refTableIdColumn.setComments("关联的表主键");
		columns.add(refTableIdColumn);
		
		CfgColumn contentColumn = new CfgColumn("content", DataTypeConstants.CLOB, 0);
		contentColumn.setName("hbm内容");
		contentColumn.setComments("hbm内容");
		columns.add(contentColumn);
		
		CfgColumn resourceNameColumn = new CfgColumn("resource_name", DataTypeConstants.STRING, 60);
		resourceNameColumn.setName("hbm资源名");
		resourceNameColumn.setComments("hbm资源名：即对应的表的资源名");
		columns.add(resourceNameColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("hibernate的hbm内容表");
		table.setRemark("hibernate的hbm内容表");
		
		table.setColumns(getColumnList());
		return table;
	}
	
	public String toDropTable() {
		return "CFG_HIBERNATE_HBM";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgHibernateHbm";
	}
}
