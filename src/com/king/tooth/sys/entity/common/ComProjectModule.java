package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;

/**
 * 项目模块信息资源对象
 * <p>即菜单</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComProjectModule extends AbstractSysResource implements ITable, IEntity, IEntityPropAnalysis{
	
	/**
	 * 关联的项目主键
	 */
	private String refProjectId;
	/**
	 * 父模块主键
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
	private Integer orderCode;
	
	//-----------------------------------------------
	
	public String getParentId() {
		return parentId;
	}
	public String getRefProjectId() {
		return refProjectId;
	}
	public void setRefProjectId(String refProjectId) {
		this.refProjectId = refProjectId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		if(StrUtils.isEmpty(name)){
			name = code;
		}
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
	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}
	
	public ComTabledata toCreateTable(String dbType) {
		ComTabledata table = new ComTabledata(dbType, "COM_PROJECT_MODULE", 0);
		table.setIsResource(1);
		table.setName("项目模块信息资源对象表");
		table.setComments("项目模块信息资源对象表：即菜单");
		table.setVersion(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(18);
		
		ComColumndata refProjectIdColumn = new ComColumndata("ref_project_id", DataTypeConstants.STRING, 32);
		refProjectIdColumn.setName("关联的项目主键");
		refProjectIdColumn.setComments("关联的项目主键");
		refProjectIdColumn.setIsNullabled(0);
		refProjectIdColumn.setOrderCode(1);
		columns.add(refProjectIdColumn);
		
		ComColumndata parentIdColumn = new ComColumndata("parent_id", DataTypeConstants.STRING, 32);
		parentIdColumn.setName("父模块主键");
		parentIdColumn.setComments("父模块主键，可为空，用于递归");
		parentIdColumn.setOrderCode(2);
		columns.add(parentIdColumn);
		
		ComColumndata nameColumn = new ComColumndata("name", DataTypeConstants.STRING, 50);
		nameColumn.setName("模块名称");
		nameColumn.setComments("模块名称");
		nameColumn.setOrderCode(3);
		columns.add(nameColumn);
		
		ComColumndata codeColumn = new ComColumndata("code", DataTypeConstants.STRING, 100);
		codeColumn.setName("模块编码");
		codeColumn.setComments("模块编码:这个编码的命名，要整个项目唯一");
		codeColumn.setIsNullabled(0);
		codeColumn.setOrderCode(4);
		columns.add(codeColumn);
		
		ComColumndata urlColumn = new ComColumndata("url", DataTypeConstants.STRING, 60);
		urlColumn.setName("url");
		urlColumn.setComments("url");
		urlColumn.setOrderCode(5);
		columns.add(urlColumn);
		
		ComColumndata iconColumn = new ComColumndata("icon", DataTypeConstants.STRING, 30);
		iconColumn.setName("模块图标");
		iconColumn.setComments("模块图标");
		iconColumn.setOrderCode(6);
		columns.add(iconColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", DataTypeConstants.INTEGER, 4);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setDefaultValue("0");
		orderCodeColumn.setOrderCode(7);
		columns.add(orderCodeColumn);
		
		table.setColumns(columns);
		return table;
	}
	public String toDropTable() {
		return "COM_PROJECT_MODULE";
	}

	public String getEntityName() {
		return "ComProjectModule";
	}
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		entityJson.put(ResourceNameConstants.ID, id);
		entityJson.put("orderCode", orderCode);
		entityJson.put("isEnabled", isEnabled);
		entityJson.put("isBuiltin", isBuiltin);
		entityJson.put("isNeedDeploy", isNeedDeploy);
		entityJson.put("isCreated", isCreated);
		entityJson.put("belongPlatformType", belongPlatformType);
		entityJson.put(ResourceNameConstants.CREATE_TIME, createTime);
		return entityJson.getEntityJson();
	}
	
	public ComSysResource turnToResource() {
		throw new IllegalArgumentException("该资源目前不支持turnToResource功能");
	}
	
	public String validNotNullProps() {
		if(!isValidNotNullProps){
			if(StrUtils.isEmpty(refProjectId)){
				validNotNullPropsResult = "模块关联的项目id不能为空";
			}
			if(StrUtils.isEmpty(code)){
				validNotNullPropsResult = "模块编码不能为空";
			}
			isValidNotNullProps = true;
		}
		return validNotNullPropsResult;
	}
	
	public String analysisResourceProp() {
		return validNotNullProps();
	}
	
	public Integer getResourceType() {
		return PROJECT_MODULE;
	}
	
	public ComPublishInfo turnToPublish() {
		ComPublishInfo publish = new ComPublishInfo();
		publish.setPublishResourceId(id);
		publish.setPublishResourceName(code);
		publish.setResourceType(PROJECT_MODULE);
		this.isBuiltin = 0;
		this.isNeedDeploy = 0;
		this.belongPlatformType = APP_PLATFORM;
		return publish;
	}
}
