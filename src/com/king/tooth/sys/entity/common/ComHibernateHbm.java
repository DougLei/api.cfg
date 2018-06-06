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
	public int getIsDeploymentApp() {
		return isDeploymentApp;
	}
	public void setIsDeploymentApp(int isDeploymentApp) {
		this.isDeploymentApp = isDeploymentApp;
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
	
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_HIBERNATE_HBM");
		table.setName("[通用的]hibernate的hbm内容对象表");
		table.setComments("[通用的]hibernate的hbm内容对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(11);
		
		CfgColumndata hbmContentColumn = new CfgColumndata("hbm_content");
		hbmContentColumn.setName("hbm内容");
		hbmContentColumn.setComments("hbm内容");
		hbmContentColumn.setColumnType(DataTypeConstants.CLOB);
		hbmContentColumn.setOrderCode(1);
		columns.add(hbmContentColumn);
		
		CfgColumndata isDeploymentAppColumn = new CfgColumndata("is_deployment_app");
		isDeploymentAppColumn.setName("是否部署到正式环境");
		isDeploymentAppColumn.setComments("是否部署到正式环境");
		isDeploymentAppColumn.setColumnType(DataTypeConstants.INTEGER);
		isDeploymentAppColumn.setLength(1);
		isDeploymentAppColumn.setOrderCode(2);
		columns.add(isDeploymentAppColumn);
		
		CfgColumndata reqResourceMethodColumn = new CfgColumndata("req_resource_method");
		reqResourceMethodColumn.setName("请求资源的方法");
		reqResourceMethodColumn.setComments("请求资源的方法:get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持");
		reqResourceMethodColumn.setColumnType(DataTypeConstants.STRING);
		reqResourceMethodColumn.setLength(20);
		reqResourceMethodColumn.setOrderCode(3);
		columns.add(reqResourceMethodColumn);

		CfgColumndata isBuiltinColumn = new CfgColumndata("is_builtin");
		isBuiltinColumn.setName("是否内置");
		isBuiltinColumn.setComments("是否内置:如果不是内置，则需要发布出去；如果是内置，且platformType=2或3，则也需要发布出去；如果是内置，且platformType=1，则不需要发布出去");
		isBuiltinColumn.setColumnType(DataTypeConstants.INTEGER);
		isBuiltinColumn.setLength(1);
		isBuiltinColumn.setOrderCode(4);
		columns.add(isBuiltinColumn);
		
		CfgColumndata platformTypeColumn = new CfgColumndata("platform_type");
		platformTypeColumn.setName("所属于的平台类型");
		platformTypeColumn.setComments("所属于的平台类型:1:配置平台、2:运行平台、3:公用");
		platformTypeColumn.setColumnType(DataTypeConstants.INTEGER);
		platformTypeColumn.setLength(1);
		platformTypeColumn.setOrderCode(5);
		columns.add(platformTypeColumn);
		
		CfgColumndata isCreatedResourceColumn = new CfgColumndata("is_created_resource");
		isCreatedResourceColumn.setName("是否已经创建资源");
		isCreatedResourceColumn.setComments("是否已经创建资源");
		isCreatedResourceColumn.setColumnType(DataTypeConstants.INTEGER);
		isCreatedResourceColumn.setLength(1);
		isCreatedResourceColumn.setOrderCode(6);
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
		json.put("isDeploymentApp", isDeploymentApp+"");
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
		return null;
	}
	public String getResourceId() {
		return getId();
	}
}
