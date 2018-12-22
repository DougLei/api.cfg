package com.king.tooth.sys.entity.sys.datalinks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;

/**
 * 人员和部门的关联关系表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysUserDeptLinks implements Serializable, ITable {

	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(7);
		
		CfgColumn leftIdColumn = new CfgColumn("left_id", DataTypeConstants.STRING, 32);
		leftIdColumn.setName("左资源id");
		leftIdColumn.setComments("左资源id(默认即主表、主资源)");
		leftIdColumn.setOrderCode(1);
		columns.add(leftIdColumn);
		
		CfgColumn rightIdColumn = new CfgColumn("right_id", DataTypeConstants.STRING, 32);
		rightIdColumn.setName("右资源id");
		rightIdColumn.setComments("右资源id(默认即子表、子资源)");
		rightIdColumn.setOrderCode(2);
		columns.add(rightIdColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 3);
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("排序");
		orderCodeColumn.setDefaultValue("0");
		orderCodeColumn.setOrderCode(3);
		columns.add(orderCodeColumn);
		
		CfgColumn isMainColumn = new CfgColumn("is_main", DataTypeConstants.INTEGER, 1);
		isMainColumn.setName("是否是主要部门");
		isMainColumn.setComments("是否是主要部门：即默认部门，其他的都属于兼职部门，默认值是0");
		isMainColumn.setDefaultValue("0");
		isMainColumn.setOrderCode(4);
		columns.add(isMainColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("人员和部门的关联关系表表");
		table.setRemark("人员和部门的关联关系表表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_USER_DEPT_LINKS";
	}

	public String getTableResourceName() {
		return "SysUserDeptLinks";
	}
}
