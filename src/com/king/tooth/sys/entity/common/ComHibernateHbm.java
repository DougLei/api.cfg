package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.constants.TableConstants;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.util.JsonUtil;

/**
 * [通用的]hibernate的hbm内容
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
	public int getIsDeploymentRun() {
		return isDeploymentRun;
	}
	public void setIsDeploymentRun(int isDeploymentRun) {
		this.isDeploymentRun = isDeploymentRun;
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
		table.setName("[配置系统]字段数据信息资源对象表");
		table.setComments("[配置系统]字段数据信息资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(7);
		
		CfgColumndata hbmContentColumn = new CfgColumndata("hbm_content");
		hbmContentColumn.setName("显示的汉字名称");
		hbmContentColumn.setComments("显示的汉字名称");
		hbmContentColumn.setColumnType(DataTypeConstants.CLOB);
		hbmContentColumn.setOrderCode(2);
		columns.add(hbmContentColumn);
		
		table.setColumns(columns);
		table.setIsBuiltin(1);
		table.setPlatformType(TableConstants.IS_CFG_PLATFORM_TYPE);
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
