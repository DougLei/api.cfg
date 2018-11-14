package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.util.StrUtils;

/**
 * 角色表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysRole extends BasicEntity implements IEntity{
	
	/**
	 * 角色名称
	 */
	private String name;
	/**
	 * 角色编码
	 */
	private String code;
	/**
	 * 角色描述
	 */
	private String descs;
	/**
	 * 排序值
	 */
	private Integer orderCode;
	/**
	 * 是否启用
	 * <p>默认值：1</p>
	 */
	private Integer isEnabled;

	// ---------------------------------------------------------------------------
	
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
	public String getDescs() {
		return descs;
	}
	public void setDescs(String descs) {
		this.descs = descs;
	}
	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}
	public Integer getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(12);
		
		CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 30);
		nameColumn.setName("角色名称");
		nameColumn.setComments("角色名称");
		nameColumn.setOrderCode(1);
		columns.add(nameColumn);
		
		CfgColumn codeColumn = new CfgColumn("code", DataTypeConstants.STRING, 20);
		codeColumn.setName("角色编码");
		codeColumn.setComments("角色编码");
		codeColumn.setIsNullabled(0);
		codeColumn.setOrderCode(2);
		columns.add(codeColumn);
		
		CfgColumn descsColumn = new CfgColumn("descs", DataTypeConstants.STRING, 100);
		descsColumn.setName("角色描述");
		descsColumn.setComments("角色描述");
		descsColumn.setOrderCode(3);
		columns.add(descsColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 4);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setOrderCode(4);
		columns.add(orderCodeColumn);
		
		CfgColumn isEnabledColumn = new CfgColumn("is_enabled", DataTypeConstants.INTEGER, 1);
		isEnabledColumn.setName("是否启用");
		isEnabledColumn.setComments("是否启用");
		isEnabledColumn.setDefaultValue("1");
		isEnabledColumn.setOrderCode(5);
		columns.add(isEnabledColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("角色表");
		table.setRemark("角色表");
		
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_ROLE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysRole";
	}
}
