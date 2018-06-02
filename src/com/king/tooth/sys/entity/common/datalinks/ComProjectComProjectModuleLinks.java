package com.king.tooth.sys.entity.common.datalinks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;

/**
 * [通用的]项目和模块的关联关系资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComProjectComProjectModuleLinks implements Serializable, ITable {
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_PROJECT_PROJMODULE_LINKS");
		table.setName("[通用的]项目和模块的关联关系资源对象表");
		table.setComments("[通用的]项目和模块的关联关系资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(4);
		
		CfgColumndata leftIdColumn = new CfgColumndata("left_id");
		leftIdColumn.setName("左资源id");
		leftIdColumn.setComments("左资源id(默认即主表、主资源)");
		leftIdColumn.setColumnType(DataTypeConstants.STRING);
		leftIdColumn.setLength(32);
		leftIdColumn.setOrderCode(1);
		columns.add(leftIdColumn);
		
		CfgColumndata rightIdColumn = new CfgColumndata("right_id");
		rightIdColumn.setName("右资源id");
		rightIdColumn.setComments("右资源id(默认即子表、子资源)");
		rightIdColumn.setColumnType(DataTypeConstants.STRING);
		rightIdColumn.setLength(32);
		rightIdColumn.setOrderCode(2);
		columns.add(rightIdColumn);
		
		CfgColumndata orderCodeColumn = new CfgColumndata("order_code");
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("排序");
		orderCodeColumn.setColumnType(DataTypeConstants.INTEGER);
		orderCodeColumn.setLength(4);
		orderCodeColumn.setOrderCode(3);
		columns.add(orderCodeColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_PROJECT_PROJMODULE_LINKS";
	}
}
