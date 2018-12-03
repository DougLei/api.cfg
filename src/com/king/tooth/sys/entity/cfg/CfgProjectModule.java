package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.IPermissionEntity;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 项目模块信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgProjectModule extends BasicEntity implements IEntityPropAnalysis, IEntity, IPermissionEntity{
	
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
	/**
	 * 是否有效
	 * <p>默认值为1</p>
	 */
	private Integer isEnabled;
	
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
	public Integer getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(10+7);
		
		CfgColumn refProjectIdColumn = new CfgColumn("ref_project_id", DataTypeConstants.STRING, 32);
		refProjectIdColumn.setName("关联的项目主键");
		refProjectIdColumn.setComments("关联的项目主键");
		refProjectIdColumn.setIsNullabled(0);
		columns.add(refProjectIdColumn);
		
		CfgColumn parentIdColumn = new CfgColumn("parent_id", DataTypeConstants.STRING, 32);
		parentIdColumn.setName("父模块主键");
		parentIdColumn.setComments("父模块主键，可为空，用于递归");
		columns.add(parentIdColumn);
		
		CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 50);
		nameColumn.setName("模块名称");
		nameColumn.setComments("模块名称");
		columns.add(nameColumn);
		
		CfgColumn codeColumn = new CfgColumn("code", DataTypeConstants.STRING, 100);
		codeColumn.setName("模块编码");
		codeColumn.setComments("模块编码:这个编码的命名，要整个项目唯一");
		codeColumn.setIsNullabled(0);
		columns.add(codeColumn);
		
		CfgColumn urlColumn = new CfgColumn("url", DataTypeConstants.STRING, 60);
		urlColumn.setName("url");
		urlColumn.setComments("url");
		columns.add(urlColumn);
		
		CfgColumn iconColumn = new CfgColumn("icon", DataTypeConstants.STRING, 30);
		iconColumn.setName("模块图标");
		iconColumn.setComments("模块图标");
		columns.add(iconColumn);
		
		CfgColumn bodyColumn = new CfgColumn("body", DataTypeConstants.CLOB, 0);
		bodyColumn.setName("模块的内容");
		bodyColumn.setComments("模块的内容:json串");
		columns.add(bodyColumn);
		
		CfgColumn structTreeColumn = new CfgColumn("struct_tree", DataTypeConstants.CLOB, 0);
		structTreeColumn.setName("功能树");
		structTreeColumn.setComments("功能树:json串");
		columns.add(structTreeColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 4);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setDefaultValue("0");
		columns.add(orderCodeColumn);
		
		CfgColumn isEnabledColumn = new CfgColumn("is_enabled", DataTypeConstants.INTEGER, 1);
		isEnabledColumn.setName("是否有效");
		isEnabledColumn.setComments("默认值为1");
		isEnabledColumn.setDefaultValue("1");
		columns.add(isEnabledColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("项目模块信息表");
		table.setRemark("项目模块信息表");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
		table.setColumns(getColumnList());
		return table;
	}
	public String toDropTable() {
		return "CFG_PROJECT_MODULE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgProjectModule";
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
	
	// ----------------------------------------------------------------
	@JSONField(serialize = false)
	public String getRefResourceId(){
		return id;
	}
	@JSONField(serialize = false)
	public String getRefResourceCode(){
		return code;
	}
	@JSONField(serialize = false)
	public String getRefParentResourceId(){
		return parentId;
	}
	@JSONField(serialize = false)
	public String getRefParentResourceCode(){
		if(StrUtils.notEmpty(parentId)){
			if(parentProjectModule == null){
				parentProjectModule = HibernateUtil.extendExecuteUniqueQueryByHqlArr(CfgProjectModule.class, "from CfgProjectModule where "+ResourcePropNameConstants.ID+"=?", parentId);
			}
			if(parentProjectModule == null){
				throw new NullPointerException("模块["+name+"]不存在id为["+parentId+"]的父模块信息");
			}
			return parentProjectModule.getCode();
		}
		return null;
	}
	@JSONField(serialize = false)
	private CfgProjectModule parentProjectModule;

	/**
	 * 是否修改了权限信息
	 * @param oldProjectModule
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isChangePermissionInfo(IPermissionEntity oldProjectModule){
		if(StrUtils.isEmpty(parentId) && StrUtils.notEmpty(oldProjectModule.getRefParentResourceId())){
			return true;
		}
		if(StrUtils.isEmpty(oldProjectModule.getRefParentResourceId()) && StrUtils.notEmpty(parentId)){
			return true;
		}
		if(parentId != null && oldProjectModule.getRefParentResourceId() != null && !oldProjectModule.getRefParentResourceId().equals(parentId)){
			return true;
		}
		return false;
	}
}
