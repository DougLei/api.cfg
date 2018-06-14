package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.IPublish;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComPublishInfo;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;

/**
 * 模块功能资源对象
 * <p>理解为模块下，可点击操作的按钮(或超链接)</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComModuleOperation extends AbstractSysResource implements ITable, IEntityPropAnalysis, IPublish{
	
	/**
	 * 所属模块主键
	 */
	private String moduleId;
	/**
	 * 功能名称
	 * <p>例如：添加</p>
	 */
	private String name;
	/**
	 * 功能编码
	 * <p>这个编码的命名，要整个项目唯一，命名规则可以考虑：(模块名+add)等方式，来确保唯一性</p>
	 */
	private String code;
	/**
	 * 操作要连接的url
	 */
	private String linkUrl;
	/**
	 * 功能图标
	 */
	private String icon;
	/**
	 * 功能是否隐藏
	 * <p>例如查看明细这个功能，没有特定的按钮，是在每条数据的第一列可以打开</p>
	 * <p>默认值：0</p>
	 */
	private Integer isHide;
	/**
	 * 排序值
	 */
	private Integer orderCode;
	
	//---------------------------------------------------------------------------
	
	/**
	 * 关联的数据库id
	 * 该字段在发布的时候用到
	 * @see turnToPublish()
	 */
	@JSONField(serialize = false)
	private String refDatabaseId;
	/**
	 * 关联的项目主键
	 * 该字段在发布的时候用到
	 * @see turnToPublish()
	 */
	@JSONField(serialize = false)
	private String refProjectId;
	
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
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
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getIsHide() {
		return isHide;
	}
	public void setIsHide(Integer isHide) {
		this.isHide = isHide;
	}
	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}
	public String getRefDatabaseId() {
		return refDatabaseId;
	}
	public void setRefDatabaseId(String refDatabaseId) {
		this.refDatabaseId = refDatabaseId;
	}
	public String getRefProjectId() {
		return refProjectId;
	}
	public void setRefProjectId(String refProjectId) {
		this.refProjectId = refProjectId;
	}
	
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_MODULE_OPERATION", 0);
		table.setName("模块功能资源对象表");
		table.setComments("模块功能资源对象表：理解为模块下，可点击操作的按钮(或超链接)");
		table.setIsResource(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setReqResourceMethod(GET);
		table.setIsCreated(1);
		table.setBelongPlatformType(COMMON_PLATFORM);
		table.setIsCore(1);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(20);
		
		ComColumndata moduleIdColumn = new ComColumndata("module_id", DataTypeConstants.STRING, 32);
		moduleIdColumn.setName("所属模块主键");
		moduleIdColumn.setComments("所属模块主键");
		moduleIdColumn.setOrderCode(1);
		columns.add(moduleIdColumn);
		
		ComColumndata nameColumn = new ComColumndata("name", DataTypeConstants.STRING, 20);
		nameColumn.setName("功能名称");
		nameColumn.setComments("功能名称：例如：添加");
		nameColumn.setOrderCode(2);
		columns.add(nameColumn);
		
		ComColumndata codeColumn = new ComColumndata("code", DataTypeConstants.STRING, 120);
		codeColumn.setName("功能编码");
		codeColumn.setComments("功能编码：这个编码的命名，要整个项目唯一，命名规则可以考虑：(模块名+add)等方式，来确保唯一性");
		codeColumn.setOrderCode(3);
		columns.add(codeColumn);
		
		ComColumndata linkUrlColumn = new ComColumndata("link_url", DataTypeConstants.STRING, 100);
		linkUrlColumn.setName("操作要连接的url");
		linkUrlColumn.setComments("操作要连接的url");
		linkUrlColumn.setOrderCode(4);
		columns.add(linkUrlColumn);
		
		ComColumndata iconColumn = new ComColumndata("icon", DataTypeConstants.STRING, 50);
		iconColumn.setName("功能图标");
		iconColumn.setComments("功能图标");
		iconColumn.setOrderCode(5);
		columns.add(iconColumn);
		
		ComColumndata isHideColumn = new ComColumndata("is_hide", DataTypeConstants.INTEGER, 1);
		isHideColumn.setName("功能是否隐藏");
		isHideColumn.setComments("功能是否隐藏:例如查看明细这个功能，没有特定的按钮，是在每条数据的第一列可以打开");
		isHideColumn.setDefaultValue("0");
		isHideColumn.setOrderCode(6);
		columns.add(isHideColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", DataTypeConstants.INTEGER, 2);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setOrderCode(7);
		columns.add(orderCodeColumn);
		
		table.setColumns(columns);
		return table;
	}
	public String toDropTable() {
		return "COM_MODULE_OPERATION";
	}
	
	public ComSysResource turnToPublishResource() {
		throw new IllegalArgumentException("该资源目前不支持turnToPublishResource功能");
	}
	
	public Integer getResourceType() {
		return PROJECT_MODULE_OPERATION;
	}
	
	public String getEntityName() {
		return "ComModuleOperation";
	}
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		entityJson.put("isHide", isHide);
		entityJson.put("orderCode", orderCode);
		super.processSysResourceProps(entityJson);
		return entityJson.getEntityJson();
	}
	
	public String validNotNullProps() {
		if(!isValidNotNullProps){
			isValidNotNullProps = true;
			if(StrUtils.isEmpty(moduleId)){
				validNotNullPropsResult = "所属模块主键不能为空！";
				return validNotNullPropsResult;
			}
			if(StrUtils.isEmpty(name)){
				validNotNullPropsResult = "功能名称不能为空！";
				return validNotNullPropsResult;
			}
			if(StrUtils.isEmpty(code)){
				validNotNullPropsResult = "功能编码不能为空！";
				return validNotNullPropsResult;
			}
			if(StrUtils.isEmpty(linkUrl)){
				validNotNullPropsResult = "操作要连接的url不能为空！";
				return validNotNullPropsResult;
			}
		}
		return validNotNullPropsResult;
	}
	
	public String analysisResourceProp() {
		return validNotNullProps();
	}
	
	public ComPublishInfo turnToPublish() {
		ComPublishInfo publish = new ComPublishInfo();
		publish.setPublishDatabaseId(refDatabaseId);
		publish.setPublishResourceId(refProjectId);
		publish.setPublishResourceName(name);
		publish.setResourceType(PROJECT_MODULE_OPERATION);
		super.turnToPublish();
		return publish;
	}
}
