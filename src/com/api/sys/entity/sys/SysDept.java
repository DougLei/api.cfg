package com.api.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.api.annotation.Table;
import com.api.constants.DataTypeConstants;
import com.api.sys.entity.BasicEntity;
import com.api.sys.entity.IEntity;
import com.api.sys.entity.cfg.CfgColumn;
import com.api.sys.entity.cfg.CfgTable;

/**
 * 部门表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysDept extends BasicEntity implements IEntity{
	
	/**
	 * 所属组织主键
	 * <p>顶级部门的这个字段有值，子部门不需要</p>
	 */
	private String orgId;
	/**
	 * 父部门主键
	 */
	private String parentId;
	/**
	 * 部门名称
	 */
	private String name;
	/**
	 * 部门简称
	 */
	private String shortName;
	/**
	 * 部门编码
	 */
	private String code;
	/**
	 * 排序值
	 */
	private Integer orderCode;
	/**
	 * 部门类型
	 * <p>部门类型，例如班组，科室等</p>
	 */
	private Integer type;
	/**
	 * 是否被删除
	 * <p>逻辑删除，默认值为0</p>
	 */
	private Integer isDelete;

	// ---------------------------------------------------------------------------
	

	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getParentId() {
		return parentId;
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
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(15);
		
		CfgColumn orgIdColumn = new CfgColumn("org_id", DataTypeConstants.STRING, 32);
		orgIdColumn.setName("所属组织主键");
		orgIdColumn.setComments("所属组织主键：顶级部门的这个字段有值，子部门不需要");
		columns.add(orgIdColumn);
		
		CfgColumn parentIdColumn = new CfgColumn("parent_id", DataTypeConstants.STRING, 32);
		parentIdColumn.setName("父部门主键");
		parentIdColumn.setComments("父部门主键");
		columns.add(parentIdColumn);
		
		CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 100);
		nameColumn.setName("部门名称");
		nameColumn.setComments("部门名称");
		columns.add(nameColumn);
		
		CfgColumn shortNameColumn = new CfgColumn("short_name", DataTypeConstants.STRING, 50);
		shortNameColumn.setName("部门简称");
		shortNameColumn.setComments("部门简称");
		columns.add(shortNameColumn);
		
		CfgColumn codeColumn = new CfgColumn("code", DataTypeConstants.STRING, 32);
		codeColumn.setName("部门编码");
		codeColumn.setComments("部门编码");
		columns.add(codeColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 4);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		columns.add(orderCodeColumn);
		
		CfgColumn typeColumn = new CfgColumn("type", DataTypeConstants.INTEGER, 3);
		typeColumn.setName("部门类型");
		typeColumn.setComments("部门类型，例如班组，科室等");
		columns.add(typeColumn);
		
		CfgColumn isDeleteColumn = new CfgColumn("is_delete", DataTypeConstants.INTEGER, 1);
		isDeleteColumn.setName("是否被删除");
		isDeleteColumn.setComments("逻辑删除，默认值为0");
		isDeleteColumn.setDefaultValue("0");
		columns.add(isDeleteColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("部门表");
		table.setRemark("部门表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_DEPT";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysDept";
	}
}
