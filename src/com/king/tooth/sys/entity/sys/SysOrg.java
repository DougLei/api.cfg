package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 组织机构表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysOrg extends BasicEntity implements ITable, IEntity{
	
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
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(13);
		
		ComColumndata parentIdColumn = new ComColumndata("parent_id", BuiltinDataType.STRING, 32);
		parentIdColumn.setName("父组织主键");
		parentIdColumn.setComments("父组织主键");
		parentIdColumn.setOrderCode(1);
		columns.add(parentIdColumn);
		
		ComColumndata nameColumn = new ComColumndata("name", BuiltinDataType.STRING, 100);
		nameColumn.setName("组织名称");
		nameColumn.setComments("组织名称");
		nameColumn.setOrderCode(2);
		columns.add(nameColumn);
		
		ComColumndata shortNameColumn = new ComColumndata("short_name", BuiltinDataType.STRING, 50);
		shortNameColumn.setName("组织简称");
		shortNameColumn.setComments("组织简称");
		shortNameColumn.setOrderCode(3);
		columns.add(shortNameColumn);
		
		ComColumndata codeColumn = new ComColumndata("code", BuiltinDataType.STRING, 32);
		codeColumn.setName("组织编码");
		codeColumn.setComments("组织编码");
		codeColumn.setOrderCode(4);
		columns.add(codeColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", BuiltinDataType.INTEGER, 4);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setOrderCode(5);
		columns.add(orderCodeColumn);
		
		ComColumndata typeColumn = new ComColumndata("type", BuiltinDataType.INTEGER, 3);
		typeColumn.setName("组织机构类型");
		typeColumn.setComments("组织机构类型");
		columns.add(typeColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toDropTable());
		table.setName("组织机构表");
		table.setComments("组织机构表");
		
		
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
