package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
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
	 * 模块的内容
	 * <p>json串</p>
	 */
	private String body;
	/**
	 * 结构树
	 * <p>json串</p>
	 */
	private String structTree;
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
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getStructTree() {
		return structTree;
	}
	public void setStructTree(String structTree) {
		this.structTree = structTree;
	}
	
	@JSONField(serialize = false)
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(9+7);
		
		ComColumndata refProjectIdColumn = new ComColumndata("ref_project_id", DataTypeConstants.STRING, 32);
		refProjectIdColumn.setName("关联的项目主键");
		refProjectIdColumn.setComments("关联的项目主键");
		refProjectIdColumn.setIsNullabled(0);
		columns.add(refProjectIdColumn);
		
		ComColumndata parentIdColumn = new ComColumndata("parent_id", DataTypeConstants.STRING, 32);
		parentIdColumn.setName("父模块主键");
		parentIdColumn.setComments("父模块主键，可为空，用于递归");
		columns.add(parentIdColumn);
		
		ComColumndata nameColumn = new ComColumndata("name", DataTypeConstants.STRING, 50);
		nameColumn.setName("模块名称");
		nameColumn.setComments("模块名称");
		columns.add(nameColumn);
		
		ComColumndata codeColumn = new ComColumndata("code", DataTypeConstants.STRING, 100);
		codeColumn.setName("模块编码");
		codeColumn.setComments("模块编码:这个编码的命名，要整个项目唯一");
		codeColumn.setIsNullabled(0);
		columns.add(codeColumn);
		
		ComColumndata urlColumn = new ComColumndata("url", DataTypeConstants.STRING, 60);
		urlColumn.setName("url");
		urlColumn.setComments("url");
		columns.add(urlColumn);
		
		ComColumndata iconColumn = new ComColumndata("icon", DataTypeConstants.STRING, 30);
		iconColumn.setName("模块图标");
		iconColumn.setComments("模块图标");
		columns.add(iconColumn);
		
		ComColumndata bodyColumn = new ComColumndata("body", DataTypeConstants.CLOB, 0);
		bodyColumn.setName("模块的内容");
		bodyColumn.setComments("模块的内容:json串");
		columns.add(bodyColumn);
		
		ComColumndata structTreeColumn = new ComColumndata("struct_tree", DataTypeConstants.CLOB, 0);
		structTreeColumn.setName("功能树");
		structTreeColumn.setComments("功能树:json串");
		columns.add(structTreeColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", DataTypeConstants.INTEGER, 4);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setDefaultValue("0");
		columns.add(orderCodeColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toDropTable());
		table.setName("项目模块信息表");
		table.setComments("项目模块信息表");
		
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
