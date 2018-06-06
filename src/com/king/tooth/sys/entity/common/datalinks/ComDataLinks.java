package com.king.tooth.sys.entity.common.datalinks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;

/**
 * [通用的]数据关联关系资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComDataLinks implements Serializable, ITable {
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_DATA_LINKS");
		table.setName("[通用的]数据关联关系资源对象表");
		table.setComments("[通用的]数据关联关系资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(6);
		
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
		
		CfgColumndata leftResourceNameColumn = new CfgColumndata("left_resource_name");
		leftResourceNameColumn.setName("左资源名");
		leftResourceNameColumn.setComments("左资源名(默认即主表、主资源)");
		leftResourceNameColumn.setColumnType(DataTypeConstants.STRING);
		leftResourceNameColumn.setLength(50);
		leftResourceNameColumn.setOrderCode(4);
		columns.add(leftResourceNameColumn);
		
		CfgColumndata rightResourceNameColumn = new CfgColumndata("right_resource_name");
		rightResourceNameColumn.setName("右资源名");
		rightResourceNameColumn.setComments("右资源名(默认即子表、子资源)");
		rightResourceNameColumn.setColumnType(DataTypeConstants.STRING);
		rightResourceNameColumn.setLength(50);
		rightResourceNameColumn.setOrderCode(5);
		columns.add(rightResourceNameColumn);
		
		table.setColumns(columns);
		table.setIsBuiltin(1);
		table.setReqResourceMethod(ISysResource.NONE);
		return table;
	}

	public String toDropTable() {
		return "COM_DATA_LINKS";
	}
}
