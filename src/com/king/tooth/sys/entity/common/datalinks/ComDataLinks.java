package com.king.tooth.sys.entity.common.datalinks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 数据关联关系资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComDataLinks implements Serializable, ITable {
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_DATA_LINKS", 0);
		table.setResourceName("ComDataLinks");
		table.setName("数据关联关系资源对象表");
		table.setComments("数据关联关系资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setReqResourceMethod(ISysResource.NONE);
		table.setIsCreated(1);
		table.setBelongPlatformType(COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(6);
		
		ComColumndata leftIdColumn = new ComColumndata("left_id", DataTypeConstants.STRING, 32);
		leftIdColumn.setName("左资源id");
		leftIdColumn.setComments("左资源id(默认即主表、主资源)");
		leftIdColumn.setOrderCode(1);
		columns.add(leftIdColumn);
		
		ComColumndata rightIdColumn = new ComColumndata("right_id", DataTypeConstants.STRING, 32);
		rightIdColumn.setName("右资源id");
		rightIdColumn.setComments("右资源id(默认即子表、子资源)");
		rightIdColumn.setOrderCode(2);
		columns.add(rightIdColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", DataTypeConstants.INTEGER, 3);
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("排序");
		orderCodeColumn.setDefaultValue("0");
		orderCodeColumn.setOrderCode(3);
		columns.add(orderCodeColumn);
		
		ComColumndata leftResourceNameColumn = new ComColumndata("left_resource_name", DataTypeConstants.STRING, 60);
		leftResourceNameColumn.setName("左资源名");
		leftResourceNameColumn.setComments("左资源名(默认即主表、主资源)");
		leftResourceNameColumn.setOrderCode(4);
		columns.add(leftResourceNameColumn);
		
		ComColumndata rightResourceNameColumn = new ComColumndata("right_resource_name", DataTypeConstants.STRING, 60);
		rightResourceNameColumn.setName("右资源名");
		rightResourceNameColumn.setComments("右资源名(默认即子表、子资源)");
		rightResourceNameColumn.setOrderCode(5);
		columns.add(rightResourceNameColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_DATA_LINKS";
	}
}
