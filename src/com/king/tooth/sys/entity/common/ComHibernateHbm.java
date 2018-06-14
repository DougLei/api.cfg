package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.IPublish;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComPublishInfo;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.ResourceHandlerUtil;

/**
 * hibernate的hbm内容对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComHibernateHbm extends AbstractSysResource implements ITable, IPublish{
	
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
	private String hbmResourceName;
	
	/**
	 * 是否是关系表的hbm
	 */
	private Integer isDataLinkTableHbm;
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
	public Integer getIsDataLinkTableHbm() {
		return isDataLinkTableHbm;
	}
	public void setIsDataLinkTableHbm(Integer isDataLinkTableHbm) {
		this.isDataLinkTableHbm = isDataLinkTableHbm;
	}
	
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_HIBERNATE_HBM", 0);
		table.setName("hibernate的hbm内容对象表");
		table.setComments("hibernate的hbm内容对象表");
		table.setIsResource(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setReqResourceMethod(NONE);
		table.setIsCreated(1);
		table.setBelongPlatformType(COMMON_PLATFORM);
		table.setIsCore(1);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(18);
		
		ComColumndata refDatabaseIdColumn = new ComColumndata("ref_database_id", DataTypeConstants.STRING, 32);
		refDatabaseIdColumn.setName("关联的数据库主键");
		refDatabaseIdColumn.setComments("关联的数据库主键：如果发布到项目中，这个字段必须有值");
		refDatabaseIdColumn.setOrderCode(1);
		columns.add(refDatabaseIdColumn);
		
		ComColumndata refTableIdColumn = new ComColumndata("ref_table_id", DataTypeConstants.STRING, 32);
		refTableIdColumn.setName("关联的表主键");
		refTableIdColumn.setComments("关联的表主键：如果在被发布到项目中，这个字段有无值均可");
		refTableIdColumn.setOrderCode(2);
		columns.add(refTableIdColumn);
		
		ComColumndata hbmResourceNameColumn = new ComColumndata("hbm_resource_name", DataTypeConstants.STRING, 60);
		hbmResourceNameColumn.setName("hbm资源名");
		hbmResourceNameColumn.setComments("hbm资源名：即对应的表的资源名");
		hbmResourceNameColumn.setOrderCode(3);
		columns.add(hbmResourceNameColumn);
		
		ComColumndata isDataLinkTableHbmColumn = new ComColumndata("is_data_link_table_hbm", DataTypeConstants.INTEGER, 1);
		isDataLinkTableHbmColumn.setName("是否是关系表的hbm");
		isDataLinkTableHbmColumn.setComments("是否是关系表的hbm");
		isDataLinkTableHbmColumn.setDefaultValue("0");
		isDataLinkTableHbmColumn.setOrderCode(4);
		columns.add(isDataLinkTableHbmColumn);
		
		ComColumndata hbmContentColumn = new ComColumndata("hbm_content", DataTypeConstants.CLOB, 0);
		hbmContentColumn.setName("hbm内容");
		hbmContentColumn.setComments("hbm内容");
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
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		entityJson.put("isDataLinkTableHbm", isDataLinkTableHbm);
		super.processSysResourceProps(entityJson);
		return entityJson.getEntityJson();
	}
	
	public ComSysResource turnToResource() {
		throw new IllegalArgumentException("该资源目前不支持turnToResource功能");
	}
	
	public ComSysResource turnToPublishResource() {
		ComSysResource resource = super.turnToResource();
		resource.setId(ResourceHandlerUtil.getIdentity());
		resource.setResourceType(TABLE);
		resource.setResourceName(hbmResourceName);
		resource.setProjectId(projectId);
		if(isDataLinkTableHbm != null && isDataLinkTableHbm == 1){
			resource.setRefResourceId(refTableId);
		}else{
			resource.setRefResourceId(id);
		}
		return resource;
	}
	
	/**
	 * 将表信息，转换为对应的hbm信息
	 * @param table
	 */
	public void tableTurnToHbm(ComTabledata table){
		this.setRefTableId(table.getId());
		this.setHbmResourceName(table.getResourceName());
		this.setIsDataLinkTableHbm(table.getIsDatalinkTable());
		this.setIsEnabled(table.getIsEnabled());
		this.setIsNeedDeploy(table.getIsNeedDeploy());
		this.setReqResourceMethod(table.getReqResourceMethod());
		this.setIsCreated(table.getIsCreated());
	}

	public Integer getResourceType() {
		return TABLE;
	}
	
	public ComPublishInfo turnToPublish() {
		ComPublishInfo publish = new ComPublishInfo();
		publish.setPublishDatabaseId(refDatabaseId);
		publish.setPublishProjectId(projectId);
		if(isDataLinkTableHbm != null && isDataLinkTableHbm == 1){
			publish.setPublishResourceId(refTableId);
		}else{
			publish.setPublishResourceId(id);
		}
		publish.setPublishResourceName(hbmResourceName);
		publish.setResourceType(TABLE);
		super.turnToPublish();
		return publish;
	}
}
