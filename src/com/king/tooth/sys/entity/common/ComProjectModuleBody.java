package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;

/**
 * [通用的]项目模块内容资源对象
 * <p>理解为每个功能模块的json串配置</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComProjectModuleBody extends BasicEntity implements ITable{
	
	/**
	 * 关联的项目模块编号
	 */
	private String moduleId;
	/**
	 * 模块的内容
	 * <p>json串</p>
	 */
	private String moduleBody;
	/**
	 * 是否启用
	 */
	private int isEnabled;
	/**
	 * 版本
	 */
	private int version;
	
	//----------------------------------------------------------------
	/**
	 * 包含的操作对象集合
	 */
	private List<ComModuleOperation> moduleOperations;
	
	public ComProjectModuleBody() {
		this.isEnabled = 1;
		this.version = 1;
	}
	
	public List<ComModuleOperation> getModuleOperations() {
		return moduleOperations;
	}
	public void setModuleOperations(List<ComModuleOperation> moduleOperations) {
		this.moduleOperations = moduleOperations;
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
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_PROJECT_MODULE_BODY");
		table.setName("[通用的]项目模块内容资源对象");
		table.setComments("[通用的]项目模块内容资源对象：理解为每个功能模块的json串配置");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(9);
		
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
		
		CfgColumndata isEnabledColumn = new CfgColumndata("is_enabled");
		isEnabledColumn.setName("是否启用");
		isEnabledColumn.setComments("是否启用");
		isEnabledColumn.setColumnType(DataTypeConstants.INTEGER);
		isEnabledColumn.setLength(1);
		isEnabledColumn.setOrderCode(3);
		columns.add(isEnabledColumn);
		
		CfgColumndata versionColumn = new CfgColumndata("version");
		versionColumn.setName("版本");
		versionColumn.setComments("版本");
		versionColumn.setColumnType(DataTypeConstants.INTEGER);
		versionColumn.setLength(1);
		versionColumn.setOrderCode(4);
		columns.add(versionColumn);
		
		table.setColumns(columns);
		return table;
	}
	public String toDropTable() {
		return "COM_PROJECT_MODULE_BODY";
	}
}
