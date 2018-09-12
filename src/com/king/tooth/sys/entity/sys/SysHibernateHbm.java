package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.IPublish;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.dm.DmPublishInfo;
import com.king.tooth.util.ResourceHandlerUtil;

/**
 * hibernate的hbm内容表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysHibernateHbm extends AbstractSysResource implements ITable, IPublish{
	
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
		ComTabledata table = new ComTabledata("SYS_HIBERNATE_HBM", 0);
		table.setName("hibernate的hbm内容表");
		table.setComments("hibernate的hbm内容表");
		table.setIsResource(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(COMMON_PLATFORM);
		table.setIsCore(1);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(19);
		
		ComColumndata refDatabaseIdColumn = new ComColumndata("ref_database_id", BuiltinDataType.STRING, 32);
		refDatabaseIdColumn.setName("关联的数据库主键");
		refDatabaseIdColumn.setComments("关联的数据库主键：如果发布到项目中，这个字段必须有值");
		refDatabaseIdColumn.setOrderCode(1);
		columns.add(refDatabaseIdColumn);
		
		ComColumndata refTableIdColumn = new ComColumndata("ref_table_id", BuiltinDataType.STRING, 32);
		refTableIdColumn.setName("关联的表主键");
		refTableIdColumn.setComments("关联的表主键：如果在被发布到项目中，这个字段有无值均可");
		refTableIdColumn.setOrderCode(2);
		columns.add(refTableIdColumn);
		
		ComColumndata hbmResourceNameColumn = new ComColumndata("hbm_resource_name", BuiltinDataType.STRING, 60);
		hbmResourceNameColumn.setName("hbm资源名");
		hbmResourceNameColumn.setComments("hbm资源名：即对应的表的资源名");
		hbmResourceNameColumn.setOrderCode(3);
		columns.add(hbmResourceNameColumn);
		
		ComColumndata isDataLinkTableHbmColumn = new ComColumndata("is_data_link_table_hbm", BuiltinDataType.INTEGER, 1);
		isDataLinkTableHbmColumn.setName("是否是关系表的hbm");
		isDataLinkTableHbmColumn.setComments("是否是关系表的hbm");
		isDataLinkTableHbmColumn.setDefaultValue("0");
		isDataLinkTableHbmColumn.setOrderCode(4);
		columns.add(isDataLinkTableHbmColumn);
		
		ComColumndata hbmContentColumn = new ComColumndata("hbm_content", BuiltinDataType.CLOB, 0);
		hbmContentColumn.setName("hbm内容");
		hbmContentColumn.setComments("hbm内容");
		hbmContentColumn.setOrderCode(5);
		columns.add(hbmContentColumn);
		
		table.setColumns(columns);
		return table;
	}
	
	public String toDropTable() {
		return "SYS_HIBERNATE_HBM";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysHibernateHbm";
	}
	
	public SysResource turnToResource() {
		throw new IllegalArgumentException("该资源目前不支持turnToResource功能");
	}
	
	/**
	 * 将表信息，转换为对应的hbm信息
	 * @param table
	 */
	public void tableTurnToHbm(ComTabledata table){
		if(table.getIsDatalinkTable() == 1){
			this.setRefTableId(table.getParentTableId());
		}else{
			this.setRefTableId(table.getId());
		}
		this.setHbmResourceName(table.getResourceName());
		this.setIsDataLinkTableHbm(table.getIsDatalinkTable());
		
		this.setIsEnabled(table.getIsEnabled());
		this.setReqResourceMethod(table.getReqResourceMethod());
		this.setIsBuiltin(table.getIsBuiltin());
		this.setIsNeedDeploy(table.getIsNeedDeploy());
		this.setBelongPlatformType(table.getBelongPlatformType());
		this.setIsCreated(table.getIsCreated());
	}

	@JSONField(serialize = false)
	public Integer getResourceType() {
		return TABLE;
	}
	
	public DmPublishInfo turnToPublish() {
		DmPublishInfo publish = new DmPublishInfo();
		publish.setPublishDatabaseId(refDatabaseId);
		publish.setPublishProjectId(projectId);
		if(isDataLinkTableHbm != null && isDataLinkTableHbm == 1){
			publish.setPublishResourceId(refTableId);
		}else{
			publish.setPublishResourceId(id);
		}
		publish.setPublishResourceName(hbmResourceName);
		publish.setResourceType(TABLE);
		return publish;
	}
	
	public JSONObject toPublishEntityJson(String projectId) {
		JSONObject json = toEntityJson();
		json.put("isCreated", "0");
		json.put("refDataId", refTableId);
		json.put(ResourcePropNameConstants.ID, ResourceHandlerUtil.getIdentity());
		json.put("projectId", projectId);
		processPublishEntityJson(json);
		return json;
	}
	
	public SysResource turnToPublishResource(String projectId, String refResourceId) {
		SysResource resource = super.turnToResource();
		resource.setId(ResourceHandlerUtil.getIdentity());
		resource.setRefDataId(refTableId);
		resource.setResourceType(TABLE);
		resource.setResourceName(hbmResourceName);
		resource.setProjectId(projectId);
		resource.setRefResourceId(refResourceId);
		return resource;
	}
}
