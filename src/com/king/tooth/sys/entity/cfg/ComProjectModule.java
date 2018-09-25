package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.IPublish;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.dm.DmPublishInfo;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.util.StrUtils;

/**
 * 项目模块信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class ComProjectModule extends AbstractSysResource implements ITable, IEntityPropAnalysis, IPublish{
	
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
	/**
	 * 模块的内容
	 * <p>json串</p>
	 */
	private String body;
	/**
	 * 功能树
	 * <p>json串</p>
	 */
	private String functionTree;
	
	//-----------------------------------------------
	/**
	 * 关联的数据库id
	 * 该字段在发布的时候用到
	 * @see turnToPublish()
	 */
	@JSONField(serialize = false)
	private String refDatabaseId;
	
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
	public void setRefDatabaseId(String refDatabaseId) {
		this.refDatabaseId = refDatabaseId;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getFunctionTree() {
		return functionTree;
	}
	public void setFunctionTree(String functionTree) {
		this.functionTree = functionTree;
	}
	
	@JSONField(serialize = false)
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(23);
		
		ComColumndata refProjectIdColumn = new ComColumndata("ref_project_id", BuiltinDataType.STRING, 32);
		refProjectIdColumn.setName("关联的项目主键");
		refProjectIdColumn.setComments("关联的项目主键");
		refProjectIdColumn.setIsNullabled(0);
		refProjectIdColumn.setOrderCode(1);
		columns.add(refProjectIdColumn);
		
		ComColumndata parentIdColumn = new ComColumndata("parent_id", BuiltinDataType.STRING, 32);
		parentIdColumn.setName("父模块主键");
		parentIdColumn.setComments("父模块主键，可为空，用于递归");
		parentIdColumn.setOrderCode(2);
		columns.add(parentIdColumn);
		
		ComColumndata nameColumn = new ComColumndata("name", BuiltinDataType.STRING, 50);
		nameColumn.setName("模块名称");
		nameColumn.setComments("模块名称");
		nameColumn.setOrderCode(3);
		columns.add(nameColumn);
		
		ComColumndata codeColumn = new ComColumndata("code", BuiltinDataType.STRING, 100);
		codeColumn.setName("模块编码");
		codeColumn.setComments("模块编码:这个编码的命名，要整个项目唯一");
		codeColumn.setIsNullabled(0);
		codeColumn.setOrderCode(4);
		columns.add(codeColumn);
		
		ComColumndata urlColumn = new ComColumndata("url", BuiltinDataType.STRING, 60);
		urlColumn.setName("url");
		urlColumn.setComments("url");
		urlColumn.setOrderCode(5);
		columns.add(urlColumn);
		
		ComColumndata iconColumn = new ComColumndata("icon", BuiltinDataType.STRING, 30);
		iconColumn.setName("模块图标");
		iconColumn.setComments("模块图标");
		iconColumn.setOrderCode(6);
		columns.add(iconColumn);
		
		ComColumndata bodyColumn = new ComColumndata("body", BuiltinDataType.CLOB, 0);
		bodyColumn.setName("模块的内容");
		bodyColumn.setComments("模块的内容:json串");
		bodyColumn.setOrderCode(7);
		columns.add(bodyColumn);
		
		ComColumndata functionTreeColumn = new ComColumndata("function_tree", BuiltinDataType.CLOB, 0);
		functionTreeColumn.setName("功能树");
		functionTreeColumn.setComments("功能树:json串");
		functionTreeColumn.setOrderCode(8);
		columns.add(functionTreeColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", BuiltinDataType.INTEGER, 4);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setDefaultValue("0");
		orderCodeColumn.setOrderCode(9);
		columns.add(orderCodeColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_PROJECT_MODULE", 0);
		table.setName("项目模块信息表");
		table.setComments("项目模块信息表");
		table.setIsResource(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(COMMON_PLATFORM);
		table.setIsCore(1);
		
		table.setColumns(getColumnList());
		return table;
	}
	public String toDropTable() {
		return "COM_PROJECT_MODULE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "ComProjectModule";
	}
	
	public SysResource turnToResource() {
		throw new IllegalArgumentException("该资源目前不支持turnToResource功能");
	}
	
	public SysResource turnToPublishResource(String projectId, String refResourceId) {
		throw new IllegalArgumentException("该资源目前不支持turnToPublishResource功能");
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(code)){
			return "模块编码不能为空";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		return validNotNullProps();
	}
	
	@JSONField(serialize = false)
	public Integer getResourceType() {
		return PROJECT_MODULE;
	}
	
	public DmPublishInfo turnToPublish() {
		DmPublishInfo publish = new DmPublishInfo();
		publish.setPublishDatabaseId(refDatabaseId);
		publish.setPublishProjectId(refProjectId);
		publish.setPublishResourceId(id);
		publish.setPublishResourceName(code);
		publish.setResourceType(PROJECT_MODULE);
		return publish;
	}
}
