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
 * 项目和表的关系
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComProjectComTabledataLinks implements Serializable, ITable {
	
	public ComTabledata toCreateTable(String dbType) {
		ComTabledata table = new ComTabledata(dbType, "COM_PROJECT_TABLE_LINKS", 0);
		table.setResourceName("ComProjectComTabledataLinks");
		table.setVersion(1);
		table.setName("项目和表的关系表");
		table.setComments("项目和表的关系表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setReqResourceMethod(ISysResource.NONE);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(4);
		
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
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_PROJECT_TABLE_LINKS";
	}
}
