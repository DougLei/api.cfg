package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.util.StrUtils;

/**
 * [通用的]系统资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComSysResource extends BasicEntity implements ITable{
	
	/**
	 * 关联的数据库主键
	 * <p>算是个冗余字段，方便直接通过数据库操作资源</p>
	 */
	private String databaseId;
	/**
	 * 关联的目标资源主键
	 * <p>目前，可以是hbm资源(ComHibernateHbmConfdata)的主键，也可以是sql脚本资源(ComSqlScript)的主键</p>
	 * <p>说明：hbm和table是一一对应的关系，所以，添加表资源的时候，传入的对象是table，但是调用的时候，实际使用的是hbm，这个要清楚</p>
	 */
	private String refResourceId;
	/**
	 * 资源名
	 */
	private String resourceName;
	/**
	 * 资源类型
	 * 1:表资源
	 * 2:sql脚本资源
	 */
	private int resourceType;
	/**
	 * 是否启用
	 */
	private int isEnabled;
	
	//-------------------------------------------------------------------------
	
	/**
	 * hbm资源
	 */
	private ComHibernateHbmConfdata hbmConfdataResource;
	/**
	 * sql脚本资源
	 */
	private ComSqlScript sqlScriptResource;
	
	
	public String getResourceName() {
		return resourceName;
	}
	public String getRefResourceId() {
		return refResourceId;
	}
	public void setRefResourceId(String refResourceId) {
		if(StrUtils.isEmpty(refResourceId)){
			refResourceId = "builtin resource";
		}
		this.refResourceId = refResourceId;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public ComHibernateHbmConfdata getHbmConfdataResource() {
		return hbmConfdataResource;
	}
	public void setHbmConfdataResource(ComHibernateHbmConfdata hbmConfdataResource) {
		this.hbmConfdataResource = hbmConfdataResource;
	}
	public ComSqlScript getSqlScriptResource() {
		return sqlScriptResource;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getDatabaseId() {
		return databaseId;
	}
	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}
	public String getId() {
		return id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public void setSqlScriptResource(ComSqlScript sqlScriptResource) {
		this.sqlScriptResource = sqlScriptResource;
	}
	public int getResourceType() {
		return resourceType;
	}
	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}
	public int getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_SYS_RESOURCE");
		table.setName("[通用的]系统资源对象表");
		table.setComments("[通用的]系统资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(11);
		
		CfgColumndata databaseIdColumn = new CfgColumndata("database_id");
		databaseIdColumn.setName("关联的数据库主键");
		databaseIdColumn.setComments("关联的数据库主键:算是个冗余字段，方便直接通过数据库操作资源");
		databaseIdColumn.setColumnType(DataTypeConstants.STRING);
		databaseIdColumn.setLength(32);
		databaseIdColumn.setOrderCode(1);
		columns.add(databaseIdColumn);
		
		CfgColumndata refResourceIdColumn = new CfgColumndata("ref_resource_id");
		refResourceIdColumn.setName("关联的目标资源主键");
		refResourceIdColumn.setComments("关联的目标资源主键:目前，可以是hbm资源(ComHibernateHbmConfdata)的主键，也可以是sql脚本资源(ComSqlScript)的主键。说明：hbm和table是一一对应的关系，所以，添加表资源的时候，传入的对象是table，但是调用的时候，实际使用的是hbm，这个要清楚");
		refResourceIdColumn.setColumnType(DataTypeConstants.STRING);
		refResourceIdColumn.setLength(32);
		refResourceIdColumn.setOrderCode(2);
		columns.add(refResourceIdColumn);
		
		CfgColumndata resourceNameColumn = new CfgColumndata("resource_name");
		resourceNameColumn.setName("资源名");
		resourceNameColumn.setComments("资源名");
		resourceNameColumn.setColumnType(DataTypeConstants.STRING);
		resourceNameColumn.setLength(100);
		resourceNameColumn.setOrderCode(3);
		columns.add(resourceNameColumn);
		
		CfgColumndata resourceTypeColumn = new CfgColumndata("resource_type");
		resourceTypeColumn.setName("资源类型");
		resourceTypeColumn.setComments("资源类型：1:表资源、2:sql脚本资源");
		resourceTypeColumn.setColumnType(DataTypeConstants.INTEGER);
		resourceTypeColumn.setLength(1);
		resourceTypeColumn.setOrderCode(4);
		columns.add(resourceTypeColumn);
		
		CfgColumndata isEnabledColumn = new CfgColumndata("is_enabled");
		isEnabledColumn.setName("是否启用");
		isEnabledColumn.setComments("是否启用");
		isEnabledColumn.setColumnType(DataTypeConstants.INTEGER);
		isEnabledColumn.setLength(1);
		isEnabledColumn.setOrderCode(5);
		columns.add(isEnabledColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_SYS_RESOURCE";
	}
}
