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

/**
 * 职务资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysPosition extends BasicEntity implements IEntity{
	
	/**
	 * 职务名称
	 */
	private String name;
	/**
	 * 职务编码
	 */
	private String code;
	/**
	 * 职务描述
	 */
	private String descs;
	/**
	 * 排序值
	 */
	private Integer orderCode;
	
	// ---------------------------------------------------------------------------
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

	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(11);
		
		CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 60);
		nameColumn.setName("职务名称");
		nameColumn.setComments("职务名称");
		nameColumn.setOrderCode(1);
		columns.add(nameColumn);
		
		CfgColumn codeColumn = new CfgColumn("code", DataTypeConstants.STRING, 32);
		codeColumn.setName("职务编码");
		codeColumn.setComments("职务编码");
		codeColumn.setOrderCode(2);
		columns.add(codeColumn);
		
		CfgColumn descsColumn = new CfgColumn("descs", DataTypeConstants.STRING, 100);
		descsColumn.setName("职务描述");
		descsColumn.setComments("职务描述");
		descsColumn.setOrderCode(3);
		columns.add(descsColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 3);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setOrderCode(4);
		columns.add(orderCodeColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("职务表");
		table.setRemark("职务表");
		
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_POSITION";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysPosition";
	}
}
