package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public int getIsNeedDeploy() {
		return isNeedDeploy;
	}
	public void setIsNeedDeploy(int isNeedDeploy) {
		this.isNeedDeploy = isNeedDeploy;
	}
	public int getIsBuiltin() {
		return isBuiltin;
	}
	public void setIsBuiltin(int isBuiltin) {
		this.isBuiltin = isBuiltin;
	}
	public int getPlatformType() {
		return platformType;
	}
	public void setPlatformType(int platformType) {
		this.platformType = platformType;
	}
	public int getIsCreatedResource() {
		return isCreatedResource;
	}
	public void setIsCreatedResource(int isCreatedResource) {
		this.isCreatedResource = isCreatedResource;
	}
	public void setReqResourceMethod(String reqResourceMethod) {
		this.reqResourceMethod = reqResourceMethod;
	}
	public String getReqResourceMethod() {
		return super.getReqResourceMethod();
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
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
		CfgTabledata table = new CfgTabledata(dbType, "COM_HIBERNATE_HBM");
		table.setName("[通用的]hibernate的hbm内容对象表");
		table.setComments("[通用的]hibernate的hbm内容对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(15);
		
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
		
		CfgColumndata isNeedDeployColumn = new CfgColumndata("is_need_deploy");
		isNeedDeployColumn.setName("是否需要发布");
		isNeedDeployColumn.setComments("是否需要发布");
		isNeedDeployColumn.setColumnType(DataTypeConstants.INTEGER);
		isNeedDeployColumn.setLength(1);
		isNeedDeployColumn.setOrderCode(6);
		columns.add(isNeedDeployColumn);
		
		CfgColumndata reqResourceMethodColumn = new CfgColumndata("req_resource_method");
		reqResourceMethodColumn.setName("请求资源的方法");
		reqResourceMethodColumn.setComments("请求资源的方法:get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持");
		reqResourceMethodColumn.setColumnType(DataTypeConstants.STRING);
		reqResourceMethodColumn.setLength(20);
		reqResourceMethodColumn.setOrderCode(7);
		columns.add(reqResourceMethodColumn);

		CfgColumndata isBuiltinColumn = new CfgColumndata("is_builtin");
		isBuiltinColumn.setName("是否内置");
		isBuiltinColumn.setComments("是否内置");
		isBuiltinColumn.setColumnType(DataTypeConstants.INTEGER);
		isBuiltinColumn.setLength(1);
		isBuiltinColumn.setOrderCode(8);
		columns.add(isBuiltinColumn);
		
		CfgColumndata platformTypeColumn = new CfgColumndata("platform_type");
		platformTypeColumn.setName("所属于的平台类型");
		platformTypeColumn.setComments("所属于的平台类型:1:配置平台、2:运行平台、3:公用");
		platformTypeColumn.setColumnType(DataTypeConstants.INTEGER);
		platformTypeColumn.setLength(1);
		platformTypeColumn.setOrderCode(9);
		columns.add(platformTypeColumn);
		
		CfgColumndata isCreatedResourceColumn = new CfgColumndata("is_created_resource");
		isCreatedResourceColumn.setName("是否已经创建资源");
		isCreatedResourceColumn.setComments("是否已经创建资源");
		isCreatedResourceColumn.setColumnType(DataTypeConstants.INTEGER);
		isCreatedResourceColumn.setLength(1);
		isCreatedResourceColumn.setOrderCode(10);
		columns.add(isCreatedResourceColumn);
		
		table.setColumns(columns);
		table.setIsBuiltin(1);
		table.setPlatformType(IS_COMMON_PLATFORM_TYPE);
		table.setIsCreatedResource(1);
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
		json.put("isNeedDeploy", isNeedDeploy+"");
		json.put("isBuiltin", isBuiltin+"");
		json.put("platformType", platformType+"");
		json.put("isCreatedResource", isCreatedResource+"");
		if(this.createTime != null){
			json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		}
		return json;
	}
	
	public int getResourceType() {
		return TABLE_HBM;
	}
	public String getResourceName() {
		return getHbmResourceName();
	}
	public String getResourceId() {
		return getId();
	}
}
