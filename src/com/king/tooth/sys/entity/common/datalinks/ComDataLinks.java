package com.king.tooth.sys.entity.common.datalinks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.common.ComColumndata;
import com.king.tooth.sys.entity.common.ComTabledata;

/**
 * 数据关联关系资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComDataLinks implements Serializable, ITable {
	
	public ComTabledata toCreateTable(String dbType) {
		ComTabledata table = new ComTabledata(dbType, "COM_DATA_LINKS", 0);
		table.setResourceName("ComDataLinks");
		table.setName("数据关联关系资源对象表");
		table.setComments("数据关联关系资源对象表");
		table.setIsDatalinkTable(1);
		table.setIsBuiltin(1);
		table.setReqResourceMethod(ISysResource.NONE);
		
		table.setIsNeedDeploy(1);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(6);
		
		ComColumndata leftIdColumn = new ComColumndata("left_id");
		leftIdColumn.setName("左资源id");
		leftIdColumn.setComments("左资源id(默认即主表、主资源)");
		leftIdColumn.setColumnType(DataTypeConstants.STRING);
		leftIdColumn.setLength(32);
		leftIdColumn.setOrderCode(1);
		columns.add(leftIdColumn);
		
		ComColumndata rightIdColumn = new ComColumndata("right_id");
		rightIdColumn.setName("右资源id");
		rightIdColumn.setComments("右资源id(默认即子表、子资源)");
		rightIdColumn.setColumnType(DataTypeConstants.STRING);
		rightIdColumn.setLength(32);
		rightIdColumn.setOrderCode(2);
		columns.add(rightIdColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code");
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("排序");
		orderCodeColumn.setColumnType(DataTypeConstants.INTEGER);
		orderCodeColumn.setLength(4);
		orderCodeColumn.setOrderCode(3);
		columns.add(orderCodeColumn);
		
		ComColumndata leftResourceNameColumn = new ComColumndata("left_resource_name");
		leftResourceNameColumn.setName("左资源名");
		leftResourceNameColumn.setComments("左资源名(默认即主表、主资源)");
		leftResourceNameColumn.setColumnType(DataTypeConstants.STRING);
		leftResourceNameColumn.setLength(50);
		leftResourceNameColumn.setOrderCode(4);
		columns.add(leftResourceNameColumn);
		
		ComColumndata rightResourceNameColumn = new ComColumndata("right_resource_name");
		rightResourceNameColumn.setName("右资源名");
		rightResourceNameColumn.setComments("右资源名(默认即子表、子资源)");
		rightResourceNameColumn.setColumnType(DataTypeConstants.STRING);
		rightResourceNameColumn.setLength(50);
		rightResourceNameColumn.setOrderCode(5);
		columns.add(rightResourceNameColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_DATA_LINKS";
	}
}
