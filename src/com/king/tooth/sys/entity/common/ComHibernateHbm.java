package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.util.JsonUtil;

/**
 * [通用的]hibernate的hbm内容对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComHibernateHbm extends AbstractSysResource implements ITable, IEntity{
	
	/**
	 * 关联的数据库主键
	 * <p>如果发布到项目中，这个字段必须有值</p>
	 */
	private String refDatabaseId;
	/**
	 * 关联的表主键
	 * <p>如果被发布到项目中在，这个字段有无值均可</p>
	 */
	private String refTableId;
	/**
	 * hbm资源名
	 * <p>即对应的表的资源名</p>
	 */
	private String hbmResourceName;
	
	/**
	 * 是否是关系表的hbm
	 */
	private int isDataLinkTableHbm;
	/**
	 * hbm内容
	 */
	private String hbmContent;

	//-------------------------------------------------------------------------
	
	public String getHbmContent() {
		return hbmContent;
	}
	public void setHbmContent(String hbmContent) {
		this.hbmContent = hbmContent;
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
	public String getHbmResourceName() {
		return hbmResourceName;
	}
	public void setHbmResourceName(String hbmResourceName) {
		this.hbmResourceName = hbmResourceName;
	}
	public int getIsDataLinkTableHbm() {
		return isDataLinkTableHbm;
	}
	public void setIsDataLinkTableHbm(int isDataLinkTableHbm) {
		this.isDataLinkTableHbm = isDataLinkTableHbm;
	}
	
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_HIBERNATE_HBM", 0);
		table.setIsResource(1);
		table.setName("[通用的]hibernate的hbm内容对象表");
		table.setComments("[通用的]hibernate的hbm内容对象表");
		table.setIsBuiltin(1);
		table.setPlatformType(IS_COMMON_PLATFORM_TYPE);
		table.setIsCreatedResource(1);
		table.setIsNeedDeploy(1);
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(18);
		
		CfgColumndata refDatabaseIdColumn = new CfgColumndata("ref_database_id");
		refDatabaseIdColumn.setName("关联的数据库主键");
		refDatabaseIdColumn.setComments("关联的数据库主键：如果发布到项目中，这个字段必须有值");
		refDatabaseIdColumn.setColumnType(DataTypeConstants.STRING);
		refDatabaseIdColumn.setLength(32);
		refDatabaseIdColumn.setOrderCode(1);
		columns.add(refDatabaseIdColumn);
		
		CfgColumndata refTableIdColumn = new CfgColumndata("ref_table_id");
		refTableIdColumn.setName("关联的表主键");
		refTableIdColumn.setComments("关联的表主键：如果被发布到项目中在，这个字段有无值均可");
		refTableIdColumn.setColumnType(DataTypeConstants.STRING);
		refTableIdColumn.setLength(32);
		refTableIdColumn.setOrderCode(2);
		columns.add(refTableIdColumn);
		
		CfgColumndata hbmResourceNameColumn = new CfgColumndata("hbm_resource_name");
		hbmResourceNameColumn.setName("hbm资源名");
		hbmResourceNameColumn.setComments("hbm资源名：即对应的表的资源名");
		hbmResourceNameColumn.setColumnType(DataTypeConstants.STRING);
		hbmResourceNameColumn.setLength(60);
		hbmResourceNameColumn.setOrderCode(3);
		columns.add(hbmResourceNameColumn);
		
		CfgColumndata isDataLinkTableHbmColumn = new CfgColumndata("is_data_link_table_hbm");
		isDataLinkTableHbmColumn.setName("是否是关系表的hbm");
		isDataLinkTableHbmColumn.setComments("是否是关系表的hbm");
		isDataLinkTableHbmColumn.setColumnType(DataTypeConstants.INTEGER);
		isDataLinkTableHbmColumn.setLength(1);
		isDataLinkTableHbmColumn.setOrderCode(4);
		columns.add(isDataLinkTableHbmColumn);
		
		CfgColumndata hbmContentColumn = new CfgColumndata("hbm_content");
		hbmContentColumn.setName("hbm内容");
		hbmContentColumn.setComments("hbm内容");
		hbmContentColumn.setColumnType(DataTypeConstants.CLOB);
		hbmContentColumn.setOrderCode(5);
		columns.add(hbmContentColumn);
		
		table.setColumns(columns);
		return table;
	}
	
	public String toDropTable() {
		return "COM_HIBERNATE_HBM";
	}
	
	public String getEntityName() {
		return "ComHibernateHbm";
	}
	
	public JSONObject toEntity() {
		JSONObject json = JsonUtil.toJsonObject(this);
		json.put("isDataLinkTableHbm", isDataLinkTableHbm+"");
		json.put("isEnabled", isEnabled+"");
		json.put("validDate", validDate);
		json.put("isNeedDeploy", isNeedDeploy+"");
		json.put("isBuiltin", isBuiltin+"");
		json.put("platformType", platformType+"");
		json.put("isCreatedResource", isCreatedResource+"");
		json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		return json;
	}
	
	public ComSysResource turnToResource() {
		ComSysResource resource = super.turnToResource();
		resource.setRefResourceId(id);
		resource.setResourceType(TABLE);
		resource.setResourceName(hbmResourceName);
		return resource;
	}
	
	/**
	 * 将表信息，转换为对应的hbm信息
	 * @param table
	 */
	public void turnToHbm(CfgTabledata table){
		this.setRefDatabaseId(CurrentThreadContext.getDatabaseId());
		this.setRefTableId(table.getId());
		this.setHbmResourceName(table.getResourceName());
		this.setIsDataLinkTableHbm(table.getIsDatalinkTable());
		this.setIsEnabled(table.getIsEnabled());
		this.setValidDate(table.getValidDate());
		this.setIsNeedDeploy(table.getIsNeedDeploy());
		this.setReqResourceMethod(table.getReqResourceMethod());
		this.setIsBuiltin(1);
		this.setPlatformType(table.getPlatformType());
		this.setIsCreatedResource(1);
	}
}
