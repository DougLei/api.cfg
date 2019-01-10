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
 * 组织机构表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysOrg extends BasicEntity implements IEntity{
	
	/**
	 * 父组织主键
	 */
	private String parentId;
	/**
	 * 组织名称
	 */
	private String name;
	/**
	 * 组织简称
	 */
	private String shortName;
	/**
	 * 组织编码
	 */
	private String code;
	/**
	 * 排序值
	 */
	private Integer orderCode;
	/**
	 * 组织机构类型
	 */
	private Integer type;

	// ---------------------------------------------------------------------------

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
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(13);
		
		CfgColumn parentIdColumn = new CfgColumn("parent_id", DataTypeConstants.STRING, 32);
		parentIdColumn.setName("父组织主键");
		parentIdColumn.setComments("父组织主键");
		parentIdColumn.setOrderCode(1);
		columns.add(parentIdColumn);
		
		CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 100);
		nameColumn.setName("组织名称");
		nameColumn.setComments("组织名称");
		nameColumn.setOrderCode(2);
		columns.add(nameColumn);
		
		CfgColumn shortNameColumn = new CfgColumn("short_name", DataTypeConstants.STRING, 50);
		shortNameColumn.setName("组织简称");
		shortNameColumn.setComments("组织简称");
		shortNameColumn.setOrderCode(3);
		columns.add(shortNameColumn);
		
		CfgColumn codeColumn = new CfgColumn("code", DataTypeConstants.STRING, 32);
		codeColumn.setName("组织编码");
		codeColumn.setComments("组织编码");
		codeColumn.setOrderCode(4);
		columns.add(codeColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 4);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setOrderCode(5);
		columns.add(orderCodeColumn);
		
		CfgColumn typeColumn = new CfgColumn("type", DataTypeConstants.INTEGER, 3);
		typeColumn.setName("组织机构类型");
		typeColumn.setComments("组织机构类型");
		columns.add(typeColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("组织机构表");
		table.setRemark("组织机构表");
		
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_ORG";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysOrg";
	}
}
