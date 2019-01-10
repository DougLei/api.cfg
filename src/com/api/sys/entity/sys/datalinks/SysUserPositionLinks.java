package com.api.sys.entity.sys.datalinks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.api.annotation.Table;
import com.api.constants.DataTypeConstants;
import com.api.sys.entity.ITable;
import com.api.sys.entity.cfg.CfgColumn;
import com.api.sys.entity.cfg.CfgTable;

/**
 * 人员和职务的关联关系表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysUserPositionLinks implements Serializable, ITable {
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(7);
		
		CfgColumn leftIdColumn = new CfgColumn("left_id", DataTypeConstants.STRING, 32);
		leftIdColumn.setName("左资源id");
		leftIdColumn.setComments("左资源id(默认即主表、主资源)");
		columns.add(leftIdColumn);
		
		CfgColumn rightIdColumn = new CfgColumn("right_id", DataTypeConstants.STRING, 32);
		rightIdColumn.setName("右资源id");
		rightIdColumn.setComments("右资源id(默认即子表、子资源)");
		columns.add(rightIdColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 3);
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("排序");
		orderCodeColumn.setDefaultValue("0");
		columns.add(orderCodeColumn);
		
		CfgColumn isMainColumn = new CfgColumn("is_main", DataTypeConstants.INTEGER, 1);
		isMainColumn.setName("是否是主要岗位");
		isMainColumn.setComments("是否是主要岗位：即默认岗位，其他的都属于兼职岗位，默认值是0");
		isMainColumn.setDefaultValue("0");
		columns.add(isMainColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("人员和职务的关联关系表");
		table.setRemark("人员和职务的关联关系表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_USER_POSITION_LINKS";
	}

	public String getTableResourceName() {
		return "SysUserPositionLinks";
	}
}
