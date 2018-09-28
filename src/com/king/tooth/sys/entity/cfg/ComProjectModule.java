package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.StrUtils;

/**
 * 项目模块信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class ComProjectModule extends BasicEntity implements ITable, IEntityPropAnalysis, IEntity{
	
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
		ComTabledata table = new ComTabledata(toGetTableName());
		table.setName("项目模块信息表");
		table.setComments("项目模块信息表");
		
		table.setColumns(getColumnList());
		return table;
	}
	public String toGetTableName() {
		return "COM_PROJECT_MODULE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "ComProjectModule";
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
}
