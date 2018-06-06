package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.constants.TableConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.util.JsonUtil;

/**
 * [通用的]项目模块内容资源对象
 * <p>理解为每个功能模块的json串配置</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComProjectModuleBody extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 关联的项目模块编号
	 */
	private String moduleId;
	/**
	 * 模块的内容
	 * <p>json串</p>
	 */
	private String moduleBody;
	
	//----------------------------------------------------------------
	
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
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleBody() {
		return moduleBody;
	}
	public void setModuleBody(String moduleBody) {
		this.moduleBody = moduleBody;
	}
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_PROJECT_MODULE_BODY");
		table.setName("[通用的]项目模块内容资源对象");
		table.setComments("[通用的]项目模块内容资源对象：理解为每个功能模块的json串配置");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(7);
		
		CfgColumndata moduleIdColumn = new CfgColumndata("module_id");
		moduleIdColumn.setName("关联的项目模块编号");
		moduleIdColumn.setComments("关联的项目模块编号");
		moduleIdColumn.setColumnType(DataTypeConstants.STRING);
		moduleIdColumn.setLength(32);
		moduleIdColumn.setOrderCode(1);
		columns.add(moduleIdColumn);
		
		CfgColumndata moduleBodyColumn = new CfgColumndata("module_body");
		moduleBodyColumn.setName("模块的内容");
		moduleBodyColumn.setComments("模块的内容:json串");
		moduleBodyColumn.setColumnType(DataTypeConstants.CLOB);
		moduleBodyColumn.setOrderCode(2);
		columns.add(moduleBodyColumn);
		
		table.setColumns(columns);
		table.setIsBuiltin(1);
		table.setPlatformType(TableConstants.IS_COMMON_PLATFORM_TYPE);
		table.setIsCreateHbm(1);
		return table;
	}
	public String toDropTable() {
		return "COM_PROJECT_MODULE_BODY";
	}
	
	public String getEntityName() {
		return "ComProjectModuleBody";
	}
	public JSONObject toEntity() {
		JSONObject json = JsonUtil.toJsonObject(this);
		if(this.createTime != null){
			json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		}
		return json;
	}
}
