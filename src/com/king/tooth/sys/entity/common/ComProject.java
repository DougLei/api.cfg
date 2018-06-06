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
import com.king.tooth.util.StrUtils;

/**
 * [通用的]项目信息资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComProject extends AbstractSysResource implements ITable, IEntity{
	
	/**
	 * 项目名称
	 */
	private String name;
	/**
	 * 项目描述
	 */
	private String descs;
	
	//-----------------------------------------------------------
	
	public ComProject() {
	}

	public void setId(String id) {
		this.id = id;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
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
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(StrUtils.isEmpty(name)){
			throw new NullPointerException("项目名称不能为空");
		}
		this.name = name;
	}
	public String getDescs() {
		return descs;
	}
	public void setDescs(String descs) {
		this.descs = descs;
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
		CfgTabledata table = new CfgTabledata(dbType, "COM_PROJECT");
		table.setName("[通用的]项目信息资源对象表");
		table.setComments("[通用的]项目信息资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(12);
		
		CfgColumndata nameColumn = new CfgColumndata("name");
		nameColumn.setName("项目名称");
		nameColumn.setComments("项目名称");
		nameColumn.setColumnType(DataTypeConstants.STRING);
		nameColumn.setLength(200);
		nameColumn.setOrderCode(1);
		columns.add(nameColumn);
		
		CfgColumndata descsColumn = new CfgColumndata("descs");
		descsColumn.setName("项目描述");
		descsColumn.setComments("项目描述");
		descsColumn.setColumnType(DataTypeConstants.STRING);
		descsColumn.setLength(800);
		descsColumn.setOrderCode(2);
		columns.add(descsColumn);
		
		CfgColumndata isDeploymentAppColumn = new CfgColumndata("is_deployment_app");
		isDeploymentAppColumn.setName("是否部署到正式环境");
		isDeploymentAppColumn.setComments("是否部署到正式环境");
		isDeploymentAppColumn.setColumnType(DataTypeConstants.INTEGER);
		isDeploymentAppColumn.setLength(1);
		isDeploymentAppColumn.setOrderCode(3);
		columns.add(isDeploymentAppColumn);
		
		CfgColumndata reqResourceMethodColumn = new CfgColumndata("req_resource_method");
		reqResourceMethodColumn.setName("请求资源的方法");
		reqResourceMethodColumn.setComments("请求资源的方法:get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持");
		reqResourceMethodColumn.setColumnType(DataTypeConstants.STRING);
		reqResourceMethodColumn.setLength(20);
		reqResourceMethodColumn.setOrderCode(4);
		columns.add(reqResourceMethodColumn);

		CfgColumndata isBuiltinColumn = new CfgColumndata("is_builtin");
		isBuiltinColumn.setName("是否内置");
		isBuiltinColumn.setComments("是否内置:如果不是内置，则需要发布出去；如果是内置，且platformType=2或3，则也需要发布出去；如果是内置，且platformType=1，则不需要发布出去");
		isBuiltinColumn.setColumnType(DataTypeConstants.INTEGER);
		isBuiltinColumn.setLength(1);
		isBuiltinColumn.setOrderCode(5);
		columns.add(isBuiltinColumn);
		
		CfgColumndata platformTypeColumn = new CfgColumndata("platform_type");
		platformTypeColumn.setName("所属于的平台类型");
		platformTypeColumn.setComments("所属于的平台类型:1:配置平台、2:运行平台、3:公用");
		platformTypeColumn.setColumnType(DataTypeConstants.INTEGER);
		platformTypeColumn.setLength(1);
		platformTypeColumn.setOrderCode(6);
		columns.add(platformTypeColumn);
		
		CfgColumndata isCreatedResourceColumn = new CfgColumndata("is_created_resource");
		isCreatedResourceColumn.setName("是否已经创建资源");
		isCreatedResourceColumn.setComments("是否已经创建资源");
		isCreatedResourceColumn.setColumnType(DataTypeConstants.INTEGER);
		isCreatedResourceColumn.setLength(1);
		isCreatedResourceColumn.setOrderCode(7);
		columns.add(isCreatedResourceColumn);
		
		table.setColumns(columns);
		table.setIsBuiltin(1);
		table.setPlatformType(IS_COMMON_PLATFORM_TYPE);
		table.setIsCreatedResource(1);
		return table;
	}
	public String toDropTable() {
		return "COM_PROJECT";
	}
	
	public int getResourceType() {
		return PROJECT;
	}
	public String getResourceName() {
		return name;
	}	
	public String getResourceId() {
		return getId();
	}
	
	public String getEntityName() {
		return "ComProject";
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
}
