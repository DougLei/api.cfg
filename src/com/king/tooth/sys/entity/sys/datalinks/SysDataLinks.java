package com.king.tooth.sys.entity.sys.datalinks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 数据关联关系信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysDataLinks implements Serializable, ITable {
	
	@JSONField(serialize = false)
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(8);
		
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
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toDropTable());
		table.setResourceName("SysDataLinks");
		table.setName("数据关联关系信息表");
		table.setComments("数据关联关系信息表");
		
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_DATA_LINKS";
	}
}
