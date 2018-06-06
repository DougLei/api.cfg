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
 * [通用的]项目模块信息资源对象
 * <p>理解为菜单</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComProjectModule extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 父模块编号
	 * <p>可为空，用于递归</p>
	 */
	private String parentId;
	/**
	 * 模块名称
	 */
	private String name;
	/**
	 * 模块编码
	 * <p>这个编码的命名，要整个项目唯一</p>
	 */
	private String code;
	/**
	 * url
	 */
	private String url;
	/**
	 * 模块图标
	 */
	private String icon;
	/**
	 * 排序值
	 */
	private int orderCode;
	/**
	 * 是否启用
	 */
	private int isEnabled;
	
	//-----------------------------------------------
	/**
	 * 包含的操作对象集合
	 */
	private List<ComModuleOperation> moduleOperations;
	
	public ComProjectModule() {
		this.isEnabled = 1;
	}
	
	public String getParentId() {
		return parentId;
	}
	public List<ComModuleOperation> getModuleOperations() {
		return moduleOperations;
	}
	public void setModuleOperations(List<ComModuleOperation> moduleOperations) {
		this.moduleOperations = moduleOperations;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(int orderCode) {
		this.orderCode = orderCode;
	}
	public int getIsEnabled() {
		return isEnabled;
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
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_PROJECT_MODULE");
		table.setName("[通用的]项目模块信息资源对象表");
		table.setComments("[通用的]项目模块信息资源对象表：理解为菜单");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(12);
		
		CfgColumndata parentIdColumn = new CfgColumndata("parent_id");
		parentIdColumn.setName("父模块编号");
		parentIdColumn.setComments("父模块编号，可为空，用于递归");
		parentIdColumn.setColumnType(DataTypeConstants.STRING);
		parentIdColumn.setLength(32);
		parentIdColumn.setOrderCode(1);
		columns.add(parentIdColumn);
		
		CfgColumndata nameColumn = new CfgColumndata("name");
		nameColumn.setName("模块名称");
		nameColumn.setComments("模块名称");
		nameColumn.setColumnType(DataTypeConstants.STRING);
		nameColumn.setLength(50);
		nameColumn.setOrderCode(2);
		columns.add(nameColumn);
		
		CfgColumndata codeColumn = new CfgColumndata("code");
		codeColumn.setName("模块编码");
		codeColumn.setComments("模块编码:这个编码的命名，要整个项目唯一");
		codeColumn.setColumnType(DataTypeConstants.STRING);
		codeColumn.setLength(100);
		codeColumn.setOrderCode(3);
		columns.add(codeColumn);
		
		CfgColumndata urlColumn = new CfgColumndata("url");
		urlColumn.setName("url");
		urlColumn.setComments("url");
		urlColumn.setColumnType(DataTypeConstants.STRING);
		urlColumn.setLength(50);
		urlColumn.setOrderCode(4);
		columns.add(urlColumn);
		
		CfgColumndata iconColumn = new CfgColumndata("icon");
		iconColumn.setName("模块图标");
		iconColumn.setComments("模块图标");
		iconColumn.setColumnType(DataTypeConstants.STRING);
		iconColumn.setLength(30);
		iconColumn.setOrderCode(5);
		columns.add(iconColumn);
		
		CfgColumndata orderCodeColumn = new CfgColumndata("order_code");
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setColumnType(DataTypeConstants.INTEGER);
		orderCodeColumn.setLength(4);
		orderCodeColumn.setOrderCode(6);
		columns.add(orderCodeColumn);
		
		CfgColumndata isEnabledColumn = new CfgColumndata("is_enabled");
		isEnabledColumn.setName("是否启用");
		isEnabledColumn.setComments("是否启用");
		isEnabledColumn.setColumnType(DataTypeConstants.INTEGER);
		isEnabledColumn.setLength(1);
		isEnabledColumn.setOrderCode(7);
		columns.add(isEnabledColumn);
		
		table.setColumns(columns);
		table.setIsBuiltin(1);
		table.setPlatformType(TableConstants.IS_COMMON_PLATFORM_TYPE);
		table.setIsCreateHbm(1);
		return table;
	}
	public String toDropTable() {
		return "COM_PROJECT_MODULE";
	}

	public String getEntityName() {
		return "ComProjectModule";
	}
	public JSONObject toEntity() {
		JSONObject json = JsonUtil.toJsonObject(this);
		json.put("orderCode", orderCode+"");
		json.put("isEnabled", isEnabled+"");
		if(this.createTime != null){
			json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		}
		return json;
	}
}
